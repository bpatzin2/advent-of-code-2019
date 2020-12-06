(ns advent-of-code-2019.n-body-problem
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.n-body-problem :refer :all]
            [clojure.string :as str]))

(def test-moons
  [{:name "m1" :v [1 7 0] :pos [4 7 0]},
   {:name "m2" :v [1 2 3] :pos [4 5 6]},
   {:name "m3" :v [1 2 3] :pos [4 6 8]}])

(describe 
 "velocity-changes"
 (it "works for test input"
     (let [updated-moons (velocity-changes test-moons)]
       (should= '(0 -2 2)  (:dv (first updated-moons)))
       (should= '(0 2 0)  (:dv (second updated-moons)))
       (should= '(0 0 -2)  (:dv (nth updated-moons 2)))))
 )

(def test-data
  (str/trim "
  <x=-1, y=0, z=2>
  <x=2, y=-10, z=-7>
  <x=4, y=-8, z=8>
  <x=30, y=50, z=-10>
  "))

(def test-lines (str/split test-data #"\n"))

(describe
 "create-moons"
 (it "works for test input"
     (let [actual-moons (map-indexed create-moon test-lines)]
       (should= '(-1 0 2) (:pos (nth actual-moons 0)))
       (should= '(2 -10 -7) (:pos (nth actual-moons 1)))
       (should= '(4 -8 8) (:pos (nth actual-moons 2)))
       (should= '(30 50 -10) (:pos (nth actual-moons 3))))))

(describe
 "update-moon"
 (it "works for test input"
     (let [moon (create-moon 0 '(1 2 3) '(1 1 1))
           moon-w-dv (assoc moon :dv '(0 -1 2)) ]
       (should= '(1 0 3)  (new-velocity moon-w-dv))
       (should= '(1 0 3)  (:vel (apply-vel-update moon-w-dv)))
       (should= '(2 2 6)  (:pos (apply-vel-update moon-w-dv))))))