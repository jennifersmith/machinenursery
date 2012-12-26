(ns machinenursery.incanter
  (:gen-class)
  (:use (incanter core stats charts)))

(defn -main [& args]
  (view (histogram (sample-normal 1000))))
