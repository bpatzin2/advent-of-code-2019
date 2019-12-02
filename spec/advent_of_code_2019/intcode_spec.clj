(ns advent-of-code-2019.intcode-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.intcode :refer :all]))

(describe "intcode"
          (it "works for test input"
              (should= 2 (intcode))))

(run-specs)