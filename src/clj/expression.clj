(defn constant
  [value]
  (fn [_] value))

(defn variable
  [name]
  (fn [vars] (get vars name)))

(def operations {})

(defn operator
  [fun string]
  (def operation
    (fn [& operands]
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
  (operator (fn [l r] (/ (double l) (double r))) "/"))
(def negate
  (operator - "negate"))

(defn parseFunction
  [expression]
  (cond
    (string? expression) (parseFunction (read-string expression))
    (seq? expression) (apply (get operations (str (first expression))) (mapv parseFunction (rest expression)))
    (number? expression) (constant expression)
    :else (variable (str expression))))