(ns koulutus.server.hugsql-music-handler
  (:require [hugsql.core :as hugsql]
            [hugsql.adapter.next-jdbc :as adapter]
            [muuntaja.core :as m]
            [ring.util.http-response :as resp]
            [reitit.ring :as ring]
            [reitit.coercion.malli]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.exception :as exception]
            [koulutus.server.db :as db]))


(declare sql-get-albums)
(declare sql-get-album-by-id)
(declare sql-get-album-tracks)
(declare sql-get-artists)
(declare sql-get-artist-by-id)

(hugsql/def-db-fns "sql/music.sql"
  {:adapter (adapter/hugsql-adapter-next-jdbc)})


(defn get-albums [request]
  (-> (sql-get-albums (-> request :ds)
                      {:album_name (-> request :parameters :query :album_name)
                       :limit      (-> request :parameters :query :limit)})
      (resp/ok)))


(defn get-album-by-id [request]
  (let [album_id (-> request :parameters :path :album_id)
        album    (sql-get-album-by-id (-> request :ds)
                                      {:album_id album_id})]
    (if album
      (resp/ok album)
      (resp/not-found {:error    "can't find album"
                       :album_id album_id}))))


(defn get-album-tracks [request]
  (let [album_id (-> request :parameters :path :album_id)
        tracks   (sql-get-album-tracks (-> request :ds)
                                       {:album_id album_id})]
    (if (seq tracks)
      (resp/ok tracks)
      (resp/not-found {:error    "can't find album tracks"
                       :album_id album_id}))))


(defn get-artists [request]
  (-> (sql-get-artists (-> request :ds)
                       {:artist_name (-> request :parameters :query :artist_name)
                        :limit       (-> request :parameters :query :limit)})
      (resp/ok)))


(defn get-artist-by-id [request]
  (-> (sql-get-artist-by-id (-> request :ds)
                            {:artist_id (-> request :parameters :query :artist_id)})
      (resp/ok)))


(def handler
  (ring/ring-handler
   (ring/router ["/api/music"
                 ["/album"
                  ["" {:get {:parameters {:query [:map
                                                  [:album_name {:default ""} :string]
                                                  [:limit {:default 100} :int]]}
                             :handler    get-albums}}]
                  ["/:album_id"
                   ["" {:parameters {:path [:map [:album_id :string]]}
                        :handler    get-album-by-id}]
                   ["/tracks" {:parameters {:path [:map [:album_id :string]]}
                               :handler    get-album-tracks}]]]
                 ["/artist"
                  ["" {:get {:parameters {:query [:map
                                                  [:artist_name {:default ""} :string]
                                                  [:limit {:default 100} :int]]}
                             :handler    get-artists}}]
                  ["/:artist_id" {:get     {:parameters {:path [:map [:artist_id :string]]}}
                                  :handler get-artist-by-id}]]]
                {:data {:coercion   (reitit.coercion.malli/create)
                        :muuntaja   m/instance ;; default content negotiation
                        :middleware [parameters/parameters-middleware
                                     muuntaja/format-negotiate-middleware
                                     muuntaja/format-response-middleware
                                     (exception/create-exception-middleware
                                      (merge exception/default-handlers
                                             {::exception/wrap (fn [handler ^Exception e request]
                                                                 (println "Error:" (.getMessage e) "\n" e)
                                                                 (handler e request))}))
                                     muuntaja/format-request-middleware
                                     coercion/coerce-response-middleware
                                     coercion/coerce-request-middleware]}})))


(comment

  (require '[koulutus.server.db :as db])
  (with-open [ds (db/make-datasource)]
    (-> (handler {:request-method :get
                  :uri            "/api/music/album"
                  :headers        {"Accepts" "application/json"}
                  :query-params   {"album_name" "foo"
                                   "limit"      "2"}
                  :ds             ds})
        :body
        (.readAllBytes)
        (String.)))

  (let [album_id "7080521d-28a0-4e39-8a44-526ae8981ccd"]
    (with-open [ds (db/make-datasource)]
      (some-> (handler {:request-method :get
                        :uri            (str "/api/music/album/" album_id "/tracks")
                        :headers        {"Accepts" "application/json"}
                        :ds             ds})
              :body
              (.readAllBytes)
              (String.))))

  (with-open [ds (db/make-datasource)]
    (some-> (handler {:request-method :get
                      :uri            (str "/api/music/artist")
                      :headers        {"Accepts" "application/json"}
                      :ds             ds})
            :body
            (.readAllBytes)
            (String.)))


  ;
  )

