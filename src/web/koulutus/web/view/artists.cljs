(ns koulutus.web.view.artists
  (:require [goog.functions :refer [debounce]]
            [promesa.core :as p]
            [lambdaisland.fetch :as fetch]
            [koulutus.web.state :refer [app-state]]))


(defn get-view-state []
  (-> @app-state :view :artists))


(defn update-view-state! [f & args]
  (apply swap! app-state update-in [:view :artists] f args))


(def load-artists!
  (debounce (fn [artist-name]
              (update-view-state! assoc :status :loading)
              (-> (fetch/get "/api/music/artist" {:accept       :json
                                                  :content-type :json
                                                  :query-params {:artist_name artist-name}})
                  (p/then (fn [{:keys [status body]}]
                            (update-view-state! merge (if (= status 200)
                                                        {:status :ok
                                                         :data   (js->clj body {:keywordize-keys true})}
                                                        {:status :error
                                                         :data   body}))))))
            500))


(defn artists-list [{:keys [artists]}]
  [:table
   [:thead
    [:tr
     [:th "Artist"]
     [:th "Disambiguation"]
     [:th "Albums"]]]
   [:tbody
    (for [{:keys [artist_id artist_name artist_disambiguation artist_albums_count]} artists]
      [:tr {:key      artist_id
            :on-click (fn [e] (println "Click:" artist_id))}
       [:td artist_name]
       [:td artist_disambiguation]
       [:td artist_albums_count]])]])


(defn on-artist-name-change [^js e]
  (let [artist-name (-> e .-target .-value)]
    (update-view-state! assoc :artist-name artist-name)
    (load-artists! artist-name)))


(defn artists-view [_]
  (let [{:keys [artist-name status data]} (get-view-state)]
    [:div {:class ["container" "music"]}
     [:h1 "Artists"]
     [:form {:on-submit (fn [^js e] (.prventDefault e))}
      [:input {:type        "text"
               :placeholder "Artist name..."
               :value       artist-name
               :on-change   on-artist-name-change}]]
     (case status
       nil [:div "Ready to seach artists"]
       :loading [:div {:aria-busy "true"} "Loading..."]
       :error [:div "Oh no"]
       :ok [artists-list {:artists data}])]))
