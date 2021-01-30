(ns advent-of-code-2019.core
  (:gen-class)
  (:require [advent-of-code-2019.rocket-equation :as re]
            [advent-of-code-2019.intcode.intcode :as intcode]
            [advent-of-code-2019.intcode.intcode-harness :as intcode-harness]
            [advent-of-code-2019.amplification :as amp]
            [advent-of-code-2019.orbit-mapping :as om]
            [advent-of-code-2019.crossed-wires :as cw]
            [advent-of-code-2019.image-decoding :as decoding]  
            [advent-of-code-2019.password-guessing :as pw]  
            [advent-of-code-2019.arcade :as arcade]  
            [advent-of-code-2019.oxygen-system :as os]      
            [advent-of-code-2019.fft.fft :as fft]
            [advent-of-code-2019.fft.special-fft :as special-fft]
            [advent-of-code-2019.space-stoichiometry.stoichiometry :as space-stoichiometry]
            [advent-of-code-2019.pluto-maze :as pluto-maze]   
            [advent-of-code-2019.pluto-maze-recursive :as pluto-maze-recursive]
            [advent-of-code-2019.shuffle :as deck-shuffle]
            [advent-of-code-2019.bugs :as bugs]
            [advent-of-code-2019.bugs-recursive :as bugs-recursive]
            [advent-of-code-2019.n-body-problem.time-steps :as time-steps]
            [advent-of-code-2019.n-body-problem.energy :as energy]
            [advent-of-code-2019.n-body-problem.cycle-length :as cycle-length]
            [advent-of-code-2019.monitoring-station.monitoring-station :as monitoring-station]
            [advent-of-code-2019.monitoring-station.laser :as laser]
            [advent-of-code-2019.input-handling :as input]))

(defn day1pt1 []
  (re/fuel-for-modules (input/day1-num-seq)))

(defn day1pt2 []
  (re/total-fuel (input/day1-num-seq)))

(defn day2pt1 []
  ((intcode/execute (input/day2-num-vec)) 0))

(defn day2pt2 []
  (let [inputs (intcode-harness/find-inputs 19690720 (input/day2-unmodified-num-vec))
        noun (first inputs)
        verb (second inputs)]
    (+ (* 100 noun) verb)))

(defn day3pt1 []
  (let [string-pair (input/day3-string-pair)] 
    (cw/closet-overlap-dist (first string-pair) (second string-pair))))

(defn day3pt2 []
  (let [string-pair (input/day3-string-pair)]
    (cw/shortest-overlap (first string-pair) (second string-pair))))

(defn day4pt1 []
  (count (pw/orig-options 137683 596253)))

(defn day4pt2 []
  (count (pw/updated-options 137683 596253)))

(defn day5pt1 []
  (intcode/diagnostic-code (input/day5-num-vec) [1]))

(defn day5pt2 []
  (intcode/diagnostic-code (input/day5-num-vec) [5]))

(defn day6pt1 []
  (om/orbit-count (input/day6-tuples) "COM"))

(defn day6pt2 []
  (om/req-transfer-count (input/day6-tuples) "COM" "YOU" "SAN"))

(defn day7pt1 []
  (amp/find-largest-output (input/day7-num-vec) [0 1 2 3 4]))

(defn day7pt2 []
  (amp/find-largest-loop-output (input/day7-num-vec) [5 6 7 8 9]))

(defn day8pt1 []
  (decoding/checksum (input/day8-num-vec) 6 25))

(defn day8pt2 []
  (decoding/decode-combined (input/day8-num-vec) 6 25))

(defn day9pt1 []
  (intcode/diagnostic-code (input/day9-num-vec) [1]))

(defn day9pt2 []
  (intcode/diagnostic-code (input/day9-num-vec) [2]))

(defn day10pt1 []
  (monitoring-station/count-visible-from-best-loc (input/day10-grid)))

(defn day10pt2 []
  (let [last-vaped (laser/vaporize-asteroids (input/day10-grid) 200)]
    (+ (* 100 (:x last-vaped))
       (:y last-vaped))))

(defn day12pt1 []
  (let [moons (time-steps/create-moons (input/day12-list))]
    (energy/total-energy moons 1000)))

(defn day12pt2 []
  (let [moons (time-steps/create-moons (input/day12-list))]
    (cycle-length/cycle-length moons)))

(defn day13pt1 []
  (arcade/run-and-count-blocks (input/day13-num-vec)))

(defn day13pt2 
  ([] day13pt2 false)
  ([debug-mode]
   (arcade/play-game
    (assoc (input/day13-num-vec) 0 2)
    debug-mode)))

(defn day14pt1
  ([] (day14pt1 "input/day14.txt"))
  ([input-file]
   (space-stoichiometry/parse-and-min-ore-to-reach-fuel
     (slurp input-file))))

(defn day15pt1 []
  (os/num-steps-to-oxygen (input/day15-num-vec)))

(defn day15pt2 []
  (os/max-steps-from-oxygen (input/day15-num-vec)))

(defn day16pt1
  ([] (day16pt1 100))
  ([phases] (fft/run-fft-str (input/day16-str) phases 8)))

(defn day16pt2 []
  (special-fft/expanded-special-fft-str (input/day16-str)))

(defn day20pt1 [] 
  (pluto-maze/shortest-path-steps (input/day20-str)))

(defn day20pt2 []
  (pluto-maze-recursive/shortest-path-steps (input/day20-str)))

(defn day22pt1 []
  (.indexOf (deck-shuffle/shuffle-deck 10007 (input/day22-list)) 2019))

(defn day24pt1 []
  (bugs/bio-divesity-rating
   (bugs/first-dup-state (input/day24-str))))

(defn day24pt2 []
  (bugs-recursive/num-bugs-after (input/day24-str) 200))