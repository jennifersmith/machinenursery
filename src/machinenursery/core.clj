(ns machinenursery.core)

(def foobar (slurp "train_mini.csv"))

(def barbar (string/split-lines foobar))

(defn doshit [s] (map #(string/split % #",") (string/split-lines s)))

(def whatever (doshit foobar))

(def test_mini (doshit (slurp "test_mini.csv")))

(def train_maxi (doshit (slurp "train_maxi.csv")))

(defn create-tuple [[ head & rem]]
  {:pixels (get-pixels rem) :label head})

(defn get-pixels [pix]
  (map #( Integer/parseInt %) pix))

(def fuck_off (first (map get-pixels test_mini)))

(defn diff-fuck-off [fo1 fo2] (Math/sqrt (apply + (map #(* % %) (map - fo1 fo2)))))

(def train_mini (map create-tuple whatever
                     ))
(def train_maxi (map create-tuple wh))
(def fucking_fives (map :pixels (take 2 (filter #(= "5" (:label %)) train_mini))))
(def fucking_twos (map :pixels (take 2 (filter #(= "2" (:label %)) train_mini))))

(def just_one_fucking_two (first fucking_twos))

(defn find-distances [training-set test-vector] (map (fn [{:keys [label pixels]}] {:label label :distance (diff-fuck-off test-vector pixels)}) training-set))

(defn k-nearest [k training-set test-vector]
  (take k
        (sort-by :distance
                 (find-distances training-set test-vector))))
(defn predict-me-bitch [wannabe-nearest]
  (ffirst
   (reverse (sort-by #(count (val %)) (group-by :label wannabe-nearest)))))

(defn predict-it [test_set test_vector ]
  (predict-me-bitch
   (k-nearest 5 test_set test_vector)))
(defn dump-it [answers]
  (apply str (map println-str answers)))

(def answers (map #(vector %1 (predict-it train_mini %2)) numberz pixelz))

(def answers-awesome (pmap #(vector %1 (predict-it train_mini %2)) numberz pixelz))

(use 'clojure.java.io)
(defn stuff [answers]
  (with-open [wrtr (writer "answers.out" :append true)]
    (doseq [answer answers]
      (.write wrtr (println-str answer)))))
