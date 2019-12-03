(ns advent-of-code-2019.crossed-wires
  (:gen-class))

(defn right-coord [start-coord index]
  [ (+ (first start-coord) 1 index) (second start-coord)])

(defn left-coord [start-coord index]
  [(- (first start-coord) 1 index) (second start-coord)])

(defn up-coord [start-coord index]
  [(first start-coord) (+ (second start-coord) 1 index)])

(defn down-coord [start-coord index]
  [(first start-coord) (- (second start-coord) 1 index)])

(defn process-direction [direction start] 
  (case (first direction) 
    "R" (map #(right-coord start %) (range (second direction)))
    "L" (map #(left-coord start %) (range (second direction)))
    "U" (map #(up-coord start %) (range (second direction)))
    "D" (map #(down-coord start %) (range (second direction))))) 

(defn wire-coords [wire-directions] 
  (loop [directions wire-directions 
         acc ['(0 0)]]
    (if (empty? directions)
        acc
        (recur (rest directions) (concat acc (process-direction (first directions) (last acc)))))))
