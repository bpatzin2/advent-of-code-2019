(ns advent-of-code-2019.space-stoichiometry.stoichiometry-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.space-stoichiometry.reactions :as reaction]
            [advent-of-code-2019.space-stoichiometry.stoichiometry :refer :all]))

(describe "decompose"
  (let [ingredients ["C" 2]
        reaction {:in {"ORE" 5 "A" 1} :out {"C" 1}}
        expected {:result {"ORE" 10 "A" 2}
                  :left-over {"C" 0}}]
    (it "decompose"
        (should= expected (decompose ingredients reaction))))
  (let [ingredients ["C" 1]
        reaction {:in {"ORE" 5 "A" 1} :out {"C" 2}}
        expected {:result {"ORE" 5 "A" 1}
                  :left-over {"C" 1}}]
    (it "decompose"
        (should= expected (decompose ingredients reaction)))))

(describe "topo-sort"
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

(describe "decompose-all2"
          (let [reactions [{:in {"ORE" 2} :out {"A" 3}}
                           {:in {"ORE" 2 "A" 1} :out {"B" 2}}
                           {:in {"ORE" 1 "B" 1} :out {"FUEL" 2}}]
                expected {"ORE" 5}]
            (it "decompose-all2"
                (should= expected (decompose-all reactions {"FUEL" 1})))))

(describe "min-ore-to-reach-fuel"
          (let [reactions [{:in {"ORE" 2} :out {"C" 1}}
                           {:in {"ORE" 1 "C" 1} :out {"D" 1}}
                           {:in {"D" 2} :out {"FUEL" 1}}]]
            (it "min-ore-to-reach-fuel"
                (should= 6 (min-ore-to-reach-fuel reactions)))))

(def small-example
  "
  9 ORE => 2 A
  8 ORE => 3 B
  7 ORE => 5 C
  3 A, 4 B => 1 AB
  5 B, 7 C => 1 BC
  4 C, 1 A => 1 CA
  2 AB, 3 BC, 4 CA => 1 FUEL")

(def large-example
  "
171 ORE => 8 CNZTR
7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
114 ORE => 4 BHXH
14 VRPVC => 6 BMBT
6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
5 BMBT => 4 WPTQ
189 ORE => 9 KTJDG
1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
12 VRPVC, 27 CNZTR => 2 XDBXC
15 KTJDG, 12 BHXH => 5 XCVML
3 BHXH, 2 VRPVC => 7 MZWV
121 ORE => 7 VRPVC
7 XCVML => 6 RJRHP
5 BHXH, 4 VRPVC => 5 LTCX")

(describe "parse-and-min-ore-to-reach-fuel"
  (it "parse-and-min-ore-to-reach-fuel"
      (should= 165 (parse-and-min-ore-to-reach-fuel small-example))
      (should= 2210736 (parse-and-min-ore-to-reach-fuel large-example))))
