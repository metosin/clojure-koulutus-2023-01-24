(ns ex.ex-7-java-interop
  (:import (java.time LocalDate)
           (java.time.format DateTimeFormatter)))


; Constructor

(new java.util.Random 42)
;; => #object[java.util.Random 0x57b1f60b "java.util.Random@57b1f60b"]

(java.util.Random. 42)
;; => #object[java.util.Random 0x24c42afb "java.util.Random@24c42afb"]

; Call method:

(let [random (java.util.Random. 42)]
  (.nextInt random))
;; => -1170105035

(let [random (java.util.Random. 42)]
  (.nextInt random 100))
;; => 30

; Static method:

(Long/parseLong "42")
;; => 42

(LocalDate/now)
;; => #object[java.time.LocalDate 0x8d5ecd9 "2023-01-23"]

(.format (LocalDate/now) DateTimeFormatter/ISO_DATE)
;; => "2023-01-23"
