(ns koulutus.server.http
  (:require [ring.adapter.jetty :as jetty]
            [jsonista.core :as json]
            [koulutus.server.simple-ring-handler :as simple]
            [koulutus.server.simple-music-handler :as simple-music]
            [koulutus.server.hugsql-music-handler :as hugsql-music])
  (:import (org.eclipse.jetty.server Server)))


(defn wrap-json [handler]
  (fn [req]
    (let [resp (handler req)]
      (cond
        (nil? resp)
        nil

        (-> resp :headers (contains? "Content-Type"))
        resp

        :else
        (-> resp
            (update :headers assoc "Content-Type" "application/json")
            (update :body json/write-value-as-string))))))


(defn wrap-datasource [handler ds]
  (fn [req]
    (handler (assoc req :ds ds))))


(def not-found-handler
  (constantly {:status  404
               :headers {"Content-Type" "text/plain"}
               :body    "The will is strong, but I'm unable to serve you"}))


(defn start-server [ds]
  (jetty/run-jetty (-> (some-fn (wrap-json simple/handler)
                                (wrap-json simple-music/handler)
                                hugsql-music/handler
                                not-found-handler)
                       (wrap-datasource ds))
                   {:host  "0.0.0.0"
                    :port  9080
                    :join? false}))


(defn stop-server [^Server server]
  (when server
    (.stop server)))
