module Dbpedia
	class Index < Elastic::Index

		def name
      		@name ||= :dbpedia
    	end

    	def type
      		@type ||= :page
    	end

		def import_data(xml)
    	delete
    	create

			source = Nokogiri::XML xml

			pages = source.xpath("//page")

      		pages.each do |page|
      			print("dbpedia procesing #{page.css('title').text}\n")
            document = {title: page.css('title').text, abstract: page.css('abstract').text}
        		client.bulk body:
          			[
            			{ index: { _index: name, _type: type }},
                  document
          			]
      		end
      		flush
    	end
	end
end

