(ns advent-of-code-2019.pluto-maze-recursive-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.pluto-maze-recursive :refer :all]))
(def simple-map-text 
"
             Z L X W       C                 
             Z P Q B       K                 
  ###########.#.#.#.#######.###############  
  #...#.......#.#.......#.#.......#.#.#...#  
  ###.#.#.#.#.#.#.#.###.#.#.#######.#.#.###  
  #.#...#.#.#...#.#.#...#...#...#.#.......#  
  #.###.#######.###.###.#.###.###.#.#######  
  #...#.......#.#...#...#.............#...#  
  #.#########.#######.#.#######.#######.###  
  #...#.#    F       R I       Z    #.#.#.#  
  #.###.#    D       E C       H    #.#.#.#  
  #.#...#                           #...#.#  
  #.###.#                           #.###.#  
  #.#....OA                       WB..#.#..ZH
  #.###.#                           #.#.#.#  
CJ......#                           #.....#  
  #######                           #######  
  #.#....CK                         #......IC
  #.###.#                           #.###.#  
  #.....#                           #...#.#  
  ###.###                           #.#.#.#  
XF....#.#                         RF..#.#.#  
  #####.#                           #######  
  #......CJ                       NM..#...#  
  ###.#.#                           #.###.#  
RE....#.#                           #......RF
  ###.###        X   X       L      #.#.#.#  
  #.....#        F   Q       P      #.#.#.#  
  ###.###########.###.#######.#########.###  
  #.....#...#.....#.......#...#.....#.#...#  
  #####.#.###.#######.#######.###.###.#.#.#  
  #.......#.......#.#.#.#.#...#...#...#.#.#  
  #####.###.#####.#.#.#.#.###.###.#.###.###  
  #.......#.....#.#...#...............#...#  
  #############.#.#.###.###################  
               A O F   N                     
               A A D   M                     ")

(def simple-map (subs simple-map-text 1))

(describe "small example"
          (it "works for test input"
              (should= '({:coord [2 15], :name ["L" "P"], :is-outer true} 
                         {:coord [2 17], :name ["X" "Q"], :is-outer true} 
                         {:coord [2 19], :name ["W" "B"], :is-outer true} 
                         {:coord [2 27], :name ["C" "K"], :is-outer true} 
                         {:coord [8 13], :name ["F" "D"], :is-outer false} 
                         {:coord [8 21], :name ["R" "E"], :is-outer false} 
                         {:coord [8 23], :name ["I" "C"], :is-outer false} 
                         {:coord [8 31], :name ["Z" "H"], :is-outer false}
                         {:coord [28 17], :name ["X" "F"], :is-outer false}
                         {:coord [28 21], :name ["X" "Q"], :is-outer false} 
                         {:coord [28 29], :name ["L" "P"], :is-outer false} 
                         {:coord [34 17], :name ["O" "A"], :is-outer true} 
                         {:coord [34 19], :name ["F" "D"], :is-outer true} 
                         {:coord [34 23], :name ["N" "M"], :is-outer true} 
                         {:coord [13 8], :name ["O" "A"], :is-outer false} 
                         {:coord [13 36], :name ["W" "B"], :is-outer false} 
                         {:coord [13 42], :name ["Z" "H"], :is-outer true} 
                         {:coord [15 2], :name ["C" "J"], :is-outer true} 
                         {:coord [17 8], :name ["C" "K"], :is-outer false} 
                         {:coord [17 42], :name ["I" "C"], :is-outer true} 
                         {:coord [21 2], :name ["X" "F"], :is-outer true} 
                         {:coord [21 36], :name ["R" "F"], :is-outer false} 
                         {:coord [23 8], :name ["C" "J"], :is-outer false} 
                         {:coord [23 36], :name ["N" "M"], :is-outer false} 
                         {:coord [25 2], :name ["R" "E"], :is-outer true} 
                         {:coord [25 42], :name ["R" "F"], :is-outer true})
                       (find-portals-wo-start-end (two-d-vec simple-map)))
              
              (should= [34 15] (find-start (two-d-vec simple-map)))
              (should= [2 13] (find-end (two-d-vec simple-map)))
              (should= 396 (shortest-path-steps simple-map))
              ))
