$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<1; i++) {
        //setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {

    });

    CommonUI.setDatePicker(datePickerTargetIds);
});

/* datepicker를 적용시킬 대상들의 dom id들 */
const datePickerTargetIds = [
    "s_tempPriceApplyDate", "tempUploadDate"
];

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
    "grid_main"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/a"
]

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/a"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
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
        dataField: "",
        headerText: "적용일자",
    }, {
        dataField: "",
        headerText: "종료일자",
    }, {
        dataField: "bpBasePrice",
        headerText: "기본금액",
    }, {
        dataField: "highClassYn",
        headerText: "명품여부",
    }, {
        dataField: "bpAddPrice",
        headerText: "추가금액",
    }, {
        dataField: "bpPriceA",
        headerText: "최종금액(A)",
    }, {
        dataField: "bpPriceB",
        headerText: "최종금액(B)",
    }, {
        dataField: "bpPriceC",
        headerText: "최종금액(C)",
    }, {
        dataField: "bpPriceD",
        headerText: "최종금액(D)",
    }, {
        dataField: "bpPriceE",
        headerText: "최종금액(E)",
    }, {
        dataField: "bpRemark",
        headerText: "특이사항",
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

function fileSelected() {
    const fileName = $("#fileExplorer")[0].files[0].name;
    $("#fileName").val(fileName);
}

function regPricePop() {
    $('#regPrice_popup').addClass('open');
}

function regPriceClose() {
    $('#regPrice_popup').removeClass('open');
}

function applyPrice() {

    regPriceClose();
}

/* 상품 그룹 가격 페이지 필터링 */
function filterItems() {
    AUIGrid.clearFilterAll(gridID[0]);

    const s_bgItemGroupcode = $("#s_bgItemGroupcode").val();
    const s_biItemcode = $("#s_biItemcode").val();
    const s_biName = $("#s_biName").val();
    const s_highClassYn = $("#s_highClassYn").val();
    const s_tempPriceApplyDate = $("#s_tempPriceApplyDate").val();

    if(s_bgItemGroupcode !== "") {
        AUIGrid.setFilter(gridID[0], "bgItemGroupcode", function (dataField, value, item) {
            return s_bgItemGroupcode === value;
        });
    }
    if(s_biItemcode !== "") {
        AUIGrid.setFilter(gridID[0], "biItemcode", function (dataField, value, item) {
            return s_biItemcode === value;
        });
    }
    if(s_biName !== "") {
        AUIGrid.setFilter(gridID[0], "biName", function (dataField, value, item) {
            return new RegExp(s_biName).test(value);
        });
    }
    if(s_highClassYn !== "") {
        AUIGrid.setFilter(gridID[0], "highClassYn", function (dataField, value, item) {
            return new RegExp(s_highClassYn).test(value);
        });
    }
    if(s_tempPriceApplyDate !== "") {
        AUIGrid.setFilter(gridID[0], "tempPriceApplyDate", function (dataField, value, item) {
            return s_tempPriceApplyDate === value;
        });
    }
}

function filterReset() {
    AUIGrid.clearFilterAll(gridID[0]);
}

function removeRow() {
    if(AUIGrid.getCheckedRowItems(gridId[0]).length){
        AUIGrid.removeCheckedRows(gridId[0]);
    }else{
        AUIGrid.removeRow(gridId[0], "selectedIndex");
    }
}

function saveRegPrice() {
    // 추가된 행 아이템들(배열)
    // const addedRowItems = AUIGrid.getAddedRowItems(gridTargetDiv[numOfGrid]);

    // 수정된 행 아이템들(배열)
    const updatedRowItems = AUIGrid.getEditedRowItems(gridTargetDiv[numOfGrid]);

    // 삭제된 행 아이템들(배열)
    const deletedRowItems = AUIGrid.getRemovedItems(gridTargetDiv[numOfGrid]);

    // 서버로 보낼 데이터 작성
    const data = {
        //"add" : addedRowItems,
        "update" : updatedRowItems,
        "delete" : deletedRowItems,
    };

}