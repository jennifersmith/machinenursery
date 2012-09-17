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
          k-nearest (create-k-nearest 5 train-set)
          test-vectors (take n train-set-tail)
          results (map
                   vector
                   (map :label test-vectors)
                   (pmap k-nearest (map :pixels test-vectors)))]
      (time (doseq [result results]
              (doto result-writer
                (.write (apply str (interpose "," result)))
                (.write "\n")
                (.flush)))))))

(defn read-train-set [n]
  (with-open [train-set-rd (reader "data/train.csv")]
    (vec (take n (parse-train-set train-set-rd)))))

(defn read-test-set [n]
  (with-open [train-set-rd (reader "data/test.csv")]
    (vec (take n (parse-test-set train-set-rd)))))
