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
    8 4
    9 2))

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

(defn get-param [p-num instruction program relative-base]
  (let [mode (get-mode instruction p-num)
        param-val (get instruction p-num)]
    (if-not (= param-val nil) 
        (case mode
          0 (program param-val)
          1 param-val
          2 (program (+ param-val relative-base))
            
            ))))

(defn execute-add [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        output-addr (get instruction 3)
        add-result (+ a b)]
    (assoc program output-addr add-result)))

(defn execute-mult [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        output-addr (get instruction 3)
        add-result (* a b)]
    (assoc program output-addr add-result)))

(defn execute-input [instruction program input]
  (let [output-addr (get instruction 1)]
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
        output-addr (get instruction 3)]
    (assoc program output-addr val)))

(defn eq-instr [instruction program relative-base]
  (let [a (get-param 1 instruction program relative-base)
        b (get-param 2 instruction program relative-base)
        val (if (= a b) 1 0)
        output-addr (get instruction 3)]
    (assoc program output-addr val)))

(defn adjust-relative-base [instruction program relative-base]
  (let [adjustment (get-param 1 instruction program relative-base)]
    (+ relative-base adjustment)))

(defn execute-instruction [instruction program input output relative-base]
  (let [opcode (get-opcode (get instruction 0))
        new-program (case opcode
                      1 (execute-add instruction program relative-base)
                      2 (execute-mult instruction program relative-base)
                      3 (execute-input instruction program input)
                      7 (less-than instruction program relative-base)
                      8 (eq-instr instruction program relative-base)
                      program)]
    {:program new-program 
     :output (if (= opcode 4) (execute-output instruction program output relative-base) output)
     :next-addr (case opcode
                  5 (jump-if-true instruction program relative-base)
                  6 (jump-if-false instruction program relative-base)
                  nil)
     :relative-base (if (= opcode 9) (adjust-relative-base instruction program relative-base) relative-base)
     }
    ))

(defn pause-or-stop [opcode input-consumed]
  (or (= opcode 99) (and input-consumed (= opcode 3))))

(defn execution-state [program output addr opcode relative-base]
  {:program program
   :output output
   :addr addr
   :relative-base relative-base
   :status (if (= opcode 99) :stopped :paused)})

(defn init-state [program]
  {:program (vec (concat program (take 1000000 (repeat 0))))
   :output []
   :addr 0
   :relative-base 0
   :status :paused})

(defn execute-segment [program addr input output relative-base]
   (loop [instruction-address addr
          output output
          curr-program program
          rb relative-base
          input-consumed false]
     (let [instruction (get-instruction curr-program instruction-address)
           opcode (get-opcode (first instruction))
           next-addr (next-instruction-address instruction-address opcode)]
       (if
        (pause-or-stop opcode input-consumed)
         (execution-state curr-program output instruction-address opcode relative-base) 
         (let [exe-result (execute-instruction instruction curr-program input output rb)
               next-addr-from-instr (get exe-result :next-addr)]
           (recur
            (or next-addr-from-instr next-addr)
            (get exe-result :output)
            (get exe-result :program)
            (get exe-result :relative-base)
            (or input-consumed (= opcode 3))))))))

(defn execute-with-output [program inputs]
  (loop [state (init-state program)
         inputs inputs]
    (if 
     (= :stopped (:status state))
      (assoc state :program (subvec (:program state) 0 (count program)))
      (let [prog (:program state)
            addr (:addr state)
            output (:output state)
            relative-base (:relative-base state)
            next-state (execute-segment prog addr (first inputs) output relative-base)]
       (recur next-state (rest inputs))))))

(defn execute
  ([program] (execute program [0]))
  ([program inputs] (get (execute-with-output program inputs) :program)))

(defn diagnostic-code [program inputs]
  (last (get (execute-with-output program inputs) :output)))