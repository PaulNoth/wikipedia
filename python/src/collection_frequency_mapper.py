#used in Hadoop stream together with the reducer.py

#command used on mac osx: hadoop jar /usr/local/Cellar/hadoop/2.2.0/libexec/share/hadoop/tools/lib/hadoop-streaming-2.2.0.jar \-file ***path/mapper.py \-file ***path/reducer.py \-input ***path/enwiki-latest-pages-articles2.xml \-mapper ***path/mapper.py \-reducer ***path/reducer.py \-output ***path/outputFile -cmdenv LC_CTYPE=zh_CN.UTF-8

#!/usr/bin/env python

import sys
import re

p2=re.compile('={2,6}([^=]*)={2,6}')
# input comes from STDIN (standard input)
for line in sys.stdin:
    line = line.strip()
    
    m2=p2.match(line)
    if m2:
        print '%s' % m2.group(1).strip()