#encoding: utf-8
require 'open-uri'
require 'cgi'
require 'fileutils.rb'

#author: Róbert Sabol

# article_categories_sk.ttl
def article_categories_sk
  hash = Hash.new([])

  file_path = "../data/sample_input_article_categories_sk_nemecko.ttl"

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


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------

# article_templates_sk.ttl
def article_templates_sk
  hash = Hash.new([])

    file_path = "../data/sample_input_article_templates_sk_nemecko.ttl"

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


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------

# external_links_sk.ttl
def external_links_sk
  hash = Hash.new([])

    file_path = "../data/sample_input_external_links_sk_nemecko.ttl"

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    external_link = splited_line[3].chop.chop.chop.chop
    #puts external_link

    hash[resource_string] += [external_link]


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------


# freebase_links_sk.ttl
def freebase_links_sk
  hash = Hash.new([])

    file_path = "../data/sample_input_freebase_links_sk_nemecko.ttl"

  my_file = File.open(file_path, "r:UTF-8")


  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string
    freebase_link = splited_line[3].chop.chop.chop.chop
    #puts freebase_link

    hash[resource_string] += [freebase_link]


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------


# long_abstracts_sk.ttl
def long_abstracts_sk
  hash = Hash.new([])

    file_path = "../data/sample_input_long_abstracts_sk_nemecko.ttl"

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


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------

#interlanguage_links_sk.ttl
def interlanguage_links_sk
  hash = Hash.new([])

    file_path = "../data/sample_input_interlanguage_links_sk_nemecko.ttl"

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


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------

#wikipedia_links_sk.ttl
def wikipedia_links_sk
  hash = Hash.new([])

    file_path = "../data/sample_input_wikipedia_links_sk_nemecko.ttl"

  my_file = File.open(file_path, "r:UTF-8")

  while (line = my_file.gets)
    next if (line.start_with?("# started") || line.start_with?("# completed"))
    splited_line = line.split("<")
    resource_string = splited_line[1].chop.chop
    #puts resource_string

    wiki_link = splited_line[3]
    wiki_link = wiki_link[0..-4]

    if (resource_string.include? "http://sk.dbpedia")
      hash[resource_string] += [wiki_link]
    end


  end
  #puts hash
  my_file.close

hash
end
#------------------------------------------------------------------------------------


def is_article_categories_ok
  fixture = 'unit_test/sample_output_article_categories_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = article_categories_sk

  if output.to_s == sample_output
    puts "article_categories_sk: OK"
  else
    puts "article_categories_sk: WRONG"
    puts output
  end
end

def is_article_templates_sk_ok
  fixture = 'unit_test/sample_output_article_templates_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = article_templates_sk

  if output.to_s == sample_output
    puts "article_templates_sk: OK"
  else
    puts "article_templates_sk: WRONG"
    puts output
  end
end

def is_external_links_sk_ok
  fixture = 'unit_test/sample_output_external_links_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = external_links_sk

  if output.to_s == sample_output
    puts "external_links_sk: OK"
  else
    puts "external_links_sk: WRONG"
    puts output
  end
end

def is_freebase_links_sk_ok
  fixture = 'unit_test/sample_output_freebase_links_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = freebase_links_sk

  if output.to_s == sample_output
    puts "freebase_links_sk: OK"
  else
    puts "freebase_links_sk: WRONG"
    puts output
  end
end

def is_long_abstracts_sk_ok
  fixture = 'unit_test/sample_output_long_abstracts_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = long_abstracts_sk

  if output.to_s == sample_output
    puts "long_abstracts_sk: OK"
  else
    puts "long_abstracts_sk: WRONG"
    puts output
  end
end

def is_interlanguage_links_sk_ok
  fixture = 'unit_test/sample_output_interlanguage_links_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = interlanguage_links_sk

  if output.to_s == sample_output
    puts "interlanguage_links_sk: OK"
  else
    puts "interlanguage_links_sk: WRONG"
    puts output
  end
end

def is_wikipedia_links_sk_ok
  fixture = 'unit_test/sample_output_wikipedia_links_sk_nemecko.txt'
  sample_output = File.read(fixture)
  output = wikipedia_links_sk

  if output.to_s == sample_output
    puts "wikipedia_links_sk: OK"
  else
    puts "wikipedia_links_sk: WRONG"
    puts output
  end
end

def run_unit_test
is_article_categories_ok
is_article_templates_sk_ok
is_external_links_sk_ok
is_freebase_links_sk_ok
is_interlanguage_links_sk_ok
is_long_abstracts_sk_ok
is_wikipedia_links_sk_ok
end

run_unit_test