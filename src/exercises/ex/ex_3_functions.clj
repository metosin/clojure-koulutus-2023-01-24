(ns ex.ex-3-functions)


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


;;
;; Funktioiden yhdisteleminen
;;

; comp

(defn add-one [v] (+ v 1))
(defn make-double [v] (* v 2))

(make-double (add-one 2))
;; => 6

(def add-one-and-double (comp make-double add-one))

(add-one-and-double 2)
;; => 6

; partial

(def multiply-by-2 (partial * 2))

(multiply-by-2 3)
;; => 6

; juxt

(def parse-lat-lon (juxt :latitude :longitude))

(parse-lat-lon {:continent "Europe"
                :latitude  60.214444
                :longitude 24.880800
                :timezone  "GMT+02:00"})
;; => [60.214444 24.8808]

(->> (range 10)
     (map (juxt identity pos? neg? odd? even?)))
;; => ([0 false false false true] 
;;     [1 true false true false] 
;;     [2 true false false true] 
;;     [3 true false true false]
;;     ...

; some-fn

(defn handle-ping [request]
  (when (= (:uri request) "/api/ping")
    {:status 200
     :body   "Pong"}))

(defn handle-time [request]
  (when (= (:uri request) "/api/time")
    {:status 200
     :body   (str "time is " (System/currentTimeMillis))}))

(defn not-found [request]
  {:status 404
   :body   (str "No route for " (:uri request))})

(def handler (some-fn handle-ping handle-time not-found))

(handler {:uri "/api/ping"})
;; => {:status 200, :body "Pong"}

(handler {:uri "/api/foo"})
;; => {:status 404, :body "No route for /api/foo"}

;; apply

; Ongelma: funktio ottaa erillisiä parametrejä, kuten esim str
;          mutta meillä on seq
; Eli haluaisimme kutsua:
;   (str 1 2 3 4)
; mutta:

(str [1 2 3 4])
;; => "[1 2 3 4]"

(apply str [1 2 3 4])
;; => "1234"

; Lisää: https://clojure.org/guides/higher_order_functions
