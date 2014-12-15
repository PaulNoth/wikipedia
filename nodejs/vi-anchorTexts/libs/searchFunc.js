'use strict';

var Q = require('q');
var elasticsearch = require('elasticsearch');

// run elasticsearch client
var client = new elasticsearch.Client({
  host: 'localhost:9200',
  log: 'error'
});

// check if elasticsearch is running
function checkES() {
  var deferred = Q.defer();
  client.ping({
    requestTimeout: 1000,
    hello: 'elasticsearch!'
  }, function (error) {
    if (error) {
      // if it's not§
      console.error('Error: Elasticsearch cluster is down!');
      deferred.reject(error);
    } else {
      // if runs
      deferred.resolve();
    }
  });
  return deferred.promise;
}

// search for 
function search(query, index) {
  var deferred = Q.defer();
  client.search({
    index: index,
    size: 10,
    body: {
      query: {
        match: {
          "anchor": {
            "query": query,
            "operator": "and"
          }
        }
      },
      "aggs": {
        "group_by_empty": {
          "missing" : { "field" : "text" }
        },
        "group_by_text": {
          "terms": {
            "field": "text"
          }
        }
      }
    }
  }, function (error, response) {
    deferred.resolve(response);
  });
  return deferred.promise;
}

module.exports = {
  checkES : checkES,
  search: search
}