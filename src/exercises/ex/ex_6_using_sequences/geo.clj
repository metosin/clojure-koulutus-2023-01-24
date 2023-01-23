(ns ex.ex-6-using-sequences.geo
  (:require [clojure.math :refer [to-radians sin cos acos]]))

;;
;; Algoritmin lähde:
;; https://github.com/Xtrimmer/javabook/blob/master/src/chapter_04/PE_04_02_Geometry_great_circle_distance.java
;;

(defn geo-dist [point-1 point-2]
  (let [x1     (to-radians (-> point-1 :latitude))
        y1     (to-radians (-> point-1 :longitude))
        x2     (to-radians (-> point-2 :latitude))
        y2     (to-radians (-> point-2 :longitude))
        radius 6371.01]
    ; double d = radius * Math.acos(Math.sin(x1) * Math.sin(x2) + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2))
    (* radius (acos (+ (* (sin x1)
                          (sin x2))
                       (* (cos x1)
                          (cos x2)
                          (cos (- y1 y2))))))))


(def karvaamokuja {:latitude  60.214444
                   :longitude 24.880800})


(def metosin-tampere {:latitude  61.498028
                      :longitude 23.767014})


(geo-dist karvaamokuja metosin-tampere)
;; => 154.94332884635352
