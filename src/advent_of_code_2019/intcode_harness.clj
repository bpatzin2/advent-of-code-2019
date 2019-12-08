(ns advent-of-code-2019.intcode-harness
  (:gen-class)
  (:require [advent-of-code-2019.intcode :as intcode]
            [clojure.math.combinatorics :as combo]))

(defn all-test-inputs []
  (combo/cartesian-product (range 0 100) (range 0 100)))

(defn create-test-program [program, test-input-pair]
  (assoc program 1 (first test-input-pair) 2 (second test-input-pair)))

(defn all-test-programs [program]
  (map #(create-test-program program %) (all-test-inputs)))

(defn is-desired-program [program desired-output]
  (= desired-output (first program)))

(defn filter-desired-output [desired-output programs]
  (filter #(is-desired-program % desired-output) programs))

(defn pluck-input [program]
  [(nth program 1) (nth program 2)])

(defn find-inputs [desired-output program]
  (->> program
       (all-test-programs)
       (map intcode/execute)
       (filter-desired-output desired-output)
       (first)
       (pluck-input)))

(defn run-in-series [program phase-settings]
  (loop [rem-phase-settings phase-settings
         input-signal 0]
    (if (empty? rem-phase-settings)
        input-signal
        (recur 
         (rest rem-phase-settings) 
         (intcode/diagnostic-code 
          program 
          [(first rem-phase-settings) input-signal])))))

(defn find-largest-output [program phase-settings]
  (let [possible-settings (combo/permutations phase-settings)]
    (loop [rem-possible-settings possible-settings
           largest-output 0]
    (if (empty? rem-possible-settings)
        largest-output
        (recur 
         (rest rem-possible-settings) 
         (let [output (run-in-series program (first rem-possible-settings))]
               (max output largest-output)))))))