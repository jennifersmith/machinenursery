(ns machinenursery.core
  (:require [clojure.string :as string])
  (:use incanter.stats)
  (:use clojure.java.io ))

(defn parse [reader]
  (drop 1 (map #(string/split % #",") (line-seq reader))))


(defn get-pixels [pix] (map #( Integer/parseInt %) pix))

(defn create-tuple [[ head & rem]] {:pixels (get-pixels rem) :label head})

(defn dump-it [answers] (apply str (map println-str answers)))


(defn parse-test-set [reader] (map get-pixels (parse reader)))
(defn parse-train-set [reader] (map create-tuple (parse reader)))

(defn read-raw [path n]
  (with-open [reader (reader path)] (vec (take n (rest  (line-seq reader))))))


(defn read-train-set [n]
  (map create-tuple
       (with-open [train-set-rd (reader "data/train.csv")]
         (vec (take n (parse-train-set train-set-rd))))))

(defn read-test-set [n]

  (with-open [train-set-rd (reader "data/test.csv")]
    (vec (take n (parse-test-set train-set-rd)))))

(def read-train-set-raw  (partial read-raw "data/train.csv"))
(def read-test-set-raw (partial read-raw "data/test.csv" ))

(defn parse [row]
  (map #(clojure.string/split % #",") row))

(defn tuples [rows]
  (map create-tuple rows))

(defn calculate-variances [rows]
  (map incanter.stats/variance (apply map vector (map :pixels rows))))

(def parsed-rows
  (tuples (parse (read-train-set-raw 42000))))


(def pixels-to-remove
  (->> parsed-rows
       (calculate-variances)
       (map vector (range 0 784))
       (filter #(= 0.0 (get % 1)))
       (map first)))

(map vector (range 1 784) (calculate-variances (take 5 parsed-rows)))




