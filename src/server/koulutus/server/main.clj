(ns koulutus.server.main
  (:require [koulutus.server.db :as db]
            [koulutus.server.http :as http]))


(defn -main [& _args]
  (println "Server starting...")
  (let [ds (db/make-datasource)]
    (http/start-server ds))
  (println "Server running"))
