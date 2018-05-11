(defn i-gen
  [i limit body]
  {:pre [(integer? i)
         (integer? limit)
         (ifn? body)]}
  (if (< i limit)
    (concat [(body i)] (i-gen (+ i 1) limit body))
    []))

(defn allnumbers?
  [i vector]
  (if (< i (count vector))
    (and (number? (nth vector i))
         (allnumbers? (+ i 1) vector))
    true))

(defn nvector?
  ([vector]
   (and (vector? vector)
        (allnumbers? 0 vector)))
  ([vec & vectors]
   (and (nvector? vec)
        (apply nvector? vectors))))

(defn nmatrix?
  ([matrix]
   (and (vector? matrix)
        (or (zero? (count matrix)) (nvector? (nth matrix 0)))))
  ([mat & matrixes]
   (and (nmatrix? mat) (apply nmatrix? matrixes))))

(defn v-eq-size?
  ([]
   true)
  ([_]
   true)
  ([vec1 vec2 & vectors]
   (and (= (count vec1) (count vec2))
        (apply v-eq-size? vec2 vectors))))

(defn m-eq-size?
  ([]
   true)
  ([_]
   true)
  ([mat1 mat2]
   (and (v-eq-size? mat1 mat2)
        (or (zero? (count mat1)) (v-eq-size? (nth mat1 0) (nth mat2 0)))))
  ([mat1 mat2 & matrixes]
   (and (m-eq-size? mat1 mat2)
        (apply m-eq-size? mat2 matrixes))))

(defn v-const-size?
  ([_]
   true)
  ([size vec1 & vectors]
   (and (= (count vec1) size) (apply v-const-size? size vectors))))

(defn m-connecting?
  [mat1 mat2]
  {:pre [(not (zero? (count mat1)))
         (not (zero? (count (nth mat1 0))))
         (not (zero? (count mat2)))
         (not (zero? (count (nth mat2 0))))]}
  (= (count (nth mat1 0)) (count mat2)))

(defn v-op
  [op vector-array]
  {:pre [(apply nvector? vector-array)
         (apply v-eq-size? vector-array)]}
  (vec (apply map op vector-array)))
(defn v-s-op
  ([op vector scalar]
   {:pre [(or (nvector? vector) (nmatrix? vector))
          (number? scalar)]}
   (mapv (fn [x] (op x scalar)) vector))
  ([op vector scalar & scalars]
   (apply v-s-op op (v-s-op op vector scalar) scalars)))

(defn v+
  [& vectors]
  (v-op + vectors))
(defn v-
  [& vectors]
  (v-op - vectors))
(defn v*
  [& vectors]
  (v-op * vectors))

(defn scalar
  [& vectors]
  (apply + (apply v* vectors)))
(defn vect
  ([vector]
   {:pre [(nvector? vector)]}
   vector)
  ([vec1 vec2]
   {:pre [(nvector? vec1 vec2)
          (v-const-size? 3 vec1 vec2)]}
   (vec (i-gen 0 3 (fn [i]
                     (def l (mod (+ i 1) 3))
                     (def r (mod (+ i 2) 3))
                     (-
                       (*
                         (nth vec1 l)
                         (nth vec2 r))
                       (*
                         (nth vec1 r)
                         (nth vec2 l))
                       )))))
  ([vec1 vec2 & vectors]
   (apply vect (vect vec1 vec2) vectors)))

(defn v*s
  ([vector]
   {:pre [(nvector? vector)]}
   vector)
  ([vec & scalars]
   (apply v-s-op * vec scalars)))

(defn m-op
  [op matrix-array]
  {:pre [(apply nmatrix? matrix-array)
         (apply m-eq-size? matrix-array)]}
  (apply mapv (fn [& rows] (v-op op rows)) matrix-array))

(defn m+
  [& matrixes]
  (m-op + matrixes))
(defn m-
  [& matrixes]
  (m-op - matrixes))
(defn m*
  [& matrixes]
  (m-op * matrixes))

(defn m*s
  ([matrix]
   {:pre [(nmatrix? matrix)]}
   matrix)
  ([matrix & scalars]
   (apply v-s-op v*s matrix scalars)))
(defn m*v
  [matrix vector]
  {:pre [(nmatrix? matrix)]}
  (mapv (fn [row] (scalar row vector)) matrix))
(defn transpose
  [matrix]
  {:pre [(nmatrix? matrix)]}
  (vec (apply mapv vector matrix)))
(defn m*m
  ([matrix]
   {:pre [(nmatrix? matrix)]}
   matrix)
  ([mat1 mat2]
   {:pre [(nmatrix? mat1)
          (nmatrix? mat2)
          (m-connecting? mat1 mat2)]}
   (def columns (transpose mat2))
   (vec (i-gen 0 (count mat1) (fn [i]
                                (def row (nth mat1 i))
                                (vec (i-gen 0 (count columns) (fn [j]
                                                                (def col (nth columns j))
                                                                (scalar row col))))))))
  ([mat1 mat2 & matrixes]
   (apply m*m (m*m mat1 mat2) matrixes)))