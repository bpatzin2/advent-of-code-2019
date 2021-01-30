(ns advent-of-code-2019.fft.special-fft-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.fft.special-fft :refer :all]
            [advent-of-code-2019.core :as core]))

(describe "validates that the conditions are met for the special case"
          (it "throws when the conditions are not met"
              (should-throw (special-fft [1 2 3] 1 1))
              (should-not-throw (special-fft [1 2 3] 1 2))
              (should-throw (special-fft [1 2 3 4] 1 1))
              (should-not-throw (special-fft [1 2 3 4] 1 2))))

(describe "special-fft"
          (it "works"
              (should= [4] (special-fft [2 4] 1 1))
              (should= [6 4] (special-fft [0 0 2 4] 1 2))
              (should= [6 1 5 8] (special-fft [1 2 3 4 5 6 7 8] 1 4))
              (should= [0 4 3 8] (special-fft [1 2 3 4 5 6 7 8] 2 4))))

(describe "get-msg-offset"
          (it "works"
              (should= 1234567 (get-msg-offset [1 2 3 4 5 6 7 8]))))

(describe "repeat-signal"
          (it "works"
              (should= [1 2 1 2] (repeat-signal [1 2] 2))))