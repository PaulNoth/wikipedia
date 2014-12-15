##NER dictionary
Parses wikipedia XML dumps and creates pairs of wikipedia page title and it's category. Enables to query those results.
###Installation
Create new java project copy and set directories java/src and java/test of this github project as source directories. Include directory java/lib to buildpath. Don't forget to include directory "data" to project classpath. Build.
###How to use
This application has console interface. With argument `-q` it can be started in query mode to query already crated dictionary. Usually the output files are created in the actual directory, but this can be changed with other arguments (see below). If not in query mode but in parse mode required arguments are `source` and `rules`. Sample of each can be found in data directory. If you want to start query mode and the output file paths where changed while parsing with arguments, use the same arguments to set changed paths together with argument `-q`. 
```
usage: NERDictionary  [-h] [-q] [-r RULES] [-o OUTPUT] [-c CATEGORIES]
                      [-i INDEX] [source [source ...]]

Create name entity recognition dictionary.

optional arguments:
  -h, --help             show this help message and exit
  -q, --query            Start query  mode  if  specified,  otherwise start
                         parse mode. Index  directory  and category mapping
                         file  must  be  specified  if  changed.  (default:
                         false)

Parse mode arguments:
  -r RULES, --rules RULES
                         Specify file in which  are  the rules for category
                         recognition stored.
  -o OUTPUT, --output OUTPUT
                         Specify file  in  which  the  dictionary  will  be
                         created. (default: NER_dictionary.tsv)
  -c CATEGORIES, --categories CATEGORIES
                         Specify file in  which  the  category mapping will
                         be stored. (default: NER_categories.txt)
  -i INDEX, --index INDEX
                         Specify directory  in  which  the  index  will  be
                         stored. (default: Index)
  source                 Source files to  parse.  If  the  path specifies a
                         directory, each  file  in  the  directory  will be
                         parsed.
```
To create your own rules file fulfill the template:
```
<ruleset>
  <rule>
		<pattern></pattern>
		<category></category>
	</rule>
	<rule type="info">
		<pattern></pattern>
		<category></category>
	</rule>
<\ruleset>
```
Where `pattern` is regular expression to match distinguishing string of category within pages's text, and `category` is name of category to assign. If the pattern should be applicable only in infobox rather then whole text use `type=info`.
