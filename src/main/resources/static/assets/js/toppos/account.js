
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
    rowNumHeaderText : "순번"
};

targetDiv = [
    "grid_branchList", "grid_franchList"
];

columnLayout[0] = [
    {
        dataField: "brName",
        headerText: "지사명",
    }, {
        dataField: "brContract",
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
        dataField: "frName",
        headerText: "가맹점명",
    }, {
        dataField: "frContract",
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
    for (const i in columnLayout) {
        gridID[i] = AUIGrid.create(targetDiv[i], columnLayout[i], gridOption);
    }

    // /* <=========================> */
    // /* 각 그리드의 url */
    // const gridUrl = [
    //     "/api/head/branohList", "/api/head/franohiseList"
    // ]
    //
    // for (let i=0; i<2; i++) {
    //     setListData(gridUrl[i], i);
    // }

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
    $('#branch_popup').removeClass('open');;
}

// 가맹점 선택 팝업닫기
function franchiseClose(){
    // console.log("가맹점선택 팝업닫기");
    $('#franchise_popup').removeClass('open');;
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