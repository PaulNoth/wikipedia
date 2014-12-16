##DBPedia

Category parsing and searching, while creting multilingual dictionary from them (at least 3 languages)

##Prerequisites
1. Python 2.7.x
2. Elasticsearch
3. Node.js (for Calaca - http-server)

##Installation
Install and run Elasticsearch cluster (default-localhost:9200)
Run and deploy Calaca on http-server (default-localhost:8080)

##Usage
Parse your .ttl dat in /data folder using: /src/parsers/dbparse.py. 
Run <code>dbparse.py language choose[sk,en,de], file mode[sample,full]</code>

### Example: 
<code>python dbparse.py sk sample</code>

Index the .csv output from your data. Run /src/parsers/dump.py. 
<code>Run dump.py [path-to-csv-file] language of file in format ".." [sk,en,de]</code>

### Example 
<code>dump.py /home/user/ir/csv/title.csv en</code>

Browse the location:
<code>localhost:8080</code>
