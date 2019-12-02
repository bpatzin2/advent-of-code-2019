(ns advent-of-code-2019.intcode-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.intcode :refer :all]))

(describe "intcode"
          (it "works for test input"
              (should= [99 8 7 6] (execute [99 8 7 6]))
              (should= [2,0,0,0,99] (execute [1,0,0,0,99]))
              (should= [2,3,0,6,99] (execute [2,3,0,3,99]))
              (should= [2,4,4,5,99,9801] (execute [2,4,4,5,99,0]))
              (should= [30,1,1,4,2,5,6,0,99] (execute [1,1,1,4,99,5,6,0,99]))
              (should= [3500,9,10,70,2,3,11,0,99,30,40,50]
                       (execute [1,9,10,3,2,3,11,0,99,30,40,50]))
              ))

              

(run-specs)