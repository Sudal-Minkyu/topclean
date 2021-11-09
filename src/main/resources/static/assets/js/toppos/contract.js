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
    "grid_contract_one", "grid_contract_two", "grid_contract_three", "grid_contract_four"
];


columnLayout[0] = [
    {
        dataField: "brName",
        headerText: "지사명",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    },
];

columnLayout[1] = [
    {
        dataField: "frName",
        headerText: "가맹점명",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "brName",
        headerText: "배정지사명",
    },
];

columnLayout[2] = [
    {
        dataField: "brName",
        headerText: "지사명",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    },
];

columnLayout[3] = [
    {
        dataField: "frName",
        headerText: "가맹점명",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "brName",
        headerText: "배정지사명",
    },
];


$(function () {

    createGrid(columnLayout, gridOption);

    const datePickerTargetIds = [
        "brContractDt", "brContractFromDt", "brContractToDt", "frContractDt",
        "frContractFromDt", "frContractToDt"
    ];

    /*
    * JqueyUI datepicker의 기간 A~B까지를 선택할 때 선택한 날짜에 따라 제한을 주기 위한 DOM id의 배열이다.
    * 배열 내 각 내부 배열은 [~부터의 제한 대상이 될 id, ~까지의 제한 대상이 될 id] 이다.
    * */
    const dateAToBTargetIds = [
        ["brContractFromDt", "brContractToDt"], ["frContractFromDt", "frContractToDt"]
    ];


    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.setDateAToBValidation(dateAToBTargetIds);

    AUIGrid.bind(gridID[0], "cellClick", function (e) {
        $("#brCode").val(e.item.brCode);
        $("#brName").val(e.item.brName);
        $("#brContractDt").val(e.item.brContractDt);
        $("#brContractFromDt").val(e.item.brContractFromDt);
        $("#brContractToDt").val(e.item.brContractToDt);
        $("#brContractState").val(e.item.brContractState);
        $("#brCarculateRateHq").val(e.item.brCarculateRateHq);
        $("#brCarculateRateBr").val(e.item.brCarculateRateBr);
        $("#brCarculateRateFr").val(e.item.brCarculateRateFr);
        $("#brRemark").html(e.item.brRemark);

    });

    AUIGrid.bind(gridID[1], "cellClick", function (e) {
        $("#frCode").val(e.item.frCode);
        $("#frName").val(e.item.frName);
        $("#frContractDt").val(e.item.frContractDt);
        $("#frContractFromDt").val(e.item.frContractFromDt);
        $("#frContractToDt").val(e.item.frContractToDt);
        $("#frContractState").val(e.item.frContractState);
        $("#frRemark").html(e.item.frRemark);
    });

});


function createGrid(columnLayout, gridOption) {
    for (const i in columnLayout) {
        gridID[i] = AUIGrid.create(targetDiv[i], columnLayout[i], gridOption);
    }

    /* <=========================> */
    /* 각 그리드의 url */
    const gridUrl = [
        "/api/head/branchList", "/api/head/franchiseList"
    ];

    for (let i=0; i<2; i++) {
    	setListData(gridUrl[i], i);
    }

}

function setListData(url, numOfGrid) {
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });
    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            gridData[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(gridID[numOfGrid], gridData[numOfGrid]);
            AUIGrid.setGridData(gridID[numOfGrid+2], gridData[numOfGrid]);
        }
    });
}

// 지사, 가맹점 코드 중복확인 체크함수
function codeOverlap(num){

    let url;
    let params;

    $(document).ajaxSend(function(e, xhr) {
        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization')); });

    if(num===1){
        console.log("지사 코드 중복확인");
        url = "/api/head/branchOverlap";
        params = {
            brCode: $("#brCode").val()
        };
    }else{
        console.log("가맹점 코드 중복확인");
        url = "/api/head/franchiseOverlap";
        params = {
            frCode: $("#frCode").val()
        };
    }


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
                if(num===1){
                    $("#brCodeChecked").val("1");
                }else{
                    $("#frCodeChecked").val("1");
                }
                console.log("중복환인 완료");
                alertSuccess("사용할 수 있는  코드입니다.");
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

// 지사 저장함수
function branchSave(){

    const $brCodeChecked = $("#brCodeChecked").val();
    if($brCodeChecked==="0"){
        alertCaution("지사코드 중복확인을 해주세요.",1);
        return false;
    }
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });

    const formData = new FormData(document.getElementById('brFormData'));

    let url = "/api/head/branchSave";

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
                alertSuccess("지사 저장완료");
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

// 가맹점 저장함수
function franchiseSave(){

    const $frCodeChecked = $("#frCodeChecked").val();
    if($frCodeChecked==="0"){
        alertCaution("가맹점코드 중복확인을 해주세요.",1);
        return false;
    }
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });

    const formData = new FormData(document.getElementById('frFormData'));

    let url = "/api/head/franchiseSave";

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
                alertSuccess("가맹점 저장완료");
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


function fromToValidation(){

    let from = Date.parse($("#brContractFromDt").val());
    let to = $("#brContractToDt").val();


    console.log(from);
    console.log(to);
}