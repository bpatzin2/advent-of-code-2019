(ns advent-of-code-2019.input-handling
  (:gen-class)
  (:require [clojure.string :as str]))

(defn newline-as-int-seq [file-name]
  (map #(Integer/parseInt %) (str/split (slurp file-name), #"\n")))

(defn csv-as-int-vec [file-name]
  (vec (map #(Long/parseLong %) (str/split (str/trim (slurp file-name)), #","))))

(defn day1-num-seq []
  (newline-as-int-seq "input/day1.txt"))

(defn day2-num-vec []
  (csv-as-int-vec "day2_input_modified.txt"))

(defn day2-unmodified-num-vec []
  (csv-as-int-vec "day2_input.txt"))

(defn day3-string-pair []
  (str/split (slurp "day3_input.txt"), #"\n"))

(defn day5-num-vec []
  (csv-as-int-vec "day5_input.txt"))

(defn day6-tuples []
  (map #(str/split % #"\)") (str/split (slurp "day6_input.txt"), #"\n")))

(defn day7-num-vec []
  (csv-as-int-vec "day7_input.txt"))

(defn nums-str-as-vec [s]
  (vec (map #(Integer/parseInt %) (str/split s #""))))

(defn nums-as-vec [file-name]
  (nums-str-as-vec (slurp file-name)))

(defn day8-num-vec []
  (nums-as-vec "day8_input.txt"))

(defn day9-num-vec []
  (csv-as-int-vec "day9_input.txt"))

(defn day10-grid
  ([]
   (day10-grid "input/day10.txt"))
  ([pathname]
   (map seq (str/split (slurp pathname), #"\n"))))

(defn day12-list []
  (str/split (slurp "input/day12.txt"), #"\n"))

(defn day13-num-vec []
  (csv-as-int-vec "day13_input.txt"))

(defn day15-num-vec []
  (csv-as-int-vec "input/day15.txt"))

(defn day16-str []
  (slurp "day16_input.txt"))

(defn day20-str []
 (slurp "day20_input.txt"))

(defn day22-list []
  (str/split (slurp "day22_input.txt"), #"\n"))

(defn day24-str []
  (slurp "day24_input.txt"))
