(ns advent-of-code-2019.bugs-recursive
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [clojure.math.numeric-tower :as math]))

(defn two-d-vec [s]
  (vec (map #(str/split % #"") (vec (str/split s #"\n")))))

(defn val-at [row-num col-num layer state]
  (get (get (get state layer) row-num) col-num))

(defn bug-at? [row-num col-num layer state]
  (= "#" (val-at row-num col-num layer state)))

(defn bugs-at
  ([offset row-num col-num layer state]
   (bugs-at (+ row-num (first offset))
            (+ col-num (second offset))
            layer
            state))
  ([row-num col-num layer state]
   (if (bug-at? row-num col-num layer state) 1 0)))

(defn adj-bug-count [row-num col-num layer state]
  (reduce + 
          (map #(bugs-at % row-num col-num layer state) 
               [[0 1] [0 -1] [1 0] [-1 0]])))

(defn next-state-for-bug [row-num col-num layer state]
  (if (= 1 (adj-bug-count row-num col-num layer state)) "#" "."))

(defn next-state-for-empty [row-num col-num layer state]
  (if (or
       (= 1 (adj-bug-count row-num col-num layer state))
       (= 2 (adj-bug-count row-num col-num layer state)))
    "#" "."))

(defn next-cell-state [row-num col-num layer state]
  (if (= (val-at row-num col-num layer state) "#")
    (next-state-for-bug row-num col-num layer state)
    (next-state-for-empty row-num col-num layer state)))

(defn next-row-state [row-num layer state]
  (let [col-count (count (get (get state 0) row-num))]
    (mapv #(next-cell-state row-num % layer state) (range col-count))))

(defn next-layer-state [layer state]
  (let [row-count (count (get state 0))] 
    (mapv #(next-row-state % layer state) (range row-count))))

(defn next-layer-range [state]
  (range 
   (+ -1 (apply min (keys state)))
   (+ 2 (apply max (keys state)))))

(defn next-state [state]
  (reduce conj {} 
          (map #(identity {% (next-layer-state % state)}) 
               (next-layer-range state))))

(defn next-state-str [state-str]
  (next-state {0 (two-d-vec state-str)}))