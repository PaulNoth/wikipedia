# Parsing wikipedia - SK, EN, HU langlinks and page sql dumps 
Matching SK wiki page titles to HU or EN titles using inter-language link SQL dumps

* Execute jar file with one argument on command line; the path to folder "data"
* At runtime, first choose which input data to use: 0 stands for small sample, 1 for the whole data set
* Data preprocessing only if whole data set was chosen
* Next the user has to determine whether he wants to run the code for SK-HU or SK-EN matches with typing in "hu" or "en"
* Program then generates both SK-[lang2] and [lang2]-SK matches along with some stats
* Unit tests executable through the IDE

1. Input files if running code on whole data set
  - skwiki-latest-langlinks.sql
  - skwiki-latest-page.sql
  - [lang2]wiki-latest-langlinks.sql
  - [lang2]wiki-latest-page.sql
2. Preprocessed data for input files (generated at runtime, unless sample data was chosen)
  - input_skwiki-latest-langlinks.sql
  - remaining 3 files analogously...
3. Output data - 3 files; found matches, found paired and unpaired (i.e., differences) matches
  - output_data_sk-[lang2]-matches.txt
  - output_stats_sk-[lang2]-paired-matches.txt
  - output_stats_sk-[lang2]-differences.txt
4. All these output files (preprocessed and resulting) generated into the same folder as the input files

Example of executing the java archive file:
- java -jar "C:\LangLinksFromSQL\dist\LangLinksFromSQL.jar" "C:\LangLinksFromSQL"

Then some basic information, which is described also above appears on the screen for further use of the program.