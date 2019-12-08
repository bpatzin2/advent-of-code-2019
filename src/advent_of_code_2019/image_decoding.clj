(ns advent-of-code-2019.image-decoding
  (:gen-class))

(defn decode [image-vec len wid]
  (vec (map vec (partition len (map vec (partition wid image-vec))))))

(defn flatten-dig-count [layer dig]
  (let [freqs (frequencies (flatten layer))] 
    (get freqs dig 0)))

(defn fewest [dig decoded]
  (loop [curr-layer 0
         fewest-layer 0
         fewest-layer-count (flatten-dig-count (decoded 0) dig)]
    (if
     (= curr-layer (count decoded))
      fewest-layer
      (let [next-layer (inc curr-layer)
            freqs (frequencies (flatten (decoded curr-layer)))
            dig-count (get freqs dig 0)]
        (recur 
         next-layer 
         (if (< dig-count fewest-layer-count) curr-layer fewest-layer)
         (min dig-count fewest-layer-count))))))

(defn checksum [image-vec len wid]
  (let [decoded (decode image-vec len wid)
        layer-fewest0 (fewest 0 decoded)
        freqs (frequencies (flatten (decoded layer-fewest0)))]
    (* (get freqs 1 0) (get freqs 2 0))))

(defn overlay-pix [layer-pix image-pix]
  (case image-pix
    0 image-pix ; black
    1 image-pix ; white
    2 layer-pix ; transparent
    ))

(defn overlay-row [layer-row image-row]
  (vec (map overlay-pix layer-row image-row)))

(defn overlay [layer image]
  (if 
   (nil? image) 
    layer
    (map overlay-row layer image)))

(defn decode-combined [image-vec len wid]
  (loop [rem-layers (decode image-vec len wid)
         overlayed-image nil]
    (if 
     (empty? rem-layers)
      overlayed-image
      (recur (rest rem-layers) (overlay (first rem-layers) overlayed-image)))))