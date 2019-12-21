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

(describe "small example"
          (it "works for test input"
              (should= '({[2 9] ["A" "A"]} {[6 9] ["B" "C"]} {[12 11] ["F" "G"]} {[16 13] ["Z" "Z"]})
                       (find-vert-portals (two-d-vec simple-map)))
              (should= '({[8 2] ["B" "C"]} {[10 6] ["D" "E"]} {[13 2] ["D" "E"]} {[15 2] ["F" "G"]})
                       (find-horz-portals (two-d-vec simple-map)))
              (should= {[2 9] ["A" "A"], [6 9] ["B" "C"], [12 11] ["F" "G"], [16 13] ["Z" "Z"], 
                        [8 2] ["B" "C"], [10 6] ["D" "E"], [13 2] ["D" "E"], [15 2] ["F" "G"]}
                       (find-portals (two-d-vec simple-map)))
              (should= 27 (count (shortest-path (two-d-vec simple-map) {} [2 9] [16 13])))
              (should= [2 9] (find-start (two-d-vec simple-map)))
              (should= [16 13] (find-end (two-d-vec simple-map)))
              (should= 23 (shortest-path-steps simple-map))
              ))

(def large-map-text 
"
                   A               
                   A               
  #################.#############  
  #.#...#...................#.#.#  
  #.#.#.###.###.###.#########.#.#  
  #.#.#.......#...#.....#.#.#...#  
  #.#########.###.#####.#.#.###.#  
  #.............#.#.....#.......#  
  ###.###########.###.#####.#.#.#  
  #.....#        A   C    #.#.#.#  
  #######        S   P    #####.#  
  #.#...#                 #......VT
  #.#.#.#                 #.#####  
  #...#.#               YN....#.#  
  #.###.#                 #####.#  
DI....#.#                 #.....#  
  #####.#                 #.###.#  
ZZ......#               QG....#..AS
  ###.###                 #######  
JO..#.#.#                 #.....#  
  #.#.#.#                 ###.#.#  
  #...#..DI             BU....#..LF
  #####.#                 #.#####  
YN......#               VT..#....QG
  #.###.#                 #.###.#  
  #.#...#                 #.....#  
  ###.###    J L     J    #.#.###  
  #.....#    O F     P    #.#...#  
  #.###.#####.#.#####.#####.###.#  
  #...#.#.#...#.....#.....#.#...#  
  #.#####.###.###.#.#.#########.#  
  #...#.#.....#...#.#.#.#.....#.#  
  #.###.#####.###.###.#.#.#######  
  #.#.........#...#.............#  
  #########.###.###.#############  
           B   J   C               
           U   P   P              ")

(def large-map (subs large-map-text 1))


(describe "large example"
          (it "works for test input"
              (should= [2 19] (find-start (two-d-vec large-map)))
              (should= [17 2] (find-end (two-d-vec large-map)))
              (should= 58 (shortest-path-steps large-map))              
              ))