require 'spec_helper'

describe Dbpedia::Parser do
	describe '.parse' do

		it 'parses abstracts from dbpedia' do 
			input_file = 'data/sample_long_abstracts_en.ttl'
			output_file = 'data/sample_long_abstracts_en_output'

			result = Dbpedia::Parser.parse(File.read(input_file))

			expect(result).to eql File.read(output_file)
		end
	end
end