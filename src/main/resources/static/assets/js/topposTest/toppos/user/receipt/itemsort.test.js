const itemSort = require("../../../../toppos/user/receipt/itemsort.js");
const {testForJest} = itemSort;

test('hello', () => {
    expect(testForJest()).toBe("hi");
});