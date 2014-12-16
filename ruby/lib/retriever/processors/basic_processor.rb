module Retriever::Processors
  BasicProcessor = Struct.new(:index, :sanitizer, :parser, :persistor) do
    def process_file(path)
      result = {}

      File.open(path, 'r') do |f|
        chunk = f.read(204800) # read 100 MB

        chunk = sanitizer.sanitize chunk

        persistor.persist(index, parser.parse_tag(chunk))
      end
    end
  end
end
