#!/bin/bash

echo ""

#params check - if no param provided nothing to lookup, program exit
if [ $# -lt 1 ]
then
	echo "Nedostatočný počet parametrov"
	exit
fi

#lookup whether requested disambiguation page exists - if not program exit
DISAMB_EXISTS=`grep "^title>$1 (disambiguation)$" disambiguations.txt | wc -l`

if [ $DISAMB_EXISTS -eq 0 ]
then
	echo "Stránka nebola nájdená"
	exit
fi

# Page has been found
echo "The result for: $1 (disambiguation)"
echo ""

# Preparing list of all refs to subpages
cat disambiguation_lookup.txt | grep "^title>$1 (disambiguation)~" | tr "~" "\n" > tmp0

#For each link search for corresponding content
while read line2
do
 #if no-lookup switch only links are displayed instead of content
 if [ "$2" == "--no-lookup" ]
 then
 	echo "$line2" | grep "\[\[" | sed 's/\[\[\(.*\)\]\]/\1/g'
 #otherwise content lookup is executed
 else
	#prepare page name
	PAGE_NAME=`echo "$line2" | grep "\[\[" | sed 's/\[\[\(.*\)\]\]/\1/g'`
	VALID=`echo $PAGE_NAME | wc -c`
	if [ $VALID -gt 1 ]
	then
		echo ""
		echo "$PAGE_NAME"
		echo ""
		#look for page in pre-prepared file and print out formatted well
		grep -A 1 -m 1 "title>$PAGE_NAME$" pages_content.txt | tail -n 1 | cut -d ">" -f 2 | tr "~" "\n"
		echo "- - - - -"
	fi

 fi
done < tmp0
#remove temporary file created at runtime
rm tmp0

exit
