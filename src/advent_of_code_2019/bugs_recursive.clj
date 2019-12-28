(ns advent-of-code-2019.bugs-recursive
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [clojure.math.numeric-tower :as math]))

(defn two-d-vec [s]
  (vec (map #(str/split % #"") (vec (str/split s #"\n")))))

(defn val-at [row-num col-num layer state]
  (get (get (get state layer) row-num) col-num))

(defn bug-at?
  ([[row-num col-num layer] state]
   (bug-at? row-num col-num layer state))
  ([row-num col-num layer state]
   (and 
    (not= [2 2] [row-num col-num])
    (= "#" (val-at row-num col-num layer state)))))

(defn up-adjs [row-num col-num layer]
  (cond  
    (= 0 row-num) [[1 2 (- layer 1)]]
    (= [3 2] [row-num col-num]) [[4 0 (+ layer 1)]
                                 [4 1 (+ layer 1)]
                                 [4 2 (+ layer 1)]
                                 [4 3 (+ layer 1)]
                                 [4 4 (+ layer 1)]]
    
    :else [[(dec row-num) col-num layer]]))

(defn down-adjs [row-num col-num layer]
    (cond  
      (= 4 row-num) [[3 2 (- layer 1)]]
      (= [1 2] [row-num col-num]) [[0 0 (+ layer 1)]
                                   [0 1 (+ layer 1)]
                                   [0 2 (+ layer 1)]
                                   [0 3 (+ layer 1)]
                                   [0 4 (+ layer 1)]]
      
      :else [[(inc row-num) col-num layer]]))

(defn left-adjs [row-num col-num layer]
  (cond  
    (= 0 col-num) [[2 1 (- layer 1)]]
    (= [2 3] [row-num col-num]) [[0 4 (+ layer 1)]
                                 [1 4 (+ layer 1)]
                                 [2 4 (+ layer 1)]
                                 [3 4 (+ layer 1)]
                                 [4 4 (+ layer 1)]]
    
    :else [[row-num (dec col-num) layer]]))
  

(defn right-adjs [row-num col-num layer]
    (cond  
      (= 4 col-num) [[2 3 (- layer 1)]]
      (= [2 1] [row-num col-num]) [[0 0 (+ layer 1)]
                                   [1 0 (+ layer 1)]
                                   [2 0 (+ layer 1)]
                                   [3 0 (+ layer 1)]
                                   [4 0 (+ layer 1)]]
      
      :else [[row-num (inc col-num) layer]]))

(defn adjacencies [row-num col-num layer]
  (mapcat #(% row-num col-num layer) [up-adjs down-adjs left-adjs right-adjs]))

(defn adj-bug-count [row-num col-num layer state]
  (count (filter #(bug-at? % state) (adjacencies row-num col-num layer))))

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

(defn next-state-str 
  ([state-str]
   (next-state-str state-str 1))
  ([state-str times]
   (nth (iterate next-state {0 (two-d-vec state-str)}) times)))

(defn num-bugs [state]
  (count (filter #(bug-at? % state) 
              (combo/cartesian-product
               (range (count (state 0)))
               (range (count ((state 0) 0)))
               (keys state))) ))

(defn num-bugs-after [state-str times]
  (num-bugs (next-state-str state-str times)))

