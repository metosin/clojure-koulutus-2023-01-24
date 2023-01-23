(ns ex.ex-10-atoms)


; Koko software-transactional-memory on laaja käsite ja tämän koulutuksen
; ulkopuolella. Frontti koodauksessa kuitenkin tarvitaan userin `atom`
; rakennetta.

; https://clojure.org/reference/atoms

(def state (atom 0))

state
;; => #atom[0 0x6ec50af7]

(deref state)
;; => 0

; Reader macro @ = deref

@state
;; => 0

; Tilan muuttaminen reset! funktiolla:

(reset! state 42)

@state
;; => 42

; Tilan muuttaminen swap! funktiolla

(swap! state (fn [old-state] (* old-state 3)))
;; => 126

@state
;; => 126

(swap! state * 3)
;; => 378

@state
;; => 378


