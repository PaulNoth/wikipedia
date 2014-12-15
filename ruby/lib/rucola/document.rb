module Rucola
  class Document < OpenStruct
    class Terms < Array
      def include?(term)
        count(term) != 0
      end

      def count(term)
        (@counter_cache ||= {})[term] ||= super(term)
      end
    end

    def size
      terms.size
    end
  end
end

