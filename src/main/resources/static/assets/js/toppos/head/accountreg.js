
$(function() {
    accountList(); // 사용자리스트 자동조회
    createAccountGrid(gridColumnLayout, gridProp); // 배열의 각 그리드 함께 생성

    /* 0번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        setFieldData(0, e.item);
        restrictCodeSelection(e.item.roleCode);
        changeUseridStatus(true);
        $password.attr("disabled", true);
    });

    AUIGrid.bind(gridId[1], "cellDoubleClick", function (e) {
        $("#brCode").val(e.item.brCode);
        branchClose();
    });

    AUIGrid.bind(gridId[2], "cellDoubleClick", function (e) {
        $("#frCode").val(e.item.frCode);
        franchiseClose();
    });

    /* 권한을 바꿀 때 가맹점 코드나, 지사 코드를 선택 가능, 불가능으로 바꾸어 준다. */
    $("#role").on("change", function (e){
        const selectedCode = $(e.target).find("option:selected").val();
        restrictCodeSelection(selectedCode);
    });

    $useridChecked = $("#useridChecked");
    $useridCheckedBtn = $("#useridCheckedBtn");
    $password = $("#password");
});

/* 상태 제어에 사용할 객체들 */
let $useridChecked;
let $useridCheckedBtn;
let $password;

// 그리드 관련 시작
// +++++++++++++++++++++++++++++++++++++//
let gridId = [];
let gridTargetDiv = [];
let gridData = [];
let gridColumnLayout = [];
let gridProp = [];

/* 그리드를 뿌릴 대상 id */
gridTargetDiv = [
    "grid_accountList", "grid_branchList", "grid_franchList"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "userid",
        headerText: "아이디",
        style: "grid_textalign_left",
    }, {
        dataField: "username",
        headerText: "이름",
        style: "grid_textalign_left",
    }, {
        dataField: "role",
        headerText: "권한",
    }, {
        dataField: "frCode",
        headerText:"가맹점코드",
    }, {
        dataField: "brCode",
        headerText:"지사코드",
    }, {
        dataField: "usertel",
        headerText: "전화번호",
        labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
            return CommonUI.formatTel(value);
        },
    }, {
        dataField: "useremail",
        headerText: "이메일",
        style: "grid_textalign_left",
    }, {
        dataField: "userremark",
        headerText: "특이사항",
        style: "grid_textalign_left",
    },
];

/* 0번 그리드의 프로퍼티(옵션) */
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[1] = [
    {
        dataField: "brCode",
        headerText: "지사코드",
    }, {
        dataField: "brName",
        headerText: "지사명",
        style: "grid_textalign_left",
    }, {
        dataField: "brContractDt",
        headerText: "계약일자",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "brContractStateValue",
        headerText: "계약상태",
    },
];

gridProp[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : true,
    width : 660,
    height : 400,
};

gridColumnLayout[2] = [
    {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
        dataField: "frName",
        headerText: "가맹점명",
        style: "grid_textalign_left",
    }, {
        dataField: "frContractDt",
        headerText: "계약시작일",
    },{
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "frContractStateValue",
        headerText: "계약상태",
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : true,
    width : 660,
    height : 400,
};

function createAccountGrid(gridColumnLayout, gridProp) {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }

    /* <=========================> */
    /* 각 그리드의 url */
    // const gridUrl = [
    //     "/api/head/branchList", "/api/head/franchiseList"
    // ]

    for (let i=1; i<3; i++) {
        if(i===1){
            brPopList(i);
        }else{
            frPopList(i);
        }
    }

}

function brPopList(numOfGrid){
    const url = "/api/head/branchList";
    const params = {
        brName: $("#pop_s_brName").val(),
        brCode: $("#pop_s_brCode").val(),
        brContractState: $("#pop_s_brContractState").val()
    };
    popupListAjax(url,numOfGrid,params);
}

function frPopList(numOfGrid){
    const url = "/api/head/franchiseList";
    const params = {
        frName: $("#pop_s_frName").val(),
        frCode: $("#pop_s_frCode").val(),
        frContractState: $("#pop_s_frContractState").val()
    };
    popupListAjax(url,numOfGrid,params);
}

function popupListAjax(url,numOfGrid,params){
    CommonUI.ajax(url, "GET", params, function(req){
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}

// +++++++++++++++++++++++++++++++++++++//
// 그리드 관련 끝

// 지사선택 함수
function brListPop(){
    // console.log("지사선택 팝업열기");
    AUIGrid.clearFilterAll(gridId[1]);
    $('#branch_popup').addClass('active');
    
    AUIGrid.resize(gridId[1]);
}

// 가맹점선택 함수
function frListPop(){
    // console.log("가맹점선택 팝업열기");
    AUIGrid.clearFilterAll(gridId[2]);
    $('#franchise_popup').addClass('active');
    
    AUIGrid.resize(gridId[2]);
}

// 지사 점 팝업닫기
function branchClose(){
    // console.log("지사선택 팝업닫기");
    $('#branch_popup').removeClass('active');
}

// 가맹점 선택 팝업닫기
function franchiseClose(){
    // console.log("가맹점선택 팝업닫기");
    $('#franchise_popup').removeClass('active');
}


// 지사, 가맹점 코드 중복확인 체크함수 - 완
function useridOverlap(){

    const $userid = $("#userid").val();
    if($userid==="" || $userid===undefined){
        alertCaution("아이디를 입력해주세요.",1);
        return false;
    }

    let url = "/api/head/useridOverlap";
    let params = {
        userid: $userid
    };

    CommonUI.ajax(url, "GET", params, function (req){
        $useridChecked.val("1");
        $useridCheckedBtn.attr("disabled", true);
        alertSuccess("사용할 수 있는  아이디입니다.");
    });
}

/* userid를 변경하면 아이디 중복 체크가 안된 상태로 변경 */
function onChangeUserId() {
    if(!$("#userid").prop("readonly")) {
        $useridChecked.val("0");
        $useridCheckedBtn.attr("disabled", false);
    }
}

// 사용자 등록 - 완
function accountSave(){

    if($useridChecked.val() === "0"){
        alertCaution("유저아이디 중복확인을 해주세요.",1);
        return false;
    }

    /* //이메일 유효성 검사. 제거요청이 있어 주석화
    if(!CommonUI.regularValidator($("#useremail").val(), "email")) {
        alertCaution("이메일을 잘 입력 해주세요.",1);
        $("#useremail").trigger("focus");
        return false;
    }
    */
    if($("#username").val() === "") {
        alertCaution("이름을 입력 해주세요.",1);
        return false;
    }

    if($("#usertel").val() === "") {
        alertCaution("전화번호를 입력 해주세요.",1);
        return false;
    }


    const userRole = $("#role").val();
    if(userRole === "") {
        alertCaution("권한을 선택 해주세요.",1);
        return false;
    }
    /* 권한에 따라 지사나 가맹점 선택을 했는지 검사 */
    if((userRole === "ROLE_MANAGER" || userRole === "ROLE_NORMAL") && $("#brCode").val() === "" ) {
        alertCaution("지사를 선택 해주세요.",1);
        return false;
    }else if(userRole === "ROLE_USER" && $("#frCode").val() === "" ) {
        alertCaution("가맹점을 선택 해주세요",1);
        return false;
    }

    const formData = new FormData(document.getElementById('accountFormData'));
    formData.set("usertel", formData.get("usertel").numString());

    let url = "/api/head/accountSave";

    CommonUI.ajax(url, "POST", formData, function (req){
        /* 저장작업을 하면 사용자 리스트를 리로드 한다. */
        accountList();
        createNewPost(0);
        alertSuccess("사용자 저장완료");
    });
}

function accountRemove() {
    alertCheck("삭제된 사용자는 복구가 불가능 합니다.<br>정말 삭제하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        let targetUser = {};
        if(AUIGrid.getSelectedItems(gridId[0])) {
            targetUser = {
                userid : AUIGrid.getSelectedItems(gridId[0])[0].item.userid
            }
        }
        CommonUI.ajax("/api/head/accountDelete", "PARAM", targetUser, function() {
            createNewPost(0);
            AUIGrid.clearGridData(gridId[0]);
            accountList();
        });
        $('#popupId').remove();
    });
}


// 사용자 조회 함수호출
function accountList(){
    let url = "/api/head/accountList";
    let params = {
        s_userid: $("#s_userid").val(),
        s_username: $("#s_username").val(),
        s_role: $("#s_role").val(),
        s_frCode: $("#s_frCode").val(),
        s_brCode: $("#s_brCode").val()
    };
    CommonUI.ajax(url, "GET", params, function(req){
        AUIGrid.setGridData("grid_accountList", req.sendData.gridListData);
    });
}

/* 지정된 입력필드에 해당되는 item(json내용)을 뿌린다 (보통 그리드내의 row를 클릭하거나, 필드를 초기화시킬 때 씀) */
function setFieldData(numOfGrid, item) {
    const frCode = item.frCode !== "해당안됨" ? item.frCode : "";
    const brCode = item.brCode !== "해당안됨" ? item.brCode : "";

    switch (numOfGrid) {
        case 0 :
            $("#userid").val(item.userid);
            $("#username").val(item.username);
            $("#password").val(item.password);
            $("#role").val(item.roleCode);
            $("#usertel").val(CommonUI.formatTel(item.usertel));
            $("#useremail").val(item.useremail);
            $("#frCode").val(frCode);
            $("#brCode").val(brCode);
            $("#userremark").val(item.userremark);
            break;

        case 1 :
            break;
    }
}

/* 사용자 신규 등록을 위해 필드를 비우고 입력 절차를 위한 상태를 초기화 시킨다. */
function createNewPost(numOfGrid) {
    setFieldData(numOfGrid, {userremark : "", useridChecked : "0", role : ""});
    switch (numOfGrid) {
        case 0 :
            restrictCodeSelection("");
            changeUseridStatus(false);
            $("#userid").trigger("focus");
            $password.attr("disabled", false);
            break;
    }
}

/* 권한 select 가 다른 옵션으로 바뀔 때 상태 필드들을 바꾸기 위함. */
function restrictCodeSelection(selectedCode) {
    const  $frCode =  $("#frCode");
    const  $frCodeBtn =  $("#frCodeBtn");
    const  $brCode =  $("#brCode");
    const  $brCodeBtn =  $("#brCodeBtn");

    switch (selectedCode) {
        case "ROLE_USER" :
            $frCode.attr("disabled", false);
            $frCodeBtn.attr("disabled", false);
            $brCode.val("");
            $brCode.attr("disabled", true);
            $brCodeBtn.attr("disabled", true);
            break;
        case "ROLE_MANAGER" :
        case "ROLE_NORMAL" :
            $frCode.val("");
            $frCode.attr("disabled", true);
            $frCodeBtn.attr("disabled", true);
            $brCode.attr("disabled", false);
            $brCodeBtn.attr("disabled", false);
            break;
        default :
            $frCode.val("");
            $frCode.attr("disabled", true);
            $frCodeBtn.attr("disabled", true);
            $brCode.val("");
            $brCode.attr("disabled", true);
            $brCodeBtn.attr("disabled", true);
            break;
    }
}

/* 이미 생성되어 있는 유저들의 경우 아이디를 바꿀 수 있도록 하면 안되므로 */
function changeUseridStatus (isDisable) {
    const checked = isDisable ? "1" : "0";
    $("#userid").attr("readonly", isDisable);
    $useridCheckedBtn.attr("disabled", isDisable);
    $useridChecked.val(checked);
}

/* 사용자 리스트 조회 필터링 type = 1 사용자 리스트 조회 필터링, type = 2 필터링 초기화*/
function filterAccountList(type) {

    switch (type) {
        case 1 :
            AUIGrid.clearGridData(gridId[0]);
            accountList();
            break;
        case 2 :
            $("#s_userid").val("");
            $("#s_username").val("");
            $("#s_role").val("");
            $("#s_frCode").val("");
            $("#s_brCode").val("");
            accountList();
            break;
    }

}

/* 전화번호 입력을 위한 유효성 검사 */
function formatTel(element) {
    let phoneNumber = element.value;
    element.value = CommonUI.formatTel(phoneNumber);
}

function branchSelect() {
    const selectedItem = AUIGrid.getSelectedItems(gridId[1])[0];
    if(selectedItem) {
        $("#brCode").val(selectedItem.item.brCode);
        branchClose();
    } else {
        alertCaution("지사를 선택해 주세요", 1);
    }
}

function franchiseSelect() {
    const selectedItem = AUIGrid.getSelectedItems(gridId[2])[0];
    if(selectedItem) {
        $("#frCode").val(selectedItem.item.frCode);
        franchiseClose();
    } else {
        alertCaution("가맹점을 선택해 주세요", 1);
    }
}