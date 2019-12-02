(ns advent-of-code-2019.intcode
  (:gen-class))

(defn get-instruction [program instruction-index]
  (vec ((vec (partition 4 4 [0 0 0] program)) instruction-index)))

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

(defn execute [program]
  (loop [instruction-index 0
         curr-program program]
    (let [instruction (get-instruction curr-program instruction-index)
          opcode (first instruction)]
      (case opcode
        99 curr-program
        1 (recur (inc instruction-index) (execute-add instruction curr-program))
        2 (recur (inc instruction-index) (execute-mult instruction curr-program))))))
