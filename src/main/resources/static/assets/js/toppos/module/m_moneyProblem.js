/* 세탁 접수 페이지 상단의 수량, 기본금액, 추가/할인금액, 합계금액 누계 계산  */
const calculateMainPrice = function (items) {
    let fdQty = 0;
    let fdNormalAmt = 0;
    let changeAmt = 0;
    let fdRequestAmt = 0;

    items.forEach(el => {
        fdQty++;
        if(el.fdRetryYn === 'N') {
            fdNormalAmt += el.fdNormalAmt * el.fdQty;
            changeAmt += (el.fdPressed + el.fdWhitening + el.fdWaterRepellent + el.fdStarch
                + el.fdPollution + el.fdAdd1Amt + el.fdRepairAmt + el.fdUrgentAmt
                - el.fdDiscountAmt - el.fdPromotionDiscountAmt) * el.fdQty;
            fdRequestAmt += el.fdRequestAmt;
        }
    });

    $('#totFdQty').html(fdQty.toLocaleString());
    $('#totFdNormalAmount').html(fdNormalAmt.toLocaleString());
    $('#totChangeAmount').html(changeAmt.toLocaleString());
    $('#totFdRequestAmount').html(fdRequestAmt.toLocaleString());
};

/* 접수, 통합조회 페이지 상품 등록, 수정 팝업에서의 가격을 계산 */
const calculateItemPrice = function () {
    const ap = initialData.addCostData;
    const gradePrice = [100, 100, ap.bcHighRt, ap.bcPremiumRt, ap.bcChildRt];
    const gradeDiscount = [0, 0, ap.bcVipDcRt, ap.bcVvipDcRt];

    /* 팝업창에서 입력, 체크된 항목을 기반으로 값이 1차적으로 정해지는 항목들 (이후 다른값과 함께 계산에 필요) */
    currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();
    currentRequest.fdPressed = $('#fdPress').is(':checked') ?
        ceil10(parseInt(initialData.addCostData.bcPressed, 10)) : 0;
    currentRequest.fdWhitening = $('#fdWhitening').is(':checked') ?
        ceil10(parseInt(initialData.addCostData.bcWhitening, 10)) : 0;
    currentRequest.fdWaterRepellent = $('#fdWaterRepellent').is(':checked') ?
        ceil10(parseInt(initialData.addCostData.bcWaterRepellent, 10)) : 0;
    currentRequest.fdStarch = $('#fdStarch').is(':checked') ?
        ceil10(parseInt(initialData.addCostData.bcStarch, 10)) : 0;
    currentRequest.fdPollutionLevel = $("input[name='cleanDirt']:checked").first().val() | 0;
    currentRequest.fdPollution = ceil10(parseInt(initialData.addCostData['bcPollution' + currentRequest.fdPollutionLevel], 10)) | 0;
    currentRequest.fdUrgentType = $("input[name='fdUrgentType']:checked").length ?
        $("input[name='fdUrgentType']:checked").val() : '0';
    currentRequest.fdNormalAmt = ceil10(currentRequest.fdOriginAmt * gradePrice[currentRequest.fdPriceGrade] / 100);

    if ($('#manualDiscountChk').is(':checked')) {
        currentRequest.fdPromotionType = 'H1';
        currentRequest.fdPromotionDiscountRate = parseInt($("#manualDiscountPercent").val());
    } else {
        currentRequest.fdPromotionType = '';
        currentRequest.fdPromotionDiscountRate = 0;
    }
    currentRequest.hpId = 0;

    /* 긴급 여부에 따라 변하는 가격 반영 */
    switch (currentRequest.fdUrgentType) {
        case '0' :
            currentRequest.fdUrgentAmt = 0;
            break;
        case '1' :
            currentRequest.fdUrgentAmt = ceil10(
                (currentRequest.fdNormalAmt * (initialData.addCostData.bcUrgentRate1 - 100) / 100));
            break;
        case '2' :
            currentRequest.fdUrgentAmt = ceil10(
                (currentRequest.fdNormalAmt * (initialData.addCostData.bcUrgentRate2 - 100) / 100));
            break;
        case '3' :
            currentRequest.fdUrgentAmt = ceil10(initialData.addCostData.bcUrgentAmt1);
            break;
    }

    if (currentRequest.fdPromotionType === "H1") {
        currentRequest.fdPromotionDiscountAmt =
            floor10(currentRequest.fdNormalAmt * currentRequest.fdPromotionDiscountRate / 100);
    } else {
        currentRequest.fdPromotionDiscountAmt = 0;
    }

    currentRequest.fdRepairAmt = ceil10(currentRequest.fdRepairAmt);
    currentRequest.fdAdd1Amt = ceil10(currentRequest.fdAdd1Amt);
    if(!currentRequest.fdAdd2Amt) {
        currentRequest.fdAdd2Amt = 0;
    }
    currentRequest.fdAdd2Amt = ceil10(currentRequest.fdAdd2Amt);

    const requestAddCost = currentRequest.fdPressed + currentRequest.fdWhitening + currentRequest.fdWaterRepellent
        + currentRequest.fdStarch + currentRequest.fdPollution + currentRequest.fdAdd1Amt
        + currentRequest.fdRepairAmt + currentRequest.fdUrgentAmt;
    currentRequest.totAddCost = requestAddCost;
    let cleanRequestAmt = ceil10((currentRequest.fdNormalAmt + requestAddCost)
        * (100 - gradeDiscount[currentRequest.fdDiscountGrade]) / 100) - currentRequest.fdPromotionDiscountAmt;

    currentRequest.fdDiscountAmt =
        (currentRequest.fdNormalAmt + requestAddCost) - (cleanRequestAmt + currentRequest.fdPromotionDiscountAmt)
    currentRequest.fdRequestAmt = (cleanRequestAmt) * currentRequest.fdQty;
    currentRequest.fdTotAmt = cleanRequestAmt * currentRequest.fdQty + currentRequest.fdAdd2Amt;

    if($('#fdRetry').is(':checked')) {
        currentRequest.fdRetryYn = 'Y';
        currentRequest.fdNormalAmt = 0;
        currentRequest.totAddCost = 0;
        currentRequest.fdDiscountAmt = 0;
        currentRequest.fdPromotionDiscountAmt = 0; // 수기할인 항목
        currentRequest.fdRequestAmt = 0;
        currentRequest.fdTotAmt = 0;
        currentRequest.fdUrgentAmt = 0;
        cleanRequestAmt = 0;
    }else{
        currentRequest.fdRetryYn = 'N';
    }

    $('#fdNormalAmt').html(currentRequest.fdNormalAmt.toLocaleString());
    $('#totAddCost').html(currentRequest.totAddCost.toLocaleString());
    $('#fdDiscountAmt').html((currentRequest.fdDiscountAmt + currentRequest.fdPromotionDiscountAmt).toLocaleString());
    $('#cleanRequestAmt').html(cleanRequestAmt.toLocaleString());
};

/* 접수된 세탁물의 그리드 리스트를 순회하여 행사를 적용한다. */
const applyPromotionDiscount = function () {
    const items = AUIGrid.getGridData('grid_main');
    const onePlusOneCnt = {};
    const twoPlusOneCnt = {};

    for (let i = 0; i < items.length; i++) {
        const activePromotion = initialData.activePromotionData[items[i].biItemcode];
        /* 프로모션 중이고, 수기행사 적용이 안되어 있으며, 고객 할인등급을 일반으로 선택한 항목의 세탁물에 대해서만 행사적용 */
        if (activePromotion && items[i].fdPromotionType !== "H1" && items[i].fdRetryYn === "N" && items[i].fdDiscountGrade === "1") {
            items[i].hpId = activePromotion.hpId;
            items[i].fdPromotionType = activePromotion.hpType;
            items[i].fdPromotionDiscountRate = 0;
            const preFdPromotionDiscountAmt = items[i].fdPromotionDiscountAmt;

            /* hpType에 따라 행사 할인 금액을 정하고 */
            switch (activePromotion.hpType) {
                case "01":
                    items[i].fdPromotionDiscountRate = activePromotion.hiDiscountRt;
                    items[i].fdPromotionDiscountAmt =
                        floor10(items[i].fdNormalAmt * items[i].fdPromotionDiscountRate / 100);
                    break;
                case "02":
                    if (onePlusOneCnt[items[i].biItemcode + items[i].fdPriceGrade] && items[i].fdQty === 1) {
                        if (items[i].fdQty === 1 && ++onePlusOneCnt[items[i].biItemcode + items[i].fdPriceGrade] % 2 === 0) {
                            items[i].fdPromotionDiscountAmt = items[i].fdNormalAmt;
                            items[i].fdPromotionDiscountRate = 100;
                        } else {
                            items[i].fdPromotionDiscountAmt = 0;
                        }
                    } else if(items[i].fdQty === 1) {
                        onePlusOneCnt[items[i].biItemcode + items[i].fdPriceGrade] = 1;
                        items[i].fdPromotionDiscountAmt = 0;
                    } else {
                        items[i].hpId = 0;
                        items[i].fdPromotionType = "";
                        items[i].fdPromotionDiscountAmt = 0;
                    }
                    break;
                case "03":
                    if (twoPlusOneCnt[items[i].biItemcode + items[i].fdPriceGrade] && items[i].fdQty === 1) {
                        if (items[i].fdQty === 1 && ++twoPlusOneCnt[items[i].biItemcode + items[i].fdPriceGrade] % 3 === 0) {
                            items[i].fdPromotionDiscountAmt = items[i].fdNormalAmt;
                            items[i].fdPromotionDiscountRate = 100;
                        } else {
                            items[i].fdPromotionDiscountAmt = 0;
                        }
                    } else if(items[i].fdQty === 1) {
                        twoPlusOneCnt[items[i].biItemcode + items[i].fdPriceGrade] = 1;
                        items[i].fdPromotionDiscountAmt = 0;
                    } else {
                        items[i].hpId = 0;
                        items[i].fdPromotionType = "";
                        items[i].fdPromotionDiscountAmt = 0;
                    }
                    break;
            }
            /* 정해진 행사 금액에 따라 합계 금액 계산 */
            items[i].fdRequestAmt = items[i].fdRequestAmt - items[i].fdPromotionDiscountAmt * items[i].fdQty
                + preFdPromotionDiscountAmt * items[i].fdQty;
            items[i].fdTotAmt = items[i].fdTotAmt - items[i].fdPromotionDiscountAmt * items[i].fdQty
                + preFdPromotionDiscountAmt * items[i].fdQty;
        }
    }

    AUIGrid.updateRowsById('grid_main', items);
}

/* 10원 단위 이하 10원 단위 올림처리 */
const ceil10 = function (num) {
    num = num.toString();
    let ceilAmount = 0;
    if(num !=='0' && num.substr(num.length - 1, 1) !== '0') {
        num = num.substr(0, num.length - 1) + '0';
        ceilAmount = 10;
    }
    return parseInt(num, 10) + ceilAmount;
};

/* 10원 단위 이하 10원 단위 버림처리 */
const floor10 = function (num) {
    num = num.toString();
    num = num.substr(0, num.length - 1) + '0';
    return parseInt(num, 10);
};

/* 결제 단계의 조작가능한 항목의 동작마다 이루어질 거스름돈(현금)과 미수발생금액 계산 */
const calculatePaymentStage = function (paidAmt) {
    const totRequestAmt = $('#totFdRequestAmount').html().toInt();
    const applySaveMoney = $('#applySaveMoney').html().toInt();
    const applyUncollectAmt = $('#applyUncollectAmt').html().toInt();

    /* 최종결제금액 = A - B + C - D */
    const totalAmt = totRequestAmt - applySaveMoney + applyUncollectAmt - paidAmt;

    $('#totalAmt').html(totalAmt.toLocaleString());

    const receiveCash = $('#receiveCash').html().toInt();
    const changeCash = receiveCash - totalAmt;
    const uncollectAmtCash = totalAmt - receiveCash;

    if(changeCash > 0) {
        $('#changeCash').html(changeCash.toLocaleString());
        $('#uncollectAmtCash').html('0');
    } else {
        $('#changeCash').html('0');
        $('#uncollectAmtCash').html(uncollectAmtCash.toLocaleString());
    }

    const receiveCard = $('#receiveCard').html().toInt();
    const uncollectAmtCard = totalAmt - receiveCard;
    if(uncollectAmtCard > 0) {
        $('#uncollectAmtCard').html(uncollectAmtCard.toLocaleString());
    }else{
        $('#uncollectAmtCard').html('0');
    }
};

export {calculateMainPrice, calculateItemPrice, ceil10, calculatePaymentStage, applyPromotionDiscount};
