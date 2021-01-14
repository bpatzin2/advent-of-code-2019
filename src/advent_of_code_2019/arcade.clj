(ns advent-of-code-2019.arcade
  (:gen-class)
  (:require [advent-of-code-2019.intcode.intcode :as intcode]))

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
  (intcode/execute-segment exe-state move))

(defn get-score [exe-state]
  (last (get exe-state :output)))

(defn tiles-from-state [exe-state]
  (draw-tiles (:output exe-state)))

(defn stopped-or-no-blocks? [exe-state debug-mode]
  (and
   (not= true (:is-first exe-state)) 
   (or 
    (= :stopped (:status exe-state))
    (= 0 (count-blocks (tiles-from-state exe-state)))
    (and 
     debug-mode
     (= 284 (count-blocks (tiles-from-state exe-state)))))))

(defn score-from-tiles [tiles]
  (get tiles [-1 0]))

(defn score-from-output [output]
  (score-from-tiles (draw-tiles output)))

(defn score-from-state [exe-state]
  (score-from-output (:output exe-state)))

(defn init-state [program]
  {:program program
   :addr 0
   :output []
   :relative-base 0
   :is-first true
   })

(defn pos-of [tile-id, tiles]
  (->> tiles
       (filter #(= tile-id (second %)))
       (map first) ;all positions
       (first)))

(defn calc-move [exe-state]
  (let [tiles (tiles-from-state exe-state)
        ball-x (first (pos-of 4 tiles))
        paddle-x (first (pos-of 3 tiles))
        x-diff (- ball-x paddle-x)]
    (cond
      (< x-diff 0) -1
      (> x-diff 0) 1
      :else 0) ))

(defn pick-move [exe-state]
  (if 
   (:is-first exe-state)
    nil
    (calc-move exe-state)))

(defn play-next-move [exe-state]
  (play-move exe-state (pick-move exe-state)))

(defn play-game 
  ([game-num-vec] (play-game game-num-vec false))
  ([game-num-vec debug-mode]
   (loop [exe-state (init-state game-num-vec)]
     (let [next-state (play-next-move exe-state)]
       (if
        (stopped-or-no-blocks? next-state debug-mode)
         (score-from-state next-state)
         (recur next-state))))))
