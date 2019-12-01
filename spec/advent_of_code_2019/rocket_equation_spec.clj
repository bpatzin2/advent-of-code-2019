(ns advent-of-code-2019.rocket-equation-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.rocket-equation :refer :all]))

(describe "works"
          (it "works for test input"
              (should= 2 (total-fuel-required [12]))
              (should= 2 (total-fuel-required [14]))
              (should= 654 (total-fuel-required [1969]))
              (should= 33583 (total-fuel-required [100756]))
              (should= 658 (total-fuel-required [12 14 1969]))))

(run-specs)