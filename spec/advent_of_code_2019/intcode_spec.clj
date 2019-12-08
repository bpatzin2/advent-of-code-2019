(ns advent-of-code-2019.intcode-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.input-handling :refer :all]
            [advent-of-code-2019.intcode :refer :all]))

(describe 
 "intcode"
 (it "add, multiply, and terminate opcodes"
     (should= [99 8 7 6] (execute [99 8 7 6]))
     (should= [2,0,0,0,99] (execute [1,0,0,0,99]))
     (should= [2,3,0,6,99] (execute [2,3,0,3,99]))
     (should= [2,4,4,5,99,9801] (execute [2,4,4,5,99,0]))
     (should= [30,1,1,4,2,5,6,0,99] (execute [1,1,1,4,99,5,6,0,99]))
     (should= [3500,9,10,70,2,3,11,0,99,30,40,50]
              (execute [1,9,10,3,2,3,11,0,99,30,40,50]))
     )
 
 (it "input opcode"
     (should= [0 0 4 0 99] (execute [3 0 4 0 99]))
     (should= [2 0 4 0 99] (execute [3 0 4 0 99], #(identity 2)))
     )

 (it "output opcode"
     (should= 
      {:program [4 0 99] :output [4]} 
      (execute-with-output [4 0 99] #(identity 2)))
     (should=
      {:program [3 3 4 2 1101 1 2 9 4 3 99 0] :output [4 2]}
      (execute-with-output [3 3 4 2 1101 1 2 9 4 3 99 0] #(identity 2)))
     )
 
 (it "immediate mode"
     (should= [3,1,2,0,99] (execute [1101,1,2,0,99]))
     (should= [1002,4,3,4,99] (execute [1002,4,3,4,33]))
     (should= [1101,100,-1,4,99] (execute [1101,100,-1,4,0]))
     )

 (it "equals op code"
     (should= 
      0
      (diagnostic-code [3,9,8,9,10,9,4,9,99,-1,8] #(identity 2)))
     
     (should=
      1
      (diagnostic-code [3,9,8,9,10,9,4,9,99,-1,8] #(identity 8)))
     
     (should=
      0
      (diagnostic-code [3,3,1108,-1,8,3,4,3,99] #(identity 2)))

     (should=
      1
      (diagnostic-code [3,3,1108,-1,8,3,4,3,99] #(identity 8)))

     )

 (it "less than op code"
     (should= 
      1
      (diagnostic-code [3,9,7,9,10,9,4,9,99,-1,8] #(identity 2)))
     
     (should=
      0
      (diagnostic-code [3,9,7,9,10,9,4,9,99,-1,8] #(identity 8)))
     
     (should= 
      1
      (diagnostic-code [3,3,1107,-1,8,3,4,3,99] #(identity 2)))
     
     (should=
      0
      (diagnostic-code [3,3,1107,-1,8,3,4,3,99] #(identity 8)))
     
     )

 (it "branching op codes"
     (should= 0 (diagnostic-code [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] #(identity 0)))
     (should= 1 (diagnostic-code [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9] #(identity 8)))
     (should= 0 (diagnostic-code [3,3,1105,-1,9,1101,0,0,12,4,12,99,1] #(identity 0)))
     (should= 1 (diagnostic-code [3,3,1105,-1,9,1101,0,0,12,4,12,99,1] #(identity 8)))
     )
 )

(run-specs)