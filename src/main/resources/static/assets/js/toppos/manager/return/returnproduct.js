/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchReceiptReturnList: {
            tagNo: "s",
            filterFromDt: "s",
            filterToDt: "s",
            frId: "n", // 가맹점 Id
        },
        branchRelease: {
            type: "s",
            fdIdList: "a",
        },
    },
    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr", // 가맹점 Id
            frName: "s",
            frTagNo: "s",
        },
        branchReceiptReturnList: {
            fdId: "n",
            frName: "s",
			insertDt: "s",
            fdS2Time: "s",
          
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
            bcName: "s",
            fdTotAmt: "n",
            fdState: "s",
            fdPreState: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getFrList: "/api/manager/branchBelongList",
    getMainGridList: "/api/manager/branchReceiptReturnList",
    executeReceipt: "/api/manager/branchRelease",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax(urls.getFrList, "GET", false, function (res) {
            const data = res.sendData.franchiseList;
            console.log(res);
            dv.chk(data, dtos.receive.managerBelongList, "지점에 속한 가맹점 받아오기");
            const $frList = $("#frList");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.frId}" data-tagno="${obj.frTagNo}">${obj.frName}</option>`
                $frList.append(htmlText);
            });
        });
    },

    getMainGridList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchReceiptReturnList, "메인 그리드 검색 조건 보내기");
        CommonUI.ajax(urls.getMainGridList, "GET", searchCondition, function (res) {
            const data = CommonUI.toppos.killNullFromArray(res.sendData.gridListData);
            console.log(data);
            grids.f.setData(0, data);
            // $("#exportXlsx").show();
        });
    },

    executeReceipt(sendList) {
        dv.chk(sendList, dtos.send.branchRelease, "체크된 품목에 대해 최종 처리하기");
        CommonUI.ajax(urls.executeReceipt, "PARAM", sendList, function (res) {
            console.log(res);
            alertSuccess("반송처리가 완료 되었습니다.");
            comms.getMainGridList(wares.searchCondition);
            // $("#exportXlsx").hide();
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
                    dataField: "frName",
                    headerText: "가맹점명",
                    style: "grid_textalign_left",
                }, {
                    dataField: "bcName",
                    headerText: "고객",
                    style: "grid_textalign_left",
                    width: 100,
                }, {
                    dataField: "insertDt",
                    headerText: "상품접수",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdS2Time",
                    headerText: "지사입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 90,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
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
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                        const sumName = CommonUI.toppos.makeSimpleProductName(item);
                        item.productName = sumName;
                        return colorSquare + ` <span style="vertical-align: middle;">` + sumName + `</span>`;
                    },
                }, {
                    dataField: "processName",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 130,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
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
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let result = "";
                        if(value) {
                            result = CommonData.name.fdState[value];
                        }
                        return result;
                    },
                }, {
                    dataField: "fdPreState",
                    headerText: "이전상태",
                    width: 90,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let result = "";
                        if(value) {
                            result = CommonData.name.fdState[value];
                        }
                        return result;
                    },
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
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
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
            
            AUIGrid.exportToXlsx(grids.s.id[0], {
                fileName : `${wares.title}_${wares.currentDetail.frName}_${wares.currentDetail.filterFromDt}`
                    + `_${wares.currentDetail.filterToDt}`,
                progressBar : true,
            });
        },
    },

    t: {
        basic() {
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
            $("#frList").on("change", function () {
                $("#foreTag").val($("#frList option:selected").attr("data-tagno"));
            });

            $("#searchListBtn").on("click", function () {
                searchOrder();
            });

            const $aftTag = $("#aftTag");
            $aftTag.on("keyup", function (e) {
                $aftTag.val($aftTag.val().numString());
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                    searchOrder();
                }
            });

            $("#executeBtn").on("click", function () {
                askExcute();
            });

            $("#exportXlsx").on("click", function () {
                grids.f.exportToXlsx();
            });
        },
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    checkedItems: [],
    title: "가맹점반송", // 엑셀 다운로드 파일명 생성에 쓰인다.
    currentDetail: { // 디테일 그리드를 뿌리기 위해 선택된 가맹점과 일자의 정보. 엑셀 파일명 생성에 쓰인다.
        frName: "",
        filterFromDt: "",
        filterToDt: "",
    },
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
    const frId = $("#frList").val();
    if(frId === "") {
        alertCaution("가맹점을 선택해 주세요.", 1);
        return false;
    }

    const searchCondition = {
        tagNo: $("#aftTag").val().numString(),
        filterFromDt: $("#filterFromDt").val(),
        filterToDt: $("#filterToDt").val(),
        frId: parseInt(frId),
    };
    wares.searchCondition = searchCondition;

    wares.currentDetail.filterFromDt = searchCondition.filterFromDt;
    wares.currentDetail.filterToDt = searchCondition.filterToDt;
    wares.currentDetail.frName = $("#frList option:selected").html();

    if(searchCondition.tagNo.length !== 0 && searchCondition.tagNo.length !==4) {
        alertCaution("택번호는 완전히 입력하거나,<br>입력하지 말아주세요.(전체검색)", 1);
        return false;
    }

    comms.getMainGridList(searchCondition);
}

function askExcute() {
    wares.checkedItems = grids.f.getCheckedItems(0);
    if(wares.checkedItems.length) {
        alertCheck("선택된 상품을 반송처리 하시겠습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            executeCheckedReceipts();
        });
    } else {
        alertCaution("반송처리할 품목을 선택해 주세요.", 1);
    }
}

function executeCheckedReceipts() {
    let fdIdList = [];

    wares.checkedItems.forEach(obj => {
        fdIdList.push(obj.item.fdId);
    });

    const sendList = {
        type: "2",
        fdIdList: fdIdList
    }

    comms.executeReceipt(sendList);
}