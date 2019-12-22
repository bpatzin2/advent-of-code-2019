(ns advent-of-code-2019.pluto-maze-recursive
  (:gen-class)
  (:require [clojure.string :as str]))

(defn maze-val 
  ([cur-coord maze]
   (maze-val (first cur-coord) (second cur-coord) maze))
  ([row-num col-num maze]
   (get (get maze row-num) col-num)))

(defn create-vert-portal [row-num col-num maze]
  (let [letter (get (get maze row-num) col-num)
        next-letter (get (get maze (inc row-num)) col-num)
        two-down (inc (inc row-num))
        portal-row (if (and (get maze two-down) (not= (get (get maze two-down) col-num) " ")) 
                     two-down 
                     (dec row-num))
        both-letters? (and 
                       (some? letter)
                       (some? next-letter)
                       (some? (re-matches #"\w" letter))
                       (some? (re-matches #"\w" next-letter)))]
   (if both-letters?
     {:coord [portal-row, col-num] 
      :name [letter, next-letter]
      :is-outer (or (= 0 row-num) (= (- (count maze) 2) row-num))}
     nil)))

(defn row-len [maze]
  (count (first maze)))

(defn create-horz-portal [row-num col-num maze]
  (let [letter (get (get maze row-num) col-num)
        next-letter (get (get maze row-num) (inc col-num))
        two-right (inc (inc col-num))
        two-right-val (get (get maze row-num) two-right)
        portal-col (if (and two-right-val (not= two-right-val " ")) 
                     two-right 
                     (dec col-num))
        both-letters? (and 
                       (some? letter)
                       (some? next-letter)
                       (some? (re-matches #"\w" letter))
                       (some? (re-matches #"\w" next-letter)))]
   (if both-letters?
     {:coord [row-num, portal-col]
      :name [letter, next-letter]
      :is-outer (or (= 0 col-num) (= (- (row-len maze) 2) col-num))}
     nil)))

(defn create-vert-portal-row [row-num, row, maze]
  (filter some? (map #(create-vert-portal row-num %1 maze) (range (count row)))))

(defn create-horz-portal-row [row-num, row, maze]
  (filter some? (map #(create-horz-portal row-num %1 maze) (range (count row)))))

(defn find-vert-portals [maze]
  (filter some? (mapcat #(create-vert-portal-row % (maze %) maze) (range (count maze)))))
 
(defn find-horz-portals [maze]
  (filter some? (mapcat #(create-horz-portal-row % (maze %) maze) (range (count maze)))))

(defn two-d-vec [s]
  (vec (map #(str/split % #"") (vec (str/split s #"\n")))))

(defn find-portals [maze]
  (concat
   (find-vert-portals maze)
   (find-horz-portals maze)))

(defn find-portals-wo-start-end [maze]
 (remove #(or (= (:name %) ["A" "A"]) (= (:name %) ["Z" "Z"])) (find-portals maze)))

(defn valid-move? [coord already-visited maze]
  (and 
   (some? coord)
   (<= 0 (get coord 2))
   (not (contains? already-visited coord)) 
   (= "." (maze-val coord maze))))

(defn same-spot? [coord portal]
  (= (subvec coord 0 2) (:coord portal)))

(defn other-portal [coord portals portal-name]
  (first (filter #(and (= portal-name (:name %)) (not (same-spot? coord %))) portals)))

(defn other-side [coord portal portals]
  (let [o-portal (other-portal coord portals (:name portal))
        o-coord (:coord o-portal)
        curr-layer (get coord 2)
        next-layer (if (:is-outer portal)
                     (dec curr-layer)
                     (inc curr-layer))]
    (conj o-coord next-layer)))

(defn through-portal [coord portals]
  (let [portal (first (filter #(same-spot? coord %) portals))]
    (if (some? portal)
      (other-side coord portal portals)
      nil)))

(defn next-coords [cur-coord already-visited portals maze]
  (let [row-num (first cur-coord)
        col-num (second cur-coord)
        layer (nth cur-coord 2)
        up [(dec row-num) col-num layer]
        down [(inc row-num) col-num layer]
        left [row-num (dec col-num) layer]
        right [row-num (inc col-num) layer]
        portal (through-portal [row-num col-num layer] portals)]
    (filter #(valid-move? % already-visited maze) [up down left right portal])))

(defn next-paths [cur-coord path already-visited portals maze]
  (map #(conj path %) (next-coords cur-coord already-visited portals maze)))

(defn shortest-path [maze portals start end]
  (loop [queue (conj clojure.lang.PersistentQueue/EMPTY [(conj start 0)])
         already-visited #{}]
    (let [path (peek queue)
          cur-coord (last path)
          n-paths (next-paths cur-coord path already-visited portals maze)
          a-visited (into already-visited (map last n-paths))]
      (if (not= cur-coord (conj end 0))
        (recur (into (pop queue) n-paths) a-visited)
        path))))

(defn find-start [maze]
  (:coord (first (filter #(= ["A" "A"] (:name %)) (find-portals maze)))))

(defn find-end [maze]
  (:coord (first (filter #(= ["Z" "Z"] (:name %)) (find-portals maze)))))

(defn shortest-path-steps [maze-str]
  (let [maze (two-d-vec maze-str)
        portals (find-portals-wo-start-end maze)
        start (find-start maze)
        end (find-end maze)]
   (dec (count (shortest-path maze portals start end)))))