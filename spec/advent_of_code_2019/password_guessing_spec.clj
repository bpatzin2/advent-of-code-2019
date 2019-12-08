(ns advent-of-code-2019.password-guessing-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.password-guessing :refer :all]))

(describe "options"
          (it "works for test input"
              (should= 2 (options 0 1))
              ))


(run-specs)