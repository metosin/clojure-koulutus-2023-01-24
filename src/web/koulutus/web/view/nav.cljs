(ns koulutus.web.view.nav
  (:require [koulutus.web.navigation :as nav]))


(defn nav-bar []
  [:nav
   [:ul>li
    [:strong "Koulutus"]]
   [:ul
    [:li [:a {:href     "#"
              :on-click (nav/on-href "/")} "Etusivu"]]
    [:li [:a {:href     "#"
              :on-click (nav/on-href "/albums")} "Albums"]]]])
