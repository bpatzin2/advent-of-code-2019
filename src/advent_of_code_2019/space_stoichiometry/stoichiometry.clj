(ns advent-of-code-2019.space-stoichiometry.stoichiometry
  (:require [clojure.string :as str]))

(defn parse-chem-quantity [quantity-chem-str]
  (let [qaunt-chem (str/split quantity-chem-str #" ")
        quant (Integer/parseInt (get qaunt-chem 0))
        chem (get qaunt-chem 1)]
    {chem quant}))

(defn parse-reaction [reaction-str]
  (let [inAndOut (str/split reaction-str #" => ")
        in-strs (str/split (get inAndOut 0) #" , ")
        out (get inAndOut 1)]
    {:in (into {} (map parse-chem-quantity in-strs))
     :out (parse-chem-quantity out)}))