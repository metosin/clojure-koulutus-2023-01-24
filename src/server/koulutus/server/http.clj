(ns koulutus.server.http
  (:require [ring.adapter.jetty :as jetty]
            [koulutus.server.simple-ring-handler :as simple])
  (:import (org.eclipse.jetty.server Server)))


(defn start-server [ds]
  (jetty/run-jetty simple/handler {:host  "0.0.0.0"
                                   :port  9080
                                   :join? false}))


(defn stop-server [^Server server]
  (when server
    (.stop server)))
