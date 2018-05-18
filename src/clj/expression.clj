(defn smart-div
  [& values]
  (reduce (fn [x y] (/ x (double y))) values))

(defn constant
  [value]
  (fn [_] value))

(defn variable
  [name]
  (fn [vars] (vars name)))

(def operations {})

(defn unary-operator
  [fun string]
  (def operation (fn [operand]
      (fn [vars] (fun (operand vars)))))
  (def operations (assoc operations string operation))
  operation)

(defn operator
  [fun string]
  (def operation (fn [& operands]
      (fn [vars] (apply fun (mapv (fn [x] (x vars)) operands)))))
  (def operations (assoc operations string operation))
  operation)

(def add
  (operator + "+"))
(def subtract
  (operator - "-"))
(def multiply
  (operator * "*"))
(def divide
  (operator smart-div "/"))
(def negate
  (unary-operator - "negate"))
(def sinh
  (unary-operator (fn [val] (Math/sinh val)) "sinh"))
(def cosh
  (unary-operator (fn [val] (Math/cosh val)) "cosh"))

(defn parseFunction
  [expression]
  (cond
    (string? expression) (parseFunction (read-string expression))
    (seq? expression) (apply (operations (str (first expression))) (mapv parseFunction (rest expression)))
    (number? expression) (constant expression)
    :else (variable (str expression))))