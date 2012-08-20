(ns machinenursery.kd-tree)

(defn distance-between [fo1 fo2]
  (apply + (map #(* % %) (map - fo1 fo2))))

(defn do-it [points depth]
(if (empty? points)
  nil
  (let [k (count (first points))
        axis (mod depth k)
        points (vec  (sort-by #(get (vec %)  axis) points))
        median (quot (count points) 2)]
    (println {:axis axis :depth depth :k k})
    [ {:axis axis :location (vec (get points median))}
      (do-it (take median points) (inc depth) )
      (do-it (drop (inc median) points) (inc depth))] )))


(defn search [tree point] (search-it tree point nil))

(defn find-nearest-branch [[ {:keys [location axis]} left right] point]
  (println {:location location :axis axis :point point})
(if (< (get point axis) (get location axis)) left right))

(defn find-furthest-branch [[ {:keys [location axis]} left right] point]
  (println {:location location :axis axis :point point})
  (if (>= (get point axis) (get location axis)) left right))

(defn distance-axis [axis location point]
  (let [x1 (get location axis)
        x2 (get point axis)
        difference (* (- x1 x2) (- x1 x2))]
    difference))

(defn search-it [[{:keys [location axis]}
                  left right :as all] point best]
  (if
    (nil? all) best
    (let [best (or best location)
          best (if (< (distance-between location point)
                      (distance-between best point))
                 location
                 best)
          furthest-branch (find-furthest-branch all point)
          nearest-branch (find-nearest-branch all point)
          best (search-it nearest-branch point best)
          best (if
                   (< (distance-axis axis location point) (distance-between best point))
                   (search-it furthest-branch point best)
                   best )] best )))
