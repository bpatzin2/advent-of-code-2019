(ns advent-of-code-2019.space-stoichiometry.stoichiometry
  (:require
    [advent-of-code-2019.space-stoichiometry.reactions :as reaction]
    [advent-of-code-2019.space-stoichiometry.topological-sort :as ts]))

(defn reaction-count [ingredient-quantity reaction-quantity]
  (let [r (rem ingredient-quantity reaction-quantity)
        q (quot ingredient-quantity reaction-quantity)]
    (if (= 0 r) q (inc q))))

(defn required-inputs [input reaction-count]
  (map (fn [[k v]] {k (* v reaction-count)}) input))

(defn decompose [ingredient reaction]
  (let [in (:in reaction)
        reaction-chem (reaction/output-chem reaction)
        reaction-quantity (reaction/output-quantity reaction)
        quantity-required (reaction/ingredient-quantity ingredient)
        reaction-count (reaction-count quantity-required reaction-quantity)
        amount-produced (* reaction-count reaction-quantity)]
    {:result (into {} (required-inputs in reaction-count))
     :left-over {reaction-chem (- amount-produced quantity-required)}}))

(defn find-and-decompose [ingredient reactions]
  (let [reaction (first (filter #(= (reaction/ingredient-chem ingredient) (reaction/output-chem %)) reactions))]
    (decompose ingredient reaction)))

(defn find-ingredient [ingredients ingredient]
  (first (filter #(= ingredient (reaction/ingredient-chem %)) ingredients)))

; Sort the reactions so that each ingredient type is only
; ever decomposed once. i.e. All reactions that decompose
; into a particular ingredient are decomposed before
; decomposing that ingredient.
(defn decompose-all [reactions initial-ingredients]
  (let [ingredient-order (ts/topological-sort reactions)]
    (loop [rest ingredient-order
           result initial-ingredients]
      (if (empty? rest)
        result
        (let [next (find-ingredient result (first rest))
              decomposed (find-and-decompose next reactions)
              remove-decomposed (dissoc result (reaction/ingredient-chem next))
              new-result (merge-with + remove-decomposed (:result decomposed))]
          (recur (drop 1 rest) new-result))))))

(defn min-ore-to-reach-fuel [reactions]
  (get (decompose-all reactions {"FUEL" 1}) "ORE"))

(defn parse-and-min-ore-to-reach-fuel [reactions-str]
  (let [reactions (reaction/parse-reactions reactions-str)]
    (min-ore-to-reach-fuel reactions)))