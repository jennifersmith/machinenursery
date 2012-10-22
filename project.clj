(defproject machinenursery "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :main "MahoutPlaybox"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [random-forests-clj "0.2.0"]
                 [nz.ac.waikato.cms.weka/weka-stable "3.6.6"]   
                 [org.apache.mahout/mahout-core "0.7"]
                 [org.apache.mahout/mahout-math "0.7"]]
  :plugins [[lein-swank "1.4.4"]]
  :jvm-opts ["-Xmx2g"]
  :java-source-paths ["src/main/java"] )
