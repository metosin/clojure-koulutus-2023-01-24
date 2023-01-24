(ns koulutus.web.main
  (:require [reagent.dom :as rdom]
            [koulutus.web.state :refer [app-state]]
            [koulutus.web.view.nav :refer [nav-bar]]
            [koulutus.web.view.welcome :refer [welcome-view]]
            [koulutus.web.view.albums :refer [albums-view]]
            [koulutus.web.view.artists :refer [artists-view]]))


(defn not-found-view []
  [:div "Not found"])


(defn main-view []
  [:div.container
   [nav-bar]
   (let [{:keys [path params]} (-> @app-state :nav)]
     (case path
       "/" [welcome-view {:params params}]
       "/albums" [albums-view {:params params}]
       "/artists" [artists-view {:params params}]
       [not-found-view]))])


(defn ^:export start []
  (let [app-div (js/document.getElementById "app")]
    (rdom/render [main-view] app-div)))
