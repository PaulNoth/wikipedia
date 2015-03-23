// Author: Adam Mockor
// Dump: 'data/sample_anchors_skwiki_latest_pages_articles.xml'
// vi-anchorTexts

var chai = require('chai');
var expect = chai.expect;
var Q = require('q');

chai.config.includeStack = false;

var parser = require('../libs/parseXMLfunc.js');
var search = require('../libs/searchFunc.js');

var file = '../../data/sample_anchors_skwiki_latest_pages_articles.xml';

describe("Check if Elastisearch is running", function() {
  it('Elastisearch is running', function() {
    return parser.checkES('testindex').then(function(bool){
      expect(bool).to.be.true;
    });
  });
});

describe("Index test anchors", function() { 
  it('Indexed', function() {
    return parser.indexAnchors(file,'testindex').then(function(counter){
      expect(counter).to.be.above(0);
    });
  });
});

describe("Search anchor", function() { 
  it('Should find just one anchor: STU', function() {
    return search.search('STU', 'testindex').then(function(response){
      expect(response.hits.total).to.be.equal(1);
    });
  });
});

describe("Check anchor text", function() { 
  it('Should find anchor text "FEI" for anchor: Fakulta elektrotechniky a informatiky Slovenskej technickej univerzity', function() {
    return search.search('Fakulta elektrotechniky a informatiky Slovenskej technickej univerzity', 'testindex').then(function(response){
      expect(response.hits.hits[0]._source.text).to.be.equal('FEI');
    });
  });
});
