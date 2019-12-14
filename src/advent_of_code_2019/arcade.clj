(ns advent-of-code-2019.arcade
  (:gen-class)
  (:require [advent-of-code-2019.intcode :as intcode]))

(defn execute-program [program]
  (get (intcode/execute-with-output program []) :output))

(defn draw-tiles [output]
  (into {} (map #(hash-map [(first %) (second %)] (nth % 2)) (partition 3 output))))

(defn run-game [game-num-vec]
  (draw-tiles (execute-program game-num-vec)))

(defn count-blocks [tiles]
  (->> tiles
       (vals)
       (filter #(= % 2))
       (count)))

(defn run-and-count-blocks [game-num-vec]
  (count-blocks (run-game game-num-vec)))