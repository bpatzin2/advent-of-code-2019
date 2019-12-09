(ns advent-of-code-2019.orbit-mapping-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.orbit-mapping :refer :all]))

(describe "orbit-map"
          (it "works for test input"
              (should= 2 (orbit-map "test")))
          )
