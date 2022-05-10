/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseInCancelChange: {
            fdIdList: "a",
        }
    },
    receive: {
        franchiseReceiptFranchiseInList: {
            fdPollutionType: "n",
            fdPollutionBack: "n",
            fdId: "nr",
            fdS5Dt: "s",
            bcName: "sr",
            fdTag: "sr",

            fdColor: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            biItemcode: "s",

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
            fdS4Dt: "s",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getMainList: "/api/user/franchiseReceiptFranchiseInCancelList",
    cancelInState: '/api/user/franchiseInCancelChange',
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    // 입고 리스트 받기
    getMainList() {
        console.log(wares.searchCondition);
        CommonUI.ajax(urls.getMainList, "GET", wares.searchCondition, function (res) {
            const data = res.sendData.gridListData;

            dv.chk(data, dtos.receive.franchiseReceiptFranchiseInList, '입고 리스트 항목 받아오기');
            grids.f.setData(0, data);
        });
    },

    cancelInState(targetDataSet) {
        dv.chk(targetDataSet, dtos.send.franchiseInCancelChange, '입고취소 항목 보내기');
    
        CommonUI.ajax(urls.cancelInState, "PARAM", targetDataSet, function(res) {
            alertSuccess("입고취소 완료");
            grids.f.clearData(0);
            comms.getMainList();
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
            "franchiseInList",
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
                    dataField: "fdS5Dt",
                    headerText: "가맹점입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                    width: 80,
                },
                {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 90,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
                    },
                }, 
                {
                    dataField: "",
                    headerText: "상품명",
                    width: 150,
                    style: "color_and_name",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                        const sumName = CommonUI.toppos.makeSimpleProductName(item);
                        return colorSquare + ` <span style="vertical-align: middle;">` + sumName + `</span>`;
                    },
                },
                {
                    dataField: "",
                    headerText: "입고유형",
                    width: 80,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let type = "";
                        if (item.fdRetryYn === "Y") {
                            type = "재세탁";
                        } else if (item.fdPriceGrade === "3") {
                            type = "명품";
                        } else if (item.biItemcode.substr(0, 3) === "D03") {
                            type = "운동화";
                        } else {
                            type = "일반";
                        }
                        return type;
                    },
                }, 
                {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 120,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    },
                },
                {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, 
                {
                    dataField: "fdUrgentYn",
                    headerText: "급세탁",
                    width: 60,
                },
                {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                }, 
                {
                    dataField: "frYyyymmdd",
                    headerText: "고객접수일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, 
                {
                    dataField: "fdS4Dt",
                    headerText: "지사입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
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
            $('#cancelInBtn').on('click', function() {
                const checkedItems = grids.f.getCheckedItems(0);
                wares.targetDataSet = makeSaveDataset(checkedItems);
                if (checkedItems.length) {
                    alertCheck("선택된 상품을 가맹점입고취소 하시겠습니까?");
                    $("#checkDelSuccessBtn").on("click", function () {
                        comms.cancelInState(wares.targetDataSet);
                    });
                } else {
                    alertCaution("입고취소 할 리스트를 선택해주세요", 1);
                }
            });

            $("#searchBtn").on("click", function () {
                getMainList();
            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    targetDataSet: [],
    searchCondition: {},
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    enableDatepicker();
    trigs.s.basicTrigger();
}

function makeSaveDataset(checkedItems) { // 저장 데이터셋 만들기
    let fdIdList = [];
    checkedItems.forEach(data => {
        fdIdList.push(data.item.fdId);
    });
    return {
        fdIdList: fdIdList,
    };
}

function enableDatepicker() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 6);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(fromday);
    $("#" + datePickerTargetIds[1]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

function getMainList() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
    };
    wares.searchCondition = searchCondition;

    comms.getMainList();
}