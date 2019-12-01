(ns advent-of-code-2019.rocket-equation
  (:gen-class))

; Specifically, to find the fuel required for a module, 
; take its mass, divide by three, round down, and subtract 2.
(defn fuel-required [module-mass]
  (- (quot module-mass 3) 2))

(defn total-fuel-required [module-masses]
  (reduce + (map fuel-required module-masses)))
