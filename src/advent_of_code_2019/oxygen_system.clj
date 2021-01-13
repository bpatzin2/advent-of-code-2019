(ns advent-of-code-2019.oxygen-system
  (:gen-class)
  (:require [advent-of-code-2019.intcode.intcode :as intcode])
  (:import (clojure.lang PersistentQueue)))

(defn execute-segment [exe-state dir]
  (intcode/execute-segment
   (:program exe-state)
   (:addr exe-state)
   dir
   (:output exe-state)
   (:relative-base exe-state)
   (:is-first exe-state)))

(defn new-coord [prev-coord dir]
  (let [x (first prev-coord)
        y (second prev-coord)]
    (case dir
      1 [x (inc y)]
      2 [x (dec y)]
      3 [(dec x) y]
      4 [(inc x) y])))

(defn create-droid-state [exe-state prev-path dir]
  {:exe-state exe-state
   :path (conj prev-path (new-coord (last prev-path) dir))
   :last-response (last (:output exe-state))})

(defn move-driod [droid-state dir]
  (let [new-exe-state (execute-segment (:exe-state droid-state) dir)]
    (create-droid-state new-exe-state (:path droid-state) dir)))

(defn init-droid-state [program]
  {:exe-state
   {:program program
    :addr 0
    :output []
    :relative-base 0
    :is-first true}
   :path [[0,0]]
   :last-response 1})

(defn move-all-dirs [droid-state]
  (map #(move-driod droid-state %) [1 2 3 4]))

(defn found-oxygen? [droid-state]
  (= 2 (:last-response droid-state)))

(defn hit-wall? [droid-state]
  (= 0 (:last-response droid-state)))

(defn last-coord [state]
  (last (:path state)))

(defn not-already-processed? [state memo dir]
  (not (contains? memo (new-coord (last-coord state) dir))))

(defn next-valid-moves [droid-state memo]
  (->> [1 2 3 4]
       (filter #(not-already-processed? droid-state memo %))
       (map #(move-driod droid-state %))
       (filter #(not (hit-wall? %)))))

(defn find-path-to-oxygen [droid-state]
  (loop [queue (conj PersistentQueue/EMPTY droid-state)
         memo #{}]
      (let [state (peek queue)
            new-memo (conj memo (last-coord state))]
        (cond
          (found-oxygen? state) (:path state)
          (hit-wall? state) (recur (pop queue) memo)
          :else (recur
                 (into (pop queue) (next-valid-moves state memo))
                 new-memo)))))

(defn num-steps-to-oxygen [droid-program]
  (dec (count (find-path-to-oxygen (init-droid-state droid-program)))))