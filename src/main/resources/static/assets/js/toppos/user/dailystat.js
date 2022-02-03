/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        businessdayList: {
            filterFromDt: "sr",
            filterToDt: "sr",
        }
    },
    receive: {
        businessdayList: {
            yyyymmdd: "s",
            frQtyAll: "",
            fdRetryYnAll: "",
            biItemGroupAll: "",
            frTotalAmountAll: "",
            fpAmtType02All: "",
            fpAmtType01All: "",
            fsAmtType02All: "",
            fpAmtCancelAll: "",
            fpAmtUncollectAll: "",
            totalAverageAll: "",
            totalReceipt: "",
            totalDelivery: "",
            averageReceiptMoney: "",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getMainData: "/api/user/businessdayList",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getMainData(filterCondition) {
        dv.chk(filterCondition, dtos.send.businessdayList, "메인그리드 필터링에 필요한 데이터 보내기");
        CommonUI.ajax(urls.getMainData, "GET", filterCondition, function (res) {
            console.log(res);
            const data = mergeData(res.sendData);
            grids.f.setData(0, data);
        });
    },
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grids = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_main"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: "yyyymmdd",
                    headerText: "일자",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    headerText: "접수건수",
                    children: [{
                        dataField: "frQtyAll",
                        headerText: "총건수",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "fdRetryYnAll",
                        headerText: "재세탁",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "biItemGroupAll",
                        headerText: "부착물",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },]
                }, {
                    dataField: "frTotalAmountAll",
                    headerText: "접수금액",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    headerText: "입금액",
                    children: [ {
                        dataField: "fpAmtType02All",
                        headerText: "카드결제",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "fpAmtType01All",
                        headerText: "현금결제",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "fsAmtType02All",
                        headerText: "적립금사용",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "fpAmtCancelAll",
                        headerText: "취소금액",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "fpAmtUncollectAll",
                        headerText: "미수결제",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },]
                }, {
                    dataField: "totalAverageAll",
                    headerText: "1점<br>평균단가",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    headerText: "방문고객",
                    children: [{
                        dataField: "totalReceipt",
                        headerText: "접수",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    }, {
                        dataField: "totalDelivery",
                        headerText: "출고",
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },]
                }, {
                    dataField: "averageReceiptMoney",
                    headerText: "고객평균<br>접수단가",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grids.s.columnLayout) {
                grids.s.id[i] = AUIGrid.create(grids.s.targetDiv[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        getData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        setData(numOfGrid, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clearData(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) { // 해당 배열 번호 그리드의 엑스트라 체크박스 선택된 (아이템 + 행번호) 객체 반환
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        }
    },

    t: {
        basicTrigger() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#filterByDate").on("click", function () {
                getRefinedData();
            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {

    grids.f.initialization();
    grids.f.create();

    trigs.s.basic();

    enableDatepicker();
}

function enableDatepicker() {
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(today);
    $("#" + datePickerTargetIds[1]).val(today);

    /*
    * JqueyUI datepicker의 기간 A~B까지를 선택할 때 선택한 날짜에 따라 제한을 주기 위한 DOM id의 배열이다.
    * 배열 내 각 내부 배열은 [~부터의 제한 대상이 될 id, ~까지의 제한 대상이 될 id] 이다.
    * */
    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

function getRefinedData() {
    const filterCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
    }
    comms.getMainData(filterCondition);
}

function mergeData(sendData) {
    const data = [];
    addData(sendData["결제 관련"]);
    addData(sendData["방문고객 접수관련"]);
    addData(sendData["방문고객 출고관련"]);
    addData(sendData["적립금 관련"]);
    addData(sendData["접수 관련"]);
    addData(sendData["접수세부 관련"]);
    return data;
    function addData(targetList) {
        targetList.forEach(obj => {
            const keys = Object.keys(obj);
            let isNewYyyymmdd = true;
            data.forEach(dataObj => {
                if(obj.yyyymmdd === dataObj.yyyymmdd) {
                    isNewYyyymmdd = false;
                    for(let i = 0; i < keys.length; i++) {
                        dataObj[keys[i]] = obj[keys[i]];
                    }
                }
            });
            if(isNewYyyymmdd) {
                data.push(obj);
            }
        });
    }
}