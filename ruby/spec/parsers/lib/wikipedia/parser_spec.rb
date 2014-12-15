require 'spec_helper'

describe Wikipedia::Parser do
	describe '.parse' do

		it 'parses abstracts from Wikipedia' do 
			input_file = 'data/enwiki-latest-pages-articles-sample-input.xml'
			output_file = 'data/enwiki-latest-pages-articles-sample-output'

			result = Wikipedia::Parser.parse(File.read(input_file))

			expect(result).to eql File.read(output_file)
		end
	end
end