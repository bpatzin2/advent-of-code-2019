(ns advent-of-code-2019.intcode-harness-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
            [advent-of-code-2019.intcode-harness :refer :all]))

(describe "find-inputs"
          (it "works for test input"
              (should= [12 2] (find-inputs 3931283
                                           [1 0 0 3 1 1 2 3 1 3 4 3 1 5 0 3 2 1 6 19 1 9 19 23 2 23 10 27 1 27 5 31 1 31 6 35 1 6 35 39 2 39 13 43 1 9 43 47 2 9 47 51 1 51 6 55 2 55 10 59 1 59 5 63 2 10 63 67 2 9 67 71 1 71 5 75 2 10 75 79 1 79 6 83 2 10 83 87 1 5 87 91 2 9 91 95 1 95 5 99 1 99 2 103 1 103 13 0 99 2 14 0 0]
                                           ))
              ))

(describe "run-in-series"
          (it "works for one phase setting"
              (should= 42 (run-in-series [3 0 4 0 99] [42])))
          (it "works for three phase settings"
              (should= 3 (run-in-series [3 11 3 12 1 11 12 12 4 12 99 -1 -1] [1 1 1]))))

(describe "find-largest-output"
          (it "works for test input"
              (should= 43210 (find-largest-output 
                              [3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0]
                              [0 1 2 3 4]))
              (should= 54321 (find-largest-output
                              [3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0]
                              [0 1 2 3 4]))
              (should= 65210 (find-largest-output
                              [3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0]
                              [0 1 2 3 4]))
              ))

(describe "process-amp"
           (it "works for test input"
               (should= {:program [3 3 99 42]
                         :output []
                         :addr 2
                         :name "a"
                         :input []
                         :status :stopped}
                        (process-amp {:program [3 3 99 0]
                                      :name "a"
                                      :input []}
                                     42))  
               
               (should= {:program [3 11 3 12 1 11 12 12 4 12 99 2 -1]
                         :output []
                         :addr 2
                         :name "a"
                         :input []
                         :status :paused}
                        (process-amp {:program [3 11 3 12 1 11 12 12 4 12 99 1 -1]
                                      :name "a"
                                      :input []}
                                     2))

               (should= {:program [3 11 3 12 1 11 12 12 4 12 99 42 44]
                         :output [44]
                         :addr 10
                         :name "a"
                         :input []
                         :status :stopped}
                        (process-amp {:program [3 11 3 12 1 11 12 12 4 12 99 1 -1]
                                      :name "a"
                                      :input [42]}
                                     2))
               ))

(describe "run-in-loop"
          (it "works for test input"
              (should= 139629729
                       (run-in-loop 
                        [3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5]
                        [9,8,7,6,5]))
              (should= 18216
                       (run-in-loop
                        [3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10]
                        [9,7,8,5,6]))
              ))

(describe "find-largest-loop-output"
          (it "works for test input"
              (should= 139629729
                       (find-largest-loop-output
                        [3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5]
                        [5,6,7,8,9]))
              (should= 18216
                       (find-largest-loop-output
                        [3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10]
                        [5,6,7,8,9]))))

(run-specs)