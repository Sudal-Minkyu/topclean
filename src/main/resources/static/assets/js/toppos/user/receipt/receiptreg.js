
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
                payAmount: "nr", // 결제금액
            }
        }
    },
}


$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids(true);

    setDataIntoGrid(2, gridCreateUrl[2]);

    /* 가상키보드의 사용 선언 */
    vkey = new VKeyboard();

    /* 새로 추가되는 행은 체크된 상태로 시작하도록 */
    AUIGrid.bind(gridId[0], "addRow", function (e) {
        AUIGrid.addCheckedRowsByIds(gridId[0], e.items[0]._$uid);
    });

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

    /* 가격 정보를 받아서 저장해둠 */
    /*
    CommonUI.ajax("", "GET", false, function(req) {
        priceData = req.sendData;
    });
    */

    // 팝업 닫기
    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');
    })


    CommonUI.ajax("/api/user/itemGroupAndPriceList", "GET", false, function (req){
        initialData = req.sendData;
        console.log(initialData);

        setBgMenu(false);

        setNextTag(initialData.etcData.fdTag);
        frTagNo = initialData.etcData.fdTag.substring(0, 3);
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
        currentRequest.fdRepairAmt = 0;
        currentRequest.fdRepairRemark = "";
        $("#fdRepair").prop("checked", false);
        $("#fdRepairAmt").val(0);
        $("#fdRepairRemark").val("");
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
        currentRequest.fdAdd1Amt = 0;
        currentRequest.fdAdd1Remark = "";
        $("#fdAdd1").prop("checked", false);
        $("#fdAdd1Amt").val(0);
        $("#fdAdd1Remark").val("");
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

    $('.choice-drop__item').on('change', function(e) {
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

/* 가맹점 고유 택번호 3자리가 기억된다. */
let frTagNo = false;

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
let gridCreateUrl = [];
let gridSaveUrl = [];

/* 그리드를 뿌릴 대상 div의 id들 */
gridTargetDiv = [
    "grid_requestList", "grid_customerList", "grid_tempSaveList", "grid_payment"
];

/* 그리드를 받아올 때 쓰이는 api, 이번엔 예외적으로 배열 2번째 값은 첫번째 그리드에서 쓰인다.*/
gridCreateUrl = [
    "/api/user/tempRequestDetailList", "/api/user/tempRequestDetailDelete", "/api/user/tempRequestList", "/api/user/"
];

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/user/requestSave", "/api/b"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "fdTag",
        headerText: "택번호",
        width: 80,
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return CommonData.formatTagNo(value);
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
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return item.fpCatIssuername ? item.fpCatIssuername : CommonData.name.fpType[value];
        },
    }, {
        dataField: "fpAmt",
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
function onRemoveTempSave(){
    alertDeleteCheck("해당 임시저장을 삭제하시겠습니까?");
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
function ajaxRemoveTempSave(frNo) {
    const params = {
        frNo: frNo
    };
    CommonUI.ajax(gridCreateUrl[1], "PARAM", params, function () {
        // 성공하면 임시저장 마스터테이블 조회
        setDataIntoGrid(2, gridCreateUrl[2]);
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
    /*
    if(numOfGrid === 0) {
        let item = [];
        for (let i = 1; i <= 20; i++) {
            item.push({
                fdTag : "ABC-00"+i,
                biName : "상품상품"+i,
                sumProcess : "제다추수",
                fdNormalAmt : i * 1000,
                fdRepairAmt : (21-i) * 500,
                fdAdd1Amt : i * 500,
                fdDiscountAmt : i * 200,
                fdQty : 21-i,
                fdColor : "색상",
                fdRemark : "특이사항특이사항",
                frEstimateDt : "18450815",
            });
        }
        AUIGrid.setGridData(gridId[0], item);
    }
    */
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
    const frNo = AUIGrid.getSelectedRows(gridId[2])[0].frNo;
    loadTempSave(frNo);
}

function loadTempSave(frNo) {
    CommonUI.ajax(gridCreateUrl[0], "GET", {frNo: frNo}, function (req) {
        initialData.etcData.frNo = frNo;
        selectedCustomer = req.sendData.gridListData[0];
        checkCustomer();
        AUIGrid.clearGridData(gridId[0]);
        AUIGrid.setGridData(gridId[0], req.sendData.requestDetailList);
        $("#tempSaveListPop").removeClass("active");
        calculateMainPrice();
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
    $("#beforeUncollectMoneyMain").html(selectedCustomer.beforeUncollectMoney.toLocaleString());
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
    calculateMainPrice();
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

function calculateMainPrice() {
    const items = AUIGrid.getGridData(gridId[0]);
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
    const softDeleteItems = AUIGrid.getRemovedItems(gridId[0]);
    softDeleteItems.forEach(el => {
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
        }else{
            console.log(e);
        }
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
            console.log(e);
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
            console.log(e);
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
    const copyObj = {
        _$uid: currentRequest._$uid,
        fdTag: currentRequest.fdTag,
        biItemcode: currentRequest.biItemcode,
        fdColor: currentRequest.fdColor,
        fdPattern: currentRequest.Pattern,
        fdPriceGrade: currentRequest.fdPriceGrade,
        fdOriginAmt: currentRequest.fdOriginAmt,
        fdNormalAmt: currentRequest.fdNormalAmt,
        fdRepairRemark: currentRequest.fdRepairRemark,
        fdRepairAmt: currentRequest.fdRepairAmt,
        fdAdd1Remark: currentRequest.fdAdd1Remark,
        fdSpecialYn: currentRequest.fdSpecialYn,
        fdUrgentYn: currentRequest.fdUrgentYn,
        fdAdd1Amt: currentRequest.fdAdd1Amt,
        fdPressed: currentRequest.fdPressed,
        fdWhitening: currentRequest.fdWhitening,
        fdPollution: currentRequest.fdPollution,
        fdPollutionLevel: currentRequest.fdPollutionLevel,
        fdPollutionLocFcn: currentRequest.fdPollutionLocFcn,
        fdPollutionLocFcs: currentRequest.fdPollutionLocFcs,
        fdPollutionLocFcb: currentRequest.fdPollutionLocFcb,
        fdPollutionLocFlh: currentRequest.fdPollutionLocFlh,
        fdPollutionLocFrh: currentRequest.fdPollutionLocFrh,
        fdPollutionLocFlf: currentRequest.fdPollutionLocFlf,
        fdPollutionLocFrf: currentRequest.fdPollutionLocFrf,
        fdPollutionLocBcn: currentRequest.fdPollutionLocBcn,
        fdPollutionLocBcs: currentRequest.fdPollutionLocBcs,
        fdPollutionLocBcb: currentRequest.fdPollutionLocBcb,
        fdPollutionLocBrh: currentRequest.fdPollutionLocBrh,
        fdPollutionLocBlh: currentRequest.fdPollutionLocBlh,
        fdPollutionLocBrf: currentRequest.fdPollutionLocBrf,
        fdPollutionLocBlf: currentRequest.fdPollutionLocBlf,
        fdStarch: currentRequest.fdStarch,
        fdWaterRepellent: currentRequest.fdWaterRepellent,
        fdDiscountGrade: currentRequest.fdDiscountGrade,
        fdDiscountAmt: currentRequest.fdDiscountAmt,
        fdQty: currentRequest.fdQty,
        fdRequestAmt: currentRequest.fdRequestAmt,
        fdTotAmt: currentRequest.fdTotAmt,
        fdRetryYn: currentRequest.fdRetryYn,
        fdRemark: currentRequest.fdRemark,
        frEstimateDate: currentRequest.frEstimateDate,
        sumName: "",
        photoList: photos,
    }
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
    let element = document.getElementsByClassName("regist__bottom");
    element[0].classList.toggle("active");

    let grid = document.getElementById("grid_requestList");
    grid.classList.toggle("active");

    AUIGrid.resize(gridId[0]);
}

function onAddOrder() {

    if(!currentRequest.biItemcode.length) {
        alertCaution("소재를 선택해 주세요", 1);
        return false;
    }
    if(selectedCustomer === undefined) {
        alertCaution("먼저 고객을 선택해 주세요", 1);
        return false;
    }

    /*
    const colorName = {
        C00: "없음", C01: "흰색", C02: "검정", C03: "회색", C04: "빨강", C05: "주황",
        C06: "노랑", C07: "초록", C08: "파랑", C09: "남색", C10: "보라", C11: "핑크"
    }
    */

    currentRequest.fdColor = $("input[name='fdColor']:checked").val();
    //item.fdColorName = colorName["C" + item.fdColor];
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

    if(currentRequest._$uid) {
        const copyObj = {
            _$uid: currentRequest._$uid,
            fdTag: currentRequest.fdTag,
            biItemcode: currentRequest.biItemcode,
            fdColor: currentRequest.fdColor,
            fdPattern: currentRequest.Pattern,
            fdPriceGrade: currentRequest.fdPriceGrade,
            fdOriginAmt: currentRequest.fdOriginAmt,
            fdNormalAmt: currentRequest.fdNormalAmt,
            fdRepairRemark: currentRequest.fdRepairRemark,
            fdRepairAmt: currentRequest.fdRepairAmt,
            fdAdd1Remark: currentRequest.fdAdd1Remark,
            fdSpecialYn: currentRequest.fdSpecialYn,
            fdUrgentYn: currentRequest.fdUrgentYn,
            fdAdd1Amt: currentRequest.fdAdd1Amt,
            fdPressed: currentRequest.fdPressed,
            fdWhitening: currentRequest.fdWhitening,
            fdPollution: currentRequest.fdPollution,
            fdPollutionLevel: currentRequest.fdPollutionLevel,
            fdPollutionLocFcn: currentRequest.fdPollutionLocFcn,
            fdPollutionLocFcs: currentRequest.fdPollutionLocFcs,
            fdPollutionLocFcb: currentRequest.fdPollutionLocFcb,
            fdPollutionLocFlh: currentRequest.fdPollutionLocFlh,
            fdPollutionLocFrh: currentRequest.fdPollutionLocFrh,
            fdPollutionLocFlf: currentRequest.fdPollutionLocFlf,
            fdPollutionLocFrf: currentRequest.fdPollutionLocFrf,
            fdPollutionLocBcn: currentRequest.fdPollutionLocBcn,
            fdPollutionLocBcs: currentRequest.fdPollutionLocBcs,
            fdPollutionLocBcb: currentRequest.fdPollutionLocBcb,
            fdPollutionLocBrh: currentRequest.fdPollutionLocBrh,
            fdPollutionLocBlh: currentRequest.fdPollutionLocBlh,
            fdPollutionLocBrf: currentRequest.fdPollutionLocBrf,
            fdPollutionLocBlf: currentRequest.fdPollutionLocBlf,
            fdStarch: currentRequest.fdStarch,
            fdWaterRepellent: currentRequest.fdWaterRepellent,
            fdDiscountGrade: currentRequest.fdDiscountGrade,
            fdDiscountAmt: currentRequest.fdDiscountAmt,
            fdQty: currentRequest.fdQty,
            fdRequestAmt: currentRequest.fdRequestAmt,
            fdTotAmt: currentRequest.fdTotAmt,
            fdRetryYn: currentRequest.fdRetryYn,
            fdRemark: currentRequest.fdRemark,
            frEstimateDate: currentRequest.frEstimateDate,
            sumName: "",
            photoList: currentRequest.photoList,
        }
        AUIGrid.updateRowsById(gridId[0], copyObj);
    }else{
        currentRequest.fdTag = nextFdTag.replace(/[^0-9A-Za-z]/g, "");
        AUIGrid.addRow(gridId[0], currentRequest, "last");
        setNextTag(currentRequest.fdTag);
    }

    calculateMainPrice();
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
}

function onRemoveOrder() {
    if(AUIGrid.getCheckedRowItems(gridId[0]).length){
        AUIGrid.removeCheckedRows(gridId[0]);
    }else{
        AUIGrid.removeRow(gridId[0], "selectedIndex");
    }
    calculateMainPrice();
}

function setNextTag(tag) {
    let nextNo = tag.substr(0,3) + "-" + (parseInt(tag.substr(-4)) + 1).toString().padStart(4, '0');
    if(tag.substring(tag.length - 4, tag.length) === "9999") {
        nextNo = tag.substr(0,3) + "-0001";
        alertCaution("택번호 뒷자리가 9999번에 도달했습니다.<br>다음 택번호 뒷자리를 0001로 설정합니다.", 1);
    }
    $("#fdTag").val(nextNo);
    nextFdTag = nextNo;
}

/* 임시저장을 두단계로 분리하기 위해 데이터를 임시로 전역변수화 */
let saveData = {};

/* 전역변수 cehckNum이 1일 경우에는 임시저장, 2일 경우에는 접수완료처리 */
function onSave() {

    // 추가된 행 아이템들(배열)
    const addedRowItems = AUIGrid.getAddedRowItems(gridId[0]);

    // 수정된 행 아이템들(배열)
    const updatedRowItems = AUIGrid.getEditedRowItems(gridId[0]);

    // 삭제된 행 아이템들(배열)
    const deletedRowItems = AUIGrid.getRemovedItems(gridId[0]);

    if(selectedCustomer === undefined) {
        alertCaution("먼저 고객을 선택해 주세요", 1);
        return false;
    }else if(addedRowItems.length === 0 && updatedRowItems.length === 0 && deletedRowItems.length === 0 && checkNum === "1") {
        alertCaution("추가/변경/삭제 사항이 없습니다", 1);
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


    if(checkNum === "1") {
        alertCheck("임시저장 하시겠습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            onSaveAjax();
        });
    }else {
        onSaveAjax();
    }
}

function onSaveAjax() {
    CommonUI.ajax(gridSaveUrl[0], "MAPPER", saveData, function (req) {
        AUIGrid.removeSoftRows(gridId[0]);
        AUIGrid.resetUpdatedItems(gridId[0]);
        AUIGrid.clearGridData(gridId[2]);
        if(checkNum === "1") {
            alertSuccess("임시저장이 되었습니다");
            initialData.etcData.frNo = req.sendData.frNo;
            setDataIntoGrid(2, gridCreateUrl[2]);
        }
        if(checkNum === "2") {
            console.log(req);
            initialData.etcData.beforeUncollectMoney = req.sendData.beforeUncollectMoney;
            initialData.etcData.saveMoney = req.sendData.saveMoney;
            initialData.etcData.todayUncollectMoney = req.sendData.todayUncollectMoney;
            selectedCustomer.beforeUncollectMoney = req.sendData.beforeUncollectMoney;
            selectedCustomer.saveMoney = req.sendData.saveMoney;

            $("#beforeUncollectMoney").html(selectedCustomer.beforeUncollectMoney.toLocaleString());
            $("#saveMoney").html(selectedCustomer.saveMoney.toLocaleString());
            $("#beforeUncollectMoneyMain").html(selectedCustomer.beforeUncollectMoney.toLocaleString());
            $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
            initialData.etcData.frNo = req.sendData.frNo;
        }
    })
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
        items[0].item["fdTag"] = nextFdTag.replace(/[^0-9a-zA-Z]/g, "");
        AUIGrid.addRow(gridId[0], items[0].item, "last");
        setNextTag(items[0].item.fdTag);
        calculateMainPrice();
    }
}

function changeQty() {
    tempItem.fdQty = $("#hiddenKeypad").val().toInt();
    tempItem.fdRequestAmt = (tempItem.fdNormalAmt + tempItem.fdPressed + tempItem.fdWhitening
        + tempItem.fdWaterRepellent + tempItem.fdStarch + tempItem.fdPollution + tempItem.fdRepairAmt
        + tempItem.fdAdd1Amt - tempItem.fdDiscountAmt) * tempItem.fdQty;
    tempItem.fdTotAmt = tempItem.fdRequestAmt;
    AUIGrid.updateRowsById(gridId[0], tempItem);
    calculateMainPrice();
}

/* 접수완료시 호출 API */
function onApply() {
    if($("#totFdQty").html() === "0") {
        alertCaution("접수완료할 품목이 없습니다.", 1);
        return false;
    }
    checkNum = "2";
    onSave();

    const totRequestAmt = $("#totFdRequestAmount").html().toInt();
    $("#payNormalAmt").html($("#totFdNormalAmount").html().toInt().toLocaleString());
    $("#payChangeAmt").html($("#totChangeAmount").html().toInt().toLocaleString());

    const applySaveMoney = selectedCustomer.saveMoney > totRequestAmt ? totRequestAmt : selectedCustomer.saveMoney;
    $("#applySaveMoney").html(applySaveMoney.toLocaleString());

    $("#applyUncollectAmt").html("0");
    $("#payRequestAmt").html((totRequestAmt - applySaveMoney).toLocaleString());
    $("#totalAmt").html((totRequestAmt - applySaveMoney).toLocaleString());
    setReceiveAmtToTotalAmt();
    calculateOne();
    calculateTwo();
    calculateThree();

    $("#paymentPop").addClass("active");
}

function calculateOne() {
    const totRequestAmt = $("#totFdRequestAmount").html().toInt();
    const applySaveMoney = $("#applySaveMoney").html().toInt();
    const applyUncollectAmt = $("#applyUncollectAmt").html().toInt();
    const paidData =  AUIGrid.getGridData(gridId[3]);
    let paidAmt = 0;
    paidData.forEach(el => {
       paidAmt += el.fpAmt;
    });

    /* 최종결제금액 = A - B + C - D */
    const totalAmt = totRequestAmt - applySaveMoney + applyUncollectAmt - paidAmt;

    $("#totalAmt").html(totalAmt.toLocaleString());
}

function calculateTwo() {
    const totalAmt = $("#totalAmt").html().toInt();
    const receiveCash = $("#receiveCash").html().toInt();
    const changeCash = receiveCash - totalAmt;
    const uncollectAmtCash = totalAmt - receiveCash;

    if(changeCash > 0) {
        $("#changeCash").html(changeCash.toLocaleString());
        $("#uncollectAmtCash").html("0");
    }else{
        $("#changeCash").html("0");
        $("#uncollectAmtCash").html(uncollectAmtCash.toLocaleString());
    }
}

function calculateThree() {
    const totalAmt = $("#totalAmt").html().toInt();
    const receiveCard = $("#receiveCard").html().toInt();
    const uncollectAmtCard = totalAmt - receiveCard;
    if(uncollectAmtCard > 0) {
        $("#uncollectAmtCard").html(uncollectAmtCard.toLocaleString());
    }else{
        $("#uncollectAmtCard").html("0");
    }
}

/* 키패드 작동용 */
let keypadNum;
function onKeypad(num) {
    const targetId = ["applySaveMoney", "receiveCash", "receiveCard"];
    keypadNum = num;
    $("#hiddenKeypad").val($("#" + targetId[keypadNum]).html());
    vkey.showKeypad("hiddenKeypad", {callback: onKeypadConfirm});
}

function onKeypadConfirm() {
    const targetId = ["applySaveMoney", "receiveCash", "receiveCard"];
    $("#" + targetId[keypadNum]).html($("#hiddenKeypad").val());

    switch (keypadNum) {
        case 0 :
            calculateOne();
            calculateTwo();
            calculateThree();
            break;
        case 1 :
            calculateTwo();
            break;
        case 2 :
            calculateThree();
            break;
    }

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
                preUncollectAmount: initialData.etcData.beforeUncollectMoney - applyUncollectAmt,
                curUncollectAmount: initialData.etcData.todayUncollectMoney,
                uncollectPayAmount: applyUncollectAmt,
                totalUncollectAmount: initialData.etcData.beforeUncollectMoney - applyUncollectAmt + initialData.etcData.todayUncollectMoney,
                month: $("#installmentMonth").val().toInt(),
                items: items,
            };


        const paymentTab = $(".pop__pay-tabs-item.active").attr("data-id");
        if (paymentTab === "tabCash") {
            const receiveCash = $("#receiveCash").html().toInt();
            if (receiveCash) {
                if (applyUncollectAmt && $("#totalAmt").html().toInt() - receiveCash > 0) {
                    alertCaution("미수전액완납시, <br>한번에 전액을 상환하셔야 합니다.", 1);
                }
                paymentData.type = "cash";
                paymentData.paymentAmount = receiveCash;
            }
        } else if (paymentTab === "tabCard") {
            const receiveCard = $("#receiveCard").html().toInt();
            if (receiveCard) {
                if (applyUncollectAmt && $("#totalAmt").html().toInt() - receiveCard > 0) {
                    alertCaution("미수전액완납시, <br>한번에 전액을 상환하셔야 합니다.", 1);
                }
                paymentData.type = "card";
                paymentData.paymentAmount = receiveCard;
            }
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
                    }
                    // 결제 실패의 경우
                    if (resjson.STATUS === "FAILURE") {
                        console.log(resjson);
                        $('#payStatus').hide();
                        alertCancel("단말기 처리 중 에러가 발생하였습니다<br>잠시후 다시 시도해주세요");
                    }
                });
            } catch (e) {
                console.log(e);
            }
        } else {
            onPaymentStageTwo();
        }

    } catch (e) {
        console.log(e);
        return false;
    }
}

/* 결재할 때 */
function onPaymentStageTwo(creditData = {}) {
    try {
        const url = "/api/user/requestPayment";
        let data = {
            payment: [],
            etc: {
                bcId: selectedCustomer.bcId,
                frNo: initialData.etcData.frNo,
            }
        }
        const applyUncollectAmt = $("#applyUncollectAmt").html().toInt();

        const paymentTab = $(".pop__pay-tabs-item.active").attr("data-id");
        if (paymentTab === "tabCash") {
            const receiveCash =
                $("#receiveCash").html().toInt() - $("#changeCash").html().toInt();
            const paymentCash = {
                fpType: "01",
                fpRealAmt: receiveCash,
                fpAmt: parseInt(receiveCash - applyUncollectAmt),
                fpCollectAmt: applyUncollectAmt,
            }
            data.payment.push(paymentCash);
        } else if (paymentTab === "tabCard") {

            const receiveCard = $("#receiveCard").html().toInt();

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

        CommonUI.ajax(url, "MAPPER", data, function (req){
            console.log("결제후 :");
            console.log(req);
            AUIGrid.addRow(gridId[3], req.sendData.paymentEtcDtos, "last");
            initialData.etcData.beforeUncollectMoney = req.sendData.beforeUncollectMoney;
            initialData.etcData.saveMoney = req.sendData.saveMoney;
            initialData.etcData.todayUncollectMoney = req.sendData.todayUncollectMoney;
            selectedCustomer.beforeUncollectMoney = req.sendData.beforeUncollectMoney;
            selectedCustomer.saveMoney = req.sendData.saveMoney;
            $("#beforeUncollectMoney").html(selectedCustomer.beforeUncollectMoney.toLocaleString());
            $("#saveMoney").html(selectedCustomer.saveMoney.toLocaleString());
            $("#beforeUncollectMoneyMain").html(selectedCustomer.beforeUncollectMoney.toLocaleString());
            $("#saveMoneyMain").html(selectedCustomer.saveMoney.toLocaleString());
            $("#applySaveMoney").html("0");
            $("#applyUncollectAmt").html("0");
            $("#receiveCash").html("0");
            $("#changeCash").html("0");
            $("#receiveCard").html("0");
            //$("#saveMoney").html(req.sendData.saveMoney);
            calculateOne();
            calculateTwo();
            calculateThree();
            if($("#totalAmt").html() === "0" && autoPrintReceipt) {
                onPrintReceipt();
                onClosePayment();
            }
        });
    }catch (e) {
        console.log(e);
        return false;
    }
}

function onPayUncollectMoney() {
    $("#applyUncollectAmt").html($("#beforeUncollectMoney").html());
    alertCaution("미수전액완납시, 적립금은 사용하지<br> 못하며 한 가지 결제수단 으로 <br>전액 결제하셔야 합니다.", 1);
    payAmtLimitation();
}

function cancelPayUncollectMoney() {
    $("#applyUncollectAmt").html(0);
    payAmtLimitation();
}

function onClosePayment() {
    const uncollectAmtCash = $("#totalAmt").html().toInt() + $("#applySaveMoney").html().toInt()
        - $("#applyUncollectAmt").html().toInt();
    if(uncollectAmtCash) {
        alertCheck("미수금이 " + uncollectAmtCash.toLocaleString() + "원 발생합니다<br> 이대로 닫으시겠습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            $('#popupId').remove();
            closePaymentPop();
        });
    }else{
        closePaymentPop();
    }
}

function closePaymentPop() {
    AUIGrid.clearGridData(gridId[0]);
    AUIGrid.clearGridData(gridId[3]);
    calculateMainPrice();
    $("#paymentPop").removeClass("active");
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

    calculateOne();
    calculateTwo();
    calculateThree();
}

function setReceiveAmtToTotalAmt() {
    const totalAmt = $("#totalAmt").html().toInt();
    $("#receiveCash").html(totalAmt.toLocaleString());
    $("#receiveCard").html(totalAmt.toLocaleString());
}

function onPrintReceipt() {
    const condition = {
        frNo: initialData.etcData.frNo,
        frId: "",
    }

    CommonUI.toppos.printReceipt(condition);
}

function onSaveFdTag() {
    const tag = $("#fdTag").val().replace(/[^0-9A-Za-z]/g, "");

    if(tag.substring(0, 3) !== frTagNo) {
        alertCaution("현 가맹점의 택번호 앞 3자리는<br>" + frTagNo + "로 시작해야 합니다. ", 1);
        return false;
    }

    if(tag.length < 7) {
        alertCaution("택번호는 7자리의 숫자여야 합니다.", 1);
        return false;
    }
    
    if(tag.substr(-4) === "0000") {
        alertCaution("다음 택번호는 최소 0001 부터 시작해 주세요.", 1);
        return false;
    }

    alertCheck(`다음 택번호는 ${nextFdTag} 입니다.<br>${$("#fdTag").val()}(으)로 변경 하시겠습니까?`);
    $("#checkDelSuccessBtn").on("click", function () {
        $("#fdTagChange").addClass("active");
        $('#popupId').remove();
    });
}

function onConfirmFdTag() {
    const tag = $("#fdTag").val().replace(/[^0-9A-Za-z]/g, "");
    const data = {
        password: $("#fdTagPassword").val(),
        frLastTagno: tag.substr(0,3) + (parseInt(tag.substr(-4)) - 1).toString().padStart(4, '0'),
    }
    
    const url = "/api/user/franchiseCheck";
    CommonUI.ajax(url, "GET", data, function (res) {
        nextFdTag = tag.substr(0,3) + "-" + tag.substr(-4);
        onCloseFdTag();
        alertSuccess("다음 택번호가 변경되었습니다.");
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
            $(".bgItemGroupcode").on("click", function () {
                onPopReceiptReg(this);
            });
        }
    });
}

function tempSaveExistWarning() {
    alertThree("해당 고객의 임시저장 내역이 존재합니다.<br>어떻게 하시겠습니까?",
        "이어서 접수", "삭제후 접수", "취소");

    $("#popFirstBtn").on("click", function() {
        loadTempSave(selectedCustomer.tempSaveFrNo);
        $('#popupId').remove();
    });

    $("#popSecondBtn").on("click", function() {
        ajaxRemoveTempSave(selectedCustomer.tempSaveFrNo);
        $('#popupId').remove();
    });

    $("#popThirdBtn").on("click", function() {
        $('#popupId').remove();
    });

}

function testFunc(a, b) {
    return a + b;
}

export {testFunc};