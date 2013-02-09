#!/bin/bash 

start=`date`
startTime=`date '+%s'`
numberOfRuns=$1
numberOfTrees=$2

seq 1 ${numberOfRuns} | parallel -P 2 "./build-forest.sh $2"

end=`date`
endTime=`date '+%s'`

echo "Started: ${start}"
echo "Finished: ${end}"
echo "Took: " $(expr $endTime - $startTime)
