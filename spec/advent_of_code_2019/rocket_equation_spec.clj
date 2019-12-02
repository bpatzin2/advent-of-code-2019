(ns advent-of-code-2019.rocket-equation-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.rocket-equation :refer :all]))

(describe "fuel-for-modules"
          (it "works for test input"
              (should= 2 (fuel-for-modules [12]))
              (should= 2 (fuel-for-modules [14]))
              (should= 654 (fuel-for-modules [1969]))
              (should= 33583 (fuel-for-modules [100756]))
              (should= 658 (fuel-for-modules [12 14 1969]))))

(describe "total-fuel"
          (it "works for test input"
              (should= 2 (total-fuel [12]))
              (should= 966 (total-fuel [1969]))
              (should= 968 (total-fuel [12 1969]))))

(run-specs)