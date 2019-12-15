(ns advent-of-code-2019.orbit-mapping
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.zip :as zip]))

(defn map-function-on-map-vals [f m]
  (reduce (fn [altered-map [k v]] (assoc altered-map k (f v))) {} m))

(defn to-mmap [orbit-map-tubles]
  (map-function-on-map-vals #(map second %) (group-by first orbit-map-tubles)))

(defn zip-children [loc]
            (when-let [first-child (zip/down loc)]
              (take-while (comp not nil?)
                          (iterate zip/right first-child))))

(defn count-breadth-first-search [z]
  (loop [ret 0
         queue (conj clojure.lang.PersistentQueue/EMPTY z)]
    (if (seq queue)
      (let [[loc children] ((juxt identity zip-children) (peek queue))]
        (recur (+ ret (count (zip/path loc))) (into (pop queue) children)))
      ret)))

(defn orbit-map [orbit-map-tubles]
  (to-mmap orbit-map-tubles))

(defn multi-map-to-zip [mmap root]
  (zip/zipper #(contains? mmap %) #(get mmap %) (fn [_ c] c) root))

(defn zip-tuples [tuples root]
  (multi-map-to-zip (to-mmap tuples) root))

(defn orbit-count [orbit-map-tubles root]
  (let [orbit-zipper (zip-tuples orbit-map-tubles root)]
    (count-breadth-first-search orbit-zipper)))
