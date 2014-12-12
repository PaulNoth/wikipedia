module Rucola::Metrics
  module TfIdfCosineSimilarity
    extend self

    def compute(a, b)
      (a.keys & b.keys).inject(0) { |s, k| s + (a[k] * b[k]) }.to_f / (Math.sqrt(sum a.values) * Math.sqrt(sum b.values))
    end

    private

    def sum(v)
      v.inject { |s, w| s + w**2 }
    end
  end
end

