(ns advent-of-code-2019.intcode.instruction
  (:require
    [clojure.math.numeric-tower :as math]))

(defn mem-access [prog addr]
  (get prog addr 0))

(defn instruction-length [opcode]
  (case opcode
    99 1
    1 4
    2 4
    3 2
    4 2
    5 3
    6 3
    7 4
    8 4
    9 2))

(defn opcode-from-val [first-instr-val]
  (rem first-instr-val 100))

(defn get-opcode [instruction]
  (opcode-from-val (first instruction)))

(defn stop? [instruction]
  (= 99 (get-opcode instruction)))

(defn input? [instruction]
  (= 3 (get-opcode instruction)))

(defn get-mode [instruction p-num]
  (rem (quot (instruction 0) (math/expt 10 (+ 1 p-num))) 10))

(defn get-param [p-num instruction program relative-base]
  (let [mode (get-mode instruction p-num)
        param-val (get instruction p-num 0)]
    (if-not (= param-val nil)
      (case mode
        0 (mem-access program param-val)
        1 param-val
        2 (mem-access program (+ param-val relative-base))))))

(defn get-addr-param [p-num instruction relative-base]
  (let [mode (get-mode instruction p-num)
        param-val (get instruction p-num)]
    (if-not (= param-val nil)
      (case mode
        0 param-val
        1 param-val
        2 (+ param-val relative-base)))))

(defn execute-add [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        output-addr (get-addr-param 3 instruction relative-base)
        add-result (+ a b)]
    (assoc program output-addr add-result)))

(defn execute-mult [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        output-addr (get-addr-param 3 instruction relative-base)
        add-result (* a b)]
    (assoc program output-addr add-result)))

(defn execute-input [instruction program input relative-base]
  (let [output-addr (get-addr-param 1 instruction relative-base)]
    (assoc program output-addr input)))

(defn execute-output [instruction program prev-output relative-base]
  (let [output-val (get-param 1 instruction program relative-base)]
    (conj prev-output output-val)))

(defn jump-if-true [instruction program relative-base]
  (let [is-true (not= 0 (get-param 1 instruction program relative-base))]
    (if is-true (get-param 2 instruction program relative-base) nil)))

(defn jump-if-false [instruction program relative-base]
  (let [is-true (= 0 (get-param 1 instruction program relative-base))]
    (if is-true (get-param 2 instruction program relative-base) nil)))

(defn less-than [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        val (if (< a b) 1 0)
        output-addr (get-addr-param 3 instruction relative-base)]
    (assoc program output-addr val)))

(defn eq-instr [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        val (if (= a b) 1 0)
        output-addr (get-addr-param 3 instruction relative-base)]
    (assoc program output-addr val)))

(defn adjust-relative-base [instruction program relative-base]
  (let [adjustment (get-param 1 instruction program relative-base)]
    (+ relative-base adjustment)))

(defn execute-instruction [instruction input exe-ctx]
  (let [opcode (get-opcode instruction)
        output (get exe-ctx :output)
        program (get exe-ctx :program)
        relative-base (get exe-ctx :relative-base)
        new-program (case opcode
                      1 (execute-add instruction program relative-base)
                      2 (execute-mult instruction program relative-base)
                      3 (execute-input instruction program input relative-base)
                      7 (less-than instruction program relative-base)
                      8 (eq-instr instruction program relative-base)
                      program)]
    {:program new-program
     :output (if (= opcode 4) (execute-output instruction program output relative-base) output)
     :next-addr (case opcode
                  5 (jump-if-true instruction program relative-base)
                  6 (jump-if-false instruction program relative-base)
                  nil)
     :relative-base (if (= opcode 9) (adjust-relative-base instruction program relative-base) relative-base)}))