module Rucola::Extractors
  Chain = Struct.new(:extractors) do
    def extract(input)
      extractors.inject(input) do |result, extractor|
        extractor.extract(result)
      end
    end

    def to_s
      "#{extractors.map(&:to_s).join ' '}"
    end
  end
end

