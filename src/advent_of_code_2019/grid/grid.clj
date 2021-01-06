(ns advent-of-code-2019.grid.grid)

(defn create-coord [x y]
  {:x x :y y})

(defn get-row [coord grid]
  (vec (nth grid (:y coord))))

(defn loc [coord grid]
  (nth (get-row coord grid) (:x coord)))

(defn row-coords [row-idx, row]
  (map-indexed (fn [idx, _] {:x idx :y row-idx}) row))

(defn all-coords [grid]
  (reduce concat (map-indexed (fn [row-idx row] (row-coords row-idx row)) grid)))
