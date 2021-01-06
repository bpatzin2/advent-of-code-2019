(ns advent-of-code-2019.monitoring-station.monitoring-station-spec
  (:require [speclj.core :refer :all]
            [advent-of-code-2019.monitoring-station.monitoring-station :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.input-handling :as input]))

(def simple-grid
  [[\.\.\#]
   [\.\#\.]
   [\#\.\#]])

(def complex-grid
  [[\.\.\#\#]
   [\.\#\.\.]
   [\#\.\#\.]])

(describe
  "get-visible-asteroid-coords"
  (it "works for simple input"
      (let [visible-asteroids (visible-asteroid-coords
                               (create-coord 0 0) simple-grid)]
        (should= [{:x 2, :y 0} {:x 1, :y 1} {:x 0, :y 2}]  (vec visible-asteroids))))
  (it "works for complex input"
      (let [visible-asteroids (visible-asteroid-coords
                                (create-coord 3 2) complex-grid)]
        (should= #{{:x 2, :y 2} {:x 1, :y 1} {:x 2, :y 0} {:x 3, :y 0}}  (set visible-asteroids)))))

(def grid-with-clear-best
  [[\.\.\.]
   [\#\#\#]])

(describe
  "best-location-w-count"
  (it "works for simple input"
      (should= {:coord {:x 1 :y 1} :count 2}
               (best-location-w-count grid-with-clear-best))))

(describe
  "best-location-w-count"
  (it "works for test file input"
      (should= {:coord {:x 11, :y 13}, :count 210}
               (best-location-w-count (input/day10-grid "input/day10TestLarge.txt")))))

(describe
  "count-visible-from-best-loc"
  (it "works for test file input"
      (should= {:x 11, :y 13}
               (best-location (input/day10-grid "input/day10TestLarge.txt")))))

(describe "vaporize-asteroids"
  (it "works for simple input"
      (let [grid-with-laser [[\.\.\#\#]
                             [\.\X\.\.]
                             [\#\.\#\.]]]
        (should=
          {:x 0 :y 2}
          (vaporize-asteroids grid-with-laser 4 (create-coord 1 1)))))

  ; TODO: This is the failing test case to work on
  (it "works for grid input with asteroid not aligned with edge"
    (let [laser (create-coord 0 2)
          grid [[\.\.\.\.]
                [\.\.\#\.]
                [\X\.\.\#]]]
      (should=
        {:x 2 :y 1}
        (vaporize-asteroids grid 1 laser)))))

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
    (let [laser (create-coord 1 1)
          grid [[\#\#\#]
                [\#\X\#]
                [\#\#\#]]]
      (should=
        [{:x 1 :y 0} {:x 2 :y 0} {:x 2 :y 1}
         {:x 2 :y 2} {:x 1 :y 2}{:x 0 :y 2}
         {:x 0 :y 1} {:x 0 :y 0}]
        (clockwise-vectors laser grid))))
  (it "works for grid with duplicates"
    (let [laser (create-coord 2 2)
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
        (clockwise-vectors laser grid)))))

;TODO: Example test case
;.#....###24...#..
;##...##.13#67..9#
;##...#...5.8####.
;..#.....X...###..
;..#.#.....#....##
(describe
  "vaporize-asteroids"
  (it "works for test input"
      (should= {:x 8 :y 1} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 1 (create-coord 8 3)))
      (should= {:x 9 :y 0} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 2 (create-coord 8 3)))
      (should= {:x 9 :y 1} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 3 (create-coord 8 3)))
      (should= {:x 10 :y 0} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 4 (create-coord 8 3)))
      (should= {:x 9 :y 2} (vaporize-asteroids (input/day10-grid "input/day10Testpt2.txt") 5 (create-coord 8 3)))))