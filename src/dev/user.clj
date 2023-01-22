(ns user
  (:require [clojure.tools.namespace.repl :as tnr]
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

(comment

  (require '[clj-http.client :as http])
  (require '[clojure.pprint :refer [pprint]])
  (require '[jsonista.core :as j])
  (require '[clojure.string :as str])

  (-> "https://randomuser.me/api/"
      (http/get {:accept       :json
                 :query-params {"nat" "fi"}})
      :body
      (j/read-value j/keyword-keys-object-mapper)
      :results
      (first)
      (select-keys [:email :name])
      (update :name (comp (partial str/join " ") (juxt :first :last))))


  (def duration-ms 420000)
  (str (/ (/ duration-ms 1000) 60) " mins")

  (macroexpand-1 '(-> duration-ms
                      (/ 1000)
                      (/ 60)
                      (str " mins")))

  (-> 420000 (/ 1000) (/ 60) (str " minutes"))



  ;; => {:cached nil, :request-time 172, :repeatable? false, :protocol-version {:name "HTTP", :major 1, :minor 1}, :streaming? true, :http-client #object[org.apache.http.impl.client.InternalHttpClient 0x7d4756d5 "org.apache.http.impl.client.InternalHttpClient@7d4756d5"], :chunked? true, :reason-phrase "OK", :headers {"Server" "cloudflare", "Content-Type" "application/json; charset=utf-8", "Access-Control-Allow-Origin" "*", "alt-svc" "h3=\":443\"; ma=86400, h3-29=\":443\"; ma=86400", "NEL" "{\"success_fraction\":0,\"report_to\":\"cf-nel\",\"max_age\":604800}", "Connection" "close", "Transfer-Encoding" "chunked", "ETag" "W/\"484-VMbFtlxIiGB9X66lKoS6Wn9zgjQ\"", "CF-Cache-Status" "DYNAMIC", "CF-RAY" "78b6cb6efe21d987-HEL", "Date" "Wed, 18 Jan 2023 10:54:48 GMT", "Vary" "Accept-Encoding", "X-Powered-By" "Express", "Report-To" "{\"endpoints\":[{\"url\":\"https:\\/\\/a.nel.cloudflare.com\\/report\\/v3?s=GRfWT0%2FznpQEN5PgjihxnirooV1kZQEsTx%2FMq1w7a3A9UvMICYgWdIxsdc4hUPRqIoygUEPFQAopmTrwVJPhwNEP0UKbdukq9bPrKQo1C2JgdbSAHoYb4rTX4Pt2mcI1\"}],\"group\":\"cf-nel\",\"max_age\":604800}", "Cache-Control" "no-cache"}, :orig-content-encoding "gzip", :status 200, :length -1, :body "{\"results\":[{\"gender\":\"female\",\"name\":{\"title\":\"Mrs\",\"first\":\"Suzanne\",\"last\":\"Lopez\"},\"location\":{\"street\":{\"number\":2586,\"name\":\"Northaven Rd\"},\"city\":\"Durham\",\"state\":\"Massachusetts\",\"country\":\"United States\",\"postcode\":35521,\"coordinates\":{\"latitude\":\"87.1360\",\"longitude\":\"153.3179\"},\"timezone\":{\"offset\":\"+4:30\",\"description\":\"Kabul\"}},\"email\":\"suzanne.lopez@example.com\",\"login\":{\"uuid\":\"e8aa4846-4b29-447d-b819-02f096b73e52\",\"username\":\"organicbear214\",\"password\":\"alison\",\"salt\":\"UkJpC1RF\",\"md5\":\"adfb753993edce81dde4c4a50fd86f76\",\"sha1\":\"c4c4f75d4fc0ca2db886a0b4fa43c7fecc39b414\",\"sha256\":\"facf8b7684a7b4f130a5e4703179ebbbcd14a9debd1b49d48a9f9170a04ca0a9\"},\"dob\":{\"date\":\"2000-05-26T07:19:09.841Z\",\"age\":22},\"registered\":{\"date\":\"2003-05-08T20:47:29.662Z\",\"age\":19},\"phone\":\"(574) 393-1975\",\"cell\":\"(901) 313-3175\",\"id\":{\"name\":\"SSN\",\"value\":\"720-03-5663\"},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/women/56.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/women/56.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/women/56.jpg\"},\"nat\":\"US\"}],\"info\":{\"seed\":\"8de41bedfd37dd1a\",\"results\":1,\"page\":1,\"version\":\"1.4\"}}", :trace-redirects []}


  (import '(javax.swing JOptionPane))
  (JOptionPane/showMessageDialog nil "Hello!")

  (require 'koulutus.server.ex.ex-1-scalar-data-types)
  ;
  )