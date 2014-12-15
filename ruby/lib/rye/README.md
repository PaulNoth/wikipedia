# Rye

Library for parsing and comparing DBpedia and Wikipedia abstracts.

## Requirements

To run this project, you need to have 

* Ruby (2.1 and more)
* Elasticsearch (1.1 and more)

## Usage

#### Parsing

First parse abstract form DBpedia.
```
rake "dbpedia:setup_dbpedia[DBpedia_file_path]"
```

Parse abstract form Wikipedia.
```
rake "wikipedia:setup_wikipedia[Wikipedia_file_path]"
```

#### Comparing

Import DBpedia abstracts.

```
rake dbpedia:import_dbpedia
```

Import Wikipedia abstracts.

```
rake wikipedia:import_wikipedia
```

**Note**: Using rake ```"dbpedia:find_dbpedia[title_of_abstract]"``` or rake ```"wikipedia:find_wikipedia[title_of_abstract]"``` can find abstract in DBpedia or Wikipedia.

Make comparing between abstracts
```
rake "abstracts:compate[title_of_abstract]"
```
### Output of comparing
```
============== result ===============
 
DBPEDIA
title:    Animalia (book)
abstract: Animalia is an illustrated children's book by Graeme Base. It was originally 
published in 1986,followed by a tenth anniversary edition in 1996 and a 25th anniversary
edition in 2012.Over three million copies have been sold. A special numbered and signed
anniversary edition was also published in 1996 with an embossed gold jacket.
 
Wikipedia
title:    Animalia (book)
abstract: '''''Animalia''''' is an illustrated [[Children's literature|children's book]]
by [[Graeme Base]].It was originally published in 1986, followed by a tenth anniversary 
edition in 1996 and a 25th anniversary edition in 2012. Over three million copies have 
been sold.   A special numbered and signed anniversary edition was also published in 1996
with an embossed gold jacket.
 
Similarity: 0.9931698897415857
```
