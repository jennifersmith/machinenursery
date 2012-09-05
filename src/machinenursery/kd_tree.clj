(ns machinenursery.kd-tree)

(defn distance-between [fo1 fo2]
  (apply + (map #(* % %) (map - fo1 fo2))))

(defn do-tree [points depth]
(if (empty? points)
  nil
  (let [k (count (:pixels (first points)))
        axis (mod depth k)
        points (vec  (sort-by #(get (vec (:pixels %))  axis) points))
        median (quot (count points) 2)
        median-data (get points median)]
    [ {:axis axis :data
       (assoc  median-data :pixels (vec (:pixels median-data)))}
      (do-tree (take median points) (inc depth) )
      (do-tree (drop (inc median) points) (inc depth))] )))


(defn find-nearest-branch [[ {:keys [data axis]} left right] point]
(if (< (get point axis) (get (:pixels data) axis)) left right))

(defn find-furthest-branch [[ {:keys [data axis]} left right] point]
  (if (>= (get point axis) (get (:pixels data) axis)) left right))

(defn distance-axis [axis location point]
  (let [x1 (get location axis)
        x2 (get point axis)
        difference (* (- x1 x2) (- x1 x2))]
    difference))

(defn search-it [[{:keys [data axis]} left right :as all] point best]
  (let [{:keys [pixels]} data]   (if
                                     (nil? all) best
                                     (let [best (or best data)
                                           best (if (< (distance-between pixels point)
                                                       (distance-between (:pixels best) point))
                                                  data
                                                  best)
                                           furthest-branch (find-furthest-branch all point)
                                           nearest-branch (find-nearest-branch all point)
                                           best (search-it nearest-branch point best)
                                           best (if
                                                    (< (distance-axis axis pixels point) (distance-between pixels point))
                                                  (search-it furthest-branch point best)
                                                  best )] best ))))

(defn search [tree point] (search-it tree point nil))
