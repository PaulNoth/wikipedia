module Rucola::Weighters
  module TfIdf
    extend self

    def compute(term, document, corpus)
      Tf.compute(term, document) * Idf.compute(term, document, corpus)
    end
  end
end

