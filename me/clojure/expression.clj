; =====             =====
; =====    Utils    =====
; =====             =====

(defn smart-div
  [& values]
  (reduce (fn [x y] (/ x (double y))) values))

(defn get-property
  [this key]
  (if (contains? this key)
    (this key)
    (get-property (:prototype this) key)))

(defn call-function
  [this key & args]
  (apply (get-property this key) this args))

(defn field
  [key]
  (fn [this] (get-property this key)))

(defn method
  [key]
  (fn [this & args] (apply call-function this key args)))

; =====             =====
; ===== Homework 13 =====
; =====             =====

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

(def functionalOperations
  {
   '+      add
   '-      subtract
   '*      multiply
   '/      divide
   'negate negate
   'sinh   sinh
   'cosh   cosh
   })

(defn parseFunctionalExpression
  [expr]
  (cond
    (seq? expr) (apply (functionalOperations (first expr)) (mapv parseFunctionalExpression (rest expr)))
    (number? expr) (constant expr)
    :else (variable (str expr))))
(def parseFunction
  (comp parseFunctionalExpression read-string))

; =====             =====
; ===== Homework 14 =====
; =====             =====

(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))

(def operands (field :operands))

(def ConstantProto
  (let [number (field :value)]
    {:toString (fn [this]
                 (let [value (number this)] (if (integer? value) (str value) (format "%.1f" value))))
     :evaluate (fn [this _]
                 (number this))}))
(defn Constant
  [number]
  {:prototype ConstantProto
   :value     number})
(let [zero (Constant 0)]
  (def ConstantProto (assoc ConstantProto :diff (fn [this _] zero))))

(def VariableProto
  (let [name (field :value)
        zero (Constant 0)
        one (Constant 1)]
    {:toString (fn [this]
                 (name this))
     :evaluate (fn [this vars]
                 (vars (name this)))
     :diff     (fn [this var]
                 (if (= (name this) var) one zero))}))
(defn Variable
  [var-name]
  {:prototype VariableProto
   :value     var-name})

(let [operands (field :operands)
      write-as (field :write-as)
      function (field :function)
      derivative (method :derivative)]
  (def Operator
    {:toString (fn [this]
                 (str "(" (write-as this) " "
                      (clojure.string/join " " (mapv toString (operands this)))
                      ")"))
     :evaluate (fn [this vars]
                 (apply (function this)
                        (mapv (fn [operand] (evaluate operand vars))
                              (operands this))))
     :diff     (fn [this var]
                 (derivative this var))}))

(defn operator-factory
  [symbol function derivative & unary]
  (let [proto {:prototype  Operator
               :write-as   symbol
               :function   function
               :derivative derivative}]
    (fn [& operands]
      {:pre [(or (zero? (count unary)) (== 1 (count operands)))]}
      {:prototype proto
       :operands  (vec operands)})))

(let [op-at (fn [this idx] ((operands this) idx))
      do-at (fn [var this idx] (diff (op-at this idx) var))
      d-operands (fn [this var] (map (fn [operand] (diff operand var)) (operands this)))]
  ; any amount of args
  (def Add
    (operator-factory '+ + (fn [this var] (apply Add (d-operands this var)))))
  ; any amount of args
  (def Subtract
    (operator-factory '- - (fn [this var] (apply Subtract (d-operands this var)))))
  ; only two args
  (def Multiply
    (operator-factory '* * (fn [this var] (Add (Multiply (op-at this 0) (do-at var this 1))
                                               (Multiply (op-at this 1) (do-at var this 0))))))
  ; only two args
  (def Divide
    (operator-factory '/ smart-div (fn [this var] (Divide (Subtract (Multiply (op-at this 1) (do-at var this 0))
                                                                    (Multiply (op-at this 0) (do-at var this 1)))
                                                          (Multiply (op-at this 1) (op-at this 1))))))
  ; unary
  (def Negate
    (operator-factory 'negate - (fn [this var] (apply Negate (d-operands this var)))
                      'unary))
  (def Cos)
  ; unary
  (def Sin
    (operator-factory 'sin (fn [x] (Math/sin x)) (fn [this var] (Multiply (Cos (op-at this 0)) (do-at var this 0)))
                      'unary))
  ; unary
  (def Cos
    (operator-factory 'cos (fn [x] (Math/cos x)) (fn [this var] (Negate (Multiply (Sin (op-at this 0)) (do-at var this 0))))
                      'unary)))

(def objectOperations
  {
   '+      Add
   '-      Subtract
   '*      Multiply
   '/      Divide
   'negate Negate
   'sin    Sin
   'cos    Cos
   })
(def unaryOperations #{'sin 'cos 'negate})
(def termOperations #{'+ '-})
(def factorOperations #{'/ '*})

(defn parseObjectExpression
  [expr]
  (cond
    (seq? expr) (apply (objectOperations (first expr)) (mapv parseObjectExpression (rest expr)))
    (number? expr) (Constant expr)
    :else (Variable (str expr))))
(def parseObject
  (comp parseObjectExpression read-string))

(defn parseObjectInfixExpression
  [expr]
  (with-local-vars [create-factor (fn [])
                    parse-factor (fn [])
                    parse-term (fn [])
                    parse-expression (fn [])]
    (var-set create-factor
             (fn [factor]
               (cond
                 (seq? factor) ((parse-expression factor) :result)
                 (number? factor) (Constant factor)
                 :else (Variable (str factor)))))
    (var-set parse-factor
             (fn [left]
               (if (contains? unaryOperations (first left))
                 (let [op (objectOperations (first left))
                       factor (parse-factor (rest left))]
                   {:result (op (factor :result))
                    :left   (factor :left)})
                 {:result (create-factor (first left))
                  :left   (rest left)})))
    (var-set parse-term
             (fn
               ([left]
                (apply parse-term (vals (parse-factor left))))
               ([acc left]
                (if (or (empty? left) (not (contains? factorOperations (first left))))
                  {:result acc
                   :left   left}
                  (let [op (objectOperations (first left))
                        factor (parse-factor (rest left))]
                    (parse-term (op acc (factor :result)) (factor :left)))))))
    (var-set parse-expression
             (fn
               ([left]
                (apply parse-expression (vals (parse-term left))))
               ([acc left]
                (if (or (empty? left) (not (contains? termOperations (first left))))
                  {:result acc
                   :left   left}
                  (let [op (objectOperations (first left))
                        term (parse-term (rest left))]
                    (parse-expression (op acc (term :result)) (term :left)))))))
    ((parse-expression expr) :result)))
(def parseObjectInfix
  (comp parseObjectInfixExpression read-string (fn [string] (str "(" string ")"))))