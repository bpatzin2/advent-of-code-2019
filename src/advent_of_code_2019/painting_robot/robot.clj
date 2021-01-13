(ns advent-of-code-2019.painting-robot.robot
  (:require [speclj.core :refer :all]
           [advent-of-code-2019.input-handling :as input]
           [advent-of-code-2019.intcode.intcode :as intcode]))

(defn init-state [program]
  {:program (intcode/init-program program)
   :addr 0
   :output []
   :relative-base 0
   :is-first true})

(defn get-initial-state []
  (init-state (input/csv-as-int-vec "input/day11.txt")))

(defn actual-robot [state input _]
  (let [robot-exe-state (:robot-exe-state state)
        next-exe-state (intcode/execute-segment robot-exe-state input)
        status (:status next-exe-state)
        output (:output next-exe-state)
        robot-state (assoc next-exe-state :output [])]
    {:output output :status status :robot-exe-state robot-state}))

(def robot {:initial-state (get-initial-state)
            :move actual-robot})