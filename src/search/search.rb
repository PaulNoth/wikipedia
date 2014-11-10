# encoding: utf-8

# Based on: http://ikaisays.com/2010/04/25/jruby-in-memory-search-example-with-lucene-3-0-1/

require 'java'
require 'nokogiri'

# Run program: jruby -r lucene-core-3.0.1.jar search.rb ..\..\data\sample_dictionary.xml

java_import org.apache.lucene.analysis.standard.StandardAnalyzer
java_import org.apache.lucene.document.Document
java_import org.apache.lucene.document.Field
java_import org.apache.lucene.index.IndexWriter
java_import org.apache.lucene.queryParser.ParseException
java_import org.apache.lucene.queryParser.QueryParser
java_import org.apache.lucene.store.RAMDirectory
java_import org.apache.lucene.util.Version

java_import org.apache.lucene.search.IndexSearcher
java_import org.apache.lucene.search.TopScoreDocCollector

class Search

  def create_document sk, en, fr
    doc = Document.new
    doc.add Field.new 'sk', sk, Field::Store::YES, Field::Index::ANALYZED
    doc.add Field.new 'en', en, Field::Store::YES, Field::Index::ANALYZED
    doc.add Field.new 'fr', fr, Field::Store::YES, Field::Index::ANALYZED
    doc
  end

  def create_index path
    idx = RAMDirectory.new
    writer = IndexWriter.new(idx, StandardAnalyzer.new(Version::LUCENE_30), IndexWriter::MaxFieldLength::LIMITED)

    puts 'Initializing index...'

    File.open path, "r:utf-8" do |f|
      f.gets
      loop do
        break if f.gets.chomp == "</dictionary>"
        sk, en, fr = ''
        loop do
          line = f.gets.chomp
          break if line != "    <version>"
          lang = f.gets.match(/<language>([[:alpha:]]{2})<\/language>/).captures[0]
          if lang != nil
            case lang
              when 'sk'
                sk = f.gets.match(/<value>(.*)<\/value>/).captures[0]
              when 'en'
                en = f.gets.match(/<value>(.*)<\/value>/).captures[0]
              when 'fr'
                fr = f.gets.match(/<value>(.*)<\/value>/).captures[0]
            end
          end
          f.gets
        end
        sk = '' if sk == nil
        en = '' if en == nil
        fr = '' if fr == nil
        writer.add_document  create_document(sk, en, fr)
      end
    end

    #doc = Nokogiri::XML(File.open path, "r:utf-8")
    #doc.css('word').each do |word|
    #  sk = word.xpath('version[language = \'sk\']/value').text.encode('utf-8')
    #  en = word.xpath('version[language = \'en\']/value').text.encode('utf-8')
    #  fr = word.xpath('version[language = \'fr\']/value').text.encode('utf-8')
    #  writer.add_document  create_document(sk, en, fr)
    #end

    writer.optimize
    writer.close
    puts 'Index successfully initialized'
    idx
  end

  def search searcher, query_string, language_code

    parser = QueryParser.new(Version::LUCENE_30, "#{language_code}", StandardAnalyzer.new(Version::LUCENE_30))
    query = parser.parse(query_string)

    hits_per_page = 10

    collector = TopScoreDocCollector.create(5 * hits_per_page, false)
    searcher.search(query, collector)

    # Notice how this differs from the Java version: JRuby automagically translates
    # underscore_case_methods into CamelCaseMethods, but scoreDocs is not a method:
    # it's a field. That's why we have to use CamelCase here, otherwise JRuby would
    # complain that score_docs is an undefined method.
    hits = collector.top_docs.scoreDocs

    hit_count = collector.get_total_hits

    if hit_count.zero?
      puts "No matching documents."
    else
      puts "%d total matching documents" % hit_count

      puts "Hits for %s were found in quotes by:" % query_string

      hits.each_with_index do |score_doc, i|
        doc = searcher.doc(score_doc.doc)
        puts "sk: #{doc.get("sk")}, en: #{doc.get("en")}, fr: #{doc.get("fr")} - score: #{score_doc.score}"
        puts

      end

    end

  end

  def main
    index = create_index ARGV[0]
    searcher = IndexSearcher.new index

    loop do
      puts 'Write the language code for the language you wish to translate from!'
      puts 'slovak  -> sk'
      puts 'english -> en'
      puts 'french  -> fr'
      puts 'Press Enter to exit'
      code = STDIN.gets.chomp
      case code
        when 'sk', 'en', 'fr'
          puts 'Insert the term you want translated:'
          term = STDIN.gets.chomp
          search searcher, term, code
        when ''
          break
        else
          puts 'ERROR: Illegal input!'
      end
    end

    searcher.close
  end

  Search.new.main
end