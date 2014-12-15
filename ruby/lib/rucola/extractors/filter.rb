module Rucola::Extractors
  Filter = Struct.new(:regexp) do
    def extract(input)
      Array(input).flatten.select { |item| item =~ regexp }
    end

    def to_s
      self.class.name
    end
  end
end

