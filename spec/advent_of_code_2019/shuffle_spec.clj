(ns advent-of-code-2019.shuffle-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.shuffle :refer :all]))

(describe "shuffle-deck"
          (it "works for test input"
              (should= [2 1 0] 
                       (shuffle-deck 3 ["deal into new stack"]))
              (should= [0 1 2]
                       (shuffle-deck 3 ["deal into new stack"
                                        "deal into new stack"]))
              (should= [2 0 1]
                       (shuffle-deck 3 ["cut 2"]))
              (should= [1 2 0]
                       (shuffle-deck 3 ["cut -2"]))
              (should= [0 7 4 1 8 5 2 9 6 3]
                       (shuffle-deck 10 ["deal with increment 3"]))
              ))


(describe "examples"
          (it "works for test input"
              (should= [0 3 6 9 2 5 8 1 4 7]
                       (shuffle-deck 10 ["deal with increment 7"
                                         "deal into new stack"
                                         "deal into new stack"]))
              
                (should= [9 2 5 8 1 4 7 0 3 6]
                         (shuffle-deck 10 ["deal into new stack"
                                           "cut -2"
                                           "deal with increment 7"
                                           "cut 8"
                                           "cut -4"
                                           "deal with increment 7"
                                           "cut 3"
                                           "deal with increment 9"
                                           "deal with increment 3"
                                           "cut -1"]))
              ))

(run-specs)