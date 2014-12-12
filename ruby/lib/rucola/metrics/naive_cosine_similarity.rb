module Rucola::Metrics
  module NaiveCosineSimilarity
    extend self

    def compute(a, b)
      (a.keys & b.keys).size.to_f / Math.sqrt(a.size * b.size)
    end
  end
end

