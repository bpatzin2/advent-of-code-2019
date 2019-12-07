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

(defn execute-input [instruction program inputter]
  (let [output-addr (get instruction 1)]
   (assoc program output-addr (inputter))))

(defn execute-output [instruction program prev-output]
  (let [output-val (get-param 1 instruction program)]
   (conj prev-output output-val)))

(defn execute-instruction [instruction program inputter output prev-ins]
  (let [opcode (get-opcode (get instruction 0))
        new-program (case opcode
                      1 (execute-add instruction program)
                      2 (execute-mult instruction program)
                      3 (execute-input instruction program inputter)
                      4 program
                      5 program
                      6 program
                      7 program
                      8 program)]
    {:program new-program 
     :output (if (= opcode 4) (execute-output instruction program output) output)
     :ins (conj prev-ins instruction) 
     }
    ))

(defn execute-with-output
  ([program inputter] (execute-with-output program inputter -1))
  ([program inputter num-instructs]
   (loop [instruction-address 0
          n num-instructs
          output []
          ins []
          curr-program program]
     (let [instruction (get-instruction curr-program instruction-address)
           opcode (get-opcode (first instruction))
           next-addr (next-instruction-address instruction-address opcode)]
       (if
        (or (= opcode 99) (= 0 n))
         {:program curr-program
          :output output}
         (let [exe-result (execute-instruction instruction curr-program inputter output ins)
               next-addr-from-instr (get exe-result :next-addr)]
           (recur
            (or next-addr-from-instr next-addr)
            (dec n)
            (get exe-result :output)
            (get exe-result :ins)
            (get exe-result :program))))))))

(defn execute
  ([program] (execute program #(identity 0)))
  ([program inputter] (get (execute-with-output program inputter) :program)))

(defn diagnostic-code [program inputter]
  (last (get (execute-with-output program inputter) :output)))