(ns advent-of-code-2019.ascii-drawing-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.ascii-drawing :refer :all]
            [clojure.string :as str]))

(def expected-sample-paint
  [" #"
   "# "])

(describe "draw"
  (it "works"
      (should= (str/join "\n" expected-sample-paint)
               (draw [{:x 1 :y 1} {:x 0 :y 0}]))))
