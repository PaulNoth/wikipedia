require 'rucola'
require 'stopwatch'

input = ARGV[0] || '../data/sample_input_long_abstracts_sk.ttl'
limit = ARGV[1] || 10
marsh = ARGV[2] || false

# use DebugChain instead of Rucola::Extractors::Chain to see how extractors work
DebugChain = Struct.new(:extractors) do
  def extract(input)
    extractors.inject(input) do |result, extractor|
      print "#{extractor} #{result}"

      result = extractor.extract(result)

      puts " -> #{result}"

      result
    end
  end

  def to_s
    "#{extractors.map(&:to_s).join ' '}"
  end
end

watch = Stopwatch.new

if marsh != 'load'
  watch.start "Parsing input data"

  raw = `head -n #{limit} #{input} | script/parsers/long_abstracts_ttl.rb`.split(/\n/).reject(&:empty?)
  data = Hash[raw.map { |item| item.partition(' ').tap { |parts| parts.delete_at 1 }}]

  watch.stop

  to_ascii_downcase = "lambda { |item| item.mb_chars.normalize(:kd).bytes.map { |b| (0x00..0x7F).include?(b) ? b.chr : '' }.join.downcase }"

  stopwords = %w(
    a aby aj ak ako ale alebo and ani áno asi až
    bez bude budem budeš budeme budete budú by bol bola boli
    bolo byť cez čo či ďalší ďalšia ďalšie dnes do ho ešte
    i ja je jeho jej ich iba iné iný som si sme sú k kam
    každý každá každé každí kde keď kto ktorá ktoré ktorou
    ktorý ktorí ku lebo len ma mať má máte medzi mi mna mne
    mnou musieť môcť môj môže my na nad nám napr náš naši nie
    nech než nič niektorý nové nový nová nové noví o od odo
    on ona ono oni ony po pod podľa pokiaľ potom práve pre
    prečo preto pretože prvý prvá prvé prví pred predo pri
    pýta s sa so si svoje svoj svojich svojím svojími ta tak
    takže táto teda ten tento tieto tým týmto tiež tj to toto
    toho tohoto tom tomto tomuto toto tu tú túto tvoj ty
    tvojími už v vám váš vaše vo viac však všetok vy z za
    zo že)

  stopwords.map! { |word| eval(to_ascii_downcase).call word }

  model = Rucola::Models::NaiveVectorSpace.new
  extractor = Rucola::Extractors::Chain.new [Rucola::Extractors::Splitter.new(/[[:space:]]+/), Rucola::Extractors::Trimmer.new(/[^[[:alnum:]]]/), Rucola::Extractors::Mapper.new(to_ascii_downcase), Rucola::Extractors::Remover.new(stopwords), Rucola::Extractors::Filter.new(/[[:alnum:]]{2,}/)]
  weighter = Rucola::Weighters::TfIdf
  metric = Rucola::Metrics::NaiveCosineSimilarity

  corpus = Rucola::Corpus.new(model, extractor, weighter, metric)

  watch.measure "Adding documents to corpus" do
    data.each { |link, text| corpus.add text, link: link, text: text }
  end
else
  watch.start "Loading corpus"

  corpus = Marshal.load(File.read "#{input}.corpus")

  watch.stop
end

# only corpus.metric can be changed on loaded corpus, other attributes cannot
# corpus.metric = Rucola::Metrics::TfIdfCosineSimilarity
# corpus.metric = Rucola::Metrics::DiceSimilarity
# corpus.metric = Rucola::Metrics::SizeDifference

puts "Corpus:"
puts "  model = #{corpus.model}"
puts "  extractor = #{corpus.extractor}"
puts "  weighter = #{corpus.weighter}"
puts "  metric = #{corpus.metric}"
puts "  size = #{corpus.size}"

if marsh == 'dump'
  watch.measure "Dumping corpus" do
    File.open("#{input}.corpus", 'w') { |file| file.write(Marshal.dump corpus) }
  end
end

watch.start "Finding similar documents"

sample = corpus.documents.sample
similar = corpus.similar(sample.text)

watch.stop

puts "Documents similar to #{sample.link}:"

similar.sort.reverse.each do |similarity, document|
  puts "  #{'%.12f' % similarity} #{document.link} #{document.text[0..80].gsub(/\s[[:alnum:]]+\s*$/, '…')}"
end

