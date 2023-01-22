(ns koulutus.web.welcome-view
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [reagent.dom :as rdom]))


(defonce state (r/atom {:counter 0}))


(defn add-like [e]
  (.preventDefault e)
  (swap! state update :counter inc))


(defn icon
  ([icon-name] (icon nil icon-name))
  ([opts icon-name]
   [:span.material-symbols-outlined opts
    (-> icon-name (name) (str/replace "-" "_"))]))


(defn welcome-view []
  [:div.container
   [:h1
    "Tervetuloa"
    [icon {:style {:padding-inline "0.3em"}} :waving-hand]]
   [:div
    [:a {:href     "#"
         :role     "button"
         :on-click add-like}
     [icon :thumb-up]]
    [:span {:style {:padding-inline "0.3em"}} "Likes:"]
    [:strong (-> @state :counter)]]])
