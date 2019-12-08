(ns advent-of-code-2019.image-decoding-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.image-decoding :refer :all]))

(describe "decode"
          (it "works for test input"
              (should= [[[1] [2]]
                        [[3] [4]]
                        [[5] [6]]] (decode [1 2 3 4 5 6] 2 1))
              (should= [[
                         [0 1 2]
                         [0 1 2]
                         ]
                        [
                         [0 0 1]
                         [1 2 1]
                         ]] (decode [0 1 2 0 1 2 0 0 1 1 2 1] 2 3))
              ))

(describe "checksum"
          (it "works for test input"
              (should= 4 (checksum [0 0 0 1 1 1 2 2] 2 2))
              (should= 1 (checksum [1 2 3 4 5 6 7 8 9 0 1 2] 2 3))))

(describe "decode-combined"
          (it "works for test input"
              (should= [[1 1]
                        [0 0]] 
                       (decode-combined [2 2 2 2 1 1 0 0] 2 2))
              (should= [[0 1 0]
                        [0 1 1]] (decode-combined [0 1 2 0 1 2 0 0 0 1 2 1] 2 3))
              ))

(run-specs)