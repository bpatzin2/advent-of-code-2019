(ns advent-of-code-2019.space-stoichiometry.apply-reaction-spec
  (:require [speclj.core :refer :all]
           [advent-of-code-2019.space-stoichiometry.apply-reaction :refer :all]))

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