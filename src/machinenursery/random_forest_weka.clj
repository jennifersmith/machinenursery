(ns machinenursery.random-forest-weka
  (:use machinenursery.boxing-people))

(defn to-attribute [key index]
  (new weka.core.Attribute (str key) index))

(defn create-instance [features]
  (let [instance (new weka.core.Instance (count features))]
    (for [feature features] 
      (.setValue instance (to-attribute (key feature) 0) (double (val feature))))
    instance))


