(ns advent-of-code-2019.n-body-problem.energy-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.n-body-problem.time-steps :refer :all]
            [advent-of-code-2019.n-body-problem.energy :refer :all]
            [clojure.string :as str]))

;<x=-1, y=0, z=2>
;<x=2, y=-10, z=-7>
;<x=4, y=-8, z=8>
;<x=3, y=5, z=-1>
(def moons-for-updating
  [(create-moon "m1" [-1 0 2] [0 0 0]),
   (create-moon "m2" [2 -10 -7] [0 0 0]),
   (create-moon "m3" [4 -8 8] [0 0 0])
   (create-moon "m4" [3 5 -1] [0 0 0])])

(describe
 "energy"
 (it "works for test input"
     (let [total-energy (total-energy moons-for-updating 10)]
       (should= 179 total-energy)
       )))

(def test-data2
  (str/trim "
    <x=-8, y=-10, z=0>
    <x=5, y=5, z=10>
    <x=2, y=-7, z=3>
    <x=9, y=-8, z=-3>
  "))

(def test-lines2 (str/split test-data2 #"\n"))
(def test-moons2 (create-moons test-lines2))

(describe
 "test input"
 (it "works for test input"
     (let [total-energy (total-energy test-moons2 100)]
       (should= 1940 total-energy))))