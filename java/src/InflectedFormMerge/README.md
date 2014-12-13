# Suffix and root changes extraction from slovak Wikipedia

For more information and documentation, please visit: http://vi.ikt.ui.sav.sk/User:Michal.Blanarik?view=home

Before starting the application you will need Wikipedia dump from http://dumps.wikimedia.org/skwiki/latest/skwiki-latest-pages-articles.xml.bz2 
and you will need to rename this file “skwiki-latest-pages-articles.xml“
You will need SimMetrics library http://sourceforge.net/projects/simmetrics/

The file structure must be as follows:
Root-->data--> skwiki-latest-pages-articles.xml

Root-><any file name>-->src->InflectedFormMergeTest
Root-><any file name>-->src->mergingInflectedFormNE
Root-><any file name>-->src->gui
Root-><any file name>-->src->testAndTools

Root-><any file name>-->lib-><SimMetrics library>

For jUnit test you will need these files:
Root-->data-->skwiki_test_link_anchor_sample.txt
Root-->data-->skwiki_test_merged_inflected_forms_sample.txt
Root-->data-->skwiki_test_rootChanges_sample.txt
Root-->data-->skwiki_test_suffixes_sample.txt
