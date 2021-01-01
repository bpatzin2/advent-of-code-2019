(ns advent-of-code-2019.space-stoichiometry.stoichiometry
  (:require [clojure.string :as str]))

(defn parse-chem-quantity [quantity-chem-str]
  (let [qaunt-chem (str/split quantity-chem-str #" ")
        quant (Integer/parseInt (get qaunt-chem 0))
        chem (get qaunt-chem 1)]
    {chem quant}))

(defn parse-reaction [reaction-str]
  (let [inAndOut (str/split reaction-str #" => ")
        in-strs (str/split (get inAndOut 0) #", ")
        out (get inAndOut 1)]
    {:in (into {} (map parse-chem-quantity in-strs))
     :out (parse-chem-quantity out)}))

(defn parse-reactions [reaction-str]
  (mapv parse-reaction (str/split reaction-str #"\n")))

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
  (into (consume-all ingredients reaction) (:out reaction)))

;TODO memoize
(defn possible-next-states [ingredients, reactions]
  (let [applicable-reactions (filter #(requirements-satisfied? ingredients %) reactions)]
    (mapv #(apply-reaction ingredients %) applicable-reactions)))

(defn all-possible-next-states [ingredients-sets, reactions]
  (apply concat (mapv #(possible-next-states % reactions) ingredients-sets)))


