(ns machinenursery.pre-processing
  (:use machinenursery.core)
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io]))

(defn tuples [rows]
  (map create-tuple rows))

(defn parse-row [row]
  (map #(clojure.string/split % #",") row))

(def parsed-rows
  (tuples (parse-row (read-train-set-raw 42000))))

(defn data [] (take 5 parsed-rows))

(defn in? 
  "true if seq contains elm"
  [seq elm]  
  (some #(= elm %) seq))

(def one (first (take 1 (data))))

(def dead-to-us-pixels
  [0 1 2 3 4 5 6 7 8 9 10 11 16 17 18 19 20  21  22  23  24  25  26  27 28 29  30 31 52 53 54 55 56 57 82 83 84 85 111 112 139 140 141 168 196 392 420 421 448 476 532 560 644 645 671 672 673 699 700 701 727 728 729 730 731 754 755 756 757 758 759 760 780 781 782 783])

(defn dead-to-us? [pixel-with-index]
  (in? dead-to-us-pixels (first pixel-with-index)))

(defn remove-unwanted-pixels [row]
  (let [new-pixels
        (->> row :pixels (map-indexed vector) (remove dead-to-us?) (map second))]
    {:pixels new-pixels :label (:label row)}))

(defn remove-unwanted-pixels-test [row]
  (->> row (map-indexed vector) (remove dead-to-us?) (map second)))

(defn to-file-format [row]
  (let [formatted-pixels
        (apply str (interpose "," (:pixels row)))]
    (str (:label row) "," formatted-pixels)))

(defn to-test-file-format [row]
  (let [formatted-pixels
        (apply str (interpose "," row))]
    (str formatted-pixels)))

;;(map (comp to-file-format remove-unwanted-pixels) (take 2 (data)))

(defn write-to-file [file-path coll]
  (spit file-path (apply str (vec (interpose "\n" coll)))))

(defn write-to-file-big [file-path coll]
  (with-open [wrt (io/writer file-path)]
    (doseq [line coll]
      (.write wrt (str line "\n")))))

(defn split-on-comma [line]
  (string/split line #","))

(defn clean-train-file []
  (with-open [rdr (clojure.java.io/reader "data/train.csv")
              wrt (clojure.java.io/writer "/tmp/huge.csv")]
    (doseq [line (drop 1 (line-seq rdr))]
      (let [line-with-removed-pixels
             ((comp to-file-format remove-unwanted-pixels create-tuple split-on-comma) line)]
        (.write wrt (str line-with-removed-pixels "\n"))))))

(defn clean-test-file []
  (with-open [rdr (clojure.java.io/reader "data/test.csv")
              wrt (clojure.java.io/writer "/tmp/huge-test.csv")]
    (doseq [line (drop 1 (line-seq rdr))]
      (let [line-with-removed-pixels
             ((comp to-test-file-format remove-unwanted-pixels-test get-pixels split-on-comma) line)]
        (.write wrt (str line-with-removed-pixels "\n"))))))


(comment (defn -main [] (clean-train-file)))

(defn read-train-file []
  (with-open [rdr (clojure.java.io/reader "data/train.csv")]
    (map (comp create-tuple split-on-comma) (vec (drop 1 (line-seq rdr))))))

(defn -main []
  (with-open [wrt (clojure.java.io/writer "/tmp/blah.txt")]
    (doseq [line parsed-rows]
      (let [line-without-pixels (to-file-format (remove-unwanted-pixels line))]
        (.write wrt (str line-without-pixels "\n"))))))

(comment  (defn -main []
            (with-open [wrt (clojure.java.io/writer "/tmp/huge-file.csv")]
              (doseq [line (read-train-file)]
                (let [line-with-removed-pixels (to-file-format (remove-unwanted-pixels line))]
                  (.write wrt (str line-with-removed-pixels "\n")))))))

(defn parse-train-set-me [reader]
  (let [lines (parse reader)]
    (map #((do (print %1) (create-tuple %1))) lines)))

(defn read-train-set-me [n]
  (map create-tuple
       (with-open [train-set-rd (clojure.java.io/reader "data/train.csv")]
         (vec (take n (parse-train-set-me train-set-rd))))))

(comment  (defn -main [](read-train-set-me 1)))

(defn all-the-data []
  (with-open [train-set-rd (clojure.java.io/reader "data/train.csv")]
    (vec (line-seq train-set-rd))))

;;; (defn -main [] (map identity (all-the-data)))

;;(spit "/tmp/new-train.txt" (apply str (vec (interpose "\n" (map remove-unwanted-pixels (take 2 (data)))))))

