var fs = require('fs');
var Xml = require('xml-stream');
var Q = require('q');
var S = require('string');
var elasticsearch = require('elasticsearch');

// run elasticsearch client
var client = new elasticsearch.Client({
  host: 'localhost:9200',
  log: 'error'
});

// check if elasticsearch is running

function checkES(index) {
  var deferred = Q.defer();
  client.ping({
    requestTimeout: 1000,
    hello: 'elasticsearch!'
  }, function (error) {
    if (error) {
      // if it's not
      deferred.reject(error);
    } else {
      // if runs      
      client.indices.exists({index: index}, function(err, response){
        if (response) {
          // if index exists
          deferred.resolve(true);
        } else {
          // if not create index with mappings
          client.indices.create({
            index: index,
            type: 'page',
            body : {
              "mappings": {
                "page": {
                  "properties" : {
                    "anchor" : {
                        "type" : "string",
                        "index" : "not_analyzed" 
                    },
                    "text" : {
                        "type" : "string",
                        "index" : "not_analyzed" 
                    }
                  }
                }
              }
            }
          }, function(err, response){
            if (err) deferred.reject(err);
            deferred.resolve(true);
          });
        }
      });
    }
  });
  return deferred.promise;
}


function indexAnchors(file, index) {
  var deferred = Q.defer();
  var counter = 0;
  var pages = [];

  // create xml stream
  var stream = fs.createReadStream(file);
  var xml = new Xml(stream);

  var bulk = [];
  var anchors;
  var anchor;
  var anchorArr;
  var anchorText;
  var fullAnchor;

  //on page element
  xml.on('endElement: page', function(item) {
    
    // check if there is text in text element 
    // and match '[[]]' pattern to find anchors
    if (item.revision.text.$text) {
      anchors = item.revision.text.$text.match(/\[\[(.*?)\]\]/gm);
    } else if (typeof item.revision.text === 'string') {
      anchors = item.revision.text.match(/\[\[(.*?)\]\]/gm);
    } else { anchors = null; }
    
    // get anchor and anchor text from anchors
    if (anchors) {
      for ( var i=0; i < anchors.length; i++ ) {
        
        fullAnchor = anchors[i].slice(2,anchors[i].length - 2);
        anchorArr = fullAnchor.split('|');
        
        // if there is anchortext
        anchorText = (anchorArr.length > 1) ? anchorArr[1] : '';
        anchor = {
          anchor : anchorArr[0],
          text: anchorText,
          page: item.title
        };
        
        // push anchor to page
        bulk.push({ index:  { _index: index, _type: "page", _id: counter + '-' + i} });
        bulk.push(anchor);
        anchor = null;
      }
    }
    anchors = null;
    
    // count pages
    counter++;
    // every 1000 page send bulk insert to ES
    if (counter % 1000 === 0) {
      client.bulk({
        body: bulk
      }, function (err, resp) {
        if (err) deferred.reject(err);
      });
      bulk = [];
      deferred.notify(counter);
    }

  });

  // on end of the stream
  xml.on('end', function() {
    // send rest of pages to ES
    client.bulk({
      body: bulk
    }, function (err, resp) {
      if (err) deferred.reject(err);
      // return how many pages was indexed
      deferred.resolve(counter);
    });
    
  });

  return deferred.promise;
}


module.exports = {
  checkES : checkES,
  indexAnchors: indexAnchors
};