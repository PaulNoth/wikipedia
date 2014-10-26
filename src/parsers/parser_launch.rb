$LOAD_PATH << './src/parsers'
require 'parser.rb'

p = Parser.new
p.ParseDictionary
