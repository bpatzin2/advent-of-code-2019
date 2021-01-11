(ns advent-of-code-2019.ascii-drawing
  (:require [clojure.string :as str]))

(defn get-row [x-range y filled-coords]
  (let [row-colors (map #(contains? filled-coords {:x % :y y}) x-range)
        chars (map #(if % "#" " ") row-colors)]
    (apply str chars)))

(defn draw [coords]
  (let [filled-coords (set coords)
        xs (map :x filled-coords)
        ys (map :y filled-coords)
        min-x (apply min xs)
        max-x (apply max xs)
        x-range (range min-x (inc max-x))
        min-y (apply min ys)
        max-y (apply max ys)
        y-range (range max-y (dec min-y) -1)
        rows (map #(get-row x-range % filled-coords) y-range)]
    (str/join "\n" rows)))