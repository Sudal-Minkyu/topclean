/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchReceiptBranchInList: {
            frId: "n", // 0이 올 경우는 통합출고, 혹은 가맹점 전체선택인 경우
            filterFromDt: "s",
            filterToDt: "s",
            isUrgent: "s" // Y이 올 경우 급세탁인 항목만, N인 경우 전체항목
        },
        branchStateChange: {
            miDegree: "n", // 몇차 출고인지에 대한 신호
            fdIdList: "a", // fdId의 목록이 담긴 배열(3차원 배열)
        },
    },
    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr",
            frName: "s",
            frTagNo: "s",
        },
        branchReceiptBranchInList: {
            fdId: "n", // 출고 처리를 위함
            frName: "s",
            frCode: "s",
            fdS2Dt: "s",
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
            fdEstimateDt: "s",
            fdTotAmt: "n",
            fdState: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getFrList: "/api/manager/branchBelongList",
    getReceiptList: "/api/manager/branchReceiptBranchInList",
    sendOutReceipt: "/api/manager/branchStateChange",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax(urls.getFrList, "GET", false, function (res) {
            const data = res.sendData.franchiseList;
            dv.chk(data, dtos.receive.managerBelongList, "가맹점 선택출고에 필요한 지점에 속한 가맹점 받아오기");
            const $frList = $("#frList");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.frId}">${obj.frName}</option>`
                $frList.append(htmlText);
            });
        });
    },
    
    getReceiptList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchReceiptBranchInList, "출고 품목 조회 조건 보내기");
        CommonUI.ajax(urls.getReceiptList, "GET", searchCondition, function (res) {
            wares.receiptList = CommonUI.toppos.killNullFromArray(res.sendData.gridListData);
            $("#frName").html($("#frList option:selected").html());
            $("#numOfList").html(wares.receiptList.length);
            $("#listStatBar").show();
        });
    },

    sendOutReceipt(sendList) {
        console.log(sendList);
        wares.sl = sendList;
        dv.chk(sendList, dtos.send.branchStateChange, "출고처리 항목 보내기");
        CommonUI.ajax(urls.sendOutReceipt, "PARAM", sendList, function (res) {
            console.log(res);
            alertSuccess("출고처리가 완료 되었습니다.");
            grids.f.clearData(0);
            grids.f.clearData(1);
            wares.receiptList = "";
            $("#listStatBar").hide();
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
            "grid_main", "grid_selected"
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
                    dataField: "",
                    headerText: "지점입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdUrgentYn",
                    headerText: "급세탁",
                    width: 70,
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    width: 90,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatTagNo(value);
                    },
                }, {
                    dataField: "",
                    headerText: "상품명",
                    style: "grid_textalign_left",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                        const sumName = CommonUI.toppos.makeSimpleProductName(item);
                        return colorSquare + ` <span style="vertical-align: middle;">` + sumName + `</span>`;
                    },
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 130,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    }
                }, {
                    dataField: "bcName",
                    headerText: "고객",
                    width: 100,
                }, {
                    dataField: "fdEstimateDt",
                    headerText: "출고예정일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
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
                        return CommonData.name.fdState[value];
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
                showAutoNoDataMessage: false,
                enableColumnResize : true,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "frName",
                    headerText: "가맹점",
                }, {
                    dataField: "qty",
                    width: 100,
                    headerText: "수량",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
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

        addRow(numOfGrid, item) {
            AUIGrid.addRow(grids.s.id[numOfGrid], item, "last");
        },

        addCheckedRowByTagNo(tagNo) {
            AUIGrid.addCheckedRowsByValue(grids.s.id[0], "fdTag", tagNo);
        },
    },

    t: {
        basicTrigger() {
            AUIGrid.bind(grids.s.id[0], "rowCheckClick", function (e) {
                listCheckChanged();
            });

            AUIGrid.bind(grids.s.id[0], "rowAllCheckClick", function (check) {
                listCheckChanged();
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#type01").on("click", function () {
                $("#frSelectUI").hide();
                $("#frList option").first().prop("selected", true);
            })

            $("#type02").on("click", function () {
                $("#frSelectUI").show();
            });

            $("#getListBtn").on("click", function () {
                getReceiptList();
            });

            $("#inputTagBtn").on("click", function () {
                inputTag();
            });

            $("#inputTagNo").on("keyup", function (e) {
                if(e.originalEvent.code === "Enter") {
                    inputTag();
                }
            });

            $("#sendOutBtn").on("click", function () {
                wares.checkedItems = grids.f.getCheckedItems(0);
                if(wares.checkedItems.length) {
                    alertCheck("선택된 상품을 출고처리 하시겠습니까?");
                    $("#checkDelSuccessBtn").on("click", function () {
                        sendOut();
                    });
                } else {
                    alertCaution("출고처리할 상품을 선택해 주세요.", 1);
                }
                
            });

            $("#addAll").on("click", function () {
                grids.f.clearData(0);
                grids.f.setData(0, wares.receiptList);
            });

            $("#clearAll").on("click", function () {
                grids.f.clearData(0);
            });

            const $miDegree = $("#miDegree");
            $miDegree.on("keyup", function () {
                $miDegree.val($miDegree.val().substring(0, 2));
            });
        },
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    receiptList: "",
    checkedItems: [],
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    grids.t.basicTrigger();
    trigs.s.basic();

    enableDatepicker();
    comms.getFrList();
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

function getReceiptList() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val(),
        filterToDt: $("#filterToDt").val(),
        isUrgent: $("#isUrgent").is(":checked") ? "Y" : "N",
        frId: 0,
    }
    if($("#type02").is(":checked")) {
        searchCondition.frId = parseInt($("#frList").val());
    }
    comms.getReceiptList(searchCondition);
}

function inputTag() {
    if(wares.receiptList === "") {
        CommonUI.toppos.speak("입고된 품목을 먼저 조회해 주세요.");
        return false;
    }
    
    const tagNo = $("#inputTagNo").val().replace(/-/g, "");
    if(tagNo.length < 7) {
        CommonUI.toppos.speak("택번호 7자리를 입력해 주세요.");
        $("#inputTagNo").focus();
        return false;
    }


    const checkedItems = grids.f.getCheckedItems(0);
    for(let i = 0; i < checkedItems.length; i++) {
        if(checkedItems[i].item.fdTag  === tagNo) {
            CommonUI.toppos.speak("이미 조회한 택번호 입니다.");
            $("#inputTagNo").val("");
            $("#inputTagNo").focus();
            return false;
        }
    }
    

    const gridItems = grids.f.getData(0);
    for(let i = 0; i < gridItems.length; i++) {
        if(gridItems[i].fdTag  === tagNo) {
            grids.f.addCheckedRowByTagNo(tagNo);
            CommonUI.toppos.speak(CommonUI.toppos.makeSimpleProductName(gridItems[i]));
            $("#inputTagNo").val("");
            $("#inputTagNo").focus();
            return false;
        }
    }

    let noExist = true;
    wares.receiptList.forEach(obj => {
        if(obj.fdTag === tagNo) {
            CommonUI.toppos.speak(CommonUI.toppos.makeSimpleProductName(obj));
            grids.f.addRow(0, obj);
            grids.f.addCheckedRowByTagNo(tagNo);
            noExist = false;
        }
    });

    if(noExist) {
        CommonUI.toppos.speak("택번호가 리스트에 존재하지 않습니다.");
    }

    $("#inputTagNo").val("");
    $("#inputTagNo").focus();
}

function sendOut() {

    if(wares.checkedItems.length) {
        let fdIdList = [];
        let codeIndex = [];
        fdIdList.push([0]);
        codeIndex.push("dummy");

        wares.checkedItems.forEach(obj => {
            const i = codeIndex.indexOf(obj.item.frCode);
            if(i + 1) {
                fdIdList[i].push(obj.item.fdId);
            } else {
                codeIndex.push(obj.item.frCode);
                fdIdList.push([obj.item.fdId]);
            }
        });

        const sendList = {
            miDegree: $("#miDegree").val().toInt(),
            fdIdList: fdIdList
        }

        if(!sendList.miDegree) {
            alertCaution("올바른 출고 차수를 입력해 주세요.", 1);
        }

        comms.sendOutReceipt(sendList);
    } else {
        alertCaution("출고할 품목을 선택해 주세요.", 1);
    }
}

function listCheckChanged() {
    const items = grids.f.getCheckedItems(0);
    const refinedData = [];

    items.forEach(obj => {

        let isNoMatchName = true;
        for(let i = 0; i < refinedData.length; i++) {
            if(refinedData[i].frCode === obj.item.frCode) {
                refinedData[i].qty++;
                isNoMatchName = false;
            }
        }

        if(isNoMatchName) {
            refinedData.push({
                frCode: obj.item.frCode,
                frName: obj.item.frName,
                qty: 1,
            });
        }
    });
    grids.f.setData(1, refinedData);
}