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

(defn test-robot-initializer [] {})

(defn run-test-robot [_ _ i]
  {:output (nth example-outputs i)
   :status (if (= (inc i) (count example-outputs)) :stopped :paused)})

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
      (let [result-state (paint-hull run-test-robot test-robot-initializer false)]
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
      (let [painting-result (paint-hull robot/actual-robot robot/robot-initializer false)]
        (should= 1876 (count (:painted-panels painting-result))))))

(defn get-row [x-range y filled-coords]
  (let [row-colors (map #(contains? filled-coords {:x % :y y}) x-range)
        chars (map #(if % "#" " ") row-colors)
        row-str (apply str chars)]
    row-str))

(defn paint [panel-colors]
  (let [white-panels (into {} (filter (fn [[_ v]] (= :white v)) panel-colors))
        filled-coords (set (keys white-panels))
        xs (map :x filled-coords)
        ys (map :y filled-coords)
        min-x (apply min xs)
        max-x (apply max xs)
        x-range (range min-x (inc max-x))
        min-y (apply min ys)
        max-y (apply max ys)
        y-range (range max-y (dec min-y) -1)
        rows (map #(get-row x-range % filled-coords) y-range)]
    (str/join "\n" rows)))

(def sample-paint {{:x 1 :y 1} :white
                   {:x 0 :y 0} :white
                   {:x -1 :y -1} :black})

(println (paint sample-paint))

(println (paint (:painted-panels (paint-hull robot/actual-robot robot/robot-initializer true))))
