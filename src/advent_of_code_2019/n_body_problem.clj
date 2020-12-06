(ns advent-of-code-2019.n-body-problem
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(def moons
  [{:name "m1" :v [1 7 0] :pos [4 7 0]},
   {:name "m2" :v [1 2 3] :pos [4 5 6]},
   {:name "m3" :v [1 2 3] :pos [4 5 6]}])

(defn my-merge [f & mlist]
  (into {}
        (map (fn [[k v]] [k (apply f (map val v))])
             (group-by key (apply concat mlist)))))

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
    (apply my-merge list all-changes)))

(defn assoc-dv-in-moon [moon-changes]
  (let[moon (first moon-changes)
       vel-changes (second moon-changes)]
    (assoc moon :dv (reduce #(map + %1 %2) vel-changes))))

(defn moon-pos-from-string [str]
  (map #(Integer/parseInt %) (re-seq #"-?\d+" str)))

(defn new-pos [curr-pos vel]
  (map + curr-pos vel))

(defn new-velocity
  ([moon]
   (new-velocity (:vel moon) (:dv moon)))
  ([curr-vel dv]
   (map + curr-vel dv)))

(defn moons-w-velocity-changes [moons]
  (let[changes-per-moon (vel-changes-per-moon moons)]
    (map assoc-dv-in-moon changes-per-moon)))

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

(defn apply-vel-update [moon]
  (let[new-vel (new-velocity moon)]
    (create-moon
     (:id moon)
     (new-pos (:pos moon) new-vel)
     new-vel)))

(defn apply-time [moons]
  (let[moons-with-dv (moons-w-velocity-changes moons)]
    (map apply-vel-update moons-with-dv)))
