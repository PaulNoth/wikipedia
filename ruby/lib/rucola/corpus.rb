module Rucola
  Corpus = Struct.new(:model, :extractor, :weighter, :metric) do
    attr_reader :documents

    def initialize(*)
      super

      @documents = Documents.new(self)
    end

    class Documents < Array
      def initialize(corpus)
        @corpus = corpus
      end

      def count(*args)
        return super unless args[0].is_a?(Hash) && (term = args[0][:include])
        
        return @corpus.model.select(term).size if @corpus.model.respond_to? :select

        super { |document| document.terms.include? term }
      end
    end

    def add(text, attrs = {})
      document = new_document(text, attrs)

      documents << document

      model.add self, document

      document
    end

    def similar(text, attrs = {})
      model.similar self, new_document(text, attrs)
    end

    def size
      documents.size
    end

    private

    def new_document(text, attrs = {})
      terms = extractor.extract text

      Document.new(attrs.merge terms: Document::Terms.new(terms))
    end
  end
end

