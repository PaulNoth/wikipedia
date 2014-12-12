module Rucola::Metrics
  module SizeDifference
    extend self

    def compute(a, b)
      (a.size - b.size).abs
    end
  end
end

