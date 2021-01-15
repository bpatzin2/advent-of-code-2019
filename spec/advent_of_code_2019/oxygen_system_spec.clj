(ns advent-of-code-2019.oxygen-system-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.oxygen-system :refer :all]
            [advent-of-code-2019.core :as core]))

(defn get-response [coord open-cells oxygen-cell]
  (cond
    (contains? open-cells coord) 1
    (= oxygen-cell coord) 2
    :else 0))

(defn move-test-droid [droid-state dir open-cells oxygen-cell]
  (let [prev-path (:path droid-state)
        next-coord (next-coord (last prev-path) dir)
        response (get-response next-coord open-cells oxygen-cell)]
    {:path (conj prev-path next-coord)
     :last-response response}))

(defn create-test-droid [open-cells oxygen-cell]
  {:move       #(move-test-droid %1 %2 open-cells oxygen-cell)
   :init-state {:path [[0,0]] :last-response 1}})

(describe "droid-num-steps-to-oxygen"
  (let [open-cells #{[1, 0]}
        oxygen-cell [2, 0]
        test-droid (create-test-droid open-cells oxygen-cell)]
    (it "the oxygen is two moves to the east"
        (should= 2 (droid-num-steps-to-oxygen test-droid))))
  (let [open-cells #{[0, 0][1, 0][0 -1]}
        oxygen-cell [-1 -1]
        test-droid (create-test-droid open-cells oxygen-cell)]
    (it "the oxygen is down one left one"
        (should= 2 (droid-num-steps-to-oxygen test-droid)))))

"
 ##
#OO##
#O#DO#
#OXO#
 ###  "
(describe "droid-max-steps-from-oxygen"
  (let [open-cells #{[0, 0][1, 0][0 -1][-2 -1] [-2 0][-2 1][-1 1]}
        oxygen-cell [-1 -1]
        test-droid (create-test-droid open-cells oxygen-cell)]
    (it "finds longest path"
        (should= 4 (droid-max-steps-from-oxygen test-droid)))))

"
#OO#7O
#OO##O
#O#DOO
#OXO#
 ###  "
(describe "droid-max-steps-from-oxygen"
  (let [open-cells
        #{[-2, 2] [-1, 2] [1 2] [2 2]
          [-2, 1] [-1, 1] [2 1]
          [-2, 0] [0 0] [1 0] [2 0]
          [-2 -1] [0 -1]}
        oxygen-cell [-1 -1]
        test-droid (create-test-droid open-cells oxygen-cell)]
    (it "finds longest path"
        (should= 7 (droid-max-steps-from-oxygen test-droid)))))

"
#OOOO#
#OOOO#
#OOOO#
##ODOO
#OXO#
 ###  "
(describe "droid-max-steps-from-oxygen"
          (let [open-cells
                #{[-2, 3] [-1, 3] [0 3] [1 3]
                  [-2, 2] [-1, 2] [0 2] [1 2]
                  [-2, 1] [-1, 1] [0 1] [1 1]
                  [-1, 0] [0 0] [1 0] [2 0]
                  [-2 -1] [0 -1]}
                oxygen-cell [-1 -1]
                test-droid (create-test-droid open-cells oxygen-cell)]
            (it "finds longest path"
                (should= 6 (droid-max-steps-from-oxygen test-droid)))))

(describe "run"
  (it "the oxygen is one move to the east"
      (should= 1 (num-steps-to-oxygen [3 99
                                       101 -4 99 100
                                       104 0
                                       1005 100 0
                                       104 2 ;found it
                                       99])))

  (it "the oxygen is two moves to the east"
      (should= 2 (num-steps-to-oxygen [3 99
                                       101 -4 99 100
                                       104 0
                                       1005 100 0
                                       104 1
                                       3 99
                                       101 -4 99 100
                                       1005 100 0
                                       104 2 ;found it
                                       99]))))

(describe "day15pt1"
  (it "works for real input"
      (should= 304 (core/day15pt1))))

(describe "day15pt2"
  (it "works for real input"
      (should= 310 (core/day15pt2))))