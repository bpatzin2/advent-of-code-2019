(ns advent-of-code-2019.intcode.instruction)

(defn opcode-from-val [first-instr-val]
  (rem first-instr-val 100))

(defn get-opcode [instruction]
  (opcode-from-val (first instruction)))

(defn stop? [instruction]
  (= 99 (get-opcode instruction)))

(defn input? [instruction]
  (= 3 (get-opcode instruction)))