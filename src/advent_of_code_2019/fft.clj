(ns advent-of-code-2019.fft
  (:require [clojure.string :as str-lib]))

(defn apply-pattern [nums, pattern]
  (->> pattern
       (interleave nums)
       (partition 2)
       (map #(apply * %))
       (reduce +)
       (#(rem % 10))
       (Math/abs)))

(defn pattern-val [pos-in-list i]
  (let [base-pattern [0 1 0 -1]
        index (rem (quot i (inc pos-in-list)) 4)]
    (base-pattern index)))

(defn gen-pattern [pos-in-list, len]
  (loop [res []
         i 0]
    (if
     (= len (count res))
     res
     (let [val (pattern-val pos-in-list i)]
        (recur
         (if (>= i 1) (conj res val) res)
         (inc i))))))

(defn output-digit [pos-in-list nums]
  (apply-pattern nums (gen-pattern pos-in-list (count nums))))

(defn apply-phase [nums _]
  (flatten (map-indexed (fn [index, _] (output-digit index nums)) nums)))

(defn run-fft [nums phases]
  (reduce apply-phase nums (range 0 phases)))

(defn str-as-int-vec [str]
  (vec (map #(Integer/parseInt %) (str-lib/split str #""))))

(defn run-fft-str [str phases out-count] 
 (let [res (vec (run-fft (str-as-int-vec str) phases))]
   (apply clojure.core/str (subvec res 0 out-count))))