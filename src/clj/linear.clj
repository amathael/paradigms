(defn i-gen
  [i limit body]
  (if (< i limit)
    (concat [(body i)] (i-gen (+ i 1) limit body))
    []))

(defn tensor-shape
  [tensor]
  (cond
    (number? tensor) []
    (= [] tensor) [0]
    :else (do (def inner (mapv tensor-shape tensor))
              (if (and (apply = inner) (not= (nth inner 0) nil))
                (vec (concat [(count tensor)] (nth inner 0)))
                nil))))

(defn fast-t-shape
  [tensor]
  (cond
    (number? tensor) []
    (= [] tensor) [0]
    :else (vec (concat [(count tensor)] (fast-t-shape (nth tensor 0))))))

(defn eq-shape?
  [& tensors]
  (apply = (map fast-t-shape tensors)))

(defn v-const-size?
  ([_]
   true)
  ([size & vectors]
   (apply = [size] (map fast-t-shape vectors))))

(defn m-connecting?
  [mat1 mat2]
  (= (count (nth mat1 0)) (count mat2)))

(defn ntensor?
  [tensor]
  (not= (tensor-shape tensor) nil))

(defn nvector?
  [vector]
  {:pre [(ntensor? vector)]}
  (= (count (fast-t-shape vector)) 1))

(defn nmatrix?
  [matrix]
  {:pre [(ntensor? matrix)]}
  (= (count (fast-t-shape matrix)) 2))

(defn drop-first
  [shape]
  (drop 1 shape))

(defn reshape?
  [shape1 shape2]
  (if (< (count shape1) (count shape2))
    false
    (or (= shape1 shape2) (reshape? (drop-first shape1) shape2))))

(defn broadcast
  [shape tensor]
  {:pre [(ntensor? tensor)
         (nvector? shape)
         (reshape? shape (fast-t-shape tensor))]}
  (if (= shape (fast-t-shape tensor))
    tensor
    (vec (repeat (first shape)
                 (broadcast (drop-first shape) tensor)))))

(defn greatest
  ([]
   [])
  ([tensor & tensors]
   (def n-shape (apply greatest tensors))
   (def f-shape (fast-t-shape tensor))
   (if (< (count f-shape) (count n-shape))
     n-shape
     f-shape)))

(defn same-shape-op
  [fun tensor-array]
  (if (every? number? tensor-array)
    (apply fun tensor-array)
    (apply mapv (fn [& sub-tensors] (same-shape-op fun sub-tensors)) tensor-array)))

(defn tensor-op
  [fun tensor-array]
  (def g (apply greatest tensor-array))
  {:pre [(every? ntensor? tensor-array)
         (every? (fn [tensor] (reshape? g (fast-t-shape tensor))) tensor-array)]}
  (same-shape-op fun (mapv (fn [tensor] (broadcast g tensor)) tensor-array)))

(defn vector-op
  [fun vector-array]
  {:pre [(every? nvector? vector-array)
         (apply eq-shape? vector-array)]}
  (same-shape-op fun vector-array))

(defn matrix-op
  [fun matrix-array]
  {:pre [(every? nmatrix? matrix-array)
         (apply eq-shape? matrix-array)]}
  (same-shape-op fun matrix-array))

(defn tensor-scalar-op
  ([fun tensor scalar]
   {:pre [(ntensor? tensor)
          (number? scalar)]}
   (mapv (fn [element] (fun element scalar)) tensor))
  ([fun tensor scalar & scalars]
   (apply tensor-scalar-op fun (tensor-scalar-op fun tensor scalar) scalars)))

(defn v+
  [& vectors]
  (vector-op + vectors))
(defn v-
  [& vectors]
  (vector-op - vectors))
(defn v*
  [& vectors]
  (vector-op * vectors))

(defn scalar
  [& vectors]
  (apply + (apply v* vectors)))
(defn vect
  ([vector]
   {:pre [(nvector? vector)]}
   vector)
  ([vec1 vec2]
   {:pre [(nvector? vec1)
          (nvector? vec2)
          (v-const-size? 3 vec1 vec2)]}
   (vec (i-gen 0 3 (fn [i]
                     (def l (mod (+ i 1) 3))
                     (def r (mod (+ i 2) 3))
                     (- (* (nth vec1 l)
                           (nth vec2 r))
                        (* (nth vec1 r)
                           (nth vec2 l)))))))
  ([vec1 vec2 & vectors]
   (apply vect (vect vec1 vec2) vectors)))

(defn v*s
  ([vector]
   {:pre [(nvector? vector)]}
   vector)
  ([vec & scalars]
   (apply tensor-scalar-op * vec scalars)))

(defn m+
  [& matrixes]
  (matrix-op + matrixes))
(defn m-
  [& matrixes]
  (matrix-op - matrixes))
(defn m*
  [& matrixes]
  (matrix-op * matrixes))

(defn m*s
  ([matrix]
   {:pre [(nmatrix? matrix)]}
   matrix)
  ([matrix & scalars]
   (apply tensor-scalar-op v*s matrix scalars)))
(defn m*v
  [matrix vector]
  {:pre [(nmatrix? matrix)]}
  (mapv (fn [row] (scalar row vector)) matrix))
(defn transpose
  [matrix]
  {:pre [(nmatrix? matrix)]}
  (apply mapv vector matrix))
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

(defn b+
  [& tensors]
  (tensor-op + tensors))
(defn b-
  [& tensors]
  (tensor-op - tensors))
(defn b*
  [& tensors]
  (tensor-op * tensors))