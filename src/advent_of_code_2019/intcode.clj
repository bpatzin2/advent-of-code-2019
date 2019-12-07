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

(defn first-input [instruction program]
  (program (instruction 1)))

(defn second-input [instruction program]
  (program (instruction 2)))

(defn execute-add [instruction program]
  (let [add-result (+ (first-input instruction program) (second-input instruction program))]
    (assoc program (instruction 3) add-result)))

(defn execute-mult [instruction program]
  (let [mult-result (* (first-input instruction program) (second-input instruction program))]
    (assoc program (instruction 3) mult-result)))

(defn execute-instruction [instruction program]
  (let [opcode (first instruction)]
    (case opcode
      1 (execute-add instruction program)
      2 (execute-mult instruction program)
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
