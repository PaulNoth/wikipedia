#encoding: utf-8
require 'builder'

class Parser
  def parse_dictionary langs

    dictionary = nil;

    for i in (0 .. langs.length - 1) do

      current_lang = langs[i]
      STDOUT.puts "Language: "+current_lang

      link_langs = langs.reject {|lang| langs.index(lang) == i}

      langlinks = parse_langlinks("data\\"+current_lang+"wiki-latest-langlinks.sql", link_langs)

      subdictionary = Hash.new{|h,k| h[k] = {}}
      word_counts = Array.new 3, 0

      langlinks.each do |langlink|
        subdictionary[langlink[0]][langlink[1]] = normalize_word langlink[2]
      end

      langlinks = nil
      pages = parse_pages_for_langlinks "data\\"+current_lang+"wiki-latest-page.sql", current_lang, subdictionary

      pages.each do |page|
        if subdictionary.has_key?(page[0])
        then subdictionary[page[0]][current_lang] = normalize_word page[1]
        end
      end

      subdictionary.each do |key, word|
        if word.has_key?(link_langs[0]) && !word.has_key?(link_langs[1])
          word_counts[0] += 1
        end
        if !word.has_key?(link_langs[0]) && word.has_key?(link_langs[1])
          word_counts[1] += 1
        end
        if word.has_key?(link_langs[0]) && word.has_key?(link_langs[1])
          word_counts[2] += 1
        end
      end

      STDOUT.puts "Links: "+link_langs[0]+": "+word_counts[0].to_s
      STDOUT.puts "Links: "+link_langs[1]+": "+word_counts[1].to_s
      STDOUT.puts "Links: "+link_langs[0]+" "+link_langs[1]+": "+word_counts[2].to_s

      dictionary = subdictionary if i == 1
    end

    #xml = Builder::XmlMarkup.new( :indent => 2 )
    #xml.dictionary do
    #  subdictionaries[0].each do |word|
    #    xml.word do
    #      word[1].each do |version|
    #        xml.version do
    #          xml.language version[0]
    #          xml.value version[1].force_encoding("utf-8")
    #        end
    #      end
    #    end
    #  end
    #end

    #xml_data = xml.target!
    #file = File.new("data\\dictionary.xml", "w:UTF-8")
    #file.write(xml_data)
    #file.close

    File.open("data\\dictionary.xml", "w:UTF-8") do |file|
      file.puts "<dictionary>"
      dictionary.each do |word|
        file.puts "  <word>"
        word[1].each do |version|
          file.puts "    <version>"
          file.puts "      <language>"+version[0]+"</language>"
          file.puts "      <value>"+version[1].gsub('&', '&amp;').force_encoding("utf-8")+"</value>"
          file.puts "    </version>"
        end
        file.puts "  </word>"
      end
      file.puts "</dictionary>"
    end


  end

  def parse_langlinks path, langs
    matches = []
    IO.foreach(path) do |line|
      line.gsub!('_', ' ')
      line.scan(/\(([[:digit:]]+),'(#{langs.join('|')})','(([^']*|\\')+)'\)/).each { |match| matches << match }
    end
    matches
  end

  def parse_pages_for_langlinks path, current_language, langlink_dictionary
    matches = []
    unconnected_counter = 0;
    IO.foreach(path) do |line|
      line.gsub!('_', ' ')
      line.scan(/\(([[:digit:]]+),[[:digit:]]+,'([^']*)','[^']*',[[:digit:]]+,[[:digit:]]+,[[:digit:]]+,[[:digit:].]+,'[[:digit:]]+','[[:digit:]]+',[[:digit:].]+,[[:digit:].]+(,[[:digit:].]+){0,1},[^\)]+\)/).each do |match|
        if langlink_dictionary.has_key?(match[0])
        then langlink_dictionary[match[0]][current_language] = normalize_word match[1]
        end
        if !langlink_dictionary.has_key?(match[0])
        then unconnected_counter += 1
        end
      end
    end
    STDOUT.puts "Unlinked: "+unconnected_counter.to_s
    matches
  end

  def normalize_word raw
    raw.delete('\\').gsub(/.*:/, "")
  end
end