(ns machinenursery.k-mean)
(use 'machinenursery.core)

(def all-the-data (read-train-set 1000))
(def even-more-data (read-train-set 40000))
(def test-data (read-test-set 30000))

;;(def labels (map :label all-the-data))

(defn find-me-all-the [number]
  (filter #(= (str number) (:label %)) even-more-data))

(defn find-me-all-the [number]
  (filter #(= (str number) (:label %)) all-the-data))

(defn mean [& v]
  (float 
   (/ (apply + v) (count v) )))

(def threes  (find-me-all-the 3))

(defn awesomeify [awesome] (apply map mean (map :pixels awesome)) )

(defn jen-is-right [idiot]
  (doall (map println  (partition 28 idiot))))

(def all-the-averages
  (map vector (range 0 10) (map #(awesomeify (find-me-all-the %)) (range 0 10))))

(defn distance-between [fo1 fo2]
         (Math/sqrt (apply + (map #(* % %) (map - fo1 fo2)))))

(defn foo [x]  (+ 1 2))

(defn which-am-i [unranked-value]
  (let [all-the-things
        (map #(vector (first %1) (distance-between (second %1) unranked-value)) all-the-averages)]
    [(ffirst (sort-by second all-the-things)) all-the-things]))


;; (spit "/tmp/wow.txt" (apply str (vec (interpose "\n" (take 30000 (map ast shiz))))))
(def shiz (map which-am-i test-data))

(defn which-am-i [unranked-value]
  (let [all-the-things (map #(vector (first %1) (distance-between (second %1) unranked-value)) all-the-averages)
        top-two (take 2 (sort-by second all-the-things))
        difference-between-top-two (Math/abs (apply - (map second top-two)))
        very-close (< difference-between-top-two 50)
        best-one (if false (ffirst (shuffle top-two)) (ffirst top-two))]
    [best-one top-two very-close]))

(defn blah [prediction row]
  { :prediction prediction
    :actual (:label row)
    :correct (= (first prediction) (Integer/parseInt (:label row)))})

(def accuracy (map blah training-attempt even-more-data))

(def comparisons
  (filter (fn [comparison] (= false (:correct comparison)))
          accuracy))

(def prediction-actual
  (map (fn [x] [(first (:prediction x)) (:actual x)]) comparisons))

(def grouped-predictions
  (group-by identity prediction-actual))

(def grouped-predictions-count
  (map (fn [x] { :key (key x) :size (count (val x))})
       grouped-predictions))

(def top-mistakes
  (reverse (sort-by :size grouped-predictions-count)))

(defn diff-me [[_ top-two _]]
  (Math/abs (apply - (map second top-two))))

(defn how-wrong-were-we [predictions]
  (map (fn [x] (diff-me (:prediction x))) predictions))


(def training-attempt (map which-am-i (map :pixels even-more-data)))

(defn mean [vals]
  (/ (apply + vals)  (count vals)))

;; looking at % in training set
;;(count (filter (fn [x] (not (:correct x))) accuracy))
;; 8110
;; (count accuracy)
;; 40000
;; Where the answer was in the top two

(count (filter (fn [x] (= (Integer/parseInt (:row x)) (first (second (second (:prediction x))))))   (filter (fn [x] (not (:correct x))) accuracy)))
