(ns advent-of-code-2019.shuffle
  (:gen-class)
  (:require [clojure.string :as str]))

(defn new-stack [deck]
  (vec (reverse deck)))

(defn cut-size [cmd]
  (Integer/parseInt (second (str/split cmd #" "))))

(defn inc-size [cmd]
  (Integer/parseInt (nth (str/split cmd #" ") 3)))

(defn cut-location [deck cut-size]
  (if (> cut-size 0) 
    cut-size 
    (+ (count deck) cut-size)))

(defn cut [deck cut-size]
  (let [cut-loc (cut-location deck cut-size)]    
    (vec (concat
          (subvec deck cut-loc)
          (subvec deck 0 cut-loc)))))

(defn insert-into-deck [deck card i inc-size]
  (assoc deck (rem (* i inc-size) (count deck)) card))

(defn deal-with-increment [deck inc-size]
  (loop [new-deck deck
         rem-deck deck
         i 0]
    (if (not-empty rem-deck)
      (recur 
       (insert-into-deck new-deck (first rem-deck) i inc-size)
       (rest rem-deck)
       (inc i))
      new-deck)))

(defn shuffle-step [deck cmd]
  (cond
    (str/includes? cmd "deal into new stack") (new-stack deck)
    (str/includes? cmd "cut") (cut deck (cut-size cmd))
    (str/includes? cmd "deal with increment") (deal-with-increment deck (inc-size cmd))
    :else deck))

(defn make-deck [deck-size]
  (vec (range deck-size)))

(defn shuffle-deck [deck-size cmds]
  (reduce shuffle-step (make-deck deck-size) cmds))
