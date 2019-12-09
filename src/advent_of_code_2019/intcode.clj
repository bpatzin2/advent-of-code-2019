(ns advent-of-code-2019.intcode
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math]))

(defn mem-access [prog addr]
  (get prog addr 0))

(defn prog-to-vec [prog start len]
  (reduce #(conj %1 (mem-access prog %2)) [] (range start (+ start len))))

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
  (opcode-ins-lengh (get-opcode (mem-access program instruction-address))))

(defn get-instruction [program instruction-address]
  (let [inst-len (instruction-length program instruction-address)] 
    (prog-to-vec program instruction-address inst-len)))

(defn next-instruction-address [instruction-address opcode]
  (+ instruction-address (opcode-ins-lengh opcode)))

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

(defn execute-instruction [instruction program input output relative-base]
  (let [opcode (get-opcode (get instruction 0))
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
     :relative-base (if (= opcode 9) (adjust-relative-base instruction program relative-base) relative-base)
     }
    ))

(defn abort? [is-diag output]
 (and is-diag (and (not (empty? output)) (not= 0 (last output)))))

(defn pause-or-stop [opcode input-consumed is-diag output]
  (or (= opcode 99) 
      (and input-consumed (= opcode 3))
      (abort? is-diag output)))

(defn execution-state [program output addr opcode relative-base is-diag output]
  {:program program
   :output output
   :addr addr
   :relative-base relative-base
   :status (if (= opcode 99) :stopped (if (abort? is-diag output) :aborted :paused))})

(defn init-state [program]
  {:program (apply merge (map-indexed hash-map program))
   :output []
   :addr 0
   :relative-base 0
   :status :paused})

(defn publish-state [state prog-len]
  (let [program (:program state)] 
    (assoc state :program (prog-to-vec program 0 prog-len))))

(defn execute-segment 
  ([program addr input output relative-base] 
   (execute-segment program addr input output relative-base false))
  ([program addr input output relative-base is-diag]
   (loop [instruction-address addr
          output output
          curr-program program
          rb relative-base
          input-consumed false]
     (let [instruction (get-instruction curr-program instruction-address)
           opcode (get-opcode (first instruction))
           next-addr (next-instruction-address instruction-address opcode)]
       (if
        (pause-or-stop opcode input-consumed is-diag output)
         (execution-state curr-program output instruction-address opcode rb is-diag output) 
         (let [exe-result (execute-instruction instruction curr-program input output rb)
               next-addr-from-instr (get exe-result :next-addr)]
           (recur
            (or next-addr-from-instr next-addr)
            (get exe-result :output)
            (get exe-result :program)
            (get exe-result :relative-base)
            (or input-consumed (= opcode 3)))))))))

(defn execute-with-output 
  ([program inputs] (execute-with-output program inputs false))
  ([program inputs diag-mode]
   (loop [state (init-state program)
          inputs inputs]
     (if 
      (or (= :stopped (:status state)) (= :aborted (:status state)))
       (publish-state state (count program))
       (let [prog (:program state)
             addr (:addr state)
             output (:output state)
             relative-base (:relative-base state)
             next-state (execute-segment prog addr (first inputs) output relative-base diag-mode)]
         (recur next-state (rest inputs)))))))

(defn execute
  ([program] (execute program [0]))
  ([program inputs] (get (execute-with-output program inputs) :program)))

(defn diagnostic-code [program inputs]
  (last (get (execute-with-output program inputs) :output)))