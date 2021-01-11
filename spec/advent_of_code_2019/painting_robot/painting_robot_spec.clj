(ns advent-of-code-2019.painting-robot.painting-robot-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.painting-robot.paint-hull :refer :all]
            [advent-of-code-2019.painting-robot.robot :as robot]
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

(defn run-test-robot [_ _ i]
  {:output (nth example-outputs i)
   :status (if (= (inc i) (count example-outputs)) :stopped :paused)})

(describe "update-state"
  (it "updates the current state based on the robot's output"
    (let [initial-state (init-painting-state {})
          result-state (update-state initial-state {:output [1,0] :status :paused} 0)] ;white, left
      (should= {{:x 0, :y 0} :white} (:painted-panels result-state))
      (should= {:x -1, :y 0} (:pos result-state))
      (should= :left (:dir result-state))
      (should= [0] (:inputs result-state)))))

(describe "paint-hull"
  (it "works for provided example"
      (let [result-state (paint-hull {:initial-state {} :move run-test-robot} false)]
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

(describe "part1 solution"
  (it "works for provided input"
      (let [painting-result (paint-hull robot/robot false)]
        (should= 1876 (count (:painted-panels painting-result))))))

(def expected-painted-text-strs
  [
   " ##   ##  ###    ##  ##   ##   ##  #   "
   "#  # #  # #  #    # #  # #  # #  # #   "
   "#    #    #  #    # #    #    #    #   "
   "#    # ## ###     # #    # ## #    #   "
   "#  # #  # #    #  # #  # #  # #  # #   "
   " ##   ### #     ##   ##   ###  ##  ####"])

(describe "part2 solution"
  (it "works for provided input"
      (let [painted-text (paint (:painted-panels (paint-hull robot/robot true)))]
        (should= (str/join "\n" expected-painted-text-strs) painted-text))))