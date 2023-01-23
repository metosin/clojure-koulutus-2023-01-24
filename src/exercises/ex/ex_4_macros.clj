(ns ex.ex-4-macros)

;;
;;A language is homoiconic if a program written in it can be manipulated as data using 
;; the language, and thus the program's internal representation can be inferred just by 
;; reading the program itself. 
;;
;; This property is often summarized by saying that the language treats "code as data" . 
;;

(def my-list '("Hello, world!" println))

my-list
;; => ("Hello, world!" println)

(reverse my-list)
;; => (println "Hello, world!")

(eval (reverse my-list))
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

; Hyvä idea muidenkin mielestä: https://github.com/tc39/proposal-pipeline-operator

(macroexpand-1 '(-> duration-ms
                    (/ 1000)
                    (/ 60)
                    (str " mins")))
;; => (str (/ (/ duration-ms 1000) 60) " mins")

; source: https://clojuredocs.org/clojure.core/defmacro#example-542692d2c026201cdc326f7a

(defmacro unless [pred a b]
  `(if (not ~pred) ~a ~b))

(if (pos? -1) "Is pos" "Is not pos")
;; => "Is not pos"

(unless (pos? -1) "Is pos" "Is not pos")
;; => "Is pos"

; JavaScript string template literals:
; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Template_literals
;
; In JavaScript:
; let a = 1
; let b = 2
; `sum is ${a + b}`

(defmacro stl [s]
  (let [m     (doto (re-matcher #"\$\{([^}]+)\}" s)
                (.find))
        parts (loop [out   []
                     start 0]
                (let [out (conj out
                                (subs s start (.start m))
                                (read-string (.group m 1)))
                      end (.end m)]
                  (if (.find m)
                    (recur out end)
                    out)))]
    `(str ~@parts)))

(let [a 1
      b 2]
  (stl "sum is ${(+ a b)}"))
;; => "sum is 3"


(macroexpand-1 '(stl "sum is ${(+ a b)}"))
;; => (clojure.core/str "sum is " (+ a b))

(macroexpand-1 '(comment "This is fine"))
;; => nil



;;
;; Yhteenveto:
;;

(-> {:name     "Clojure"
     :invented {:by "Rich Hikey"
                :in 2006}
     :paradigm :fp}
    (assoc :coolnes "level-9000")
    (assoc-in [:invented :at] "NC, USA")
    (update-in [:invented :in] inc)
    (dissoc :paradigm))
;; => {:name "Clojure"
;;     :invented {:by "Rich Hikey"
;;                :in 2007
;;                :at "NC, USA"}
;;     :coolnes "level-9000"}

(->> (range 0 20)
     (map (fn [v] (* v v)))
     (filter odd?)
     (partition-all 3))
;; => ((1 9 25) (49 81 121) (169 225 289) (361))
