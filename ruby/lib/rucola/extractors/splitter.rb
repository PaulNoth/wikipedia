module Rucola::Extractors
  Splitter = Struct.new(:regexp) do
    def extract(input)
      Array(input).flat_map { |item| item.split regexp }
    end

    def to_s
      self.class.name
    end
  end
end

