(ns ex.ex-2-collections)

;;
;; Vector:
;; -----------------------------------
;;

(type [1 2 3])
;; => clojure.lang.PersistentVector

(instance? java.util.List [1 2 3])
;; => true

[1 "hello" true [:a :b :c] 42N]
;; => [1 "hello" true [:a :b :c] 42N]


;;
;; List:
;; -----------------------------------
;;


(type '(1 2 3))
;; => clojure.lang.PersistentList

(instance? java.util.List '(1 2 3))
;; => true


;;
;; Map:
;; -----------------------------------
;;


(def album {"album"    "Right On"
            "artist"   "The Supremes"
            "released" "1970-04-01"})

(type album)
;; => clojure.lang.PersistentArrayMap

(instance? java.util.Map album)
;; => true

(map? album)
;; => true

;;
;; Set:
;; -----------------------------------
;;


(def genres #{"soul" "funk" "smooth soul"})

genres
;; => #{"funk" "soul" "smooth soul"}

(set? genres)
;; => true

(instance? java.util.Set genres)
;; => true


;;
;; Seq
;; ------------------------------------
;;

(def my-seq (seq genres))

(first my-seq)
;; => "funk"

(rest my-seq)
;; => ("soul" "smooth soul")

(seq [])
;; => nil

(seq "Hello")
;; => (\H \e \l \l \o)
