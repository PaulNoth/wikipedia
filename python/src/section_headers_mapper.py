#used in Hadoop stream together with the reducer.py

#command used on mac osx: hadoop jar /usr/local/Cellar/hadoop/2.2.0/libexec/share/hadoop/tools/lib/hadoop-streaming-2.2.0.jar \-file ***path/mapper.py \-file ***path/reducer.py \-input ***path/enwiki-latest-pages-articles2.xml \-mapper ***path/mapper.py \-reducer ***path/reducer.py \-output ***path/outputFile -cmdenv LC_CTYPE=zh_CN.UTF-8

#!/usr/bin/env python

import sys
import re

p1=re.compile('<title>(.*)</title>')
p2=re.compile('={2,6} ?([^=]*) ?={2,6}')
firstTitle=0
# input comes from STDIN (standard input)
for line in sys.stdin:
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