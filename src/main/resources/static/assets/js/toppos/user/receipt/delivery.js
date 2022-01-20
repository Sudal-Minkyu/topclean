/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        /* 해당 검색은 기존 customerInfo API를 사용해 고객을 선택할 예정이며,
         * 고객에 따른 디테일 S5, S8 리스트가 고객 선택과 동시에 떠야한다.
         * 고객선택즉시 -> 디테일리스트 함수를 연계하여 호출하여 활용할 예정 */
        customerInfo: { // integrate 의 customerInfo와 같은 구성이지만 택번호도 포함
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소
            searchString: "sr", // 검색문자
        },

        /* integrate 의 franchiseRequestDetailSearch와 유사한 dto 구성, 하지만 다른 조건 API */
        디테일리스트: {
            bcId: "n"
        },
    },
    receive: {
        customerInfo: { // integrate 의 customerInfo와 같은 구성
            bcAddress: "s",
            bcGrade: "s",
            bcHp: "s",
            bcId: "nr",
            bcLastRequestDt: "s",
            bcName: "s",
            bcRemark: "s",
            bcValuation: "s",
            beforeUncollectMoney: "nr",
            saveMoney: "nr",
        },
        
        디테일리스트: {
            frRefType: "sr", // 1월 19일 추가
            bcName: "s", // 고객의 이름
            frYyyymmdd: "s", // 접수일자
            fdId: "n",
            frId: "n",
            frNo: "s",
            fdTag: "s",
            fdPreState: "s", // 1월 17일 추가
            fdState: "s",
            biItemcode: "s",
            fdS2Dt: "s",
            fdS3Dt: "s",
            fdS4Dt: "s",
            fdS5Dt: "s",
            fdS6Dt: "s",
            fdCancel: "s",
            fdCacelDt: "s",
            fdColor: "s",
            fdPattern: "s",
            fdPriceGrade: "s",
            fdOriginAmt: "n",
            fdNormalAmt: "n",
            fdPollution: "n",
            fdDiscountGrade: "s",
            fdDiscountAmt: "n",
            fdQty: "n",
            fdRequestAmt: "n",
            fdSpecialYn: "s",
            fdUrgentYn: "s",
            fdTotAmt: "n",
            fdRemark: "s",
            fdEstimateDt: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdAdd2Amt: "n",
            fdAdd2Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "n",
            fdPollutionLevel: "n",
            fdWaterRepellent: "n",
            fdStarch: "n",
            fpCancelYn: "sr",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    
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
            "grid_laundryList", "grid_selectedLaundry"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            grids.s.columnLayout[0] = [
                {
                    dataField: "",
                    headerText: "구분",
                    width: 40,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.frRefType[value];
                    },
                }, {
                    dataField: "",
                    headerText: "고객명",
                }, {
                    dataField: "",
                    headerText: "접수일자",
                }, {
                    dataField: "",
                    headerText: "인도일자",
                }, {
                    dataField: "",
                    headerText: "택번호",
                }, {
                    dataField: "",
                    headerText: "상품명",
                }, {
                    dataField: "",
                    headerText: "처리내역",
                }, {
                    dataField: "",
                    headerText: "접수금액",
                }, {
                    dataField: "",
                    headerText: "현재상태",
                }, {
                    dataField: "",
                    headerText: "급세탁",
                }, {
                    dataField: "",
                    headerText: "특이사항",
                }, {
                    dataField: "",
                    headerText: "지사입고",
                }, {
                    dataField: "",
                    headerText: "지사출고",
                }, {
                    dataField: "",
                    headerText: "완성일자",
                },
            ];

            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : true,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[0] = [
                {
                    dataField: "",
                    headerText: "접수일",
                }, {
                    dataField: "",
                    headerText: "택번호",
                }, {
                    dataField: "",
                    headerText: "상품명",
                },
            ];

            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : true,
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

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grids.t.basicTrigger();
}