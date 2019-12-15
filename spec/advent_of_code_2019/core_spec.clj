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

(describe "day3pt2"
          (it "works for real input"
              (should= 27330 (day3pt2))))

(describe "day4pt1"
          (it "works for real input"
              (should= 1864 (day4pt1))))

(describe "day4pt2"
          (it "works for real input"
              (should= 1258 (day4pt2))))

(describe "day5pt1"
          (it "works for real input"
              (should= 15508323 (day5pt1))))

(describe "day5pt2"
          (it "works for real input"
              (should= 9006327 (day5pt2))))

(describe "day7pt1"
          (it "works for real input"
              (should= 17790 (day7pt1))))

(describe "day7pt2"
          (it "works for real input"
              (should= 19384820 (day7pt2))))

(describe "day8pt1"
          (it "works for real input"
              (should= 1560 (day8pt1))))

(describe "day8pt2"
          (it "works for real input"
              (should= 
               '([1 0 0 1 0 0 1 1 0 0 0 1 1 0 0 1 0 0 1 0 1 0 0 1 0] 
                 [1 0 0 1 0 1 0 0 1 0 1 0 0 1 0 1 0 0 1 0 1 0 0 1 0] 
                 [1 0 0 1 0 1 0 0 0 0 1 0 0 0 0 1 0 0 1 0 1 1 1 1 0] 
                 [1 0 0 1 0 1 0 1 1 0 1 0 0 0 0 1 0 0 1 0 1 0 0 1 0] 
                 [1 0 0 1 0 1 0 0 1 0 1 0 0 1 0 1 0 0 1 0 1 0 0 1 0] 
                 [0 1 1 0 0 0 1 1 1 0 0 1 1 0 0 0 1 1 0 0 1 0 0 1 0])
               (day8pt2))))

(describe "day9pt1"
          (it "works for real input"
              (should= 3335138414 (day9pt1))))

(describe "day9pt1"
          (it "works for real input"
              (should= 49122 (day9pt2))))

(describe "day13pt1"
          (it "works for real input"
              (should= 286 (day13pt1))))

(describe "day13pt2"
          (it "works for real input"
              (should= 146 (day13pt2 true))))

(run-specs)