/* 세탁 접수 페이지 상단의 수량, 기본금액, 추가/할인금액, 합계금액 누계 계산  */
function calculateMainPrice(items, softDeletedItems) {
    let fdQty = 0;
    let fdNormalAmt = 0;
    let changeAmt = 0;
    let fdRequestAmt = 0;

    items.forEach(el => {
        fdQty++;
        if(el.fdRetryYn === "N") {
            fdNormalAmt += el.fdNormalAmt * el.fdQty;
            changeAmt += (el.fdPressed + el.fdWhitening + el.fdWaterRepellent + el.fdStarch
                + el.fdPollution + el.fdAdd1Amt + el.fdRepairAmt -el.fdDiscountAmt) * el.fdQty;
            fdRequestAmt += el.fdRequestAmt;
        }
    });
    
    softDeletedItems.forEach(el => {
        fdQty--;
        if(el.fdRetryYn === "N") {
            fdNormalAmt -= el.fdNormalAmt * el.fdQty;
            changeAmt -= (el.fdPressed + el.fdWhitening + el.fdWaterRepellent + el.fdStarch
                + el.fdPollution + el.fdAdd1Amt + el.fdRepairAmt -el.fdDiscountAmt) * el.fdQty;
            fdRequestAmt -= el.fdRequestAmt;
        }
    });

    $("#totFdQty").html(fdQty.toLocaleString());
    $("#totFdNormalAmount").html(fdNormalAmt.toLocaleString());
    $("#totChangeAmount").html(changeAmt.toLocaleString());
    $("#totFdRequestAmount").html(fdRequestAmt.toLocaleString());
}

export {calculateMainPrice};