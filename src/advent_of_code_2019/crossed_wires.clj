(ns advent-of-code-2019.crossed-wires
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.set :as set-lib]))

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

(defn parse-direction [direction-str]
  [(subs direction-str 0 1) (Integer/parseInt (subs direction-str 1))])

(defn parse [wire-directions-str]
  (map parse-direction (str/split wire-directions-str #",")))

(defn manhattan-distance [coord]
  (+ (Math/abs (first coord)) (Math/abs (second coord))))

(defn not-origin [coord]
  (not= '(0 0) coord))

(defn closest-overlap-coords [coordsA coordsB]
  (let [overlaps (set-lib/intersection (set coordsA) (set coordsB))]
    (first (sort-by manhattan-distance (filter not-origin overlaps)))))

(defn closet-overlap [wire-directions-str1 wire-directions-str2]
  (closest-overlap-coords (wire-coords (parse wire-directions-str1))
                          (wire-coords (parse wire-directions-str2))))

(defn closet-overlap-dist [wire-directions-str1 wire-directions-str2]
  (manhattan-distance (closet-overlap wire-directions-str1  wire-directions-str2)))