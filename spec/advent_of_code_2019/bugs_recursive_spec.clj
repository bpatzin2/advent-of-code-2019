(ns advent-of-code-2019.bugs-recursive-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.bugs-recursive :refer :all]))

(def state0-text
  "
....#
#..#.
#..##
..#..
#....")

(def s1l0-text
  "
#..#.
####.
###.#
##.##
.##..")

(def s1l1-text
  "
....#
#..#.
#..##
..#..
#....")

(def s1l-1-text
  "
....#
#..#.
#..##
..#..
#....")

(def state0 (subs state0-text 1))
(def s1l0 (subs s1l0-text 1))
(def s1l1 (subs s1l1-text 1))
(def s1l-1 (subs s1l-1-text 1))

(describe "next-state"
          (it "works for test input"
              (should= [[1 2 -1] [1 0 0] [2 1 -1] [0 1 0]]
                       (adjacencies 0 0 0))
              (should= [[4 0 1] [4 1 1] [4 2 1] [4 3 1] [4 4 1] 
                        [4 2 0] [3 1 0] [3 3 0]]
                       (adjacencies 3 2 0))
              
              (should= 4 (count (adjacencies 3 3 0)))
              (should= 8 (count (adjacencies 1 2 0)))
              
              (should= (two-d-vec s1l0)
                       (get (next-state-str state0) 0))
              ))
