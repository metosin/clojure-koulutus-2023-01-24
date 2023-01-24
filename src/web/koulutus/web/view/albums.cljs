(ns koulutus.web.view.albums
  (:require [goog.functions :refer [debounce]]
            [promesa.core :as p]
            [lambdaisland.fetch :as fetch]
            [koulutus.web.state :refer [app-state]]))


(defn get-view-state []
  (-> @app-state :view :albums))


(defn update-view-state! [f & args]
  (apply swap! app-state update-in [:view :albums] f args))


(def load-albums!
  (debounce (fn [album-name]
              (update-view-state! assoc :status :loading)
              (-> (fetch/get "/api/music/album" {:accept       :json
                                                 :content-type :json
                                                 :query-params {:album_name album-name}})
                  (p/then (fn [{:keys [status body]}]
                            (update-view-state! merge (if (= status 200)
                                                        {:status :ok
                                                         :data   (js->clj body {:keywordize-keys true})}
                                                        {:status :error
                                                         :data   body}))))))
            500))


(defn albums-list [{:keys [albums]}]
  [:table
   [:thead
    [:tr
     [:th "Album"]
     [:th "Artist"]
     [:th "Released"]]]
   [:tbody
    (for [{:keys [album_id album_name artist_name album_released]} albums]
      [:tr {:key      album_id
            :on-click (fn [e] (println "Click:" album_id))}
       [:td album_name]
       [:td artist_name]
       [:td album_released]])]])


(defn on-album-name-change [^js e]
  (let [album-name (-> e .-target .-value)]
    (update-view-state! assoc :album-name album-name)
    (load-albums! album-name)))


(defn albums-view [_]
  (let [{:keys [album-name status data]} (get-view-state)]
    [:div {:class ["container" "music"]}
     [:h1 "Albums"]
     [:form {:on-submit (fn [^js e] (.prventDefault e))}
      [:input {:type        "text"
               :placeholder "Album name..."
               :value       album-name
               :on-change   on-album-name-change}]]
     (case status
       nil [:div "Ready to seach albums"]
       :loading [:div {:aria-busy "true"} "Loading..."]
       :error [:div "Oh no"]
       :ok [albums-list {:albums data}])]))
