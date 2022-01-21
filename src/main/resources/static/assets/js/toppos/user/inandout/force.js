/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseStateChange: {
            fdIdList: "",
            stateType: "sr",
        }
    },
    receive: {
        franchiseStateChange: {
            fdId: "nr",
            fdS4Dt: "sr", 
            bcName: "sr",
            fdTag: "sn",

            fdColor: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            checkFdId: "s",

            fdPriceGrade: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "",
            fdPollution: "",
            fdWaterRepellent: "",
            fdStarch: "",
            fdUrgentYn: "s",
            fdTotAmt: "n",
            fdRemark: "s",

            frYyyymmdd: "sr",
            fdS2Dt: "sr",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getForceList: '/api/user/franchiseReceiptForceList',
    changeClosedList: '/api/user/franchiseStateChange',
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getForceList() {
        CommonUI.ajax(urls.getForceList, "GET", false, function (res) {
            const data = res.sendData;
            const dataLength = data.gridListData.length;
            let fastItemCount = 0;
            let retryItemCount = 0;
            let totalAmount = 0;
            console.log(data);
    
            for (let i = 0; i < data.gridListData.length; i++) {
                // 접수총액
                totalAmount += data.gridListData[i].fdTotAmt;
                // 급세탁건수
                if (data.gridListData[i].fdUrgentYn == "Y") {
                    fastItemCount = fastItemCount + 1;
                }
                // 재세탁건수
                if (data.gridListData[i].fdRetryYn == "Y") {
                    retryItemCount = retryItemCount + 1;
                }
            }
            console.log(data.gridListData);
    
            dv.chk(data.gridListData, dtos.receive.franchiseStateChange, '강제입고 항목 받아오기');
            grids.f.setData(0, data.gridListData);
            
            $('#totalNum').text(dataLength);
            $('#totalItems').val(dataLength);
            $('#totalAmount').val(totalAmount.toLocaleString());
            $('#fastItems').val(fastItemCount);
            $('#retryItems').val(retryItemCount);
        });
    }
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
            "forceList"
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
                    dataField: "fdS4Dt",
                    headerText: "자사출고일",
                    width: 100,
                }, {
                    dataField: "bcName",
                    headerText: "고객명",
                    width: 80,
                },
                {
                    dataField: "fdTag",
                    headerText: "택번호",
                    width: 90,
                }, {
                    dataField: "",
                    headerText: "상품명",
                    width: 140,
                    style: "color_and_name",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${wares.fdColorCode['C'+item.fdColor]}; vertical-align: middle;"></span>`;
                        const sumName = CommonUI.toppos.makeSimpleProductName(item);
                        return colorSquare + ` <span style="vertical-align: middle;">` + sumName + `</span>`;
                    },
                },
                {
                    dataField: "",
                    headerText: "반품유형",
                    width: 100,
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    width: 100,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    },
                },
                {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
                    width: 120,
                }, {
                    dataField: "fdUrgentYn",
                    headerText: "급세탁",
                    width: 60,
                },
                {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                }, {
                    dataField: "frYyyymmdd",
                    headerText: "고객접수일",
                    width: 100,
                }, {
                    dataField: "fdS2Dt",
                    headerText: "자사입고일",
                    width: 100,
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : true,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
                rowCheckColumnWidth: 40,
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
        
        // 그리드 체크된 로우
        getCheckedItems(numOfGrid) {
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        },

        // 그리드 데이터 클리어
        clearData(numOfGrid) {
			AUIGrid.clearGridData(grids.s.id[numOfGrid]);
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
        basicTrigger() {
            $('.aui-checkbox').on('click', function () {
                const checkedItems = grids.f.getCheckedItems(0);
                const checkedLength = checkedItems.length;
                let totalAmount = 0;
                
                checkedItems.forEach(checkedItem => {
                    // 접수총액
                    totalAmount += checkedItem.item.fdTotAmt;
                });

                $('#selectItems').val(checkedLength);
                $('#selectAmount').val(totalAmount.toLocaleString());
            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    fdColorCode: { // 컬러코드에 따른 실제 색상
        C00: "#D4D9E1", C01: "#D4D9E1", C02: "#3F3C32", C03: "#D7D7D7", C04: "#F54E50", C05: "#FB874B",
        C06: "#F1CE32", C07: "#349A50", C08: "#55CAB7", C09: "#398BE0", C10: "#DE9ACE", C11: "#FF9FB0",
    },
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();

    trigs.s.basicTrigger();
    comms.getForceList();

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grids.e.basicEvent();
}