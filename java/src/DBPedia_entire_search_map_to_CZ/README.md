VINF_1415_xsuta (Erik Suta)
===============

This is a simple search webapplication built on
parsed and indexed data from Slovak and Czech DBPedia
dump.

For more information and documentation, please visit:
http://vi.ikt.ui.sav.sk/User:Erik.Suta?view=home

Before launching application, import necessary RDF data
(.ttl format is preferred) from:
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/sk/
and
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/cs/

to
src/main/resources/rdf, resp. src/main/resources/rdf/cz.

To run application on embedded Jetty server, run test:
src/test/java/com/eriksuta/Start main().

Search is possible in application UI.

However, parsing and indexing processes can't be launched
directly from UI. To launch these processes, use tests
in file BasicTest:
performParseProcess() and performIndexProcess()


