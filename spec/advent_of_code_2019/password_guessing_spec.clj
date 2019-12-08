(ns advent-of-code-2019.password-guessing-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.password-guessing :refer :all]))

(describe "never-decrease?"
          (it "works for test input"
              (should= true (never-decrease? 111))
              (should= false (never-decrease? 211))))

(describe "same-adj-digs?"
          (it "works for test input"
              (should= true (same-adj-digs? 112))
              (should= false (same-adj-digs? 121))))

(describe "meets-criteria"
          (it "works for test input"
              (should= true (meets-orig-criteria? 111))
              (should= false (meets-orig-criteria? 121))
              (should= true (meets-orig-criteria? 111111))
              (should= false (meets-orig-criteria? 223450))
              (should= false (meets-orig-criteria? 123789))))

(describe "options"
          (it "works for test input"
              (should= '(111 112 113 114 115 116 117 118 119) 
                       (orig-options 100 120))
              ))

(describe "countains-double-digit?"
          (it "works for test input"
              (should= true (countains-double-digit? 112))
              (should= false (countains-double-digit? 121))
              (should= false (countains-double-digit? 111))
              (should= true (countains-double-digit? 11122))))

(describe "meets-updated-criteria"
          (it "works for test input"
              (should= true (meets-updated-criteria? 112))
              (should= false (meets-updated-criteria? 111))
              (should= false (meets-updated-criteria? 121))
              (should= true (meets-updated-criteria? 112233))
              (should= false (meets-updated-criteria? 123444))
              (should= true (meets-updated-criteria? 111122))))


(run-specs)