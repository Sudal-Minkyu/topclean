import {calculateMainPrice, calculateItemPrice, ceil100, calculatePaymentStage} from "../module/m_moneyProblem.js";

function runMoneyProblem() {
    QUnit.module('moneyProblem 돈계산', function () {
        QUnit.test('접수페이지 상단 누계 계산 1', function (assert) {
            const testDOM = `
            <div class="regist__info">
                <ul class="regist__amount">
                    <li>
                        <h4>수량</h4>
                        <p><span id="totFdQty">0</span></p>
                    </li>
                    <li>
                        <h4>기본금액</h4>
                        <p><span id="totFdNormalAmount">0</span><span class="won">원</span></p>
                    </li>
                    <li>
                        <h4>추가/할인금액</h4>
                        <p><span id="totChangeAmount">0</span><span class="won">원</span></p>
                    </li>
                </ul>
                <div class="regist__sum">
                    <h4>합계금액</h4>
                    <p><span id="totFdRequestAmount">0</span><span class="won">원</span></p>
                </div>
            </div>
        `;
            const playGround = document.createElement("div");
            $(playGround).html(testDOM);
            $("body").append(playGround);

            const existItem = [
                {
                    "fdTag": "2220020",
                    "biItemcode": "D01N004",
                    "fdColor": "00",
                    "fdPattern": "00",
                    "fdPriceGrade": "1",
                    "fdOriginAmt": 4800,
                    "fdNormalAmt": 4800,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 4800,
                    "fdTotAmt": 4800,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 0,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "AE21248D-71C4-EF71-1227-8C1A1ADBE61B",
                    "sumName": "니트 상의",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220022",
                    "biItemcode": "D01L001",
                    "fdColor": "06",
                    "fdPattern": "00",
                    "fdPriceGrade": "2",
                    "fdOriginAmt": 6500,
                    "fdNormalAmt": 9800,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 2000,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 11800,
                    "fdTotAmt": 11800,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 2000,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "C0C49DAF-8729-0965-F005-8C1AD94107BB",
                    "sumName": "롱 패딩 상의",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220023",
                    "biItemcode": "D01S002",
                    "fdColor": "00",
                    "fdPattern": "00",
                    "fdPriceGrade": "1",
                    "fdOriginAmt": 4400,
                    "fdNormalAmt": 4400,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "Y",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 6000,
                    "fdPollutionLevel": 3,
                    "fdStarch": 3000,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 13400,
                    "fdTotAmt": 13400,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 9000,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "Y",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "04B450B6-8A4A-A90A-C6C9-8C1B61420C46",
                    "sumName": "숏 맨투맨 상의",
                    "fdUrgentAmt": 0,
                }
            ];

            calculateMainPrice(existItem, []);
            assert.equal($("#totFdQty").html(), "3", "수량 검사");
            assert.equal($("#totFdNormalAmount").html(), "19,000", "기본금액 검사");
            assert.equal($("#totChangeAmount").html(), "11,000", "추가/할인금액 검사");
            assert.equal($("#totFdRequestAmount").html(), "30,000", "합계금액 검사");
            $(".regist__info").parent("div").remove();
        });

        QUnit.test('접수페이지 상단 누계 계산 2', function (assert) {
            const testDOM = `
            <div class="regist__info">
                <ul class="regist__amount">
                    <li>
                        <h4>수량</h4>
                        <p><span id="totFdQty">0</span></p>
                    </li>
                    <li>
                        <h4>기본금액</h4>
                        <p><span id="totFdNormalAmount">0</span><span class="won">원</span></p>
                    </li>
                    <li>
                        <h4>추가/할인금액</h4>
                        <p><span id="totChangeAmount">0</span><span class="won">원</span></p>
                    </li>
                </ul>
                <div class="regist__sum">
                    <h4>합계금액</h4>
                    <p><span id="totFdRequestAmount">0</span><span class="won">원</span></p>
                </div>
            </div>
        `;
            const playGround = document.createElement("div");
            $(playGround).html(testDOM);
            $("body").append(playGround);

            const existItem = [
                {
                    "fdTag": "2220024",
                    "biItemcode": "D01N004",
                    "fdColor": "00",
                    "fdPattern": "00",
                    "fdPriceGrade": "1",
                    "fdOriginAmt": 4800,
                    "fdNormalAmt": 4800,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 4800,
                    "fdTotAmt": 4800,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 0,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "5C9D599C-B414-F716-41C1-8CBB855770A7",
                    "sumName": "니트 상의",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220025",
                    "biItemcode": "D01L001",
                    "fdColor": "04",
                    "fdPattern": "00",
                    "fdPriceGrade": "1",
                    "fdOriginAmt": 6500,
                    "fdNormalAmt": 0,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 0,
                    "fdTotAmt": 0,
                    "fdRetryYn": "Y",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 0,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "D62599DC-53AF-CAEC-E3CD-8CBBDB920CEA",
                    "sumName": "롱 패딩 상의",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220026",
                    "biItemcode": "D03N001",
                    "fdColor": "05",
                    "fdPattern": "00",
                    "fdPriceGrade": "1",
                    "fdOriginAmt": 1200,
                    "fdNormalAmt": 1200,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 2000,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 3200,
                    "fdTotAmt": 3200,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 2000,
                    "fdAgreeType": "2",
                    "fdSignImage": "",
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "38C0B440-D429-7526-6E73-8CBC200D8D9B",
                    "sumName": "면 운동화",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220027",
                    "biItemcode": "D01S002",
                    "fdColor": "07",
                    "fdPattern": "00",
                    "fdPriceGrade": "3",
                    "fdOriginAmt": 4400,
                    "fdNormalAmt": 11000,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 1000,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 6000,
                    "fdPollutionLevel": 3,
                    "fdStarch": 0,
                    "fdWaterRepellent": 7000,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 25000,
                    "fdTotAmt": 25000,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 14000,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "Y",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "B8FD5753-606B-0832-6370-8CBC73FD95DB",
                    "sumName": "숏 맨투맨 상의",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220028",
                    "biItemcode": "D01N004",
                    "fdColor": "00",
                    "fdPattern": "00",
                    "fdPriceGrade": "4",
                    "fdOriginAmt": 4800,
                    "fdNormalAmt": 3900,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "Y",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 3900,
                    "fdTotAmt": 3900,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 0,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "8337055D-14E2-97FD-76CC-8CBC9CF6D9B6",
                    "sumName": "니트 상의",
                    "fdUrgentAmt": 0,
                }
            ];

            const removedItem = [
                {
                    "fdTag": "2220027",
                    "biItemcode": "D01S002",
                    "fdColor": "07",
                    "fdPattern": "00",
                    "fdPriceGrade": "3",
                    "fdOriginAmt": 4400,
                    "fdNormalAmt": 11000,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 0,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 1000,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 6000,
                    "fdPollutionLevel": 3,
                    "fdStarch": 0,
                    "fdWaterRepellent": 7000,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 25000,
                    "fdTotAmt": 25000,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 14000,
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "Y",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "B8FD5753-606B-0832-6370-8CBC73FD95DB",
                    "sumName": "숏 맨투맨 상의",
                    "fdUrgentAmt": 0,
                },
                {
                    "fdTag": "2220026",
                    "biItemcode": "D03N001",
                    "fdColor": "05",
                    "fdPattern": "00",
                    "fdPriceGrade": "1",
                    "fdOriginAmt": 1200,
                    "fdNormalAmt": 1200,
                    "fdRepairRemark": "",
                    "fdRepairAmt": 2000,
                    "fdAdd1Remark": "",
                    "fdSpecialYn": "N",
                    "fdUrgentYn": "N",
                    "fdAdd1Amt": 0,
                    "fdAdd2Remark": "",
                    "fdAdd2Amt": 0,
                    "fdPressed": 0,
                    "fdWhitening": 0,
                    "fdPollution": 0,
                    "fdPollutionLevel": 0,
                    "fdStarch": 0,
                    "fdWaterRepellent": 0,
                    "fdDiscountGrade": "1",
                    "fdDiscountAmt": 0,
                    "fdQty": 1,
                    "fdRequestAmt": 3200,
                    "fdTotAmt": 3200,
                    "fdRetryYn": "N",
                    "fdRemark": "",
                    "frEstimateDate": "20220318",
                    "photoList": [],
                    "totAddCost": 2000,
                    "fdAgreeType": "2",
                    "fdSignImage": "",
                    "fdPollutionLocFcn": "N",
                    "fdPollutionLocBcn": "N",
                    "fdPollutionLocFrh": "N",
                    "fdPollutionLocBlh": "N",
                    "fdPollutionLocFlh": "N",
                    "fdPollutionLocBrh": "N",
                    "fdPollutionLocFcs": "N",
                    "fdPollutionLocBcs": "N",
                    "fdPollutionLocFrf": "N",
                    "fdPollutionLocBlf": "N",
                    "fdPollutionLocFlf": "N",
                    "fdPollutionLocBrf": "N",
                    "fdPollutionLocFcb": "N",
                    "fdPollutionLocBcb": "N",
                    "_$uid": "38C0B440-D429-7526-6E73-8CBC200D8D9B",
                    "sumName": "면 운동화",
                    "fdUrgentAmt": 0,
                }
            ];

            calculateMainPrice(existItem, removedItem);
            assert.equal($("#totFdQty").html(), "3", "수량 검사");
            assert.equal($("#totFdNormalAmount").html(), "8,700", "기본금액 검사");
            assert.equal($("#totChangeAmount").html(), "0", "추가/할인금액 검사");
            assert.equal($("#totFdRequestAmount").html(), "8,700", "합계금액 검사");
            $(".regist__info").parent("div").remove();
        });

        QUnit.test('접수페이지 접수물품 가격 계산', function (assert) {
            window.ceil100 = ceil100;

            window.initialData = {};

            window.initialData.addCostData = {
                "bcVipDcRt": 30,
                "bcVvipDcRt": 50,
                "bcHighRt": 150,
                "bcPremiumRt": 250,
                "bcChildRt": 80,
                "bcPressed": 2000,
                "bcWhitening": 1000,
                "bcPollution1": 2000,
                "bcPollution2": 3500,
                "bcPollution3": 6000,
                "bcPollution4": 7500,
                "bcPollution5": 10000,
                "bcStarch": 3000,
                "bcWaterRepellent": 7000
            };

            window.currentRequest = {
                "fdTag": "",
                "biItemcode": "D01N001",
                "fdColor": "00",
                "fdPattern": "00",
                "fdPriceGrade": "1",
                "fdOriginAmt": 3600,
                "fdNormalAmt": 0,
                "fdRepairRemark": "기장",
                "fdRepairAmt": 1000,
                "fdAdd1Remark": "황변제거",
                "fdSpecialYn": "N",
                "fdUrgentYn": "N",
                "fdAdd1Amt": 200,
                "fdAdd2Remark": "",
                "fdAdd2Amt": 0,
                "fdPressed": 0,
                "fdWhitening": 0,
                "fdPollution": 0,
                "fdPollutionLevel": 0,
                "fdStarch": 0,
                "fdWaterRepellent": 0,
                "fdDiscountGrade": "2",
                "fdDiscountAmt": 0,
                "fdQty": 1,
                "fdRequestAmt": 0,
                "fdTotAmt": 0,
                "fdRetryYn": "Y",
                "fdRemark": "",
                "frEstimateDate": "",
                "photoList": [],
                "totAddCost": 0
            };

            const testDOM = `
            <div id="testDiv">
                <input type="radio" name="fdPriceGrade" value="3" id="price3" checked>
                <input type="radio" name="fdDiscountGrade" value="1" id="class01" checked>
                <input type="checkbox" name="fdPress" value="다림질" id="fdPress" checked>
                <input type="checkbox" name="fdRetry" value="재세탁" id="fdRetry">
                <input type="radio" name="cleanDirt" id="pollution05" value="5" checked>
                <input type="checkbox" name="fdWhitening" id="fdWhitening" checked>
                <input type="radio" id="fdWaterRepellent" name="waterProcess" checked>
                <input type="radio" id="fdStarch" name="waterProcess">

                <ul class="product-pop__price-list">
                    <li class="product-pop__price">
                        <h4>기본가격</h4>
                        <span id="fdNormalAmt"></span>
                        <span class="won">원</span>
                    </li>
                    <li class="product-pop__price">
                        <h4>수선/추가요금</h4>
                        <span id="totAddCost"></span>
                        <span class="won">원</span>
                    </li>
                    <li class="product-pop__price">
                        <h4>할인요금</h4>
                        <span id="fdDiscountAmt"></span>
                        <span class="won">원</span>
                    </li>
                    <li class="product-pop__price-sum">
                        <h4>최종가격</h4>
                        <span id="sumAmt"></span>
                        <span class="won">원</span>
                    </li>
                </ul>
            </div>
        `;

            const playGround = document.createElement("div");
            $(playGround).html(testDOM);
            $("body").append(playGround);

            calculateItemPrice();

            assert.equal(currentRequest.fdPriceGrade, "3", "가격 등급");
            assert.equal(currentRequest.fdNormalAmt, 9000, "가격 등급 반영 가격");
            assert.equal(currentRequest.fdDiscountGrade, "1", "할인 등급");
            assert.equal(currentRequest.fdDiscountAmt, 0, "할인 등급 반영 할인액");
            assert.equal(currentRequest.fdPressed, 2000, "다림질");
            assert.equal(currentRequest.fdRetryYn, "N", "재세탁여부");
            assert.equal(currentRequest.fdPollution, 10000, "오염금액");
            assert.equal(currentRequest.fdPollutionLevel, 5, "오염등급");
            assert.equal(currentRequest.fdWhitening, 1000, "표백");
            assert.equal(currentRequest.fdWaterRepellent, 7000, "발수가공");
            assert.equal(currentRequest.fdStarch, 0, "풀먹임");
            assert.equal(currentRequest.totAddCost, 21200, "수선/추가 요금");
            assert.equal(currentRequest.fdTotAmt, 30200, "최종가격");
            assert.equal($("#fdNormalAmt").html(), "9,000", "기본가격 표시");
            assert.equal($("#totAddCost").html(), "21,200", "수선/추가요금 표시");
            assert.equal($("#fdDiscountAmt").html(), "0", "할인요금 표시");
            assert.equal($("#sumAmt").html(), "30,200", "최종가격 표시");

            $("#testDiv").parent("div").remove();
            delete window.initialData;
            delete window.ceil100;
            delete window.currentRequest;
        });

        QUnit.test('접수페이지 접수물품 가격 계산_220509', function (assert) {
            window.ceil100 = ceil100;

            window.initialData = {};

            window.initialData.addCostData = {
                "bcVipDcRt": 30,
                "bcVvipDcRt": 50,
                "bcHighRt": 150,
                "bcPremiumRt": 250,
                "bcChildRt": 80,
                "bcPressed": 2000,
                "bcWhitening": 1000,
                "bcPollution1": 2000,
                "bcPollution2": 3500,
                "bcPollution3": 6000,
                "bcPollution4": 7500,
                "bcPollution5": 10000,
                "bcStarch": 3000,
                "bcWaterRepellent": 7000,
                "bcUrgentRate1": 200,
                "bcUrgentRate2": 150,
                "bcUrgentAmt1": 500
            };

            window.currentRequest = {
                "fdTag": "2220007",
                "biItemcode": "D01N003",
                "fdColor": "04",
                "fdPattern": "01",
                "fdPriceGrade": "0",
                "fdOriginAmt": 3600,
                "fdNormalAmt": 0,
                "fdRepairRemark": "",
                "fdRepairAmt": 0,
                "fdAdd1Remark": "",
                "fdSpecialYn": "N",
                "fdUrgentYn": "Y",
                "fdUrgentType": "0",
                "fdUrgentAmt": 0,
                "fdAdd1Amt": 0,
                "fdAdd2Remark": "",
                "fdAdd2Amt": 0,
                "fdPressed": 0,
                "fdWhitening": 0,
                "fdPollution": 7500,
                "fdPollutionLevel": 4,
                "fdStarch": 0,
                "fdWaterRepellent": 0,
                "fdDiscountGrade": "0",
                "fdDiscountAmt": 0,
                "fdQty": 1,
                "fdRequestAmt": 0,
                "fdTotAmt": 0,
                "fdRetryYn": "N",
                "fdRemark": "testuser",
                "frEstimateDate": "20220512",
                "photoList": [],
                "totAddCost": 0,
                "sumName": "군복 상의",
                "fdPollutionLocFcn": "N",
                "fdPollutionLocBcn": "N",
                "fdPollutionLocFrh": "N",
                "fdPollutionLocBlh": "N",
                "fdPollutionLocFlh": "Y",
                "fdPollutionLocBrh": "N",
                "fdPollutionLocFcs": "N",
                "fdPollutionLocBcs": "N",
                "fdPollutionLocFrf": "N",
                "fdPollutionLocBlf": "N",
                "fdPollutionLocFlf": "N",
                "fdPollutionLocBrf": "N",
                "fdPollutionLocFcb": "N",
                "fdPollutionLocBcb": "N",
                "fdPollutionBack": 0,
                "fdPollutionType": 1,
            };

            const testDOM = `
            <div id="testDiv">
                <input type="radio" name="fdPriceGrade" value="3" id="price3" checked>
                <input type="radio" name="fdDiscountGrade" value="3" id="class03" checked>
                <input type="checkbox" name="fdUrgentType" id="fdUrgent1" value="1" checked>
                <input type="checkbox" name="fdPress" value="다림질" id="fdPress">
                <input type="checkbox" name="fdRetry" value="재세탁" id="fdRetry">
                <input type="radio" name="cleanDirt" id="pollution04" value="4" checked>
                <input type="checkbox" name="fdWhitening" id="fdWhitening">
                <input type="radio" id="fdWaterRepellent" name="waterProcess">
                <input type="radio" id="fdStarch" name="waterProcess">

                <ul class="product-pop__price-list">
                    <li class="product-pop__price">
                        <h4>기본가격</h4>
                        <span id="fdNormalAmt"></span>
                        <span class="won">원</span>
                    </li>
                    <li class="product-pop__price">
                        <h4>수선/추가요금</h4>
                        <span id="totAddCost"></span>
                        <span class="won">원</span>
                    </li>
                    <li class="product-pop__price">
                        <h4>할인요금</h4>
                        <span id="fdDiscountAmt"></span>
                        <span class="won">원</span>
                    </li>
                    <li class="product-pop__price-sum">
                        <h4>최종가격</h4>
                        <span id="sumAmt"></span>
                        <span class="won">원</span>
                    </li>
                </ul>
            </div>
        `;

            const playGround = document.createElement("div");
            $(playGround).html(testDOM);
            $("body").append(playGround);

            calculateItemPrice();

            assert.equal(currentRequest.fdPriceGrade, "3", "가격 등급");
            assert.equal(currentRequest.fdNormalAmt, 9000, "가격 등급 반영 가격");
            assert.equal(currentRequest.fdDiscountGrade, "3", "할인 등급");
            assert.equal(currentRequest.fdDiscountAmt, 12700, "할인 등급 반영 할인액");
            assert.equal(currentRequest.fdPressed, 0, "다림질");
            assert.equal(currentRequest.fdRetryYn, "N", "재세탁여부");
            assert.equal(currentRequest.fdPollution, 7500, "오염금액");
            assert.equal(currentRequest.fdPollutionLevel, 4, "오염등급");
            assert.equal(currentRequest.fdWhitening, 0, "표백");
            assert.equal(currentRequest.fdWaterRepellent, 0, "발수가공");
            assert.equal(currentRequest.fdStarch, 0, "풀먹임");
            assert.equal(currentRequest.totAddCost, 16500, "수선/추가 요금");
            assert.equal(currentRequest.fdTotAmt, 12800, "최종가격");
            assert.equal($("#fdNormalAmt").html(), "9,000", "기본가격 표시");
            assert.equal($("#totAddCost").html(), "16,500", "수선/추가요금 표시");
            assert.equal($("#fdDiscountAmt").html(), "12,700", "할인요금 표시");
            assert.equal($("#sumAmt").html(), "12,800", "최종가격 표시");

            $("#testDiv").parent("div").remove();
            delete window.initialData;
            delete window.ceil100;
            delete window.currentRequest;
        });

        QUnit.test('접수페이지 결제창 거스름돈, 미수발생금액 계산', function (assert) {

            const testDOM = `
            <div id="testDiv">
                <span id="totFdRequestAmount">10,500</span>
                <span id="applySaveMoney">2,000</span>
                <span id="applyUncollectAmt">3,000</span>
                <span id="receiveCash">12,500</span>
                <span id="receiveCard">4,000</span>

                <span id="totalAmt">0</span>
                <span id="changeCash">0</span>
                <span id="uncollectAmtCash">0</span>
                <span id="uncollectAmtCard">0</span>
            </div>
        `;

            const playGround = document.createElement("div");
            $(playGround).html(testDOM);
            $("body").append(playGround);

            /* 미리 한 결제 총 금액이 3000원이라 상정하고 구동  */
            calculatePaymentStage(3000);

            assert.equal($("#totalAmt").html(), "8,500", "총 결제할(현금, 카드) 금액");
            assert.equal($("#changeCash").html(), "4,000", "현금 거스름돈");
            assert.equal($("#uncollectAmtCash").html(), "0", "미수금발생(현금)");
            assert.equal($("#uncollectAmtCard").html(), "4,500", "미수금발생(카드)");

            $("#testDiv").parent("div").remove();
        });
    });
}

export {runMoneyProblem};
