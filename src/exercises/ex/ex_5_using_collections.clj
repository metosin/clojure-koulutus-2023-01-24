(ns ex.ex-5-using-collections)

; Hyvä pitää käsillä:
;   https://clojure.org/api/cheatsheet

;;
;; Vectors:
;; ===================================================
;;

(conj [1 2] 3)
;; => [1 2 3]

(vector 1 2 3)
;; => [1 2 3]

(vec '(1 2 3))
;; => [1 2 3]

(assoc [1 2 3] 1 0)
;; => [1 0 3]

(subvec [1 2 3 4 5 6] 2 4)
;; => [3 4]

(def my-vector [1 2 3])

(conj my-vector 3)
;; => [1 2 3]

my-vector
;; => [1 2]


;;
;; Lists:
;; ===================================================
;;


; By default, lists are evaluated as function calls:

(+ 1 2 3)
;; => 6

; Use `quote` to stop evaluation

(quote (+ 1 2 3))
;; => (+ 1 2 3)

; Koska käytetään melko usein, tälle on oma reader-macro: ' (= yksöis-hipsu)

'(+ 1 2 3)
;; => (+ 1 2 3)

(count '(+ 1 2 3))
;; => 4

; Listan voi evaluoida:

(eval '(+ 1 2 3))
;; => 6

(conj '(1 2 3) '+)
;; => (+ 1 2 3)

(eval (conj '(1 2 3) '+))
;; => 6

(eval (conj '(1 2 3) '-))
;; => -4

(conj [1 2] 3)
;; => [1 2 3]
;;         ^
;;         vector kasvaa lopusta

(conj '(1 2) 3)
;; => (3 1 2)
;;     ^
;;     lista kasvaa alusta

(def my-list '(1 2))

(conj my-list 3)
;; => (3 1 2)

my-list
;; => (1 2)


;;
;; Maps:
;; ===================================================
;;


(def album {:album    "Right On"
            :artist   "The Supremes"
            :released "1970-04-01"})

(contains? album :released)
;; => true

(get album :released)
;; => "1970-04-01"

(contains? album :price)
;; => false

(get album :price)
;; => nil

(get album :price "$0.00")
;; => "$0.00"

(assoc album :price "$1.20")
;; => {:album    "Right On"
;;     :artist   "The Supremes"
;;     :released "1970-04-01"
;;     :price    "$1.20"}

(dissoc album :released)
;; => {:album  "Right On"
;;     :artist "The Supremes"}

album
;; => {:album    "Right On"
;;     :artist   "The Supremes"
;;     :released "1970-04-01"}

(contains? (assoc album :price "$1.20") :price)
;; => true

(def album-with-price (assoc album :price 52.50))

(update album-with-price :price (fn [v] (+ v 10)))
;; => {:album "Right On"
;;     :artist "The Supremes"
;;     :released "1970-04-01"
;;     :price 62.5}

(update album-with-price :price + 10)
;; => {:album "Right On"
;;     :artist "The Supremes"
;;     :released "1970-04-01"
;;     :price 62.5}

(defn parse-iso-date [date-str]
  (java.time.LocalDate/parse date-str java.time.format.DateTimeFormatter/ISO_DATE))

(parse-iso-date "2023-01-24")
;; => #object[java.time.LocalDate 0x8d0f159 "2023-01-24"]

(update album-with-price :released parse-iso-date)
;; => {:album "Right On"
;;     :artist "The Supremes"
;;     :released #object[java.time.LocalDate 0x7fbfbca6 "1970-04-01"]
;;     :price 52.5}

;; *-in versiot:
;;
;; assoc  -> assoc-in
;; dissoc -> dissoc-in
;; update -> update-in
;;
;; Ottavat avaimen sijasta seq avaimia, pureutuu sisäkkäiseen tietorakenteeseen
;; Näistä lisää myöhemmin.

(keys album)
;; => (:album :artist :released)

(vals album)
;; => ("Right On" "The Supremes" "1970-04-01")

(seq album)
;; => ([:album "Right On"]
;;     [:artist "The Supremes"]
;;     [:released "1970-04-01"])

(select-keys album [:album :artist])
;; => {:album "Right On"
;;     :artist "The Supremes"}


;;
;; Sets:
;; ===================================================
;;

(def genres #{"soul" "funk" "smooth soul"})

(conj genres "death metal")
;; => #{"funk" "soul" "smooth soul" "death metal"}

(disj genres "funk")
;; => #{"soul" "smooth soul"}

genres
;; => #{"funk" "soul" "smooth soul"}

(seq genres)
;; => ("funk" "soul" "smooth soul")

;;
;; Seqs
;; ===================================================
;;

(def my-seq (seq [1 2 3]))

(first my-seq)
;; => 1

(next my-seq)
;; => (2 3)

(first (next my-seq))
;; => 2

; rest ja next toimivat melkein samoin, mutta:

(next [])
;; => nil

(rest [])
;; => ()


;;
;; Yleistä kaikille tietorakenteille:
;; ===================================================
;;


(count [1 2 3])
;; => 3

(count '(1 2 3))
;; => 3

(count {:album    "Right On"
        :artist   "The Supremes"
        :released "1970-04-01"})
;; => 3


(count #{"soul" "funk" "smooth soul"})
;; => 3


(empty? [1 2 3])
;; => false
(empty? [])
;; => true


;;
;; Monet tietorakenteet ovat myös funktioita:
;; ===================================================
;;


(ifn? +)
;; => true

(ifn? [1 2 3])
;; => true

([1 2 3] 1)
;; => 2

(comment
  ([1 2 3] 10)
  ;; => Execution error (IndexOutOfBoundsException) at ex.ex-5-using-collections/eval40737 (REPL:186).
  ;;    null 
  )


;; HUOM: Lista ja seq ei ole funktioita:

(ifn? '(1 2 3))
;; => false

(ifn? (seq [1 2 3]))
;; => false

(nth [1 2 3] 1)
;; => 2

; Mäp on funktio avaimilleen:

(ifn? album)
;; => true

(album :released)
;; => "1970-04-01"

(album :price)
;; => nil

; Hyväksyy myös oletus arvon:

(album :price "$0.00")
;; => "$0.00"

;; Keyword on funktio mapille:

(ifn? :released)
;; => true

(:released album)
;; => "1970-04-01"

(:price album "$0.00")
;; => "$0.00"

; Set on funktio

(ifn? genres)

(genres "funk")
;; => "funk"

(genres "death metal")
;; => nil
