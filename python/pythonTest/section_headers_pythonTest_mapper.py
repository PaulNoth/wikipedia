#used in osx shell together with the reducer.py

#REQUIRED: include the sample file from github/data/sample_enwiki-latest-pages-articles2

#command used on mac osx: python mapper.py | sort -k1,1 | python reducer.py

#!/usr/bin/env python

import sys
import re

p1=re.compile('<title>(.*)</title>')
p2=re.compile('={2,6} ?([^=]*) ?={2,6}')
firstTitle=0
# input comes from sample file
with open ("sample_enwiki-latest-pages-articles2.xml", "r") as myfile:
    data=myfile.readlines()

for line in data:
    line = line.strip()
    m1=p1.match(line)
    m2=p2.match(line)
    if m1:
        if firstTitle:
            sys.stdout.write('\n%s' % m1.group(1))
        else:
            sys.stdout.write('%s' % m1.group(1))
            firstTitle=1
    elif (m2 and firstTitle):
        sys.stdout.write('\t%s' % m2.group(1))
sys.stdout.write('\n')