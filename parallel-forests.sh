#!/bin/bash 

start=`date`
numberOfRuns=$1

seq 1 ${numberOfRuns} | parallel -P 8 "./build-forest.sh"

end=`date`

echo "Started: ${start}"
echo "Finished: ${end}"
