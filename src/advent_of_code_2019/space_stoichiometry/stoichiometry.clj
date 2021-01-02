(ns advent-of-code-2019.space-stoichiometry.stoichiometry
  (:require [advent-of-code-2019.space-stoichiometry.reactions :as reaction]))

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

(defn remaining? [remaining-ingredients reaction]
  (contains? (set remaining-ingredients) (reaction/output-chem reaction)))

(defn not-still-input [ingredient remaining reactions]
  (let [remaining-reactions (filter #(remaining? remaining %) reactions)
        remaining-inputs (set (flatten (map keys (map :in remaining-reactions))))]
    (not (contains? remaining-inputs ingredient))))

(defn next-ingredient [remaining reactions]
  (let [candidates (filter #(not-still-input % remaining reactions) remaining)]
    (first candidates)))

(defn topo-sort [reactions]
  (loop [result []
         remaining (reaction/all-output-chems reactions)]
    (if (empty? remaining)
      result
      (let [next (next-ingredient remaining reactions)]
        (recur (conj result next) (remove #(= next %) remaining))))))

(defn find-ingredient [ingredients ingredient]
  (first (filter #(= ingredient (reaction/ingredient-chem %)) ingredients)))

(defn decompose-all2 [reactions]
  (let [ingredient-order (topo-sort reactions)]
    (loop [rest ingredient-order
           result {"FUEL" 1}]
      (if (empty? rest)
        result
        (let [next (find-ingredient result (first rest))
              decomposed (find-and-decompose next reactions)
              remove-decomposed (dissoc result (reaction/ingredient-chem next))
              new-result (merge-with + remove-decomposed (:result decomposed))]
          (recur (drop 1 rest) new-result))))))

(defn min-ore-to-reach-fuel [reactions]
  (get (decompose-all2 reactions) "ORE"))

(defn parse-and-min-ore-to-reach-fuel [reactions-str]
  (let [reactions (reaction/parse-reactions reactions-str)]
    (min-ore-to-reach-fuel reactions)))