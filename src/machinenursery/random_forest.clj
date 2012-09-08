(ns machinenursery.random-forest (:use random-forests.core))


(defn make-devop []
  {:talking (rand-int 4)
   :coding (+ 3 (rand-int 5))
   :debugging (+ 6 (rand-int 4))
   :analysing (rand-int 3)
   :presenting (+ 2 (rand-int 8))
   :classification :devop})

(defn make-dev []
  {:talking (+ 4  (rand-int 4))
   :coding (+ 5 (rand-int 5))
   :debugging (+ 3 (rand-int 3))
   :analysing (+ 2 (rand-int 4))
   :presenting (+ 0 (rand-int 5))
   :classification :dev})

(defn make-ba []
  {:talking (+ 6  (rand-int 4))
   :coding (+ 0 (rand-int 2))
   :debugging 0
   :analysing (+ 6 (rand-int 4))
   :presenting (+ 7 (rand-int 3))
   :classification :ba})


(defn make-qa []
  {:talking (+ 6  (rand-int 4))
   :coding (+ 3 (rand-int 7))
   :debugging (+ 3 (rand-int 5))
   :analysing (+ 6 (rand-int 2))
   :presenting (+ 6 (rand-int 3))
   :classification :qa})



(def people (shuffle (concat
                      (take 200 (repeatedly make-qa))
                      (take 200 (repeatedly make-dev))
                      (take 200 (repeatedly make-ba))
                      (take 200 (repeatedly make-devop)))))

(def people-tuples (map #(vec (vals %)) people))

(def features1 (set (list (feature "talking" 0 :categorical)
                         (feature "coding" 1 :categorical)
                         (feature "debugging" 2 :categorical)
                         (feature "analysing" 3 :categorical)
                         (feature "presenting" 4 :categorical))))

(def examples (apply list people-tuples))


(defn make-forest [] (build-random-forest examples features1 5 200))

(defn make-pixel-features []
  (set (map #(feature (str "pixel" %) % :categorical)   (range 784))) )

(defn make-nursery-forest [examples feature-size sample-size]
  (build-random-forest examples (make-pixel-features) feature-size sample-size))

(defn make-data-nursery-ready [data]
  (map #(conj (vec (:pixels %)) (:label %)) data) )