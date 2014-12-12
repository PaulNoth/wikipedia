$LOAD_PATH << './src/parsers'
require 'parser.rb'

p = Parser.new
p.parse_dictionary  ['sk', 'en', 'fr']
