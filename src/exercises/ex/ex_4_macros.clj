(ns ex.ex-4-macros)

;;
;;A language is homoiconic if a program written in it can be manipulated as data using 
;; the language, and thus the program's internal representation can be inferred just by 
;; reading the program itself. 
;;
;; This property is often summarized by saying that the language treats "code as data" . 
;;

(type '(1 2 3))
;; => clojure.lang.PersistentList


(type '(println "Hello, world!"))
;; => clojure.lang.PersistentList


(def my-list '(println))
(def my-list-2 (cons "Hello, world!" my-list))

my-list-2
;; => ("Hello, world!" println)

(reverse my-list-2)
;; => (println "Hello, world!")

(eval (reverse my-list-2))
;; => nil
;; prints: Hello, world!

;; Example: thread macros

(def duration-ms 420000)

(str (/ (/ duration-ms 1000) 60) " mins")
;; => "7 mins"

(-> duration-ms
    (/ 1000)
    (/ 60)
    (str " mins"))
;; => "7 mins"

(macroexpand-1 '(-> duration-ms
                    (/ 1000)
                    (/ 60)
                    (str " mins")))
;; => (str (/ (/ duration-ms 1000) 60) " mins")

(def albums)
