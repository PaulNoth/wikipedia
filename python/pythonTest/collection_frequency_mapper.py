#used in osx shell together with the reducer.py

#REQUIRED: include the sample file from github/data/sample_enwiki-latest-pages-articles2

#command used on mac osx: python mapper.py | sort -k1,1 | python reducer.py

#!/usr/bin/env python

import sys
import re

p2=re.compile('={2,6}([^=]*)={2,6}')
# input comes from sample file
with open ("sample_enwiki-latest-pages-articles2.xml", "r") as myfile:
    data=myfile.readlines()

for line in data:
    line = line.strip()
    
    m2=p2.match(line)
    if m2:
        print '%s' % m2.group(1).strip()