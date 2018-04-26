'use strict';

var cnst = function (value) {
    return function () {
        return value;
    };
};

var TOKENS = {};

var CONSTS = {
    'pi': Math.PI,
    'e': Math.E
};

var VARIABLES = {
    'x' : 0,
    'y' : 1,
    'z' : 2
};

var create_operator = function (operation) {
    return function () {
        var args = arguments;
        return function () {
            var operands = [];
            for (var i = 0; i < args.length; i++) {
                operands.push(args[i].apply(null, arguments));
            }
            return operation.apply(null, operands);
        }
    }
};

var ready_operator = function (token, operation) {
    var operator = create_operator(operation);

    TOKENS[token] = {
        op: operator,
        arg_count: operation.length
    };
    return operator;
};

var variable = function (name) {
    return function () {
        return arguments[VARIABLES[name]];
    }
};

var add = ready_operator('+', function (a, b) {
    return a + b;
});

var subtract = ready_operator('-', function (a, b) {
    return a - b;
});

var multiply = ready_o1perator('*', function (a, b) {
    return a * b;
});

var divide = ready_operator('/', function (a, b) {
    return a / b;
});

var negate = ready_operator('negate', function (a) {
    return -a;
});

var cube = ready_operator('cube', function (a) {
    return Math.pow(a, 3);
});

var cuberoot = ready_operator('cuberoot', function (a) {
    return Math.pow(a, 1 / 3);
});

var min3 = ready_operator('min3', function (a1, a2, a3) {
    return Math.min.apply(null, arguments);
});

var max5 = ready_operator('max5', function (a1, a2, a3, a4, a5) {
    return Math.max.apply(null, arguments);
});

var pi = cnst(CONSTS['pi']);
var e = cnst(CONSTS['e']);
var x = variable('x');
var y = variable('y');
var z = variable('z');

var parse = function (str) {
    var str_tokens = str.split(' ').filter(function (token) {
        return token !== '';
    });

    var stack = [];
    for (var i = 0; i < str_tokens.length; i++) {
        if (str_tokens[i] in CONSTS) {
            stack.push(cnst(CONSTS[str_tokens[i]]));
        } else if (str_tokens[i] in TOKENS) {
            var args = [];
            for (var j = 0; j < TOKENS[str_tokens[i]].arg_count; j++) {
                args.push(stack.pop());
            }
            stack.push(TOKENS[str_tokens[i]].op.apply(null, args.reverse()));
        } else if (!isNaN(parseInt(str_tokens[i]))) {
            stack.push(cnst(parseInt(str_tokens[i])));
        } else {
            stack.push(variable(str_tokens[i]));
        }
    }

    return stack.pop();
};