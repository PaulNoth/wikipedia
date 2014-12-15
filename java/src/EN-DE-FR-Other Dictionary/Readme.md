After the compilation of the program, it can be run with the command 
java -cp ..;..\lib\lucene-core-4.10.2.jar;..\lib\lucene-queries-4.10.2.jar;..\lib\lucene-analyzers-common-4.10.2.jar;..\lib\lucene-queryparser-4.10.2.jar; dataManipulation.Director -h
while the directory you are in is the one where is the compiled code. In the folder where are the compiled files, has to be an folder "data" where are 3
language packs from dbpedia with their default name - "interlanguage_links_en.ttl", "interlanguage_links_de.ttl","interlanguage_links_fr.ttl"
The program has several parameters that can be run:

"-h" : help
"-p" : parses the files
"-i" : indexes the files
"-search [language] [word]" : searches the translation to language from the word (the word has to be in en/de/from
"-stat" : shows the parsed stats
"-langStat [language]" : shows the statistic of the language

To use search the parser ("dataManipulation.Director -p") has to be run and the 
indexer ("dataManipulation.Director -i").

All the libs are added in the lib/ directory.