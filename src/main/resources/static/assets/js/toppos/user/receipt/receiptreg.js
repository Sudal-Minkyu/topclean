
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids(true);

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<1; i++) {
        setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /* 가상키보드의 사용 선언 */
    vkey = new VKeyboard();

    /* 새로 추가되는 행은 체크된 상태로 시작하도록 */
    AUIGrid.bind(gridId[0], "addRow", function (e) {
        AUIGrid.addCheckedRowsByIds(gridId[0], e.items[0]._$uid);
    });

    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        console.log(e.item);
    });

    // 세탁 가격 선택 항목의 변경시 필요작업과 이벤트 반영
    $("#inReceiptPop input[type='radio']").on("change", function(){
        calculateItemPrice();
    });
    const $processInput = $("#processCheck input[type='checkbox'], #processCheck input[type='radio']");
    $processInput.on("change", function(e) {
        const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
        if(!$("#processCheck input[type='checkbox']:checked").length && !$isEtcProcessChecked) {
            $processInput.first().prop("checked", true);
        }else if($processInput.first().is(":checked") || $isEtcProcessChecked) {
            $processInput.first().prop("checked", false);
        }
        const targetId = e.target.id;
        additionalProcess(targetId);

        calculateItemPrice();
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

        initialData.userItemGroupSortData.forEach(bgItem => {
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
        });
    });

    $('.choice-drop__btn').on('click', function(e) {
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
});

/* 상품 주문을 받을 때 적용되는 가격 정보 데이터를 API로 불러와 미리 저장 */
let priceData;

const fdColorCode = {
    C00: "#D4D9E1", C01: "#D4D9E1", C02: "#3F3C32", C03: "#D7D7D7", C04: "#F54E50", C05: "#FB874B",
    C06: "#F1CE32", C07: "#349A50", C08: "#55CAB7", C09: "#398BE0", C10: "#DE9ACE", C11: "#FF9FB0",
}

/* 선택된 고객이나 세탁물(대분류)의 이용을 위함  */
let selectedCustomer;

/* 선택한 상품의 대분류 3자, 중분류 1자, 최종분류 7자, 가격이 입력될 곳 */
let selectedLaundry = {
    bgCode: "",
    bsCode: "",
    biCode: "",
}

const fsRequestDtl = {
    frNo: "",
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
    fdAdd1SpecialYn: "",
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
    fdRetryYn: "N",
    fdRemark: "",
    fdEstimateDt: "",
}

// fsRequestDtl 객체를 깊은 복사하기위함.
let currentRequest = JSON.parse(JSON.stringify(fsRequestDtl));

let initialData = [];

/* 실제 가격 데이터가 올 때 까지만 */
const dummyPrice = {
    userItemPriceSortData: [
        {biItemcode: "D01N001", bgName: "상의", bsName: "일반", biName: "면", price: "1500", bfSort: "2"},
        {biItemcode: "D01S001", bgName: "상의", bsName: "숏", biName: "청", price: "2000", bfSort: "1"},
        {biItemcode: "D01L001", bgName: "상의", bsName: "롱", biName: "실크", price: "3000", bfSort: "3"},
        {biItemcode: "D01L002", bgName: "상의", bsName: "롱", biName: "폴리에스테르", price: "6000", bfSort: "8"},
        {biItemcode: "D01L003", bgName: "상의", bsName: "롱", biName: "고어텍스", price: "4000", bfSort: "6"},
        {biItemcode: "D02N001", bgName: "하의", bsName: "보통", biName: "사포", price: "2500", bfSort: "4"},
        {biItemcode: "D02L001", bgName: "하의", bsName: "라지", biName: "삼베", price: "4000", bfSort: "11"},
    ],
    userItemGroupSListData: [
        {bgItemGroupcode: "D01", bsItemGroupcodeS: "N", bsName: "일반"},
        {bgItemGroupcode: "D01", bsItemGroupcodeS: "S", bsName: "숏"},
        {bgItemGroupcode: "D01", bsItemGroupcodeS: "L", bsName: "롱"},
        {bgItemGroupcode: "D02", bsItemGroupcodeS: "N", bsName: "보통"},
        {bgItemGroupcode: "D02", bsItemGroupcodeS: "L", bsName: "라지"},
    ],
    addCostData: {
        bcVipDcRt: "30",
        bcVvipDcRt: "50",
        bcHighRt: "150",
        bcPremiumRt: "250",
        bcChildRt: "80",
        bcPressed: "2000",
        bcWhitening: "1000",
        bcPollution1: "2000",
        bcPollution2: "3500",
        bcPollution3: "6000",
        bcPollution4: "7500",
        bcPollution5: "10000",
        bcStarch: "3000",
        bcWaterRepellent: "7000",
    },
    repairListData: [
        {baName: "기장", baRemark: "기장비고"},
        {baName: "허리", baRemark: "허리허리"},
        {baName: "소매", baRemark: "소매매소"},
    ],
    addAmountData: [
        {baName: "오점", baRemark: "오점점오"},
        {baName: "황변제거", baRemark: "황변비고"},
        {baName: "표백", baRemark: "표백비고"},
    ],
}

/* 가상키보드 사용을 위해 */
let vkey;
let vkeyProp = [];
let vkeyTargetId = ["searchCustomerField", "fdTag", "fdRemark", "BBB", "CCC"];

vkeyProp[0] = {
    title : "고객 검색",
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
    title : "",
}

vkeyProp[4] = {
    title : "",
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
    "grid_requestList", "grid_customerList", "grid_tempSaveList"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/a", "/api/b"
]

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/a", "/api/b"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "fdTag",
        headerText: "택번호",
    }, {
        dataField: "sumName",
        headerText: "상품명",
    }, {
        dataField: "sumProcess",
        headerText: "처리내용",
    }, {
        dataField: "fdNormalAmt",
        headerText: "정상금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdRepairAmt",
        headerText: "수선금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdAdd1Amt",
        headerText: "추가금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdDiscountAmt",
        headerText: "할인금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdQty",
        headerText: "수량",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdRequestAmt",
        headerText: "접수금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return (item.fdNormalAmt + item.fdRepairAmt + item.fdAdd1Amt - item.fdDiscountAmt) * item.fdQty | 0;
        }
    }, {
        dataField: "fdColor",
        headerText: "색",
        style: "colorColumn",
        renderer : {
            type : "TemplateRenderer",
        },
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return `<div class="colorSquare" style="background-color: ${fdColorCode['C'+value]}"></div>`;
        }
    }, {
        dataField: "fdRemark",
        headerText: "특이사항",
    }, {
        dataField: "frEstimateDt",
        headerText: "출고예정일",
        dataType: "date",
        formatString: "yyyy-mm-dd",
    }, {
        dataField: "",
        headerText: "수정",
        renderer : {
            type: "ButtonRenderer",
            labelText: "수정",
            onClick: onModifyOrder,
        }
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
    enableColumnResize : false,
    showStateColumn : true,
    enableFilter : true,
    showRowCheckColumn : true,
};

gridColumnLayout[1] = [
    {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "전화번호",
    }, {
        dataField: "bcAddress",
        headerText: "주소",
    },
];

gridProp[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false,
    enableFilter : true,
};

gridColumnLayout[2] = [
    {
        dataField: "frInsertDt",
        headerText: "접수시간",
    }, {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "전화번호",
    }, {
        dataField: "",
        headerText: "삭제",
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false,
    showStateColumn : true,
    enableFilter : true,
};

function onShowVKeyboard(num) {
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

    /*
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
    */
}

function onSearchCustomer() {
    const params = {searchType : $("#searchCustomerType").val(),
            searchString : $("#searchCustomerField").val()};
    CommonUI.ajax("/api/user/customerInfo", "GET", params, function (req) {
       const items = req.sendData.gridListData;

        $("#searchCustomerType").val(0);
        $("#searchCustomerField").val("");
       if(items.length === 1) {
           selectedCustomer = items[0];
           onPutCustomer(selectedCustomer);
       }else if(items.length > 1) {
           AUIGrid.setGridData(gridId[1], items);
           // 고객선택창 열기
       }
    });
}

function onSelectCustomer() {
    selectedCustomer = AUIGrid.getSelectedRows(gridId[1]);
    if(selectedCustomer.length) {
        onPutCustomer(selectedCustomer[0]);
        // 고객선택창 닫기 추가
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function onPutCustomer(selectedCustomer) {
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
        "propensity__star propensity__star--" + selectedCustomer.bcValuation);
    $("#bcAddress").html(selectedCustomer.bcAddress);
    $("#bcHp").html(CommonUI.onPhoneNumChange(selectedCustomer.bcHp));
    $("#bcRemark").html(selectedCustomer.bcRemark);
    // 최근방문일

    AUIGrid.clearGridData(gridId[0]);
    alertSuccess("성함 : " + selectedCustomer.bcName + " 님");
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
    setBiItemList("N");

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
    currentRequest.fdPollution = parseInt(initialData.addCostData["bcPollution"+currentRequest.fdPollutionLevel]) | 0;

    currentRequest.fdAdd1Amt = currentRequest.fdPressed + currentRequest.fdWhitening + currentRequest.fdWaterRepellent
            + currentRequest.fdStarch + currentRequest.fdPollution;

    currentRequest.fdNormalAmt = currentRequest.fdOriginAmt * gradePrice[currentRequest.fdPriceGrade] / 100;
    currentRequest.fdRequestAmt = (currentRequest.fdNormalAmt + currentRequest.fdAdd1Amt + currentRequest.fdRepairAmt) * (100 - gradeDiscount[currentRequest.fdDiscountGrade]) / 100;
    currentRequest.fdDiscountAmt = currentRequest.fdNormalAmt + currentRequest.fdAdd1Amt + currentRequest.fdRepairAmt - currentRequest.fdRequestAmt;

    if($("#fdRetry").is(":checked")) {
        currentRequest.fdRetryYn = "Y";
        currentRequest.fdNormalAmt = 0;
        currentRequest.fdAdd1Amt = 0;
        currentRequest.fdDiscountAmt = 0;
        currentRequest.fdRequestAmt = 0;
    }else{
        currentRequest.fdRetryYn = "N";
    }

    $("#fdNormalAmt").html(currentRequest.fdNormalAmt.toLocaleString());
    $("#fdAdd1Amt").html(currentRequest.fdAdd1Amt.toLocaleString());
    $("#fdDiscountAmt").html(currentRequest.fdDiscountAmt.toLocaleString());
    $("#sumAmt").html(currentRequest.fdRequestAmt.toLocaleString());
}


/* 웹 카메라와 촬영 작업중 */
async function onPopTakePicture() {

    // const cameraList = document.getElementById("cameraList"); 복수 카메라를 사용할 경우 해제하여 작업
    const stream = await navigator.mediaDevices.getUserMedia({audio: false,
        video: {
            width: {ideal: 4096},
            height: {ideal: 2160}
        }
    });
    /* 복수 카메라를 사용할 경우 해제하여 작업
    let videoInput = [];
    await navigator.mediaDevices.enumerateDevices().then(devices => {
        for(let i = 0; i < devices.length; i++) {
            if(devices[i].kind === "videoinput") {
                videoInput.push({
                    label : devices[i].label,
                    deviceId : devices[i].deviceId
                });
            }
        }
    });
    videoInput.forEach(e => {
        const option = document.createElement('option');
        option.value = e.deviceId;
        option.text = e.label;
        cameraList.appendChild(option);
    });
    */
    const screen = document.getElementById("cameraScreen");
    screen.srcObject = stream;
}

/* 복수 카메라를 사용할 경우 해제하여 작업
async function onChangeCamera(deviceId) {
    const stream = await navigator.mediaDevices.getUserMedia({audio: false, video: {
            deviceId : deviceId ? {exact : deviceId} : undefined,
            width: { ideal: 4096 },
            height: { ideal: 2160 }
        }});
    const screen = document.getElementById("cameraScreen");
    screen.srcObject = stream;
}
*/


function onTakePicture() {
    const video = document.getElementById("cameraScreen");
    const canvas = document.getElementById('cameraCanvas');
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    const context = canvas.getContext('2d');
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    const takenPic = canvas.toDataURL('image/jpeg', 1.0);
    const file = dataURLtoFile(takenPic,'camera.jpg');

    const data = {
        file : file,
    }
    console.log(data);
    /*
    CommonUI.ajaxjson("/api/user/", data, function(){

    });
    */
}

function dataURLtoFile(dataurl, filename) {

    var arr = dataurl.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);

    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, {type:mime});
}

function toggleBottom() {
    var element = document.getElementsByClassName("regist__bottom");
    element[0].classList.toggle("active");

    var grid = document.getElementById("grid_requestList");
    grid.classList.toggle("active");

    AUIGrid.resize(gridId[0]);
}

function onAddOrder() {
    const bgName = {
        N: "", L: "롱", S: "숏"
    }
    /*
    const colorName = {
        C00: "없음", C01: "흰색", C02: "검정", C03: "회색", C04: "빨강", C05: "주황",
        C06: "노랑", C07: "초록", C08: "파랑", C09: "남색", C10: "보라", C11: "핑크"
    }
    */
    currentRequest.sumName = bgName[selectedLaundry.bsCode] + " 소재명 " +
        $("#bgItemList button[value=" + selectedLaundry.bgCode + "] span").html();


    currentRequest.fdColor = $("input[name='fdColor']:checked").val();
    //item.fdColorName = colorName["C" + item.fdColor];
    currentRequest.fdPattern = $("input[name='fdPattern']:checked").val();
    currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();
    currentRequest.fdRemark = $("#fdRemark").val();

    /* 상세한 사항이 정해지면 수정할 것 */
    currentRequest.urgent = $("input[name='urgent']:checked").val();
    currentRequest.etcNone = $("input[name='etcNone']").is(":checked") ? "Y" : "N";
    currentRequest.etc1 = $("input[name='etc1']").is(":checked") ? "Y" : "N";
    currentRequest.etc2 = $("input[name='etc2']").is(":checked") ? "Y" : "N";
    currentRequest.etc3 = $("input[name='etc3']").is(":checked") ? "Y" : "N";
    currentRequest.etc4 = $("input[name='etc4']").is(":checked") ? "Y" : "N";

    console.log(currentRequest);
    if(currentRequest._$uid) {
        console.log("update");
        AUIGrid.updateRowsById(gridId[0], currentRequest);
        AUIGrid.refresh(gridId[0]);
    }else{
        console.log("add");
        AUIGrid.addRow(gridId[0], currentRequest, "last");
    }

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
    $("input[name='urgent']").first().prop("checked", true);
    $(".choice input[type='checkbox']").prop("checked", false);
    $("input[name='etcNone']").first().prop("checked", true);
    $("#fdRemark").val("");

    $("#addProductPopChild").parents('.pop').removeClass('active');
}

function onModifyOrder(event) {
    currentRequest = event.item;
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

    /* currentRequest의 각 벨류값에 따라 화면의 라디오 세팅을 구성한다. */

    $('#productPop').addClass('active');
}

function additionalProcess(id) {
    switch (id) {
        case "" :
            break;
    }
}

