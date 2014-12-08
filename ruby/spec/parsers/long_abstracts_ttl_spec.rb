describe :long_abstracts_ttl do
  it 'parses long abstracts in ttl format' do
    fixture = '../data/sample_output_long_abstracts_sk'
    input = '../data/sample_input_long_abstracts_sk.ttl'
    output = `script/parsers/long_abstracts_ttl.rb < #{input}`

    expect(output).to be_eql File.read(fixture)
  end
end

