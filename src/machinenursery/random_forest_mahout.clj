(ns machinenursery.random-forest-mahout
  (:gen-class)
  (:import main.java.MahoutPlaybox)
  (:use machinenursery.core))

(defn create-descriptors []
  (->> (repeat (* 28 28) "N ")
      (cons "L ")
      (apply str)))

(defn strings
  "Casts to String[]"
  [xs] (into-array String xs))

(defn fix-up-test-data [line] (apply str (concat "-," line)))

(defn mahout-playbox-main-conversion []
(let [descriptors (create-descriptors)
      training-data (read-train-set-raw 1000)
      test-data (map fix-up-test-data (read-test-set-raw 10))]
  (MahoutPlaybox/runIteration 1
                              (strings training-data)
                              (strings test-data )
                              descriptors)))

(def number-of-trees 5)

(defn -main [& args] (MahoutPlaybox/main (strings [(str number-of-trees)])))