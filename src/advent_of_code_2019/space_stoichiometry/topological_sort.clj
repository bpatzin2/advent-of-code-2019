(ns advent-of-code-2019.space-stoichiometry.topological-sort
  (:require [advent-of-code-2019.space-stoichiometry.reactions :as reaction]))

(defn remaining? [remaining-ingredients reaction]
  (contains? (set remaining-ingredients) (reaction/output-chem reaction)))

(defn not-still-input [ingredient remaining reactions]
  (let [remaining-reactions (filter #(remaining? remaining %) reactions)
        remaining-inputs (set (flatten (map keys (map :in remaining-reactions))))]
    (not (contains? remaining-inputs ingredient))))

(defn next-ingredient [remaining reactions]
  (let [candidates (filter #(not-still-input % remaining reactions) remaining)]
    (first candidates)))

(defn topological-sort [reactions]
  (loop [result []
         remaining (reaction/all-output-chems reactions)]
    (if (empty? remaining)
      result
      (let [next (next-ingredient remaining reactions)]
        (recur (conj result next) (remove #(= next %) remaining))))))

