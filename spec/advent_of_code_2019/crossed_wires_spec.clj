(ns advent-of-code-2019.crossed-wires-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.crossed-wires :refer :all]))

(describe "wire-coords"
          (it "works for test input"
              (should= ['(0 0)] (wire-coords []))
              (should= ['(0 0) '(1 0) '(2 0)] (wire-coords ['("R" 2)]))
              (should= ['(0 0) '(0 1) '(0 2)] (wire-coords ['("U" 2)]))
              (should= ['(0 0) '(-1 0) '(-2 0)] (wire-coords ['("L" 2)]))
              (should= ['(0 0) '(0 -1) '(0 -2)] (wire-coords ['("D" 2)]))
              (should= ['(0 0) 
                        '(1 0) '(2 0)
                        '(2 1) '(2 2)] (wire-coords ['("R" 2) '("U" 2)]))
              ))

(describe "parse"
          (it "works for test input"
              (should= ['("R" 1)] (parse "R1"))
              (should= ['("R" 1) '("U" 11) '("L" 222) '("D" 9)] (parse "R1,U11,L222,D9"))))

(describe "closest-overlap-coords"
          (it "works for test input"
              (should= [1 0] (closest-overlap-coords [[1 0]] [[1 0]]))
              (should= [10 0] (closest-overlap-coords [[0 0] [1 0] [10 0] [100 0]] 
                                                     [[0 0] [2 0] [10 0] [100 0]]))
              ))

(describe "closet-overlap"
          (it "works for test input"
              (should= [1 0] (closet-overlap "R2" "R2"))
              (should= [-1 2] (closet-overlap "R2,U2,L10" "L1,U10"))))

(describe "closet-overlap-dist"
          (it "works for test input"
              (should= 1 (closet-overlap-dist "R2" "R2"))
              (should= 3 (closet-overlap-dist "R2,U2,L10" "L1,U10"))
              (should= 159 (closet-overlap-dist "R75,D30,R83,U83,L12,D49,R71,U7,L72" "U62,R66,U55,R34,D71,R55,D58,R83"))
              (should= 135 (closet-overlap-dist "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51" "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"))
              ))

(describe "shortest-overlap"
          (it "works for test input"
              (should= 2 (shortest-overlap "R2" "R2"))
              (should= 10 (shortest-overlap "R2,U2,L10" "L1,U10"))
              (should= 610 (shortest-overlap "R75,D30,R83,U83,L12,D49,R71,U7,L72" "U62,R66,U55,R34,D71,R55,D58,R83"))
              (should= 410 (shortest-overlap "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51" "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"))
              ))

(run-specs)