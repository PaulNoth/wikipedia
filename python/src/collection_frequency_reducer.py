#used in Hadoop stream together with the mapper.py

#command used on mac osx: hadoop jar /usr/local/Cellar/hadoop/2.2.0/libexec/share/hadoop/tools/lib/hadoop-streaming-2.2.0.jar \-file ***path/mapper.py \-file ***path/reducer.py \-input ***path/enwiki-latest-pages-articles2.xml \-mapper ***path/mapper.py \-reducer ***path/reducer.py \-output ***path/outputFile -cmdenv LC_CTYPE=zh_CN.UTF-8

#!/usr/bin/env python

#from operator import itemgetter
import sys

current_word = None
current_count = 0
word = None

#num_lines = sum(1 for line in sys.stdin)
data = sys.stdin.readlines()
num_lines = len(data)

#for line in sys.stdin:
for line in data:
    word = line.strip()
    
    # logic based on hadoops sorting of the mappers output
    if current_word == word:
        current_count += 1
    else:
        if current_word:
            print '%s\t%s\t%f' % (current_word, current_count, float(current_count)/num_lines)
        current_count = 1
        current_word = word

if current_word == word:
    print '%s\t%s\t%f' % (current_word, current_count, float(current_count)/num_lines)