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

(describe "day6pt1"
          (it "works for real input"
              (should= 117672 (day6pt1))))

(describe "day6pt2"
          (it "works for real input"
              (should= 277 (day6pt2))))

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

(describe "day10pt1"
          (it "works for real input"
              (should= 326 (day10pt1))))

(describe "day10pt2"
          (it "works for real input"
              (should= 1623 (day10pt2))))

(describe "day12pt1"
          (it "works for real input"
              (should= 9876 (day12pt1))))

(describe "day12pt2"
          (it "works for real input"
              (should= 307043147758488 (day12pt2))))

(describe "day13pt1"
          (it "works for real input"
              (should= 286 (day13pt1))))

(describe "day14pt1"
          (it "works for test input"
              (should= 2210736 (day14pt1 "input/day14-test.txt")))
          (it "works for real input"
              (should= 362713 (day14pt1))))

(describe "day13pt2"
          (it "works for real input"
              (should= 146 (day13pt2 true))))

(describe "day15pt1"
          (it "works for real input"
              (should= 304 (day15pt1))))

(describe "day15pt2"
          (it "works for real input"
              (should= 310 (day15pt2))))

(describe "day16pt1"
          (it "works for real input"
              (should= "33717412" (day16pt1 3))))

(describe "day16pt2"
          (it "works for real input"
              (should= "37717791" (day16pt2))))