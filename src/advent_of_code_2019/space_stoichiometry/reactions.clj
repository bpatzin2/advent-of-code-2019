(ns advent-of-code-2019.space-stoichiometry.reactions
  (:require [clojure.string :as str]))

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
