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

(def my-vector [1 2 3])

my-vector
;; => [1 2 3]

(conj my-vector 4)
;; => [1 2 3 4]

my-vector
;; => [1 2 3]


;;
;; List:
;; -----------------------------------
;;


(type '(1 2 3))
;; => clojure.lang.PersistentList

(instance? java.util.List '(1 2 3))
;; => true

(def my-list '(1 2 3))

my-list
;; => (1 2 3)

(conj my-list 4)
;; => (4 1 2 3)
;;     ^
;;     \------ Huom paikka, vertaa `(conj 4 [1 2 3])`

my-list
;; => (1 2 3)


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

(get album "released")
;; => "1970-04-01"

(assoc album "price" "$1.20")
;; => {"album" "Right On",
;;     "artist" "The Supremes",
;;     "released" "1970-04-01",
;;     "price" "$1.20"}


;; Tyypillisesti map avaimet on keywordejä:

(def album {:album    "Right On"
            :artist   "The Supremes"
            :released "1970-04-01"})


album
;; => {"album" "Right On",
;;     "artist" "The Supremes", 
;;     "released" "1970-04-01"}


;;
;; Set:
;; -----------------------------------
;;


(def genres #{"soul" "funk" "smooth soul"})

genres
;; => #{"funk" "soul" "smooth soul"}

(conj genres "doom metal")
;; => #{"funk" "doom metal" "soul" "smooth soul"}

(disj genres "funk")
;; => #{"soul" "smooth soul"}

genres
;; => #{"funk" "soul" "smooth soul"}


;;
;; Seq
;; ------------------------------------
;;

(def my-seq (seq genres))

(first my-seq)
;; => "funk"

(rest my-seq)
;; => ("soul" "smooth soul")

(first (rest my-seq))
;; => "soul"

(rest (rest my-seq))
;; => ("smooth soul")

(rest (rest (rest my-seq)))
;; => ()

; rest ja next toimivat melkein samoin, mutta:

(rest [])
;; => ()

(next [])
;; => nil

