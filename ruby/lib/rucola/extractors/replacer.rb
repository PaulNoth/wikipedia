module Rucola::Extractors
  Replacer = Struct.new(:regexp, :replacement) do
    def extract(input)
      Array(input).flat_map { |item| item.gsub regexp, replacement }
    end

    def to_s
      self.class.name
    end
  end
end

