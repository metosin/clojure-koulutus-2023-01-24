(ns ex.ex-8-destructuring)

; Kohta johon tulisi symboli on kohta jossa voi destructuroida:

(let [x [1 2]]
  (+ (x 0) (x 1)))
;; => 3

(let [[a b] [1 2]]
  (+ a b))
;; => 3

(let [[a b] [1 2 3 4 5 6]]
  (+ a b))
;; => 3

(let [[a b] [1]]
  (println "a =" a)
  (println "b =" b))
;; tulostaa:
;; a = 1
;; b = nil

(let [[a b & more] [1 2 3 4 5 6]]
  (println "a =" a)
  (println "b =" b)
  (println "more =" more))
;; tulostaa:
;; a = 1
;; b = 2
;; more = (3 4 5 6)

; Ylläoleva on ns "sequential destructuring"
; Toinen destructurointi muoto on "associative destructuring"

(def album {:album    "Right On"
            :artist   "The Supremes"
            :released "1970-04-01"})

(let [{artist :artist} album]
  artist)
;; => "The Supremes"

(let [{artist   :artist
       released :released} album]
  (str artist ", " released))
;; => "The Supremes, 1970-04-01"

(let [{artist-name   :artist
       released-date :released} album]
  (str artist-name ", " released-date))
;; => "The Supremes, 1970-04-01"

(let [{:keys [artist released]} album]
  (str artist ", " released))
;; => "The Supremes, 1970-04-01"

; Lisää https://clojure.org/guides/destructuring
