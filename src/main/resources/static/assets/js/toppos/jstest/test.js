import { testFunc } from "../user/receipt/receiptreg.js";

QUnit.module('receiptreg', function() {
    QUnit.test('', function(assert) {
        assert.equal(testFunc(1, 2), 3);
    });
});