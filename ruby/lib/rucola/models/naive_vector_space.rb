module Rucola::Models
  class NaiveVectorSpace
    def initialize
      @documents_to_term_weights = {}
      @terms_to_documents = {}
    end

    def add(corpus, document)
      document.terms.each do |term|
        documents = @terms_to_documents[term] ||= []

        documents << document

        update_weights(term, documents, corpus)
      end
    end

    def similar(corpus, document)
      weights = compute_weights_for(document, corpus)
      documents = prefetch_similar_to(document)

      compute_similarities(weights, documents, corpus)
    end

    def select(term)
      @terms_to_documents[term]
    end

    def weight(document)
      @documents_to_term_weights[document]
    end

    def to_s
      self.class.name
    end

    private

    def compute_weights_for(document, corpus)
      weights = {}

      document.terms.each do |term|
        weights[term] = corpus.weighter.compute(term, document, corpus)      
      end

      weights
    end

    def prefetch_similar_to(document)
      documents = Set.new

      document.terms.each do |term|
        documents.merge @terms_to_documents[term]
      end
      
      documents.flatten
    end

    def compute_similarities(weights, documents, corpus)
      similarities = {}

      documents.each do |document|
        similarities[corpus.metric.compute(weights, @documents_to_term_weights[document])] = document
      end

      similarities.delete 0.0
      similarities
    end

    def update_weights(term, documents, corpus)
      documents.each do |document|
        weights = @documents_to_term_weights[document] ||= {}

        weights[term] = corpus.weighter.compute(term, document, corpus)
      end
    end
  end
end

