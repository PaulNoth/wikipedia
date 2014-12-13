# Suffix and root changes extraction from slovak Wikipedia

For more information and documentation, please visit: http://vi.ikt.ui.sav.sk/User:Michal.Blanarik?view=home
<br>
Before starting the application you will need Wikipedia dump from:<br>
http://dumps.wikimedia.org/skwiki/latest/skwiki-latest-pages-articles.xml.bz2 <br>
and you will need to rename this file “skwiki-latest-pages-articles.xml“<br>
You will need SimMetrics library http://sourceforge.net/projects/simmetrics/<br>
<br>
The file structure must be as follows:<br>
Root-->data--> skwiki-latest-pages-articles.xml<br>
<br>
Root->"any file name"-->src->InflectedFormMergeTest<br>
Root->"any file name"-->src->mergingInflectedFormNE<br>
Root->"any file name"-->src->gui<br>
Root->"any file name"-->src->testAndTools<br>
<br>
Root->"any file name"-->lib->"SimMetrics library"<br>
<br>
For jUnit test you will need these files:<br>
Root-->data-->skwiki_test_link_anchor_sample.txt<br>
Root-->data-->skwiki_test_merged_inflected_forms_sample.txt<br>
Root-->data-->skwiki_test_rootChanges_sample.txt<br>
Root-->data-->skwiki_test_suffixes_sample.txt<br>
