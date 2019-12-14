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

(defn play-move [game-num-vec move]
  (intcode/execute-segment game-num-vec 0 1 [] 0 true))

(defn get-score [exe-state]
  (last (get exe-state :output)))

(defn play-game [game-num-vec] 
  (loop [progam game-num-vec
         addr 0
         input 1
         output []
         rb 0
         is-first true]
    (let [exe-state (intcode/execute-segment progam addr 1 [] rb is-first)]
      (if
       (= :stopped (get exe-state :status))
        (get-score exe-state)
        (recur 
         (get exe-state :program)
         (get exe-state :addr)
         1 ;input
         (get exe-state :output)
         (get exe-state :relative-base)
         false)
        ))))
