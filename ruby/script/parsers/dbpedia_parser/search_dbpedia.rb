require 'java'

# author:	  Róbert Sabol
# implementation ispired by http://ikaisays.com/2010/04/25/jruby-in-memory-search-example-with-lucene-3-0-1/
# and Ondrej Kassak "Infromation Retrieval project" : http://vi.ikt.ui.sav.sk/User:Ondrej.Kassak?view=home)

# RUN:        jruby -r lucene-core-3.0.1.jar search_dbpedia.rb
#OR:          jruby -J-Xmx1024m -r lucene-core-3.0.1.jar search_dbpedia.rb
# REQUIRES:   lucene-core-3.0.1.jar & parsed_data.txt in same folder as search_dbpedia.rb
 
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

def sample_input
  false
end

def create_document(resource, parsedData)
  doc = Document.new
  doc.add Field.new("resource", resource, Field::Store::YES, Field::Index::ANALYZED)
  doc.add Field.new("parsedData", parsedData, Field::Store::YES, Field::Index::ANALYZED)
  doc
end
 
def create_index
  idx     = RAMDirectory.new
  writer  = IndexWriter.new(idx, StandardAnalyzer.new(Version::LUCENE_30), IndexWriter::MaxFieldLength::LIMITED)

  if sample_input
    file_name = "sample_parsed_data.txt"
  else
    file_name = "final_parsed_data.txt"
  end

  file = File.open(file_name,"r")
  while (line = file.gets)    
    line = line.strip.split("\t")
    writer.add_document(create_document(line[0], line[1]))
  end

  writer.optimize
  writer.close
  idx
end

def search(searcher, query_string)

  parser = QueryParser.new(Version::LUCENE_30, "resource", StandardAnalyzer.new(Version::LUCENE_30))
  query = parser.parse(query_string)
  hits_per_page = 10
  collector = TopScoreDocCollector.create(1 * hits_per_page, false)
  searcher.search(query, collector)

  hits = collector.top_docs.scoreDocs
  hit_count = collector.get_total_hits

  first_version = TRUE

  if hit_count.zero?
    puts "No search results."
  else
    puts "%d total results" % hit_count
     
    puts "search word %s was found:" % query_string

    if first_version
      hits.each_with_index do |score_doc, i|
        doc = searcher.doc(score_doc.doc)
        parsed_data = doc.get("parsedData")
        parsed_data = parsed_data.split(";")
        parsed_data_string = String.new
        parsed_data.each  do |value|
          parsed_data_string += "\t#{value}"
          parsed_data_string += "\n"
        end
        puts "\t#{i+1}."
        puts "\tScore: #{score_doc.score}\n"
        puts "\t#{doc.get("resource")} --> \n\n"
        puts "#{parsed_data_string} --> \n\n"
        #puts "\t#{doc.get("parsedData")}\n\n"
        puts "--------------------------------------------------------------------"

      end

      #maybe later in version 2
    else
      my_test_output = Array.new
      my_test_hash = Hash.new
      hits.each_with_index do |score_doc, i|
        doc = searcher.doc(score_doc.doc)
        my_string = doc.get("resource")
        if ! my_test_output.include?(my_string)
          my_test_output.insert(my_test_output.count, my_string)
        end
        my_test_hash[my_string] = doc.get("parsedData")
        puts my_test_hash
        #puts "\t#{i+1}. #{doc.get("resource")} - #{doc.get("parsedData")}"

      end
    end
  end
 
end
 
def run_search_dbpedia
  index = create_index
  searcher = IndexSearcher.new(index)

  print("\n Type word to start search \n or \n type 'exit' to exit program. \n")
  puts " Have a fun :-)"
  puts
  print("Please type word to search:  ")
  loop do
    search_word = gets.chomp.downcase
    case search_word
    when "exit"
      exit
      else
        if search_word.length == 0
          puts "You dont type any word!!!"
        else
          search(searcher, search_word)
        end
        print("Please type word to search:  ")
    end    
  end
     
  searcher.close
end
 
run_search_dbpedia