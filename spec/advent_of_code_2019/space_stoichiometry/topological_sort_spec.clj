(ns advent-of-code-2019.space-stoichiometry.topological-sort-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.space-stoichiometry.reactions :as reaction]
            [advent-of-code-2019.space-stoichiometry.topological-sort :refer :all]))

(describe "topological-sort"
  (let [reactions [{:in {"ORE" 5 "A" 1} :out {"C" 1}}
                   {:in {"ORE" 1} :out {"A" 2}}]
        decomposes-to {"C" '("ORE" "A"), "A" '("ORE")}
        expected ["C" "A"]]
    (it "next-ingredient"
        (should= "C" (next-ingredient (keys decomposes-to) reactions)))
    (it "topo-sort"
        (should= expected (topological-sort reactions))))
  (let [reactions [{:in {"ORE" 2} :out {"A" 3}}
                   {:in {"ORE" 2 "A" 1} :out {"B" 2}}
                   {:in {"ORE" 1 "B" 1} :out {"FUEL" 2}}]
        expected ["FUEL" "B" "A"]]
    (it "next-ingredient"
        (should= "FUEL" (next-ingredient (reaction/all-output-chems reactions) reactions)))
    (it "topo-sort"
        (should= expected (topological-sort reactions)))))
