(ns advent-of-code-2019.bugs-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.bugs :refer :all]))

(def state0-text
  "
....#
#..#.
#..##
..#..
#....")

(def state1-text
  "
#..#.
####.
###.#
##.##
.##..")

(def state2-text
  "
#####
....#
....#
...#.
#.###")

(def first-dup-text
  "
.....
.....
.....
#....
.#...")

(def state0 (subs state0-text 1))
(def state1 (subs state1-text 1))
(def state2 (subs state2-text 1))
(def first-dup (subs first-dup-text 1))

(describe "next-state"
          (it "works for test input"
              (should= (two-d-vec state1) 
                       (next-state state0))
              (should= (two-d-vec state2)
                       (next-state state1))
              ))

(describe "first-dup-state"
          (it "works for test input"
              (should= (two-d-vec first-dup)
                       (first-dup-state state0))))