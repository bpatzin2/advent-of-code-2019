(ns advent-of-code-2019.intcode-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
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

(describe "find-inputs"
          (it "works for test input"
              (should= [12 2] (find-inputs [1 0 0 3 1 1 2 3 1 3 4 3 1 5 0 3 2 1 6 19 1 9 19 23 2 23 10 27 1 27 5 31 1 31 6 35 1 6 35 39 2 39 13 43 1 9 43 47 2 9 47 51 1 51 6 55 2 55 10 59 1 59 5 63 2 10 63 67 2 9 67 71 1 71 5 75 2 10 75 79 1 79 6 83 2 10 83 87 1 5 87 91 2 9 91 95 1 95 5 99 1 99 2 103 1 103 13 0 99 2 14 0 0]
                                           3931283))
              ))

(run-specs)