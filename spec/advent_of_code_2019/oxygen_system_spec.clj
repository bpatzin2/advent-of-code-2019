(ns advent-of-code-2019.oxygen-system-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.oxygen-system :refer :all]
            [advent-of-code-2019.core :as core]))

(describe "run"
          (it "move-all-dirs"
              (should= [0 0 0 2] 
                       (map #(last (:output (:exe-state %))) 
                            (move-all-dirs 
                             (init-droid-state
                              [3 99
                               101 -4 99 100
                               104 0
                               1005 100 0
                               104 2 ;found it
                               99]
                              [0,0])))))
          
          (it "the oxygen is one move to the east"
              (should= 1 (num-steps-to-oxygen [3 99
                                               101 -4 99 100
                                               104 0
                                               1005 100 0
                                               104 2 ;found it
                                               99])))
          
          (it "the oxygen is two moves to the east"
              (should= 2 (num-steps-to-oxygen [3 99
                                               101 -4 99 100
                                               104 0
                                               1005 100 0
                                               104 1
                                               3 99
                                               101 -4 99 100
                                               1005 100 0
                                               104 2 ;found it
                                               99]))))

(describe "day15pt1"
          (it "works for real input"
              (should= 304 (core/day15pt1))))