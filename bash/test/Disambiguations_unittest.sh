#!/bin/bash

diff sample_output.xml $1 | tee test_output.diff

LINES=`cat test_output.diff | wc -l`
if [ $LINES -eq 0 ] ; then
 echo "The given file fully matches the sample output" > test_output.diff
 echo -e "[ \e[0;32mOK\e[0m ] The given file fully matches the sample output"
fi
