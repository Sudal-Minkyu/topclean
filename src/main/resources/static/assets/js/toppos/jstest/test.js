import * as moneyProblem from "../module/m_moneyProblem.js";


QUnit.module('moneyProblem 돈계산', function() {
    QUnit.test('접수페이지 상단 누계 계산 1', function(assert) {
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

        const testItem = [
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
                "sumName": "니트 상의"
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
                "sumName": "롱 패딩 상의"
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
                "sumName": "숏 맨투맨 상의"
            }
        ];

        moneyProblem.calculateMainPrice(testItem, []);
        assert.equal($("#totFdQty").html(), "3", "수량 검사");
        assert.equal($("#totFdNormalAmount").html(), "19,000", "기본금액 검사");
        assert.equal($("#totChangeAmount").html(), "11,000", "추가/할인금액 검사");
        assert.equal($("#totFdRequestAmount").html(), "30,000", "합계금액 검사");
        $(".regist__info").parent("div").remove();
    });
});