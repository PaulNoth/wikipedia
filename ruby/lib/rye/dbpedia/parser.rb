module Dbpedia
	class Parser
		def self.parse(text)
			abstracts = "<document>"
			text.each_line do |line|
				next if line =~ /\A\W?\#/
		
				_, link, text = *line.match(/\<([^\>]*)\>.*\>\s"(.*)"/)

				title = *link.match(/[^\/]+$/)
				title[0].gsub!(/_/,' ')
			
				abstracts << "<page> <title>#{title[0]}</title> <abstract>#{text}</abstract> </page>\n"
			end
			
			abstracts << "</document>\n"
		end
	end
end

