(ns advent-of-code-2019.painting-robot.painting-robot-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
            [advent-of-code-2019.intcode.intcode :as intcode]
            [clojure.string :as str]))

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
   1 ; (0,0) was prev painted white
   0
   0])

(defn init-state [program]
  {:program (intcode/init-program program)
   :addr 0
   :output []
   :relative-base 0
   :is-first true})

(def turns {0 :turn-left 1 :turn-right})

(def colors {0 :black 1 :white})
(defn color-code [color] (if (= color :black) 0 1))

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

(defn init-painting-state-pt2 [robot-initializer]
  (create-painting-state {{:x 0 :y 0} :white} {:x 0 :y 0} :up [] (robot-initializer)))

(defn current-panel-color [state]
  (get (:painted-panels state) (:pos state) :black))

(defn run-test-robot [state input i]
  {:output (nth example-outputs i)
   :status (if (= (inc i) (count example-outputs)) :stopped :paused)})

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

(defn paint-hull [robot-runner robot-initializer]
  (loop [state (init-painting-state-pt2 robot-initializer)
         i 0]
    (let [color (current-panel-color state)
          next-input (color-code color)
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

(defn actual-robot [state input i]
  (let [robot-exe-state (:robot-exe-state state)
        next-exe-state (intcode/execute-segment robot-exe-state input false)
        status (:status next-exe-state)
        output (:output next-exe-state)
        robot-state (assoc next-exe-state :output [])]
    {:output output :status status :robot-exe-state robot-state}))

(describe "part1 solution"
  (it "works for provided input"
      (let [painting-result (paint-hull actual-robot robot-initializer)]
        (should= 1876 (count (:painted-panels painting-result))))))


(defn get-row [x-range y coord-colors]
  (let [row-colors (map #(get coord-colors {:x % :y y} :black) x-range)
        row-color-codes (apply str (map #(if (= % :white) "#" "_") row-colors))]
    row-color-codes))

(defn paint [coord-colors]
  (let [xs (map :x (keys coord-colors))
        ys (map :y (keys coord-colors))
        min-x (apply min xs)
        max-x (apply max xs)
        x-range (range min-x (inc max-x))
        min-y (apply min ys)
        max-y (apply max ys)
        y-range (range max-y (dec min-y) -1)
        rows (map #(get-row x-range % coord-colors) y-range)]
    (str/join "\n" rows)))

(def sample-paint {{:x 1 :y 1} :white
                   {:x 0 :y 0} :white
                   {:x -1 :y -1} :black})

(println (paint sample-paint))

(println (paint (:painted-panels (paint-hull actual-robot robot-initializer))))

(println "done")
