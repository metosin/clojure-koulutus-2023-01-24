(ns koulutus.web.navigation
  (:require [koulutus.web.state :refer [app-state]]))


(defn- set-nav-state! [path params]
  (swap! app-state assoc :nav {:path   path
                               :params params}))


(defn push-state
  ([path] (push-state path nil))
  ([path params]
   (let [query (when params (-> params
                                (clj->js)
                                (js/URLSearchParams.)
                                (.toString)))]
     (js/history.pushState nil "" (str path (when query "?") query))
     (set-nav-state! path params))
   nil))


(defn on-href
  ([path] (on-href path nil))
  ([path params] (fn [e]
                   (.preventDefault e)
                   (push-state path params))))


(defn on-popstate [_e]
  (set-nav-state! js/window.location.pathname
                  (some->> (js/URLSearchParams. js/window.location.search)
                           (.entries)
                           (seq)
                           (map js->clj)
                           (map (fn [[k v]]
                                  [(keyword k) v]))
                           (into {}))))

(defonce _ (do (js/window.addEventListener "popstate" on-popstate)
               (on-popstate nil)))
