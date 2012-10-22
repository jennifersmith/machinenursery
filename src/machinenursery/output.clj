(ns machinenursery.output
  (:import (java.awt.Color)
           (java.io.File)
           (javax.imageio.ImageIO)) )

(defn gray-scale [pixels]
  (map #(.getRGB (new java.awt.Color % % %)) (map #(- 255 %) pixels)))00

;; definitely not right!
(defn rows-and-cols [count]
  (let [rows (int (Math/sqrt count))
        remaining (- count (* rows rows))]
    [rows (if (zero? remaining) rows (inc rows) )]
))
(defn write-image [xoffset yoffset image-file image-data]
  (doall (map #(.setRGB image-file (first %1) (second %1) %2)
               (for [
                     y (range (* 28 yoffset) (* 28 (inc yoffset)))
                     x (range (* 28 xoffset) (* 28 (inc xoffset)))]
                 [x y])
               (gray-scale (:pixels image-data)))))

(defn save-image [filename & image-data]
(let [
      [rows cols] (rows-and-cols (count image-data))
      image-file
      (new BufferedImage (* cols 28) (* rows 28) BufferedImage/TYPE_INT_ARGB)]
  (doall
   (map
    #(doall (write-image (first %1) (second %1) image-file %2 ))
    (for [ y (range rows) x (range cols)] [x y])
    image-data))

  (ImageIO/write  image-file "png" (new File filename))
  image-file))

;;(def train-set (read-train-set 1000))
;;(apply save-image "data/eek.png" train-set )
