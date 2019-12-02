(ns advent-of-code-2019.core-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.core :refer :all]))

(describe "day1pt1"
          (it "works for real input"
              (should= 3361299 (day1pt1))))

(describe "day1pt2"
          (it "works for real input"
              (should= 5039071 (day1pt2))))

(run-specs)