
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 0번 그리드에 값 가져와서 채워넣기 */
    setDataIntoGrid(0, gridCreateUrl[0]);

    /* 각 그리드 내의 셀 클릭시 1번 그리드에 해당 대분류에 따른 중분류 리스트를 띄운다. */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        const selectedBig = e.item.bgItemGroupcode;
        if(currentBig !== selectedBig && !AUIGrid.isAddedById(gridId[0], e.item._$uid)) {/* 새로 추가된 행의 경우 하위 항목이 없을테니 동작을 막는다. */
            currentBig = selectedBig;

        }
    });

    AUIGrid.bind(gridId[1], "cellClick", function (e) {
        const selectedMedium = e.item.sItemGroupcodeS;
        if(currentMedium !== selectedMedium && !AUIGrid.isAddedById(gridId[1], e.item._$uid)) {/* 새로 추가된 행의 경우 하위 항목이 없을테니 동작을 막는다. */
            currentMedium = selectedMedium;

        }
    });

    /* 각 그리드 입력을 시작할 때 새로 추가된 코드가 아니면서, 값이 존재하는 코드를 편집하려 하면 편집을 막고 경고를 띄운다. */
    AUIGrid.bind(gridId[0], "cellEditBegin", function (e) {
        if(e.dataField === "bgItemGroupcode" && e.value !== "" && !AUIGrid.isAddedById(gridId[0], e.item._$uid)) {
            setTimeout(function (){
                AUIGrid.showToastMessage(gridId[0], e.rowIndex, e.columnIndex, "입력된 코드는 수정할 수 없습니다.");
            }, 0);
            return false;
        }
    });

    AUIGrid.bind(gridId[1], "cellEditBegin", function (e) {
        if(e.dataField === "bsItemGroupcodeS" && e.value !== "" && !AUIGrid.isAddedById(gridId[1], e.item._$uid)) {
            setTimeout(function (){
                AUIGrid.showToastMessage(gridId[0], e.rowIndex, e.columnIndex, "입력된 중분류 코드는 수정할 수 없습니다.");
            }, 0);
            return false;
        }
    });

    AUIGrid.bind(gridId[2], "cellEditBegin", function (e) {
        if(e.dataField === "biItemcode" && e.value !== "" && !AUIGrid.isAddedById(gridId[2], e.item._$uid)) {
            setTimeout(function (){
                AUIGrid.showToastMessage(gridId[0], e.rowIndex, e.columnIndex, "입력된 상품코드는 수정할 수 없습니다.");
            }, 0);
            return false;
        }
    });

    /* 각 그리드 입력이 끝난 코드의 대문자 변환 */
    AUIGrid.bind(gridId[0], "cellEditEnd", function (e) {
        if(e.dataField === "bgItemGroupcode") {
            e.item.bgItemGroupcode = e.item.bgItemGroupcode.toUpperCase();
            AUIGrid.updateRow(gridId[0], e.item, e.rowIndex);
        }
    });

    AUIGrid.bind(gridId[1], "cellEditEnd", function (e) {
        if(e.dataField === "bsItemGroupcodeS") {
            e.item.bsItemGroupcodeS = e.item.bsItemGroupcodeS.toUpperCase();
            AUIGrid.updateRow(gridId[1], e.item, e.rowIndex);
        }
    });

    AUIGrid.bind(gridId[2], "cellEditEnd", function (e) {
        if(e.dataField === "biItemcode") {
            e.item.biItemcode = e.item.biItemcode.toUpperCase();
            AUIGrid.updateRow(gridId[2], e.item, e.rowIndex);
        }
    });

});

/* 현재의 선택퇸 대분류코드, 중분류 코드에 따라서 중분류와 최종상품을 출력하고, 이전 선택과 지금의 선택이 중복선택인지에 대한 체크에 쓰인다. */
let currentBig;
let currentMedium;

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
    "grid_bigclass", "grid_mediumclass", "grid_finalproduct"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/head/itemGroupAList", "/api/head/", "/api/head/"
];

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/head/itemGroupA", "/api/head/", "/api/head/"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "bgItemGroupcode",
        headerText: "코드",
        editRenderer: { // 입력된 코드의 각종 유효성 검사
            type: "InputEditRenderer",
            maxlength: 3,
            validator: codeValidator,
        },
    }, {
        dataField: "bgName",
        headerText: "명칭",
    }, {
        dataField: "bgRemark",
        headerText: "대분류 특이사항",
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable : true,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    showRowNumColumn : false,
    enableColumnResize : false,
    rowIdTrustMode : true,
    showStateColumn : true,
    enableFilter : true,
    showRowCheckColumn : true,
};

gridColumnLayout[1] = [
    {
        dataField: "bgItemGroupcode",
        headerText: "대분류 코드",
        editable : false,
    }, {
        dataField: "bgName",
        headerText: "대분류명",
        editable : false,
    }, {
        dataField: "bsItemGroupcodeS",
        headerText: "중분류 코드",
        editRenderer: { // 입력된 코드의 각종 유효성 검사
            type: "InputEditRenderer",
            maxlength: 1,
            validator: codeValidatorTwo,
        },
    }, {
        dataField: "bsName",
        headerText: "명칭",
    }, {
        dataField: "bsRemark",
        headerText: "중분류 특이사항",
    },
];

gridProp[1] = {
    editable : true,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    showRowNumColumn : false,
    enableColumnResize : false,
    rowIdTrustMode : true,
    showStateColumn : true,
    enableFilter : true,
    showRowCheckColumn : true,
};

gridColumnLayout[2] = [
    {
        dataField: "bgItemGroupcode",
        headerText: "대분류 코드",
        editable : false,
    }, {
        dataField: "bgName",
        headerText: "대분류명",
        editable : false,
    }, {
        dataField: "bsItemGroupcodeS",
        headerText: "중분류 코드",
        editable : false,
    }, {
        dataField: "bsName",
        headerText: "중분류명",
        editable : false,
    }, {
        dataField: "biItemcode",
        headerText: "상품코드",
        editRenderer: { // 입력된 코드의 각종 유효성 검사
            type: "InputEditRenderer",
            maxlength: 3,
            validator: codeValidator,
        },
    }, {
        dataField: "biitemSequence",
        headerText: "상품순번",
    }, {
        dataField: "biName",
        headerText: "상품명",
    }, {
        dataField: "biRemark",
        headerText: "상품 특이사항",
    },
];

gridProp[2] = {
    editable : true,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    showRowNumColumn : false,
    enableColumnResize : false,
    rowIdTrustMode : true,
    showStateColumn : true,
    enableFilter : true,
    showRowCheckColumn : true,
};


/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids() {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }
}

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid(numOfGrid, url, code = false) {
    CommonUI.ajax(url, "GET", code, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}

/* 지정된 그리드에 새 빈 데이터 추가 */
function addRow(numOfGrid) {
    let item = {};
    switch (numOfGrid) {
        case 0 :
            AUIGrid.addRow(gridId[numOfGrid], item, "first");
            break;
        case 1 :
            item = AUIGrid.getSelectedRows(gridId[0]);
            if(item.length && !AUIGrid.isAddedById(gridId[0], item[0]._$uid)) {
                delete item[0]['_$uid'];
                console.log(item);
                AUIGrid.addRow(gridId[numOfGrid], item, "first");
            }else{
                alertCaution("저장되어 있는 대분류 항목을 선택해 주세요", 1);
            }
            break;
        case 2 :
            item = AUIGrid.getSelectedRows(gridId[1]);
            if(item.length && !AUIGrid.isAddedById(gridId[1], item[0]._$uid)) {
                delete item[0]['_$uid'];
                AUIGrid.addRow(gridId[numOfGrid], item, "first");
            }else{
                alertCaution("저장되어 있는 중분류 항목을 선택해 주세요", 1);
            }
            break;
    }
}

// 선택한 그리드 행 삭제
function removeRow(numOfGrid) {
    if(AUIGrid.getCheckedRowItems(gridId[numOfGrid]).length){
        AUIGrid.removeCheckedRows(gridId[numOfGrid]);
    }else{
        AUIGrid.removeRow(gridId[numOfGrid], "selectedIndex");
    }
}

/* 해당 번호의 그리드의 저장기능 */
function gridSave(numOfGrid) {
    // 추가된 행 아이템들(배열)
    const addedRowItems = AUIGrid.getAddedRowItems(gridTargetDiv[numOfGrid]);

    // 수정된 행 아이템들(배열)
    const updatedRowItems = AUIGrid.getEditedRowItems(gridTargetDiv[numOfGrid]);

    // 삭제된 행 아이템들(배열)
    const deletedRowItems = AUIGrid.getRemovedItems(gridTargetDiv[numOfGrid]);

    // 서버로 보낼 데이터 작성
    const data = {
        "add" : addedRowItems,
        "update" : updatedRowItems,
        "delete" : deletedRowItems
    };

    let isExecute = true;
    /* 각 그리드 배열번호 항목마다의 유효성 검사 */
    switch (numOfGrid) {
        case 0 :
            /* 입력되지 않은 코드가 있는지 검사*/
            addedRowItems.forEach(function (item){
                if(item.bgItemGroupcode === undefined) {
                    alertCaution("모든 코드를 입력해 주세요.", 1);
                    isExecute = false;
                }
            });
            break;
        case 1 :
            addedRowItems.forEach(function (item){
                if(item.bsItemGroupcodeS === undefined) {
                    alertCaution("모든 중분류 코드를 입력해 주세요.", 1);
                    isExecute = false;
                }
            });
            break;
        case 2 :
            addedRowItems.forEach(function (item){
                if(item.biItemcode === undefined) {
                    alertCaution("모든 상품코드를 입력해 주세요.", 1);
                    isExecute = false;
                }
            });
            break;
    }

    if(isExecute) {
        const jsonString = JSON.stringify(data);
        CommonUI.ajaxjson(gridSaveUrl[numOfGrid], jsonString, function () {
            AUIGrid.removeSoftRows(gridId[numOfGrid]);
            AUIGrid.resetUpdatedItems(gridId[numOfGrid]);
        });
    }
}

function codeValidator(oldValue, newValue, item, dataField, fromClipboard) {
    let isValid = false;
    let failMessage = "";
    if(newValue.length < 3) {
        failMessage = "세글자를 입력해 주세요";
    }else if(/[^a-zA-Z0-9]/.test(newValue)) {
        failMessage = "영문자나 숫자로 구성하여 입력해 주세요";
    }else{
        isValid = true;
    }
    return {"validate": isValid, "message": failMessage};
}

function codeValidatorTwo(oldValue, newValue, item, dataField, fromClipboard) {
    let isValid = false;
    let failMessage = "";
    if(newValue.length < 1) {
        failMessage = "한글자를 입력해 주세요";
    }else if(/[^a-zA-Z0-9]/.test(newValue)) {
        failMessage = "영문자나 숫자로 구성하여 입력해 주세요";
    }else{
        isValid = true;
    }
    return {"validate": isValid, "message": failMessage};
}