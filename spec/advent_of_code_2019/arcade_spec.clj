(ns advent-of-code-2019.arcade-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.arcade :refer :all]))

(describe "count-blocks"
          (it "works for test input"
              (should= {[1 2] 3
                        [6 5] 4} (draw-tiles '(1,2,3,6,5,4)))
              (should= 2 (count-blocks {[1 2] 2
                                        [6 5] 4
                                        [6 6] 2}))))

(describe "play-game"
          (it "move right wins"
              (should= 1 (play-game [3 1
                                     4 1
                                     99]))
              )
          (it "move left wins"
              (should= 1 (play-game [3 42
                                     102 -1 42 43
                                     4 43
                                     99])))
          (it "move right twice wins"
              (should= 2 (play-game [3 42
                                     ;output block
                                     104 0 
                                     104 0
                                     104 02
                                     3 43
                                     1 42 43 44 ;add the two inputs
                                     4 44
                                     99])))
          )