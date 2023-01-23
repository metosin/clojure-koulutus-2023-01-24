(ns koulutus.server.http
  (:require [ring.adapter.jetty :as jetty]
            [koulutus.server.simple-ring-handler :as simple]
            [koulutus.server.simple-music-handler :as music])
  (:import (org.eclipse.jetty.server Server)))


(defn start-server [ds]
  (jetty/run-jetty music/handler {:host  "0.0.0.0"
                                  :port  9080
                                  :join? false}))


(defn stop-server [^Server server]
  (when server
    (.stop server)))
