(ns advent-of-code-2019.n-body-problem.cycle-length-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.n-body-problem.time-steps :refer :all]
            [advent-of-code-2019.n-body-problem.cycle-length :refer :all]
            [clojure.string :as str]))

(def static-moons
  [(create-moon "m1" [1 2 3] [0 0 0]),
   (create-moon "m2" [1 2 3] [0 0 0])])

;each step: update velocity, apply velocity
;
;pos A no vel (initial state)
;pos B with velocity
;pos B no vel
;pos A with vel
;pos A no vel (initial state)
(def oscillating-moons
  [(create-moon "m1" [0 0 0] [0 0 0]),
   (create-moon "m2" [0 0 1] [0 0 0])])

(def test-data1
  (str/trim "
  <x=-1, y=0, z=2>
  <x=2, y=-10, z=-7
  <x=4, y=-8, z=8
  <x=3, y=5, z=-1>"))

(def test-lines1 (str/split test-data1 #"\n"))
(def test-moons1 (create-moons test-lines1))

;Need the start index and the period of the cycle
(describe
  "cycle-length"
  (it "1 step for static moons"
      (should= 1 (axis-cycle-length static-moons :x))
      (should= 1 (axis-cycle-length static-moons :y))
      (should= 1 (axis-cycle-length static-moons :z)))
  (it "2 step for oscillating moons"
      (should= 4 (axis-cycle-length oscillating-moons :z)))
  (it "works per-axis"
      (should= 2772 (cycle-length test-moons1))
      ;(should= 4686774924 (cycle-length test-moons2)) ;SLOW
      )
  )
