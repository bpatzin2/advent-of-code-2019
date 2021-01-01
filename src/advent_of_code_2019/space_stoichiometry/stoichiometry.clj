(ns advent-of-code-2019.space-stoichiometry.stoichiometry
  (:require [clojure.string :as str]
            [clojure.set :as set-lib]))

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

(defn decompose [ingredient reaction]
  (let [in (:in reaction)
        reaction-chem (first (keys (:out reaction)))
        reaction-quantity (first (vals (:out reaction)))
        quantity-required (first (vals ingredient))
        reaction-count (reaction-count quantity-required reaction-quantity)
        amount-produced (* reaction-count reaction-quantity)]
    {:result (into {} (required-inputs in reaction-count))
     :left-over {reaction-chem (- amount-produced quantity-required)}}))

