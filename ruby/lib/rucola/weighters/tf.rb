module Rucola::Weighters
  module Tf
    extend self

    def compute(term, document, corpus = nil)
      document.terms.count(term).to_f / document.terms.size.to_f
    end
  end
end

