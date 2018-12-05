(ns advent-2017.day05)

(def input (slurp (clojure.java.io/resource "day05.txt")))

(defn pair [ch] (let [c (int ch) ] (char (if (> c 96) (- c 32) (+ c 32)))))

(defn react [in]
  (loop [ rem (rest in) st (list (first in)) ]
    (if (empty? rem) (apply str (reverse st))
      (recur (rest rem) (if (= (peek st) (pair (first rem))) (pop st) (conj st (first rem)))))))

(def reacted (react input))

(defn strip [coll chars]
  (apply str (remove #((set chars) %) coll)))

(println "Part 1:" (count reacted))
(println "Part 2:" (->> (range (int \a) (inc (int \z))) 
                        (map #(strip reacted [ (char %) (Character/toUpperCase (char %)) ])) 
                        (map react) 
                        (map count) 
                        (apply min)))