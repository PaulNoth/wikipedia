module Retriever::Parsers
  WikipediaParser = Struct.new(:tag) do
    def parse_tag(text)
      xml_doc = Nokogiri::XML text

      data = xml_doc.css("//#{ tag }")

      encoding_options = {
        :invalid           => :replace,  # Replace invalid byte sequences
        :undef             => :replace,  # Replace anything not defined in ASCII
        :replace           => '',        # Use a blank for those replacements
        :universal_newline => true       # Always break lines with \n
      }

      result = []

      data.each do |element|
        title = element.css('title').text.gsub(/ /, '_')
        title = title.gsub(/[\(\)\s]*/, '')
        title = title.downcase

        result << {
          title: title,
          text:  element.css('text').text.split("\n\n")[0]
        }
      end

      result
    end
  end
end
