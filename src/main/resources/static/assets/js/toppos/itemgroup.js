
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 0번 그리드에 값 가져와서 채워넣기 */
    setDataIntoGrid(0, gridCreateUrl[0]);

    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {

    });

    /* 0번 그리드 입력을 시작할 때 값이 존재하는 코드를 편집하려 하면 편집을 막고 경고를 띄운다. */
    AUIGrid.bind(gridId[0], "cellEditBegin", function (e) {
        if(e.dataField === "bgItemGroupcode" && e.value !== "") {
            setTimeout(function (){
                AUIGrid.showToastMessage(gridId[0], e.rowIndex, e.columnIndex, "입력된 코드는 수정할 수 없습니다.");
            }, 0);
            return false;
        }
    });

    /* 0번 그리드 입력이 끝난 코드의 대문자 변환 */
    AUIGrid.bind(gridId[0], "cellEditEnd", function (e) {
        if(e.dataField === "bgItemGroupcode") {
            e.item.bgItemGroupcode = e.item.bgItemGroupcode.toUpperCase();
            AUIGrid.updateRow(gridId[0], e.item, e.rowIndex);
        }
    });
});


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
            validator: function (oldValue, newValue, item, dataField, fromClipboard) {
                let isValid = false;
                let failMessage = "";
                if(newValue.length < 3) {
                    failMessage = "3자를 입력해 주세요";
                }else if(/[^a-zA-Z0-9]/.test(newValue)) {
                    failMessage = "영문자나 숫자로 구성하여 입력해 주세요";
                }else{
                    isValid = true;
                }

                return {"validate": isValid, "message": failMessage};

            }
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
    }, {
        dataField: "bgName",
        headerText: "대분류명",
    }, {
        dataField: "bsItemGroupcodeS",
        headerText: "중분류 코드",
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
    }, {
        dataField: "bgName",
        headerText: "대분류명",
    }, {
        dataField: "bsItemGroupcodeS",
        headerText: "중분류 코드",
    }, {
        dataField: "bsName",
        headerText: "중분류명",
    }, {
        dataField: "biItemcode",
        headerText: "상품코드",
    }, {
        dataField: "bgItemSequence",
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
function setDataIntoGrid(numOfGrid, url) {
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}

/* 지정된 그리드에 새 빈 데이터 추가 */
function addRow(numOfRow) {
    const item = {};
    AUIGrid.addRow(gridId[numOfRow], item, "first");
}

// 선택한 그리드 행 삭제
function removeRow(numOfRow) {
    if(AUIGrid.getCheckedRowItems(gridId[numOfRow]).length){
        AUIGrid.removeCheckedRows(gridId[numOfRow]);
    }else{
        AUIGrid.removeRow(gridId[numOfRow], "selectedIndex");
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

    /* 각 그리드 배열번호 항목마다의 유효성 검사 */
    switch (numOfGrid) {
        case 0 :
            /* 입력되지 않은 코드가 있는지 검사*/
            addedRowItems.forEach(function (item){
                if(item.bgItemGroupcode === undefined) {
                    alertCaution("모든 코드를 입력해 주세요.", 1);
                    return false;
                }
            });
            break;
        case 1 :
            break;
        case 2 :
            break;
    }

    const jsonString = JSON.stringify(data);
    CommonUI.ajaxjson(gridSaveUrl[numOfGrid], jsonString, function (){

    });

}