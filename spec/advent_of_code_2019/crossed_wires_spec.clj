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


(run-specs)