(ns koulutus.web.main
  (:require [reagent.dom :as rdom]
            [koulutus.web.welcome-view :refer [welcome-view]]))


(defn ^:export start []
  (let [app-div (js/document.getElementById "app")]
    (rdom/render [welcome-view] app-div)))
