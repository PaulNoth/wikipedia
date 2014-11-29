module Rucola::Metrics
  module DiceSimilarity
    extend self

    def compute(a, b)
      (2 * (a.keys & b.keys).size).to_f / (a.size + b.size).to_f
    end
  end
end

