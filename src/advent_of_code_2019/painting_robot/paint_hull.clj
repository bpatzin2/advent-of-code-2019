(ns advent-of-code-2019.painting-robot.paint-hull
  (:require [advent-of-code-2019.painting-robot.robot :as robot]
            [clojure.string :as str]))

(def turns {0 :turn-left 1 :turn-right})

(def colors {0 :black 1 :white})
(defn color-code [color] (if (= color :black) 0 1))

(defn create-painting-state [painted-panels pos dir inputs robot-exe-state]
  {:painted-panels  painted-panels
   :pos             pos
   :dir             dir
   :inputs          inputs
   :robot-exe-state robot-exe-state})

(defn init-painting-state [robot-initializer]
  (create-painting-state {} {:x 0 :y 0} :up [] (robot-initializer)))

(defn init-painting-state-pt2 [robot-initializer]
  (create-painting-state {{:x 0 :y 0} :white} {:x 0 :y 0} :up [] (robot-initializer)))

(defn current-panel-color [state]
  (get (:painted-panels state) (:pos state) :black))


(defn stopped? [robot-state]
  (= :stopped (:status robot-state)))

(defn paint-color [robot-output]
  (let [color-code (first robot-output)]
    (get colors color-code)))

(defn update-painted-panels [state robot-output]
  (let [curr-painted-panels (:painted-panels state)
        color (paint-color robot-output)]
    (conj curr-painted-panels [(:pos state) color])))

(defn update-pos [state dir]
  (let [curr-pos (:pos state)
        curr-x (:x curr-pos)
        curr-y (:y curr-pos)]
    (case dir
      :up (assoc curr-pos :y (inc curr-y))
      :down (assoc curr-pos :y (dec curr-y))
      :left(assoc curr-pos :x (dec curr-x))
      :right (assoc curr-pos :x (inc curr-x)))))

(defn turn-left [dir]
  (case dir
    :up :left
    :down :right
    :left :down
    :right :up))

(defn turn-right [dir]
  (case dir
    :up :right
    :down :left
    :left :up
    :right :down))

(defn parse-turn [robot-output]
  (let [turn-code (second robot-output)]
    (get turns turn-code)))

(defn update-dir [state robot-output]
  (let [curr-dir (:dir state)
        turn-dir (parse-turn robot-output)]
    (if (= :turn-left turn-dir) (turn-left curr-dir) (turn-right curr-dir))))

(defn update-state [state robot-state next-input]
  (let [robot-output (:output robot-state)
        robot-exe-state (:robot-exe-state robot-state)
        painted-panels (update-painted-panels state robot-output)
        dir (update-dir state robot-output)
        pos (update-pos state dir)
        inputs (conj (:inputs state) next-input)]
    (assoc state
      :painted-panels painted-panels
      :pos pos
      :dir dir
      :inputs inputs
      :robot-exe-state robot-exe-state)))

; Program:
; 1. determine current panel color
; 2. provide it as input to the robot
; 3. get the robots output
; 4. update the current state of the hull
; 5. turn and move the robot
(defn paint-hull [robot-runner robot-initializer start-on-white]
  (loop [state (if start-on-white (init-painting-state-pt2 robot-initializer)
                                  (init-painting-state robot-initializer))
         i 0]
    (let [color (current-panel-color state)
          next-input (color-code color)
          robot-state (robot-runner state next-input i)
          painting-state (update-state state robot-state next-input)]
      (if (stopped? robot-state)
        painting-state
        (recur painting-state (inc i))))))