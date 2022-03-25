$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();
    CommonUI.setDatePicker(datePickerTargetIds);
});

/* datepicker를 적용시킬 대상들의 dom id들 */
const datePickerTargetIds = [
    "setDt"
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
    "/api/head/itemPriceList"
]

/* 그리드를 저장할 때 쓰이는 api 배열 */
gridSaveUrl = [
    "/api/head/itemPriceUpdate"
]

/* 0번 그리드의 레이아웃 */
gridColumnLayout[0] = [
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
        dataField: "setDt",
        headerText: "적용일자",
        dataType: "date",
        formatString: "yyyy-mm-dd",
        editable: false,
    }, {
        dataField: "closeDt",
        headerText: "종료일자",
        dataType: "date",
        formatString: "yyyy-mm-dd",
        editable: false,
    }, {
        dataField: "bpBasePrice",
        headerText: "기본금액",
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
        dataField: "bpAddPrice",
        headerText: "추가금액",
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
        dataField: "bpPriceA",
        headerText: "최종금액(A)",
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
        dataField: "bpPriceB",
        headerText: "최종금액(B)",
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
        dataField: "bpPriceC",
        headerText: "최종금액(C)",
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
        dataField: "bpPriceD",
        headerText: "최종금액(D)",
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
        dataField: "bpPriceE",
        headerText: "최종금액(E)",
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
        dataField: "bpRemark",
        headerText: "특이사항",
        style: "grid_textalign_left",
    },
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp[0] = {
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

/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids() {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }
}

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid(numOfGrid, url, code = false) {
    CommonUI.ajax(url, "PARAM", code, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}

/* 실제 숨겨진 input file 이 작동하여 파일이 선택되면 파일명을 사용자에게 보여주는 용도 */
function fileSelected() {
    const fileName = $("#priceUpload")[0].files[0].name;
    $("#fileName").val(fileName);
}

/* 엑셀파일 업로드 팝업 띄우고 닫기 */
function regPricePop() {
    $('#regPrice_popup').addClass('open');
}
function regPriceClose() {
    $('#regPrice_popup').removeClass('open');
}

/* 엑셀파일 처리를 통해 업데이트 */
function applyPrice() {
    const $setDt = $("#setDt");
    const $priceUpload = $("#priceUpload");

    if($setDt.val() === "") {
        alertCaution("가격적용일을 선택해 주세요", 1);
        return false;
    }else if($priceUpload[0].files.length === 0) {
        alertCaution("파일을 첨부해 주세요", 1);
        return false;
    }else if(
        $priceUpload[0].files[0].type !== "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" &&
        $priceUpload[0].files[0].type !== "application/vnd.ms-excel"
    ) {
        alertCaution("올바른 확장자의 파일을 첨부해 주세요", 1);
        return false;
    }

    const url = "/api/head/itemPrice";
    const formData = new FormData();
    formData.append("setDt", $setDt.val());
    formData.append("priceUpload", $priceUpload[0].files[0]);

    CommonUI.ajax(url, "POST", formData, function (req) {
        if(req.sendData.errorListData !== null){
            console.log("req : "+req.sendData.errorListData);
            // 에러 내용이 많을경우 대비 얼터화면 확장할 것. to.성낙원
            alertCancel(req.sendData.errorListData);
        }else{
            AUIGrid.clearGridData(gridId[0]);
            setDataIntoGrid(0, gridCreateUrl[0]);
            alertSuccess("가격 업로드 성공");
        }
    });
    regPriceClose();

    $setDt.val("");
    $priceUpload.val("");
    $("#fileName").val("");
}

/* 상품 그룹 가격 페이지 필터링 */
function filterItems() {
    const s_bgName = $("#s_bgName").val();
    const s_biItemcode = $("#s_biItemcode").val();
    const s_biName = $("#s_biName").val();
    const s_setDt = $("#s_setDt").val();
    setDataIntoGrid(0, gridCreateUrl[0], {bgName: s_bgName, biItemcode: s_biItemcode, biName: s_biName, setDt: s_setDt});
}

function filterReset() {
    AUIGrid.clearGridData(gridId[0]);
}

/* 체크된 행이 존재할 경우 체크된 행을 삭제하고, 아닐 경우 선택된 행 하나만 삭제 */
function removeRow() {
    if(AUIGrid.getCheckedRowItems(gridId[0]).length) {
        AUIGrid.removeCheckedRows(gridId[0]);
    }else{
        AUIGrid.removeRow(gridId[0], "selectedIndex");
    }
}

function saveRegPrice() {
    // 추가된 행 아이템들(배열) 차후 데이터 처리 방식에 따라 바뀜
    // const addedRowItems = AUIGrid.getAddedRowItems(gridTargetDiv[0]);

    // 수정된 행 아이템들(배열)
    const updatedRowItems = AUIGrid.getEditedRowItems(gridTargetDiv[0]);

    // 삭제된 행 아이템들(배열)
    const deletedRowItems = AUIGrid.getRemovedItems(gridTargetDiv[0]);

    // 서버로 보낼 데이터 작성
    const data = {
        //"add" : addedRowItems,
        "update" : updatedRowItems,
        "delete" : deletedRowItems,
    };


    CommonUI.ajax(gridSaveUrl[0], "MAPPER", data, function () {
        AUIGrid.clearGridData(gridId[0]);
        setDataIntoGrid(0, gridCreateUrl[0]);
        alertSuccess("상품가격 업데이트를 완료되었습니다.");
    });

}

/* 상품코드 텍스트필드 입력시 유효성에 맞추기 */
function itemCodeValidator() {
    const $sBiIteamcode = $("#s_biItemcode");
    let targetString = $sBiIteamcode.val();
    $sBiIteamcode.val(targetString.replace(/[^a-zA-Z0-9]/g, ""));
}

// 엑셀 내보내기(Export)
function exportToXlsx() {
    //FileSaver.js 로 로컬 다운로드가능 여부 확인
    if(!AUIGrid.isAvailableLocalDownload(gridId[0])) {
        alertCaution("파일 다운로드가 불가능한 브라우저 입니다.", 1);
        return;
    }
    AUIGrid.exportToXlsx(gridId[0], {
        fileName : "상품그룹가격_"+$("#s_setDt").val(),
        progressBar : true,
    });
}
