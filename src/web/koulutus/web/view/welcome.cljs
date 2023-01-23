(ns koulutus.web.view.welcome
  (:require [clojure.string :as str]
            [koulutus.web.state :refer [app-state]]))

(defn get-counter []
  (-> @app-state :view :welcome :counter (or 0)))


(defn on-add-counter [e]
  (swap! app-state update-in [:view :welcome :counter] inc)
  nil)


(defn icon
  ([icon-name] (icon nil icon-name))
  ([opts icon-name]
   [:span.material-symbols-outlined opts
    (-> icon-name (name) (str/replace "-" "_"))]))


(defn welcome-view [_]
  [:div.container
   [:h1
    "Tervetuloa"
    [icon {:style {:padding-inline "0.3em"}} :waving-hand]]
   [:div
    [:a {:href     "#"
         :role     "button"
         :on-click on-add-counter}
     [icon :thumb-up]]
    [:span {:style {:padding-inline "0.3em"}} "Likes:"]
    [:strong (get-counter)]]])
