(ns advent-of-code-2019.crossed-wires-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.crossed-wires :refer :all]))

(describe "wire-coords"
          (it "works for test input"
              (should= ['(0 0)] (wire-coords []))
              (should= ['(0 0) 
                        '(1 0) '(2 0)] (wire-coords ['("R" 2)]))
              (should= ['(0 0) 
                        '(1 0) '(2 0)
                        '(2 1)] (wire-coords ['("R" 2) '("U" 1)]))
              ))


(run-specs)