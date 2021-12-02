
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<1; i++) {
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
    "grid_requestList", "grid_customerList", "grid_tempSaveList"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
gridCreateUrl = [
    "/api/a", "/api/b"
]

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/a", "/api/b"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
    {
        dataField: "fdTag",
        headerText: "택번호",
    }, {
        dataField: "biName",
        headerText: "상품명",
    }, {
        dataField: "sumProcess",
        headerText: "처리내용",
    }, {
        dataField: "fdNormalAmt",
        headerText: "정상금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdRepairAmt",
        headerText: "수선금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdAdd1Amt",
        headerText: "추가금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdDiscountAmt",
        headerText: "할인금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdQty",
        headerText: "수량",
        dataType: "numeric",
        autoThousandSeparator: "true",
    }, {
        dataField: "fdRequestAmt",
        headerText: "접수금액",
        dataType: "numeric",
        autoThousandSeparator: "true",
        labelFunction : function (rowIndex, columnIndex, value, headerText, item) {
            return (item.fdNormalAmt + item.fdRepairAmt + item.fdAdd1Amt - item.fdDiscountAmt) * item.fdQty;
        }
    }, {
        dataField: "fdColor",
        headerText: "색",
    }, {
        dataField: "fdRemark",
        headerText: "특이사항",
    }, {
        dataField: "frEstimateDt",
        headerText: "출고예정일",
        dataType: "date",
        formatString: "yyyy-mm-dd",
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "userid",
    enableColumnResize : false,
    rowIdTrustMode : true,
    showStateColumn : true,
    enableFilter : true
};

gridColumnLayout[1] = [
    {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "전화번호",
    }, {
        dataField: "bcAddress",
        headerText: "주소",
    },
];

gridProp[1] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "userid",
    enableColumnResize : false,
    rowIdTrustMode : true,
    enableFilter : true,
};

gridColumnLayout[2] = [
    {
        dataField: "frInsertDt",
        headerText: "접수시간",
    }, {
        dataField: "bcName",
        headerText: "고객명",
    }, {
        dataField: "bcHp",
        headerText: "전화번호",
    }, {
        dataField: "",
        headerText: "삭제",
    },
];

gridProp[2] = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    rowIdField : "userid",
    enableColumnResize : false,
    rowIdTrustMode : true,
    showStateColumn : true,
    enableFilter : true,
};


/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids() {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }
}

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid(numOfGrid, url) {

    if(numOfGrid === 0) {
        let item = [];
        for (let i = 1; i <= 20; i++) {
            item.push({
                fdTag : "ABC-00"+i,
                biName : "상품상품"+i,
                sumProcess : "제다추수",
                fdNormalAmt : i * 1000,
                fdRepairAmt : (21-i) * 500,
                fdAdd1Amt : i * 500,
                fdDiscountAmt : i * 200,
                fdQty : 21-i,
                fdColor : "색상",
                fdRemark : "특이사항특이사항",
                frEstimateDt : "18450815",
            });
        }
        AUIGrid.setGridData(gridId[0], item);
    }

    /*
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
    */
}