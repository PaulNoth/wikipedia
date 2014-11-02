#used in osx shell together with the mapper.py

#REQUIRED: include the sample file from github/data/sample_enwiki-latest-pages-articles2

#command used on mac osx: python mapper.py | sort -k1,1 | python reducer.py

#!/usr/bin/env python

import sys

# input comes from STDIN
for line in sys.stdin:
    #sys.stdout.write('%s' %line)
    print line