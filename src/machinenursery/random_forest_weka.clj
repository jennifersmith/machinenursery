(ns machinenursery.random-forest-weka
  (:import  (weka.classifiers.trees.RandomForest)) )

(use 'clojure.java.io)

(import 'weka.classifiers.trees.RandomForest)
(import 'weka.core.Instances)

(def random-forest
  (let [rf (new RandomForest)]
    (doto rf  (.buildClassifier (arff-me)))))



(defn arff-me []
  (with-open [arff (reader "data/arff.txt")]
    (doto  (new Instances arff) (.setClassIndex 4))))

