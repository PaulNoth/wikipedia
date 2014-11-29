# Rucola

Very simple information retrieval library for explanatory purposes.

Library code is at `lib/rucola`, includes one example script at `example/rucola/rucola.rb` and is acompanied by a simple TTL file parser script at `scripts/parsers/long_abstracts_ttl.rb`.

## Structure

- `Corpus` - collection of documents defined by model, extractor, weighter and metric. Creates documents from text, finds similar documents to specfied query.
- `Document` - text transformed to a collection of terms by corpus utilizing specified extractors.
- `Model` - physical representation of terms in a corpus. For example a document-to-term weight matrix.
- `Extractor` - simple utility for converting text into terms like regexp splitter, stopword remover, word-to-word mapper etc.
- `Weighter` - function to compute weight for specified term of document in corpus such as TF-IDF.
- `Metric` - function to compute similarity between two documents, i.e. term-to-weight maps.

### Model

Each model must respond to `add(corpus, document)` and `similar(corpus, document)` where `corpus` is a `Corpus` and `document` is a `Document`, `add` returns unspecified value and `similar` returns a similarity map (a `Hash` of computed `Metric` values to `Document` instances) of documents to the specified document.

### Extractor

Each extractor must respond to `extract(input)` where `input` is a string or an enumerable of strings, `extract` returns a string or an enumerable of strings.

### Weighter

Each weighter must respond to `compute(term, document, corpus)` where `term` is a string, `document` is a `Document` and corpus is a `Corpus`, it always holds that `coprus` contains `document` contains `term`, `compute` usually returns a number.

### Metric

Each metric must respond to `compute(a, b)` where `a` and `b` are term-to-weight `Hash` maps, `compute` returns a number.

## TODO

- add real VSM matrix-based implementation
- tune extractors, for example do not remove terms in upper case shorter than 2 characters

## Example Rucola Script

Simple script demonstrating Rucola capabilities.

### Usage

```
ruby -Ilib example/rucola/rucola.rb [path [limit [dump|load]]]
```

#### Parameters

- `path` Path to TTL file.
- `limit` Read lines limit.
- `dump|load` Dumps or loads `path.corpus` (in case of load limit is ignored).

#### Examples

```
ruby -Ilib example/rucola/rucola.rb data/long_abstracts_sk.ttl 20
ruby -Ilib example/rucola/rucola.rb data/long_abstracts_sk.ttl 20 dump
ruby -Ilib example/rucola/rucola.rb data/long_abstracts_sk.ttl 20 load
```

### Input

Any TTL file with long abstracts from DBPedia in Slovak language.

### Output

Sample output for illustration purposes.

```
Parsing input data ... done (0.033s)
Adding documents to corpus ... done (0.502s)
Corpus:
  model = Rucola::Models::NaiveVectorSpace
  extractor = Rucola::Extractors::Splitter Rucola::Extractors::Deletor
  weighter = Rucola::Weighters::TfIdf
  metric = Rucola::Metrics::NaiveCosineSimilarity
  size = 19
Finding similar documents ... done (0.059s)
Documents similar to http://sk.dbpedia.org/resource/Taekkyon:
  1.000000000000 http://sk.dbpedia.org/resource/Taekkyon Taekkyon (slovenská výslovnosť \"thäkkjon, kor. 택견]) je starý kórejský bojový…
  0.122581520698 http://sk.dbpedia.org/resource/Turingov_test Turingov test sformuloval Alan Turing. Pokúša sa dať odpoveď na otázku ako…
  0.113102005726 http://sk.dbpedia.org/resource/Astronómia Astronómia, čo etymologicky znamená „zákon hviezd,“ (z gréčtiny: αστρονομία =…
  0.108618681326 http://sk.dbpedia.org/resource/Matematika Matematika (z gr. μαθηματικός (mathematikós)= „milujúci poznanie“ > μάθημα (máthe
  0.097085239771 http://sk.dbpedia.org/resource/Filozofia Filozofia (z gr. láska k múdrosti: filein = ľúbiť a sofia = múdrosť) je štúdia…
  0.095146311487 http://sk.dbpedia.org/resource/Francúzsko Francúzsko (po francúzsky La France, výslovnosť IPA [fʀɑ̃s]), dlhý tvar…
  0.094464764572 http://sk.dbpedia.org/resource/Fyzika Fyzika (zo starogr. φυσικός (fysikos) = \"prirodzený\" a φύσις (fysis) = \"prírod
  0.083612269175 http://sk.dbpedia.org/resource/RTFM RTFM je skratka v internetovom slangu pre frázu Read The Fucking Manual (po…
  0.083077316343 http://sk.dbpedia.org/resource/Literatúra Literatúra (z latinčiny litteratura = písmo / jazykoveda) : a) (tiež…
  0.077130157291 http://sk.dbpedia.org/resource/Tantrizmus Tantrizmus alebo tantra (-sanskritský kmeň; sanskritský nominatív: tantram) je…
  0.074306610693 http://sk.dbpedia.org/resource/Weblog Weblog (z anglického web log – doslova: webový denník) alebo skrátene a…
  0.071876908378 http://sk.dbpedia.org/resource/Tibet_(historické_územie) Tibet je historické a geografické územie v Ázii približne zodpovedajúce…
  0.069124505516 http://sk.dbpedia.org/resource/Biológia Biológia (grec. bios, βιος – život; logos, λογος – slovo) je náuka o živote.…
  0.067488887630 http://sk.dbpedia.org/resource/Internet Internet, hovorovo net alebo sieť je verejne dostupný celosvetový systém…
  0.063952947939 http://sk.dbpedia.org/resource/Egypt Egypt (arab. مصر‎ Misr) dlhý názov Egyptská arabská republika, je severoafrická…
  0.060429855663 http://sk.dbpedia.org/resource/Václav_Havel Václav Havel (* 5. október 1936, Praha, ČSR – † 18. december 2011, Hrádeček)…
  0.051276432870 http://sk.dbpedia.org/resource/Alan_Mathison_Turing Alan Mathison Turing (* 23. jún 1912, Londýn, Spojené kráľovstvo – † 7. jún 1954,
  0.039163022499 http://sk.dbpedia.org/resource/Zoznam_štátov Toto je zoznam štátov.
```

### Debug

Currently only extractor chain debug output is possible. To enable it edit main `rucola.rb` script and change `extractor = Rucola::Extractors::Chain` to `extractor = DebugChain`. Otherwise use [pry](https://github.com/pry/pry).
