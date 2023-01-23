(ns koulutus.web.state
  (:require [reagent.core :as r]))


(defonce app-state (r/atom {}))
