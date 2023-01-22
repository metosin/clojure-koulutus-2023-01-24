(ns ex.ex-1-scalar-data-types)

; Tutustutaan perus tietotyyppeihin:

(type 1)
;; => java.lang.Long

(type 1.0)
;; => java.lang.Double

(type "Hello")
;; => java.lang.String

(type \x)
;; => java.lang.Character

(type true)
;; => java.lang.Boolean

; nil on sama kuin `null`

(type nil)
;; => nil

(type 1N)
;; => clojure.lang.BigInt

(type 1.0M)
;; => java.math.BigDecimal

(type #"\d+")
;; => java.util.regex.Pattern

;
; Ratio:
;
; harvoin käytetään, mutta saattaa puraista jos ei ole tietoinen.
;

(type (/ 1 2))
;; => clojure.lang.Ratio

(/ 2 3)
;; => 2/3

(/ 2.0 3.0)
;; => 0.6666666666666666

(double (/ 2 3))
;; => 0.6666666666666667

;; Keyword

:max-power
;; => :max-power

(type :max-power)
;; => clojure.lang.Keyword

;; Symbols

; Symbol evaluoituu aina siten että sille etsitään arvo nimi-avaruudesta. Tästä lisää
; myöhemmin.
; 
; Tässä vaiheessa riittää kun todetaan että evaluoinnin voi pusäyttää yksöis-hipsulla:

'this-is-a-symbol
;; => this-is-a-symbol

(type 'this-is-a-symbol)
;; => clojure.lang.Symbol


;; this-is-a-symbol
;; ;; => Syntax error compiling at (src/server/koulutus/server/ex/scalar_data_types.clj:0:0).
;; ;;    Unable to resolve symbol: this-is-a-symbol in this context

println
;; => #function[clojure.core/println]

(type println)
;; => clojure.core$println

(type 'println)
;; => clojure.lang.Symbol

