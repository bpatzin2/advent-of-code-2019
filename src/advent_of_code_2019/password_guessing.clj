(ns advent-of-code-2019.password-guessing
  (:gen-class)
  (:require [clojure.string :as str]))

(defn char-vec [num]
  (str/split (str num) #""))

(defn dig-vec [num]
  (map #(Integer/parseInt %) (char-vec num)))

(defn never-decrease? [num]
  (not= false (reduce #(if (> %1 %2) (reduced false) %2) (dig-vec num))))

(defn same-adj-digs? [num]
  (= true (reduce #(or (= true %1) (if (= %1 %2) true %2)) (char-vec num))))

(defn meets-orig-criteria? [num]
  (and (same-adj-digs? num)
       (never-decrease? num)))

(defn meets-updated-criteria? [num]
  (and (same-adj-digs? num)
       (never-decrease? num)))

(defn orig-options [range-start range-end] 
  (filter meets-orig-criteria? (range range-start range-end)))

(defn updated-options [range-start range-end]
  (filter meets-updated-criteria? (range range-start range-end)))