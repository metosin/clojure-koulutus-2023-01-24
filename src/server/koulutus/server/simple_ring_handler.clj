(ns koulutus.server.simple-ring-handler
  (:require [next.jdbc :as jdbc]
            [jsonista.core :as json]))


(defn ping-handler [req]
  (when (-> req :uri (= "/api/ping"))
    {:status  200
     :headers {"Content-Type" "text/plain"}
     :body    "Ping"}))


(defn time-handler [req]
  (when (-> req :uri (= "/api/time"))
    (let [ds (-> req :ds)
          rs (jdbc/execute-one! ds ["select now() as time"])]
      {:status 200
       :body   {:time (-> rs :time str)}})))


(def not-found-handler
  (constantly {:status  404
               :headers {"Content-Type" "text/plain"}
               :body    "The will is strong, but I'm unable to serve you"}))


(def api-handler (some-fn ping-handler
                          time-handler
                          not-found-handler))




(defn wrap-json [handler]
  (fn [req]
    (let [resp (handler req)]
      (if (-> resp :headers (contains? "Content-Type"))
        resp
        (-> resp
            (update :headers assoc "Content-Type" "application/json")
            (update :body json/write-value-as-string))))))

(def handler (-> api-handler
                 (wrap-json)))