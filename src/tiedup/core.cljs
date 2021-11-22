(ns tiedup.core
    (:require 
              [reagent.core :as reagent :refer [atom]]
              [reagent.dom :as rd]
              [clojure.string :as string]))

(enable-console-print!)

(println "This text is printed from src/tiedup/core.cljs. Go ahead and edit it and see reloading in action.")

(def available-characters (vec "abcdefghijklmnopqrstuvwxyz"))
(def number-of-characters (count available-characters))

(defn get-pairs
  "This will be replaced by rust wasm function"
  []
  [{:left-word "headache"
    :right-word "chemical"
    :answer "che"}
   {:left-word "ukelele"
    :right-word "element"
    :answer "ele"}
   {:left-word "dialogue"
    :right-word "guessing"
    :answer "gue"}])

(defonce app-state (atom {:first-letter (first available-characters)
                          :second-letter (get available-characters 1)
                          :third-letter (get available-characters 2)
                          :actual-pair-index 0}))

(defn prepare-left-word
  [input-word]
  (subs input-word 0 (- (count input-word) 3)))

(defn prepare-right-word
  [input-word]
  (subs input-word 3))

(defn generate-new-character
  [component operator k]
  (let [actual-letter (:value (get component 1))
        letter-index (string/index-of available-characters actual-letter)
        next-index (if (>= (operator letter-index 1) 0)
                     (mod (operator letter-index 1) number-of-characters)
                     (- number-of-characters 1))
        next-character (get available-characters next-index)]
    (swap! app-state assoc-in [k] next-character)))

(defn verify-answer
  [first-box second-box third-box pair]
  (let [first-letter (:value (get first-box 1))
        second-letter (:value (get second-box 1))
        third-letter (:value (get third-box 1))
        answer (str first-letter second-letter third-letter)]
    (= answer (:answer pair))))

(defn character-change-button
  [operation component k]
  (let [operation-symbol (if (= operation +) "+" "-")]
    [:input {:type "button"
             :value operation-symbol
             :on-click #(generate-new-character component operation k)}]))

(defn character-box
  [value]
  [:input {:type "text" :value value}])

(defn left-word
  [left-word]
  (prepare-left-word left-word))

(defn right-word
  [right-word]
  (prepare-right-word right-word))

(defn create-board
  []
  (let [first-letter (character-box (:first-letter @app-state))
        first-letter-minus-button (character-change-button - first-letter :first-letter)
        first-letter-plus-button (character-change-button + first-letter :first-letter)
        second-letter (character-box (:second-letter @app-state))
        second-letter-minus-button (character-change-button - second-letter :second-letter)
        second-letter-plus-button (character-change-button + second-letter :second-letter)
        third-letter (character-box (:third-letter @app-state))
        third-letter-minus-button (character-change-button - third-letter :third-letter)
        third-letter-plus-button (character-change-button + third-letter :third-letter)
        pair (get (get-pairs) (:actual-pair-index @app-state))]
    [:div
     (left-word (:left-word pair))
     first-letter-minus-button
     first-letter
     first-letter-plus-button
     second-letter-minus-button
     second-letter
     second-letter-plus-button
     third-letter-minus-button
     third-letter
     third-letter-plus-button
     (right-word (:right-word pair))
     [:div
      [:input {:type "button" 
               :value "Verify!" 
               :on-click #(if (verify-answer first-letter second-letter third-letter pair)
                            (do
                              (if (= (- (count (get-pairs)) 1) (:actual-pair-index @app-state))
                                (js/alert "Congratulations! You win!")
                                (do
                                  (swap! app-state assoc :actual-pair-index (+ (:actual-pair-index @app-state) 1))
                                  (js/alert "You guessed one word. Good job!"))))
                            (js/alert "Keep trying!"))}]]]))

(rd/render [create-board]
           (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
