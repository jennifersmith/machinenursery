rsync --progress --ignore-existing ~/Downloads/train.csv ~/Downloads/test.csv data/

head -n2000 data/train.csv > data/train_head.csv
tail -n2000 data/train.csv > data/train_tail.csv
head -n2000 data/test.csv > data/test_head.csv
tail -n2000 data/test.csv > data/test_tail.csv
