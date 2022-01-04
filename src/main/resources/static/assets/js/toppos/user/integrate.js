$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dto = {
    send: {
        통합조회고객및택번호검색: {
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소, 4 택번호
            searchString: "sr", // 검색문자
            filterCondition: "sr", // 0 전체, 1 고객접수, 2 자사입고, 3 가맹점입고, 4 입고완료, 5 확인품, 6 가맹검품
            filterFromDt: "sr", // 시작 조회기간
            filterToDt: "sr", // 종료 조회기간
        }
    },
    receive: {
        통합조회고객및택번호검색: { // 접수페이지의 고객정보 검색 결과의 정보와 동일
            고객정보: {
                bcId: "n",
                bcName: "s",
                bcGrade: "s",
                bcValuation: "s",
                bcAddress: "s",
                bcHp: "s",
                beforeUncollectMoney: "n",
                saveMoney: "n",
                bcRemark: "s",
                bcLastRequestDt: "s",
            },
            세부접수정보: { // 접수세부테이블의 모든 정보 + 해당 접수세부테이블 고객 이름
                bcName: "s", // 위 고객정보의 검색 결과와 별도로 택번호 검색시, 혹은 필터링만 걸어서 검색할 경우에도 필요

                insertDt: "s", // 접수일자인데 이게 맞는지 잘 모르겠음
                fdId: "n",
                frId: "n",
                fdTag: "s",
                biItemcode: "s",
                fdState: "s",
                fdStateDt: "d",
                fdPreState: "d",
                fdS2Dt: "s",
                fdS3Dt: "s",
                fdS4Dt: "s",
                fdS5Dt: "s",
                fdPreStateDt: "d",
                fdCancel: "s",
                fdCancelDt: "s",
                fdColor: "s",
                fdPattern: "s",
                fdPriceGrade: "s",
                fdOriginAmt: "n",
                fdNormalAmt: "n",
                fdAdd2Amt: "n",
                fdAdd2Remark: "s",
                fdPollution: "n",
                fdDiscountGrade: "s",
                fdDiscountAmt: "n",
                fdQty: "n",
                fdRequestAmt: "n",
                fdSpecialYn: "s",
                fdTotAmt: "n",
                fdRemark: "s",
                fdEstimateDt: "s",
                modifyId: "d",
                modifyDt: "d",
                fdRetryYn: "s",
                fdPressed: "n",
                fdAdd1Amt: "n",
                fdAdd1Remark: "s",
                fdRepairAmt: "n",
                fdRepairRemark: "s",
                fdWhitening: "n",
                fdPollutionLevel: "n",
                fdWaterRepellent: "n",
                fdStarch: "n",
            },
            검품정보: {
                검품사진목록: {
                    ffPath: "s",
                    ffFilename: "s",
                    ffRemark: "s",
                },
                fiId: "n",
                fdId: "n",
                frCode: "d",
                brCode: "d",
                fiType: "s",
                fiComment: "s",
                fiAddAmt: "n",
                fiPhotoYn: "s",
                fiSendMsgYn: "s",
                fiCustomerConfirm: "s",
                fiProgressStateDt: "s",
                fiMessage: "s",
                fiMessageSendDt: "s",
                modifyId: "d",
                modifyDt: "d",
                insertId: "d",
                insertDt: "d",
            },
        },
        itemGroupAndPriceList: { // 접수페이지 시작때 호출되는 API와 같은 API
            addAmountData: {
                baName: "s",
                baRemark: "",
            },
            addCostData: {
                bcChildRt: "nr",
                bcHighRt: "nr",
                bcPollution1: "nr",
                bcPollution2: "nr",
                bcPollution3: "nr",
                bcPollution4: "nr",
                bcPollution5: "nr",
                bcPremiumRt: "nr",
                bcPressed: "nr",
                bcStarch: "nr",
                bcVipDcRt: "nr",
                bcVvipDcRt: "nr",
                bcWaterRepellent: "nr",
                bcWhitening: "nr",
            },
            etcData: {
                fdTag: "s",
                frBusinessNo: "s",
                frCode: "s",
                frEstimateDate: "s",
                frName: "s",
                frRpreName: "s",
                frTelNo: "s",
            },
            repairListData: {
                baName: "s",
                baRemark: "",
            },
            userItemGroupSListData: {
                bgItemGroupcode: "s",
                bsItemGroupcodeS: "s",
                bsName: "s",
            },
            userItemGroupSortData: {
                bgSort: "n",
                bgItemGroupcode: "s",
                bgName: "s",
                bgIconFilename: "s",
            },
            userItemPriceSortData: {
                bfSort: "n",
                bgName: "s",
                biItemcode: "s",
                biName: "s",
                bsName: "s",
                price: "n",
            }
        },
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const ajax = {
    getInitialData() {
        CommonUI.ajax("/api/user/itemGroupAndPriceList", "GET", false, function (req){
            window.initialData = req.sendData;
            console.log(initialData);
        });
    },
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function (req) {
            grid.s.data[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(grid.s.id[numOfGrid], grid.s.data[numOfGrid]);
        });
    },
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grid = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_main"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/"
            ],
            read: [
                "/api/"
            ],
            update: [
                "/api/"
            ],
            delete: [
                "/api/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grid.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grid.s.columnLayout[0] = [
                {
                    dataField: "",
                    headerText: "고객명",
                }, {
                    dataField: "",
                    headerText: "접수일자",
                }, {
                    dataField: "",
                    headerText: "택번호",
                }, {
                    dataField: "sumName",
                    headerText: "상품명",
                    style: "colorColumn",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return `<div class="colorSquare" style="background-color: ${data.fdColorCode['C'+item.fdColor]}"></div>`;
                    }
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
                    headerText: "가맹검품",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state">등록</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "",
                    headerText: "검품확인",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state">등록</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "",
                    headerText: "상품수정",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--modify">수정</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "",
                    headerText: "접수취소",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "",
                    headerText: "결제취소",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "",
                    headerText: "인도취소",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        return template;
                    },
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grid.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };
        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grid.s.columnLayout) {
                grid.s.id[i] = AUIGrid.create(grid.s.targetDiv[i], grid.s.columnLayout[i], grid.s.prop[i]);
            }
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            ajax.setDataIntoGrid(numOfGrid, grid.s.read[numOfGrid]);
        },
    },

    e: {
        basicEvent() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(gridId[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const data = {
    fdColorCode: { // 컬러코드에 따른 실제 색상
        C00: "#D4D9E1", C01: "#D4D9E1", C02: "#3F3C32", C03: "#D7D7D7", C04: "#F54E50", C05: "#FB874B",
        C06: "#F1CE32", C07: "#349A50", C08: "#55CAB7", C09: "#398BE0", C10: "#DE9ACE", C11: "#FF9FB0",
    }
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    ajax.getInitialData();

    enableDatepicker();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    grid.f.initialization();
    grid.f.create();

    event.s.vkeys();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    // grid.f.setInitialData(0);

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grid.e.basicEvent();
}

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grid.e에 위치 */
const event = {
    s: { // 이벤트 설정
        vkeys() {
            $("#vkeyboard0").on("click", function() {
                onShowVKeyboard(0);
            });
        }
    },
    r: { // 이벤트 해제

    }
}

function enableDatepicker() {

    const today = new Date();
    const targetYear = today.getFullYear();
    const targetMonth = today.getMonth();

    /* 월의 시작 날 */
    const firstDate = new Date(targetYear, targetMonth, 1).format("yyyy-MM-dd");
    /* 월의 마지막 날 */
    const lastDate = new Date(targetYear, targetMonth+1, 0).format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "startDt", "endDt"
    ];

    $("#" + datePickerTargetIds[0]).val(firstDate);
    $("#" + datePickerTargetIds[1]).val(lastDate);

    /*
    * JqueyUI datepicker의 기간 A~B까지를 선택할 때 선택한 날짜에 따라 제한을 주기 위한 DOM id의 배열이다.
    * 배열 내 각 내부 배열은 [~부터의 제한 대상이 될 id, ~까지의 제한 대상이 될 id] 이다.
    * */
    const dateAToBTargetIds = [
        ["startDt", "endDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

function onShowVKeyboard(num) {

    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["searchString"];

    vkeyProp[0] = {
        title: "고객 검색",
        // callback:
    }

    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

/* jest 기본 동작을 테스트 하기 위한 함수, 기본 테스트를 통과하면 아래의 module.exports 부분을 수정하고 이 함수는 지울 것 */
function testForJest() {
    return "hi";
}

/* jest 테스트를 위해 nodejs 의 요소에 테스트가 필요한 기능을 탑재하여 내보내기 한다. 보통의 실행 환경에서는 무시된다. */
try {
    module.exports = {testForJest};
}catch (e) {
    if(!(e instanceof ReferenceError)) {
        console.log(e);
    }
}