(ns machinenursery.core
  (:require [clojure.string :as string])
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






