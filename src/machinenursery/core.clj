(ns machinenursery.core (:require [clojure.string :as string])
(:use clojure.java.io machinenursery.k-nearest-neighbours))

(defn parse [reader]
  (drop 1
        (map #(string/split % #",") (line-seq reader))))


(defn get-pixels [pix]
  (map #( Integer/parseInt %) pix))

(defn create-tuple [[ head & rem]]
  {:pixels (get-pixels rem) :label head})

(defn dump-it [answers]
  (apply str (map println-str answers)))

(defn parse-test-set [reader] (map get-pixels (parse reader)))
(defn parse-train-set [reader] (map create-tuple (parse reader)))

(defn do-it []
  (with-open
      [test-set-rdr  (reader "test.csv")
       train-set-rdr (reader "train.csv")]
    (let [test-set (parse-test-set test-set-rdr)
          train-set (take 2000 (parse-train-set train-set-rdr))
          k-nearest (create-k-nearest 5 train-set)]
      (vec (map k-nearest (take 10 test-set))))))