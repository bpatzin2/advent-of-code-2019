(ns advent-of-code-2019.orbit-mapping-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.orbit-mapping :refer :all]))

(describe 
 "orbit-map"
 (it "works for test input"
     (should= {"A" ["B" "C"]} (orbit-map [["A" "B"] ["A" "C"]])))
 )

(describe
 "orbit-count"
 (it "works for test input"
     (should= 4 (orbit-count [["A" "B"]
                              ["A" "C"]
                              ["C" "Y"]]
                             "A"))
     
     (should= 7 (orbit-count [["A" "B"]
                              ["A" "C"]
                              ["C" "Y"]
                              ["Y" "Z"]]
                             "A"))
     
     (should= 42 (orbit-count [["COM" "B"]
                               ["B" "C"]
                               ["C" "D"]
                               ["D" "E"]
                               ["E" "F"]
                               ["B" "G"]
                               ["G" "H"]
                               ["D" "I"]
                               ["E" "J"]
                               ["J" "K"]
                               ["K" "L"]]
                              "COM"))
     
     ))

(describe
 "req-transfer-count"
 (it "works for test input"
     (should= 3 (req-transfer-count [["A" "B"]
                                     ["A" "C"]
                                     ["C" "Y"]
                                     ["Y" "YOU"]
                                     ["B" "SAN"]]
                                    "A"
                                    "YOU"
                                    "SAN")))
  (it "works for test input"
     (should= 2 (req-transfer-count [["COM" "B"]
                                     ["B" "C"]
                                     ["C" "D"]
                                     ["D" "E"]
                                     ["E" "F"]
                                     ["B" "G"]
                                     ["G" "H"]
                                     ["D" "I"]
                                     ["E" "J"]
                                     ["J" "K"]
                                     ["K" "L"]]
                                    "COM"
                                    "L"
                                    "F"))
      ))