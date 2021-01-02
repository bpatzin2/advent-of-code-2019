(ns advent-of-code-2019.space-stoichiometry.maximize-output
  (:require [advent-of-code-2019.space-stoichiometry.reactions :as reaction]
            [advent-of-code-2019.space-stoichiometry.stoichiometry :as stoic]))

(defn bsearch-at-most [func x low high]
  "Binary Search to find the value between low and high
   which when func is applied to it produces a value closest to
   but at most x"
  (if (< high low)
    high ;return the lower one
    (let [middle (quot (+ low high) 2)
          v (func middle)]
      (cond (= x v) middle
            (< x v) (recur func x low (dec middle))
            (> x v) (recur func x (inc middle) high)))))

(defn maximize-fuel [ore-count reactions]
  (bsearch-at-most #(stoic/min-ore-to-reach-fuel reactions %) ore-count 0 ore-count))

(defn parse-and-maximize-fuel [reactions-str]
  (let [reactions (reaction/parse-reactions reactions-str)]
    (maximize-fuel 1000000000000 reactions)))