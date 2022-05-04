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
                + el.fdPollution + el.fdAdd1Amt + el.fdRepairAmt + el.fdUrgentAmt - el.fdDiscountAmt) * el.fdQty;
            fdRequestAmt += el.fdRequestAmt;
        }
    });

    softDeletedItems.forEach(el => {
        fdQty--;
        if(el.fdRetryYn === "N") {
            fdNormalAmt -= el.fdNormalAmt * el.fdQty;
            changeAmt -= (el.fdPressed + el.fdWhitening + el.fdWaterRepellent + el.fdStarch
                + el.fdPollution + el.fdAdd1Amt + el.fdRepairAmt + el.fdUrgentAmt - el.fdDiscountAmt) * el.fdQty;
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
        ceil100(parseInt(initialData.addCostData.bcPressed)) : 0;
    currentRequest.fdWhitening = $("#fdWhitening").is(":checked") ?
        ceil100(parseInt(initialData.addCostData.bcWhitening)) : 0;
    currentRequest.fdWaterRepellent = $("#fdWaterRepellent").is(":checked") ?
        ceil100(parseInt(initialData.addCostData.bcWaterRepellent)) : 0;
    currentRequest.fdStarch = $("#fdStarch").is(":checked") ?
        ceil100(parseInt(initialData.addCostData.bcStarch)) : 0;
    currentRequest.fdPollutionLevel = $("input[name='cleanDirt']:checked").first().val() | 0;
    currentRequest.fdPollution = ceil100(parseInt(initialData.addCostData["bcPollution" + currentRequest.fdPollutionLevel])) | 0;

    currentRequest.fdRepairAmt = ceil100(currentRequest.fdRepairAmt);
    currentRequest.fdAdd1Amt = ceil100(currentRequest.fdAdd1Amt);
    if(!currentRequest.fdAdd2Amt) currentRequest.fdAdd2Amt = 0;
    currentRequest.fdAdd2Amt = ceil100(currentRequest.fdAdd2Amt);



    currentRequest.fdNormalAmt = ceil100(currentRequest.fdOriginAmt * gradePrice[currentRequest.fdPriceGrade] / 100);

    currentRequest.fdUrgentType = $("input[name='fdUrgentType']:checked").length ?
        $("input[name='fdUrgentType']:checked").val() : "0";
    switch (currentRequest.fdUrgentType) {
        case "0" :
            currentRequest.fdUrgentAmt = 0;
            break;
        case "1" :
            currentRequest.fdUrgentAmt = ceil100(
                (currentRequest.fdNormalAmt * (initialData.addCostData.bcUrgentRate1 - 100) / 100));
            break;
        case "2" :
            currentRequest.fdUrgentAmt = ceil100(
                (currentRequest.fdNormalAmt * (initialData.addCostData.bcUrgentRate2 - 100) / 100));
            break;
        case "3" :
            currentRequest.fdUrgentAmt = ceil100(initialData.addCostData.bcUrgentAmt1);
            break;
    }

    currentRequest.totAddCost = currentRequest.fdPressed + currentRequest.fdWhitening + currentRequest.fdWaterRepellent
        + currentRequest.fdStarch + currentRequest.fdPollution + currentRequest.fdAdd1Amt
        + currentRequest.fdAdd2Amt + currentRequest.fdRepairAmt + currentRequest.fdUrgentAmt;

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

/* 100원 단위 이하 100원 단위 올림처리 */
function ceil100(num) {
    num = num.toString();
    let ceilAmount = 0;
    if(num.length >= 2 && num.substr(num.length - 2, 2) !== "00") {
        num = num.substr(0, num.length - 2) + "00";
        ceilAmount = 100;
    }
    return parseInt(num) + ceilAmount;
}

/* 결제 단계의 조작가능한 항목의 동작마다 이루어질 거스름돈(현금)과 미수발생금액 계산 */
function calculatePaymentStage(paidAmt) {
    const totRequestAmt = $("#totFdRequestAmount").html().toInt();
    const applySaveMoney = $("#applySaveMoney").html().toInt();
    const applyUncollectAmt = $("#applyUncollectAmt").html().toInt();

    /* 최종결제금액 = A - B + C - D */
    const totalAmt = totRequestAmt - applySaveMoney + applyUncollectAmt - paidAmt;

    $("#totalAmt").html(totalAmt.toLocaleString());

    const receiveCash = $("#receiveCash").html().toInt();
    const changeCash = receiveCash - totalAmt;
    const uncollectAmtCash = totalAmt - receiveCash;

    if(changeCash > 0) {
        $("#changeCash").html(changeCash.toLocaleString());
        $("#uncollectAmtCash").html("0");
    } else {
        $("#changeCash").html("0");
        $("#uncollectAmtCash").html(uncollectAmtCash.toLocaleString());
    }

    const receiveCard = $("#receiveCard").html().toInt();
    const uncollectAmtCard = totalAmt - receiveCard;
    if(uncollectAmtCard > 0) {
        $("#uncollectAmtCard").html(uncollectAmtCard.toLocaleString());
    }else{
        $("#uncollectAmtCard").html("0");
    }
}

export {calculateMainPrice, calculateItemPrice, ceil100, calculatePaymentStage};