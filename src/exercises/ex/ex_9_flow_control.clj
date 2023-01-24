(ns ex.ex-9-flow-control)


(if (odd? 42)
  "That's strange"
  "All is good")

; Clojure `if` on expression

(println (if (odd? 42)
           "That's strange"
           "All is good"))
; Tulostaa:
; All is good

; Clojuressa sallittu, mutta yleisesti linterit ei tykkää:

(if (odd? 42)
  "That's strange")
;; => nil

; Sama, mutta lintteri tykkää:

(when (odd? 42)
  "That's strange")
;; => nil

; cond

(let [x 15]
  (cond
    (< x 5) "Less than 5"
    (> x 10) "More than 10"))
;; => "More than 10"

(let [x 10]
  (cond
    (< x 5) "Less than 5"
    (> x 10) "More than 10"))
;; => nil


(let [x 10]
  (cond
    (< x 5) "Less than 5"
    (> x 10) "More than 10"
    :else "Something else"))
;; => "Something else"

;;
;; try/catch/finally
;;

(try
  (/ 1 0)
  (catch java.io.IOException e
    (str "io error: " (.getMessage e)))
  (catch java.lang.ArithmeticException e
    (str "bad math:" (.getMessage e)))
  (finally
    (println "it's finally over")))
;; prints: it's finally over
;; => "bad math:Divide by zero"

