(ns advent-of-code-2019.orbit-mapping
  (:gen-class)
  (:require [clojure.zip :as zip]
            [clojure.set :as set-lib]))

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

(defn path-or-nil [z n]
  (if
   (= n (zip/node z))
    (zip/path z)
    nil))

(defn zfind [z n]
  (loop [ret nil
         queue (conj clojure.lang.PersistentQueue/EMPTY z)]
    (if (and (seq queue) (nil? ret))
      (let [[loc children] ((juxt identity zip-children) (peek queue))]
        (recur (path-or-nil loc n) (into (pop queue) children)))
      ret)))

(defn zip-neighbors [loc]
  (let [zip-kids (zip-children loc)
        par (zip/up loc)]
    (conj zip-kids par)))

(defn count-explore-steps [z]
  (loop [ret 0
         queue (conj clojure.lang.PersistentQueue/EMPTY z)]
    (if (seq queue)
      (let [loc (peek queue)
            neighbors (zip-neighbors loc)]
        (recur (+ ret (count (zip/path loc))) (into (pop queue) neighbors)))
      ret)))

(defn count-uncommon-ancestors [z, a, b]
  (let [a-path (set (zfind z a))
        b-path (set (zfind z b))
        a-diff (set-lib/difference a-path b-path)
        b-diff (set-lib/difference b-path a-path)]
    (+ (count a-diff) (count b-diff))))

(defn orbit-map [orbit-map-tubles]
  (to-mmap orbit-map-tubles))

(defn multi-map-to-zip [mmap root]
  (zip/zipper #(contains? mmap %) #(get mmap %) (fn [_ c] c) root))

(defn zip-tuples [tuples root]
  (multi-map-to-zip (to-mmap tuples) root))

(defn orbit-count [orbit-map-tubles root]
  (let [orbit-zipper (zip-tuples orbit-map-tubles root)]
    (count-breadth-first-search orbit-zipper)))

(defn req-transfer-count [orbit-map-tubles root src dst]
  (let [orbit-zipper (zip-tuples orbit-map-tubles root)]
    (count-uncommon-ancestors orbit-zipper src dst)))
