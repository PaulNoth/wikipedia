require 'rubygems'
require 'lda-ruby'
require 'active_support/all'
require 'nokogiri'
require 'pry'
require 'htmlentities'
require 'ostruct'
require 'rake'
require 'measurable'

require 'elasticsearch'

require 'elastic/index'

require 'dbpedia/parser'
require 'dbpedia/index'
require 'dbpedia/search'
require 'dbpedia/results'

require 'wikipedia/parser'
require 'wikipedia/index'
require 'wikipedia/search'
require 'wikipedia/results'

require 'rye/analyzer'

module Rye
end