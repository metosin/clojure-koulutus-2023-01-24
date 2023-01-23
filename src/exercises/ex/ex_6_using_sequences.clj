(ns ex.ex-6-using-sequences
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clj-http.client :as http]
            [jsonista.core :as json]
            [ex.ex-2-collections]
            [ex.ex-6-using-sequences.geo :as geo]))


;;
;; filter, map, reduce
;;


(range 10)
;; => (0 1 2 3 4 5 6 7 8 9)

(->> (range 10)
     (filter (fn [v] (odd? v))))
;; => (1 3 5 7 9)


; Yleinen käyttö tapaus:
;   - Muista, set on funktio
;   - comp yhdistää yhdistelee funktioita:

(def some-users [{:name "a"
                  :role :user}
                 {:name "b"
                  :role :admin}
                 {:name "c"
                  :role :clerk}
                 {:name "d"
                  :role :admin}])

(def admin-roles #{:admin :super-user :god})

(admin-roles :super-user)
;; => :super-user

(admin-roles :user)
;; => nil

; Pikainen kysymys: mikä on totta?

(if true
  "Yes"
  "No")
;; => "Yes"

(if 42
  "Yes"
  "No")
;; => "Yes"

(if ""
  "Yes"
  "No")
;; => "Yes"

(if []
  "Yes"
  "No")
;; => "Yes"

;; Paljon helmpomi tarkastella, mikä EI ole totta.

(if false
  "Yes"
  "No")
;; => "No"

(if nil
  "Yes"
  "No")
;; => "No"

;; KAIKKI muu kuin `false` ja `nil` on "truthy"

; Jatkletaan admin esimerkillä:

(def admin? (comp admin-roles :role))
;; (admin? x) on sama kuin (admin-roles (:role x))

(admin? {:name "a"
         :role :user})
;; => nil
(admin? {:name "a"
         :role :super-user})
;; => :super-user

(filter admin? some-users)
;; => ({:name "b", :role :admin} 
;;     {:name "d", :role :admin})


;;
;; map
;;  - map funktio, ei Map tietotyyppi
;;


(defn square [v]
  (* v v))

(->> (range 10)
     (map square))
;; => (0 1 4 9 16 25 36 49 64 81)

(->> (range 10)
     (filter odd?)
     (map square))
;; => (1 9 25 49 81)

;;
;; reduce
;;

;       redusointi funktio
(reduce (fn [acc v]
          (println "acc:" acc "v:" v)
          (+ acc v))
        ; alku arvo       
        0
        ; seq arvoja jotka halutaan redusoida 
        (range 10))
;; => 45
;; prints:
;; acc: 0 v: 0
;; acc: 0 v: 1
;; acc: 1 v: 2
;; acc: 3 v: 3
;; acc: 6 v: 4
;; acc: 10 v: 5
;; acc: 15 v: 6
;; acc: 21 v: 7
;; acc: 28 v: 8
;; acc: 36 v: 9

; (reduce + 1 [2 3 4])
; => (+ (+ (+ 1 2) 3) 4)
; => (-> 1
;        (+ 2)
;        (+ 3)
;        (+ 4))

; Jos reducelle antaa vain kaksi argumenttiä, otetaan alku
; arvo sequensesta

; (reduce + [2 3 4])
; => (-> 2
;        (+ 3)
;        (+ 4))

;; All together:

(->> (range 10)
     (filter (fn [v] (odd? v)))
     (map (fn [v] (square v)))
     (reduce (fn [acc v] (+ acc v))
             0))
;; => 165

(->> (range 10)
     (filter odd?)
     (map square)
     (reduce +))
;; => 165


;;
;; Otetaan realistisempi esimerkki:
;;


;; Haetaan esimerkki dataa HTTP yli
;; https://randomuser.me/documentation


(def user-response
  (http/get "https://randomuser.me/api/"
            {:query-params {"results" 10
                            "inc"     "name,location,email"}}))

(type user-response)
;; => clojure.lang.PersistentHashMap

(keys user-response)
;; => (:status
;;     :headers
;;     :body
;;     ...

(:status user-response)
;; => 200

(:headers user-response)
;; => {"Content-Type"  "application/json; charset=utf-8"
;;     "Date"          "Mon, 23 Jan 2023 10:32:03 GMT"
;;     ...

(:body user-response)
;; => "{\"results\":[{\"name\":{\"title\":\"Mr\",\"first\":\"Eli\", ...

(defn parse-json [json-str]
  (json/read-value json-str json/keyword-keys-object-mapper))

(parse-json (:body user-response))
;; => {:info {:page 1, ...
;;     :results [{:email "vemund.roberg@example.com"
;;                :name {:title "Mr", :first "Vemund", :last "Roberg"}
;;                ...

(def users (:results (parse-json (:body user-response))))

(def users (-> user-response
               :body
               parse-json
               :results))
users
;; => [{:email "eli.kim@example.com"
;;      :name {:title "Mr", :first "Eli", :last "Kim"}
;;      :location {...
;;     {:email "sofia.larsen@example.com"
;;      :name {:title "Ms", :first "Sofia", :last "Larsen"}
;;      :location {...
;;     ...

;;
;; Tarvoite: Listaa kaikki ei-miesten nimet niin että jokainen nimi on "sukunimi, etunimi"
;;

;; Vaihe 1: Poistetaan `users` listasta ne joiden :title on "Mr"

(defn get-user-title [user]
  (:title (:name user)))

(get-user-title {:name {:title "Prof"}})
;; => "Prof"

(defn user-is-not-mr? [user]
  (not= "Mr" (get-user-title user)))

(->> users
     (filter user-is-not-mr?))
;; => ({:email "sofia.larsen@example.com"
;;      :name {:title "Ms", :first "Sofia", :last "Larsen"}
;;      :location {...
;;     {:email "enni.laakso@example.com"
;;      :name {:title "Ms" :first "Enni" :last  "Laakso"}
;;      :location {...
;;     ...

;; Sama kuin yllä, mutta tiiviimmässä muodossa:

(def user-is-not-mr? (comp (partial not= "Mr")
                           :title
                           :name))

;; Nyt meillä on kaikki käyttäjät joiden :title ei ole "Mr"

;; Vaihe 2: Otetaan käyttäjien tiedoista vain :name osa:

(->> users
     (filter user-is-not-mr?)
     (map :name))
;; => ({:title "Ms", :first "Sofia", :last "Larsen"}
;;     {:title "Ms", :first "Enni", :last "Laakso"}
;;     {:title "Mrs", :first "Loiva", :last "Moreira"} 
;;     ...

;; Vaihe 3: Otetaan näistä :last ja :first ja yhdistetään ne:

(->> users
     (filter user-is-not-mr?)
     (map :name)
     (map (fn [user-name]
            (str (:last user-name) ", " (:first user-name)))))
;; => ("Larsen, Sofia"
;;     "Laakso, Enni"
;;     "Moreira, Loiva"
;;     ...

;; Mission acomplished!


;; 
;; Special bonus:
;; ==========================================================================
;;
;; Käyttäjien tiedoissa on myös käyttäjän sijainti koordinaatit.

(map (comp :coordinates :location) users)
;; => ({:longitude "-98.0051"
;;      :latitude "42.5550"}
;;     {:longitude "93.4561"
;;      :latitude "-77.2053"}
;;     ...
;;
;; Hae kaikki käyttäjät jotka on enintään x km etäisyydellä tästä paikasta.
;; Helpotuksena todettakoon että `ex.ex-5-using-collections.geo` nimiavaruudessa
;; on funktio jolla voi laskea kahden pisteen välisen etäisyyden kilometreinä.
;;

(geo/geo-dist geo/karvaamokuja
              {:longitude -98.0051
               :latitude  42.5550})
;; => 7467.113625478214

;;
;; Special bonus 2:
;;
;; https://adventofcode.com/2022/day/1
;;
;; Docker kontissa valmiina palvelu joka tuottaa lähtö datan:
;;

(defn get-enery-seq []
  (->> (http/get "http://localhost:9000/api/advent-of-code" {:as           :stream
                                                             :query-params {"seed" 123}})
       :body
       (io/reader)
       (line-seq)))

(get-enery-seq)
;; => ("2763"
;;     ""
;;     "8884"
;;     "9970"
;;     ...


(comment

  (defn parse-long-line [line]
    (if (str/blank? line)
      nil
      (Long/parseLong line)))

  (defn get-max-energy [inputs]
    (->> inputs
         (map parse-long-line)
         (partition-by nil?)
         (filter (fn [p] (not= p '(nil))))
         (map-indexed (fn [pos v]
                        [pos (reduce + v)]))
         (sort-by second >)
         (first))))


(comment
  (defn get-max-energy [inputs]
    (->> inputs
         (map parse-long-line)
         (reduce (fn [acc v]
                   (if (some? v)
                     (update acc :energy + v)
                     (if (> (:energy acc) (:max-energy acc))
                       (-> acc
                           (assoc :energy 0)
                           (update :position inc)
                           (assoc :max-energy   (:energy acc))
                           (assoc :max-position (:position acc)))
                       (-> acc
                           (assoc :energy 0)
                           (update :position inc)))))
                 {:energy       0
                  :position     0
                  :max-energy   0
                  :max-position 0})
         ((juxt :max-position :max-energy)))))

(comment
  (->> (get-enery-seq)
       (get-max-energy))
  ;; => [11 58592]
  )
