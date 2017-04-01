(ns lunch-roulette.core
    (:require [sablono.core :as sab]
              [reagent.core :as r]))

(def roulette-teams (atom {}))
(def roulette-participants (atom []))
(def number-of-teams (atom 0))

(defn calc-number-of-teams
  [number]
  (let [div-by-3 (/ number 3)]
    (if (< number 6)
      1
      (int div-by-3))))

(defn set-teams
  [roulette-participants roulette-teams team-number]
  (let [shuffled-list (shuffle roulette-participants)
        random-player (last shuffled-list)
        rest-of-participants (pop shuffled-list)
        conjed-list (if (get roulette-teams team-number)
                         (conj (get roulette-teams team-number) random-player)
                         [random-player])
        new-roulette-teams (assoc roulette-teams team-number conjed-list)]
      (if-not (empty? rest-of-participants)
        (let [next-team-number (if (< (inc team-number) @number-of-teams)
                          (inc team-number)
                          0)]
          (recur rest-of-participants new-roulette-teams next-team-number))
        new-roulette-teams)))

(defn form-groups []
  (reset! roulette-teams {})
  (let [teams-count (calc-number-of-teams (count @roulette-participants))]
    (and (reset! number-of-teams teams-count)
         (reset! roulette-teams (set-teams @roulette-participants {} 0)))))

(defn build-participants-prints []
  (let [players-string (if (empty? @roulette-participants)
                           "No one yet"
                           (clojure.string/join ", " @roulette-participants))]
    [:p players-string]))

(defn build-participant-input
  [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))
           :on-key-down #(if (and (= "Enter" (.-key %)) (not (empty? (-> % .-target .-value)))) (reset! roulette-participants (conj @roulette-participants @value)))}])

(defn roulette-string
  [my-value]
  (cond
    (= 1 @number-of-teams) [:p "You should all go out to lunch together" ]
    (empty? my-value) my-value
    :else
    [:div
      (loop [this-team (first @roulette-teams) rest-of-teams (rest @roulette-teams) p-array []]
        (let [p-string [:p (str (clojure.string/join ", " (last this-team)) ".")]
              next-p-array (conj p-array p-string)]
          (if-not (empty? rest-of-teams)
            (recur (first rest-of-teams) (rest rest-of-teams) next-p-array)
            next-p-array)))]))

(defn build-participants-div []
  (let [val (r/atom "")]
    [:div
     [:h1 "Welcome to Lunch Roulette!"]
     [:div
        [:h2 "Participants:"]
        [:p (build-participant-input val)
          [:a {:href "#"
               :onClick #(if-not (empty? @val) (reset! roulette-participants (conj @roulette-participants @val)))}
               "Add participant"]]]
     (build-participants-prints)]))

(defn build-roulette-div []
  [:div
    [:img {:src "./images/walken-roulette.jpeg"}]
    [:h2 "Roulette Teams:"]
    [:div [:a {:href "#" :onClick #(form-groups)} "Spin the cylinder"]]
    [:div (roulette-string @roulette-teams)]])

(defn build-divs [] (sab/html [:div (build-participants-div) (build-roulette-div)]))

(defn render! []
  (.render js/ReactDOM
           (build-divs)
           (.getElementById js/document "app")))

(add-watch roulette-participants :on-change (fn [_ _ _ _] (render!)))
(add-watch roulette-teams :on-change (fn [_ _ _ _] (render!)))

(render!)
