(ns advent-of-code-2019.pluto-maze
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
     {[portal-row, col-num] [letter, next-letter]}
     nil)))

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
     {[row-num, portal-col] [letter, next-letter]}
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
  (apply merge
         (concat
          (find-vert-portals maze)
          (find-horz-portals maze))))

(defn valid-move? [coord already-visited maze]
  (and 
   (some? coord)
   (not (contains? already-visited coord)) 
   (= "." (maze-val coord maze))))

(defn other-side? [[key value] coord portal-name]
  (and (= portal-name value) (not= coord key)))

 (defn other-side [coord portals portal-name]
  (get (get (vec (filter #(other-side? % coord portal-name) portals)) 0) 0))

(defn through-portal [coord portals]
  (let [portal-name (get portals coord)]
    (if (some? portal-name)
      (other-side coord portals portal-name)
      nil)))

(defn next-coords [cur-coord already-visited portals maze]
  (let [row-num (first cur-coord)
        col-num (second cur-coord)
        up [(dec row-num) col-num]
        down [(inc row-num) col-num]
        left [row-num (dec col-num)]
        right [row-num (inc col-num)]
        portal (through-portal [row-num col-num] portals)]
    (filter #(valid-move? % already-visited maze) [up down left right portal])))

(defn next-paths [cur-coord path already-visited portals maze]
  (map #(conj path %) (filter #(not (contains? already-visited %)) (next-coords cur-coord already-visited portals maze))))

(defn shortest-path [maze portals start end]
  (loop [queue (conj clojure.lang.PersistentQueue/EMPTY [start])
         already-visited #{}]
    (let [path (peek queue)
          cur-coord (last path)
          n-paths (next-paths cur-coord path already-visited portals maze)
          a-visited (into already-visited (map last n-paths))]
      (if (not= cur-coord end)
        (recur (into (pop queue) n-paths) a-visited)
        path))))

(defn find-start [maze]
  (first (first (filter #(= ["A" "A"] (second %)) (find-portals maze)))))

(defn find-end [maze]
  (first (first (filter #(= ["Z" "Z"] (second %)) (find-portals maze)))))

(defn shortest-path-steps [maze-str]
  (let [maze (two-d-vec maze-str)
        portals (find-portals maze)
        start (find-start maze)
        end (find-end maze)]
   (dec (count (shortest-path maze portals start end)))))