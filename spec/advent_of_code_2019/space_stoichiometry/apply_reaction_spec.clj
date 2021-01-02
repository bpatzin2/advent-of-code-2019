(ns advent-of-code-2019.space-stoichiometry.apply-reaction-spec
  (:require [speclj.core :refer :all]
           [advent-of-code-2019.space-stoichiometry.apply-reaction :refer :all]))

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
          (let [ingredients {"ORE" 5 "A" 2 "B" 1 "C" 1}
                reaction {:in {"ORE" 5 "A" 1} :out {"C" 1}}
                expected {"ORE" 0 "A" 1 "B" 1 "C" 2}]
            (it "apply-reaction"
                (should= expected (apply-reaction ingredients reaction)))))

(describe "contains-fuel?"
          (it "contains-fuel?"
              (should= false (contains-fuel? [{"D" 1}]))
              (should= true (contains-fuel? [{"D" 1} {"FUEL" 2}]))))
