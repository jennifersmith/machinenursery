#!/bin/bash

#lein run -m  machinenursery.random-forest-mahout $1
#java -Xmx1024m -cp target/machinenursery-1.0.0-SNAPSHOT-standalone.jar main.java.MahoutPlaybox $1 true false
ng main.java.MahoutPlaybox $1 true false
