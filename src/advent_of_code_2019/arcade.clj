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

(defn play-move [exe-state move]
  (intcode/execute-segment
   (:program exe-state)
   (:addr exe-state)
   move
   (:output exe-state)
   (:relative-base exe-state)
   (:is-first exe-state)))

(defn get-score [exe-state]
  (last (get exe-state :output)))

(defn play-each-move [exe-state]
  (map #(play-move exe-state %) [-1 0 1]))

(defn tiles-from-state [exe-state]
  (draw-tiles (:output exe-state)))

(defn stopped-or-no-blocks? [exe-state]
  (and
   (not= true (:is-first exe-state)) 
   (or 
    (= :stopped (:status exe-state))
    (= 0 (count-blocks (tiles-from-state exe-state))))))

(defn play-valid-moves [exe-state]
  (if
   (stopped-or-no-blocks? exe-state)
    [exe-state]
    (map #(play-move exe-state %) [-1 0 1])))

(defn play-all-valid-moves [exe-states]
  (mapcat play-valid-moves exe-states))

(defn score-from-tiles [tiles]
  (get tiles [-1 0]))

(defn score-from-output [output]
  (score-from-tiles (draw-tiles output)))

(defn score-from-state [exe-state]
 (score-from-output (:output exe-state)))

(defn highest-score [exe-states]
  (apply max (map score-from-state exe-states)))

(defn init-state [program]
  {:program program
   :addr 0
   :output []
   :relative-base 0
   :is-first true
   })

(defn play-game [game-num-vec] 
 (loop [exe-states [(init-state game-num-vec)]]
   (let [next-states (play-all-valid-moves exe-states)]
     (if 
      (= (count next-states) (count exe-states))
       (highest-score exe-states)
       (recur next-states)))))
