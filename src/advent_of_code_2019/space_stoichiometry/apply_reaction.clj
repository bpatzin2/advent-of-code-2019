(ns advent-of-code-2019.space-stoichiometry.apply-reaction)

(defn contains-fuel? [ingredients-sets]
  (let [ks (flatten (map keys ingredients-sets))]
    (not (empty? (filter #(= "FUEL" %) ks)))))

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

