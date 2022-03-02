
/*
* 각 그리드와 관련된 정보들이 배열 순차적으로 담길 곳들
* 이후 이 정보를 참조하여 순차적으로 뿌리게 된다.
* 현재 페이지의 경우 Grid 화면은 4개인데, 2개의 데이터 셋을 불러오고 공유하므로, 차후 나올 코드를 감안해야 한다.
* */

let gridId = [];
let gridTargetDiv = [];
let gridData = [];
let gridColumnLayout = [];
let gridProp = [];

/* 그리드를 뿌릴 대상 id */
gridTargetDiv = [
    "grid_contract_0", "grid_contract_1", "grid_contract_2", "grid_contract_3", "grid_branchList"
];

/* 각 그리드의 url */
const gridCreateUrl = [
    "/api/head/branchList", "/api/head/franchiseList", "/api/head/branchList", "/api/head/franchiseList", "/api/head/branchList"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "brCode",
        headerText: "지사코드",
    }, {
        dataField: "brName",
        headerText: "지사명",
        style: "grid_textalign_left",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    },
];

/* 0번 그리드의 프로퍼티(옵션) */
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "brCode",
    rowIdTrustMode : true,
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : true,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[1] = [
    {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
        dataField: "frName",
        headerText: "가맹점명",
        style: "grid_textalign_left",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "brName",
        headerText: "배정지사명",
        style: "grid_textalign_left",
    }
];

gridProp[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "frCode",
    rowIdTrustMode : true,
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : true,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[2] = [
    {
        dataField: "brCode",
        headerText: "지사코드",
    }, {
        dataField: "brName",
        headerText: "지사명",
        style: "grid_textalign_left",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "brCode",
    rowIdTrustMode : true,
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : true,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[3] = [
    {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
        dataField: "frName",
        headerText: "가맹점명",
        style: "grid_textalign_left",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "brName",
        headerText: "배정지사명",
        style: "grid_textalign_left",
    }, {
        dataField: "brAssignStateValue",
        headerText: "배정상태"
    },
];

gridProp[3] = {
    editable : false,
    selectionMode : "singleRow",
    rowNumHeaderText : "순번",
    rowIdField : "frCode",
    rowIdTrustMode : true,
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : true,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[4] = [
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

gridProp[4] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : true,
    showStateColumn : false,
    enableFilter : true,
    width : 660,
    height : 480,
}

/* datepicker를 적용시킬 대상들의 dom id들 */
const datePickerTargetIds = [
    "brContractDt", "brContractFromDt", "brContractToDt", "frContractDt", "frContractFromDt",
        "frContractToDt"
];

/*
* JqueyUI datepicker의 기간 A~B까지를 선택할 때 선택한 날짜에 따라 제한을 주기 위한 DOM id의 배열이다.
* 배열 내 각 내부 배열은 [~부터의 제한 대상이 될 id, ~까지의 제한 대상이 될 id] 이다.
* */
const dateAToBTargetIds = [
    ["brContractFromDt", "brContractToDt"], ["frContractFromDt", "frContractToDt"]
];

/* 1번 그리드의 필터조건 첫번째, 두번째가 임시 저장된다. */
let filterCondition1A = "";
let filterCondition1B = "";

/* 실행부 */
$(function () {

    /* 그리드 생성과 데이터 세팅 */
    createGrid(gridColumnLayout, gridProp);

    /* datePickerTargetIds 배열에 따라 날짜 선택기 적용 */
    CommonUI.setDatePicker(datePickerTargetIds);

    /* dateAToBTargetIds 배열에 따라 날짜 선택 제한 적용 */
    CommonUI.restrictDateAToB(dateAToBTargetIds);

    /* 0번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        setFieldData(0, e.item);
    });

    /* 1번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridId[1], "cellClick", function (e) {
        setFieldData(1, e.item);
    });

    /* 2번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridId[2], "cellClick", function (e) {
        CommonUI.ajax("/api/head/branchAssignList", "GET", {brCode : e.item.brCode}, function (req) {
            const resultData = req.sendData.gridListData;
            AUIGrid.clearGridData(gridId[3]);
            AUIGrid.setGridData(gridId[3], resultData);
        });
    });

    /* 3번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridId[3], "cellClick", function (e) {
        CommonUI.ajax("/api/head/franchiseInfo", "GET", {frCode : e.item.frCode}, function(req) {
            const resultData = req.sendData.franchiseInfoData;
            resultData.frContractStateValue = resultData.frContractState;
            setFieldData(3, resultData);
        });
    });

    AUIGrid.bind(gridId[4], "cellDoubleClick", function (e) {
        putBranchInfo(e.item);
    });
});

/* 레이아웃, 프로퍼티를 적용하여 그리드 생성 */
function createGrid(gridColumnLayout, gridProp) {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }

    /* 그리드들에 초기 데이터 주입 */
    for (let i=0; i<5; i++) {
    	setListData(gridCreateUrl[i], i);
    }
}

/* 해당 그리드와 연관된 그리드의 데이터를 주입한다. */
function setListData(url, numOfGrid, callback = function (){}) {
    CommonUI.ajax(url, "GET", false, function(req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
        return callback();
    })
}

// 지사, 가맹점 코드 중복확인 체크함수
function codeOverlap(num){

    let url;
    let params;

    if(num===1){
        url = "/api/head/branchOverlap";
        params = {
            brCode: $("#brCode").val()
        };
    }else{
        url = "/api/head/franchiseOverlap";
        params = {
            frCode: $("#frCode").val()
        };
    }

    CommonUI.ajax(url,"GET",params, function (req){
        if(num===1){
            $("#brCodeChecked").val("1");
        }else{
            $("#frCodeChecked").val("1");
        }
        alertSuccess("사용할 수 있는  코드입니다.");
    });
}

// 지사 저장함수
function branchSave(){

    const $brCodeChecked = $("#brCodeChecked").val();
    if($brCodeChecked==="0"){
        alertCaution("지사코드 중복확인을 해주세요.",1);
        return false;
    }
    const valCalRateHq = $("#brCarculateRateHq").val();
    const valCalRateBr = $("#brCarculateRateBr").val();
    const valCalRateFr = $("#brCarculateRateFr").val();
    const sumCalRate = parseFloat(valCalRateHq) + parseFloat(valCalRateBr) + parseFloat(valCalRateFr);
    if(sumCalRate !== 100) {
        alertCaution("정산비율의 합은 100 이어야 합니다." +
            "<br> 현재값 :" + parseFloat(sumCalRate.toFixed(2))
            + "&nbsp; 차이 :" + parseFloat((100 - sumCalRate).toFixed(2)), 1);
        return false;
    }

    const formData = new FormData(document.getElementById('brFormData'));
    const url = "/api/head/branchSave";

    CommonUI.ajax(url, "POST", formData, function (req){
        const sentData = Object.fromEntries(formData);
        const isUpdated = AUIGrid.rowIdToIndex(gridId[0], sentData.brCode) > -1;
        if(isUpdated) {
            AUIGrid.updateRowsById(gridId[0], sentData);
        }else {
            AUIGrid.addRow(gridId[0], sentData, "last");
        }
        AUIGrid.resetUpdatedItems(gridId[0]);
        AUIGrid.clearGridData(gridId[2]);
        setListData(gridCreateUrl[2], 2);
        createNewPost(0);
        alertSuccess("지사 저장완료");
    });
}

function branchDelete() {
    alertCheck("정말 삭제하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        const selectedItems = AUIGrid.getSelectedItems(gridId[0]);
        if(selectedItems.length) {
            const url = "/api/head/branchDelete";
            const data = {
                brCode: selectedItems[0].item.brCode
            }
            CommonUI.ajax(url, "PARAM", data, function () {
                AUIGrid.clearGridData(gridId[0]);
                setListData(gridCreateUrl[0], 0);
                createNewPost(0);
                alertSuccess("지사 삭제가 완료되었습니다.");
            });
        }
        $('#popupId').remove();
    });
}

// 가맹점 저장함수
function franchiseSave() {

    const $frCodeChecked = $("#frCodeChecked").val();
    if($frCodeChecked==="0"){
        alertCaution("가맹점코드 중복확인을 해주세요.",1);
        return false;
    }
    if(!$("#frEstimateDuration").val()) {
        alertCaution("출고예정일계산을 입력해 주세요",1);
        return false;
    }

    const formData = new FormData(document.getElementById('frFormData'));
    let url = "/api/head/franchiseSave";
    formData.set("frBusinessNo", formData.get("frBusinessNo").numString());
    formData.set("frTelNo", formData.get("frTelNo").numString());

    CommonUI.ajax(url, "POST", formData, function (req){
        const sentData = Object.fromEntries(formData);
        console.log(sentData);
        const isUpdated = AUIGrid.rowIdToIndex(gridId[1], sentData.frCode) > -1;

        if(isUpdated) {
            AUIGrid.updateRowsById(gridId[1], sentData);
        }else {
            AUIGrid.addRow(gridId[1], sentData, "last");
        }
        AUIGrid.resetUpdatedItems(gridId[1]);
        AUIGrid.clearGridData(gridId[3]);
        setListData(gridCreateUrl[3], 3);
        alertSuccess("가맹점 저장완료");
        AUIGrid.clearGridData(gridId[1]);
        setListData(gridCreateUrl[1], 1);
        createNewPost(1);
    });
}

function franchiseDelete() {
    alertCheck("정말 삭제하시겠습니까?");
    $("#checkDelSuccessBtn").on("click", function () {
        const selectedItems = AUIGrid.getSelectedItems(gridId[1]);
        if(selectedItems.length) {
            const url = "/api/head/franchiseDelete";
            const data = {
                frCode: selectedItems[0].item.frCode
            }
            CommonUI.ajax(url, "PARAM", data, function () {
                AUIGrid.clearGridData(gridId[1]);
                setListData(gridCreateUrl[1], 1);
                createNewPost(1);
                alertSuccess("가맹점 삭제가 완료되었습니다.");
            });
        }
        $('#popupId').remove();
    });
}

/* 가맹점 배정 저장 */
function assignmentSave() {
    const frCode = $("#bot_frCode").val();
    const brCode = $("#bot_brCode").val();
    const brAssignState = $("#bot_brAssignState").val();

    if(frCode === ""){
        alertCaution("가맹점을 선택 해주세요.",1);
        return false;
    }
    if(brCode === ""){
        alertCaution("지사를 선택 해주세요.",1);
        return false;
    }

    const formData = new FormData();
    formData.append("frCode", frCode);
    formData.append("brCode", brCode);
    formData.append("bot_brAssignState", brAssignState);
    const url = "/api/head/franchiseAssignment";

    CommonUI.ajax(url, "POST", formData, function(req) {
        formData.append("brAssignState", brAssignState);
        formData.append("brName", $("#bot_brName").val());
        const jsonData = Object.fromEntries(formData);
        AUIGrid.updateRowsById(gridId[3], jsonData);
        setListData(gridCreateUrl[1], 1);
        //filterGrid3();
        alertSuccess("가맹점 배정정보 저장 완료");
    });

}

/* 클릭, 신규에 따른 필드의 값을 대입하거나, 비우고 적합한 상태를 세팅한다. */
function setFieldData(numOfGrid, item) {
    let isLockField = item.brCode !== undefined || item.frCode !== undefined;
    switch (numOfGrid) {
        case 0 :
            $("#brCode").val(item.brCode).attr("readonly", isLockField);
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
            $("#brRemark").val(item.brRemark);
            CommonUI.restrictDate(dateAToBTargetIds[0][0], dateAToBTargetIds[0][1], false);
            CommonUI.restrictDate(dateAToBTargetIds[0][0], dateAToBTargetIds[0][1], true);
            break;

        case 1 :
            $("#frCode").val(item.frCode).attr("readonly", isLockField);
            $("#frCodeChkBtn").attr("disabled", isLockField);
            $("#frCodeChecked").val(1);
            $("#frName").val(item.frName);
            $("#frContractDt").val(item.frContractDt);
            $("#frContractFromDt").val(item.frContractFromDt);
            $("#frContractToDt").val(item.frContractToDt);
            $("#frContractState").val(item.frContractState);
            $("#frPriceGrade").val(item.frPriceGrade);
            $("#frRefCode").val(item.frRefCode);
            $("#frBusinessNo").val(CommonUI.formatBusinessNo(item.frBusinessNo));
            $("#frRpreName").val(item.frRpreName);
            $("#frTelNo").val(item.frTelNo);
            $("#frTagNo").val(item.frTagNo);
            $("#frEstimateDuration").val(item.frEstimateDuration);
            $("#frRemark").val(item.frRemark);
            CommonUI.restrictDate(dateAToBTargetIds[1][0], dateAToBTargetIds[1][1], false);
            CommonUI.restrictDate(dateAToBTargetIds[1][0], dateAToBTargetIds[1][1], true);
            break;

        case 3 :
            $("#bot_frCode").val(item.frCode);
            $("#bot_frName").val(item.frName);
            $("#bot_frContractDt").val(item.frContractDt);
            $("#bot_frContractFromDt").val(item.frContractFromDt);
            $("#bot_frContractToDt").val(item.frContractToDt);
            $("#bot_frContractStateValue").val(item.frContractStateValue);
            $("#bot_brAssignState").val(item.brAssignState);
            $("#bot_brCode").val(item.brCode);
            $("#bot_brName").val(item.brName);
            $("#bot_brCarculateRateHq").val(item.brCarculateRateHq);
            $("#bot_brCarculateRateBr").val(item.brCarculateRateBr);
            $("#bot_brCarculateRateFr").val(item.brCarculateRateFr);
            break;
    }
}

/* 그리드에 새로운 row를 생성하고 입력창을 비워 새로운 데이터 저장 준비에 들어간다. */
function createNewPost(numOfGrid) {
    /* 기본적으로 undefined 로 되어있을시 데이터 배치에 문제가 생기는 값이나
    * 초기값 지정한 값들은 기본값을 지정하여 문제가 생기지 않도록 한다. */
    setFieldData(numOfGrid, {brRemark : "", frRemark : "",
                brCodeChecked : "0", frCodeChecked : "0", brContractState : "01", frContractState : "01"});
    /* 신규 row를 추가하고 나서 편집이 용이하도록 포커스를 준다. */
    switch (numOfGrid) {
        case 0 :
            $("#brContractDt").val(new Date().toISOString().slice(0, 10));
            $("#brCode").trigger("focus");
            break;
        case 1 :
            $("#frContractDt").val(new Date().toISOString().slice(0, 10));
            $("#frCode").trigger("focus");
            break;
    }
    AUIGrid.clearSelection(gridId[numOfGrid]);
}
/* 가맹점 리스트 조회 필터링
* type = 1 상부 가맹점 리스트 조회 필터링, type = 2 상부 필터링 초기화
* type = 3 하부 가맹점 리스트 조회 필터링, type = 2 하부 필터링 초기화
* */
function filterFranchiseList(type) {
    switch (type) {
        case 1 :
            AUIGrid.clearFilterAll(gridId[1]);
            const s_frNameFilter = $("#frNameFilter").val();
            if(s_frNameFilter !== "") {
                AUIGrid.setFilter(gridId[1], "frName", function (dataField, value, item) {
                    return new RegExp(s_frNameFilter.toUpperCase()).test(value.toUpperCase());
                });
            }
            break;
        case 2 :
            AUIGrid.clearFilterAll(gridId[1]);
            break;
        case 3 :
            AUIGrid.clearFilterAll(gridId[3]);
            AUIGrid.clearGridData(gridId[3]);
            setListData(gridCreateUrl[3], 3, filterCase3);
            break;
        case 4 :
            AUIGrid.clearFilterAll(gridId[3]);
            AUIGrid.clearGridData(gridId[3]);
            setListData(gridCreateUrl[3], 3);
            break;
    }

    function filterCase3() {
        const s_brAssignState = $("#s_brAssignState").val();
        const s_frName = $("#s_frName").val();
        if(s_brAssignState !== "") {
            AUIGrid.setFilter(gridId[3], "brAssignStateValue", function (dataField, value, item) {
                return s_brAssignState === item.brAssignState;
            });
        }
        if(s_frName !== "") {
            AUIGrid.setFilter(gridId[3], "frName", function (dataField, value, item) {
                return new RegExp(s_frName.toUpperCase()).test(value.toUpperCase());
            });
        }
    }
}

/* 지사 리스트 조회 필터링 type = 1 지사 리스트 조회 필터링, type = 2 필터링 초기화 */
function filterBranchList(type) {
    switch (type) {
        case 1 :
            AUIGrid.clearFilterAll(gridId[4]);
            const s_brName = $("#pop_s_brName").val();
            const s_brCode = $("#pop_s_brCode").val();
            const s_brContractState = $("#pop_s_brContractState").val();
            if(s_brName !== "") {
                AUIGrid.setFilter(gridId[4], "brName", function (dataField, value, item) {
                    return new RegExp(s_brName.toUpperCase()).test(value.toUpperCase());
                });
            }
            if(s_brCode !== "") {
                AUIGrid.setFilter(gridId[4], "brCode", function (dataField, value, item) {
                    return s_brCode === value;
                });
            }
            if(s_brContractState !== "") {
                AUIGrid.setFilter(gridId[4], "brContractStateValue", function (dataField, value, item) {
                    return s_brContractState === item.brContractState;
                });
            }
            break;
        case 2 :
            AUIGrid.clearFilterAll(gridId[4]);
            break;
    }
}

/* 지사 선택 팝업 */
function brListPop(){
    filterBranchList(2);
    $('#branch_popup').addClass('open');
    
    AUIGrid.resize(gridId[4]);
}

function branchSelect() {
    const selectedItem = AUIGrid.getSelectedItems(gridId[4])[0];
    if(selectedItem) {
        putBranchInfo(selectedItem.item);
    } else {
        alertCaution("지사를 선택해 주세요", 1);
    }
}

function putBranchInfo(item) {
    $("#bot_brCode").val(item.brCode);
    $("#bot_brName").val(item.brName);
    $("#bot_brCarculateRateHq").val(item.brCarculateRateHq);
    $("#bot_brCarculateRateBr").val(item.brCarculateRateBr);
    $("#bot_brCarculateRateFr").val(item.brCarculateRateFr);
    branchClose();
}

// 지사 점 팝업닫기
function branchClose(){
    $('#branch_popup').removeClass('open');
}

/* 입력 숫자 유효성 검사 및 수정 */
function validateNumber(element, type) {
    switch (type) {
        case 1 :
            if(parseFloat(element.value) > 100) {
                element.value = "100";
            }else{
                element.value = element.value.replace(/^(\d+.?\d{0,2})\d*$/,"$1");
            }
            break;
        case 2 :
                element.value = element.value.numString();
            break;
    }
}

function onKeyupFrTelNo(el) {
    el.value = CommonUI.formatTel(el.value);
    console.log("act");
}

function onKeyupFrBusinessNo(el) {
    el.value = CommonUI.formatBusinessNo(el.value);
}