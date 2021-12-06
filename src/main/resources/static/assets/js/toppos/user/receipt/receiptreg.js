
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids(true);

    /* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. */
    for(let i=0; i<1; i++) {
        setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /* 가상키보드의 사용 선언 */
    vkey = new VKeyboard();

    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
    });

    $(".bgItemGroupcode").on("click", function () {
        onPopReceiptReg(this.value);
    });



    /* 가격 정보를 받아서 저장해둠 */
    /*
    CommonUI.ajax("", "GET", false, function(req) {
        priceData = req.sendData;
    });
    */

});

/* 상품 주문을 받을 때 적용되는 가격 정보 데이터를 API로 불러와 미리 저장 */
let priceData;

let selectedCustomer;

/* 가상키보드 사용을 위해 */
let vkey;
let vkeyProp = [];
let vkeyTargetId = ["searchCustomerField", "fdTag", "AAA", "BBB", "CCC"];

vkeyProp[0] = {
    title : "고객 검색",
    // 고객 처리 입력 후 바로 검색되도록 콜백처리
}

vkeyProp[1] = {
    title : "택번호 입력",
    defaultKeyboard : 2,
    // 택번호 입력 후 바로 저장되도록 처리
}

vkeyProp[2] = {
    title : "수선 내역 입력",
}

vkeyProp[3] = {
    title : "추가 처리 입력",
}

vkeyProp[4] = {
    title : "특이사항 입력",
}

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
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false,
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
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false,
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
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    rowNumHeaderText : "순번",
    enableColumnResize : false,
    showStateColumn : true,
    enableFilter : true,
};

function onShowVKeyboard(num) {
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids(type = false) {
    if(type) {
        for (const i in gridColumnLayout) {
            gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
        }
    }else{
        gridId[0] = AUIGrid.create(gridTargetDiv[0], gridColumnLayout[0], gridProp[0]);
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

function onSearchCustomer() {
    const params = {searchType : $("#searchCustomerType").val(),
            searchString : $("#searchCustomerField").val()};
    CommonUI.ajax("/api/user/customerInfo", "GET", params, function (req) {
       const items = req.sendData.gridListData;

        $("#searchCustomerType").val(0);
        $("#searchCustomerField").val("");
       if(items.length === 1) {
           onPutCustomer(items[0]);
       }else if(items.length > 1) {
           AUIGrid.setGridData(gridId[1], items);
           // 고객선택창 열기
       }
    });
}

function onSelectCustomer() {
    selectedCustomer = AUIGrid.getSelectedRows(gridId[1]);
    if(selectedCustomer.length) {
        onPutCustomer(selectedCustomer[0]);
        // 고객선택창 닫기 추가
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function onPutCustomer(selectedCustomer) {
    $("#bcGrade").html(selectedCustomer.bcGrade); // 등급에 따른 표시변화
    switch (selectedCustomer.bcGrade) {
        case "일반" :  // 고객구분이 결정나고 나서 작업
            break;
        case "VIP" :
            break;
        case "VVIP" :
            break;
    }
    $("#bcName").html(selectedCustomer.bcName + "님");
    $("#bcValuation").attr("class",
        "propensity__star propensity__star--" + selectedCustomer.bcValuation);
    $("#bcAddress").html(selectedCustomer.bcAddress);
    $("#bcHp").html(selectedCustomer.bcHp);
    $("#bcRemark").html(selectedCustomer.bcRemark);
    // 최근방문일

    alertSuccess("성함 : " + selectedCustomer.bcName + " 님");
}


function onCloseSelectCustomer() {
    // 고객 선택을 취소하고 돌아가시겠습니까? 물음 추가
    // 고객선택창 닫기 추가
}


/* 하단의 세탁물 종류 버튼 클릭시 해당 세탁물 대분류 코드를 가져와 팝업을 띄운다. */
function onPopReceiptReg(bgItemGroupcode) {
    console.log(bgItemGroupcode);
}



/* 웹 카메라와 촬영 작업중 */
async function onPopTakePicture() {
    const cameraList = document.getElementById("cameraList");
    const stream = await navigator.mediaDevices.getUserMedia({audio: false, video: true});
    let videoInput = [];
    await navigator.mediaDevices.enumerateDevices().then(devices => {
        for(let i = 0; i < devices.length; i++) {
            if(devices[i].kind === "videoinput") {
                videoInput.push({
                    label : devices[i].label,
                    deviceId : devices[i].deviceId
                });
            }
        }
    });
    videoInput.forEach(e => {
        const option = document.createElement('option');
        option.value = e.deviceId;
        option.text = e.label;
        cameraList.appendChild(option);
    });

    const screen = document.getElementById("cameraScreen");
    screen.srcObject = stream;
}

async function onChangeCamera(deviceId) {
    const stream = await navigator.mediaDevices.getUserMedia({audio: false, video: {
        deviceId : deviceId ? {exact : deviceId} : undefined
        }});
    const screen = document.getElementById("cameraScreen");
    screen.srcObject = stream;
}

function toggleBottom() {
    var element = document.getElementsByClassName("regist__bottom");
    element[0].classList.toggle("active");

    var grid = document.getElementById("grid_requestList");
    grid.classList.toggle("active");

    AUIGrid.resize(gridId[0]);
}