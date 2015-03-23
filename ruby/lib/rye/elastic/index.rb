module Elastic
	class Index
		attr_accessor :client, :name, :type, :mappings, :settings

		def client
			@client ||= Elasticsearch::Client.new
		end

		 def create
		 	return if exists?

      		client.indices.create index: name, type: type, body: { settings: settings, mappings: mappings }

      		flush
    	end

		def delete
      		client.indices.delete index: name if exists?
    	end

    	def exists?
      		client.indices.exists index: name
    	end


		def flush
      		client.indices.flush index: name
    	end

    	def stats
      		client.indices.stats index: name, all: true
    	end

    	def mappings
    		@mappings ||= {
      			type => {
        			properties: {
          				title: { type: :string, analyzer: :text},
            			abstract: { type: :string }
        			}
      			}
    		}
    	end

    	def settings 
    		@settings ||= {
      			index: {
        			number_of_shards: 1,
        			number_of_replicas: 0,
        			analysis: {
          				analyzer: {
            				text: {
              					type: :custom,
              					tokenizer: :standard,
              					filter: [:asciifolding, :lowercase, :trim]
              				}
              			},

                  filter: {
                    stop_en: {
                      type: :stop,
                      lang: :english
                      }
                    }
              		}
      			}
    		}
    	end
	end
end

