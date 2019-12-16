(ns advent-of-code-2019.fft-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.fft :refer :all]))

(describe "gen-pattern"
          (it "works for test input"
              (should= [1 0 -1 0 1 0 -1 0] (gen-pattern 0 8))
              (should= [0 1 1 0 0 -1 -1 0] (gen-pattern 1 8))))

(describe "run-fft"
          (it "works for test input"
              (should= [4 8 2 2 6 1 5 8] 
                       (run-fft [1 2 3 4 5 6 7 8] 1))
              (should= [3 4 0 4 0 4 3 8]
                       (run-fft [1 2 3 4 5 6 7 8] 2))
              ))

(describe "run-fft-str"
          (it "works for test input"
              (should= "24176176"
                       (run-fft-str "80871224585914546619083218645595" 
                                    100 8))
              ))