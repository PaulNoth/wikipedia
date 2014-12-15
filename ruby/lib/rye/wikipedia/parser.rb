module Wikipedia
	class Parser 
		def self.parse(input)	
			doc = Nokogiri::XML input

    		pages = doc.css("//page")
    		abstracts = "<document>"
    		pages.each do |page|
    			content  = page.css('text').text

                content.gsub!(/(<ref>.*?<\/ref>)/m,'')
                content.gsub!(/(<ref name=.*?<\/ref>)/m,'')
                content.gsub!(/(\{\{.*?\}\})/m,'')
    			abstract_text = *content.match(/('{3}.*?(?=\n\n={2}))/m) || "No abstract available"
    			abstracts << "<page> <title>#{page.css('title').text}</title> <abstract>#{HTMLEntities.new.encode(abstract_text[0])}</abstract> </page>\n"
    		end

    		abstracts << "</document>\n"
		end
	end
end
