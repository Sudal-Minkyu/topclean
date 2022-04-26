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
    $("#grid_requestList").addClass("active");

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
        const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
        if(!$("#processCheck input[type='checkbox']:checked").length && !$isEtcProcessChecked) {
            $processInput.first().prop("checked", true);
        }else if($processInput.first().is(":checked") || $isEtcProcessChecked) {
            $processInput.first().prop("checked", false);
        }

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

    $("#etcNone").on("click", function () {
        $processInput.prop("checked", false);
        $("#waterNone").prop("checked", true);
        $("#waterBtn").removeClass("choice-drop__btn--active");
        resetPollutionPop();
        resetFdRepairInputs();
        resetFdAddInputs();
        // 각 내부 항목들의 초기화
        setTimeout(function () {
            calculateItemPrice();
        }, 0);
    });

    $("#urgentNone").on("click", function () {
        $urgentInput.prop("checked", false);
        calculateItemPrice();
    });

    // 팝업 닫기
    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');
    })


    CommonUI.ajax("/api/user/itemGroupAndPriceList", "GET", false, function (req){
        initialData = req.sendData;
        console.log(initialData);

        setBgMenu(false);
        setPreDefinedKeywords();
        setNextTag(initialData.etcData.fdTag);
        getParamsAndAction();
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


    // 결제팝업 탭
    const $payTabsBtn = $('.pop__pay-tabs-item');
    const $payTabsContent = $('.pop__tabs-content');

    $payTabsBtn.on('click', function() {
        const idx = $(this).index();
        $payTabsBtn.removeClass('active');
        $payTabsBtn.eq(idx).addClass('active');
        $payTabsContent.removeClass('active');
        $payTabsContent.eq(idx).addClass('active');
        setReceiveAmtToTotalAmt();
    });

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
});

/* 다음 택번호가 기억된다. */
let nextFdTag = false;

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
    "grid_requestList", "grid_customerList", "grid_tempSaveList", "grid_payment"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "fdTag",
        headerText: "택번호",
        style: "datafield_tag",
        width: 80,
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
        },
        style: "aui-grid-tagno-column",
    }, {
        dataField: "sumName",
        headerText: "상품명",
        style: "receiptreg-product-name",
        width: 200,
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            CommonUI.toppos.makeProductName(item, initialData.userItemPriceSortData);
            return item.sumName;
        },
        renderer : {
            type: "IconRenderer",
            iconPosition: "left",
            iconWidth : 30,
            iconHeight : 30,
            iconFunction: function(rowIndex, columnIndex, value, item) {
                let icon = "";
                if(item.photoList.length) {
                    icon = "/assets/images/icon__picture.svg";
                }else{
                    icon = "/assets/images/icon__camera.svg";
                }
                return icon;
            }
            ,
            onClick: function(event) {
                onPopTakePicture(event);
            },
        }
    }, {
        dataField: "sumProcess",
        headerText: "처리내역",
        style: "grid_textalign_left",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.toppos.processName(item);
        }
    }, {
        dataField: "fdNormalAmt",
        headerText: "기본금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return item.fdRetryYn==="Y" ? 0 : value.toLocaleString();
        },
    }, {
        dataField: "fdRepairAmt",
        headerText: "수선금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return item.fdRetryYn === "Y" ? 0 : value.toLocaleString();
        },
    }, {
        dataField: "",
        headerText: "추가금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            const addAmount = item.fdPressed + item.fdWhitening + item.fdWaterRepellent + item.fdStarch
                + item.fdPollution + item.fdAdd1Amt;
            return item.fdRetryYn === "Y" ? 0 : addAmount.toLocaleString();
        },
    }, {
        dataField: "fdDiscountAmt",
        headerText: "할인금액",
        style: "grid_textalign_right",
        width: 70,
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return item.fdRetryYn === "Y" ? 0 : value.toLocaleString();
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
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
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
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
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
        labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
            const template = `
                <button class="c-button c-button--solid  c-button--supersmall" 
                    onclick="onModifyOrder(${rowIndex})">수정</button>
            `;
            return template;
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
    showRowAllCheckBox: true,
    showRowCheckColumn: true,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : false,
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
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
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
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
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
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return item.fpCatIssuername ? item.fpCatIssuername : CommonData.name.fpType[value];
        },
    }, {
        dataField: "fpAmt",
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
    console.log(params);
    CommonUI.ajax("/api/user/tempRequestDetailDelete", "PARAM", params, function (res) {
        console.log(res);
        // 성공하면 임시저장 마스터테이블 조회
        setDataIntoGrid(2, "/api/user/tempRequestList");
        alertSuccess("임시저장이 삭제되었습니다.");
        if(setAfterDelete) {
            onPutCustomer();
        }
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
        console.log(items);
        if(items.length === 1) {
            selectedCustomer = items[0];
            checkCustomer();
            delete initialData.etcData["frNo"];
            checkNum = "1";
        }else if(items.length > 1) {
            AUIGrid.setGridData(gridId[1], items);
            $("#customerListPop").addClass("active");
        }else{
            alertCheck("일치하는 고객 정보가 없습니다.<br>신규고객으로 등록 하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                location.href="/user/customerreg"
            });
        }
    });
}

function onSelectCustomer() {
    selectedCustomer = AUIGrid.getSelectedRows(gridId[1])[0];
    if(selectedCustomer) {
        checkCustomer();
        delete initialData.etcData["frNo"];
        checkNum = "1";
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
        AUIGrid.clearGridData(gridId[0]);
        AUIGrid.setGridData(gridId[0], req.sendData.requestDetailList);
        $("#tempSaveListPop").removeClass("active");
        calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));
        checkNum = "1";
    });
}

function checkCustomer() {
    if(selectedCustomer.tempSaveFrNo) {
        tempSaveExistWarning();
    } else {
        onPutCustomer();
    }
}

function onPutCustomer() {
    let bcGradeName;
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
    $("#bcName").html(selectedCustomer.bcName + "님");
    $("#bcValuation").attr("class",
        "propensity__star propensity__star--" + selectedCustomer.bcValuation).css('display','block');
    $("#bcAddress").html(selectedCustomer.bcAddress);
    $("#bcHp").html(CommonUI.formatTel(selectedCustomer.bcHp));
    $("#uncollectMoneyMain").html(selectedCustomer.uncollectMoney.toLocaleString());
    $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
    $("#bcRemark").html(selectedCustomer.bcRemark);
    if(selectedCustomer.bcLastRequestDt) {
        $("#bcLastRequestDt").html(
            selectedCustomer.bcLastRequestDt.substr(0, 4) + "-"
            + selectedCustomer.bcLastRequestDt.substr(4, 2) + "-"
            + selectedCustomer.bcLastRequestDt.substr(6, 2)
        );
    }else{
        $("#bcLastRequestDt").html("없음");
    }

    $("#class02, #class03").parents("li").css("display", "none");
    $("#class" + selectedCustomer.bcGrade).parents("li").css("display", "block");
    AUIGrid.clearGridData(gridId[0]);
    calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));

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

    // 처음 표시 중분류 기본상태 N, 만일 중분류에 N이 없는 예외상황시 수정해줘야함.
    setBiItemList("N");
    $("input[name='bsItemGroupcodeS']").first().prop("checked", true);

    calculateItemPrice();
    $('#productPop').addClass('active');
    $("#biItemList").scrollTop(0);
}


function setBiItemList(bsCode) {
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

        $(".camBoiler").on("click", function () {
            const $ffRemark = $("#ffRemark");
            $ffRemark.val($ffRemark.val() + " " + this.innerHTML);
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

            const formData = new FormData();
            formData.append("source", blob);
            formData.append("ffRemark", $ffRemark.val());
            console.log(Object.fromEntries(formData));

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

    $camBoiler = $(".camBoiler");
    if($camBoiler.length) {
        for(let i = 0; i < $camBoiler.length; i++) {
            removeEventsFromElement($camBoiler[i]);
        }
    }

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

    let grid = document.getElementById("grid_requestList");
    grid.classList.toggle("active");

    AUIGrid.resize(gridId[0]);
}

function onAddOrder() {
    if(selectedCustomer === undefined) {
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
            alertSuccess("접수의뢰를 리스트에 추가합니다.<br>고객으로부터 서류를 통해 동의를 받아주세요.");
            $("#successBtn").on("click", function () {
                currentRequest.fdAgreeType = "2";
                currentRequest.fdSignImage = "";
                putItemIntoGrid();
                $('#popupId').remove();
            });
        });
        $("#popThirdBtn").on("click", function () {
            $('#popupId').remove();
        });
        return false;
    }
    currentRequest.sumName = "";

    putItemIntoGrid();
}

function putItemIntoGrid() {
    currentRequest.fdColor = $("input[name='fdColor']:checked").val();
    currentRequest.fdPattern = $("input[name='fdPattern']:checked").val();
    currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();
    currentRequest.fdRemark = $("#fdRemark").val();
    currentRequest.frEstimateDate = initialData.etcData.frEstimateDate.numString();
    currentRequest.fdSpecialYn = $("#fdSpecialYn").is(":checked") ? "Y" : "N";
    currentRequest.fdUrgentYn = $("#fdUrgentYn").is(":checked") ? "Y" : "N";
    
    const pollutionLoc = $("input[name='pollutionLoc']");
    for(let i = 0; i < pollutionLoc.length; i++) {
        if($(pollutionLoc[i]).is(":checked")) {
            currentRequest[pollutionLoc[i].id] = "Y";
        }else {
            currentRequest[pollutionLoc[i].id] = "N";
        }
    }

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

    if(currentRequest._$uid) {
        const copyObj = CommonUI.cloneObj(currentRequest);
        copyObj["dummy" + parseInt(Math.random() * 100000).toString()] = "dummy";
        AUIGrid.updateRowsById(gridId[0], copyObj);
    }else{
        currentRequest.fdTag = nextFdTag.numString();
        AUIGrid.addRow(gridId[0], currentRequest, "last");
        setNextTag(currentRequest.fdTag);
    }

    calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));
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

    if($("#pollution00").is(":checked") && $("#fdPollution").is(":checked")) {
        $("#fdPollution").trigger("click");
    }else if(!$("#pollution00").is(":checked") && !$("#fdPollution").is(":checked")){
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

    setBiItemList(currentRequest.biItemcode.substr(3, 1));

    selectedLaundry.bgCode = currentRequest.biItemcode.substr(0, 3);
    selectedLaundry.bsCode = currentRequest.biItemcode.substr(3, 1);
    $("input[name='bsItemGroupcodeS']:input[value='" + selectedLaundry.bsCode + "']").prop("checked", true);
    $("#" + currentRequest.biItemcode).prop("checked", true);
    $(".choice-color__input[value='" + currentRequest.fdColor + "']").prop("checked", true);
    $("input[name='fdPattern']:input[value='" + currentRequest.fdPattern +"']").prop("checked", true);
    $("input[name='fdPriceGrade']:input[value='" + currentRequest.fdPriceGrade +"']").prop("checked", true);
    $("input[name='fdDiscountGrade']:input[value='" + currentRequest.fdDiscountGrade +"']").prop("checked", true);

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

    if(currentRequest.fdSpecialYn === "Y") {
        $("#fdSpecialYn").prop("checked", true);
    }else{
        $("#fdSpecialYn").prop("checked", false);
    }

    if(currentRequest.fdUrgentYn === "Y") {
        $("#fdUrgentYn").prop("checked", true);
    }else{
        $("#fdUrgentYn").prop("checked", false);
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



    /* currentRequest의 각 벨류값에 따라 화면의 라디오 세팅을 구성한다. */

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
    calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));
}

function setNextTag(tag) {
    let nextNo = tag.substring(frTagInfo.frTagType, frTagInfo.frTagType + 1) + "-"
        + (parseInt(tag.substring(frTagInfo.frTagType + 1, 7)) + 1).toString().padStart(6 - frTagInfo.frTagType, '0');

    const endOfTagBundle = frTagInfo.frTagType === 2 ? "9999" : "999";
    if(tag.substring(frTagInfo.frTagType + 1, 7) === endOfTagBundle) {
        let midNum = (parseInt(tag.substring(frTagInfo.frTagType, frTagInfo.frTagType + 1)) + 1).toString();
        midNum = midNum === "10" ? "0" : midNum;
        nextNo = midNum + "-" + (frTagInfo.frTagType === 2 ? "0001" : "001");

        alertCaution(`택번호 뒷자리가 ${frTagInfo.frTagType === 2 ? "9" : ""}999번에 도달했습니다.`
            + `<br>다음 택번호 뒷자리는 ${frTagInfo.frTagType === 2 ? "0" : ""}001로 설정합니다.`, 1);
    }

    $("#fdTag").val(nextNo);
    nextFdTag = frTagInfo.frTagNo + nextNo.numString();
}

/* 임시저장을 두단계로 분리하기 위해 데이터를 임시로 전역변수화 */
let saveData = {};

function onTempSave() {
    alertCheck("임시저장 하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        onSave(true);
    });
}

/* 전역변수 cehckNum이 1일 경우에는 임시저장, 2일 경우에는 접수완료처리 */
function onSave(turnOnSuccessMsg = false, callback = function () {}, tossData = {}) {

    // 추가된 행 아이템들(배열)
    const addedRowItems = AUIGrid.getAddedRowItems(gridId[0]);

    // 수정된 행 아이템들(배열)
    const updatedRowItems = AUIGrid.getEditedRowItems(gridId[0]);

    // 삭제된 행 아이템들(배열)
    const deletedRowItems = AUIGrid.getRemovedItems(gridId[0]);

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
    }

    const data = {
        "add" : addedRowItems,
        "update" : updatedRowItems,
        "delete" : deletedRowItems,
        "etc" : etc,
    };
    saveData = data;

    onSaveAjax(turnOnSuccessMsg, callback, tossData);
}

function onSaveAjax(turnOnSuccessMsg, callback, tossData) {
    CommonUI.ajax("/api/user/requestSave", "MAPPER", saveData, function (req) {
        AUIGrid.removeSoftRows(gridId[0]);
        AUIGrid.resetUpdatedItems(gridId[0]);
        AUIGrid.clearGridData(gridId[2]);
        if(checkNum === "1") {
            setDataIntoGrid(2, "/api/user/tempRequestList");
            if(turnOnSuccessMsg) {
                alertSuccess("임시저장이 되었습니다");
            }
        }
        initialData.etcData.frNo = req.sendData.frNo;

        console.log(req);

        if(checkNum === "2") {
            initialData.etcData.uncollectMoney = req.sendData.uncollectMoney;
            initialData.etcData.saveMoney = req.sendData.saveMoney;
            selectedCustomer.uncollectMoney = req.sendData.uncollectMoney;
            selectedCustomer.saveMoney = req.sendData.saveMoney;
        }
        $("#uncollectMoney").html(selectedCustomer.uncollectMoney.toLocaleString());
        $("#saveMoney").html(selectedCustomer.saveMoney.toLocaleString());
        $("#uncollectMoneyMain").html(selectedCustomer.uncollectMoney.toLocaleString());
        $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
        callback(tossData);
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

    for(let i = 0; i < $keypadBtn.length; i++) {
        removeEventsFromElement($keypadBtn[i]);
    }

    for(let i = 0; i < $keypadBackspace.length; i++) {
        removeEventsFromElement($keypadBackspace[i]);
    }

    for(let i = 0; i < $keypadBoilerplate.length; i++) {
        removeEventsFromElement($keypadBoilerplate[i]);
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
        items[0].item["fdTag"] = nextFdTag.numString();
        AUIGrid.addRow(gridId[0], items[0].item, "last");
        setNextTag(items[0].item.fdTag);
        calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));
    }
}

function changeQty() {
    tempItem.fdQty = $("#hiddenKeypad").val().toInt();
    tempItem.fdRequestAmt = (tempItem.fdNormalAmt + tempItem.fdPressed + tempItem.fdWhitening
        + tempItem.fdWaterRepellent + tempItem.fdStarch + tempItem.fdPollution + tempItem.fdRepairAmt
        + tempItem.fdAdd1Amt - tempItem.fdDiscountAmt) * tempItem.fdQty;
    tempItem.fdTotAmt = tempItem.fdRequestAmt;
    AUIGrid.updateRowsById(gridId[0], tempItem);
    calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));
}

/* 접수완료시 호출 API */
function onApply() {
    if($("#totFdQty").html() === "0") {
        alertCaution("접수완료할 품목이 없습니다.", 1);
        return false;
    }
    onSave();

    const totRequestAmt = $("#totFdRequestAmount").html().toInt();
    $("#payNormalAmt").html($("#totFdNormalAmount").html().toInt().toLocaleString());
    $("#payChangeAmt").html($("#totChangeAmount").html().toInt().toLocaleString());

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

/* 키패드 작동용 */
let keypadNum;
function onKeypad(num) {
    const targetId = ["applySaveMoney", "receiveCash", "receiveCard"];
    keypadNum = num;
    $("#hiddenKeypad").val($("#" + targetId[keypadNum]).html());
    vkey.showKeypad("hiddenKeypad", {callback: onKeypadConfirm, clrfirst: true});
}

function onKeypadConfirm() {
    const targetId = ["applySaveMoney", "receiveCash", "receiveCard"];
    $("#" + targetId[keypadNum]).html($("#hiddenKeypad").val());

    calculatePaymentStage(getPaidAmt());
    payAmtLimitation();
}

/* 임시 카드 결제용 함수 */
function onPaymentStageOne() {
    try {
        const applyUncollectAmt = $("#applyUncollectAmt").html().toInt();
        autoPrintReceipt = $("#autoPrintReceipt").is(":checked");

        let items = [];
        const orderData = AUIGrid.getGridData(gridId[0]);
        orderData.forEach(el => {
            items.push({
                tagno: el.fdTag,
                color: el.fdColor,
                itemname: el.sumName,
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
            $('#payStatus').show();
            try {
                CAT.CatCredit(paymentData, function (res) {
                    $('#payStatus').hide();
                    let resjson = JSON.parse(res);
                    // 결제 성공일경우 Print
                    if (resjson.STATUS === "SUCCESS") {
                        onPaymentStageTwo(resjson);
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
                        } else {
                            alertCancel(resjson.ERRORMESSAGE);
                        }
                    }
                });
            } catch (e) {
                CommonUI.toppos.underTaker(e, "receiptreg : 카드 단말기 결제");
            }
        } else {
            onPaymentStageTwo();
        }

    } catch (e) {
        CommonUI.toppos.underTaker(e, "receiptreg : 결제 1단계");
        return false;
    }
}

/* 결재할 때 */
function onPaymentStageTwo(creditData = {}) {
    if (checkNum === "1") {
        checkNum = "2";
        onSave(false, onPaymentStageThree, creditData);
    } else {
        onPaymentStageThree(creditData);
    }
}

function onPaymentStageThree(creditData) {
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
                fpMonth: 0,
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

        console.log("결제데이터 : ");
        console.log(data);

        CommonUI.ajax("/api/user/requestPayment", "MAPPER", data, function (req){
            console.log("결제후 :");
            console.log(req);

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
            if($("#totalAmt").html() === "0" && autoPrintReceipt) {
                printReceipt();
                onClosePayment();
            } else if($("#totalAmt").html() === "0") {
                $("#btnPayLater").parents("li").hide();
                $("#btnPayment").parents("li").hide();
                $("#btnClosePayment").parents("li").show();
            }

            for (const {fpType, fpRealAmt} of data.payment) {
                if (fpType === "01") {
                    const paymentData = {
                        paymentAmount: fpRealAmt,
                        franchiseNo: initialData.etcData.frCode,
                    }
                    alertThree("현금 영수증을 발행 하시겠습니까?", "개인용", "사업자용", "아니오");
                    $("#popFirstBtn").on("click", function () {
                        $('#payStatus').show();
                        paymentData.frNo = initialData.etcData.frNo;
                        paymentData.fcType = "1";
                        paymentData.fcInType = "01";
                        paymentData.fcRealAmt = paymentData.paymentAmount;
                        CAT.CatSetCashReceipt(paymentData, function (res) {
                            paymentData.fcCatApprovaltime = res.APPROVALTIME;
                            paymentData.fcCatApprovalno = res.APPROVALNO;
                            paymentData.fcCatCardno = res.CARDNO;
                            paymentData.fcCatMessage1 = res.MESSAGE1;
                            paymentData.fcCatMessage2 = res.MESSAGE2;
                            paymentData.fcCatNotice1 = res.NOTICE1;
                            paymentData.fcCatTotamount = res.TOTAMOUNT;
                            paymentData.fcCatVatamount = res.VATAMOUNT;
                            paymentData.fcCatTelegramflagt = res.TELEGRAMFLAG;

                            paymentData.fcCatIssuercode = res.ISSUERCODE;
                            paymentData.fcCatIssuername = res.ISSUERNAME;
                            paymentData.fcMuechantnumber = res.MERCHANTNUMBER;
                            console.log(paymentData);
                        });
                        $('#popupId').remove();
                    });

                    $("#popSecondBtn").on("click", function () {
                        $('#payStatus').show();
                        paymentData.frNo = initialData.etcData.frNo;
                        paymentData.fcType = "2";
                        paymentData.fcInType = "01";
                        paymentData.fcRealAmt = paymentData.paymentAmount;
                        CAT.CatSetCashReceipt(paymentData, function (res) {
                            paymentData.fcCatApprovaltime = res.APPROVALTIME;
                            paymentData.fcCatApprovalno = res.APPROVALNO;
                            paymentData.fcCatCardno = res.CARDNO;
                            paymentData.fcCatMessage1 = res.MESSAGE1;
                            paymentData.fcCatMessage2 = res.MESSAGE2;
                            paymentData.fcCatNotice1 = res.NOTICE1;
                            paymentData.fcCatTotamount = res.TOTAMOUNT;
                            paymentData.fcCatVatamount = res.VATAMOUNT;
                            paymentData.fcCatTelegramflagt = res.TELEGRAMFLAG;

                            paymentData.fcCatIssuercode = res.ISSUERCODE;
                            paymentData.fcCatIssuername = res.ISSUERNAME;
                            paymentData.fcMuechantnumber = res.MERCHANTNUMBER;
                            console.log(paymentData);
                        });
                        $('#popupId').remove();
                    });

                    $("#popThirdBtn").on("click", function () {
                        $('#popupId').remove();
                    });
                }
            }
        });
    }catch (e) {
        CommonUI.toppos.underTaker(e, "receiptreg : 결제 2단계");
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
                onSave(false, closePaymentPop, false);
            }
            if (autoPrintReceipt) {
                printReceipt();
            }
        });
    } else {
        closePaymentPop();
        if (autoPrintReceipt) {
            printReceipt();
        }
    }
}

function closePaymentPop(isSimpleClose = false) {
    $("#paymentPop").removeClass("active");

    if(isSimpleClose) return false;
    console.log("act");

    AUIGrid.clearGridData(gridId[0]);
    AUIGrid.clearGridData(gridId[3]);
    calculateMainPrice(AUIGrid.getGridData(gridId[0]), AUIGrid.getRemovedItems(gridId[0]));

    const url = "/api/user/requestReceiptMessage";
    const sendData = {
        frNo: initialData.etcData.frNo,
        locationHost: location.host,
    }

    CommonUI.ajax(url, "PARAM", sendData, function (res) {
        goToDeliver();
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
    for(let i = 0; i < backTagLength; i++) {
        prohibitedStartNum += "0";
    }

    if(tag === prohibitedStartNum) {
        alertCaution(`택번호 뒷자리는 최소 ${prohibitedStartNum.substring(0, prohibitedStartNum.length - 1)}1 부터 시작해 주세요.`, 1);
        return false;
    }

    alertCheck(`다음 택번호는 ${CommonData.formatFrTagNo(nextFdTag, frTagInfo.frTagType)} 입니다.<br>`
        + `${CommonData.formatFrTagNo(frTagInfo.frTagNo + $("#fdTag").val(), frTagInfo.frTagType)}(으)로 변경 하시겠습니까?`);
    $("#checkDelSuccessBtn").on("click", function () {
        $("#fdTagChange").addClass("active");
        $('#popupId').remove();
    });
}

function onConfirmFdTag() {
    const tag = frTagInfo.frTagNo + $("#fdTag").val().numString();
    const data = {
        password: $("#fdTagPassword").val(),
        frLastTagno: tag.substr(0, frTagInfo.frTagType + 1)
            + (parseInt(tag.substring(frTagInfo.frTagType + 1, 7)) - 1).toString().padStart(6 - frTagInfo.frTagType, '0'),
    }
    const url = "/api/user/franchiseCheck";
    CommonUI.ajax(url, "GET", data, function (res) {
        nextFdTag = tag;
        onCloseFdTag();
        alertSuccess("다음 택번호가 변경되었습니다.");
        $("#fdTagPassword").val("");
    });
}

function onCloseFdTag() {
    $("#fdTagChange").removeClass("active");
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
    alertThree("임시저장 내역이 존재합니다.<br>어떻게 하시겠습니까?",
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
    // $("#resultmsg").text(": 승인중 메세지- 고객이 서명중입니다. ------전체화면으로 가리기 ");

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
        alertSuccess("접수의뢰를 리스트에 추가합니다.<br>고객으로부터 서류를 통해 동의를 받아주세요.");
        $("#successBtn").on("click", function () {
            currentRequest.fdAgreeType = "2";
            currentRequest.fdSignImage = "";
            putItemIntoGrid();
            $('#popupId').remove();
        });
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
    $("#fdRepairAmt").val(0);
    $("#fdRepairRemark").val("");
}

function resetFdAddInputs() {
    currentRequest.fdAdd1Amt = 0;
    currentRequest.fdAdd1Remark = "";
    $("#fdAdd1").prop("checked", false);
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

function printReceipt() {
    if ($("#isPrintCustomersChk").is(":checked")) {
        CommonUI.toppos.printReceipt(initialData.etcData.frNo, "", true, "N");
    } else {
        CommonUI.toppos.printReceipt(initialData.etcData.frNo, "", false, "N");
    }
}