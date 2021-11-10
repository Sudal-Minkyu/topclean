
/*
* 각 그리드와 관련된 정보들이 배열 순차적으로 담길 곳들
* 이후 이 정보를 참조하여 순차적으로 뿌리게 된다.
* 현재 페이지의 경우 Grid 화면은 4개인데, 2개의 데이터 셋을 불러오고 공유하므로, 차후 나올 코드를 감안해야 한다.
* */

let gridID = [];
let targetDiv = [];
let gridData = [];
let columnLayout = [];
let gridOption = [];

targetDiv = [
    "grid_contract_0", "grid_contract_1", "grid_contract_2", "grid_contract_3"
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

gridOption[0] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "brCode",
    showStateColumn : true
};

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

gridOption[1] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "frCode",
    showStateColumn : true
};

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

gridOption[2] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "brCode",
    showStateColumn : true
};

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

gridOption[3] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "frCode",
    showStateColumn : true
};

/* datepicker를 적용시킬 대상들의 dom id들 */
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

/* 실행부 */
$(function () {

    /* 그리드 생성과 데이터 세팅 */
    createGrid(columnLayout, gridOption);

    /* datePickerTargetIds 배열에 따라 날짜 선택기 적용 */
    CommonUI.setDatePicker(datePickerTargetIds);

    /* dateAToBTargetIds 배열에 따라 날짜 선택 제한 적용 */
    CommonUI.restrictDateAToB(dateAToBTargetIds);

    /* 0번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridID[0], "cellClick", function (e) {
        setFieldData(0, e.item);
    });

    /* 1번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridID[1], "cellClick", function (e) {
        setFieldData(1, e.item);
    });



});


function createGrid(columnLayout, gridOption) {
    for (const i in columnLayout) {
        gridID[i] = AUIGrid.create(targetDiv[i], columnLayout[i], gridOption[i]);
    }

    /* <=========================> */
    /* 각 그리드의 url */
    const setGridUrl = [
        "/api/head/branchList", "/api/head/franchiseList"
    ];

    for (let i=0; i<2; i++) {
    	setListData(setGridUrl[i], i);
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

                const sentData = Object.fromEntries(formData);
                const isUpdated = AUIGrid.rowIdToIndex(gridID[0], sentData.brCode) > -1;

                if(isUpdated) {
                    AUIGrid.updateRowsById(gridID[0], sentData);
                    AUIGrid.refresh(gridID[2]);
                }else {
                    AUIGrid.addRow(gridID[0], sentData);
                }

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
                const sentData = Object.fromEntries(formData);
                const isUpdated = AUIGrid.rowIdToIndex(gridID[1], sentData.frCode) > -1;

                if(isUpdated) {
                    AUIGrid.updateRowsById(gridID[1], sentData);
                    AUIGrid.refresh(gridID[3]);
                }else {
                    AUIGrid.addRow(gridID[1], sentData);``
                }
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
}

function setFieldData(numOfGrid, item) {
    let isLockField = item.brCode !== undefined || item.frCode !== undefined;
    switch (numOfGrid) {
        case 0 :
            $("#brCode").val(item.brCode);
            $("#brCode").attr("readonly", isLockField);
            $("#brCodeChkBtn").attr("disabled", isLockField);
            $("#brCodeChecked").val('1');
            $("#brName").val(item.brName);
            $("#brContractDt").val(item.brContractDt);
            $("#brContractFromDt").val(item.brContractFromDt);
            $("#brContractToDt").val(item.brContractToDt);
            $("#brContractState").val(item.brContractState);
            $("#brCarculateRateHq").val(item.brCarculateRateHq);
            $("#brCarculateRateBr").val(item.brCarculateRateBr);
            $("#brCarculateRateFr").val(item.brCarculateRateFr);
            $("#brRemark").html(item.brRemark);
            CommonUI.restrictDate(dateAToBTargetIds[0][0], dateAToBTargetIds[0][1], false);
            CommonUI.restrictDate(dateAToBTargetIds[0][0], dateAToBTargetIds[0][1], true);
            console.log($("#brCode").val());
            break;

        case 1 :
            $("#frCode").val(item.frCode);
            $("#frCode").attr("readonly", isLockField);
            $("#frCodeChkBtn").attr("disabled", isLockField);
            $("#frCodeChecked").val(1);
            $("#frName").val(item.frName);
            $("#frContractDt").val(item.frContractDt);
            $("#frContractFromDt").val(item.frContractFromDt);
            $("#frContractToDt").val(item.frContractToDt);
            $("#frContractState").val(item.frContractState);
            $("#frRemark").html(item.frRemark);
            CommonUI.restrictDate(dateAToBTargetIds[1][0], dateAToBTargetIds[1][1], false);
            CommonUI.restrictDate(dateAToBTargetIds[1][0], dateAToBTargetIds[1][1], true);
            break;
    }

}

/* 그리드에 새로운 row를 생성하고 입력창을 비워 새로운 데이터 저장 준비에 들어간다. */
function createNewPost(numOfGrid) {
    setFieldData(numOfGrid, {brRemark : "", frRemark : "",
                brCodeChecked : "0", frCodeChecked : "0", brContractState : "01", frContractState : "01"});
    switch (numOfGrid) {
        case 0 :
            $("#brCode").trigger("focus");
            break;
        case 1 :
            $("#frCode").trigger("focus");
            break;
    }
}