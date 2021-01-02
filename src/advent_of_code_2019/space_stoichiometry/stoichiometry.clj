(ns advent-of-code-2019.space-stoichiometry.stoichiometry
  (:require [clojure.string :as str]))

(defn parse-chem-quantity [quantity-chem-str]
  (let [qaunt-chem (str/split quantity-chem-str #" ")
        quant (Integer/parseInt (get qaunt-chem 0))
        chem (get qaunt-chem 1)]
    {chem quant}))

(defn parse-reaction [reaction-str]
  (let [inAndOut (str/split (str/trim reaction-str) #" => ")
        in-strs (str/split (get inAndOut 0) #", ")
        out (get inAndOut 1)]
    {:in (into {} (map parse-chem-quantity in-strs))
     :out (parse-chem-quantity out)}))

(defn parse-reactions [reaction-str]
  (mapv parse-reaction (str/split (str/trim reaction-str) #"\n")))

(defn requirement-satisfied? [ingredients reaction-requirement]
 (let [required-chem (key reaction-requirement)
       required-amount (val reaction-requirement)
       ingredient-amount (get ingredients required-chem 0)]
   (>= ingredient-amount required-amount)))

(defn requirements-satisfied? [ingredients reaction]
  (every? #(requirement-satisfied? ingredients %) (:in reaction)))

(defn consume [ingredients reaction-requirement]
  (let [required-chem (key reaction-requirement)
        required-amount (val reaction-requirement)
        ingredient-amount (get ingredients required-chem 0)]
    {required-chem (- ingredient-amount required-amount)}))

(defn consume-all [ingredients reaction]
  (let [inputs (:in reaction)
        new-ingredient-amounts (map #(consume ingredients %) inputs)]
    (into ingredients new-ingredient-amounts)))

(defn apply-reaction [ingredients reaction]
  (merge-with + (consume-all ingredients reaction) (:out reaction)))

(defn contains-fuel? [ingredients-sets]
  (let [ks (flatten (map keys ingredients-sets))]
    (not (empty? (filter #(= "FUEL" %) ks)))))

(defn reaction-count [ingredient-quantity reaction-quantity]
  (let [r (rem ingredient-quantity reaction-quantity)
        q (quot ingredient-quantity reaction-quantity)]
    (if (= 0 r) q (inc q))))

(defn required-inputs [input reaction-count]
  (map (fn [[k v]] {k (* v reaction-count)}) input))

(defn output-chem [reaction]
  (first (keys (:out reaction))))

(defn output-quantity [reaction]
  (first (vals (:out reaction))))

(defn all-output-chems [reactions]
  (map output-chem reactions))

(defn ingredient-chem [ingredient]
  (first ingredient))

(defn ingredient-quantity [ingredient]
  (second ingredient))

(defn decompose [ingredient reaction]
  (let [in (:in reaction)
        reaction-chem (output-chem reaction)
        reaction-quantity (output-quantity reaction)
        quantity-required (ingredient-quantity ingredient)
        reaction-count (reaction-count quantity-required reaction-quantity)
        amount-produced (* reaction-count reaction-quantity)]
    {:result (into {} (required-inputs in reaction-count))
     :left-over {reaction-chem (- amount-produced quantity-required)}}))

(defn find-and-decompose [ingredient reactions]
  (let [reaction (first (filter #(= (ingredient-chem ingredient) (output-chem %)) reactions))]
    (decompose ingredient reaction)))

(defn remaining? [remaining-ingredients reaction]
  (contains? (set remaining-ingredients) (output-chem reaction)))

(defn not-still-input [ingredient remaining reactions]
  (let [remaining-reactions (filter #(remaining? remaining %) reactions)
        remaining-inputs (set (flatten (map keys (map :in remaining-reactions))))]
    (not (contains? remaining-inputs ingredient))))

(defn next-ingredient [remaining reactions]
  (let [candidates (filter #(not-still-input % remaining reactions) remaining)]
    (first candidates)))

(defn topo-sort [reactions]
  (loop [result []
         remaining (all-output-chems reactions)]
    (if (empty? remaining)
      result
      (let [next (next-ingredient remaining reactions)]
        (recur (conj result next) (remove #(= next %) remaining))))))

(defn find-ingredient [ingredients ingredient]
  (first (filter #(= ingredient (ingredient-chem %)) ingredients)))

(defn decompose-all2 [reactions]
  (let [ingredient-order (topo-sort reactions)]
    (loop [rest ingredient-order
           result {"FUEL" 1}]
      (if (empty? rest)
        result
        (let [next (find-ingredient result (first rest))
              decomposed (find-and-decompose next reactions)
              remove-decomposed (dissoc result (ingredient-chem next))
              new-result (merge-with + remove-decomposed (:result decomposed))]
          (recur (drop 1 rest) new-result))))))

(defn min-ore-to-reach-fuel [reactions]
  (get (decompose-all2 reactions) "ORE"))

(defn parse-and-min-ore-to-reach-fuel [reactions-str]
  (let [reactions (parse-reactions reactions-str)]
    (min-ore-to-reach-fuel reactions)))