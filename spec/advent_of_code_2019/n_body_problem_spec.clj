(ns advent-of-code-2019.n-body-problem
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.n-body-problem :refer :all]
            [clojure.string :as str]))

;(def test-moons
;  [{:name "m1" :v [1 7 0] :pos [4 7 0]},
;   {:name "m2" :v [1 2 3] :pos [4 5 6]},
;   {:name "m3" :v [1 2 3] :pos [4 6 8]}])
;
;(describe
; "velocity-changes"
; (it "works for test input"
;     (let [updated-moons (velocity-changes test-moons)]
;       (should= '(0 -2 2)  (:dv (first updated-moons)))
;       (should= '(0 2 0)  (:dv (second updated-moons)))
;       (should= '(0 0 -2)  (:dv (nth updated-moons 2)))))
; )
;
;(def test-data
;  (str/trim "
;  <x=-1, y=0, z=2>
;  <x=2, y=-10, z=-7>
;  <x=4, y=-8, z=8>
;  <x=30, y=50, z=-10>
;  "))
;
;(def test-lines (str/split test-data #"\n"))
;
;(describe
; "create-moons"
; (it "works for test input"
;     (let [actual-moons (map-indexed create-moon test-lines)]
;       (should= '(-1 0 2) (:pos (nth actual-moons 0)))
;       (should= '(2 -10 -7) (:pos (nth actual-moons 1)))
;       (should= '(4 -8 8) (:pos (nth actual-moons 2)))
;       (should= '(30 50 -10) (:pos (nth actual-moons 3))))))
;
;(describe
; "apply-vel-update"
; (it "works for test input"
;     (let [moon (create-moon 0 '(1 2 3) '(1 1 1))
;           moon-w-dv (assoc moon :dv '(0 -1 2)) ]
;       (should= '(1 0 3)  (new-velocity moon-w-dv))
;       (should= '(1 0 3)  (:vel (apply-vel-update moon-w-dv)))
;       (should= '(2 2 6)  (:pos (apply-vel-update moon-w-dv))))))

(defn assert-moon [expected-pos expected-vel actual-moon]
  (should= expected-pos (:pos actual-moon))
  (should= expected-vel (:vel actual-moon)))

;<x=-1, y=0, z=2>
;<x=2, y=-10, z=-7>
;<x=4, y=-8, z=8>
;<x=3, y=5, z=-1>
(def moons-for-updating
  [(create-moon "m1" [-1 0 2] [0 0 0]),
   (create-moon "m2" [2 -10 -7] [0 0 0]),
   (create-moon "m3" [4 -8 8] [0 0 0])
   (create-moon "m4" [3 5 -1] [0 0 0])])

;pos=<x= 2, y=-1, z= 1>, vel=<x= 3, y=-1, z=-1>
;pos=<x= 3, y=-7, z=-4>, vel=<x= 1, y= 3, z= 3>
;pos=<x= 1, y=-7, z= 5>, vel=<x=-3, y= 1, z=-3>
;pos=<x= 2, y= 2, z= 0>, vel=<x=-1, y=-3, z= 1>
(describe
 "apply-time"
 (it "works for test input"
     (let [moons (apply-time moons-for-updating) ]
       (assert-moon '(2 -1 1) '(3 -1 -1) (nth moons 0))
       (assert-moon '(3 -7 -4) '(1 3 3) (nth moons 1))
       (assert-moon '(1 -7 5) '(-3 1 -3) (nth moons 2))
       (assert-moon '(2 2 0) '(-1 -3 1) (nth moons 3)))))

;After 0 steps:
;pos=<x=-1, y=  0, z= 2>, vel=<x= 0, y= 0, z= 0>
;After 1 step:
;pos=<x= 2, y=-1, z= 1>, vel=<x= 3, y=-1, z=-1>
;After 2 steps:
;pos=<x= 5, y=-3, z=-1>, vel=<x= 3, y=-2, z=-2>
;After 3 steps:
;pos=<x= 5, y=-6, z=-1>, vel=<x= 0, y=-3, z= 0>
(describe
 "apply-time with steps"
 (it "works for test input"
     (let [moons1 (apply-time moons-for-updating 1)
           moons2 (apply-time moons-for-updating 2)
           moons3 (apply-time moons-for-updating 3)]
       (assert-moon '(2 -1 1) '(3 -1 -1) (nth moons1 0))
       (assert-moon '(5 -3 -1) '(3 -2 -2) (nth moons2 0))
       (assert-moon '(5 -6 -1) '(0 -3 0) (nth moons3 0)))))

(describe
 "energy"
 (it "works for test input"
     (let [moons10 (apply-time moons-for-updating 10)
           total-energy (total-energy moons-for-updating 10)
           ]
       (assert-moon '(2 1 -3) '(-3 -2 1) (nth moons10 0))
       (should= 179 total-energy)
       )))

