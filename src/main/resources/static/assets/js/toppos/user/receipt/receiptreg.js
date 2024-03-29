const dtos = {
    send: {
        영수증발행: { // 사용메뉴에 따라 frId나 frNo 둘중 하나가 식별자로 보내질 수 있음.
            frId: "n",
            frNo: "s",
        }
    },

    receive: {
        영수증발행: {
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
                fpRealAmt: "nr", // 결제금액
            }
        }
    },
}


$(function() {
    /* 헤더의 상단 해당메뉴 큰 버튼의 아이콘 및 색 활성화 */
    $("#menuReceiptreg").addClass("active")
        .children("img").attr("src", "/assets/images/icon__reg--active.svg");

    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids(true);

    setDataIntoGrid(2, "/api/user/tempRequestList");

    /* 하단 패널과 그리드의 너비 버그 수정용 코드 */
    AUIGrid.resize(gridId[0]);
    $("#grid_main").addClass("active");

    /* 가상키보드의 사용 선언 */
    vkey = new VKeyboard();

    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        if(e.dataField === "fdQty") {
            tempItem = e.item;
            $("#hiddenKeypad").val(e.item.fdQty);
            vkey.showKeypad("hiddenKeypad", {callback:changeQty});
        }else{
            console.log(e);
        }
    });

    // 세탁 가격 선택 항목의 변경시 필요작업과 이벤트 반영
    $("#inReceiptPop input[type='radio']").on("change", function(){
        calculateItemPrice();
    });

    // 접수 세부 우측 하단 각종 처리 버튼의 온오프에 따른 없음버튼 온오프
    const $processInput = $("#processCheck input[type='checkbox'], #processCheck input[type='radio']");
    $processInput.on("change", function(e) {
        if($("#waterNone").is(":checked")) {
            $("#waterBtn").removeClass("choice-drop__btn--active");
        }else{
            $("#waterBtn").addClass("choice-drop__btn--active");
        }
        judgeEtcNoneOnOff();
        const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
        if($("#fdRetry").is(":checked") &&
            ($("#processCheck input[type='checkbox']:checked").length > 1 || $isEtcProcessChecked)) {
            $("#fdRetry").prop("checked", false);
        }

        calculateItemPrice();
    });

    /* 수기 할인 적용, 해제시 가격 계산 */
    $("#manualDiscountChk").on("change", function(e) {
        calculateItemPrice();
    });

    // 긴급항목 버튼의 온오프에 따른 없음버튼 온오프
    const $urgentInput = $("#urgentCheck input[type='checkbox']");
    $urgentInput.on("change", function(e) {
        if(!$("#urgentCheck input[type='checkbox']:checked").length) {
            $urgentInput.first().prop("checked", true);
        }else{
            $urgentInput.first().prop("checked", false);
        }
    });

    $("#etcNone, #fdRetry").on("click", function (e) {
        $processInput.prop("checked", false);
        $("#waterNone").prop("checked", true);
        $("#waterBtn").removeClass("choice-drop__btn--active");
        resetPollutionPop();
        resetFdRepairInputs();
        resetFdAddInputs();
        if (e.target.id === "fdRetry") {
            $("#fdRetry").prop("checked", true);
        }
        // 각 내부 항목들의 초기화
        setTimeout(function () {
            calculateItemPrice();
        }, 0);
    });

    $("#urgentNone").on("click", function () {
        $urgentInput.prop("checked", false);
        calculateItemPrice();
    });

    $("#manualDiscountPercent").on("keyup", function () {
        validateManualDiscountPercent();
    });

    // 팝업 닫기
    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');
    })


    CommonUI.ajax("/api/user/itemGroupAndPriceList", "GET", false, function (req) {
        initialData = req.sendData;
        refinePromotionData();

        const rearStartTag = CommonData.formatFrTagNo(initialData.etcData.fdTag, frTagInfo.frTagType);
        if (initialData.etcData.frManualPromotionYn !== "Y") {
            $("#manualDiscountChk").parents("li").hide();
        }

        setAddMenu();
        setBgMenu(false);
        setPreDefinedKeywords();

        $("#fdTag").val(rearStartTag);
        getParamsAndAction();
    }, function () {
        $("#successBtn").on("click", function () {
            mainPage();
        });
    });

    $("#toggleFavorite").on("change", function () {
        setBgMenu($("#toggleFavorite").is(":checked"));
    });

    $("#fdRepair").on("click", function () {
        $("#fdRepairPop").addClass("active");
        enableKeypad();
    });

    $("#fdAdd1").on("click", function () {
        $("#fdAddPop").addClass("active");
        enableKeypad();
    });

    $("#fdPollution").on("click", function () {
        $("#foreLoc").trigger("click");
        $("#fdPollutionPop").addClass("active");
    });

    $("#fdRepairCancel").on("click", function () {
        resetFdRepairInputs();
        calculateItemPrice();
        disableKeypad();
    });

    $("#fdRepairComplete").on("click", function () {
        currentRequest.fdRepairAmt = $("#fdRepairAmt").val().toInt();
        currentRequest.fdRepairRemark = $("#fdRepairRemark").val();
        if(currentRequest.fdRepairAmt || currentRequest.fdRepairRemark.length) {
            $("#fdRepair").prop("checked", true);
        }else{
            $("#fdRepair").prop("checked", false);
        }
        calculateItemPrice();
        disableKeypad();
    });

    $("#fdAddCancel").on("click", function () {
        resetFdAddInputs();
        calculateItemPrice();
        disableKeypad();
    });

    $("#fdAddComplete").on("click", function () {
        currentRequest.fdAdd1Amt = $("#fdAdd1Amt").val().toInt();
        currentRequest.fdAdd1Remark = $("#fdAdd1Remark").val();
        if(currentRequest.fdAdd1Amt || currentRequest.fdAdd1Remark.length) {
            $("#fdAdd1").prop("checked", true);
        }else{
            $("#fdAdd1").prop("checked", false);
        }
        calculateItemPrice();
        disableKeypad();
    });

    $('.choice-drop__btn').on('click', function(e) {
        $(".choice-drop__content--active").removeClass("choice-drop__content--active");
        $(this).next('.choice-drop__content').toggleClass('choice-drop__content--active');
    });

    $('.choice-drop__item').on('click', function(e) {
        $(this).parents('.choice-drop__content').removeClass('choice-drop__content--active');
        if(this.className === "choice-drop__item closDrop"){
            $(this).parents('.choice-drop').children('.choice-drop__btn').removeClass('choice-drop__btn--active');
        }else{
            $(this).parents('.choice-drop').children('.choice-drop__btn').addClass('choice-drop__btn--active');
        }
    });

    $("#foreLoc").on("click", function() {
        $(".foreLoc").show();
        $(".pop__pollution-content").removeClass("back");
        $(".backLoc").hide();
    });

    $("#backLoc").on("click", function() {
        $(".backLoc").show();
        $(".pop__pollution-content").addClass("back");
        $(".foreLoc").hide();
    });

    // 검색 엔터 이벤트
    $("#searchCustomerField").on("keypress", function(e) {
        if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
            onSearchCustomer();
        }
    });

    $("#uncollectMoneyMain").parents("li").on("click", function () {
        if(selectedCustomer && selectedCustomer.bcHp) {
            location.href = "/user/unpaid?bchp=" + selectedCustomer.bcHp;
        } else {
            location.href = "/user/unpaid";
        }
    });

    $("#isPrintCustomersChk, #autoPrintReceipt, #goToDeliverChk, #manualDiscountChk").on("click", function (e) {
        setCookie(e.target.id, e.target.checked, 3);
    });

    /* 고객 초기화 */
    $("#resetCustomer").on("click", function() {
        selectedCustomer = {
            bcId: null,
            uncollectMoney: 0,
            saveMoney: 0,
            bcAddress: "",
            bcRemark: "",
            deliveryS5 : 0,
            deliveryS8 : 0,
        };
        onPutCustomer();
    });

    // 결제팝업 탭
    const $payTabsBtn = $('.pop__pay-tabs-item');
    const $payTabsContent = $('.pop__tabs-content');

    $payTabsBtn.on('click', function() {
        let idx = $(this).index() - 1;
        if (idx) {
            setCookie('payMethodIdx', idx);
            $payTabsBtn.removeClass('active');
            $payTabsBtn.eq(idx).addClass('active');
        }
        $payTabsContent.removeClass('active');
        $payTabsContent.eq(idx - 1).addClass('active');

        setReceiveAmtToTotalAmt();
    });

    const urgentTypeElement = $("input[name='fdUrgentType']");
    urgentTypeElement.on("click", function (e) {
        urgentTypeElement.prop("checked", false);
        $(e.currentTarget).prop("checked", true);
        calculateItemPrice();
    });

    // 상품 사진 촬영 상용구
    $(".camBoiler").on("click", function () {
        const $ffRemark = $("#ffRemark");
        $ffRemark.val($ffRemark.val() + " " + this.innerHTML);
    });

    $("#btnPayment").on("click", function () {
        const paymentTab = $(".pop__pay-tabs-item.active").attr("data-id");
        const changeCash = $("#changeCash").html().toInt();
        if (paymentTab === "tabCard" && $("#receiveCard").html().toInt() >= 50000) {
            const selectedMonthText = $("#installmentMonth option:selected").html();
            alertCheck(`현재 카드 할부는 ${selectedMonthText} 입니다.<br>계속 진행하시겠습니까?`);
            $("#checkDelSuccessBtn").on("click", function () {
                $('#popupId').remove();
                onPaymentStageOne();
            });
        } else if (paymentTab === "tabCash" && changeCash > 0) {
            alertThree(`거스름돈이 ${changeCash.toLocaleString()}원 존재합니다.<br>거스름돈을 적립하시겠습니까?`);
            $("#popFirstBtn").on("click", function () {
                $('#popupId').remove();
                onPaymentStageOne({changeCashToSaveMoneyAmt: changeCash, targetBcId: selectedCustomer.bcId});
            });
            $("#popSecondBtn").on("click", function () {
                $('#popupId').remove();
                onPaymentStageOne();
            });
            $("#popThirdBtn").on("click", function () {
                $('#popupId').remove();
            });
        } else {
            onPaymentStageOne();
        }
    });

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
});

let deletedRowItems = [];

let isCashReceiptPublished = false;

/* 마지막 택번호가 기억된다. 시작 택번호는 initialData.etcData.fdTag 에 */
let lastFdTag;

/* 결제창 영수증 자동인쇄 체크박스 체크 여부를 기억해 두었다가, 해당 동작 수행  */
let autoPrintReceipt = false;

/* 목록의 카메라 버튼을 누를 때 마다 카메라의 존재를 감지해 그 유무를 저장해둠 */
let isCameraExist = false;

/* 1일 때는 임시저장, 2일 때는 접수완료 flag */
let checkNum = "1";

/* 상품 주문을 받을 때 적용되는 가격 정보 데이터를 API로 불러와 미리 저장 */
let priceData;

let tempItem;

/* 선택된 고객이나 세탁물(대분류)의 이용을 위함  */
let selectedCustomer;

/* 선택한 상품의 대분류 3자, 중분류 1자, 최종분류 7자, 가격이 입력될 곳 */
let selectedLaundry = {
    bgCode: "",
    bsCode: "",
    biCode: "",
}

const fsRequestDtl = {
    fdTag: "",
    biItemcode: "",
    fdColor: "00",
    fdPattern: "00",
    fdPriceGrade: "1",
    fdOriginAmt: 0,
    fdNormalAmt: 0,
    fdRepairRemark: "",
    fdRepairAmt: 0,
    fdAdd1Remark: "",
    fdSpecialYn: "N",
    fdUrgentYn: "N",
    fdUrgentType: "0",
    fdUrgentAmt: 0,
    fdAdd1Amt: 0,
    fdAdd2Remark: "",
    fdAdd2Amt: 0,
    fdPressed: 0,
    fdWhitening: 0,
    fdPollution: 0,
    fdPollutionLevel: 0,
    fdStarch: 0,
    fdWaterRepellent: 0,
    fdDiscountGrade: 1,
    fdDiscountAmt: 0,
    fdQty: 1,
    fdRequestAmt: 0,
    fdTotAmt: 0,
    fdRetryYn: "N",
    fdRemark: "",
    frEstimateDate: "",
    photoList: [],
}

// fsRequestDtl 객체를 깊은 복사하기위함.
let currentRequest = JSON.parse(JSON.stringify(fsRequestDtl));

let initialData = [];

/* 가상키보드 사용을 위해 */
let vkey;
let vkeyProp = [];
let vkeyTargetId = ["searchCustomerField", "fdTag", "fdRemark", "fdRepairRemark", "fdAdd1Remark",
                    "ffRemark", "fdTagPassword"];

vkeyProp[0] = {
    title : "동작시 제목설정",
    callback : onSearchCustomer,
}

vkeyProp[1] = {
    title : "택번호 입력",
    defaultKeyboard : 2,
    // 택번호 입력 후 바로 저장되도록 처리
}

vkeyProp[2] = {
    title : "브랜드, 특이사항",
}

vkeyProp[3] = {
    title : "수선 내용 입력",
}

vkeyProp[4] = {
    title : "추가 내용 입력",
}

vkeyProp[5] = {
    title : "검품 특이사항 입력",
}

vkeyProp[6] = {
    title : "비밀번호 입력",
}

/* 그리드 생성과 운영에 관한 중요 변수들. grid라는 이름으로 시작하도록 통일했다. */
let gridId = [];
let gridTargetDiv = [];
let gridData = [];
let gridColumnLayout = [];
let gridProp = [];

/* 그리드를 뿌릴 대상 div의 id들 */
gridTargetDiv = [
    "grid_main", "grid_customerList", "grid_tempSaveList", "grid_payment"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "fdTag",
        headerText: "택번호",
        style: "datafield_tag",
        width: 80,
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
        },
    }, {
        dataField: "productName",
        headerText: "상품명",
        style: "receiptreg-product-name",
        width: 200,
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            CommonUI.toppos.makeProductName(item, initialData.userItemPriceSortData);
            return item.productName;
        },
        renderer : {
            type: "IconRenderer",
            iconPosition: "left",
            iconWidth : 30,
            iconHeight : 30,
            iconFunction(rowIndex, columnIndex, value, item) {
                let icon = "";
                if(item.photoList.length) {
                    icon = "/assets/images/icon__picture.svg";
                }else{
                    icon = "/assets/images/icon__camera.svg";
                }
                return icon;
            }
            ,
            onClick(event) {
                onPopTakePicture(event);
            },
        }
    }, {
        dataField: "sumProcess",
        headerText: "처리내역",
        style: "grid_textalign_left",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.toppos.processName(item);
        }
    }, {
        dataField: "fdNormalAmt",
        headerText: "기본금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return value.toLocaleString();
        },
    }, {
        dataField: "fdRepairAmt",
        headerText: "수선금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return value.toLocaleString();
        },
    }, {
        dataField: "",
        headerText: "추가금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            const addAmount = item.fdPressed + item.fdWhitening + item.fdWaterRepellent + item.fdStarch
                + item.fdPollution + item.fdAdd1Amt + item.fdUrgentAmt;
            return addAmount.toLocaleString();
        },
    }, {
        dataField: "fdDiscountAmt",
        headerText: "할인금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return (value + item.fdPromotionDiscountAmt).toLocaleString();
        },
    }, {
        dataField: "fdQty",
        headerText: "수량",
        style: "grid_textalign_right",
        width: 50,
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdRequestAmt",
        headerText: "접수금액",
        style: "grid_textalign_right",
        width: 90,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return value.toLocaleString();
        }
    }, {
        dataField: "fdColor",
        headerText: "색",
        width: 30,
        style: "colorColumn",
        renderer : {
            type : "TemplateRenderer",
        },
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return `<div class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}"></div>`;
        }
    }, {
        dataField: "fdRemark",
        headerText: "특이사항",
        style: "grid_textalign_left",
    }, {
        dataField: "frEstimateDate",
        headerText: "출고예정일",
        width: 90,
        dataType: "date",
        formatString: "yyyy-mm-dd",
    }, {
        dataField: "",
        headerText: "수정",
        width: 50,
        renderer : {
            type: "TemplateRenderer",
        },
        labelFunction(rowIndex, columnIndex, value, headerText, item ) {
            return `
                <button class="c-button c-button--solid  c-button--supersmall" 
                    onclick="onModifyOrder(${rowIndex})">수정</button>
            `;
        },
        style: "btn-modify",
    }
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "주문받은 품목을 선택하여 주세요",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: true,
    enableColumnResize : false,
    showEditedCellMarker: false,
    showRowAllCheckBox: true,
    showRowCheckColumn: true,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : false,
    enableSorting : false,
    rowHeight : 48,
    headerHeight : 48,
};

gridColumnLayout[1] = [
    {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "전화번호",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.formatTel(value);
        }
    }, {
        dataField: "bcAddress",
        headerText: "주소",
        style: "grid_textalign_left",
    },
];

gridProp[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: false,
    enableColumnResize : false,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : false,
    width : 830,
    height : 480,
    rowHeight : 48,
    headerHeight : 48,
};

gridColumnLayout[2] = [
    {
        dataField: "frInsertDate",
        headerText: "접수시간",
    }, {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "전화번호",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.formatTel(value);
        }
    }, {
        dataField: "",
        headerText: "삭제",
        renderer : {
            type: "ButtonRenderer",
            labelText: "삭제",
            onClick: onRemoveTempSave,
        }
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "임시저장 내역이 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: true,
    enableColumnResize : false,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : true,
    rowHeight : 48,
    headerHeight : 48,
};

gridColumnLayout[3] = [
    {
        dataField: "fpType",
        headerText: "결제수단",
        labelFunction(rowIndex, columnIndex, value, headerText, item) {
            return item.fpCatIssuername ? item.fpCatIssuername : CommonData.name.fpType[value];
        },
    }, {
        dataField: "fpRealAmt",
        headerText: "결제금액",
        style: "grid_textalign_right",
        dataType: "numeric",
        autoThousandSeparator: "true",
    },
];

gridProp[3] = {
    editable : false,
    selectionMode : "singleRow",
    showAutoNoDataMessage: false,
    enableColumnResize : false,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : false,
    height : 140,
    rowHeight : 32,
    headerHeight : 40,
};


// 1. 임시저장 삭제여부 묻기
function onRemoveTempSave(e){
    alertCheck("해당 임시저장을 삭제하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        ajaxRemoveTempSave(e.item.frNo);
    });

}

// 2. 삭제로식 실행
function checkYesOrNo(booleanValue) {
    $('#popupId').remove();
    if (booleanValue === false) {
        return false;
    } else {
        const frNo = AUIGrid.getSelectedRows(gridId[2])[0].frNo;
        ajaxRemoveTempSave(frNo);
    }
}

/* 실제 임시저장 삭제 동작 */
function ajaxRemoveTempSave(frNo, setAfterDelete = false) {
    const params = {
        frNo: frNo
    };

    CommonUI.ajax("/api/user/tempRequestDetailDelete", "PARAM", params, function (res) {
        // 성공하면 임시저장 마스터테이블 조회
        setDataIntoGrid(2, "/api/user/tempRequestList");
        alertSuccess("임시저장이 삭제되었습니다.");
        if(setAfterDelete) {
            getCustomerDetail();
        }
    });
}

/**
 * 대상 고객의 적립금을 적립한다.
 * @param data : object - 적립금을 적립할 대상 - bcId와 적립금액 - controlMoney
 */
function ajaxUpdateSaveMoney(data) {
    CommonUI.ajax("/api/user/customerSaveMoneyControl", "PARAM", data, function() {
        alertSuccess(`거스름돈 ${data.controlMoney.toLocaleString()}원을<br>적립금으로 전환 하였습니다.`);
        const $saveMoney = $("#saveMoney");
        $saveMoney.html(($saveMoney.html().toInt() + data.controlMoney).toLocaleString());
        const $saveMoneyMain = $("#saveMoneyMain");
        $saveMoneyMain.html(($saveMoneyMain.html().toInt() + data.controlMoney).toLocaleString());
    }, function () {
        alertCancel(`처리실패경고!<br>거스름돈 ${data.controlMoney.toLocaleString()}원을<br>적립금으로 전환 실패 하였습니다.<br>`
            + `${data.controlMoney.toLocaleString()}원을 거슬러 주세요.`);
    }, function () {
        alertCancel(`통신장애경고!<br>거스름돈 ${data.controlMoney.toLocaleString()}원을<br>적립금으로 전환 실패 하였습니다.<br>`
            + `${data.controlMoney.toLocaleString()}원을 거슬러 주세요.`);
    });
}

function onShowVKeyboard(num) {
    if(num === 0) {
        vkeyProp[0].title = $("#searchCustomerType option:selected").html() + " (검색)";
    }
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids(type = false) {
    if(type) {
        for (const i in gridColumnLayout) {
            gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
        }
    }else{
        gridId[0] = AUIGrid.create(gridTargetDiv[0], gridColumnLayout[0], gridProp[0]);
    }
}

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid(numOfGrid, url) {
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}

function onSearchCustomer() {
    const $searchCustomerField = $("#searchCustomerField");
    if($searchCustomerField.val() === ""){
        alertCaution("검색어를 입력해주세요.",1);
        return false;
    }
    const params = {searchType : $("#searchCustomerType").val(),
        searchString : $searchCustomerField.val()};

    CommonUI.ajax("/api/user/customerInfo", "GET", params, function (req) {
        const items = req.sendData.gridListData;
        $("#searchCustomerType").val(0);
        $("#searchCustomerField").val("");
        if(items.length === 1) {
            selectedCustomer = items[0];
            checkCustomer();
            delete initialData.etcData["frNo"];
        }else if(items.length > 1) {
            AUIGrid.setGridData(gridId[1], items);
            $("#customerListPop").addClass("active");
        }else{
            alertCheck("일치하는 고객 정보가 없습니다.<br>신규고객으로 등록 하시겠습니까?");
            let additionalCondition = params.searchString;
            if (additionalCondition.numString().length) {
                additionalCondition = "?bchp=" + additionalCondition.numString();
            } else {
                additionalCondition = "?bcname=" + additionalCondition;
            }
            $("#checkDelSuccessBtn").on("click", function () {
                location.href="/user/customerreg" + additionalCondition;
            });
        }
    });
}

function onSelectCustomer() {
    selectedCustomer = AUIGrid.getSelectedRows(gridId[1])[0];
    if(selectedCustomer) {
        checkCustomer();
        delete initialData.etcData["frNo"];
        $("#customerListPop").removeClass("active");
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function onReadTempSave() {
    $("#tempSaveListPop").addClass("active");
    AUIGrid.resize(gridId[2]);
}

function onSelectTempSave() {
    const selectedSave = AUIGrid.getSelectedRows(gridId[2])[0];
    if(selectedSave) {
        const frNo = AUIGrid.getSelectedRows(gridId[2])[0].frNo;
        loadTempSave(frNo);
    } else {
        // 오류 출력
        alertCaution("불러올 내역을 선택해 주세요", 1);
    }

}

function loadTempSave(frNo) {
    CommonUI.ajax("/api/user/tempRequestDetailList", "GET", {frNo: frNo}, function (req) {
        initialData.etcData.frNo = frNo;
        selectedCustomer = req.sendData.gridListData[0];
        checkCustomer();
        AUIGrid.setGridData(gridId[0], req.sendData.requestDetailList);
        $("#tempSaveListPop").removeClass("active");
        calculateMainPrice(AUIGrid.getGridData(gridId[0]));
        checkNum = "1";
        sortTag();
    }, function () {
        $("#successBtn").on("click", function () {
            mainPage();
        });
    });
}

function checkCustomer() {
    if(selectedCustomer.tempSaveFrNo) {
        tempSaveExistWarning();
    } else {
        getCustomerDetail();
    }
}

function getCustomerDetail() {
    if (!selectedCustomer.bcId) {
        return;
    }

    /* deliveryS5,S8이 아직 오지 않았다는 것은 세탁접수 고객검색을 통해서 들어왔기 때문에 고객출고품목 두가지를 추가로 요청해야 한다. */
    if (selectedCustomer.deliveryS5 || selectedCustomer.deliveryS5 === 0) {
        onPutCustomer();
    } else {
        CommonUI.ajax("/api/user/deliveryInfo", "GET", {bcId: selectedCustomer.bcId}, function (res) {
            const data = res.sendData;
            selectedCustomer.deliveryS5 = data.deliveryS5;
            selectedCustomer.deliveryS8 = data.deliveryS8;
            onPutCustomer();
        }, function () {
            $("#successBtn").on("click", function () {
                mainPage();
            });
        });
    }
}

function onPutCustomer() {
    let bcGradeName = "";
    $(".client__badge").removeClass("active");
    switch (selectedCustomer.bcGrade) {
        case "01" :
            $(".client__badge:nth-child(1)").addClass("active");
            bcGradeName = "일반";
            break;
        case "02" :
            $(".client__badge:nth-child(2)").addClass("active");
            bcGradeName = "VIP";
            break;
        case "03" :
            $(".client__badge:nth-child(3)").addClass("active");
            bcGradeName = "VVIP";
            break;
    }
    $("#bcGrade").html(bcGradeName);
    $("#bcName").html(selectedCustomer.bcName ? selectedCustomer.bcName + "님" : "");
    if (selectedCustomer.bcValuation) {
        $("#bcValuation").attr("class",
            "propensity__star propensity__star--" + selectedCustomer.bcValuation).css('display', 'block');
    } else {
        $("#bcValuation").css('display', 'none');
    }
    $("#bcAddress").html(selectedCustomer.bcAddress);
    $("#bcHp").html(CommonUI.formatTel(selectedCustomer.bcHp));
    $("#uncollectMoneyMain").html(selectedCustomer.uncollectMoney.toLocaleString());
    $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
    $("#bcRemark").html(selectedCustomer.bcRemark);
    if (selectedCustomer.bcLastRequestDt) {
        $("#bcLastRequestDt").html(
            selectedCustomer.bcLastRequestDt.substr(0, 4) + "-"
            + selectedCustomer.bcLastRequestDt.substr(4, 2) + "-"
            + selectedCustomer.bcLastRequestDt.substr(6, 2)
        );
    } else if (selectedCustomer.bcId) {
        $("#bcLastRequestDt").html("없음");
    } else {
        $("#bcLastRequestDt").html("");
    }

    $("#class02, #class03").parents("li").css("display", "none");
    $("#class" + selectedCustomer.bcGrade).parents("li").css("display", "block");
    AUIGrid.clearGridData(gridId[0]);
    deletedRowItems = [];
    calculateMainPrice(AUIGrid.getGridData(gridId[0]));

    selectedCustomer.deliverySize = selectedCustomer.deliveryS5 + selectedCustomer.deliveryS8;
    if (selectedCustomer.deliverySize) {
        $("#numOfDeliverablePop").attr("style", "visibility: show;");
        $("#numOfDeliverableBcName").html(selectedCustomer.bcName);
        $("#numOfDeliverable").html(selectedCustomer.deliverySize);
    } else {
        $("#numOfDeliverablePop").attr("style", "visibility: hidden;");
    }

    setTopMenuHref();
}

/* 하단의 세탁물 종류 버튼 클릭시 해당 세탁물 대분류 코드를 가져와 팝업을 띄운다. */
function onPopReceiptReg(btnElement) {
    selectedLaundry.bgCode = btnElement.value;
    formMidItemcodeOnPop();

    // 처음 표시 중분류 기본상태 N, 만일 중분류에 N이 없는 예외상황시 수정해줘야함.
    setBiItemList("N");
    $("input[name='bsItemGroupcodeS']").first().prop("checked", true);
    const {manualDiscountPercent} = getCookie();
    if (manualDiscountPercent) {
        $("#manualDiscountPercent").val(manualDiscountPercent);
    }

    calculateItemPrice();
    $('#productPop').addClass('active');
    $("#biItemList").scrollTop(0);
}


function setBiItemList(bsCode, resetBiItemcode = true) {
    if (resetBiItemcode) {
        onSelectBiItem("", 0);
    }
    selectedLaundry.bsCode = bsCode;

    const $biItemList = $("#biItemList");
    $biItemList.html("");

    initialData.userItemPriceSortData.forEach(el => {
       if(el.biItemcode.substr(0,4) === selectedLaundry.bgCode + bsCode) {
            $biItemList.append(`
                <li>
                    <div class="choice choice--material">
                        <input type="radio" name="material" id="${el.biItemcode}" 
                                value="${el.price}" onclick="onSelectBiItem('${el.biItemcode}', ${el.price})" />
                        <label for="${el.biItemcode}">
                            <span class="choice__name">${el.biName}</span>
                            <span class="choice__cost">${parseInt(el.price).toLocaleString()}</span><span>원</span>
                        </label>
                    </div>
                </li>
            `);
       }
    });
}

function onSelectBiItem(biCode, price) {
    currentRequest.biItemcode = biCode;
    currentRequest.fdOriginAmt = price;
    calculateItemPrice();
}


/* 웹 카메라 촬영 스트림이 담긴다. */
let cameraStream;

/* 웹 카메라와 촬영 작업중 */
async function onPopTakePicture(event) {
    currentRequest = event.item;

    try {
        isCameraExist = true;
        // const cameraList = document.getElementById("cameraList"); 복수 카메라를 사용할 경우 해제하여 작업
        cameraStream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: {
                width: {ideal: 4096},
                height: {ideal: 2160}
            },
        });

        const screen = document.getElementById("cameraScreen");
        screen.srcObject = cameraStream;
    }catch (e) {
        if(e instanceof DOMException) {
            alertCaution("연결된 카메라가 존재하지 않습니다", 1);
        }
        CommonUI.toppos.underTaker(e, "receiptreg : 카메라");
        isCameraExist = false;
    }


    currentRequest.photoList.forEach(picJson => {
        picJson.fullImage = picJson.ffPath + picJson.ffFilename;
        picJson.thumbnailImage = picJson.ffPath + "s_" + picJson.ffFilename;
        putTakenPictureOnTheRightSide(picJson);
    });
    $("#cameraPop").addClass("active");
}

function onTakePicture() {
    if(isCameraExist && cameraStream.active) {
        try {
            const $ffRemark = $("#ffRemark");
            if (!$ffRemark.val().length) {
                alertCaution("특이사항을 입력해 주세요", 1);
                return false;
            }

            const video = document.getElementById("cameraScreen");
            const canvas = document.getElementById('cameraCanvas');
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            const context = canvas.getContext('2d');
            context.drawImage(video, 0, 0, canvas.width, canvas.height);

            const takenPic = canvas.toDataURL();

            const blob = b64toBlob(takenPic);

            if (!blob.size) {
                return;
            }

            const formData = new FormData();
            formData.append("source", blob);
            formData.append("ffRemark", $ffRemark.val());

            CommonUI.ajax("/api/user/takePicture", "POST", formData, function (req) {
                let picJson = {
                    ffPath: req.sendData.ffPath,
                    ffFilename: req.sendData.ffFilename,
                    ffRemark: $ffRemark.val(),
                }
                $ffRemark.val("");
                picJson.fullImage = req.sendData.ffPath + req.sendData.ffFilename;
                picJson.thumbnailImage = req.sendData.ffPath + "s_" + req.sendData.ffFilename;
                putTakenPictureOnTheRightSide(picJson);
            });
        } catch (e) {
            CommonUI.toppos.underTaker(e, "receiptreg : 사진촬영");
        }
    }
}

function onRemovePicture(btnElement) {
    btnElement.parents(".photo__picture").remove();
}

function onCloseTakePicture() {
    if(isCameraExist) {
        try {
            cameraStream.getTracks().forEach(function (track) {
                track.stop();
            });
        }catch (e) {
            CommonUI.toppos.underTaker(e, "receiptreg : 카메라가 끊어졌을 가능성");
        }
    }

    const $photoList = $(".photo__picture");
    let photos = [];
    for(let i = 0; i < $photoList.length; i++) {
        photos.push({
            ffPath: $photoList.eq(i).attr("data-ffPath"),
            ffFilename: $photoList.eq(i).attr("data-ffFilename"),
            ffRemark: $photoList.eq(i).attr("data-ffRemark"),
        });
    }
    currentRequest.photoList = photos;

    const copyObj = CommonUI.cloneObj(currentRequest);
    copyObj["dummy" + parseInt(Math.random() * 100000).toString()] = "dummy";

    AUIGrid.updateRowsById(gridId[0], copyObj);

    $("#ffRemark").val("");
    cameraStream = 0;
    $("#cameraPop").removeClass("active");
    $photoList.remove();
    currentRequest = JSON.parse(JSON.stringify(fsRequestDtl));

    AUIGrid.refresh(gridId[0]);
}

function putTakenPictureOnTheRightSide(picJson) {
    const aPictureSet = `
            <div class="photo__picture" data-ffPath="${picJson.ffPath}"
                data-ffFilename="${picJson.ffFilename}" data-ffRemark="${picJson.ffRemark}">
                <div class="photo__picture-head">
                    <h5 class="photo__picture-title">${picJson.ffRemark}</h5>
                    <button class="photo__picture-delete" data-image="${picJson.fullImage}" onclick="onRemovePicture($(this))">삭제</button>
                </div>
                <div class="photo__picture-item">
                    <a href="${picJson.fullImage}" class="photo__img" data-lightbox="images" data-title="${picJson.ffRemark}">
                        <img src="${picJson.thumbnailImage}" alt="" />
                    </a>
                </div>
            </div>
        `;
    $("#photoList").append(aPictureSet);
}

function b64toBlob(dataURI) {

    const byteString = atob(dataURI.split(',')[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: 'image/jpeg' });
}

function dataURLtoFile(dataurl, filename) {

    let arr = dataurl.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);

    while(n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, {type:mime});
}

function toggleBottom() {
    let element = document.getElementById("registBottom");
    element.classList.toggle("active");

    let grid = document.getElementById("grid_main");
    grid.classList.toggle("active");

    AUIGrid.resize(gridId[0]);
}

function onAddOrder() {
    if(selectedCustomer === undefined || !selectedCustomer.bcId) {
        alertCaution("먼저 고객을 선택해 주세요", 1);
        return false;
    }
    if(!currentRequest.biItemcode.length) {
        alertCaution("소재를 선택해 주세요", 1);
        return false;
    }
    if(currentRequest.biItemcode.substring(0, 3) === "D03" && !currentRequest.fdAgreeType) {
        alertThree("운동화는 고객의 동의와 서명이 필요합니다.<br>어떻게 하시겠습니까?", "스크린 동의", "서면 동의", "취소");
        if(initialData.etcData.frMultiscreenYn !== "Y") {
            $("#popFirstBtn").hide();
        }
        $("#popFirstBtn").on("click", function () {
            askConfirmShoes();
            $('#popupId').remove();
        });
        $("#popSecondBtn").on("click", function () {
            askSignPaper();
        });
        $("#popThirdBtn").on("click", function () {
            $('#popupId').remove();
        });
        return false;
    }
    currentRequest.productName = "";

    putItemIntoGrid();
}

function putItemIntoGrid() {

    currentRequest.fdColor = $("input[name='fdColor']:checked").val();
    currentRequest.fdPattern = $("input[name='fdPattern']:checked").val();
    currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();
    currentRequest.fdRemark = $("#fdRemark").val();
    currentRequest.frEstimateDate = initialData.etcData.frEstimateDate.numString();
    currentRequest.fdSpecialYn = "N";
    currentRequest.fdUrgentType = $("input[name='fdUrgentType']:checked").val() ?
        $("input[name='fdUrgentType']:checked").val() : "";
    currentRequest.fdUrgentYn = currentRequest.fdUrgentType !== "" ? "Y" : "N";

    const pollutionLoc = $("input[name='pollutionLoc']");
    for(const element of pollutionLoc) {
        if ($(element).is(":checked")) {
            currentRequest[element.id] = "Y";
        } else {
            currentRequest[element.id] = "N";
        }
    }

    decidePollutionType();

    if(currentRequest._$uid) {
        const copyObj = CommonUI.cloneObj(currentRequest);
        copyObj["dummy" + parseInt(Math.random() * 100000).toString()] = "dummy";
        AUIGrid.updateRowsById(gridId[0], copyObj);
    }else{
        AUIGrid.addRow(gridId[0], currentRequest, "last");
        sortTag();
    }

    applyPromotionDiscount();
    calculateMainPrice(AUIGrid.getGridData(gridId[0]));
    onCloseAddOrder();
}

function onCloseAddOrder() {
    currentRequest = JSON.parse(JSON.stringify(fsRequestDtl));
    $("input[name='bsItemGroupcodeS']").first().prop("checked", true);
    $("input[name='fdColor']").first().prop("checked", true);
    $("input[name='fdPattern']").first().prop("checked", true);
    $("input[name='fdPriceGrade']").first().prop("checked", true);
    $("input[name='fdDiscountGrade']").first().prop("checked", true);

    $("input[name='material']").first().prop("checked", true);
    $(".choice input[type='checkbox']").prop("checked", false);
    $("input[name='cleanDirt']").first().prop("checked", true);
    $("input[name='waterProcess']").first().prop("checked", true);
    $(".choice-drop__btn.etcProcess").removeClass('choice-drop__btn--active');
    $(".choice-drop__content.choice-drop__content--right").removeClass('choice-drop__content--active');
    $(".keypad_remark").val("");
    $(".keypad_field").val(0);
    $("#foreLoc").prop("checked", true);
    $("input[name='pollutionLoc']").prop("checked", false);
    $("input[name='etcNone']").first().prop("checked", true);
    $("input[name='urgentNone']").first().prop("checked", true);
    $("#fdRemark").val("");

    $("#addProductPopChild").parents('.pop').removeClass('active');
}

function confirmPollutionPop() {

    const isLocChecked = $("input[name='pollutionLoc']:checked").length;
    const isSizeChecked = !$("#pollution00").is(":checked");

    if(isLocChecked && !isSizeChecked) {
        alertCaution("오염위치가 선택되어 있습니다.<br>오염크기도 선택해 주세요.", 1);
        return false;
    }
    if(!isLocChecked && isSizeChecked) {
        alertCaution("오염크기가 선택되어 있습니다.<br>오염위치도 선택해 주세요.", 1);
        return false;
    }

    if (($("#pollution00").is(":checked")
            && $("#fdPollution").is(":checked"))
        || (!$("#pollution00").is(":checked")
            && !$("#fdPollution").is(":checked"))) {
        $("#fdPollution").trigger("click");
    }
    $("#fdPollutionPop").removeClass("active");

    calculateItemPrice();
}

function resetPollutionPop() {
    $("#pollution00").prop("checked", true);
    $("input[name='pollutionLoc']").prop("checked", false);
    if($("#fdPollution").is(":checked")) {
        $("#fdPollution").trigger("click");
    }
    $("#fdPollutionPop").removeClass("active");

    calculateItemPrice();
}

function onModifyOrder(rowIndex) {

    currentRequest = AUIGrid.getItemByRowIndex(gridId[0], rowIndex);
    selectedLaundry.bgCode = currentRequest.biItemcode.substr(0, 3);
    formMidItemcodeOnPop();

    setBiItemList(currentRequest.biItemcode.substr(3, 1), false);

    selectedLaundry.bgCode = currentRequest.biItemcode.substr(0, 3);
    selectedLaundry.bsCode = currentRequest.biItemcode.substr(3, 1);
    $("input[name='bsItemGroupcodeS']:input[value='" + selectedLaundry.bsCode + "']").prop("checked", true);
    $("#" + currentRequest.biItemcode).prop("checked", true);
    $(".choice-color__input[value='" + currentRequest.fdColor + "']").prop("checked", true);
    $("input[name='fdPattern']:input[value='" + currentRequest.fdPattern +"']").prop("checked", true);
    $("input[name='fdPriceGrade']:input[value='" + currentRequest.fdPriceGrade +"']").prop("checked", true);
    $("input[name='fdDiscountGrade']:input[value='" + currentRequest.fdDiscountGrade +"']").first().prop("checked", true);

    const {manualDiscountPercent} = getCookie();
    $("#manualDiscountPercent").val(currentRequest.fdPromotionType === "H1"
        ? currentRequest.fdPromotionDiscountRate : manualDiscountPercent ? manualDiscountPercent : "0" );
    /* 할인이 본사직영의 수기행사할인인 경우 해당 체크박스를 체크 */
    if (currentRequest.fdPromotionType === "H1") {
        $("#manualDiscountChk").prop("checked", true);
    }

    if(currentRequest.fdPressed) {
        $("#fdPress").prop("checked", true);
    }
    if(currentRequest.fdRetryYn === "Y") {
        $("#fdRetry").prop("checked", true);
    }

    if(currentRequest.fdRepairRemark.length || currentRequest.fdRepairAmt) {
        $("#fdRepair").prop("checked", true);
        $("#fdRepairAmt").val(currentRequest.fdRepairAmt);
        $("#fdRepairRemark").val(currentRequest.fdRepairRemark);
    }

    if(currentRequest.fdAdd1Remark.length || currentRequest.fdAdd1Amt) {
        $("#fdAdd1").prop("checked", true);
        $("#fdAdd1Amt").val(currentRequest.fdAdd1Amt);
        $("#fdAdd1Remark").val(currentRequest.fdAdd1Remark);
    }

    if (currentRequest.fdUrgentYn === "Y") {
        $("#fdUrgent" + currentRequest.fdUrgentType).prop("checked", true);
    }

    if(currentRequest.fdWhitening) {
        $("#fdWhitening").prop("checked", true);
    }

    $("input[name='cleanDirt']:input[value='" + currentRequest.fdPollutionLevel +"']").prop("checked", true);
    if($("#pollution00").is(":checked")) {
        $("#fdPollution").prop("checked", false);
    }else{
        $("#fdPollution").prop("checked", true);
    }

    const pollutionLocKeys = [
        "fdPollutionLocFcn", "fdPollutionLocFcs", "fdPollutionLocFcb",
        "fdPollutionLocFlh", "fdPollutionLocFrh", "fdPollutionLocFlf", "fdPollutionLocFrf",
        "fdPollutionLocBcn", "fdPollutionLocBcs", "fdPollutionLocBcb",
        "fdPollutionLocBrh", "fdPollutionLocBlh", "fdPollutionLocBrf", "fdPollutionLocBlf",
    ];

    pollutionLocKeys.forEach(key => {
        if(currentRequest[key] === "Y") {
            $("#" + key).prop("checked", true);
        }
    });

    if(currentRequest.fdWaterRepellent) {
        $("#fdWaterRepellent").prop("checked", true);
    }
    if(currentRequest.fdStarch) {
        $("#fdStarch").prop("checked", true);
    }
    if($("#waterNone").is(":checked")) {
        $("#waterBtn").removeClass("choice-drop__btn--active");
    }else{
        $("#waterBtn").addClass("choice-drop__btn--active");
    }

    if(currentRequest.fdRemark.length) {
        $("#fdRemark").val(currentRequest.fdRemark);
    }

    if($("#processCheck input:checked").length > 2 || $("#processCheck .choice-drop__btn--active").length) {
        $("#etcNone").prop("checked", false);
    }

    if($("#urgentCheck input:checked").length > 1) {
        $("#urgentNone").prop("checked", false);
    }

    calculateItemPrice();
    $('#productPop').addClass('active');
    $("#biItemList").scrollTop(0);
}

function onRemoveOrder() {
    if(AUIGrid.getCheckedRowItems(gridId[0]).length){
        AUIGrid.removeCheckedRows(gridId[0]);
    }else{
        AUIGrid.removeRow(gridId[0], "selectedIndex");
    }
    const removedItems = AUIGrid.getRemovedItems(gridId[0]);
    for (const item of removedItems) {
        deletedRowItems.push(item);
    }
    AUIGrid.removeSoftRows(gridId[0]);
    sortTag();
    applyPromotionDiscount();
    calculateMainPrice(AUIGrid.getGridData(gridId[0]));
}

function setNextTag(tag) {
    let nextNo = tag.substring(frTagInfo.frTagType, frTagInfo.frTagType + 1) + "-"
        + (parseInt(tag.substring(frTagInfo.frTagType + 1, 7)) + 1).toString().padStart(6 - frTagInfo.frTagType, '0');

    const endOfTagBundle = frTagInfo.frTagType === 2 ? "9999" : "999";
    if(tag.substring(frTagInfo.frTagType + 1, 7) === endOfTagBundle) {
        let midNum = (parseInt(tag.substring(frTagInfo.frTagType, frTagInfo.frTagType + 1)) + 1).toString();
        midNum = midNum === "10" ? "0" : midNum;
        nextNo = midNum + "-" + (frTagInfo.frTagType === 2 ? "0001" : "001");
    }

    return nextNo;
}

function sortTag() {
    let fdTag = initialData.etcData.fdTag;
    const gridItems = AUIGrid.getGridData(gridId[0]);

    for (let i = 0; i < gridItems.length; i++) {
        gridItems[i].fdTag = fdTag;
        lastFdTag = fdTag;
        fdTag = frTagInfo.frTagNo + setNextTag(fdTag).numString();
    }

    AUIGrid.updateRowsById(gridId[0], gridItems);
}

/* 임시저장을 두단계로 분리하기 위해 데이터를 임시로 전역변수화 */
let saveData = {};

function onTempSave() {
    alertCheck("임시저장 하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        checkNum = "1";
        onSave(true);
        $('#popupId').remove();
    });
}

/* 전역변수 cehckNum이 1일 경우에는 임시저장, 2일 경우에는 접수완료처리 */
function onSave(turnOnSuccessMsg = false, callback = function () {/* intentional */}, tossData = {/* intentional */}, callbackProp = {}) {

    // 추가된 행 아이템들(배열)
    let addedRowItems = AUIGrid.getAddedRowItems(gridId[0]);

    // 수정된 행 아이템들(배열)
    let updatedRowItems = AUIGrid.getEditedRowItems(gridId[0]);

    // 삭제된 행 아이템들(배열)
    deletedRowItems = AUIGrid.getRemovedItems(gridId[0]);

    if(selectedCustomer === undefined) {
        alertCaution("먼저 고객을 선택해 주세요", 1);
        return false;
    }

    let etc = {
        checkNum: checkNum,
        bcId: selectedCustomer.bcId,
        frNo: initialData.etcData.frNo,
        frNormalAmount: $("#totFdNormalAmount").html().toInt(),
        frDiscountAmount: $("#totChangeAmount").html().toInt(),
        frTotalAmount: $("#totFdRequestAmount").html().toInt(),
        frQty: $("#totFdQty").html().toInt(),
        frLastTagno: frTagInfo.frTagNo + setNextTag(lastFdTag).numString(),
    }

    const data = {
        "add" : addedRowItems,
        "update" : updatedRowItems,
        "delete" : deletedRowItems,
        "etc" : etc,
    };
    saveData = data;

    onSaveAjax(turnOnSuccessMsg, callback, tossData, callbackProp);
}

function onSaveAjax(turnOnSuccessMsg, callback, tossData, callbackProp = {}) {
    CommonUI.ajax("/api/user/requestSave", "MAPPER", saveData, function (res) {
        deletedRowItems = [];
        AUIGrid.resetUpdatedItems(gridId[0]);
        AUIGrid.clearGridData(gridId[2]);
        if(checkNum === "1") {
            setDataIntoGrid(2, "/api/user/tempRequestList");
            if(turnOnSuccessMsg) {
                alertSuccess("임시저장이 되었습니다");
            }
            loadTempSave(res.sendData.frNo);
        }
        initialData.etcData.frNo = res.sendData.frNo;

        if(checkNum === "2") {
            /* 접수완료 후 접수화면을 나가지 않고 계속 접수할 경우를 위해 시작 택번호를 다시 세팅*/
            const rearStartTag = setNextTag(lastFdTag);
            $("#fdTag").val(rearStartTag);
            initialData.etcData.fdTag = frTagInfo.frTagNo + rearStartTag.numString();

            initialData.etcData.uncollectMoney = res.sendData.uncollectMoney;
            initialData.etcData.saveMoney = res.sendData.saveMoney;
            selectedCustomer.uncollectMoney = res.sendData.uncollectMoney;
            selectedCustomer.saveMoney = res.sendData.saveMoney;
        }
        $("#uncollectMoney").html(selectedCustomer.uncollectMoney.toLocaleString());
        $("#saveMoney").html(selectedCustomer.saveMoney.toLocaleString());
        $("#uncollectMoneyMain").html(selectedCustomer.uncollectMoney.toLocaleString());
        $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
        callback(tossData, callbackProp);
    }, function () {
        $("#successBtn").on("click", function () {
            mainPage();
        });
    });
}

function enableKeypad() {
    const $keypadBtn = $(".add-cost .keypad_btn");
    const $keypadBackspace = $(".add-cost .keypad_btn_backspace");
    const $keypadBoilerplate = $(".add-cost .add-cost__example-btn");

    $keypadBtn.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost__keypad").find(".keypad_field");
        $keypad_field.val(parseInt($keypad_field.val().numString() + this.value).toLocaleString());
    });

    $keypadBackspace.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost__keypad").find(".keypad_field");
        const currentValue = $keypad_field.val().numString();
        if(currentValue.length > 1) {
            $keypad_field.val(parseInt(currentValue.substr(0,
                currentValue.numString().length - 1)).toLocaleString());
        }else{
            $keypad_field.val("0");
        }
    });

    $keypadBoilerplate.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost").find(".keypad_remark");
        $keypad_field.val($keypad_field.val() + this.innerHTML);
    });
}

function disableKeypad() {
    const $keypadBtn = $(".add-cost .keypad_btn");
    const $keypadBackspace = $(".add-cost .keypad_btn_backspace");
    const $keypadBoilerplate = $(".add-cost .add-cost__example-btn");

    for(const element of $keypadBtn) {
        removeEventsFromElement(element);
    }

    for(const element of $keypadBackspace) {
        removeEventsFromElement(element);
    }

    for(const element of $keypadBoilerplate) {
        removeEventsFromElement(element);
    }
}

function removeEventsFromElement(element) {
    const elementClone = element.cloneNode(true);
    element.parentNode.replaceChild(elementClone, element);
}

function onRepeatRequest() {
    let items = AUIGrid.getSelectedItems(gridId[0]);
    if(items.length) {
        delete items[0].item["_$uid"];
        delete items[0].item["fdId"];
        AUIGrid.addRow(gridId[0], items[0].item, "last");
        sortTag();
        applyPromotionDiscount();
        calculateMainPrice(AUIGrid.getGridData(gridId[0]));
    }
}

/* 수량변경 */
function changeQty() {
    tempItem.fdQty = $("#hiddenKeypad").val().toInt();
    if (["02", "03"].includes(tempItem.fdPromotionType)) {
        tempItem.fdPromotionDiscountAmt = 0;
    }
    tempItem.fdRequestAmt = (tempItem.fdNormalAmt + tempItem.fdPressed + tempItem.fdWhitening
        + tempItem.fdWaterRepellent + tempItem.fdStarch + tempItem.fdPollution + tempItem.fdRepairAmt
        + tempItem.fdAdd1Amt + tempItem.fdUrgentAmt - tempItem.fdDiscountAmt - tempItem.fdPromotionDiscountAmt) * tempItem.fdQty;
    tempItem.fdTotAmt = tempItem.fdRequestAmt + tempItem.fdAdd2Amt;
    AUIGrid.updateRowsById(gridId[0], tempItem);
    applyPromotionDiscount();
    calculateMainPrice(AUIGrid.getGridData(gridId[0]));
}

/* 접수완료시 호출 API */
function onApply() {
    if($("#totFdQty").html() === "0") {
        alertCaution("접수완료할 품목이 없습니다.", 1);
        return false;
    }
    checkNum = "1";
    onSave();

    const totRequestAmt = $("#totFdRequestAmount").html().toInt();
    $("#payNormalAmt").html($("#totFdNormalAmount").html().positiveNumberInput());
    $("#payChangeAmt").html($("#totChangeAmount").html().numberInput());

    const applySaveMoney = selectedCustomer.saveMoney > totRequestAmt ? totRequestAmt : selectedCustomer.saveMoney;
    $("#applySaveMoney").html(applySaveMoney.toLocaleString());

    $("#applyUncollectAmt").html("0");
    $("#payRequestAmt").html((totRequestAmt).toLocaleString());
    $("#totalAmt").html((totRequestAmt - applySaveMoney).toLocaleString());
    setReceiveAmtToTotalAmt();
    calculatePaymentStage(getPaidAmt());

    $("#btnPayLater").parents("li").show();
    $("#btnPayment").parents("li").show();
    $("#btnClosePayment").parents("li").show();
    $("#btnPrintReceipt").parents("li").hide();

    /* 쿠기에 저장된 결제팝업의 각종 인풋 활성화, 선택 상태를 가져와서 결제팝업을 띄우기 전에 적용 */
    const {isPrintCustomersChk, autoPrintReceipt, goToDeliverChk, payMethodIdx} = getCookie();
    $("#isPrintCustomersChk").prop("checked", isPrintCustomersChk);
    $("#autoPrintReceipt").prop("checked", autoPrintReceipt);
    $("#goToDeliverChk").prop("checked", goToDeliverChk);
    $('.pop__pay-tabs-item').eq(parseInt(payMethodIdx)).trigger("click");

    $("#paymentPop").addClass("active");
}


function getPaidAmt() {
    const paidData =  AUIGrid.getGridData(gridId[3]);
    let paidAmt = 0;
    paidData.forEach(el => {
       paidAmt += el.fpAmt;
    });
    return paidAmt;
}

/* 수기할인 적용동작시 키패드 동작 정의*/
function onClickManualDiscountPercentPad() {
    const props = {
        callback: validateManualDiscountPercent,
        maxlength: 3,
        clrfirst: true,
    }
    vkey.showKeypad("manualDiscountPercent", props);
}

/* 시작택번호 키패드 입력 */
function onShowTagKeypad() {
    const props = {
        midprocess: "none",
        callback: onSaveFdTag,
        maxlength: 7 - frTagInfo.frTagType,
        clrfirst: true,
    }
    vkey.showKeypad("fdTag", props);
}

/* 키패드 작동용 */
let keypadNum;
function onKeypad(num) {
    const targetId = ["applySaveMoney", "receiveCash", "receiveCard"];
    keypadNum = num;
    $("#hiddenKeypad").val($("#" + targetId[keypadNum]).html());
    const props = {
        callback: onKeypadConfirm,
        clrfirst: true,
        maxlength: 10,
    };
    vkey.showKeypad("hiddenKeypad", props);
}

function onKeypadConfirm() {
    const targetId = ["applySaveMoney", "receiveCash", "receiveCard"];
    $("#" + targetId[keypadNum]).html($("#hiddenKeypad").val());

    calculatePaymentStage(getPaidAmt());
    payAmtLimitation();
}

/* 결제 1단계 */
function onPaymentStageOne(prop = {}) {

    try {
        /* 미수 발생 금액 */
        const uncollectAmt = $("#totalAmt").html().toInt() + $("#applySaveMoney").html().toInt()
            - $("#applyUncollectAmt").html().toInt();
        /* 미수 발생 금액이 0원이라는 것은 해당 상품은 0원 결제건이라는 뜻이므로 결제없이 완료처리를 할 수 있도록 기능을 연결한다. */
        if (!uncollectAmt) {
            completePaymentProcessForFreeReceipt();
            return;
        }

        const applyUncollectAmt = $("#applyUncollectAmt").html().toInt();
        autoPrintReceipt = $("#autoPrintReceipt").is(":checked");

        let items = [];
        const orderData = AUIGrid.getGridData(gridId[0]);
        orderData.forEach(el => {
            items.push({
                tagno: el.fdTag,
                color: el.fdColor,
                itemname: el.productName,
                specialyn: el.fdSpecialYn,
                price: el.fdRequestAmt,
            });
        });

        let paymentData =
            {
                franchiseNo: initialData.etcData.frCode,
                franchiseName: initialData.etcData.frName,
                businessNO: initialData.etcData.frBusinessNo,
                repreName: initialData.etcData.frRpreName,
                franchiseTel: initialData.etcData.frTelNo,
                customerName: selectedCustomer.bcName,
                customerTel: CommonUI.formatTel(selectedCustomer.bcHp),
                requestDt: new Date().format("yyyy-MM-dd HH:mm"),
                totalAmount: $("#payRequestAmt").html().toInt(),
                normalAmount: $("#payNormalAmt").html().toInt(),
                changeAmount: $("#payChangeAmt").html().toInt(),
                estimateDt: initialData.etcData.frEstimateDate,
                uncollectPayAmount: applyUncollectAmt,
                totalUncollectAmount: initialData.etcData.uncollectMoney - applyUncollectAmt + initialData.etcData.uncollectMoney,
                month: $("#installmentMonth").val().toInt(),
                items: items,
            };

        const paymentTab = $(".pop__pay-tabs-item.active").attr("data-id");
        if (paymentTab === "tabCash") {
            const receiveCash = $("#receiveCash").html().toInt();
            if (receiveCash) {
                if (applyUncollectAmt && $("#totalAmt").html().toInt() - receiveCash > 0) {
                    alertCaution("미수전액완납시, <br>한번에 전액을 상환하셔야 합니다.", 1);
                    return false;
                }
                paymentData.type = "cash";
                paymentData.paymentAmount = receiveCash;
            }
        } else if (paymentTab === "tabCard") {
            const receiveCard = $("#receiveCard").html().toInt();
            if (receiveCard) {
                if (applyUncollectAmt && $("#totalAmt").html().toInt() - receiveCard > 0) {
                    alertCaution("미수전액완납시, <br>한번에 전액을 상환하셔야 합니다.", 1);
                    return false;
                }
                paymentData.type = "card";
                paymentData.paymentAmount = receiveCard;
            }
        }

        const applySaveMoney = $("#applySaveMoney").html().toInt();
        if(!paymentData.paymentAmount && !applySaveMoney) {
            alertCaution("0원을 결제시도 할 수 없습니다.<br>미수금이 남은채로 창을 닫기를 원하시면<br>"
                + "닫기 버튼을 눌러주세요.", 1);
            return false;
        }

        // type: card or cash
        // franchiseNo : 가맹점코드 3자리 문자열
        // totalAmount : 총 결제금액
        // month : 할부 (0-일시불, 2-2개월)

        if (paymentData.type ==="card") {
            validateCurrentCATMachine(function () {
                $('#payStatus').show();
                try {
                    CAT.CatCredit(paymentData, function (res) {
                        $('#payStatus').hide();
                        let resjson = JSON.parse(res);
                        // 결제 성공일경우 Print
                        if (resjson.STATUS === "SUCCESS") {
                            resjson.month = paymentData.month;
                            onPaymentStageTwo(resjson, prop);
                            alertSuccess("카드 결제가 성공하였습니다.<br>단말기에서 카드를 제거해 주세요.");
                        }
                        // 결제 실패의 경우
                        if (resjson.STATUS === "FAILURE") {
                            $('#payStatus').hide();
                            if (resjson.ERRORDATA === "erroecode:404, error:error") {
                                alertCancel("카드결제 단말기 연결이 감지되지 않습니다.<br>연결을 확인해 주세요.");
                            } else if (resjson.ERRORDATA === "erroecode:0, error:timeout") {
                                alertCancel("유효 결제 시간이 지났습니다.<br>다시 결제창 버튼을 이용하여<br>원하시는 기능을 선택 해주세요.");
                            } else if(resjson.ERRORMESSAGE === "단말기에서종료키누름 /                  /                 ") {
                                alertCancel("단말기 종료키를 통해 결제가 중지되었습니다.");
                            } else if (resjson.ERRORMESSAGE === " /  / ") {
                                alertCancel("단말기가 사용중입니다.<br>확인 후 다시 시도해 주세요.");
                            } else {
                                alertCancel(resjson.ERRORMESSAGE);
                            }
                        }
                    });
                } catch (e) {
                    CommonUI.toppos.underTaker(e, "receiptreg : 카드 단말기 결제");
                }
            });
        } else {
            onPaymentStageTwo({}, prop);
        }

    } catch (e) {
        CommonUI.toppos.underTaker(e, "receiptreg : 결제 1단계");
        return false;
    }
}

/* 결재할 때 */
function onPaymentStageTwo(creditData = {}, prop = {}) {
    if (checkNum === "1") {
        checkNum = "2";
        onSave(false, onPaymentStageThree, creditData, prop);
    } else {
        onPaymentStageThree(creditData, prop);
    }
}

function onPaymentStageThree(creditData, prop = {}) {
    try {
        let data = {
            payment: [],
            etc: {
                bcId: selectedCustomer.bcId,
                frNo: initialData.etcData.frNo,
            }
        }
        const applyUncollectAmt = $("#applyUncollectAmt").html().toInt();

        const paymentTab = $(".pop__pay-tabs-item.active").attr("data-id");
        const receiveCash = $("#receiveCash").html().toInt() - $("#changeCash").html().toInt();
        const receiveCard = $("#receiveCard").html().toInt();
        if (paymentTab === "tabCash" && receiveCash) {
            const paymentCash = {
                fpType: "01",
                fpRealAmt: receiveCash,
                fpAmt: parseInt(receiveCash - applyUncollectAmt),
                fpCollectAmt: applyUncollectAmt,
            }
            data.payment.push(paymentCash);
        } else if (paymentTab === "tabCard" && receiveCard) {
            const paymentCard = {
                fpType: "02",
                fpMonth: creditData.month,
                fpRealAmt: receiveCard,
                fpAmt: receiveCard - applyUncollectAmt,
                fpCollectAmt: applyUncollectAmt,
                fpCatApprovalno: creditData.APPROVALNO,
                fpCatApprovaltime: creditData.APPROVALTIME,
                fpCatCardno: creditData.CARDNO,
                fpCatIssuercode: creditData.ISSUERCODE,
                fpCatIssuername: creditData.ISSUERNAME,
                fpCatMuechantnumber: creditData.MERCHANTNUMBER,
                fpCatMessage1: creditData.MESSAGE1,
                fpCatMessage2: creditData.MESSAGE2,
                fpCatNotice1: creditData.NOTICE1,
                fpCatTotamount: creditData.TOTAMOUNT,
                fpCatVatamount: creditData.VATAMOUNT,
                fpCatTelegramflagt: creditData.TELEGRAMFLAG
            }
            data.payment.push(paymentCard);
        }
        const applySaveMoney = $("#applySaveMoney").html().toInt();
        if (applySaveMoney) {
            const paymentSaved = {
                fpType: "03",
                fpRealAmt: applySaveMoney,
                fpAmt: applySaveMoney,
                fpCollectAmt: 0,
            }
            data.payment.push(paymentSaved);
        }

        /* 결제 정보를 서버에 전송하여 저장 */
        CommonUI.ajax("/api/user/requestPayment", "MAPPER", data, function (req) {
            console.log(req);
            /* 결제 정보에 따라 계산과 데이터의 재배치 */
            AUIGrid.addRow(gridId[3], req.sendData.paymentEtcDtos, "last");
            initialData.etcData.uncollectMoney = req.sendData.uncollectMoney;
            initialData.etcData.saveMoney = req.sendData.saveMoney;
            selectedCustomer.uncollectMoney = req.sendData.uncollectMoney;
            selectedCustomer.saveMoney = req.sendData.saveMoney;
            $("#uncollectMoney").html(selectedCustomer.uncollectMoney.toLocaleString());
            $("#saveMoney").html(selectedCustomer.saveMoney.toLocaleString());
            $("#uncollectMoneyMain").html(selectedCustomer.uncollectMoney.toLocaleString());
            $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
            $("#applySaveMoney").html("0");
            $("#applyUncollectAmt").html("0");
            $("#receiveCash").html("0");
            $("#changeCash").html("0");
            $("#receiveCard").html("0");

            calculatePaymentStage(getPaidAmt());

            $("#btnPrintReceipt").parents("li").show();
            $("#btnClosePayment").parents("li").hide();

            if($("#totalAmt").html() === "0") {
                $("#btnPayLater").parents("li").hide();
                $("#btnPayment").parents("li").hide();
                $("#btnClosePayment").parents("li").show();
                if (autoPrintReceipt) printReceipt();
            }

            for (const {fpType} of data.payment) {
                if (fpType === "01" && !isCashReceiptPublished) {
                    $("#publishCashReceipt").show();
                }
            }

            if (prop.changeCashToSaveMoneyAmt) {
                const addSaveMoneyData = {
                    controlMoney: prop.changeCashToSaveMoneyAmt,
                    bcId: prop.targetBcId,
                }

                ajaxUpdateSaveMoney(addSaveMoneyData);
            }
        });
    } catch (e) {
        CommonUI.toppos.underTaker(e, "receiptreg : 결제 3단계");
        return false;
    }
}

function onPayUncollectMoney() {
    $("#applyUncollectAmt").html($("#uncollectMoney").html());
    alertCaution("미수전액완납시, 적립금은 사용하지<br> 못하며 한 가지 결제수단 으로 <br>전액 결제하셔야 합니다.", 1);
    payAmtLimitation();
}

function cancelPayUncollectMoney() {
    $("#applyUncollectAmt").html(0);
    payAmtLimitation();
    setReceiveAmtToTotalAmt();
}

function onClosePayment() {
    /* 결제를 하되 완료되지 않으면 사라져 사라져 있는 닫기버튼을 누를 때 미수금이 남아있다는 것은 아직 결제를 하지 않았다는 것을 의미*/
    const hasUncollectAmtCash = !!($("#totalAmt").html().toInt() + $("#applySaveMoney").html().toInt()
        - $("#applyUncollectAmt").html().toInt());
    closePaymentPop(hasUncollectAmtCash);
}

function onPaymentLater() {
    const uncollectAmtCash = $("#totalAmt").html().toInt() + $("#applySaveMoney").html().toInt()
        - $("#applyUncollectAmt").html().toInt();
    const paidAmt = getPaidAmt();
    autoPrintReceipt = $("#autoPrintReceipt").is(":checked");
    if(uncollectAmtCash) {
        alertCheck(uncollectAmtCash.toLocaleString() + "원의 미수금(후불)이 발생합니다<br> 이대로 닫으시겠습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            $('#popupId').remove();
            if (paidAmt) {
                closePaymentPop();
            } else {
                checkNum = "2";
                const frNo = initialData.etcData.frNo;
                onSave(false,function () {
                    closePaymentPop();
                    if (autoPrintReceipt) {
                        printReceipt(frNo);
                    }
                }, false);
            }
        });
    } else if (!paidAmt) { // 결제금액이 남지 않았는데, 미수발생금액도 없다면 0원짜리 상품(재세탁이거나)을 결제중이므로 여기서 처리
        completePaymentProcessForFreeReceipt();
    } else {
        closePaymentPop();
        if (autoPrintReceipt) {
            printReceipt();
        }
    }
}

/* 결제금액이 0원인 접수건의 최종처리 */
function completePaymentProcessForFreeReceipt() {
    autoPrintReceipt = $("#autoPrintReceipt").is(":checked");
    alertCheck("결제금액이 0원인 접수건의 처리를 완료 하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        if (autoPrintReceipt) {
            printReceipt();
        }
        checkNum = "2";
        onSave(false, function () {
            $("#btnPayLater").parents("li").hide();
            $("#btnPayment").parents("li").hide();
            $("#btnClosePayment").parents("li").show();
            $("#btnPrintReceipt").parents("li").show();
        }, false);
        $('#popupId').remove();
    });
}

function closePaymentPop(isSimpleClose = false) {
    $("#paymentPop").removeClass("active");

    if(isSimpleClose) return false;

    AUIGrid.clearGridData(gridId[0]);
    AUIGrid.clearGridData(gridId[3]);
    calculateMainPrice(AUIGrid.getGridData(gridId[0]));

    /* 접수가 정상적으로 완료되고 창이 닫힐 때 고객에게 문자가 간다. */
    const url = "/api/user/requestReceiptMessage";
    const sendData = {
        frNo: initialData.etcData.frNo,
        locationHost: location.host,
    }
    CommonUI.ajax(url, "PARAM", sendData, function (res) {
        /* 창 닫힐 때 고객출고 자동이동 */
        if ($("#goToDeliverChk").is(":checked")) {
            goToDeliver();
        }
    });

    delete initialData.etcData["frNo"];
}

function payAmtLimitation() {
    const currentSaveMoney = $("#saveMoney").html().toInt();
    const $receiveCash = $("#receiveCash");
    let receivedCashAmt = $receiveCash.html().toInt();
    const $receiveCard = $("#receiveCard");
    let receivedCardAmt = $receiveCard.html().toInt();
    const payRequestAmtA = $("#payRequestAmt").html().toInt();
    const $applySaveMoney = $("#applySaveMoney");
    let applySaveMoneyB = $applySaveMoney.html().toInt();
    const applyUncollectAmtC = $("#applyUncollectAmt").html().toInt();
    const paidData =  AUIGrid.getGridData(gridId[3]);
    let paidAmtD = 0;
    paidData.forEach(el => {
        paidAmtD += el.fpAmt;
    });

    if(applyUncollectAmtC) {
        const totAmt = payRequestAmtA + applyUncollectAmtC - paidAmtD;
        applySaveMoneyB = 0;
        receivedCardAmt = totAmt;
        if(receivedCashAmt < totAmt) {
            receivedCashAmt = totAmt;
        }
    }

    /* 사용한 적립금이 가지고 있는 적립금보다 큰 경우 */
    if(applySaveMoneyB - currentSaveMoney > 0) {
        applySaveMoneyB = currentSaveMoney;
    }

    // 최종결제금액보다 받은 카드금액까지의 합계가 큰 경우
    if(payRequestAmtA - applySaveMoneyB + applyUncollectAmtC - paidAmtD - receivedCardAmt < 0) {
        receivedCardAmt = payRequestAmtA - applySaveMoneyB + applyUncollectAmtC - paidAmtD;
        receivedCardAmt = receivedCardAmt < 0 ? 0 : receivedCardAmt;
    }

    // 최종 결제금액보다 사용한 적립금이 많은 경우
    if(payRequestAmtA - applySaveMoneyB + applyUncollectAmtC - paidAmtD < 0) {
        applySaveMoneyB = payRequestAmtA + applyUncollectAmtC - paidAmtD;
        applySaveMoneyB = applySaveMoneyB < 0 ? 0 : applySaveMoneyB;
        receivedCashAmt = 0;
        receivedCardAmt = 0;
    }

    // 최종 결제금액과 사용한 적립금이 같을 경우
    if(payRequestAmtA - applySaveMoneyB + applyUncollectAmtC - paidAmtD === 0) {
        applySaveMoneyB = payRequestAmtA + applyUncollectAmtC - paidAmtD;
        receivedCashAmt = 0;
        receivedCardAmt = 0;
    }

    $applySaveMoney.html(applySaveMoneyB.toLocaleString());
    $receiveCash.html(receivedCashAmt.toLocaleString());
    $receiveCard.html(receivedCardAmt.toLocaleString());

    calculatePaymentStage(getPaidAmt());
}

function setReceiveAmtToTotalAmt() {
    const totalAmt = $("#totalAmt").html().toInt();
    $("#receiveCash").html(totalAmt.toLocaleString());
    $("#receiveCard").html(totalAmt.toLocaleString());
    calculatePaymentStage(getPaidAmt());
}

function onSaveFdTag() {
    const tag = $("#fdTag").val().numString();
    const backTagLength = 7 - frTagInfo.frTagType;

    if(tag.length !== backTagLength) {
        alertCaution(`택번호는 ${backTagLength}자리의 숫자여야 합니다.`, 1);
        return false;
    }

    let prohibitedStartNum = "";
    for(let i = 0; i < backTagLength - 2; i++) {
        prohibitedStartNum += "0";
    }

    if (!parseInt(tag.substring(1))) {
        alertCaution(`택번호는 X-${prohibitedStartNum}1 부터 시작해 주세요.`, 1);
        return;
    }

    const beforeTagNo = CommonData.formatFrTagNo(initialData.etcData.fdTag, frTagInfo.frTagType);
    const changedTagNo = CommonData.formatFrTagNo(frTagInfo.frTagNo + $("#fdTag").val(), frTagInfo.frTagType);
    alertCheck(`시작 택번호는 ${beforeTagNo} 입니다.<br>` + `${changedTagNo}(으)로 변경 하시겠습니까?`);
    $("#checkDelSuccessBtn").on("click", function () {
        $("#fdTagChange").addClass("active");
        $("#fdTag").val(changedTagNo);
        $('#popupId').remove();
    });
}

function onConfirmFdTag() {
    const tag = frTagInfo.frTagNo + $("#fdTag").val().numString();
    const data = {
        password: $("#fdTagPassword").val(),
        frLastTagno: tag,
    }
    const url = "/api/user/franchiseCheck";
    CommonUI.ajax(url, "GET", data, function (res) {
        initialData.etcData.fdTag = tag;
        onCloseFdTag();
        alertSuccess("시작 택번호가 변경되었습니다.");
        sortTag();
        $("#fdTagPassword").val("");
    }, function () {
        $("#successBtn").on("click", function () {
            mainPage();
        });
    });
}

function onCloseFdTag() {
    $("#fdTagChange").removeClass("active");
}

function setAddMenu() {
    if (initialData.etcData.frUrgentDayYn === "N") $("#fdUrgent1").parents("#urgentCheck li").remove();
}

function setBgMenu(isFavorite) {
    $("#bgItemList").html("");
    initialData.userItemGroupSortData.forEach(bgItem => {
        if(bgItem.bgFavoriteYn === "Y" || !isFavorite) {
            $("#bgItemList").append(`
                <li>
                    <button type="button" class="regist__category-btn bgItemGroupcode" value="${bgItem.bgItemGroupcode}">
                    <img src="/assets/images/category/${bgItem.bgIconFilename}" alt="${bgItem.bgName}" />
                    <span>${bgItem.bgName}</span></button>
                </li>
            `);
        }
    });
    $(".bgItemGroupcode").on("click", function () {
        onPopReceiptReg(this);
    });
}

function tempSaveExistWarning() {
    alertThree(`${selectedCustomer.tempSaveBcName} 고객님의<br> 임시저장 내역이 존재합니다.<br>어떻게 하시겠습니까?`,
        "이어서 접수", "삭제후 접수", "취소");

    $("#popFirstBtn").on("click", function() {
        loadTempSave(selectedCustomer.tempSaveFrNo);
        $('#popupId').remove();
    });

    $("#popSecondBtn").on("click", function() {
        ajaxRemoveTempSave(selectedCustomer.tempSaveFrNo, true);
        $('#popupId').remove();
    });

    $("#popThirdBtn").on("click", function() {
        $('#popupId').remove();
    });

}

/* 서명을 요청할 때 고객측의 모니터에 주의사항 고지 후 서명하도록 뜬다. */
function requestSign() {
    try {
        const protocol = location.protocol;
        const hostName = location.hostname;
        const port = location.port;

        cAPI.approvalCall(protocol + '//' + hostName + ':' + port + '/user/consent');
    }catch (e) {
        CommonUI.toppos.underTaker(e, "receiptreg : 사인 요청단계");
        return;
    }

    const maskHeight = $(document).height();
    const maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#windowMask').css({'width':maskWidth,'height':maskHeight}).show();
    $('#mask').show();
}

/* 운동화일 경우 승인창을 먼저 띄운다. */
function askConfirmShoes() {
    try {
        const protocol = location.protocol;
        const hostName = location.hostname;
        const port = location.port;

        cAPI.approvalCall(protocol + '//' + hostName + ':' + port + '/user/consent');
    } catch (e) {
        CommonUI.toppos.underTaker(e, "신발접수 사인 요청");
        initialData.etcData.frMultiscreenYn = "N";
        setTimeout(function () {
            noScreenWarning();
        }, 0);
        return false;
    }

    const maskHeight = $(document).height();
    const maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#windowMask').css({'width':maskWidth,'height':maskHeight}).show();
    $('#mask').show();
}

function noScreenWarning() {
    alertCheck("고객용 스크린이 감지되지 않습니다.<br>서면으로 동의를 받고 진행하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        askSignPaper();
    });
}

function resultFunction(msg) {
    $("#windowMask").hide();
    $("#mask").hide();
    if(msg === "cancel") {
        alertCaution("고객께서 서명을 취소하셨습니다.", 1);
        return false;
    }

    currentRequest.fdSignImage = msg;
    currentRequest.fdAgreeType = "1";
    putItemIntoGrid();
    alertSuccess("고객께서 서명을 완료하셨습니다.<br>접수된 항목을 리스트에 추가합니다.");
}

/* 추가요금과 수선항목의 상용구 버튼 나열 */
function setPreDefinedKeywords() {
    const $addCostBoilerList = $("#addCostBoilerList");
    const $repairBoilerList = $("#repairBoilerList");

    for(const [i, obj] of initialData.addAmountData.entries()) {
        $addCostBoilerList.append(`
            <li class="add-cost__example-item">
                <button class="add-cost__example-btn">${obj.baName}</button>
            </li>
        `);
        if(i === 11) break;
    }

    for(const [i, obj] of initialData.repairListData.entries()) {
        $repairBoilerList.append(`
            <li class="add-cost__example-item">
                <button class="add-cost__example-btn">${obj.baName}</button>
            </li>
        `);
        if(i === 11) break;
    }
}

/* 브라우저의 get 파라미터들을 가져오고 그에 따른 작업을 반영하기 위해 */
function getParamsAndAction() {
    const url = new URL(window.location.href);
    const params = url.searchParams;

    if(params.has("bchp")) {
        const bcHp = params.get("bchp");
        $("#searchCustomerType").val("2");
        $("#searchCustomerField").val(bcHp);
        onSearchCustomer();
    }
}

function resetFdRepairInputs() {
    currentRequest.fdRepairAmt = 0;
    currentRequest.fdRepairRemark = "";
    $("#fdRepair").prop("checked", false);
    judgeEtcNoneOnOff();
    $("#fdRepairAmt").val(0);
    $("#fdRepairRemark").val("");
}

function resetFdAddInputs() {
    currentRequest.fdAdd1Amt = 0;
    currentRequest.fdAdd1Remark = "";
    $("#fdAdd1").prop("checked", false);
    judgeEtcNoneOnOff();
    $("#fdAdd1Amt").val(0);
    $("#fdAdd1Remark").val("");
}

function setTopMenuHref() {
    if(selectedCustomer.bcHp) {
        $("#menuReceiptreg").attr("href", `/user/receiptreg?bchp=${selectedCustomer.bcHp}`);
        $("#menuDelivery").attr("href", `/user/delivery?bchp=${selectedCustomer.bcHp}`);
        $("#menuIntegrate").attr("href", `/user/integrate?bchp=${selectedCustomer.bcHp}`);
    } else {
        $("#menuReceiptreg").attr("href", `/user/receiptreg`);
        $("#menuDelivery").attr("href", `/user/delivery`);
        $("#menuIntegrate").attr("href", `/user/integrate`);
    }
}

function goToDeliver() {

    if (selectedCustomer.bcHp) {
        location.href = "/user/delivery?bchp=" + selectedCustomer.bcHp;
    }
}

function printReceipt(frNo = initialData.etcData.frNo) {
    if ($("#isPrintCustomersChk").is(":checked")) {
        CommonUI.toppos.printReceipt(frNo, "", true, true, "N");
    } else {
        CommonUI.toppos.printReceipt(frNo, "", false, true, "N");
    }
}

function publishCashReceipt() {
    const paidData =  AUIGrid.getGridData(gridId[3]);
    let paidCash = 0;
    for (const {fpRealAmt, fpType} of paidData) {
        if(fpType === "01") {
            paidCash += fpRealAmt;
        }
    }

    if (paidCash) {
        const paymentData = {
            paymentAmount: paidCash,
            franchiseNo: initialData.etcData.frCode,
            frNo: initialData.etcData.frNo,
            fcInType: "01",
        }
        paymentData.fcRealAmt = paymentData.paymentAmount;
        alertThree("현금 영수증을 발행 하시겠습니까?", "개인용", "사업자용", "아니오");

        $("#popFirstBtn").on("click", function () {
            preparePublishCashReceipt(paymentData, "1");
        });

        $("#popSecondBtn").on("click", function () {
            preparePublishCashReceipt(paymentData, "2");
        });

        $("#popThirdBtn").on("click", function () {
            $('#popupId').remove();
        });
    }
}

/* userItemGroupSListData를 기반으로 상품 접수창에 진입할 때, 중간상품코드에 정해진 명칭을 넣고 선택버튼의 표시 유무를 결정한다. */
function formMidItemcodeOnPop() {
    let bsType = ["N", "L", "S"];

    initialData.userItemGroupSListData.forEach(el => {
        for(let i = 0; i < bsType.length; i++) {
            if(el.bgItemGroupcode === selectedLaundry.bgCode && el.bsItemGroupcodeS === bsType[i]) {
                $("#size" + bsType[i]).css("display", "block");
                $("#size" + bsType[i] +" label").html(el.bsName);
                delete bsType[i];
                break;
            }
        }
    });

    bsType.forEach(type => {
        $("#size" + type).css("display", "none");
    });
}

/* 고객용 스크린이 없고, 운동화일 경우(22.05.10 시점에서는 운동화만) 서류 사인 요청 */
function askSignPaper() {
    alertSuccess("접수의뢰를 리스트에 추가합니다.<br>고객으로부터 서류를 통해 동의를 받아주세요.");
    $("#successBtn").on("click", function () {
        currentRequest.fdAgreeType = "2";
        currentRequest.fdSignImage = "";
        putItemIntoGrid();
        $('#popupId').remove();
    });
}

/* 결제데이터와 발행할 현금영수증 종류에 맞추어 현금영수증 발행 */
function preparePublishCashReceipt(paymentData, fcType) {
    $('#popupId').remove();
    $('#payStatus').show();
    paymentData.fcType = fcType;
    CommonUI.toppos.publishCashRceipt(paymentData, function () {
        isCashReceiptPublished = true;
        $("#publishCashReceipt").hide();
        $('.pop__pay-tabs-item').eq(1).trigger("click");
    });
}

/* 체크된 오염지점을 근거로 fdPollutionType을 판단한다. */
function decidePollutionType() {
    let forePollutionChecked = false;
    let backPollutionChecked = false;

    if(currentRequest.fdPollutionLocFcn === "Y" || currentRequest.fdPollutionLocFcs === "Y" ||
        currentRequest.fdPollutionLocFcb === "Y" ||
        currentRequest.fdPollutionLocFrh === "Y" || currentRequest.fdPollutionLocFlh === "Y" ||
        currentRequest.fdPollutionLocFrf === "Y" || currentRequest.fdPollutionLocFlf === "Y") {
        forePollutionChecked = true;
    }

    if(currentRequest.fdPollutionLocBcn === "Y" || currentRequest.fdPollutionLocBcs === "Y" ||
        currentRequest.fdPollutionLocBcb === "Y" ||
        currentRequest.fdPollutionLocBrh === "Y" || currentRequest.fdPollutionLocBlh === "Y" ||
        currentRequest.fdPollutionLocBrf === "Y" || currentRequest.fdPollutionLocBlf === "Y") {
        backPollutionChecked = true;
    }

    currentRequest.fdPollutionBack = backPollutionChecked ? 1 : 0;
    if(forePollutionChecked && backPollutionChecked) {
        currentRequest.fdPollutionType = 2;
    } else if (forePollutionChecked || backPollutionChecked) {
        currentRequest.fdPollutionType = 1;
    } else {
        currentRequest.fdPollutionType = 0;
    }
}

/* 현재 연결된 CAT 단말기가 본사에 등록된 단말기인지를 검증하고 일치하지 않을 경우 카드결제를 막는다. */
function validateCurrentCATMachine(onSuccess) {

    CommonUI.toppos.getTID(function (res) {
        if(res.STATUS === "SUCCESS") {
            const currentTID = res.CARDNO;
            if (initialData.etcData.frCardTid === currentTID) {
                return onSuccess();
            } else {
                alertCancel(`카드 단말기의 고유 TID가 다릅니다.<br>현재 단말기 번호는 ${currentTID} 입니다.<br>본사에 전화 문의를 해 주세요.`);
            }
        } else if (res.STATUS === "FAILURE") {
            if (res.ERRORDATA === "erroecode:404, error:error") {
                alertCancel("카드결제 단말기 연결이 감지되지 않습니다.<br>정상적으로 연결 하신다음, 다시 시도해 주세요.");
            } else if (res.ERRORMESSAGE === " /  / ") {
                alertCancel("카드 단말기를 조작중이지 않을 때,<br>다시 세탁접수 메뉴로 들어와 주시면<br>카드결제 기능이 활성화 됩니다.");
            } else {
                alertCancel(res.ERRORMESSAGE);
            }
        }
    });
}

function setCookie(key, value, exdays = 7) {
    let cookieValue = getCookie();
    cookieValue[key] = value;
    const today = new Date();
    today.setTime(today.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires="+today.toUTCString();
    document.cookie = "receiptreg=" + JSON.stringify(cookieValue) + ";" + expires + ";path=/user/receiptreg";
}

function getCookie() {
    const name = "receiptreg=";
    let ca = document.cookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            let cookieValue = c.substring(name.length, c.length);
            try {
                cookieValue = JSON.parse(cookieValue ? cookieValue : "{}");
            } catch (e) {
                document.cookie = name + ";  expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                return {};
            }
            return cookieValue;
        }
    }
    return {};
}

/* 수기행사 할인 퍼센트 유효성 검사동작 */
function validateManualDiscountPercent() {
    const $manualDiscountPercent = $("#manualDiscountPercent");
    let value = $manualDiscountPercent.val().positiveNumberInput();
    if (value && value > 100) {
        value = 100;
    }
    $manualDiscountPercent.val(value);

    setCookie("manualDiscountPercent", value, 3);
    calculateItemPrice();
}

/* 처리내역 버튼 동작에 따라 없음 버튼에 불이 들어올지 꺼져야 할지를 판단하여 조치한다. */
function judgeEtcNoneOnOff() {
    const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
    if(!$("#processCheck input[type='checkbox']:checked").length && !$isEtcProcessChecked) {
        $("#etcNone").prop("checked", true);
    }else if($("#etcNone").is(":checked") || $isEtcProcessChecked) {
        $("#etcNone").prop("checked", false);
    }
}

/* 서버에서 건네준 행사 정보를 우선순위에 따라 biItemcode 당 1개의 행사만으로 정제한다. */
function refinePromotionData() {
    const rawData = initialData.promotionData;
    const refinedPromotionData = {
    }

    for (const data of rawData) {
        /* 해당 biItemcode로 먼저 기억해둔 행사가 없거나, 루프로 확인중인 데이터가 우선순위가 더 높을 때 해당 biItemcode의 데이터를 등록, 교체한다. */
        if (!refinedPromotionData[data.biItemcode]
            || hasDataPriority(refinedPromotionData[data.biItemcode], data)) {
            refinedPromotionData[data.biItemcode] = data;
        }
    }

    initialData.activePromotionData = refinedPromotionData;

    /* 행사타입과 할인율의 우선순위를 검사하여 해당 biItemcode의 기억될 값을 교체해야 할 경우 true를 반환한다. */
    function hasDataPriority(expData, ctrlData) {
        /* 행사 타입의 우선순위 번호를 매기기 위해 순위대로 배열에 할당한 hpType값 */
        const hpTypePriority = ["02", "03", "01"];
        const expPriority = hpTypePriority.indexOf(expData.hpType);
        const ctrlPriority = hpTypePriority.indexOf(ctrlData.hpType);

        if (expPriority > ctrlPriority) {
            return true;
        } else if (expPriority === 2 && ctrlPriority === 2) {
            return expData.hiDiscountRt < ctrlData.hiDiscountRt;
        } else {
            return false;
        }
    }
}

