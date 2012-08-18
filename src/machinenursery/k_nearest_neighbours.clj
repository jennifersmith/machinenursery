(ns machinenursery.k-nearest-neighbours)



(defn distance-between [fo1 fo2]
         (Math/sqrt (apply + (map #(* % %) (map - fo1)))))

(defn find-distances [training-set test-vector]
  (map (fn [{:keys [label pixels]}]
         {:label label :distance (distance-between test-vector pixels)})
       training-set))

(defn find-k-nearest [k training-set test-vector]
  (take k
        (sort-by :distance
                 (find-distances training-set test-vector))))

(defn select-best [candidates]
  (ffirst
   (reverse (sort-by #(count (val %)) (group-by :label candidates)))))


(defn create-k-nearest [k training-set]
     (fn [test-vector]
       (let [k-nearest (find-k-nearest k training-set test-vector)]
         (select-best k-nearest)
         )))
