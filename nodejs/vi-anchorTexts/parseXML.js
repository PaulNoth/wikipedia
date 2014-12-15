'use strict';
var f = require('./libs/parseXMLfunc.js');
var Q = require('q');

// get arguments
var file = process.argv[2];

var index = 'myindex';

// check if there is file
if (typeof file === 'undefined') {
  console.error('Error: No input file!');
  process.exit(1);
}

// Run
f.checkES(index)
.then(function() {
  console.time('Parsing time');
  
  f.indexAnchors(file,index)
  .then(function(counter) {
    console.timeEnd('Parsing time');
    console.log('File is indexed!');
    process.stdout.write(counter + ' articles readed\n');
    return true;
  }).catch(function(error){
    console.log(error);
  }).progress(function(progress){
    process.stdout.write('Readed: ' + progress + '\r');
  });
  
}).catch(function(error){
  console.log(error);
  console.log('Error: Elasticsearch cluster is down!');
});


