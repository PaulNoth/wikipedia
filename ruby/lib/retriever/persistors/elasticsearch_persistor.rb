module Retriever::Persistors
  class ElasticsearchPersistor
    def initialize
      @client = Elasticsearch::Client.new log: true
    end

    def persist(index, data)
      request = data.map do |abstract|
        [
          { index: { _index: index, _type: :abstract, _id: abstract[:id] }},
          abstract
        ]
      end

      @client.bulk body: request.flatten
    end

    def create_index(name)
      self.drop_index name if self.exists?(name)

      settings = {
        index: {
          number_of_shards: 1,
          number_of_replicas: 0
        },

        analysis: {
          analyzer: {
            title: {
              type: :custom,
              tokenizer: :standard,
              filter: [:asciifolding, :lowercase, :trim]
            },

            text: {
              type: :custom,
              tokenizer: :standard,
              filter: [:asciifolding, :lowercase, :trim]
            },
          },

          filter: {
            stop_de: {
              type: :stop,
              lang: :german
            }
          }
        }
      }

      mappings = {
        abstract: {
          properties: {
            id: {
              type: :integer
            },

            title: {
              type: :multi_field,
              fields: {
                title: {
                  type: :string,
                  analyzer: :text,
                },
                untouched: {
                  type: :string,
                  index: :not_analyzed
                }
              }
            },

            text: {
              type: :multi_field,
              fields: {
                text: {
                  type: :string,
                  analyzer: :text,
                },
                untouched: {
                  type: :string,
                  index: :not_analyzed
                }
              }
            },
          }
        }
      }


      @client.indices.create index: name, type: :abstract, body: { settings: settings, mappings: mappings }

    end

    def search(index, query)
      @client.search index: index, q: query
    end

    def drop_index(name)
      @client.indices.delete index: name
    end

    def exists?(name)
      @client.indices.exists index: name
    end
  end
end
