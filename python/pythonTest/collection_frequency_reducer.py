#used in osx shell together with the mapper.py

#REQUIRED: include the sample file from github/data/sample_enwiki-latest-pages-articles2

#command used on mac osx: python mapper.py | sort -k1,1 | python reducer.py

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