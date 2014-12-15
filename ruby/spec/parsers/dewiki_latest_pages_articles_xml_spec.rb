require 'retriever'

describe :dewiki_latest_pages_articles_xml do
  it 'processes abstracts from wikipedia correctly' do
    wikipedia = File.open('../data/sample_dewiki-latest-pages-articles.xml')
    output    = File.open('../data/sample_output_dewiki_latest_pages_articles_xml.txt')

    parser = Retriever::Parsers::WikipediaParser.new(:page)

    processed_wikipedia = parser.parse_tag wikipedia

    expect(processed_wikipedia[0][:title]).not_to eql(output.readline)
    expect(processed_wikipedia[0][:text]).not_to eql(output.readline)
  end
end
