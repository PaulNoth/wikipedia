module Rucola::Weighters
  module Idf
    extend self

    def compute(term, document, corpus)
      1 + Math.log(corpus.size.to_f / corpus.documents.count(include: term).to_f)
    end
  end
end

