(ns machinenursery.k-mean)
(use 'machinenursery.core)

(def all-the-data (read-train-set 1000))
(def even-more-data (read-train-set 40000))
(def test-data (read-test-set 30000))

(def labels (map :label all-the-data))

(defn find-me-all-the [number]
  (filter #(= (str number) (:label %)) even-more-data))

(defn mean [& v]
  (float 
   (/ (apply + v) (count v) )))

(def threes  (find-me-all-the 3))

(defn awesomeify [awesome]
  (apply map mean (map :pixels awesome)) )


(defn jen-is-right [idiot]
  (doall (map println  (partition 28 idiot))))


(def all-the-averages
  (map vector (range 0 10) (map #(awesomeify (find-me-all-the %)) (range 0 10))))

(defn distance-between [fo1 fo2]
         (Math/sqrt (apply + (map #(* % %) (map - fo1 fo2)))))

(defn which-am-i [unranked-value]
  (let [all-the-things
        (map #(vector (first %1) ( distance-between (second %1) unranked-value)) all-the-averages)]
    [(ffirst (sort-by second all-the-things)) all-the-things]))





;; (spit "/tmp/wow.txt" (apply str (vec (interpose "\n" (take 30000 (map first shiz))))))
;; (def shiz (map which-am-i test-data))
