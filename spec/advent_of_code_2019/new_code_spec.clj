(ns advent-of-code-2019.new-code-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.new-code :refer :all]))

(describe "run"
          (it "works for test input"
              (should= 2 (run "test"))))
