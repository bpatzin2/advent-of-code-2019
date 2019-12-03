(ns advent-of-code-2019.core-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.core :refer :all]))

(describe "day1pt1"
          (it "works for real input"
              (should= 3361299 (day1pt1))))

(describe "day1pt2"
          (it "works for real input"
              (should= 5039071 (day1pt2))))

(describe "day2pt1"
          (it "works for real input"
              (should= 3931283 (day2pt1))))

(describe "day2pt2"
          (it "works for real input"
              (should= 6979 (day2pt2))))

(describe "day3pt1"
          (it "works for real input"
              (should= 1626 (day3pt1))))

(run-specs)