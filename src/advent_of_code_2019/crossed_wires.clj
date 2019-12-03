(ns advent-of-code-2019.crossed-wires
  (:gen-class))

(defn right-coord [start-coord index]
  [ (+ 1 index (first start-coord)) (second start-coord)])

(defn up-coord [start-coord index]
  [(first start-coord) (+ 1 index (second start-coord))])

(defn process-direction [direction start] 
  (case (first direction) 
    "R" (map #(right-coord start %) (range (second direction)))
    "U" (map #(up-coord start %) (range (second direction))))) 

(defn wire-coords [wire-directions] 
  (loop [directions wire-directions 
         acc ['(0 0)]]
    (if (empty? directions)
        acc
        (recur (rest directions) (concat acc (process-direction (first directions) (last acc)))))))
