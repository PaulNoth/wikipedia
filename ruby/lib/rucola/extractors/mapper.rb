module Rucola::Extractors
  Mapper = Struct.new(:block_or_string) do
    def extract(input)
      Array(input).flat_map &block
    end

    def to_s
      self.class.name
    end

    private

    def block
      @block ||= block_or_string.is_a?(Proc) ? block_or_string : eval(block_or_string)
    end

    def _dump(level)
      block_or_string
    end

    def self._load(args)
      new(*args)
    end
  end
end

