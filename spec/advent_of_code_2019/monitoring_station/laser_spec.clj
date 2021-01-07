(ns advent-of-code-2019.monitoring-station.laser-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.monitoring-station.laser :refer :all]
            [advent-of-code-2019.grid.grid :as g]
            [advent-of-code-2019.input-handling :as input]))

(describe "is-clockwise-of"
  (it "works for 3x3"
    (let [fixed {:x 1 :y 1}]
      (should= true (is-clockwise-of {:x 2 :y 0} {:x 1 :y 0} fixed))
      (should= true (is-clockwise-of {:x 2 :y 1} {:x 2 :y 0} fixed))
      (should= true (is-clockwise-of {:x 2 :y 2} {:x 2 :y 1} fixed))
      (should= true (is-clockwise-of {:x 1 :y 2} {:x 2 :y 2} fixed))
      (should= true (is-clockwise-of {:x 0 :y 2} {:x 1 :y 2} fixed))
      (should= true (is-clockwise-of {:x 0 :y 1} {:x 0 :y 2} fixed))
      (should= true (is-clockwise-of {:x 0 :y 0} {:x 0 :y 1} fixed))
      (should= true (is-clockwise-of {:x 0 :y 0} {:x 1 :y 0} fixed))))
  (it "works for 5x5"
    (let [fixed {:x 2 :y 2}]
      (should= true (is-clockwise-of {:x 3 :y 0} {:x 2 :y 0} fixed))
      (should= true (is-clockwise-of {:x 1 :y 0} {:x 2 :y 0} fixed))
      (should= true (is-clockwise-of {:x 4 :y 0} {:x 3 :y 0} fixed))
      (should= true (is-clockwise-of {:x 4 :y 0} {:x 3 :y 0} fixed))
      (should= true (is-clockwise-of {:x 4 :y 1} {:x 3 :y 0} fixed))
      (should= true (is-clockwise-of {:x 4 :y 4} {:x 4 :y 3} fixed))
      (should= true (is-clockwise-of {:x 1 :y 3} {:x 1 :y 4} fixed))
      (should= true (is-clockwise-of {:x 1 :y 0} {:x 0 :y 0} fixed))))
  (it "works for 6x5"
    (let [fixed {:x 2 :y 2}]
      (should= true (is-clockwise-of {:x 4 :y 1} {:x 5 :y 0}  fixed)))))

(describe "clockwise-vectors"
  (it "works for grid with no duplicates"
      (let [laser (g/create-coord 1 1)
            grid [[\#\#\#]
                  [\#\X\#]
                  [\#\#\#]]]
        (should=
          [{:x 1 :y 0} {:x 2 :y 0} {:x 2 :y 1}
           {:x 2 :y 2} {:x 1 :y 2}{:x 0 :y 2}
           {:x 0 :y 1} {:x 0 :y 0}]
          (clockwise-vectors laser grid))))
  (it "works for grid with duplicates"
      (let [laser (g/create-coord 2 2)
            grid [[\# \# \# \# \# \#]
                  [\# \# \# \# \# \#]
                  [\# \# \X \# \# \#]
                  [\# \# \# \# \# \#]
                  [\# \# \# \# \# \#]]]
        (should=
          [{:x 2, :y 0} {:x 3, :y 0} {:x 4, :y 0}
           {:x 5, :y 0} {:x 4, :y 1} {:x 5, :y 1}
           {:x 5, :y 2} {:x 5, :y 3} {:x 4, :y 3}
           {:x 5, :y 4} {:x 4, :y 4} {:x 3, :y 4}
           {:x 2, :y 4} {:x 1, :y 4} {:x 0, :y 4}
           {:x 0, :y 3} {:x 0, :y 2} {:x 0, :y 1}
           {:x 0, :y 0} {:x 1, :y 0}]
          (clockwise-vectors laser grid)))

  ;.#....###24...#..
  ;##...##.13#67..9#
  ;##...#...5.8####.
  ;..#.....X...###..
  ;..#.#.....#....##
  (it "works for test input"
      (should= {:x 8 :y 1} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 1 (g/create-coord 8 3)))
      (should= {:x 9 :y 0} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 2 (g/create-coord 8 3)))
      (should= {:x 9 :y 1} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 3 (g/create-coord 8 3)))
      (should= {:x 10 :y 0} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 4 (g/create-coord 8 3)))
      (should= {:x 9 :y 2} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 5 (g/create-coord 8 3))))))