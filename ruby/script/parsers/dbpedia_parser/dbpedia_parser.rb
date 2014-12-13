#encoding: utf-8
require 'open-uri'
require 'cgi'
require 'fileutils.rb'

#author: Róbert Sabol (code inspired by Ondrej Kassak "Infromation Retrieval project" : http://vi.ikt.ui.sav.sk/User:Ondrej.Kassak?view=home)

def sample_input
  true
end

# article_categories_sk.ttl
def article_categories_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_article_categories_sk.ttl"
  else
    file_path = "full_input/article_categories_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string

    category = splited_line[3]
    category = category[/(Kategória:)(.*?>)/,2]
    category = category[0..-2]
    #puts category

    hash[resource_string] += [category]

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "categories time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  hash
end
#------------------------------------------------------------------------------------

# article_templates_sk.ttl
def article_templates_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_article_templates_sk.ttl"
  else
    file_path = "full_input/article_templates_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    my_template = splited_line[3]
    my_template = my_template[/(Šablóna:)(.*)/,2]
    my_template = my_template[0..-4]
    #puts my_template

    hash[resource_string] += [my_template]

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "templates time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  hash
end
#------------------------------------------------------------------------------------

# external_links_sk.ttl
def external_links_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_external_links_sk.ttl"
  else
    file_path = "full_input/external_links_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    external_link = splited_line[3].chop.chop.chop.chop
    #puts external_link

    hash[resource_string] += [external_link]

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "external links time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  hash
end
#------------------------------------------------------------------------------------


# freebase_links_sk.ttl
def freebase_links_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_freebase_links_sk.ttl"
  else
    file_path = "full_input/freebase_links_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")


  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    freebase_link = splited_line[3].chop.chop.chop.chop
    #puts freebase_link

    hash[resource_string] += [freebase_link]

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "freebase links time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  hash
end
#------------------------------------------------------------------------------------


# long_abstracts_sk.ttl
def long_abstracts_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_long_abstracts_sk.ttl"
  else
    file_path = "full_input/long_abstracts_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    long_abstract = line[/(<.*abstract>*)(.*")/,2]
    long_abstract = long_abstract[2..-2]
    #puts freebase_link

    hash[resource_string] += [long_abstract]

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "long abstracts time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  hash
end
#------------------------------------------------------------------------------------

#interlanguage_links_sk.ttl
def interlanguage_links_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_interlanguage_links_sk.ttl"
  else
    file_path = "full_input/interlanguage_links_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    interlanguage_link = splited_line[3]
    interlanguage_link = interlanguage_link[0..-5]

    if (interlanguage_link.include? "http://dbpedia") || (interlanguage_link.include? "http://wikidata")
      hash[resource_string] += [interlanguage_link]
    end

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "interlangugage links time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  hash
end
#------------------------------------------------------------------------------------

#wikipedia_links_sk.ttl
def wikipedia_links_sk
  t1 = Time.now
  hash = Hash.new([])

  if sample_input
    file_path = "../data/sample_input_wikipedia_links_sk.ttl"
  else
    file_path = "full_input/wikipedia_links_sk.ttl"
  end

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string

    wiki_link = splited_line[3]
    wiki_link = wiki_link[0..-5]

    if (resource_string.include? "http://sk.dbpedia")
      hash[resource_string] += [wiki_link]
    end

    t3 = Time.now
    temp_delta = t3 - t1
    if (temp_delta.round % 60 == 0)
      puts "wikipedia links time: #{temp_delta}"
    end
  end
  #puts hash
  my_file.close

  file = File.open("wiki.txt","w:Utf-8")
  file.write(hash)
  file.close
  hash
end
#------------------------------------------------------------------------------------


# validation of strings in format \uXXXX
def validate(str)
  re = /\\u[0-9a-fA-F]{4}/
  str.gsub(re) {|match| Array(match[2..5].to_i(16)).pack('U')}
end
#------------------------------------------------------------------------------------

# parsing my parsed data
def parse_parsed_documents

  my_documents = [article_categories_sk, article_templates_sk, external_links_sk, freebase_links_sk, long_abstracts_sk, interlanguage_links_sk]

  my_parsed_categories = Hash.new([])

  Hash[article_categories_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["categories_sk"] += my_value
    end
    if !my_values.nil?
      my_parsed_categories["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end


  my_parsed_templates = Hash.new([])

  Hash[article_templates_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["templates_sk"] += my_value
    end
    if !my_values.nil?
      my_parsed_templates["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end

  my_parsed_ext_links = Hash.new([])

  Hash[external_links_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["external_links"] += my_value
    end
    if !my_values.nil?
      my_parsed_ext_links["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end

  my_parsed_freebase_links = Hash.new([])

  Hash[freebase_links_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["freebase_links"] += my_value
    end
    if !my_values.nil?
      my_parsed_freebase_links["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end

  my_parsed_abstracts = Hash.new([])

  Hash[long_abstracts_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["long_abstracts_sk"] += my_value
    end
    if !my_values.nil?
      my_parsed_abstracts["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end


  my_parsed_interlanguage_links = Hash.new([])

  Hash[interlanguage_links_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["interlanguage_links"] += my_value
    end
    if !my_values.nil?
      my_parsed_interlanguage_links["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end


  my_parsed_wiki_links = Hash.new([])

  Hash[wikipedia_links_sk.sort].each do |my_key, my_value|
    if !my_value.nil?
      my_values = Hash.new([])
      my_values["wiki_links"] += my_value
    end
    if !my_values.nil?
      my_parsed_wiki_links["#{validate(my_key).encode("utf-8")}"] = my_values
    end
  end


  my_parsed_data = [my_parsed_categories, my_parsed_templates, my_parsed_ext_links, my_parsed_freebase_links, my_parsed_abstracts, my_parsed_interlanguage_links, my_parsed_wiki_links]

  my_keys = Array.new
  my_parsed_documents = Hash.new([])

  my_parsed_data.each do |my_file|
    Hash[my_file.sort].each do |my_key, my_value|
      if !my_value.nil?
        my_value.each do |val|
          my_parsed_documents["#{validate(my_key).encode("utf-8")}"] += val
          if ! my_keys.include?(my_key)
            my_keys.insert(my_keys.count, my_key)
          end
        end

      end
    end
  end
  file = File.open("dodokument.txt","a:Utf-8")

  file.write(my_parsed_documents)
  my_parsed_documents
end
#------------------------------------------------------------------------------------

def run
  t1 = Time.now

  my_documents = parse_parsed_documents

  reserved_strings = ["categories_sk", "templates_sk", "external_links", "freebase_links", "long_abstracts_sk", "interlanguage_links", "wiki_links"]

  if sample_input
    file_name = "sample_parsed_data2.txt"
  else
    file_name = "final_parsed_data.txt"
  end

  file = File.open(file_name,"a:Utf-8")

  #my_documents.each do |my_file|
      Hash[my_documents.sort].each do |k, v|
        if !v.nil?
          file.write("#{validate(k).encode("utf-8")}\t")
          v.each do |val|
            if (reserved_strings.include?(val))
              file.write("#{val}:")
            else
              file.write("#{val};")
            end
            #puts "#{validate(k).encode("utf-8")}\t#{CGI::unescape(val).encode("utf-8")}\n".encode("utf-8")
            #file.write("#{validate(k).encode("utf-8")}\t#{CGI::unescape(val).encode("utf-8")}\n".encode("utf-8"))
            #puts k
          end
          file.write("\n")
        end
      end
  #end

  file.close

  t2 = Time.now
  delta = t2 - t1
  puts "total time: #{delta}"
end

run
