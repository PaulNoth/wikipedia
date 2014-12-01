$LOAD_PATH << '.'
require 'parser.rb'
require 'rspec'

describe :parser do

  it 'should parse langlinks and page dumps into dictionary XML' do

    sample = IO.read("data\\sample_dictionary.xml")
    Parser.new.ParseDictionary
    output = IO.read("data\\dictionary.xml")
    expect(output).to be_eql sample
  end
end