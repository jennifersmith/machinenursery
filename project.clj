(defproject machinenursery "1.0.0-SNAPSHOT"
  :description "Jen and Mark try to learn machine learning"
  :main "MahoutPlaybox"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [random-forests-clj "0.2.0"]
                 [nz.ac.waikato.cms.weka/weka-stable "3.6.6"]
                 [org.apache.mahout/mahout-core "0.7"]
                 [org.apache.mahout/mahout-math "0.7"]
                 [org.codehaus.groovy/groovy "1.7-beta-2"]
                 [edu.washington.cs.knowitall/morpha-stemmer "1.0.4"]
                 [edu.stanford.nlp/stanford-corenlp "1.3.3"]
                 [org.apache.lucene/lucene-snowball "3.0.3"]
                 [org.yawni/yawni-wordnet-api "2.0.0-SNAPSHOT"]
                 [org.yawni/yawni-wordnet-data30 "2.0.0-SNAPSHOT"]
                 [org.slf4j/slf4j-jdk14 "1.6.6"]
                 [com.google.guava/guava "13.0.1"]
                 [org.encog/encog-core "3.1.0"]
                 [cc.mallet/mallet "2.0.7-RC2"]]
                 [criterium "0.3.0"]]

  :plugins [[lein-swank "1.4.4"]]
  :jvm-opts ["-Xmx2g"]
  :java-source-paths ["src/main/java"] )
