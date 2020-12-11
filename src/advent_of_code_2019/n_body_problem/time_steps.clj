(ns advent-of-code-2019.n-body-problem.time-steps
  (:gen-class)
  (:require [clojure.math.numeric-tower :as math]))

(defn velocity-change-int [this-pos other-pos]
  (cond
    (> this-pos other-pos) -1
    (< this-pos other-pos) 1
    :else 0))

(defn moon-velocity-change [this other]
  (map #(velocity-change-int %1 %2) (:pos this) (:pos other)))

(defn total-velocity-change [moon all-moons]
  (reduce #(map + %1 %2) (map #(moon-velocity-change moon %) (remove #(= % moon) all-moons))))

(defn moon-pos-from-string [str]
  (map #(Integer/parseInt %) (re-seq #"-?\d+" str)))

(defn new-pos [curr-pos vel]
  (map + curr-pos vel))

(defn new-velocity [moon]
  (map + (:vel moon) (:dv moon)))

(defn apply-gravity [moons]
  (map #(assoc % :dv (total-velocity-change % moons)) moons))

;/////////////////////////////

(defn create-moon
  ([moon-id moon-str]
    {:id moon-id
     :pos (moon-pos-from-string moon-str)
     :vel '(0 0 0)})
  ([moon-id pos vel]
   {:id moon-id
    :pos pos
    :vel vel}))

(defn create-moons [str-list]
  (map-indexed create-moon str-list))

(defn apply-velocity [moon]
  (let[new-vel (new-velocity moon)]
    (create-moon
     (:id moon)
     (new-pos (:pos moon) new-vel)
     new-vel)))

(defn apply-time
  ([moons steps]
   (nth (iterate apply-time moons) steps))
  ([moons]
    (let[moons-with-dv (apply-gravity moons)]
      (doall (map apply-velocity moons-with-dv)))))

