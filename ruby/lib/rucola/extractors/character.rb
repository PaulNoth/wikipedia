module Rucola::Extractors
  module Character
    def extract(input)
      Array(input).flat_map { |item| item.chars }
    end
  end
end

