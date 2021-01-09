(ns advent-of-code-2019.painting-robot.painting-robot-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
            [advent-of-code-2019.intcode.intcode :refer :all]))

(defn create-robot-output [paint turn]
  {:paint paint :turn turn})

(def example-outputs
  [[1,0] ;paint white, turn left
   [0,0] ;paint black, turn left
   [1,0] ;paint white, turn left
   [1,0] ;paint white, turn left
   [0,1]]) ;paint black, turn right

(def expected-inputs
  [0 ; (0,0) starts black
   0 ; (-1,0) starts black
   0 ; (-1,-1) starts black
   0 ; (0,-1) starts black
   1]) ; (0,0) was prev painted white

; Program:
; 1. determine current panel color
; 2. provide it as input to the robot
; 3. get the robots output
; 4. update the current state of the hull
; 5. turn and move the robot

(defn create-painting-state [painted-panels pos dir inputs]
  {:painted-panels painted-panels
   :pos            pos
   :dir            dir
   :inputs         inputs})

(defn init-painting-state []
  (create-painting-state {} {:x 0 :y 0} :up []))

(defn current-panel-color [state]
  (if (contains? (:painted-panels state) (:pos state))
    1 0))

(defn run-test-robot [input i]
  {:output (nth example-outputs i)
   :status (if (= (inc i) (count example-outputs)) :stopped :paused)})

(defn stopped? [robot-state]
  (= :stopped (:status robot-state)))

(defn update-painted-panels [state robot-output]
  (let [curr-painted-panels (:painted-panels state)
        color (if (= 1 (first robot-output)) :white :black)]
      (assoc curr-painted-panels (:pos state) color)))

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

(defn update-dir [state robot-output]
  (let [curr-dir (:dir state)]
    (if (= 0 (second robot-output)) (turn-left curr-dir) (turn-right curr-dir))))

(defn update-state [state robot-state next-input]
  (let [robot-output (:output robot-state)
        painted-panels (update-painted-panels state robot-output)
        dir (update-dir state robot-output)
        pos (update-pos state dir)
        inputs (conj (:inputs state) next-input)]
    (assoc state :painted-panels painted-panels :pos pos :dir dir :inputs inputs)))

(defn paint-hull [robot-runner]
  (loop [state (init-painting-state)
         i 0]
    (let [next-input (current-panel-color state)
          robot-state (robot-runner next-input i)
          painting-state (update-state state robot-state next-input)]
      (if (stopped? robot-state)
        painting-state
        (recur painting-state (inc i))))))

(describe "update-state"
  (it "updates the current state based on the robot's output"
    (should= (create-painting-state {{:x 0, :y 0} :white} {:x -1, :y 0} :left [0])
             (update-state (init-painting-state) {:output [1,0] :status :paused} 0)))) ;white, left

(describe "paint-hull"
  (it "works for provided example"
    (should= (create-painting-state
               {{:x 0, :y 0} :black
                {:x -1, :y 0} :black
                {:x -1, :y -1} :white
                {:x 0, :y -1} :white}
               {:x 1, :y 0}
               :right,
               expected-inputs)
             (paint-hull run-test-robot))))
