
$(function() {

    // 김민규 12.07 추가 페이지 들어가면 현재 로그인한 가맹점의 고객리스트를 자동 조회한다.
    onSearchCustomer();

    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<0; i++) {
        setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
    });

    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');

        e.preventDefault()
    })

    vkey = new VKeyboard();
});
/* 가상키보드 사용 */
let vkey;

let item;

/* 고객 등급에 대한 이름을 정의 */
const bcGradeName = {
    "01" : "일반",
    "02" : "VIP",
    "03" : "VVIP",
}

/* 가상키보드 입력 대상이 되는 텍스트 필드나 텍스트 에어리어 */
let vkeyTargetId = ["bcName", "bcHp", "bcAddress", "bcRemark"];

let vkeyProp = [];

vkeyProp[0] = {
    title : "고객명",
}

vkeyProp[1] = { // 키패드로 변경 필요
    title : "휴대폰(숫자만 입력해 주세요)",
    postprocess : function(text) {
        return text.replace(/[^0-9]/g, "");
    },
    callback : onHpChange,
}

vkeyProp[2] = {
    title : "주소",
}

vkeyProp[3] = {
    title : "특이사항",
}

/* 호출하여 목표 가상 키보드 띄우기, 0번부터 배열 순서대로 */
function openVKeyboard(num) {
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
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
    "/api/a"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "휴대폰",
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.onPhoneNumChange(value);
        }
    }, {
        dataField: "bcAddress",
        headerText: "주소",
    }, {
        dataField: "bcGrade",
        headerText: "등급",
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return bcGradeName[value];
        },
    }, {
        dataField: "",
        headerText: "미수금",
    }, {
        dataField: "",
        headerText: "적립금",
    }, {
        dataField: "bcMessageAgree",
        headerText: "SMS수신",
    }, {
        dataField: "bcAge",
        headerText: "연령/생일",
        labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
            return value + "대/" + item.bcBirthday.substr(0, 4) + "-" +
                item.bcBirthday.substr(4, 2) + "-" + item.bcBirthday.substr(6, 2);
        },
    }, {
        dataField: "insertDateTime",
        headerText: "가입일자",
    }, {
        dataField: "bcQuitDate",
        headerText: "탈퇴일",
    }, {
        dataField: "",
        headerText: "수정",
        renderer : {
            type: "ButtonRenderer",
            labelText: "수정",
            onClick: onModifyCustomer,
        }
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    enableColumnResize : false,
    enableFilter : true,
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

function onModifyCustomer(event) {
    item = event.item;
    $("#bcId").val(item.bcId);
    $("#bcName").val(item.bcName);
    $("#bcHp").val(CommonUI.onPhoneNumChange(item.bcHp));
    $("input:radio[name='bcSex']:radio[value='" + item.bcSex + "']")
        .prop('checked', true);
    $("#bcAddress").val(item.bcAddress);
    $("#bcBirthYYYY").val(item.bcBirthday.substr(0, 4));
    $("#bcBirthMM").val(item.bcBirthday.substr(4, 2));
    $("#bcBirthDD").val(item.bcBirthday.substr(6, 2));
    $("#bcAge").val(item.bcAge);
    $("#bcGrade").val(bcGradeName[item.bcGrade]);
    $("#bcValuation").val(item.bcValuation);
    $("input:radio[name='bcQuitYn']:radio[value='" + item.bcQuitYn + "']")
            .prop('checked', true);
    $("input:radio[name='bcMessageAgree']:radio[value='" + item.bcMessageAgree + "']")
        .prop('checked', true);
    $("#bcAgreeType").val(item.bcAgreeType);
    $("#bcRemark").val(item.bcRemark);
    $("#signImage").attr("src", item.bcSignImage);

    $('#showCustomerDetailPop').addClass('active');
    console.log(item);
}

/* 전화번호 입력을 위한 유효성 검사 */
function onHpChange () {
    const element = document.getElementById("bcHp");
    let phoneNumber = element.value;
    element.value = CommonUI.onPhoneNumChange(phoneNumber);
}

/* 입력된 폼 정보 저장 */
function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val()+$("#bcBirthMM").val()+$("#bcBirthDD").val();

    if(!CommonUI.regularValidator(birthday, "dateExist") && birthday !== "") {
        alertCaution("존재할 수 없는 생년월일 입니다.", 1);
        return false;
    }

    formData.set("bcHp", formData.get("bcHp").replace(/[^0-9]/g, ""));
    formData.append("bcBirthday", birthday);
    formData.append("bcGrade", item.bcGrade);
    formData.append("bcSignImage", $("#signImage").attr("src"));

    const url = "/api/user/customerSave";
    CommonUI.ajax(url, "POST", formData, function (){
        alertSuccess("고객 데이터 저장 성공");
        onSearchCustomer();
        // 완료 후 팝업닫기 추가하기 to.성낙원
    });
}

/* 회원정보 변경을 취소 */
function cancelRegister() {
    // 팝업 닫기 추가
}

/* 서명을 요청할 때 고객측의 모니터에 서명하도록 뜬다. */
function requestSign() {
    const protocol = location.protocol;
    const hostName = location.hostname;
    const port = location.port;

    cAPI.approvalCall(protocol +'//'+hostName+ ':' + port + '/user/sign');

    // $("#resultmsg").text(": 승인중 메세지- 고객이 서명중입니다. ------전체화면으로 가리기 ")

    const maskHeight = $(document).height();
    const maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#windowMask').css({'width':maskWidth,'height':maskHeight}).show();
    $('#mask').show();
}