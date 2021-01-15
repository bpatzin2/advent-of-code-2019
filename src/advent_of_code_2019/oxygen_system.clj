(ns advent-of-code-2019.oxygen-system
  (:gen-class)
  (:require [advent-of-code-2019.intcode.intcode :as intcode])
  (:import (clojure.lang PersistentQueue)))

(defn execute-segment [exe-state dir]
  (intcode/execute-segment exe-state dir))

(defn next-coord [prev-coord dir]
  (let [x (first prev-coord)
        y (second prev-coord)]
    (case dir
      1 [x (inc y)]
      2 [x (dec y)]
      3 [(dec x) y]
      4 [(inc x) y])))

(defn move-droid [droid-state dir]
  (let [prev-path (:path droid-state)
        next-coord (next-coord (last prev-path) dir)
        exe-state (execute-segment (:exe-state droid-state) dir)]
    {:exe-state exe-state
     :path (conj prev-path next-coord)
     :last-response (last (:output exe-state))}))

(defn init-droid-state [program start-coord]
  {:exe-state
   {:program program
    :addr 0
    :output []
    :relative-base 0
    :is-first true}
   :path [start-coord]
   :last-response 1})

(defn found-oxygen? [droid-state]
  (= 2 (:last-response droid-state)))

(defn hit-wall? [droid-state]
  (= 0 (:last-response droid-state)))

(defn last-coord [state]
  (last (:path state)))

(defn not-already-processed? [state memo dir]
  (let [next-coord (next-coord (last-coord state) dir)]
    (not (contains? memo next-coord))))

(defn make-next-valid-moves [droid droid-state memo]
  (->> [1 2 3 4]
       (filter #(not-already-processed? droid-state memo %))
       (map #((:move droid) droid-state %))
       (filter #(not (hit-wall? %)))))

(defn shortest-path-to-oxygen [droid]
  (loop [queue (conj PersistentQueue/EMPTY (:init-state droid))
         memo #{}]
      (let [state (peek queue)
            new-memo (conj memo (last-coord state))]
        (if
          (found-oxygen? state)
          (:path state)
          (recur
           (into (pop queue) (make-next-valid-moves droid state new-memo))
           new-memo)))))

(defn create-droid [droid-program]
  {:move       move-droid
   :init-state (init-droid-state droid-program [0,0])})

(defn droid-num-steps-to-oxygen [droid]
  (dec (count (shortest-path-to-oxygen droid))))

(defn num-steps-to-oxygen [droid-program]
  (let [droid (create-droid droid-program)]
    (droid-num-steps-to-oxygen droid)))

(defn longest-path [droid]
  (loop [queue (conj PersistentQueue/EMPTY (:init-state droid))
         memo #{}]
    (let [state (peek queue)
          new-memo (conj memo (last-coord state))
          next-droid-states (make-next-valid-moves droid state new-memo)
          remaining-q (pop queue)
          next-q (into remaining-q next-droid-states)]
      (if
        (empty? next-q)
        (:path state)
        (recur next-q new-memo)))))

(defn max-steps-from-oxygen [droid-program]
  (let [starting-droid (create-droid droid-program)
        oxygen-loc (last (shortest-path-to-oxygen starting-droid))
        droid-as-oxygen (init-droid-state droid-program oxygen-loc)]
    (dec (count (longest-path droid-as-oxygen)))))
