module Retriever::Processors
  class DbpediaSanitizer
    def sanitize(text)
      text.gsub!(/."@de ./, '</abstract>')
      text.gsub!(/<http:\/\/de.dbpedia.org\/resource\//, '<abstract source=\'')
      text.gsub!(/> <http:\/\/dbpedia.org\/ontology\/abstract> /, '\'>')
    end
  end
end