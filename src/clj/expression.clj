(defn smart-div
  [& values]
  (reduce (fn [x y] (/ x (double y))) values))

(defn constant
  [value]
  (constantly value))

(defn variable
  [name]
  (fn [vars] (vars name)))

(defn unary-operator
  [fun]
  (fn [operand]
    (fn [vars] (fun (operand vars)))))

(defn operator
  [fun]
  (fn [& operands]
    (fn [vars] (apply fun (mapv (fn [x] (x vars)) operands)))))

(def add
  (operator +))
(def subtract
  (operator -))
(def multiply
  (operator *))
(def divide
  (operator smart-div))
(def negate
  (unary-operator -))
(def sinh
  (unary-operator (fn [val] (Math/sinh val))))
(def cosh
  (unary-operator (fn [val] (Math/cosh val))))

(def operations
  {
   '+ add
   '- subtract
   '* multiply
   '/ divide
   'negate negate
   'sinh sinh
   'cosh cosh
   })

(defn parseExpression
  [expr]
  (cond
    (seq? expr) (apply (operations (first expr)) (mapv parseExpression (rest expr)))
    (number? expr) (constant expr)
    :else (variable (str expr))))

(def parseFunction (comp parseExpression read-string))