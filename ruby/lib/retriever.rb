require 'rubygems'
require 'nokogiri'
require 'elasticsearch'

require 'active_support/core_ext'

require 'retriever/base'
require 'retriever/parsers/dbpedia_parser'
require 'retriever/parsers/wikipedia_parser'
require 'retriever/persistors/elasticsearch_persistor'
require 'retriever/processors/basic_processor'
require 'retriever/processors/dbpedia_sanitizer'
require 'retriever/processors/wikipedia_sanitizer'


module Retriever
end
