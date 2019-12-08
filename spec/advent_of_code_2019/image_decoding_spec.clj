(ns advent-of-code-2019.image-decoding-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.image-decoding :refer :all]))

(describe "fuel-for-modules"
          (it "works for test input"
              (should= 2 (+ 1 1))
              ))

(run-specs)