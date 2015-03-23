#! /bin/bash

echo ""
echo "- - - - -"
echo ""
echo "Welcome to Disambiguation parser data preparation"
echo ""
echo "... working ..."

FILE="insert-filename-here.xml.bz2"

bzip2 -dc $FILE | tr "\n" "~" > rawdata
cat rawdata | tr "<" "\n" > rawdata_endls

rm -f rawdata

cat rawdata_endls | grep "^title>\|^text" | grep -A 1 "title>.*disambiguation" > disambiguation_contents.txt
cat disambiguation_contents.txt | sed 's/\[\[/\n[[/g' | sed 's/\]\]/]]\n/g' | grep "\[\[\|^title" | sed 's/|\(.*\)\]\]/]]/g' > disambiguation_links.txt
rm -f disambiguation_contents.txt
cat disambiguation_links.txt | tr "\n" "~" | sed 's/title/\ntitle/g' > disambiguation_lookup.txt
rm -f disambiguation_lins.txt

cat rawdata_endls | grep "^title\|^text" | cut -b 1-250 > pages_content.txt
rm -f rawdata_endls
cat pages_content.txt | grep "^title.*disambiguation" > disambiguations.txt

echo ""
echo "Done!"
echo ""
echo "- - - - -"
echo ""

exit
