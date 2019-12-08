(ns advent-of-code-2019.intcode
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math]))

(defn opcode-ins-lengh [opcode]
  (case opcode
    99 1
    1 4
    2 4
    3 2
    4 2
    5 3
    6 3
    7 4
    8 4))

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

(defn execute-input [instruction program input]
  (let [output-addr (get instruction 1)]
   (assoc program output-addr input)))

(defn execute-output [instruction program prev-output]
  (let [output-val (get-param 1 instruction program)]
   (conj prev-output output-val)))

(defn jump-if-true [instruction program]
  (let [is-true (not= 0 (get-param 1 instruction program))]
    (if is-true (get-param 2 instruction program) nil)))

(defn jump-if-false [instruction program]
  (let [is-true (= 0 (get-param 1 instruction program))]
    (if is-true (get-param 2 instruction program) nil)))

(defn less-than [instruction program]
  (let [a (get-param 1 instruction program)
        b (get-param 2 instruction program)
        val (if (< a b) 1 0)
        output-addr (get instruction 3)]
    (assoc program output-addr val)))

(defn eq-instr [instruction program]
  (let [a (get-param 1 instruction program)
        b (get-param 2 instruction program)
        val (if (= a b) 1 0)
        output-addr (get instruction 3)]
    (assoc program output-addr val)))

(defn execute-instruction [instruction program inputs output]
  (let [opcode (get-opcode (get instruction 0))
        new-program (case opcode
                      1 (execute-add instruction program)
                      2 (execute-mult instruction program)
                      3 (execute-input instruction program (first inputs))
                      7 (less-than instruction program)
                      8 (eq-instr instruction program)
                      program)]
    {:program new-program 
     :output (if (= opcode 4) (execute-output instruction program output) output)
     :inputs (if (= opcode 3) (rest inputs) inputs)
     :next-addr (case opcode
                  5 (jump-if-true instruction program)
                  6 (jump-if-false instruction program)
                  nil)
     }
    ))

(defn execute-with-output [program inputs]
   (loop [instruction-address 0
          output []
          inputs inputs
          curr-program program]
     (let [instruction (get-instruction curr-program instruction-address)
           opcode (get-opcode (first instruction))
           next-addr (next-instruction-address instruction-address opcode)]
       (if
        (= opcode 99)
         {:program curr-program
          :output output}
         (let [exe-result (execute-instruction instruction curr-program inputs output)
               next-addr-from-instr (get exe-result :next-addr)]
           (recur
            (or next-addr-from-instr next-addr)
            (get exe-result :output)
            (get exe-result :inputs)
            (get exe-result :program)))))))

(defn execute
  ([program] (execute program [0]))
  ([program inputs] (get (execute-with-output program inputs) :program)))

(defn diagnostic-code [program inputs]
  (last (get (execute-with-output program inputs) :output)))