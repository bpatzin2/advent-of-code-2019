(ns advent-of-code-2019.n-body-problem
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(def moons
  [{:name "m1" :v [1 7 0] :pos [4 7 0]},
   {:name "m2" :v [1 2 3] :pos [4 5 6]},
   {:name "m3" :v [1 2 3] :pos [4 5 6]}])

(defn pairs [items]
  (combo/combinations items 2))

(defn velocity-change-ps [p1 p2]
  (cond
    (> p1 p2) '(-1 1)
    (< p1 p2) '(1 -1)
    :else '(0 0)))

(defn velocity-change
  ([moon-pair]
   (velocity-change (first moon-pair) (second moon-pair)))
  ([m1 m2]
    (let [p1 (:pos m1)
          p2 (:pos m2)
          changes (map velocity-change-ps p1 p2)]
      {m1 (map first changes)
       m2 (map second changes)})))

(defn vel-changes-per-moon [moons]
  (let[all-changes (map velocity-change (pairs moons))]
    (apply merge-with list all-changes)))

(defn assoc-dv-in-moon [moon-changes]
  (let[moon (first moon-changes)
       vel-changes (second moon-changes)]
    (assoc moon :dv (reduce #(map + %1 %2) vel-changes))))

(defn velocity-changes [moons]
  (let[changes-per-moon (vel-changes-per-moon moons)]
    (map assoc-dv-in-moon changes-per-moon)))

