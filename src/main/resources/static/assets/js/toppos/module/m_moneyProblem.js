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

function calculateItemPrice() {
    const ap = initialData.addCostData;
    const gradePrice = [100, 100, ap.bcHighRt, ap.bcPremiumRt, ap.bcChildRt];
    const gradeDiscount = [0, 0, ap.bcVipDcRt, ap.bcVvipDcRt];
    currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();

    currentRequest.fdPressed = $("#fdPress").is(":checked") ?
            parseInt(initialData.addCostData.bcPressed) : 0;
    currentRequest.fdWhitening = $("#fdWhitening").is(":checked") ?
        parseInt(initialData.addCostData.bcWhitening) : 0;
    currentRequest.fdWaterRepellent = $("#fdWaterRepellent").is(":checked") ?
        parseInt(initialData.addCostData.bcWaterRepellent) : 0;
    currentRequest.fdStarch = $("#fdStarch").is(":checked") ?
        parseInt(initialData.addCostData.bcStarch) : 0;
    currentRequest.fdPollutionLevel = $("input[name='cleanDirt']:checked").first().val() | 0;
    currentRequest.fdPollution = parseInt(initialData.addCostData["bcPollution" + currentRequest.fdPollutionLevel]) | 0;

    currentRequest.fdRepairAmt = ceil100(currentRequest.fdRepairAmt);
    currentRequest.fdAdd1Amt = ceil100(currentRequest.fdAdd1Amt);

    currentRequest.totAddCost = currentRequest.fdPressed + currentRequest.fdWhitening + currentRequest.fdWaterRepellent
            + currentRequest.fdStarch + currentRequest.fdPollution + currentRequest.fdAdd1Amt + currentRequest.fdRepairAmt;

    currentRequest.fdNormalAmt = ceil100(currentRequest.fdOriginAmt * gradePrice[currentRequest.fdPriceGrade] / 100);
    let sumAmt = ceil100((currentRequest.fdNormalAmt + currentRequest.totAddCost)
        * (100 - gradeDiscount[currentRequest.fdDiscountGrade]) / 100);
    currentRequest.fdRequestAmt = sumAmt * currentRequest.fdQty;
    currentRequest.fdTotAmt = currentRequest.fdRequestAmt;
    currentRequest.fdDiscountAmt = currentRequest.fdNormalAmt + currentRequest.totAddCost - sumAmt;

    if($("#fdRetry").is(":checked")) {
        currentRequest.fdRetryYn = "Y";
        currentRequest.fdNormalAmt = 0;
        currentRequest.totAddCost = 0;
        currentRequest.fdDiscountAmt = 0;
        currentRequest.fdRequestAmt = 0;
        currentRequest.fdTotAmt = 0;
        sumAmt = 0;
    }else{
        currentRequest.fdRetryYn = "N";
    }

    $("#fdNormalAmt").html(currentRequest.fdNormalAmt.toLocaleString());
    $("#totAddCost").html(currentRequest.totAddCost.toLocaleString());
    $("#fdDiscountAmt").html(currentRequest.fdDiscountAmt.toLocaleString());
    $("#sumAmt").html(sumAmt.toLocaleString());
}

function ceil100(num) {
    num = num.toString();
    let ceilAmount = 0;
    if(num.length >= 2 && num.substr(num.length - 2, 2) !== "00") {
        num = num.substr(0, num.length - 2) + "00";
        ceilAmount = 100;
    }
    return parseInt(num) + ceilAmount;
}

export {calculateMainPrice, calculateItemPrice, ceil100};