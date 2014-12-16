# Retriever

Simple library for parsing Wikipedia and DBpedia abstracts.

Source code for the library is at `lib/retriever`, one example script is included at `example/retriever/retriever.rb`

## Structure
- parsers - parsers for different data sources
- persistors - classes to persist parsed data; recently there is Elasticsearch persistor implemented
- processors - data processors; responsible for the load, parse, store chain

Library uses [Elasticsearch](elasticsearch.org) for storing and searching the data and [Nokogiri](http://www.nokogiri.org/) for parsing the XML and TTL files.

## Usage
In our example we used the library to load and process wikipedia and dbpedia data dumps. We parse abstracts from both, load them into Elasticsearch and then perform query search on abstracts. After retrieving the relevant results we compare the similarity of abstracts from Wikipedia vs. DBpedia (for top 3 results)

To run the example script: `ruby -Ilib example/retriever/retriever.rb [wikipedia_file_path] [dbpedia_file_path] [term]`

### Parameters
- `wikipedia_file_path` - path to desired Wikipedia articles file
- `dbpedia_file_path` - path to desired dbpedia abstracts file
- `term` - term to be searched for in abstracts' titles

