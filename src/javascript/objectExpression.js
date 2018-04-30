"use strict";

var expressions = (function () {
    function createBy(constructor, args) {
        var instance = Object.create(constructor.prototype);
        constructor.apply(instance, args);
        return instance;
    }

    var VARS = {
        'x': 0,
        'y': 1,
        'z': 2,
    };
    var OPS = {};

    var ConstComparable = function () {
        this.equals = function (value) {
            return this instanceof Const && this.value === value;
        };
    };

    var Primitive = function () {
        this.toString = function () {
            return '' + this.value;
        };
        this.simplify = function () {
            return this;
        };
    };
    Primitive.prototype = new ConstComparable();

    var Const = function (value) {
        this.value = value;
    };
    Const.prototype = new Primitive();
    Const.prototype.evaluate = function () {
        return this.value;
    };
    Const.prototype.diff = function () {
        return new Const(0);
    };

    var Variable = function (name) {
        this.value = name;
    };
    Variable.prototype = new Primitive();
    Variable.prototype.evaluate = function () {
        return arguments[VARS[this.value]];
    };
    Variable.prototype.diff = function (name) {
        return new Const(name === this.value ? 1 : 0);
    };

    var AbstractOperator = function (symbol, operation, derivative, simplifier) {
        this.op = function (idx) {
            return this.operands[idx];
        };

        this.toString = function () {
            return this.operands.map(function (elem) {
                return elem.toString();
            }).join(' ') + ' ' + symbol;
        };
        this.evaluate = function () {
            var args = arguments;
            return operation.apply(null, this.operands.map(function (elem) {
                return elem.evaluate.apply(elem, args);
            }));
        };
        this.diff = function (name) {
            return derivative.call(this, name);
        };
        this.simplify = function () {
            var ops = this.operands.map(function (elem) {
                return elem.simplify();
            });
            if (ops.every(function (elem) {
                return elem instanceof Const
            })) {
                return new Const(operation.apply(null, ops.map(function (elem) {
                    return elem.value;
                })));
            }
            return simplifier.call(this);
        }
    };
    AbstractOperator.prototype = new ConstComparable();

    var operatorFactory = function (symbol, operation, derivative, simplifier) {
        var Operator = function () {
            this.operands = [].slice.call(arguments);
        };
        Operator.prototype = new AbstractOperator(symbol, operation, derivative,
            simplifier !== undefined ? simplifier : function () {
                return this
            });

        OPS[symbol] = {
            op: Operator,
            opCount: operation.length,
        };
        return Operator;
    };

    var Negate = operatorFactory(
        'negate',
        function (a) {
            return -a;
        },
        function (name) {
            return new Negate(this.op(0).diff(name));
        }
    );

    var Square = operatorFactory(
        'square',
        function (a) {
            return a * a;
        },
        function (name) {
            return new Multiply(
                new Const(2),
                new Multiply(this.op(0), this.op(0).diff(name))
            );
        }
    );

    var Sqrt = operatorFactory(
        'sqrt',
        function (a) {
            return Math.pow(Math.abs(a), 0.5);
        },
        function (name) {
            return new Divide(
                new Multiply(this.op(0).diff(name), this.op(0)),
                new Multiply(new Const(2), new Sqrt(new Multiply(this.op(0), new Square(this.op(0)))))
            );
        }
    );

    var Add = operatorFactory(
        '+',
        function (a, b) {
            return a + b;
        },
        function (name) {
            return new Add(this.op(0).diff(name), this.op(1).diff(name));
        },
        function () {
            if (this.op(0).equals(0)) {
                return this.op(1);
            }
            if (this.op(1).equals(0)) {
                return this.op(0);
            }
            return this;
        }
    );

    var Subtract = operatorFactory(
        '-',
        function (a, b) {
            return a - b;
        },
        function (name) {
            return new Subtract(this.op(0).diff(name), this.op(1).diff(name));
        },
        function () {
            if (this.op(0).equals(0)) {
                return new Negate(this.op(1));
            }
            if (this.op(1).equals(0)) {
                return this.op(0);
            }
            return this;
        }
    );

    var Multiply = operatorFactory(
        '*',
        function (a, b) {
            return a * b;
        },
        function (name) {
            return new Add(
                new Multiply(this.op(0), this.op(1).diff(name)),
                new Multiply(this.op(0).diff(name), this.op(1))
            );
        },
        function () {
            if (this.op(0).equals(0) || this.op(1).equals(0)) {
                return new Const(0);
            }
            if (this.op(0).equals(1)) {
                return this.op(1);
            }
            if (this.op(1).equals(1)) {
                return this.op(0);
            }
            return this;
        }
    );

    var Divide = operatorFactory(
        '/',
        function (a, b) {
            return a / b;
        },
        function (name) {
            return new Divide(
                new Subtract(
                    new Multiply(this.op(0).diff(name), this.op(1)),
                    new Multiply(this.op(0), this.op(1).diff(name))
                ),
                new Square(this.op(1))
            );
        },
        function () {
            if (this.op(0).equals(0)) {
                return new Const(0);
            }
            if (this.op(1).equals(1)) {
                return this.op(0);
            }
            return this;
        }
    );

    var parse = function (str) {
        var tokens = str.split(' ').filter(function (token) {
            return token !== '';
        });

        var stack = [];
        for (var i = 0; i < tokens.length; i++) {
            var token = tokens[i];
            if (token in OPS) {
                stack.push(createBy(OPS[token].op,
                    stack.splice(stack.length - OPS[token].opCount, OPS[token].opCount)));
            } else if (token in VARS) {
                stack.push(new Variable(token));
            } else if (!isNaN(parseInt(token))) {
                stack.push(new Const(parseInt(token)));
            } else {
                // some problems
            }
        }

        return stack.pop();
    };

    return {
        Const: Const,
        Variable: Variable,

        Negate: Negate,
        Square: Square,
        Sqrt: Sqrt,

        Add: Add,
        Subtract: Subtract,
        Multiply: Multiply,
        Divide: Divide,

        parse: parse,
    }

})();

var
    Const = expressions.Const,
    Variable = expressions.Variable,

    Negate = expressions.Negate,
    Square = expressions.Square,
    Sqrt = expressions.Sqrt,

    Add = expressions.Add,
    Subtract = expressions.Subtract,
    Multiply = expressions.Multiply,
    Divide = expressions.Divide,

    parse = expressions.parse;

var expr = new Add(new Const(1), new Const(1));
