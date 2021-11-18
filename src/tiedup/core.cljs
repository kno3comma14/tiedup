(ns tiedup.core
    (:require 
              [reagent.core :as reagent :refer [atom]]
              [reagent.dom :as rd]))

(enable-console-print!)

(println "This text is printed from src/tiedup/core.cljs. Go ahead and edit it and see reloading in action.")

(def available-characters (vec "abcdefghijklmnopqrstuvwxyz"))
(def number-of-characters (count available-characters))

(defn get-pairs
  "This will be replaced by rust wasm function"
  []
  [{:left-word "mapache"
    :right-word "chemical"}
   {:left-word "ukelele"
    :right-word "element"}])

(defonce app-state (atom {:text "Hello world!"
                          :input-pairs (get-pairs)
                          :first-letter (first available-characters)
                          :second-letter (get available-characters 1)
                          :third-character (get available-characters 2)}))

(defn get-random-pair
  "This will be replaced by rust wasm function"
  [pair-index]
  (let [random-index (rand-int (count (:input-pairs @app-state)))]
    (if (not= random-index pair-index)
      random-index
      (get-random-pair random-index))))

(defn generate-new-character
  [actual-letter]
  (let [letter-index (clojure.string/index-of available-characters actual-letter)
        next-index (mod (+ letter-index 1) number-of-characters)]
    (get available-characters next-index)))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this and watch it change!"]])

(rd/render [hello-world]
           (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
