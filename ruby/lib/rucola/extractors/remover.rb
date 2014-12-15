module Rucola::Extractors
  Remover = Struct.new(:items) do
    def extract(input)
      Array(input).flatten.reject { |item| items.include? item }
    end

    def to_s
      self.class.name
    end
  end
end

