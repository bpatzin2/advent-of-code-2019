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
              (should= (two-d-vec s1l0)
                       (get (next-state-str state0) 0))
              ; (should= (two-d-vec s1l1)
              ;          (get (next-state-str state0) 1))

              ))
