(ns advent-of-code-2019.rocket-equation
  (:gen-class))

; Specifically, to find the fuel required for a module, 
; take its mass, divide by three, round down, and subtract 2.
(defn fuel-required [mass]
  (- (quot mass 3) 2))

(defn fuel-for-modules [module-masses]
  (reduce + (map fuel-required module-masses)))

(defn fuel-for-fuel [fuel-mass]
  (loop [mass fuel-mass acc 0]
    (let [next-mass (fuel-required mass)]
      (if (<= next-mass 0)
        acc
        (recur next-mass (+ acc next-mass))))))

(defn total-module-fuel [module-mass]
  (let [module-fuel (fuel-required module-mass)]
    (+ module-fuel (fuel-for-fuel module-fuel))))

(defn total-fuel [module-masses]
  (reduce + (map total-module-fuel module-masses)))
