(defn tensor-shape
  [tensor]
  (if (number? tensor)
    []
    (let [inner (map tensor-shape tensor)]
      (if (and (apply = inner) (not (nil? (first inner))))
        (vec (cons (count tensor) (first inner)))))))

(defn fast-t-shape
  [tensor]
  (vec ((fn [t] (if (number? t)
                  []
                  (cons (count t) (fast-t-shape (first t))))) tensor)))

(defn eq-shape?
  [& tensors]
  (apply = (map fast-t-shape tensors)))

(defn v-const-size?
  ([_]
   true)
  ([size & vectors]
   (apply = [size] (map fast-t-shape vectors))))

(defn m-connecting?
  [matrix1 matrix2]
  {:pre [(not= 0 (count matrix1))]}
  (= (count (first matrix1)) (count matrix2)))

(defn ntensor?
  [tensor]
  (not= (tensor-shape tensor) nil))

(defn nvector?
  [vector]
  (and (vector? vector) (every? number? vector)))

(defn nmatrix?
  [matrix]
  (and (vector? matrix) (every? nvector? matrix) (not (empty? matrix))))

(defn can-reshape?
  [shape1 shape2]
  (or (and (= (count shape1) (count shape2)) (= shape1 shape2))
      (and (> (count shape1) (count shape2)) (recur (rest shape1) shape2))))

(defn wider
  [sub-shape tensor]
  (if (empty? sub-shape)
    tensor
    (vec (repeat (first sub-shape)
                 (wider (rest sub-shape) tensor)))))

(defn broadcast
  [shape tensor]
  {:pre [(ntensor? tensor)
         (nvector? shape)
         (can-reshape? shape (fast-t-shape tensor))]}
  (wider (drop-last (count (fast-t-shape tensor)) shape) tensor))

(defn greatest-of
  [tensor-array]
  (apply max-key (fn [tensor] (count (fast-t-shape tensor))) tensor-array))

(defn same-shape-op
  [fun tensor-array]
  (if (every? number? tensor-array)
    (apply fun tensor-array)
    (apply mapv (fn [& sub-tensors] (same-shape-op fun sub-tensors)) tensor-array)))

(defn tensor-op
  [fun tensor-array]
  (let [g (fast-t-shape (greatest-of tensor-array))]
    {:pre [(every? ntensor? tensor-array)
           (every? (fn [tensor] (can-reshape? g (fast-t-shape tensor))) tensor-array)]}
    (same-shape-op fun (mapv (fn [tensor] (broadcast g tensor)) tensor-array))))

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
   {:pre [(nvector? vector)
          (v-const-size? 3 vector)]}
   vector)
  ([vector1 vector2]
   {:pre [(nvector? vector1)
          (nvector? vector2)
          (v-const-size? 3 vector1 vector2)]}
   (mapv (fn [i] (let [l (mod (+ i 1) 3)
                       r (mod (+ i 2) 3)]
                   (- (* (vector1 l) (vector2 r))
                      (* (vector1 r) (vector2 l)))))
         (range 0 3)))
  ([vector1 vector2 & vectors]
   (apply vect (vect vector1 vector2) vectors)))

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
  ([matrix1 matrix2]
   {:pre [(nmatrix? matrix1)
          (nmatrix? matrix2)
          (m-connecting? matrix1 matrix2)]}
   (let [t-matrix2 (transpose matrix2)]
     (mapv (fn [i] (mapv (fn [j] (scalar (matrix1 i) (t-matrix2 j)))
                         (range 0 (count t-matrix2))))
           (range 0 (count matrix1)))))
  ([matrix1 matrix2 & matrixes]
   (apply m*m (m*m matrix1 matrix2) matrixes)))

(defn b+
  [& tensors]
  (tensor-op + tensors))
(defn b-
  [& tensors]
  (tensor-op - tensors))
(defn b*
  [& tensors]
  (tensor-op * tensors))