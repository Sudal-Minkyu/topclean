const dtos = {
    send: {
        requestPaymentMobilePaper: {
            frId: "n",
            frNo: "s",
        },
    },
    receive: {
        requestPaymentMobilePaper: {
            paymentData: {
                franchiseNo: "s", // frCode
                franchiseName: "s", // frName
                businessNO: "s", // frBusinessNo
                repreName: "s", // frRpreName
                franchiseTel: "s", // frTel
                customerName: "s", // bcName
                customerTel: "s", // bcHp
                requestDt: "s", // frYyyymmdd
                normalAmount: "n", // frNormalAmount
                changeAmount: "n", // frDiscountAmount
                totalAmount: "n", // frTotalAmount
                paymentAmount: "n", // frPayAmount
                preUncollectAmount: "n", // 고객 전일미수금
                curUncollectAmount: "n", // 고객 당일미수금
                uncollectPayAmount: "n", // 미수금 상환액
                totalUncollectAmount: "n", // 총미수금
            },

            items: { // 디테일 품목들의 배열
                tagno: "s",
                color: "s", // 남색
                itemname: "s", // 롱 오리털 코트
                specialyn: "s", // Y
                price: "n",
				estimateDt: "s", // fdEstimateDt
            },
            
            creditData: { // 결제한 내역들의 배열, 적립금이나 현금은 금액과 타입만 오면 됨
                type: "sr", // fpType 기준 01: cash, 02: card, 03: save 
                cardNo: "s", // fpCatCardNo
                cardName: "s", // fpCatIssuername
                approvalTime: "s", // fpCatApprovaltime
                approvalNo: "s", // fpCatApprovalno
                month: "n", // fpMonth
                payAmount: "nr", // 결제금액
            }
        },
    }
};

const urls = {
    getReceiptData: "/api/mobile/unAuth/requestPaymentMobilePaper",
}

const comms = {
    getReceiptData(target) {
        CommonUI.ajax(urls.getReceiptData, "GET", target, function (res) {
            const data = res.sendData;
            console.log(data); // 영수증 데이터 확인해서 뿌리기
            setField(data);
        });
    }
};

const trigs = {
    basic() {
    },
}

const wares = {
    colorName: {
        "00": "없음", "01": "흰색", "02": "검정", "03": "회색", "04": "빨강", "05": "주황",
        "06": "노랑", "07": "초록", "08": "파랑", "09": "남색", "10": "보라", "11": "핑크",
    },
}

$(function() {
    onPageLoad();
});

function onPageLoad() {
    trigs.basic();

    chkParams();    
}

function chkParams() {
    const url = new URL(window.location.href);
    wares.params = url.searchParams;

    if(wares.params.has("id")) {
        const frNo = wares.params.get("id"); // 어떤종류의 id인지 파악해서 변수명 바꿀것

        const target = {
            frNo: frNo,
            frId: "",
        }

        comms.getReceiptData(target);
    }
}

function setField(data) {
    $("#requestDt").html(data.paymentData.requestDt);
    
    $("#franchiseName").html(data.paymentData.franchiseName);
    $("#businessNO").html(CommonUI.formatBusinessNo(data.paymentData.businessNO));
    $("#repreName").html(data.paymentData.repreName);
    $("#franchiseTel").html(CommonUI.formatTel(data.paymentData.franchiseTel));

    $("#normalAmount").html(data.paymentData.normalAmount.toLocaleString());
    $("#changeAmount").html(data.paymentData.changeAmount.toLocaleString());
    $("#totalAmount").html(data.paymentData.totalAmount.toLocaleString());

    $("#preUncollectAmount").html(data.paymentData.preUncollectAmount.toLocaleString());
    $("#curUncollectAmount").html(data.paymentData.curUncollectAmount.toLocaleString());
    $("#uncollectPayAmount").html(data.paymentData.uncollectPayAmount.toLocaleString());
    $("#totalUncollectAmount").html(data.paymentData.totalUncollectAmount.toLocaleString());

    $("#qty").html(data.items.length.toLocaleString());
    $("#paymentAmount").html(data.paymentData.paymentAmount.toLocaleString());

    if (data.items.length) {
        $("#estimateDt").html(data.items[0].estimateDt);
    }

    for (const {color, itemname, price, tagno} of data.items) {
        $("#itemList").append(`
            <tr>
                <td>${tagno.substr(3, 7)}</td>
                <td>${itemname}</td>
                <td>${wares.colorName[color]}</td>
                <td class="align-right">${price.toLocaleString()}</td>
            </tr>
        `);
    }

    for (const obj of data.creditData) {
        if (obj.type === "02") { // 카드 결제 목록보고 결정
            const approvalTime = `20${obj.approvalTime.substring(0, 2)}-${obj.approvalTime.substring(2, 4)}-${obj.approvalTime.substring(4, 6)} ${obj.approvalTime.substring(6, 8)}:${obj.approvalTime.substring(8, 10)}`;
            $("#receiptList").append(`
                <div class="receipt__sub">
                    <dl>
                        <dt>결제구분</dt>
                        <dd>${obj.cardName}</dd>
                        <dt>승인일시</dt>
                        <dd>${approvalTime}</dd>
                        <dt>할부</dt>
                        <dd>${obj.month ? obj.month + " 개월" : "일시불"}</dd>
                        <dt>결제금액</dt>
                        <dd>${obj.fpRealAmt.toLocaleString()}</dd>
                        <dt>카드번호</dt>
                        <dd>${obj.cardNo}</dd>
                        <dt>승인번호</dt>
                        <dd>${obj.approvalNo}</dd>
                    </dl>
                </div>
            `);
        } else {
            $("#receiptList").append(`
                <div class="receipt__sub">
                    <dl>
                        <dt>결제구분</dt>
                        <dd>${CommonData.name.fpType[obj.type]}</dd>
                        <dt>결제금액</dt>
                        <dd>${obj.fpRealAmt.toLocaleString()}</dd>
                    </dl>
                </div>
            `);
        }
    }
}