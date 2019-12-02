(ns advent-of-code-2019.intcode
  (:gen-class)
  (:require [clojure.math.combinatorics :as combo]))

(defn get-instruction [program instruction-index]
  (vec ((vec (partition 4 4 [0 0 0] program)) instruction-index)))

(defn first-input [instruction program]
  (program (instruction 1)))

(defn second-input [instruction program]
  (program (instruction 2)))

(defn execute-add [instruction program]
  (let [add-result (+ (first-input instruction program) (second-input instruction program))]
    (assoc program (instruction 3) add-result)))

(defn execute-mult [instruction program]
  (let [mult-result (* (first-input instruction program) (second-input instruction program))]
    (assoc program (instruction 3) mult-result)))

(defn execute [program]
  (loop [instruction-index 0
         curr-program program]
    (let [instruction (get-instruction curr-program instruction-index)
          opcode (first instruction)]
      (case opcode
        99 curr-program
        1 (recur (inc instruction-index) (execute-add instruction curr-program))
        2 (recur (inc instruction-index) (execute-mult instruction curr-program))))))

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

(defn find-inputs [program desired-output]
  (->> program
       (all-test-programs) 
       (map execute)
       (filter-desired-output desired-output)
       (first)
       (pluck-input)))
