
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 0번 그리드에 값 가져와서 채워넣기 */
    setDataIntoGrid(0, gridCreateUrl[0]);

    /* 각 그리드 내의 셀 클릭시 1번 그리드에 해당 대분류에 따른 중분류 리스트를 띄운다 + 새로 추가된 행은 수정을 막지 않음 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        const selectedBig = {bgItemGroupcode : e.item.bgItemGroupcode};
        if(currentBig !== selectedBig && !AUIGrid.isAddedById(gridId[0], e.item._$uid)) {
            currentBig = selectedBig;
            AUIGrid.clearGridData(gridId[1]);
            AUIGrid.clearGridData(gridId[2]);
            setDataIntoGrid(1, gridCreateUrl[1], currentBig);
        }
    });

    // 최종삼품 API 작성후 주석해제
    AUIGrid.bind(gridId[1], "cellClick", function (e) {
        const selectedMedium = {bgItemGroupcode : currentBig.bgItemGroupcode, bsItemGroupcodeS : e.item.bsItemGroupcodeS};
        if(currentMedium !== selectedMedium && !AUIGrid.isAddedById(gridId[1], e.item._$uid)) {
            currentMedium = selectedMedium;
            AUIGrid.clearGridData(gridId[2]);
            setDataIntoGrid(2, gridCreateUrl[2], currentMedium);
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
                AUIGrid.showToastMessage(gridId[1], e.rowIndex, e.columnIndex, "입력된 중분류 코드는 수정할 수 없습니다.");
            }, 0);
            return false;
        }
    });

    AUIGrid.bind(gridId[2], "cellEditBegin", function (e) {
        if(e.dataField === "biItemSequence" && e.value !== "" && !AUIGrid.isAddedById(gridId[2], e.item._$uid)) {
            setTimeout(function (){
                AUIGrid.showToastMessage(gridId[2], e.rowIndex, e.columnIndex, "입력된 상품순번은 수정할 수 없습니다.");
            }, 0);
            return false;
        }
    });

    /* 각 0, 1, 2 그리드 입력이 끝난 코드의 각 그리드 중복확인과 대문자화하여 변환 */
    AUIGrid.bind(gridId[0], "cellEditEnd", function (e) {
        if(e.dataField === "bgItemGroupcode") {
            const numOfValuesInThisRow = AUIGrid.getRowIndexesByValue(gridId[0],
                    "bgItemGroupcode", [e.value, e.value.toUpperCase()]).length;
            if(numOfValuesInThisRow === 1) {
                e.item.bgItemGroupcode = e.value.toUpperCase();
                AUIGrid.updateRow(gridId[0], e.item, e.rowIndex);
            }else{
                e.item.bgItemGroupcode = undefined;
                AUIGrid.updateRow(gridId[0], e.item, e.rowIndex);
                setTimeout(function (){
                    AUIGrid.showToastMessage(gridId[0], e.rowIndex, e.columnIndex, "중복되지 않는 코드를 입력해 주세요");
                }, 0);
            }
        }
    });

    AUIGrid.bind(gridId[1], "cellEditEnd", function (e) {
        if(e.dataField === "bsItemGroupcodeS") {
            const numOfValuesInThisRow = AUIGrid.getRowIndexesByValue(gridId[1],
                    "bsItemGroupcodeS", [e.value, e.value.toUpperCase()]).length;
            if(numOfValuesInThisRow === 1) {
                e.item.bsItemGroupcodeS = e.value.toUpperCase();
                AUIGrid.updateRow(gridId[1], e.item, e.rowIndex);
            }else{
                e.item.bsItemGroupcodeS = undefined;
                AUIGrid.updateRow(gridId[1], e.item, e.rowIndex);
                setTimeout(function (){
                    AUIGrid.showToastMessage(gridId[1], e.rowIndex, e.columnIndex, "중복되지 않는 코드를 입력해 주세요");
                }, 0);
            }
        }
    });

    AUIGrid.bind(gridId[2], "cellEditEnd", function (e) {
        if(e.dataField === "biItemSequence") {
            const numOfValuesInThisRow = AUIGrid.getRowIndexesByValue(gridId[2], "biItemSequence", e.value).length;
            if(numOfValuesInThisRow === 1) {
                e.item.biItemcode = e.item.bgItemGroupcode + e.item.bsItemGroupcodeS + e.item.biItemSequence;
                AUIGrid.updateRow(gridId[2], e.item, e.rowIndex);
            }else{
                e.item.biItemSequence = undefined;
                e.item.biItemcode = undefined;
                AUIGrid.updateRow(gridId[2], e.item, e.rowIndex);
                setTimeout(function (){
                    AUIGrid.showToastMessage(gridId[2], e.rowIndex, e.columnIndex, "중복되지 않는 코드를 입력해 주세요");
                }, 0);
            }
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
    "/api/head/itemGroupAList", "/api/head/itemGroupBList", "/api/head/itemGroupCList"
];

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/head/itemGroupA", "/api/head/itemGroupB", "/api/head/itemGroupC"
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
    }, {
        dataField: "bgUseYn",
        headerText: "사용여부",
        renderer: {
            type: "DropDownListRenderer",
            list: ["Y", "N"]
        },
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable: true,
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
        editable: false,
    }, {
        dataField: "bgName",
        headerText: "대분류명",
        editable: false,
    }, {
        dataField: "bsItemGroupcodeS",
        headerText: "중분류 코드",
        editRenderer: { // 입력된 코드의 각종 유효성 검사
            type: "InputEditRenderer",
            maxlength: 1,
            validator: codeValidatorTwo,
        },
        styleFunction: defaultColumn,
    }, {
        dataField: "bsName",
        headerText: "명칭",
    }, {
        dataField: "bsRemark",
        headerText: "중분류 특이사항",
    }, {
        dataField: "bsUseYn",
        headerText: "사용여부",
        renderer: {
            type: "DropDownListRenderer",
            list: ["Y", "N"]
        },
    }
];

gridProp[1] = {
    editable: true,
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
        editable: false,
    }, {
        dataField: "bgName",
        headerText: "대분류명",
        editable: false,
    }, {
        dataField: "bsItemGroupcodeS",
        headerText: "중분류 코드",
        editable: false,
    }, {
        dataField: "bsName",
        headerText: "중분류명",
        editable: false,
    }, {
        dataField: "biItemcode",
        headerText: "상품코드",
        editable: false,
    }, {
        dataField: "biItemSequence",
        headerText: "상품순번",
        editRenderer: { // 입력된 코드의 각종 유효성 검사
            type: "InputEditRenderer",
            maxlength: 3,
            onlyNumeric: true,
            validator: codeValidator,
        },
    }, {
        dataField: "biName",
        headerText: "상품명",
    }, {
        dataField: "biRemark",
        headerText: "상품 특이사항",
    }, {
        dataField: "biUseYn",
        headerText: "사용여부",
        renderer: {
            type: "DropDownListRenderer",
            list: ["Y", "N"]
        },
    }
];

gridProp[2] = {
    editable: true,
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
    switch (numOfGrid) {
        case 0 :
            AUIGrid.addRow(gridId[numOfGrid], {bgUseYn : "Y"}, "last");
            break;
        case 1 :
            item = AUIGrid.getSelectedRows(gridId[0]);
            if(item.length && !AUIGrid.isAddedById(gridId[0], item[0]._$uid)) {
                delete item[0]['_$uid'];
                console.log(item);
                AUIGrid.addRow(gridId[numOfGrid], {bsUseYn : "Y"}, "last");
            }else{
                alertCaution("저장되어 있는 대분류 항목을 선택해 주세요", 1);
            }
            break;
        case 2 :
            item = AUIGrid.getSelectedRows(gridId[1]);
            if(item.length && !AUIGrid.isAddedById(gridId[1], item[0]._$uid)) {
                delete item[0]['_$uid'];
                AUIGrid.addRow(gridId[numOfGrid], {biUseYn : "Y"}, "last");
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
            /* 입력되지 않은 코드나 명칭이 있는지 검사*/
            addedRowItems.forEach(function (item) {
                if(item.bgItemGroupcode === undefined || item.bgItemGroupcode === "") {
                    alertCaution("모든 코드를 입력해 주세요", 1);
                    isExecute = false;
                }else if(item.bgName === undefined || item.bgName === "") {
                    alertCaution("모든 명칭을 입력해 주세요", 1);
                    isExecute = false;
                }
            });
            updatedRowItems.forEach(function (item) {
                if(item.bgName === undefined || item.bgName === "") {
                    alertCaution("모든 명칭을 입력해 주세요", 1);
                    isExecute = false;
                }
            });
            break;
        case 1 :
            addedRowItems.forEach(function (item) {
                if(item.bsItemGroupcodeS === undefined || item.bsItemGroupcodeS === "") {
                    alertCaution("모든 중분류 코드를 입력해 주세요", 1);
                    isExecute = false;
                }else if(item.bsName === undefined || item.bsName === "") {
                    alertCaution("모든 명칭을 입력해 주세요", 1);
                    isExecute = false;
                }
            });
            updatedRowItems.forEach(function (item) {
                if(item.bsName === undefined || item.bsName === "") {
                    alertCaution("모든 명칭을 입력해 주세요", 1);
                    isExecute = false;
                }
            });
            break;
        case 2 :
            addedRowItems.forEach(function (item) {
                if(item.biItemSequence === undefined || item.biItemSequence === "") {
                    alertCaution("모든 상품순번을 입력해 주세요", 1);
                    isExecute = false;
                }else if(item.biName === undefined || item.biName === "") {
                    alertCaution("모든 상품명을 입력해 주세요", 1);
                    isExecute = false;
                }
            });
            updatedRowItems.forEach(function (item) {
                if(item.biName === undefined || item.biName === "") {
                    alertCaution("모든 상품명을 입력해 주세요", 1);
                    isExecute = false;
                }
            });
            break;
    }

    if(isExecute) {
        console.log(data);
        const jsonString = JSON.stringify(data);
        CommonUI.ajaxjson(gridSaveUrl[numOfGrid], jsonString, function () {
            AUIGrid.removeSoftRows(gridId[numOfGrid]);
            AUIGrid.resetUpdatedItems(gridId[numOfGrid]);
            switch (numOfGrid) {
                case 0 :
                    if(currentBig !== undefined) {
                        AUIGrid.clearGridData(gridId[1]);
                        setDataIntoGrid(1, gridCreateUrl[1], currentBig);
                    }
                    if(currentMedium !== undefined) {
                        AUIGrid.clearGridData(gridId[2]);
                        setDataIntoGrid(2, gridCreateUrl[2], currentMedium);
                    }
                    break;
                case 1 :
                    if(currentMedium !== undefined) {
                        AUIGrid.clearGridData(gridId[2]);
                        setDataIntoGrid(2, gridCreateUrl[2], currentMedium);
                    }
                    break;
            }
        });
    }
}

/* 입력된 각 코드들의 형식과 길이를 유효성 검증함 */
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

function defaultColumn(rowIndex, columnIndex, value, headerText, item, dataField) {
    let returnStyle = "aui-grid-editable-column";
    if(value !== "" && !AUIGrid.isAddedById(gridId[1], item._$uid)) {
        returnStyle = "";
    }
    return returnStyle;
}