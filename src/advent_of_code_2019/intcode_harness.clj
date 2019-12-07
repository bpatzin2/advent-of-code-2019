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