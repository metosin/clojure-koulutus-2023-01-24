(ns koulutus.server.db
  (:require [next.jdbc.result-set :as rs]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [koulutus.server.db-setup])
  (:import (com.zaxxer.hikari HikariDataSource)))


(def db-spec {:dbtype   "postgres"
              :host     (or (System/getenv "DB_HOST") "localhost")
              :port     5432
              :username "postgres"
              :password "postgres"
              :dbname   "postgres"})


(def opts {:builder-fn rs/as-unqualified-lower-maps})


(defn make-datasource []
  (let [ds (connection/->pool HikariDataSource db-spec)]
    ; Ensure we have connectivity to DB
    (-> (jdbc/get-connection ds)
        (.close))
    ds))


(defn close-datasource [ds]
  (when ds
    (-> (jdbc/get-datasource ds)
        (.close))))
