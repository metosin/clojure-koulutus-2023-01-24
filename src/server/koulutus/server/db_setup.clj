(ns koulutus.server.db-setup
  (:require [next.jdbc.result-set :as rs]
            [jsonista.core :as jsonista])
  (:import (org.postgresql.jdbc PgArray)
           (org.postgresql.util PGobject)))


(defn read-pgobject [^PGobject v]
  (let [type  (.getType v)
        value (.getValue v)]
    (case type
      ("json", "jsonb") (jsonista/read-value value jsonista/keyword-keys-object-mapper))))


(extend-protocol rs/ReadableColumn
  java.sql.Date
  (read-column-by-label [^java.sql.Date v _]
    (.toLocalDate v))
  (read-column-by-index [^java.sql.Date v _2 _3]
    (.toLocalDate v))
  java.sql.Timestamp
  (read-column-by-label [^java.sql.Timestamp v _]
    (.toInstant v))
  (read-column-by-index [^java.sql.Timestamp v _2 _3]
    (.toInstant v))
  PgArray
  (read-column-by-label [^PgArray v _]
    (seq (.getArray v)))
  (read-column-by-index [^PgArray v _2 _3]
    (seq (.getArray v)))
  PGobject
  (read-column-by-label [^PGobject v _]
    (read-pgobject v))
  (read-column-by-index [^PGobject v _2 _3]
    (read-pgobject v)))

