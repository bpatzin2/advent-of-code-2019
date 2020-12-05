(ns advent-of-code-2019.n-body-problem
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.n-body-problem :refer :all]))

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