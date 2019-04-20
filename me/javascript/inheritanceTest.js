"use strict";

var Primitive = function (symbol) {
    if (symbol !== undefined) {
        this.symbol = symbol;
    }

    this.toString = function () {
        return this.value + ' from call to instance of Primitive(\'' + this.symbol + '\')';
    };
};
Primitive.prototype.toString = function () {
    return this.value + ' from call to Primitive.prototype';
};
Primitive.prototype.symbol = 'lol';

var Const = function (value) {
    this.value = value;
};
Const.prototype = Primitive.prototype;

var Variable = function (name) {
    this.value = name;
};
Variable.prototype = new Primitive('kek');

var OtherShit = function (other_shit) {
    this.value = 'some ' + other_shit;
};
OtherShit.prototype = new Primitive();

var NoPrototype = function () {
    this.value = 'no base';
};

console.log(new Const(1).toString());
console.log(new Variable('x').toString());
console.log(new OtherShit('other shit').toString());
console.log(new NoPrototype().toString());