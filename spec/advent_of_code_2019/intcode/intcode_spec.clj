(ns advent-of-code-2019.intcode.intcode-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
            [advent-of-code-2019.intcode.intcode :refer :all]))

(describe "intcode"
 (it "add, multiply, and terminate opcodes"
     (should= [99 8 7 6] (execute [99 8 7 6]))
     (should= [2,0,0,0,99] (execute [1,0,0,0,99]))
     (should= [2,3,0,6,99] (execute [2,3,0,3,99]))
     (should= [2,4,4,5,99,9801] (execute [2,4,4,5,99,0]))
     (should= [30,1,1,4,2,5,6,0,99] (execute [1,1,1,4,99,5,6,0,99]))
     (should= [3500,9,10,70,2,3,11,0,99,30,40,50]
              (execute [1,9,10,3,2,3,11,0,99,30,40,50])))

 (it "input opcode"
     (should= [0 0 4 0 99] (execute [3 0 4 0 99]))
     (should= [2 0 4 0 99] (execute [3 0 4 0 99], [2])))

 (it "output opcode"
     (should=
      {:program [4 0 99] :output [4]}
      (select-keys (execute-with-output [4 0 99] [2]) [:program :output]))
     (should=
      {:program [3 3 4 2 1101 1 2 9 4 3 99 0] :output [4 2]}
      (select-keys (execute-with-output [3 3 4 2 1101 1 2 9 4 3 99 0] [2]) [:program :output])))

 (it "immediate mode"
     (should= [3,1,2,0,99] (execute [1101,1,2,0,99]))
     (should= [1002,4,3,4,99] (execute [1002,4,3,4,33]))
     (should= [1101,100,-1,4,99] (execute [1101,100,-1,4,0])))

 (it "equals op code"
     (should=
      0
      (diagnostic-code [3,9,8,9,10,9,4,9,99,-1,8] [2]))

     (should=
      1
      (diagnostic-code [3,9,8,9,10,9,4,9,99,-1,8] [8]))

     (should=
      0
      (diagnostic-code [3,3,1108,-1,8,3,4,3,99] [2]))

     (should=
      1
      (diagnostic-code [3,3,1108,-1,8,3,4,3,99] [8])))

 (it "less than op code"
     (should=
      1
      (diagnostic-code [3,9,7,9,10,9,4,9,99,-1,8] [2]))

     (should=
      0
      (diagnostic-code [3,9,7,9,10,9,4,9,99,-1,8] [8]))

     (should=
      1
      (diagnostic-code [3,3,1107,-1,8,3,4,3,99] [2]))

     (should=
      0
      (diagnostic-code [3,3,1107,-1,8,3,4,3,99] [8])))

 (it "jmp-if-true"
     (should= 42 (diagnostic-code [1005,9,6,104,42,99,104,69,99,0] [])))

 (it "branching op codes"
     (should= 0 (diagnostic-code [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] [0]))
     (should= 1 (diagnostic-code [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] [8]))
     (should= 0 (diagnostic-code [3,3,1105,-1,9,1101,0,0,12,4,12,99,1] [0]))
     (should= 1 (diagnostic-code [3,3,1105,-1,9,1101,0,0,12,4,12,99,1] [8])))

 (it "complex input"
     (should= 999 (diagnostic-code
                 [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99]
                 [0]))
     (should= 1000 (diagnostic-code
                 [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99]
                 [8]))
     (should= 1001 (diagnostic-code
                 [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99]
                 [9])))

 (it "execution-state"
     (should= {:program [3 11 3 12 1 11 12 12 4 12 99 1 -1]
               :output []
               :addr 2
               :is-first false
               :relative-base 0
               :status :paused}
              (execute-segment
                {:program [3 11 3 12 1 11 12 12 4 12 99 -1 -1]
                 :addr 0
                 :output []
                 :relative-base 0
                 :is-first false}
                1)))

 (it "relative mode"
     (should= [44402,0,0,0,99] (execute [22201,0,0,0,99]))
     (should= 109 (diagnostic-code [109,2000,109,19,204,-2019,99] [])))

 (it "large values"
     (should= 42 (diagnostic-code [203,1985,109,2000,109,19,204,-34,99] [42]))
     (should= [109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99]
              (:output (execute-with-output [109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99] [])))
     (should= 1219070632396864 (diagnostic-code [1102,34915192,34915192,7,4,7,99,0] []))
     (should= 1125899906842624 (diagnostic-code [104,1125899906842624,99] []))))

(run-specs)