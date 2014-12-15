module Wikipedia
  class Search
    attr_accessor :index

    def initialize
      @index = Wikipedia::Index.new
    end

    def client
      index.client
    end

    def search(params)
      query = { query: {
                  query_string: {
                    query: params[:q],
                    default_operator: :and,
                    fields: [:title]
                    }
                }
              }

      response = client.search index: index.name, body: query

      response_abstract = Results.new(response)
      response_abstract.results

    end
  end
end

