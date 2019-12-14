(ns advent-of-code-2019.arcade-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.arcade :refer :all]))

(describe "run"
          (it "works for test input"
              (should= {[1 2] 3
                        [6 5] 4} (draw-tiles '(1,2,3,6,5,4)))
              (should= 2 (count-blocks {[1 2] 2
                                        [6 5] 4
                                        [6 6] 2}))))