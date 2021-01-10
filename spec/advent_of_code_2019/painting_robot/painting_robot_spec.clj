(ns advent-of-code-2019.painting-robot.painting-robot-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
            [advent-of-code-2019.intcode.intcode :as intcode]))

(defn create-robot-output [paint turn]
  {:paint paint :turn turn})

(def example-outputs
  [[1,0] ;paint white, turn left
   [0,0] ;paint black, turn left
   [1,0] ;paint white, turn left
   [1,0] ;paint white, turn left
   [0,1] ;paint black, turn right
   [1,0]
   [1,0]])

(def expected-inputs
  [0 ; (0,0) starts black
   0 ; (-1,0) starts black
   0 ; (-1,-1) starts black
   0 ; (0,-1) starts black
   1
   0
   0]) ; (0,0) was prev painted white

(defn init-state [program]
  {:program (intcode/init-program program)
   :addr 0
   :output []
   :relative-base 0
   :is-first true})

; Program:
; 1. determine current panel color
; 2. provide it as input to the robot
; 3. get the robots output
; 4. update the current state of the hull
; 5. turn and move the robot

(defn create-painting-state [painted-panels pos dir inputs robot-exe-state]
  {:painted-panels  painted-panels
   :pos             pos
   :dir             dir
   :inputs          inputs
   :robot-exe-state robot-exe-state})

(defn init-painting-state [robot-initializer]
  (create-painting-state {} {:x 0 :y 0} :up [] (robot-initializer)))

(defn current-panel-color [state]
  (if (contains? (:painted-panels state) (:pos state))
    1 0))

(defn run-test-robot [state input i]
  {:output (nth example-outputs i)
   :status (if (= (inc i) (count example-outputs)) :stopped :paused)})

(defn stopped? [robot-state]
  (= :stopped (:status robot-state)))

(defn paint-color [robot-output]
  (if (= 1 (first robot-output)) :white :black))

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
  (if (= 0 (second robot-output)) :turn-left :turn-right))

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

(defn paint-hull [robot-runner robot-initializer]
  (loop [state (init-painting-state robot-initializer)
         i 0]
    (let [next-input (current-panel-color state)
          robot-state (robot-runner state next-input i)
          painting-state (update-state state robot-state next-input)]
      (if (stopped? robot-state)
        painting-state
        (recur painting-state (inc i))))))

(defn test-robot-initializer [] {})

(describe "update-state"
  (it "updates the current state based on the robot's output"
    (let [initial-state (init-painting-state test-robot-initializer)
          result-state (update-state initial-state {:output [1,0] :status :paused} 0)] ;white, left
      (should= {{:x 0, :y 0} :white} (:painted-panels result-state))
      (should= {:x -1, :y 0} (:pos result-state))
      (should= :left (:dir result-state))
      (should= [0] (:inputs result-state)))))

(describe "paint-hull"
  (it "works for provided example"
      (let [result-state (paint-hull run-test-robot test-robot-initializer)]
        (should= {{:x 0, :y 0} :black
                  {:x -1, :y 0} :black
                  {:x -1, :y -1} :white
                  {:x 0, :y -1} :white
                  {:x 1, :y 0} :white
                  {:x 1, :y 1} :white}
                 (:painted-panels result-state))
        (should= 6 (count (:painted-panels result-state)))
        (should= {:x 0, :y 1} (:pos result-state))
        (should= :left (:dir result-state))
        (should= expected-inputs (:inputs result-state)))))

(defn robot-initializer []
  (init-state (csv-as-int-vec "input/day11.txt")))

(defn parse-output [output]
  [(paint-color output) (parse-turn output)])

(defn actual-robot [state input i]
  ;(println (:dir state) (:pos state) input)
  ;(if (= 10 i) (throw (Exception. "my exception message")) nil)
  (let [robot-exe-state (:robot-exe-state state)
        next-exe-state (intcode/execute-segment robot-exe-state input false)
        status (:status next-exe-state)
        output (:output next-exe-state)
        robot-state (assoc next-exe-state :output [])]
    ;(println (parse-output output))
    {:output output :status status :robot-exe-state robot-state}))

(def result (paint-hull actual-robot robot-initializer))
(println (count (:painted-panels result)))