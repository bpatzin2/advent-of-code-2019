(ns advent-of-code-2019.amplification
  (:gen-class)
  (:require [advent-of-code-2019.intcode.intcode :as intcode]
            [clojure.math.combinatorics :as combo]))

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

(defn initialize-exe-state-if-needed [amp]
  (assoc amp
    :addr (get amp :addr 0)
    :output (get amp :output [])
    :relative-base (get amp :relative-base 0)))

(defn process-amp [amp prev-output]
  (let [initial-amp (initialize-exe-state-if-needed amp)]
    (loop [rem-inputs (concat (get amp :input) [prev-output])
           updated-amp initial-amp]
      (if
        (empty? rem-inputs)
        (assoc updated-amp :input [])
        (let [input (first rem-inputs)
              new-state (intcode/execute-segment updated-amp input)
              new-amp (merge updated-amp new-state)]
          (recur (rest rem-inputs) new-amp))))))

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