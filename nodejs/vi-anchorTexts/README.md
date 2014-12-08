# vi-anchorTexts

**Project**: 7. Anchor texts a štatistika k anchortextom. Document frequency, collection frequency.

## Installation

1. Clone this repository
2. Run: ```npm install```
3. Install [Elasticsearch](http://www.elasticsearch.org/guide/en/elasticsearch/guide/current/_installing_elasticsearch.html)

## Usage

1. Parse XML file and create Elasticsearch index (myindex)
```$ node parseXML.js [fileName]```

2. Search for anchor   
```$ node search.js "[anchor]"```


## Test

Tests using mocha.js.

Run: ```npm test```

## Author

Adam Močkoř @ 2014
