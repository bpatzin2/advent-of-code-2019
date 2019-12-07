(ns advent-of-code-2019.input-handling
  (:gen-class)
  (:require [clojure.string :as str]))

(defn newline-as-int-seq [file-name]
  (map #(Integer/parseInt %) (str/split (slurp file-name), #"\n")))

(defn csv-as-int-vec [file-name]
  (vec (map #(Integer/parseInt %) (str/split (slurp file-name), #","))))

(defn day1-num-seq []
  (newline-as-int-seq "day1_input.txt"))

(defn day2-num-vec []
  (csv-as-int-vec "day2_input_modified.txt"))

(defn day2-unmodified-num-vec []
  (csv-as-int-vec "day2_input.txt"))

(defn day3-string-pair []
  (str/split (slurp "day3_input.txt"), #"\n"))

(defn day5-num-vec []
  (csv-as-int-vec "day5_input.txt"))
