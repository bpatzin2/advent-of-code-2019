(ns advent-of-code-2019.space-stoichiometry.stoichiometry-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.space-stoichiometry.stoichiometry :refer :all]))

(describe "parsing"
  (it "parses a reaction"
      (should= {:in {"ORE" 9} :out {"A" 2}}
               (parse-reaction "9 ORE => 2 A"))
      (should= {:in {"A" 7 "E" 1} :out {"FUEL" 1}}
               (parse-reaction "7 A, 1 E => 1 FUEL")))
  (it "parses reactions"
      (should= [{:in {"ORE" 10} :out {"A" 10}}
                {:in {"ORE" 1} :out {"B" 1}}]
               (parse-reactions "10 ORE => 10 A\n1 ORE => 1 B"))))
