module Wikipedia
	class Results
    	attr_accessor :response

    def initialize(response)
       @response = response
       @hits     = response['hits']
       @results  = @hits['hits'].map { |hit| OpenStruct.new(hit['_source']) }
    end

    def results
    	@results
    end 
  end
end

