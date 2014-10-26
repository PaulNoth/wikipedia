#encoding: utf-8
require 'builder'

class Parser
  def ParseDictionary

    sk_langlinks = parse_langlinks("data\\sample_skwiki-latest-langlinks.sql", ['en', 'fr'])
    sk_pages = parse_pages("data\\sample_skwiki-latest-page.sql")

    sk_hash = Hash.new{|h,k| h[k] = {}}
    sk_langlinks.each { |langlink| sk_hash[langlink[0]][langlink[1]] = normalize_word langlink[2] }

    sk_pages.each do |page|
      if sk_hash.has_key?(page[0])
      then sk_hash[page[0]]['sk'] = normalize_word page[1]
      end
    end

    xml = Builder::XmlMarkup.new( :indent => 2 )
    xml.dictionary do
      sk_hash.each do |word|
        xml.word do
          word[1].each do |version|
            xml.version do
              xml.language version[0]
              xml.value version[1].force_encoding("utf-8")
            end
          end
        end
      end
    end

    xml_data = xml.target!
    file = File.new("data\\dictionary.xml", "w:UTF-8")
    file.write(xml_data)
    file.close

  end

  def parse_langlinks (path, langs)
    str = IO.read(path).gsub('_', ' ')
    match = str.scan(/\(([[:digit:]]+),'(#{langs.join('|')})','(([^']*|\\')+)'\)/)
  end

  def parse_pages (path)
    str = IO.read(path).gsub('_', ' ')
    match = str.scan(/\(([[:digit:]]+),[[:digit:]]+,'([^']*)','[^']*',[[:digit:]]+,[[:digit:]]+,[[:digit:]]+,[[:digit:].]+,'[[:digit:]]+','[[:digit:]]+',[[:digit:].]+,[[:digit:].]+,[[:digit:].]+,[^\)]+\)/)
  end

  def normalize_word raw
    raw.delete('\\').gsub(/.*:/, "")
  end
end