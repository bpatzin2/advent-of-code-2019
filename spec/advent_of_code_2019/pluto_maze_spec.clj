(ns advent-of-code-2019.pluto-maze-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.pluto-maze :refer :all]))
(def simple-map-text 
"
         A           
         A           
  #######.#########  
  #######.........#  
  #######.#######.#  
  #######.#######.#  
  #######.#######.#  
  #####  B    ###.#  
BC...##  C    ###.#  
  ##.##       ###.#  
  ##...DE  F  ###.#  
  #####    G  ###.#  
  #########.#####.#  
DE..#######...###.#  
  #.#########.###.#  
FG..#########.....#  
  ###########.#####  
             Z       
             Z       ")

(def simple-map (subs simple-map-text 1))
(def maze (two-d-vec simple-map))

(describe "run"
          (it "works for test input"
              (should= '({[2 9] ["A" "A"]} {[6 9] ["B" "C"]} {[12 11] ["F" "G"]} {[16 13] ["Z" "Z"]})
                       (find-vert-portals (two-d-vec simple-map)))
              (should= '({[8 2] ["B" "C"]} {[10 6] ["D" "E"]} {[13 2] ["D" "E"]} {[15 2] ["F" "G"]})
                       (find-horz-portals (two-d-vec simple-map)))
              (should= {[2 9] ["A" "A"], [6 9] ["B" "C"], [12 11] ["F" "G"], [16 13] ["Z" "Z"], 
                        [8 2] ["B" "C"], [10 6] ["D" "E"], [13 2] ["D" "E"], [15 2] ["F" "G"]}
                       (find-portals (two-d-vec simple-map)))
              (should= 26 (shortest-path-steps (two-d-vec simple-map) {} [2 9] [16 13]))
              ))