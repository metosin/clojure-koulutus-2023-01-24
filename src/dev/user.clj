(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
            [shadow.cljs.devtools.api :as shadow]
            [koulutus.server.db :as db]
            [koulutus.server.http :as http]))


(def ds nil)
(def server nil)


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn start []
  (alter-var-root #'ds (constantly (db/make-datasource)))
  (alter-var-root #'server (constantly (http/start-server ds))))


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn reset []
  (http/stop-server server)
  (db/close-datasource ds)
  (tnr/refresh :after 'user/start))


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn repl-web []
  (shadow/repl :web))


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn repl-node []
  (shadow/repl :node))


(comment

  (reset)

  ;
  )