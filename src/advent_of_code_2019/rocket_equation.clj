(ns advent-of-code-2019.rocket-equation
  (:gen-class))

; Specifically, to find the fuel required for a module, 
; take its mass, divide by three, round down, and subtract 2.
(defn fuel-required [mass]
  (-> mass
      (quot 3)
      (- 2)))

(defn fuel-for-modules [module-masses]
  (->> module-masses
       (map fuel-required)
       (reduce +)))

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
  (->> module-masses
       (map total-module-fuel)
       (reduce +)))
