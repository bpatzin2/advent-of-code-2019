(ns advent-of-code-2019.n-body-problem
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]))

(def moons
  [{:id "m1" :vel [1 7 0] :pos [4 7 0]},
   {:id "m2" :vel [1 2 3] :pos [4 5 6]},
   {:id "m3" :vel [1 2 3] :pos [4 5 6]}])

;https://stackoverflow.com/questions/15858365/how-to-use-merge-with-to-create-a-list-for-duplicate-keys-in-clojure
;Needed this to group a map by keys and collect grouped values in a list
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

(defn new-velocity [moon]
  (map + (:vel moon) (:dv moon)))

(defn apply-gravity [moons]
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
      (map apply-velocity moons-with-dv))))

(defn energy [coords]
  (reduce #(+ (Math/abs %1) (Math/abs %2)) coords))

(defn potential-energy [moon]
  (energy (:pos moon)))

(defn kenetic-energy [moon]
  (energy (:vel moon)))

(defn moon-energy [moon]
  (* (potential-energy moon) (kenetic-energy moon)))

(defn total-energy [moons steps]
  (reduce + (map moon-energy (apply-time moons steps))))

;//////////////////////////

(defn cycle-length
  ([moons]
   (loop [moons moons
          pos-vels #{}]
     (let [next-state (apply-time moons)
           all-pos (map first (map :pos moons))
           all-vels (map first (map :vel moons))
           pos-vel {:pos all-pos :vel all-vels}]
       (if
         (contains? pos-vels pos-vel)
         (inc (count pos-vels))
         (recur next-state (conj pos-vels pos-vel)))))))

