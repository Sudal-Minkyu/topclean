/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchStoreCurrentList: { // 좌측 가맹점 그리드의 조회시
            filterFromDt: "s",
            filterToDt: "s",
            franchiseId: "s",
            type: "s",
        },

        branchStoreRemainList: {
            frCode: "s",
            fdS2Dt: "sr",
        },
    },

    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr", // 가맹점 id
            frName: "s",
            frTagNo: "s",
        },

        branchStoreCurrentList: {
            frCode: "s", // 가맹점 id
            frName: "s",
            fdS2Dt: "s", // 입고일
            input_cnt: "n",
            output_cnt: "n",
            remain_cnt: "n",
            tot_amt: "n",
        },

        branchStoreRemainList: {
            frRefType: "s",
            fdS2Type: "",
            fdS2Dt: "s",
            // fdS4Dt: "s",
            bcName: "s",
            fdTag: "s",

            fdColor: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            fdPriceGrade: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "n",
            fdPollution: "n",
            fdWaterRepellent: "n",
            fdStarch: "n",
            fdUrgentYn: "s",

            fdTotAmt: "n",
            fdState: "s",
            fdRemark: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getFrList: "/api/manager/branchBelongList",
    getMainList: "/api/manager/branchStoreCurrentList",
    getDetailList: "/api/manager/branchStoreRemainList",
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax(urls.getFrList, "GET", false, function (res) {
            const data = res.sendData.franchiseList;
            dv.chk(data, dtos.receive.managerBelongList, "지점에 속한 가맹점 받아오기");
            const $frList = $("#frList");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.frId}">${obj.frName}</option>`;
                $frList.append(htmlText);
            });
        });
    },

    getMainList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchStoreCurrentList, "프랜차이즈 그리드 검색 조건 보내기");
        CommonUI.ajax(urls.getMainList, "GET", searchCondition, function (res) {
            const data = res.sendData.gridListData;
            grids.f.clearData(1);
            grids.f.setData(0, data);
        });
    },

    getDetailList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchStoreRemainList, "디테일 그리드 검색 조건 보내기");
        CommonUI.ajax(urls.getDetailList, "GET", searchCondition, function (res) {
            const data = res.sendData.gridListData;
            CommonUI.toppos.makeSimpleProductNameList(data);
            grids.f.setData(1, data);
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
            "grid_franchise", "grid_detail"
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
                    dataField: "frName",
                    headerText: "가맹점",
                    style: "grid_textalign_left",
                }, {
                    dataField: "fdS2Dt",
                    headerText: "입고일자",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "input_cnt",
                    headerText: "입고<br>건수",
                    width: 50,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "output_cnt",
                    headerText: "출고<br>건수",
                    width: 50,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "remain_cnt",
                    headerText: "체류<br>건수",
                    width: 50,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "tot_cnt",
                    headerText: "접수총액",
                    style: "grid_textalign_right",
                    width: 90,
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
                noDataMessage : "존재하는 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : true,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "frRefType",
                    headerText: "구분",
                    width: 50,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.name.frRefType[value];
                    },
                }, {
                    dataField: "fdS2Type",
                    headerText: "입고<br>타입",
                    width: 60,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.name.fdS2Type[value];
                    },
                }, {
                    dataField: "fdS2Dt",
                    headerText: "입고일자",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                },/*  {
                    dataField: "fdS4Dt",
                    headerText: "출고일자",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, */ {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                    width: 100,
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 90,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.formatBrTagNo(value);
                    },
                }, {
                    dataField: "productName",
                    headerText: "상품명",
                    style: "grid_textalign_left",
                    width: 200,
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                        const productName = CommonUI.toppos.makeSimpleProductName(item);
                        item.productName = productName;
                        return colorSquare + ` <span style="vertical-align: middle;">` + productName + `</span>`;
                    },
                }, {
                    dataField: "processName",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 130,
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                        item.processName = CommonUI.toppos.processName(item);
                        return item.processName;
                    }
                }, {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 90,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "fdUrgentYn",
                    headerText: "급세탁",
                    width: 70,
                }, {
                    dataField: "fdRetryYn",
                    headerText: "재세탁",
                    width: 70,
                }, {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                    width: 200,
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "존재하는 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : true,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
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
        },

        // 엑셀 내보내기(Export)
        exportToXlsx() {
            //FileSaver.js 로 로컬 다운로드가능 여부 확인
            if(!AUIGrid.isAvailableLocalDownload(grids.s.id[1])) {
                alertCaution("파일 다운로드가 불가능한 브라우저 입니다.", 1);
                return;
            }
            AUIGrid.exportToXlsx(grids.s.id[1], {
                fileName : `${wares.title}_${wares.currentDetail.frName}_${wares.currentDetail.fdS2Dt}`,
                progressBar : true,
            });
        }
    },

    t: {
        basic() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                showDetail(e.item);
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#searchListBtn").on("click", function () {
                searchOrder();
            });

            $("#exportXlsx").on("click", function () {
                if(hasGrid1Data()) {
                    grids.f.exportToXlsx();
                }
            });
        },
    },
    r: { // 이벤트 해제

    }
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    title: "체류세탁물현황", // 엑셀 다운로드 파일명 생성에 쓰인다.
    currentDetail: { // 디테일 그리드를 뿌리기 위해 선택된 가맹점과 일자의 정보. 엑셀 파일명 생성에 쓰인다.
        frCode: 0,
        frName: "",
        fdS2Dt: "",
    },
};

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    grids.t.basic();
    enableDatepicker();
    comms.getFrList();
    trigs.s.basic();
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

function searchOrder() {

    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
        franchiseId: $("#frList").val(),
        type: "2", // 1 지사입고현황, 2 체류세탁물현황
    };

    comms.getMainList(searchCondition);
}

function showDetail(item) {
    const searchCondition = {
        frCode: item.frCode,
        fdS2Dt: item.fdS2Dt,
    };

    /* 선택된 가맹점과 날짜 항목에 대한 기억 */
    wares.currentDetail.frCode = searchCondition.frCode;
    wares.currentDetail.fdS2Dt = searchCondition.fdS2Dt;
    wares.currentDetail.frName = item.frName;

    comms.getDetailList(searchCondition);
}

function hasGrid1Data() {
    const result = grids.f.getData(1).length ? true : false ;
    if(!result) {
        alertCaution("엑셀 다운로드를 실행할 데이터가 없습니다.<br>조회후 왼쪽 표에서 데이터를 선택해 주세요.", 1);
    }
    return result;
}
