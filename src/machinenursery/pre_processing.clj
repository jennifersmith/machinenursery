(ns machinenursery.pre-processing
  (:use machinenursery.core)
  (:use incanter.io)
  (:use incanter.core)
  (:require incanter.stats))

(defn tuples [rows]
  (map create-tuple rows))

(defn calculate-variances [rows]
  (map incanter.stats/variance (apply map vector (map :pixels rows))))

(defn parse-row [row]
  (map #(clojure.string/split % #",") row))

(def parsed-rows
  (tuples (parse-row (read-train-set-raw 42000))))

(defn data [] (read-dataset "data/train.csv" :header true))


(defn pixels-to-remove []
  (->> parsed-rows
       (calculate-variances)
       (map vector (range 0 784))
       (filter #(= 0.0 (get % 1)))
       (map first)))

(comment
  (map vector (range 1 784) (calculate-variances (take 5 parsed-rows))))

(defn -main []
  (sel (data) :rows [1 2 3]))