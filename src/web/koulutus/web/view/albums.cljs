(ns koulutus.web.view.albums
  (:require [clojure.string :as str]
            [promesa.core :as p]
            [lambdaisland.fetch :as fetch]
            [koulutus.web.state :refer [app-state]]))


(defn set-view-state! [state]
  (swap! app-state assoc-in [:view :albums] state))


(defn get-view-state []
  (-> @app-state :view :albums))


(defn load-albums! []
  (set-view-state! {:status :loading})
  (-> (fetch/get "/api/music/albums" {:accept       :json
                                      :content-type :json})
      (p/then (fn [{:keys [status body]}]
                (set-view-state! (if (= status 200)
                                   {:status :ok
                                    :data   (js->clj body {:keywordize-keys true})}
                                   {:status :error
                                    :data   body}))))))


(defn albums-list [{:keys [albums]}]
  [:table
   [:thead
    [:tr
     [:th "Album"]
     [:th "Artist"]
     [:th "Released"]
     [:th "Genres"]]]
   [:tbody
    (for [{:keys [id name artist_name released genres]} albums]
      [:tr {:key      id
            :on-click (fn [e] (println "Click:" id))}
       [:td name]
       [:td artist_name]
       [:td released]
       [:td (str/join ", " genres)]])]])


(defn albums-view [_]
  (load-albums!)
  (fn []
    [:div {:class ["container" "simple-music"]}
     [:h1 "Albums"]
     (let [{:keys [status data]} (get-view-state)]
       (case status
         :loading [:div {:aria-busy "true"} "Loading..."]
         :error [:div "Oh no"]
         :ok [albums-list {:albums data}]))]))
