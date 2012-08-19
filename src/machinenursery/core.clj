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
      [test-set-rdr  (reader "data/test_head.csv")
       train-set-rdr (reader "data/train_head.csv")]
    (let [test-set (parse-test-set test-set-rdr)
          train-set (take 2000 (parse-train-set train-set-rdr))
          k-nearest (create-k-nearest 5 train-set)]
      (vec (map k-nearest (take 10 test-set))))))

;; run it with n rows of the training vector to see if it is right
;; taking training data from the bottom. Use copy_files.sh to create the files
(defn test-it [n]
  (with-open [result-writer (writer "output/test_results.log")
              train-set-rdr (reader "data/train_head.csv")
              train-set-tail-rdr (reader "data/train_tail.csv")]
    (let [train-set (parse-train-set train-set-rdr)
          train-set-tail (parse-train-set train-set-tail-rdr)
          k-nearest (create-k-nearest 5 train-set)]
      (time (doseq [test-vector (take n train-set-tail)]
              (let [result (k-nearest (:pixels test-vector))]
                (doto result-writer
                  (.write (str (:label test-vector) "," result "\n" ))
                  (.flush))))))))
