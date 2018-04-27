"use strict";

var expressions = (function () {

    function createBy(constructor, args) {
        var result = Object.create(constructor.prototype);
        constructor.apply(result, args);
        return result;
    }

    var VARS = {
        'x': 0,
        'y': 1,
        'z': 2,
    };
    var OPS = {};

    function Primitive() {
        this.simplify = function () {
            return this;
        }
    }

    function Const(value) {
        this.getValue = function () {
            return value;
        };
        this.equals = function (other) {
            return value === other;
        }
    }

    var equals = function (c, value) {
        return c instanceof Const && c.equals(value);
    };
    Const.prototype = new Primitive();
    Const.prototype.toString = function () {
        return this.getValue().toString();
    };
    Const.prototype.evaluate = function () {
        return this.getValue();
    };
    Const.prototype.diff = function () {
        return new Const(0);
    };

    function Variable(name) {
        this.getName = function () {
            return name;
        }
    }

    Variable.prototype = new Primitive();
    Variable.prototype.toString = function () {
        return this.getName();
    };
    Variable.prototype.evaluate = function () {
        return arguments[VARS[this.getName()]];
    };
    Variable.prototype.diff = function (name) {
        return name === this.getName() ? new Const(1) : new Const(0);
    };

    function BasicOperator(operands, symbol, operation, derivative, simplifier) {
        this.operation = operation;
        this.derivative = derivative;
        this.simplifier = simplifier;
        this.getOperands = function () {
            return operands;
        };
        this.getSymbol = function () {
            return symbol;
        };
    }

    BasicOperator.prototype.toString = function () {
        return this.getOperands().join(' ') + ' ' + this.getSymbol()
    };
    BasicOperator.prototype.evaluate = function () {
        var args = arguments;
        return this.operation.apply(null, this.getOperands().map(function (elem) {
            return elem.evaluate.apply(elem, args);
        }));
    };
    BasicOperator.prototype.diff = function (name) {
        return this.derivative.apply(this, [name].concat(this.getOperands()));
    };
    BasicOperator.prototype.simplify = function () {
        var operands = this.getOperands().map(function (elem) {
            return elem.simplify()
        });
        if (operands.every(function (elem) {
            return elem instanceof Const
        })) {
            return new Const(createBy(this.constructor,
                [operands, undefined, this.operation, undefined, undefined]).evaluate());
        }

        return this.simplifier.apply(this, operands);
    };

    function operatorFactory(symbol, operation, derivative, simplifier) {
        var operator = function () {
            var operands = Array.prototype.slice.call(arguments);
            BasicOperator.call(this, operands, symbol, operation, derivative, simplifier);
        };
        operator.prototype = BasicOperator.prototype;
        OPS[symbol] = {
            op: operator,
            opCount: operation.length
        };

        return operator;
    }

    var Add = operatorFactory(
        '+',
        function (a, b) {
            return a + b;
        },
        function (name, a, b) {
            return new Add(a.diff(name), b.diff(name));
        },
        function (a, b) {
            if (equals(a, 0)) {
                return b;
            }
            if (equals(b, 0)) {
                return a;
            }
            return new Add(a, b);
        }
    );

    var Subtract = operatorFactory(
        '-',
        function (a, b) {
            return a - b;
        },
        function (name, a, b) {
            return new Subtract(a.diff(name), b.diff(name));
        },
        function (a, b) {
            if (equals(a, 0)) {
                return new Negate(b);
            }
            if (equals(b, 0)) {
                return a;
            }
            return new Subtract(a, b);
        }
    );

    var Square = operatorFactory(
        'square',
        function (a) {
            return a * a;
        },
        function (name, a) {
            return new Multiply(new Const(2), new Multiply(a, a.diff(name)));
        },
        function (a) {
            return new Square(a);
        }
    );

    var Sqrt = operatorFactory(
        'sqrt',
        function (a) {
            return Math.pow(Math.abs(a), 0.5);
        },
        function (name, a) {
            return new Divide(new Multiply(a.diff(name), a),
                new Multiply(new Const(2), new Sqrt(new Multiply(a, new Multiply(a, a)))));
        },
        function (a) {
            return new Sqrt(a);
        }
    );

    var Negate = operatorFactory(
        'negate',
        function (a) {
            return -a;
        },
        function (name, a) {
            return new Negate(a.diff(name));
        },
        function (a) {
            return new Negate(a);
        }
    );

    var Multiply = operatorFactory(
        '*',
        function (a, b) {
            return a * b;
        },
        function (name, a, b) {
            return new Add(new Multiply(a, b.diff(name)), new Multiply(a.diff(name), b));
        },
        function (a, b) {
            if (equals(a, 0) || equals(b, 0)) {
                return new Const(0);
            }
            if (equals(a, 1)) {
                return b;
            }
            if (equals(b, 1)) {
                return a;
            }
            return new Multiply(a, b);
        }
    );

    var Divide = operatorFactory(
        '/',
        function (a, b) {
            return a / b;
        },
        function (name, a, b) {
            return new Divide(new Subtract(new Multiply(a.diff(name), b), new Multiply(a, b.diff(name))),
                new Multiply(b, b));
        },
        function (a, b) {
            if (equals(a, 0)) {
                return new Const(0);
            }
            if (equals(b, 1)) {
                return a;
            }
            return new Divide(a, b);
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
        'Const': Const,
        'Variable': Variable,
        'Add': Add,
        'Subtract': Subtract,
        'Negate': Negate,
        'Square': Square,
        'Sqrt': Sqrt,
        'Multiply': Multiply,
        'Divide': Divide,
        'parse': parse
    }

})();

for (var name in expressions) {
    global[name] = expressions[name];
}