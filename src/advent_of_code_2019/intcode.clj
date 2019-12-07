(ns advent-of-code-2019.intcode
  (:gen-class))

(defn opcode-ins-lengh [opcode]
  (case opcode
    99 1
    1 4
    2 4
    3 2
    4 2))

(defn instruction-length [program instruction-address]
  (opcode-ins-lengh (program instruction-address)))

(defn get-instruction [program instruction-address]
  (subvec 
   program 
   instruction-address 
   (+ instruction-address (instruction-length program instruction-address))))

(defn next-instruction-address [instruction-address opcode]
  (+ instruction-address (opcode-ins-lengh opcode)))

(defn get-param [num instruction program]
  (let [param-addr (get instruction num)]
    (if-not (= param-addr nil) (program param-addr))))

(defn execute-add [a b output-addr program]
  (let [add-result (+ a b)]
    (assoc program output-addr add-result)))

(defn execute-mult [a b output-addr program]
  (let [mult-result (* a b)]
    (assoc program output-addr mult-result)))

(defn execute-instruction [instruction program]
  (let [opcode (get instruction 0)
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
          opcode (first instruction)
          next-addr (next-instruction-address instruction-address opcode)]
      (if
       (= opcode 99)
        curr-program
        (recur next-addr (execute-instruction instruction curr-program))))))
