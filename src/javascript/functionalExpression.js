'use strict';

var create_operation = function(operation) {
    return function() {
        var args = arguments;
        return function(value) {
            for (var i = 0; i < args.length; i++) {
                args[i] = (args[i])(value);
            }
            return operation.apply(null, args);
        }
    }
};

var variable = function(name) {
    return function(var_value) {
        return var_value;
    }
};

var cnst = function(value) {
    return function() {
        return value;
    };
};

var add = create_operation(function(a, b) {
    return a + b;
});

var subtract = create_operation(function(a, b) {
    return a - b;
});

var multiply = create_operation(function(a, b) {
    return a * b;
});

var divide = create_operation(function(a, b) {
    return a / b;
});

var negate = create_operation(function(a) {
    return a;
});

var expr = add(variable('x'), cnst(2));