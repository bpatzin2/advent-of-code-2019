(ns advent-of-code-2019.space-stoichiometry.maximize-output-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.space-stoichiometry.reactions :as reaction]
            [advent-of-code-2019.space-stoichiometry.maximize-output :refer :all]))

(describe "bsearch-at-most"
    (it "bsearch-at-most"
        (should= 4 (bsearch-at-most identity 4 0 10))
        (should= 5 (bsearch-at-most #(* 2 %) 11 0 10))))

(describe "maximize-fuel"
  (it "maximize-fuel"
      (let [reactions [{:in {"ORE" 1} :out {"A" 1}}
                       {:in {"A" 2} :out {"FUEL" 1}}]]
        (should= 2 (maximize-fuel 4 reactions)))))

(def med1-example
 "
  157 ORE => 5 NZVS
  165 ORE => 6 DCFZ
  44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
  12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
  179 ORE => 7 PSHF
  177 ORE => 5 HKGWZ
  7 DCFZ, 7 PSHF => 2 XJWVT
  165 ORE => 2 GPVTF
  3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT")

(describe "parse-and-maximize-fuel"
  (it "parse-and-maximize-fuel"
      (should= 82892753 (parse-and-maximize-fuel med1-example))))
