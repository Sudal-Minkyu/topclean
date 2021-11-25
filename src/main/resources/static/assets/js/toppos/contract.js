
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

/* 그리드를 뿌릴 대상 id */
targetDiv = [
    "grid_contract_0", "grid_contract_1", "grid_contract_2", "grid_contract_3", "grid_branchList"
];

/* 각 그리드의 url */
const setGridUrl = [
    "/api/head/branchList", "/api/head/franchiseList", "/api/head/branchList", "/api/head/franchiseList", "/api/head/branchList"
];

/* 0번 그리드의 레이아웃 */
columnLayout[0] = [
    {
        dataField: "brCode",
        headerText: "지사코드",
    }, {
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

/* 0번 그리드의 프로퍼티(옵션) */
gridOption[0] = {
    editable : true,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "brCode",
    rowIdTrustMode : true
};

columnLayout[1] = [
    {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
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
    }
];

gridOption[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "frCode",
    rowIdTrustMode : true,
    enableFilter: true,
};

columnLayout[2] = [
    {
        dataField: "brCode",
        headerText: "지사코드",
    }, {
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
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "brCode",
    rowIdTrustMode : true
};

columnLayout[3] = [
    {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
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
    }, {
        dataField: "brAssignStateValue",
        headerText: "배정상태"
    },
];

gridOption[3] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "대상 가맹점 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "frCode",
    rowIdTrustMode : true,
    enableFilter : true,
};

columnLayout[4] = [
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

gridOption[4] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false,
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

    /* 2번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridID[2], "cellClick", function (e) {
        CommonUI.ajax("/api/head/branchAssignList", "GET", {brCode : e.item.brCode}, function (req) {
            const resultData = req.sendData.gridListData;
            AUIGrid.clearGridData(gridID[3]);
            AUIGrid.setGridData(gridID[3], resultData);
        })
    });

    /* 3번그리드 내의 아이템 클릭시 필드에 적용 */
    AUIGrid.bind(gridID[3], "cellClick", function (e) {
        CommonUI.ajax("/api/head/franchiseInfo", "GET", {frCode : e.item.frCode}, function(req) {
            const resultData = req.sendData.franchiseInfoData;
            resultData.frContractStateValue = resultData.frContractState;
            setFieldData(3, resultData);
        });
    });

    AUIGrid.bind(gridID[4], "cellClick", function (e) {
        $("#bot_brCode").val(e.item.brCode);
        $("#bot_brName").val(e.item.brName);
        $("#bot_brCarculateRateHq").val(e.item.brCarculateRateHq);
        $("#bot_brCarculateRateBr").val(e.item.brCarculateRateBr);
        $("#bot_brCarculateRateFr").val(e.item.brCarculateRateFr);
        $('#branch_popup').removeClass('open');
    });


});

/* 레이아웃, 프로퍼티를 적용하여 그리드 생성 */
function createGrid(columnLayout, gridOption) {
    for (const i in columnLayout) {
        gridID[i] = AUIGrid.create(targetDiv[i], columnLayout[i], gridOption[i]);
    }

    /* 그리드들에 초기 데이터 주입 */
    for (let i=0; i<5; i++) {
    	setListData(setGridUrl[i], i);
    }
}

/* 해당 그리드와 연관된 그리드의 데이터를 주입한다. */
function setListData(url, numOfGrid) {
    CommonUI.ajax(url, "GET", false, function(req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridID[numOfGrid], gridData[numOfGrid]);
    })
}

// 지사, 가맹점 코드 중복확인 체크함수
function codeOverlap(num){

    let url;
    let params;

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
        const isUpdated = AUIGrid.rowIdToIndex(gridID[0], sentData.brCode) > -1;
        if(isUpdated) {
            AUIGrid.updateRowsById(gridID[0], sentData);
        }else {
            AUIGrid.addRow(gridID[0], sentData, "last");
        }
        AUIGrid.resetUpdatedItems(gridID[0]);
        AUIGrid.clearGridData(gridID[2]);
        setListData(setGridUrl[2], 2);
        alertSuccess("지사 저장완료");
    });
}

// 가맹점 저장함수
function franchiseSave() {

    const $frCodeChecked = $("#frCodeChecked").val();
    if($frCodeChecked==="0"){
        alertCaution("가맹점코드 중복확인을 해주세요.",1);
        return false;
    }

    const formData = new FormData(document.getElementById('frFormData'));
    let url = "/api/head/franchiseSave";

    CommonUI.ajax(url, "POST", formData, function (req){
        const sentData = Object.fromEntries(formData);
        const isUpdated = AUIGrid.rowIdToIndex(gridID[1], sentData.frCode) > -1;

        if(isUpdated) {
            AUIGrid.updateRowsById(gridID[1], sentData);
        }else {
            AUIGrid.addRow(gridID[1], sentData, "last");
        }
        AUIGrid.resetUpdatedItems(gridID[1]);
        AUIGrid.clearGridData(gridID[3]);
        setListData(setGridUrl[3], 3);
        alertSuccess("가맹점 저장완료");
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
        AUIGrid.updateRowsById(gridID[3], jsonData);
        alertSuccess("가맹점 배정 완료");
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
            $("#bot_frContractState").val(item.frContractStateValue);
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
}


/* 그리드 필터링 기능 */
function filterFrList(type, filterValue = "") {

    /* 두가지 조건이 중첩할 경우 중첩된 조건이 필터링 되게 함 */
    function filterGrid3() {
        AUIGrid.clearFilterAll(gridID[3]);
        if(filterCondition1A !== "") {
            AUIGrid.setFilter(gridID[3], "brAssignStateValue", function (dataField, value, item) {
                return item.brAssignState === filterCondition1A;
            });
        }
        if(filterCondition1B !== "") {
            AUIGrid.setFilter(gridID[3], "frName", function (dataField, value, item){
                /* 입력문자를 정규표현식화 하여 해당 문자를 포함한 결과를 true로 반환해 필터링 */
                const filterCondition = new RegExp(filterCondition1B);
                return filterCondition.test(value);
            });
        }
    }

    /* 누른 버튼에 따라 필터링을 함*/
    switch (type) {
        case 1 :
            filterCondition1A = filterValue;
            filterGrid3();
            break;
        case 2 :
            filterCondition1B = filterValue;
            filterGrid3();
            break;
        case 3 :
            filterCondition1A = "";
            filterCondition1B = "";
            $("#bot_frNameFilter").val("");
            AUIGrid.clearGridData(gridID[3]);
            setListData(setGridUrl[3], 3);
            break;

        case 6 :
            AUIGrid.clearFilterAll(gridID[1]);
            AUIGrid.setFilter(gridID[1], "frName", function (dataField, value, item){
                const filterCondition = new RegExp(filterValue);
                return filterCondition.test(value);
            });
            break;
        case 7 :
            AUIGrid.clearFilterAll(gridID[1]);
            $("#frNameFilter").val("");
            break;
    }

}

/* 지사 선택 팝업 */
function brListPop(){
    $('#branch_popup').addClass('open');
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
                element.value = element.value.replace(/[^0-9]/g, "");
            break;
    }



}