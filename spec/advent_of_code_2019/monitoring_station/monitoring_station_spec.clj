(ns advent-of-code-2019.monitoring-station.monitoring-station-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.monitoring-station.monitoring-station :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.input-handling :as input]))

(def simple-grid
  [[\.\.\#]
   [\.\#\.]
   [\#\.\#]])

(def complex-grid
  [[\.\.\#\#]
   [\.\#\.\.]
   [\#\.\#\.]])

(describe
  "get-visible-asteroid-coords"
  (it "works for simple input"
      (let [visible-asteroids (visible-asteroid-coords
                               (create-coord 0 0) simple-grid)]
        (should= [{:x 2, :y 0} {:x 1, :y 1} {:x 0, :y 2}]  (vec visible-asteroids))))
  (it "works for complex input"
      (let [visible-asteroids (visible-asteroid-coords
                                (create-coord 3 2) complex-grid)]
        (should= #{{:x 2, :y 2} {:x 1, :y 1} {:x 2, :y 0} {:x 3, :y 0}}  (set visible-asteroids)))))

(def grid-with-clear-best
  [[\.\.\.]
   [\#\#\#]])

(describe
  "best-location-w-count"
  (it "works for simple input"
      (should= {:coord {:x 1 :y 1} :count 2}
               (best-location-w-count grid-with-clear-best))))

(describe
  "best-location-w-count"
  (it "works for test file input"
      (should= {:coord {:x 11, :y 13}, :count 210}
               (best-location-w-count (input/day10-grid "input/day10TestLarge.txt")))))

(describe
  "count-visible-from-best-loc"
  (it "works for test file input"
      (should= {:coord {:x 11, :y 13}}
               (best-location (input/day10-grid "input/day10TestLarge.txt")))))