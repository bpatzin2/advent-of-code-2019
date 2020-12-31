(ns advent-of-code-2019.space-stoichiometry.stoichiometry-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.space-stoichiometry.stoichiometry :refer :all]))

(describe "parsing"
  (it "parses a reaction"
      (should= {:in {"ORE" 9} :out {"A" 2}}
               (parse-reaction "9 ORE => 2 A"))))
