(ns advent-of-code-2019.intcode.intcode
  (:gen-class)
  (:require
    [advent-of-code-2019.intcode.instruction :as inst]))

(defn mem-access [prog addr]
  (get prog addr 0))

(defn prog-to-vec [prog start len]
  (reduce #(conj %1 (mem-access prog %2)) [] (range start (+ start len))))

(defn abort? [is-diag output]
  (and is-diag (and (not (empty? output)) (not= 0 (last output)))))

(defn init-program [program-vec]
  (apply merge (map-indexed hash-map program-vec)))

(defn execution-state [program addr opcode relative-base is-diag output]
  {:program program
   :output output
   :addr addr
   :relative-base relative-base
   :is-first false
   :status (if (= opcode 99) :stopped (if (abort? is-diag output) :aborted :paused))})

(defn init-state [program]
  {:program (init-program program)
   :output []
   :addr 0
   :relative-base 0
   :status :paused})

(defn create-ctx [program output relative-base]
  {:relative-base relative-base
   :program program
   :output output})

(defn instruction-length [program instruction-address]
  (inst/instruction-length (inst/opcode-from-val (mem-access program instruction-address))))

(defn get-instruction [program instruction-address]
  (let [inst-len (instruction-length program instruction-address)] 
    (prog-to-vec program instruction-address inst-len)))

(defn next-instruction-address [instruction-address opcode]
  (+ instruction-address (inst/instruction-length opcode)))

(defn pause-or-stop [instruction input-consumed is-diag output]
  (or (inst/stop? instruction)
      (and input-consumed (inst/input? instruction) )
      (abort? is-diag output)))

(defn publish-state [state prog-len]
  (let [program (:program state)] 
    (assoc state :program (prog-to-vec program 0 prog-len))))

(defn execute-segment
  ([program addr input output relative-base]  
   (execute-segment program addr input output relative-base false false))
  ([program addr input output relative-base is-first]  
   (execute-segment program addr input output relative-base is-first false))
  ([program addr input output relative-base is-first is-diag]
   (loop [instruction-address addr
          exe-ctx (create-ctx (if is-first (init-program program) program) output relative-base)
          input-consumed (= nil input)]
     (let [curr-program (get exe-ctx :program)
           curr-output (get exe-ctx :output)
           instruction (get-instruction curr-program instruction-address)
           opcode (inst/get-opcode instruction)]
       (if
        (pause-or-stop instruction input-consumed is-diag curr-output)
        (execution-state curr-program instruction-address opcode (get exe-ctx :relative-base) is-diag curr-output)
        (let [exe-result (inst/execute-instruction instruction input exe-ctx)
              next-addr (or (get exe-result :next-addr) (next-instruction-address instruction-address opcode))
              is-input-consumed (or input-consumed (= opcode 3))]
          (recur next-addr exe-result is-input-consumed)))))))

(defn execute-segment-diag
  ([program addr input output relative-base]
   (execute-segment program addr input output relative-base false true))
  ([program addr input output relative-base is-first]
   (execute-segment program addr input output relative-base is-first true)))

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