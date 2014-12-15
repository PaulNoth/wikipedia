module Rucola::Extractors
  Trimmer = Struct.new(:regexp) do
    def extract(input)
      Array(input).flat_map { |item| item.gsub regexp, '' }
    end

    def to_s
      self.class.name
    end
  end
end

