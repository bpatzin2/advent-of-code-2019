(ns advent-of-code-2019.space-stoichiometry.stoichiometry-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.space-stoichiometry.stoichiometry :refer :all]))

(describe "parsing"
  (it "parses a reaction"
      (should=
        {:in {"ORE" 9} :out {"A" 2}}
        (parse-reaction "9 ORE => 2 A"))
      (should=
        {:in {"A" 7 "E" 1} :out {"FUEL" 1}}
        (parse-reaction "7 A, 1 E => 1 FUEL")))
  (it "parses reactions"
      (should=
        [{:in {"ORE" 10} :out {"A" 10}}
         {:in {"ORE" 1} :out {"B" 1}}]
        (parse-reactions "10 ORE => 10 A\n1 ORE => 1 B"))))

(describe "requirement-satisfied?"
  (let [ingredients {"ORE" 5}]
    (it "requirement-satisfied?"
        (should=
          true
          (requirement-satisfied? ingredients (first {"ORE" 5})))
        (should=
          false
          (requirement-satisfied? ingredients (first {"ORE" 6})))
        (should=
          false
          (requirement-satisfied? ingredients (first {"B" 1}))))))

(describe "requirements-satisfied?"
  (let [ingredients {"ORE" 5 "A" 1 "B" 1}]
    (it "requirement-satisfied?"
        (should=
          true
          (requirements-satisfied? ingredients {:in {"ORE" 5 "A" 1}}))
        (should=
          false
          (requirements-satisfied? ingredients {:in {"ORE" 5 "A" 2}}))
        (should=
          false
          (requirements-satisfied? ingredients {:in {"ORE" 5 "A" 1 "C" 1}})))))

(describe "consume-all"
  (let [ingredients {"ORE" 5 "A" 2 "B" 1}]
    (it "consume-all"
        (should=
          {"ORE" 4}
          (consume ingredients (first {"ORE" 1})))
        (should=
          {"ORE" 0 "A" 1 "B" 1}
          (consume-all ingredients {:in {"ORE" 5 "A" 1}})))))

(describe "apply-reaction"
  (let [ingredients {"ORE" 5 "A" 2 "B" 1}
        reaction {:in {"ORE" 5 "A" 1} :out {"C" 1}}
        expected {"ORE" 0 "A" 1 "B" 1 "C" 1}]
    (it "apply-reaction"
        (should= expected (apply-reaction ingredients reaction)))))

(describe "possible-next-states"
  (let [ingredients {"ORE" 5 "A" 2 "B" 1}
        reactions [{:in {"ORE" 5 "A" 1} :out {"C" 1}}
                   {:in {"ORE" 1} :out {"D" 2}}
                   {:in {"ORE" 10} :out {"E" 1}}]
        expected [{"ORE" 0 "A" 1 "B" 1 "C" 1}
                  {"ORE" 4 "A" 2 "B" 1 "D" 2}]]
    (it "apply-reaction"
        (should= expected (possible-next-states ingredients reactions)))))


