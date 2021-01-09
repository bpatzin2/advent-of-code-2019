(ns advent-of-code-2019.amplification
  (:gen-class)
  (:require [advent-of-code-2019.intcode.intcode :as intcode]
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

(defn merge-amps [new-amp amps index]
  (assoc amps index new-amp))

(defn process-amp [amp prev-output] 
  (loop [rem-inputs (concat (get amp :input) [prev-output])
         addr (get amp :addr 0)
         outputs (get amp :output [])
         relative-base (get amp :relative-base 0)
         updated-amp amp
         program (get amp :program)]
    (if
     (empty? rem-inputs)
      (assoc updated-amp :input [])
      (let [input (first rem-inputs)
            new-state (intcode/execute-segment program addr input outputs relative-base)
            new-amp (merge updated-amp new-state)
            next-addr (:addr new-amp)
            new-outputs (vec (concat outputs (:output new-amp)))
            new-relative-base (:relative-base new-amp)
            new-prog (:program new-amp)]
        (recur (rest rem-inputs) next-addr new-outputs new-relative-base new-amp new-prog)))))

(defn run-in-loop [program phase-settings]
(loop [amps   
       [{:program program :name "a" :input [(phase-settings 0)]}
        {:program program :name "b" :input [(phase-settings 1)]}
        {:program program :name "c" :input [(phase-settings 2)]}
        {:program program :name "d" :input [(phase-settings 3)]}
        {:program program :name "e" :input [(phase-settings 4)]}]
       curr-index 0
       prev-output 0]
  (if
   (and (= 0 curr-index) (= :stopped (get (amps 4) :status)))
    (last (get (amps 4) :output))
    (let [amp (amps curr-index)
          updated-amp (process-amp amp prev-output)
          updated-amps (merge-amps updated-amp amps curr-index)
          outputs (:output (updated-amps curr-index))
          next-index (rem (inc curr-index) 5)]
      (recur updated-amps next-index (last outputs))))))

(defn find-largest-loop-output [program phase-settings]
  (let [possible-settings (combo/permutations phase-settings)]
    (loop [rem-possible-settings possible-settings
           largest-output 0]
      (if (empty? rem-possible-settings)
        largest-output
        (recur
         (rest rem-possible-settings)
         (let [output (run-in-loop program (first rem-possible-settings))]
           (max output largest-output)))))))