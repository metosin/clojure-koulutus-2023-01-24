(ns koulutus.server.simple-music-handler
  (:require [clojure.java.io :as io]
            [clj-http.client :as http]
            [jsonista.core :as json]
            [ex.ex-2-collections]))


(defn parse-json [json-str]
  (json/read-value json-str json/keyword-keys-object-mapper))


(defn GET [uri handler]
  (fn [request]
    (when (and (-> request :request-method (= :get))
               (-> request :uri (= uri)))
      (let [body (handler request)]
        {:status  200
         :headers {"Content-Type" "application/json"}
         :body    (json/write-value-as-string body)}))))


(def handler (some-fn (GET "/api/music/albums"
                        (fn [request]
                          (->> (http/get "http://localhost:9000/api/music/albums" {:as :stream})
                               :body
                               (io/reader)
                               (line-seq)
                               (map parse-json)
                               (into []))))
                      (GET "/api/music/artists"
                        (fn [request]
                          (->> (http/get "http://localhost:9000/api/music/artists" {:as :stream})
                               :body
                               (io/reader)
                               (line-seq)
                               (map parse-json)
                               (into []))))
                      (constantly {:status  404
                                   :headers {"Content-Type" "application/json"}
                                   :body    "{\"error\": \"Not found\"}"})))


(comment
  (-> (handler {:request-method :get
                :uri            "/api/music/albums"})
      :body)

  (->> (http/get "http://localhost:9000/api/music/albums" {:as :stream})
       :body
       (io/reader)
       (line-seq)
       (map parse-json)
       (into []))
  ;
  )