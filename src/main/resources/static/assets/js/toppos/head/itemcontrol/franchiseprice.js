
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<1; i++) {
        setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /* 1번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        AUIGrid.clearGridData(gridId[1]);
        selectedFrCode = e.item.frCode;
        setDataIntoGrid(1, gridCreateUrl[1], {frCode : selectedFrCode});
    });

    AUIGrid.bind(gridId[1], "cellClick", function (e) {
        if(e.columnIndex < 4 && AUIGrid.isAddedById(gridId[1], e.rowIdValue) ) {
            setDataIntoGrid(2, gridCreateUrl[2]);
            itemListPop(e.rowIdValue);
        }
    });

    AUIGrid.bind(gridId[2], "cellDoubleClick", function (e) {
        putItemListInfo(e.item);
    });

    AUIGrid.bind(gridId[1], "cellEditBegin", function (e) {
        if(e.dataField === "bsIte" && e.value !== "" && !AUIGrid.isAddedById(gridId[1], e.item._$uid)) {
            setTimeout(function (){
                AUIGrid.showToastMessage(gridId[1], e.rowIndex, e.columnIndex, "입력된 중분류 코드는 수정할 수 없습니다.");
            }, 0);
        }
    });
});

/* 차후 팝업에서 목표가 선택되었을 때 목표가 반영될 1번그리드의 행 고유번호를 저장해둔다. */
let idIndex = "";
/* 선택한 가맹점의 코드가 저장된다. */
let selectedFrCode = "";

/* 그리드 생성과 운영에 관한 중요 변수들. grid라는 이름으로 시작하도록 통일했다. */
const gridId = [];
let gridTargetDiv = [];
const gridData = [];
const gridColumnLayout = [];
const gridProp = [];
let gridCreateUrl = [];
let gridSaveUrl = [];

/* 그리드를 뿌릴 대상 div의 id들 */
gridTargetDiv = [
    "grid_franchise", "grid_franchisePrice", "grid_itemList"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/head/franchiseList", "/api/head/franchisePriceList", "/api/head/itemGroupCList"
];

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/head/franchisePrice"
];

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "frName",
        headerText: "가맹점명",
        style: "grid_textalign_left",
    }, {
        dataField: "frCode",
        headerText: "가맹점코드",
    }, {
        dataField: "frRefCode",
        headerText: "관리코드",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
        dataType: "date",
        formatString: "yyyy-mm-dd",
    }, {
        dataField: "brName",
        headerText: "배정지사명",
        style: "grid_textalign_left",
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    showAutoNoDataMessage: true,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : true,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[1] = [
    {
        dataField: "biItemcode",
        headerText: "상품코드",
        editable: false,
    }, {
        dataField: "bgName",
        headerText: "대분류",
        style: "grid_textalign_left",
        editable: false,
    }, {
        dataField: "bsName",
        headerText: "중분류",
        style: "grid_textalign_left",
        editable: false,
    }, {
        dataField: "biName",
        headerText: "상품명",
        style: "grid_textalign_left",
        editable: false,
    }, {
        dataField: "bfPrice",
        headerText: "적용금액",
        style: "grid_textalign_right",
        dataType: "numeric",
        editRenderer: {
            type: "InputEditRenderer",
            autoThousandSeparator: "true",
            maxlength: 8,
            onlyNumeric: true,
            allowPoint: false,
            allowNegative: false,
        },
    }, {
        dataField: "bfRemark",
        headerText: "특이사항",
        style: "grid_textalign_left",
    },
];

gridProp[1] = {
    editable : true,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    showAutoNoDataMessage: false,
    enableColumnResize : true,
    showRowAllCheckBox: true,
    showRowCheckColumn: true,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : true,
};

gridColumnLayout[2] = [
    {
        dataField: "biItemcode",
        headerText: "상품코드",
    }, {
        dataField: "bgName",
        headerText: "대분류",
        style: "grid_textalign_left",
    }, {
        dataField: "bsName",
        headerText: "중분류",
        style: "grid_textalign_left",
    }, {
        dataField: "biName",
        headerText: "상품명",
        style: "grid_textalign_left",
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    showAutoNoDataMessage: true,
    enableColumnResize : true,
    showRowAllCheckBox: false,
    showRowCheckColumn: false,
    showRowNumColumn : false,
    showStateColumn : false,
    enableFilter : true,
    width : 660,
    height : 480,
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


/* 상품팝업을 띄우고 차후 입력에 활용될 클릭된 행의 고유번호를 받아 저장한다. */
function itemListPop(rowIdValue) {
    idIndex = rowIdValue;
    filterItemList(2);
    $('#itemList_popup').addClass('active');
    AUIGrid.resize(gridId[2]);
}

function itemListSelect() {
    const selectedItem = AUIGrid.getSelectedItems(gridId[2])[0];
    if(selectedItem) {
        putItemListInfo(selectedItem.item);
    } else {
        alertCaution("지사를 선택해 주세요", 1);
    }
}

function putItemListInfo(item) {
    delete item["_$uid"];
    item["_$uid"] = idIndex;
    AUIGrid.updateRowsById(gridId[1], item);
    itemListClose();
}

function itemListClose() {
    $('#itemList_popup').removeClass('active');
}

/* 가맹점 필터링 type 1 : 필터링  type 2 : 필터링 초기화 */
function filterFranchise(type) {
    selectedFrCode = "";
    AUIGrid.clearGridData(gridId[1]);
    switch (type) {
        case 1 :
            const s_frCode = $("#s_frCode").val();
            const s_frRefCode = $("#s_frRefCode").val();
            const s_frName = $("#s_frName").val();
            setDataIntoGrid(0, gridCreateUrl[0], {frCode:s_frCode, frRefCode: s_frRefCode, frName: s_frName});
            break;
        case 2 :
            setDataIntoGrid(0, gridCreateUrl[0]);
            break;
    }
}

/* 상품b 필터링 type 1 : 필터링  type 2 : 필터링 초기화 */
function filterItemList(type) {
    switch (type) {
        case 1 :
            const s_biItemcode = $("#s_biItemcode").val();
            const s_biName = $("#s_biName").val();
            setDataIntoGrid(2, gridCreateUrl[2], {biItemcode: s_biItemcode, biName: s_biName});
            break;
        case 2 :
            setDataIntoGrid(2, gridCreateUrl[2]);
            break;
    }
}

function addPriceRow() {
    if(selectedFrCode) {
        AUIGrid.addRow(gridId[1], {}, "last");
    } else {
        alertCaution("라인을 추가하실 가맹점을 선택해 주세요.", 1);
    }
}

/* 1번 그리드의 선택된 체크박스 항목들이나, 없을 경우 선택된 행 하나를 삭제 */
function deletePriceRow() {
    if(AUIGrid.getCheckedRowItems(gridId[1]).length){
        AUIGrid.removeCheckedRows(gridId[1]);
    }else{
        AUIGrid.removeRow(gridId[1], "selectedIndex");
    }
}

/* 1번 그리드의 저장작업 */
function savePriceList() {
    // 추가된 행 아이템들(배열)
    const addedRowItems = dataRefinary(AUIGrid.getAddedRowItems(gridTargetDiv[1]));
    let proceed = true;
    addedRowItems.forEach(element => {
        if(element["biItemcode"] === undefined) {
            alertCaution("상품을 선택해 주세요.", 1);
            proceed = false;
        }else if(element["bfPrice"] === undefined) {
            alertCaution("적용금액을 입력해 주세요.", 1);
            proceed = false;
        }
    });

    // 수정된 행 아이템들(배열)
    const updatedRowItems = dataRefinary(AUIGrid.getEditedRowItems(gridTargetDiv[1]));

    // 삭제된 행 아이템들(배열)
    const deletedRowItems = dataRefinary(AUIGrid.getRemovedItems(gridTargetDiv[1]));

    // 서버로 보낼 데이터 작성
    const data = {
        "add" : addedRowItems,
        "update" : updatedRowItems,
        "delete" : deletedRowItems
    };

    if(proceed) {
        CommonUI.ajax(gridSaveUrl[0], "MAPPER", data, function () {
            AUIGrid.clearGridData(gridId[1]);
            setDataIntoGrid(1, gridCreateUrl[1], {frCode: selectedFrCode});
            alertSuccess("등록이 완료되었습니다");
        });
    }

}

/* API 통신에 필요한 요소는 추가하고, 필요없는 요소들은 제거 */
function dataRefinary(itemArray) {
    itemArray.forEach(element => {
        element["frCode"] = selectedFrCode;
        delete element["bgItemGroupcode"];
        delete element["bgName"];
        delete element["biItemSequence"];
        delete element["biName"];
        delete element["biRemark"];
        delete element["biUseYn"];
        delete element["bsItemGroupcodeS"];
        delete element["bsName"];
        delete element["_$uid"];
    });
    return itemArray;
}
