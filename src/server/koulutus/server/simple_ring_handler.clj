(ns koulutus.server.simple-ring-handler
  (:require [next.jdbc :as jdbc]))


(defn ping-handler [req]
  (when (-> req :uri (= "/api/ping"))
    {:status  200
     :headers {"Content-Type" "text/plain"}
     :body    "Ping"}))


(defn time-handler [req]
  (when (-> req :uri (= "/api/time"))
    (let [ds (:ds req)
          rs (jdbc/execute-one! ds ["select now() as time"])]
      {:status 200
       :body   {:time (-> rs :time str)}})))


(def handler (some-fn ping-handler
                      time-handler))
