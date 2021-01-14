(ns advent-of-code-2019.arcade-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.arcade :refer :all]))

(describe "count-blocks"
          (it "works for test input"
              (should= {[1 2] 3
                        [6 5] 4} (draw-tiles '(1,2,3,6,5,4)))
              (should= 2 (count-blocks {[1 2] 2
                                        [6 5] 4
                                        [6 6] 2}))
              (should= [6 5] (pos-of 4 {[1 2] 2
                                        [6 5] 4
                                        [6 6] 2}))))

(describe "play-game"
          (it "keep joystick neutral"
              (should= 0 (play-game [
                                     ;block at [1 1]
                                     104 1
                                     104 1
                                     104 2
                                     ;ball at [0 0]
                                     104 0
                                     104 0
                                     104 4
                                     ;paddle at [0 1]
                                     104 0
                                     104 1
                                     104 3
                                     ;get joystick mvmt
                                     3 1
                                     ;output input as score
                                     104 -1
                                     104 0
                                     4 1 
                                     99])))

          (it "move joystick left"
              (should= 1 (play-game [
                                      ;block at [1 1]
                                     104 2
                                     104 2
                                     104 2
                                     ;ball at [0 0]
                                     104 0
                                     104 0
                                     104 4
                                     ;paddle at [1 1]
                                     104 1
                                     104 1
                                     104 3
                                     3 99
                                     ;score is input * -1
                                     102 -1 99 100
                                     ;output score
                                     104 -1
                                     104 0
                                     4 100
                                     99]))))


(describe "score"
          (it "works"
              (should= 42 (score-from-output
                           [0 0 0
                            0 1 1
                            0 2 4
                            -1 0 42 ; score is 42
                            1 0 1]))))