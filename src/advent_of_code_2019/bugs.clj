(ns advent-of-code-2019.bugs
  (:gen-class)
  (:require [clojure.string :as str]))

(defn two-d-vec [s]
  (vec (map #(str/split % #"") (vec (str/split s #"\n")))))

(defn val-at [row-num col-num state]
  (get (get state row-num) col-num))

(defn bug-at? [offset row-num col-num state]
  (= "#" (val-at (+ row-num (first offset))
                 (+ col-num (second offset))
                 state)))

(defn adj-bug-count [row-num col-num state]
  (count (filter #(bug-at? % row-num col-num state) 
                 [[0 1] [0 -1] [1 0] [-1 0]])))

(defn next-state-for-bug [row-num col-num state]
  (if (= 1 (adj-bug-count row-num col-num state)) "#" "."))

(defn next-state-for-empty [row-num col-num state]
  (if (or 
       (= 1 (adj-bug-count row-num col-num state))
       (= 2 (adj-bug-count row-num col-num state))) 
    "#" "."))

(defn next-cell-state [row-num col-num state]
  (if (= (val-at row-num col-num state) "#")
    (next-state-for-bug row-num col-num state)
    (next-state-for-empty row-num col-num state)))

(defn next-row-state [row-num state]
  (mapv #(next-cell-state row-num % state) (range (count (state row-num)))))

(defn next-state-vec [state-vec]
  (mapv #(next-row-state % state-vec) (range (count state-vec))))

(defn next-state [state]
  (next-state-vec (two-d-vec state)))

(defn first-dup-state [state]
  (loop [curr-state (two-d-vec state)
         prev-states #{}]
    (if (contains? prev-states curr-state)
      curr-state
      (recur (next-state-vec curr-state) (conj prev-states curr-state)))))