(ns machinenursery.what-is-entropy)


(defn individual-entropy [x data-size]
  (let [p (float (/ x data-size))]
    (/ (* (* -1 p) (Math/log p)) (Math/log 2.0))))


(defn calculate-entropy [counts data-size]
  (if (= 0 data-size)
    0.0
    (reduce (fn [entropy x] (+ entropy (individual-entropy x data-size))) 0 counts)))

(defn calculate-entropy [counts data-size]
  (->>  counts
       (remove #{0})
       (map #(individual-entropy % data-size))
       (reduce +)))


(calculate-entropy [10 10] 20 ) ;; should be one
