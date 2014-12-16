require 'retriever'
require 'stopwatch'
require 'text'

wikipedia_file = ARGV[0] || '../data/sample_dewiki-latest-pages-articles.xml'
dbpedia_file   = ARGV[1] || '../data/sample_long_abstracts_de.ttl'
query          = ARGV[2] || 'smithee'

persistor = Retriever::Persistors::ElasticsearchPersistor.new

persistor.create_index :wikipedia
persistor.create_index :dbpedia

watch = Stopwatch.new

# parse given wikipedia file and load data to elasticsearch
wikipedia_parser    = Retriever::Parsers::WikipediaParser.new(:page)
sanitizer           = Retriever::Processors::WikipediaSanitizer.new
wikipedia_processor = Retriever::Processors::BasicProcessor.new(:wikipedia, sanitizer, wikipedia_parser, persistor)

watch.start 'Parsing data from wikipedia file and loading them to elasticsearch...'

wikipedia_processor.process_file(wikipedia_file)

watch.stop

# parse given dbpedia file and load data to elasticsearch
dbpedia_parser    = Retriever::Parsers::DbpediaParser.new(:abstract)
sanitizer         = Retriever::Processors::DbpediaSanitizer.new
dbpedia_processor = Retriever::Processors::BasicProcessor.new(:dbpedia, sanitizer, dbpedia_parser, persistor)

watch.start 'Parsing data from dbpedia file and loading them to elasticsearch...'

dbpedia_processor.process_file(dbpedia_file)

watch.stop

# search for query and compare similarity of 10 first wikipedia vs. dbpedia results
wikipedia = persistor.search('wikipedia', query)["hits"]["hits"] # `curl -XGET "localhost:9200/wikipedia/_search?q=#{query}"`
dbpedia   = persistor.search('dbpedia', query)["hits"]["hits"]

white      = Text::WhiteSimilarity.new

puts '-------------------------------------------'
puts "Query: #{query}"
puts '-------------------------------------------'
puts 'Results:'
3.times do |i|
  next if wikipedia[i] == nil || dbpedia[i] == nil

  similarity = white.similarity(wikipedia[i]["_source"]["text"], dbpedia[i]["_source"]["text"])

  puts '###########################################'
  puts "Wikipedia abstract (#{ wikipedia[i]["_source"]["title"] })"
  puts '-------------------------------------------'
  puts wikipedia[i]["_source"]["text"]

  puts '-------------------------------------------'
  puts "DBpedia abstract (#{ dbpedia[i]["_source"]["title"] })"
  puts '-------------------------------------------'
  puts dbpedia[i]["_source"]["text"]

  puts '-------------------------------------------'
  puts "Similarity: #{ similarity }"
  puts '-------------------------------------------'
  puts '###########################################'
end
#
# # drop the indices afterwards
#
persistor.drop_index :wikipedia
persistor.drop_index :dbpedia
