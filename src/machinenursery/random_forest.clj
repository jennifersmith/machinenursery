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


(defn make-forest [m k] (build-random-forest examples features1 m k))

(defn make-pixel-features []
  (set (map #(feature (str "pixel" %) % :categorical)   (range 784))) )

(defn make-nursery-forest [examples feature-size sample-size]
  (build-random-forest examples (make-pixel-features) feature-size sample-size))

(defn make-data-nursery-ready [data]
  (map #(conj (vec (:pixels %)) (:label %)) data) )

;; random defs
;; (def train-set (read-train-set 2000))
;;(def train-set-ready (make-data-nursery-ready train-set))
;; (def forr (make-nursery-forest train-set-ready 150 200))



;; so [[0 1]] => 1 mean classification error
(defn mean-classification-error
  "measures l1 loss from forest evaluation"
  [evaluation]
  (println
   (count evaluation))
  (->> evaluation
       (map (fn [[a b]] (if (= (last a) (first b)) 0 1)))
       (avg)
       (float)))

;; if you have a forest forr this is gonna combine all the "evals" - so that all the guesses are together
;; (def allevals (reduce (partial merge-with concat)  (map (comp :eval meta) (take 10 forr))))

;; finds the most popular one
(defn best [guesses]
  (key (first (reverse (sort-by val (frequencies guesses))))))

;; this will take the allevals thingy from above and find the 'best' guess
;; (def scored (map #(vector (last (key %)) (best (val %))) allevals ))