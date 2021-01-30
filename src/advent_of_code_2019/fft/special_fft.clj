(ns advent-of-code-2019.fft.special-fft
  (:require [clojure.string :as str]))

; If the message offset is > than the message length / 2
; then you know
; 1. the pattern will be shaped like 000111
; 2. the message and after are all covered by 1s
;    and are only affected by indexes to the right
; 3. you can build up the next phase by starting from
;    the rightmost index and working left summing numbers
; So 100 phases working right over ~ 3250000 (3.2M) is just
; 300M total operations

(defn special-phase [nums]
  (loop [rev (reverse nums)
         curr-total 0
         result []]
    (if (empty? rev)
      (vec (reverse result))
      (let [total (+ curr-total (first rev))
            val (mod total 10)]
        (recur (rest rev) total (conj result val))))))

(defn special-fft [nums phases msg-offset]
  (if (> (/ (count nums) 2) msg-offset)
    (throw (Exception. "does not conform to special case"))
    (let [sub-nums (subvec nums msg-offset)
          output (nth (iterate special-phase sub-nums) phases)
          msg-len (min (count output) 8)]
      (subvec output 0 msg-len))))

(defn get-msg-offset [nums]
  (let [msg-offset-vec (subvec nums 0 7)
        msg-offset-str (str/join "" msg-offset-vec)]
    (Integer/parseInt msg-offset-str)))

(defn repeat-signal [signal signal-repeat]
  (vec (apply concat (repeat signal-repeat signal))))

(defn expanded-special-fft [nums]
  (let [phases 100
        signal-repeat 10000
        msg-offset (get-msg-offset nums)
        expanded-signal (repeat-signal nums signal-repeat)]
    (special-fft expanded-signal phases msg-offset)))

(defn str-as-int-vec [str]
  (let [chars (str/split str #"")]
    (mapv #(Integer/parseInt %) chars)))

(defn expanded-special-fft-str [num-str]
  (let [input-vec (str-as-int-vec num-str)
        result-vec (expanded-special-fft input-vec)]
   (apply clojure.core/str result-vec)))
