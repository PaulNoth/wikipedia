module Rye
	class Analyzer

		Lda::Lda.class_eval do
  			def compute_topic_document_probability
    			outp = Array.new

    			@corpus.documents.each_with_index do |doc, idx|
      				tops = [0.0] * self.num_topics
      				self.phi[idx].each_with_index do |word_dist, word_idx|
        				word_dist.each_with_index do |top_prob, top_idx|
          					tops[top_idx] += top_prob
        				end
      				end
      			outp << tops
    			end

    		outp
  			end
		end

		def self.compare(query)

			corpus = Lda::Corpus.new

    		dbpedia_search   = Dbpedia::Search.new

    		wikipedia_search = Wikipedia::Search.new

    		dbpedia_result   = dbpedia_search.search(q: query)[0]
    		wikipedia_results = wikipedia_search.search(q: query)

    		document = Lda::TextDocument.new(corpus, [dbpedia_result.title, dbpedia_result.abstract].join(' '))
    
    		corpus.add_document(document)
    
    		wikipedia_results.each do |page|
      			document = Lda::TextDocument.new(corpus, [page.title, HTMLEntities.new.decode(page.abstract)].join(' '))

      			corpus.add_document(document)
    		end

    		lda = Lda::Lda.new(corpus)

    		lda.em('random')

    		matrix = lda.compute_topic_document_probability

    		pattern = matrix[0]

    		similarities = Hash.new

    		matrix.each_with_index do |other, index|
      			next if other == pattern

      			similarities[index] = Measurable.cosine_similarity(other, pattern)
    		end

    		best_index = similarities.max_by{|k,v| v}

    		puts " "
    		puts '============== result ==============='
    		puts " "

    		puts "DBPEDIA"
    		puts "title:    #{dbpedia_result.title}"
    		puts "abstract: #{dbpedia_result.abstract}"

    		puts " "
    		puts "Wikipedia"
    		puts "title:    #{wikipedia_results[best_index[0]-1].title}"
    		puts "abstract: #{wikipedia_results[best_index[0]-1].abstract}"

    		puts " "
    		puts "Similarity: #{best_index[1]}"

		end

	end
end