module Retriever::Parsers
  DbpediaParser = Struct.new(:tag) do
    def parse_tag(text)
      html_doc = Nokogiri::HTML text

      data = html_doc.css("//#{ tag }")

      encoding_options = {
        :invalid           => :replace,  # Replace invalid byte sequences
        :undef             => :replace,  # Replace anything not defined in ASCII
        :replace           => '',        # Use a blank for those replacements
        :universal_newline => true       # Always break lines with \n
      }

      result = []

      data.each do |element|
        title = element.attribute('source').text.gsub(/ /, '_')
        title = title.gsub(/[\(\)\s]*/, '')
        title = title.downcase
        title = title.encode(Encoding.find('ASCII'), encoding_options)

        result << {
          title: title,
          text:  element.text.encode(Encoding.find('ASCII'), encoding_options)
        }
      end

      result
    end
  end
end
