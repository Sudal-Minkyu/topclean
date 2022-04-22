$(function() {

    // 김민규 12.07 추가 페이지 들어가면 현재 로그인한 가맹점의 고객리스트를 자동 조회한다.
    // onSearchCustomer();

    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
    });

    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');

        e.preventDefault()
    })

    vkey = new VKeyboard();

    getBoilerPlate();
});
/* 가상키보드 사용 */
let vkey;

let tempSaveMoneyItem;

/* 고객 등급에 대한 이름을 정의 */
const bcGradeName = {
    "01" : "일반",
    "02" : "VIP",
    "03" : "VVIP",
}

/* 가상키보드 입력 대상이 되는 텍스트 필드나 텍스트 에어리어 */
let vkeyTargetId = ["bcName", "bcAddress", "bcRemark", "searchCustomerField"];

let vkeyProp = [];

vkeyProp[0] = {
    title : "고객명",
}

vkeyProp[1] = {
    title : "주소",
}

vkeyProp[2] = {
    title : "특이사항",
}

vkeyProp[3] = {
    title : "검색어 입력",
    
}

/* 가상키보드 입력 대상이 되는 텍스트 필드나 텍스트 에어리어 */
let vkeypadTargetId = ["addSaveMoney", "bcHp"];

/* 상용구 받아와서 특정 가상 키보드에 세팅하기 */
function getBoilerPlate() {
    const url = "/api/user/franchiseAddProcessList";
    CommonUI.ajax(url, "GET", {baType: "0"}, function (res) {
        let boilerPlate = [];
        const data = res.sendData.keyWordData;
        for({baName} of data) {
            boilerPlate.push(baName);
        }
        vkeyProp[1].boilerplate = boilerPlate;
        vkeyProp[2].boilerplate = boilerPlate;
    });
}

let vkeypadProp = [];

vkeypadProp[0] = {
    type: "plusminus",
    callback: insertIntoHtmlTag,
}

vkeypadProp[1] = { // 키패드로 변경 필요
    title : "휴대폰(숫자만 입력해 주세요)",
    midprocess: "tel",
    callback : onHpChange,
}

/* 호출하여 목표 가상 키보드 띄우기, 0번부터 배열 순서대로 */
function openVKeyboard(num) {
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function openVKeypad(num) {
    if (num === 0) {
        $("#tempKeypadField").val($("#" + vkeypadTargetId[num]).html());
        vkey.showKeypad("tempKeypadField", vkeypadProp[num]);
    } else {
        vkey.showKeypad(vkeypadTargetId[num], vkeypadProp[num]);
    }
}

function insertIntoHtmlTag() {
    $("#" + vkeypadTargetId[0]).html(parseInt($("#tempKeypadField").val()).toLocaleString());
    calculateResultSaveMoney();
}

function calculateResultSaveMoney() {
    const current = $("#currentSaveMoney").html().toInt();
    let add = $("#addSaveMoney").html().toInt();
    if((current + add) < 0) {
        alertCaution("최종 적립금이 0원 보다 작아질 수는 없습니다.<br>조정금액을 수정합니다.", 1);
        $("#addSaveMoney").html(current.toLocaleString());
        add = -current;
    }
    $("#resultSaveMoney").html((current + add).toLocaleString());
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
    "grid_customerList"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/user/customerList"
]

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/user/"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "bcName",
        headerText: "고객명",
        style: "grid_textalign_left",
        width: 80,
    }, {
        dataField: "bcHp",
        headerText: "휴대폰",
        width: 120,
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.formatTel(value);
        }
    }, {
        dataField: "bcAddress",
        headerText: "주소",
        style: "grid_textalign_left",
        width: 190,
    }, {
        dataField: "bcGrade",
        headerText: "등급",
        width: 60,
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return bcGradeName[value];
        },
    }, {
        dataField: "uncollectMoney",
        headerText: "미수금",
        style: "grid_textalign_right",
        dataType: "numeric",
        autoThousandSeparator: "true",

    }, {
        dataField: "saveMoney",
        headerText: "적립금",
        style: "grid_textalign_right",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "",
        headerText: "적립금 조정",
        width: 80,
        renderer : {
            type: "TemplateRenderer",
        },
        labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
            const template = `
                <button class="c-button c-button--supersmall" 
                    onclick="onModifySaveMoney(${rowIndex})">조정</button>
            `;
            return template;
        },
    }, {
        dataField: "bcMessageAgree",
        headerText: "SMS",
        width: 50,
    }, {
        dataField: "bcAge",
        headerText: "연령/생일",
        width: 140,
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return value + "대/" + item.bcBirthday.substr(0, 4) + "-" +
                item.bcBirthday.substr(4, 2) + "-" + item.bcBirthday.substr(6, 2);
        },
    },/*  {
        dataField: "bcWeddingAnniversary",
        headerText: "결혼기념일",
        width: 100,
        dataType: "date",
        formatString: "yyyy-mm-dd",
    }, */ {
        dataField: "insertDateTime",
        headerText: "가입일자",
        width: 100,
    }, {
        dataField: "bcQuitDate",
        headerText: "탈퇴일",
        width: 100,
    }, {
        dataField: "",
        headerText: "수정",
        width: 60,
        renderer : {
            type: "TemplateRenderer",
        },
        labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
            const template = `
                <button class="c-button c-button--solid  c-button--supersmall" 
                    onclick="onModifyCustomer(${rowIndex})">수정</button>
            `;
            return template;
        },
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
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


/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids() {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }
}

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid(numOfGrid, url) {
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}

// 고객리스트 조회
function onSearchCustomer() {
    const params = {searchType : $("#searchCustomerType").val(),
        searchString : $("#searchCustomerField").val()};
    CommonUI.ajax(gridCreateUrl[0], "GET", params, function (req) {
        const items = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[0], items);
    });
}

function onYearChange(selectedYear) {
    const currentYear = new Date().getFullYear();
    const currentAge = currentYear - selectedYear + 1;
    let resultAger = "";
    if(currentAge > 9 && currentAge < 100) {
        resultAger = Math.floor(currentAge/10) * 10 + "";
    }
    $("#bcAge").val(resultAger).prop("selected", true);
}

function onModifyCustomer(rowIndex) {
    const item = AUIGrid.getItemByRowIndex(gridId[0], rowIndex);
    $("#bcId").val(item.bcId);
    $("#bcName").val(item.bcName);
    $("#bcHp").val(CommonUI.formatTel(item.bcHp));
    $("input:radio[name='bcSex']:radio[value='" + item.bcSex + "']")
        .prop('checked', true);
    $("#bcAddress").val(item.bcAddress);
    if(item.bcBirthday) {
        $("#bcBirthYYYY").val(item.bcBirthday.substr(0, 4));
        $("#bcBirthMM").val(item.bcBirthday.substr(4, 2));
        $("#bcBirthDD").val(item.bcBirthday.substr(6, 2));
    }
    if(item.bcWeddingAnniversary) {
        $("#bcWeddingAnniversaryYYYY").val(item.bcWeddingAnniversary.substr(0, 4));
        $("#bcWeddingAnniversaryMM").val(item.bcWeddingAnniversary.substr(4, 2));
        $("#bcWeddingAnniversaryDD").val(item.bcWeddingAnniversary.substr(6, 2));
    }
    $("#bcAge").val(item.bcAge);
    $("#bcGrade").val(bcGradeName[item.bcGrade]);
    $("#bcGradeNo").val(item.bcGrade);
    $("#bcValuation").val(item.bcValuation);
    $("#bcSignImage").val(item.bcSignImage);
    $("input:radio[name='bcQuitYn']:radio[value='" + item.bcQuitYn + "']")
            .prop('checked', true);
    $("input:radio[name='bcMessageAgree']:radio[value='" + item.bcMessageAgree + "']")
        .prop('checked', true);
    /*$("#bcAgreeType").val(item.bcAgreeType);*/
    $("#bcAgreeTypeName").val(
        item.bcAgreeType === "1" ? "온라인" : "서면"
    );
    $("#bcRemark").val(item.bcRemark);
    const $signImage = $("#signImage");
    $signImage.attr("src", item.bcSignImage);
    if(item.bcSignImage !== null && item.bcSignImage !== "" /*&& item.bcAgreeType === "1"*/) {
        $signImage.show();
    }else{
        $signImage.hide();
    }

    $('#showCustomerDetailPop').addClass('active');
}

function onModifySaveMoney(rowIndex) {
    tempSaveMoneyItem = AUIGrid.getItemByRowIndex(gridId[0], rowIndex);
    const currentSaveMoney = tempSaveMoneyItem.saveMoney.toLocaleString();
    $("#addSaveMoney").html("0");
    $("#currentSaveMoney").html(currentSaveMoney);
    $("#resultSaveMoney").html(currentSaveMoney);
    $("#modifySaveMoneyPop").addClass("active");
}

function onUpdateSaveMoney() {
    const data = {
        bcId: tempSaveMoneyItem.bcId,
        controlMoney: $("#addSaveMoney").html().toInt(),
    }
    alertCheck("적립금을 " + data.controlMoney.toLocaleString() + "원 추가/삭감 합니다.<br>진행 하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function() {
        ajaxUpdateSaveMoney(data);
    });
    
}

function ajaxUpdateSaveMoney(data) {
    CommonUI.ajax("/api/user/customerSaveMoneyControl", "PARAM", data, function(res) {
        tempSaveMoneyItem.saveMoney = res.sendData.saveMoney;
        AUIGrid.updateRowsById(gridId[0], tempSaveMoneyItem);
        alertSuccess("적립금 조정이 완료되었습니다.");
        AUIGrid.resetUpdatedItems(gridId[0]);
        $("#modifySaveMoneyPop").removeClass("active");
    });
}


/* 전화번호 입력을 위한 유효성 검사 */
function onHpChange () {
    const element = document.getElementById("bcHp");
    element.value = CommonUI.formatTel(element.value);
}

/* 입력된 폼 정보 저장 */
function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val() + $("#bcBirthMM").val() + $("#bcBirthDD").val();
    const weddingAnniversary = $("#bcWeddingAnniversaryYYYY").val() + $("#bcWeddingAnniversaryMM").val() 
        + $("#bcWeddingAnniversaryDD").val();

    if(!$("#bcName").val()) {
        alertCaution("고객명을 입력해 주세요", 1);
        return false;
    }
    if($("#bcHp").val().length < 4) {
        alertCaution("휴대폰 번호를 입력해 주세요", 1);
        return false;
    }
    if(!CommonUI.regularValidator(birthday, "dateExist") && birthday !== "") {
        alertCaution("존재할 수 없는 생년월일 입니다", 1);
        return false;
    }

    if($("#bcAgreeType").val() === "1") {
        console.log("온라인 입니다.");
        if($("#bcSignImage").val().length) {
            formData.append("bcSignImage", $("#bcSignImage").val());
        }else{
            alertCaution("사인을 해주세요",1);
            return false;
        }
    }else{
        console.log("서면 입니다.");
    }

    formData.set("bcHp", formData.get("bcHp").numString());
    formData.append("bcBirthday", birthday);
    formData.append("bcWeddingAnniversary", weddingAnniversary);
    formData.append("bcGrade", $("#bcGradeNo").val());
    formData.append("bcSignImage", $("#signImage").attr("src"));

    const url = "/api/user/customerSave";
    CommonUI.ajax(url, "POST", formData, function () {
        alertCheck("고객 데이터 저장을 성공하였습니다.<br>해당 고객으로 바로 접수하시겟습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            $('#popupId').remove();
            const bcHp = formData.get("bcHp").numString();
            if(bcHp) {
                location.href = "./receiptreg?bchp=" + bcHp;
            }
        });
        onSearchCustomer();
    });
}

/* 회원정보 변경을 취소 */
function cancelRegister() {
    // 팝업 닫기 추가
}

function onAgreeTypeChange(type) {
    const $reqSign = $("#reqSign");
    const $signImage = $("#signImage");
    if(type === "1") {
        $reqSign.attr("disabled", false);
        if($signImage.attr("src")) $signImage.show();
    }else{
        $reqSign.attr("disabled", true);
        $signImage.hide();
    }
}

// base64를 Blob로 변환하는 함수
function b64toBlob(b64Data, contentType, sliceSize) {
    if( b64Data === "" || b64Data === undefined ) return null;
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    var byteCharacters = atob(b64Data);

    var byteArrays = [];

    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);
        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {type: contentType});
}

/* 서명을 요청할 때 고객측의 모니터에 서명하도록 뜬다. */
function requestSign() {
    try {
        const protocol = location.protocol;
        const hostName = location.hostname;
        const port = location.port;

        // TOPPOS 프로그램 내 함수 사용. 일반 브라우저에서는 오류
        cAPI.approvalCall(protocol + '//' + hostName + ':' + port + '/user/sign');
    }catch (e) {
        CommonUI.toppos.underTaker(e, "customerlist : 서명요청");
        return false;
    }
    // $("#resultmsg").text(": 승인중 메세지- 고객이 서명중입니다. ------전체화면으로 가리기 ");

    const maskHeight = $(document).height();
    const maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#windowMask').css({'width':maskWidth,'height':maskHeight}).show();
    $('#mask').show();
}

function resultFunction(msg){
    $("#windowMask").hide();
    $("#mask").hide();
    $("#resultmsg").text(msg);
    $("#bcSignImage").val(msg);
    document.getElementById('signImage').src = msg;
    document.getElementById('signImage').style.border = "2px solid black";
    $("#signImage").show();
}

function formatTel(num) {
    $("#bcHp").val(CommonUI.formatTel(num.value));
}