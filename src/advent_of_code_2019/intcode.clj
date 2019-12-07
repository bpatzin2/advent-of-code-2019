(ns advent-of-code-2019.intcode
  (:gen-class))

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

(defn get-mode [instruction]
  (rem (quot (instruction 0) 100) 10))

(defn get-param [num instruction program]
  (let [mode (get-mode instruction)
        param-val (get instruction num)]
    (if-not (= param-val nil) 
      (if 
       (= mode 0) 
        (program param-val)
        param-val))))

(defn execute-add [a b output-addr program]
  (let [add-result (+ a b)]
    (assoc program output-addr add-result)))

(defn execute-mult [a b output-addr program]
  (let [mult-result (* a b)]
    (assoc program output-addr mult-result)))

(defn execute-instruction [instruction program]
  (let [opcode (get-opcode (get instruction 0))
        first-param (get-param 1 instruction program)
        second-param (get-param 2 instruction program)
        output-addr (get instruction 3)]
    (case opcode
      1 (execute-add first-param second-param output-addr program)
      2 (execute-mult first-param second-param output-addr program)
      3 program
      4 program)))

(defn execute [program]
  (loop [instruction-address 0
         curr-program program]
    (let [instruction (get-instruction curr-program instruction-address)
          opcode (get-opcode (first instruction))
          next-addr (next-instruction-address instruction-address opcode)]
      (if
       (= opcode 99)
        curr-program
        (recur next-addr (execute-instruction instruction curr-program))))))
