(ns ex.ex-5-using-collections
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]
            [clj-http.client :as http]
            [jsonista.core :as json]
            [ex.ex-2-collections])
  (:import (java.time LocalDate Month)
           (java.time.format DateTimeFormatter)))


;; Haetaan esimerkki dataa HTTP yli

(defn parse-json [json-str]
  (json/read-value json-str json/keyword-keys-object-mapper))


(defn parse-date [date-str]
  (LocalDate/parse date-str DateTimeFormatter/ISO_LOCAL_DATE))


(comment

  (->> (http/get "http://localhost:9000/api/music/albums"
                 {:query-params {"limit" 200000}
                  :as           :stream})
       :body
       (io/reader)
       (line-seq)
       (map parse-json)
       (map (fn [album]
              (-> album
                  (update :genres set)
                  (update :released parse-date))))
       (filter (fn [{:keys [genres released]}]
                 (and (genres "rock")
                      (= (.getYear released) 1978)
                      (= (.getMonth released) Month/OCTOBER))))
       (map (fn [album]
              (select-keys album [:name :artist_name])))
       pprint)

  ;
  )