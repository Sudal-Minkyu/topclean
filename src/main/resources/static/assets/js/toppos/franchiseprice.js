
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<0; i++) {
        setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
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
    "grid_franchise", "grid_franchisePrice", "grid_itemList"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/a", "/api/b", "/api/c"
]

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/a", "/api/b", "/api/c"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "frName",
        headerText: "가맹점명",
    }, {
        dataField: "frRefCode",
        headerText: "관리코드",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brName",
        headerText: "배정지사명",
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
    rowIdField : "",
    enableColumnResize : false,
    showStateColumn : true,
    enableFilter : true
};

gridColumnLayout[1] = [
    {
        dataField: "biItemcode",
        headerText: "상품코드",
    }, {
        dataField: "bgItemGroupcode",
        headerText: "대분류",
    }, {
        dataField: "bgItemGroupcodeS",
        headerText: "중분류",
    }, {
        dataField: "biName",
        headerText: "상품명",
    }, {
        dataField: "highClassYn",
        headerText: "명품",
    }, {
        dataField: "bfPrice",
        headerText: "적용금액",
    }, {
        dataField: "bfRemark",
        headerText: "특이사항",
    },
];

gridProp[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowIdField : "",
    enableColumnResize : false,
    showStateColumn : true,
    showRowNumColumn : false,
    enableFilter : true,
};

gridColumnLayout[2] = [
    {
        dataField: "biItemcode",
        headerText: "상품코드",
    }, {
        dataField: "bsItemGroup",
        headerText: "대분류",
    }, {
        dataField: "bsItemGroupS",
        headerText: "중분류",
    }, {
        dataField: "biName",
        headerText: "상품명",
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowIdField : "",
    enableColumnResize : false,
    rowIdTrustMode : true,
    showStateColumn : true,
    showRowNumColumn : false,
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
function setDataIntoGrid(numOfGrid, url) {
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}


/* 상품 목록 업로드 팝업 띄우고 닫기 */
function itemListPop() {
    filterItemList(2);
    $('#itemList_popup').addClass('open');
}
function itemListClose() {
    $('#itemList_popup').removeClass('open');
}

function filterFranchise(type) {
    switch (type) {
        case 1 :
            break;
        case 2 :
            break;
    }
}

function filterItemList(type) {
    switch (type) {
        case 1 :
            AUIGrid.clearFilterAll(gridId[2]);
            const s_biItemcode = $("#s_biItemcode").val();
            const s_biName = $("#s_biName").val();
            if(s_biItemcode !== "") {
                AUIGrid.setFilter(gridId[2], "biItemcode", function (dataField, value, item) {
                    return s_biItemcode === value;
                });
            }
            if(s_biName !== "") {
                AUIGrid.setFilter(gridId[2], "biName", function (dataField, value, item) {
                    return s_biName === value;
                });
            }
            break;
        case 2 :
            AUIGrid.clearFilterAll(gridId[2]);
            break;
    }
}