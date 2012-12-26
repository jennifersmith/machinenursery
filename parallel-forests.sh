#!/bin/bash 

start=`date`
startTime=`date '+%s'`
numberOfRuns=$1

seq 1 ${numberOfRuns} | parallel -P 8 "./build-forest.sh"

end=`date`
endTime=`date '+%s'`

echo "Started: ${start}"
echo "Finished: ${end}"
echo "Took: " $(expr $endTime - $startTime)
