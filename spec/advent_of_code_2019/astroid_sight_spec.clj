(ns advent-of-code-2019.astroid-sight-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.astroid-sight :refer :all]))

(describe "count-blocks"
          (it "works for test input"
              (should= 2 (run))
              ))