(ns advent-of-code-2019.core-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.core :refer :all]))

(describe "works"
          (it "works for real input"
              (should= 1 (identity 1))))

(run-specs)