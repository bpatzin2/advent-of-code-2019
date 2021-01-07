(ns advent-of-code-2019.space-stoichiometry.apply-reaction)

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

