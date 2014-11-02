#used in Hadoop stream together with the mapper.py

#command used on mac osx: hadoop jar /usr/local/Cellar/hadoop/2.2.0/libexec/share/hadoop/tools/lib/hadoop-streaming-2.2.0.jar \-file ***path/mapper.py \-file ***path/reducer.py \-input ***path/enwiki-latest-pages-articles2.xml \-mapper ***path/mapper.py \-reducer ***path/reducer.py \-output ***path/outputFile -cmdenv LC_CTYPE=zh_CN.UTF-8

#!/usr/bin/env python

import sys

# input comes from STDIN
for line in sys.stdin:
    #sys.stdout.write('%s' %line)
    print line