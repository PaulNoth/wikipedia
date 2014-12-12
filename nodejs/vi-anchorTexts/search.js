'use strict';

var f = require('./libs/searchFunc.js');
var Q = require('q');

var index = 'myindex';

// get arguments
var query = process.argv[2];

// check if there is string
if (typeof query === 'undefined') {
  console.error('Error: No input string!');
  process.exit(1);
}

// log response
function writeResponse(word,response) {
  console.log('Total anchors of "%s": %d', word, response.hits.total);
  console.log('Most used anchor text:');
  response.aggregations.group_by_text.buckets.forEach(function(item){
    console.log('"%s" (%d)', item.key, item.doc_count);
  });
}

// check if ES is running
f.checkES()
.then(function() {
  // serch query
  f.search(query,index).then(function(response){
    writeResponse(query, response);
  });
});
