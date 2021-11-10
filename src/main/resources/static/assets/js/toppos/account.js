
$(function() {
    accountList(); // 사용자리스트 자동조회
    createAccountGrid(columnLayout, gridOption);

    AUIGrid.bind(gridID[0], "cellClick", function (e) {
        $("#brCode").val(e.item.brCode);
        $('#branch_popup').removeClass('open');
    });

    AUIGrid.bind(gridID[1], "cellClick", function (e) {
        $("#frCode").val(e.item.frCode);
        $('#franchise_popup').removeClass('open');;
    });

});

// 그리드 관련 시작
// +++++++++++++++++++++++++++++++++++++//
let gridID = [];
let targetDiv = [];
let gridData = [];
let columnLayout = [];

let gridOption = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false
};

targetDiv = [
    "grid_branchList", "grid_franchList"
];

accountColumnLayout = [
    {
        dataField: "userid",
        headerText: "아이디",
    }, {
        dataField: "username",
        headerText: "이름",
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
    }, {
        dataField: "useremail",
        headerText: "이메일",
    }, {
        dataField: "userremark",
        headerText: "특이사항",
    },
];

columnLayout[0] = [
    {
        dataField: "brCode",
        headerText: "지사코드",
    }, {
        dataField: "brName",
        headerText: "지사명",
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

columnLayout[1] = [
    {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
        dataField: "frName",
        headerText: "가맹점명",
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

function createAccountGrid(columnLayout, gridOption) {

    AUIGrid.create("grid_accountList", accountColumnLayout, gridOption);

    for (const i in columnLayout) {
        gridID[i] = AUIGrid.create(targetDiv[i], columnLayout[i], gridOption);
    }

    /* <=========================> */
    /* 각 그리드의 url */
    // const gridUrl = [
    //     "/api/head/branchList", "/api/head/franchiseList"
    // ]

    for (let i=0; i<2; i++) {
        if(i===0){
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
    popupListAjax(url,numOfGrid,params)
}

function frPopList(numOfGrid){
    const url = "/api/head/franchiseList";
    const params = {
        frName: $("#pop_s_frName").val(),
        frCode: $("#pop_s_frCode").val(),
        frContractState: $("#pop_s_frContractState").val()
    };
    popupListAjax(url,numOfGrid,params)
}

function popupListAjax(url,numOfGrid,params){
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });
    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        data: params,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            gridData[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(gridID[numOfGrid], gridData[numOfGrid]);
            AUIGrid.resize(gridID[numOfGrid], 660, 480);
        }
    });
}

// +++++++++++++++++++++++++++++++++++++//
// 그리드 관련 끝

// 지사선택 함수
function brListPop(){
    // console.log("지사선택 팝업열기");
    $('#branch_popup').addClass('open');
}

// 가맹점선택 함수
function frListPop(){
    // console.log("가맹점선택 팝업열기");
    $('#franchise_popup').addClass('open');
}

// 지사 점 팝업닫기
function branchClose(){
    // console.log("지사선택 팝업닫기");
    $('#branch_popup').removeClass('open');
}

// 가맹점 선택 팝업닫기
function franchiseClose(){
    // console.log("가맹점선택 팝업닫기");
    $('#franchise_popup').removeClass('open');
}



// 지사, 가맹점 코드 중복확인 체크함수 - 완
function useridOverlap(){
    console.log("사용자 아이디 중복확인");

    const $userid = $("#userid").val();
    if($userid==="" || $userid===undefined){
        alertCaution("아이디를 입력해주세요.",1);
        return false;
    }

    let url = "/api/head/useridOverlap";
    let params = {
        userid: $userid
    };

    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization')); });
    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        data: params,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            if (req.status === 200) {
                $("#useridChecked").val("1");
                console.log("중복환인 완료");
                alertSuccess("사용할 수 있는  아이디입니다.");
            }else{
                if (req.err_msg2 === null) {
                    alertCaution(req.err_msg, 1);
                }else{
                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                }
            }
        }
    });
}

// 사용자 등록 - 완
function accountSave(){

    const $useridCheckedr = $("#useridCheckedr").val();
    if($useridCheckedr==="0"){
        alertCaution("유저아이디 중복확인을 해주세요.",1);
        return false;
    }

    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization')); });

    const formData = new FormData(document.getElementById('accountFormData'));

    let url = "/api/head/accountSave";

    $.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            if (req.status === 200) {
                console.log("저장 완료");
                alertSuccess("사용자 저장완료");
            }else {
                if (req.err_msg2 === null) {
                    alertCaution(req.err_msg, 1);
                } else {
                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                }
            }
        }
    });
}

// 사용자 조회 함수호출
function accountList(){

    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization')); });

    let url = "/api/head/accountList";
    let params = {
        s_userid: $("#s_userid").val(),
        s_username: $("#s_username").val(),
        s_role: $("#s_role").val(),
        s_frCode: $("#s_frCode").val(),
        s_brCode: $("#s_brCode").val()
    };

    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        data: params,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            if (req.status === 200) {
                AUIGrid.setGridData("grid_accountList", req.sendData.gridListData);
            }else {
                if (req.err_msg2 === null) {
                    alertCaution(req.err_msg, 1);
                } else {
                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                }
            }
        }
    });

}