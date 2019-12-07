(ns advent-of-code-2019.intcode
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math]))

(defn opcode-ins-lengh [opcode]
  (case opcode
    99 1
    1 4
    2 4
    3 2
    4 2))

(defn get-opcode [first-instr-val]
  (rem first-instr-val 100))

(defn instruction-length [program instruction-address]
  (opcode-ins-lengh (get-opcode (program instruction-address))))

(defn get-instruction [program instruction-address]
  (subvec 
   program 
   instruction-address 
   (+ instruction-address (instruction-length program instruction-address))))

(defn next-instruction-address [instruction-address opcode]
  (+ instruction-address (opcode-ins-lengh opcode)))

(defn get-mode [instruction p-num]
  (rem (quot (instruction 0) (math/expt 10 (+ 1 p-num))) 10))

(defn get-param [p-num instruction program]
  (let [mode (get-mode instruction p-num)
        param-val (get instruction p-num)]
    (if-not (= param-val nil) 
      (if 
       (= mode 0) 
        (program param-val)
        param-val))))

(defn execute-add [instruction program]
  (let [a (get-param 1 instruction program)
        b (get-param 2 instruction program)
        output-addr (get instruction 3)
        add-result (+ a b)]
    (assoc program output-addr add-result)))

(defn execute-mult [instruction program]
  (let [a (get-param 1 instruction program)
        b (get-param 2 instruction program)
        output-addr (get instruction 3)
        add-result (* a b)]
    (assoc program output-addr add-result)))

(defn execute-input [instruction program input-fetcher]
  (let [output-addr (get instruction 1)]
   (assoc program output-addr (input-fetcher))))

(defn execute-instruction [instruction program input-fetcher]
  (let [opcode (get-opcode (get instruction 0))]
    (case opcode
      1 (execute-add instruction program)
      2 (execute-mult instruction program)
      3 (execute-input instruction program input-fetcher)
      4 program)))

(defn execute 
  ([program] (execute program #(identity 0)))
  ([program input-fetcher]
   (loop [instruction-address 0
          curr-program program]
     (let [instruction (get-instruction curr-program instruction-address)
           opcode (get-opcode (first instruction))
           next-addr (next-instruction-address instruction-address opcode)]
       (if
        (= opcode 99)
         curr-program
         (recur next-addr (execute-instruction instruction curr-program input-fetcher)))))))
