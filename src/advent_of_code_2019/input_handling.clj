(ns advent-of-code-2019.input-handling
  (:gen-class)
  (:require [clojure.string :as str]))

(defn day1-num-seq []
  (map #(Integer/parseInt %) (str/split (slurp "day1_input.txt"), #"\n")))
