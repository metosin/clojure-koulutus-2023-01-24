(ns ex.ex-3-functions
  (:require [ex.ex-2-collections]))


(type println)
;; => clojure.core$println

println
;; => #function[clojure.core/println]

(println "Hello, world!")
;; => nil
;; consolissa: Hello, world!

(+ 1 2)
;; => 3

(* 1 2 3 4 5)
;; => 120

(< 1 2)
;; => true

(< 1 13 21 42)
;; => true

(def my-function (fn [your-name]
                   (printf "Hello, %s!\n" your-name)))

#_(my-function)
;; => Execution error (ArityException) at ex.ex-3-functions/eval37152 (REPL:35).
;;    Wrong number of args (0) passed to: ex.ex-3-functions/my-function

(my-function "Jarppe")
;; => nil

(defn my-function [your-name]
  (printf "Hello, %s!\n" your-name))

;;
;; In computer science, functional programming is a programming paradigm 
;; where programs are constructed by applying and composing functions.
;;
;; In functional programming, functions are treated as first-class citizens, 
;; meaning that they can be bound to names (including local identifiers), passed 
;; as arguments, and returned from other functions, just as any other data type can. 
;;
;; This allows programs to be written in a declarative and composable style, where
;; small functions are combined in a modular manner.
;;
;;   https://en.wikipedia.org/wiki/Functional_programming
;;

; fn?  - onko argumentti määritelty (fn) tai (defn) formeilla?
; ifn? - onko argumentti funktio

(fn? +)
;; => true
(ifn? +)
;; => true

ex.ex-2-collections/album
;; => {:album "Right On"
;;     :artist "The Supremes"
;;     :released "1970-04-01"}


(fn? ex.ex-2-collections/album)
;; => false
(ifn? ex.ex-2-collections/album)
;; => true

(ex.ex-2-collections/album :artist)
;; => "The Supremes"

(ex.ex-2-collections/album :price)
;; => nil

(ex.ex-2-collections/album :price "$0.00")
;; => "$0.00"

(ifn? :artist)
;; => true

(:artist ex.ex-2-collections/album)
;; => "The Supremes"

