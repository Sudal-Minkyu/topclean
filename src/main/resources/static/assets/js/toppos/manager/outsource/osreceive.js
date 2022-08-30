/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchReceiptBranchOutOutsouringList: {
            frId: "n", // 0이 올 경우는 통합입고, 혹은 가맹점 전체선택인 경우
            filterFromDt: "s",
            filterToDt: "s",
        },
        branchStateOutsouringOutChange: {
            miDegree: "n", // 몇차 입고인지에 대한 신호
            fdIdList: "a", // fdId의 목록이 담긴 배열(3차원 배열)
            fdS4TypeList: "a",
        },
        branchDispatchPrint: {
            miNoList: "a",
        }
    },
    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr",
            frName: "s",
            frTagNo: "s",
        },
        branchReceiptBranchOutOutsouringList: {
            fdId: "n", // 외주입고 처리를 위함
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
        branchStateOutsouringOutChange: {
            miNoList: "a",
        }
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax('/api/manager/branchBelongList', "GET", false, function (res) {
            const data = res.sendData.franchiseList;
            dv.chk(data, dtos.receive.managerBelongList, "가맹점 선택 외주입고에 필요한 지점에 속한 가맹점 받아오기");
            const $frList = $("#frList");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.frId}">${obj.frName}</option>`;
                $frList.append(htmlText);
            });
        });
    },

    getCandidateList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchReceiptBranchOutOutsouringList, '외주 품목 조회 조건 보내기');
        CommonUI.ajax('/api/manager/branchReceiptBranchOutOutsouringList', "GET", searchCondition, function (res) {
            wares.candidateList = CommonUI.toppos.killNullFromArray(res.sendData.gridListData);
            CommonUI.toppos.makeSimpleProductNameList(wares.candidateList);

            $("#statPanel").html(
                `[${$("#frList option:selected").html()}] 상품이 ${wares.candidateList.length}건 조회되었습니다.`);
            grids.f.clearData(0);
            grids.f.clearData(1);
            $("#inputTagNo").focus();
        });
    },

    receivedOutsource(sendList) {
        wares.sl = sendList;
        dv.chk(sendList, dtos.send.branchStateOutsouringOutChange, "외주입고처리 항목 보내기");
        CommonUI.ajax('/api/manager/branchStateOutsouringOutChange', "PARAM", sendList, function (res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.branchStateOutsouringOutChange, "외주입고처리된 항목 외주입고증 인쇄 위한 miNoList 받아오기");
            alertSuccess("외주입고처리가 완료 되었습니다.");
            grids.f.clearData(0);
            grids.f.clearData(1);
            wares.candidateList = "";
            $("#statPanel").html("조회버튼을 통해 상품을 조회하세요.");
            $("#exportXlsx").hide();
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
                    dataField: 'frName',
                    headerText: '가맹점명',
                    style: 'grid_textalign_left',
                }, {
                    dataField: 'moDt',
                    headerText: '외주출고일',
                    width: 90,
                    dataType: 'date',
                    formatString: 'yyyy-mm-dd',
                }, {
                    dataField: 'fdUrgentYn',
                    headerText: '급세탁',
                    width: 55,
                }, {
                    dataField: 'fdTag',
                    headerText: '택번호',
                    style: 'datafield_tag',
                    width: 80,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.formatBrTagNo(value);
                    },
                }, {
                    dataField: 'productName',
                    headerText: '상품명',
                    style: 'grid_textalign_left',
                    width: 200,
                    renderer : {
                        type : 'TemplateRenderer',
                    },
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                        const colorSquare =
                            `<span class='colorSquare' style='background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;'></span>`;
                        const productName = CommonUI.toppos.makeSimpleProductName(item);
                        item.productName = productName;
                        return colorSquare + ` <span style='vertical-align: middle;'>` + productName + `</span>`;
                    },
                }, {
                    dataField: 'processName',
                    headerText: '처리내역',
                    style: 'grid_textalign_left',
                    width: 130,
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                        item.processName = CommonUI.toppos.processName(item);
                        return item.processName;
                    }
                }, {
                    dataField: 'bcName',
                    headerText: '고객',
                    style: 'grid_textalign_left',
                    width: 100,
                }, {
                    dataField: 'fdTotAmt',
                    headerText: '접수금액',
                    style: 'grid_textalign_right',
                    width: 90,
                    dataType: 'numeric',
                    autoThousandSeparator: 'true',
                }, {
                    dataField: 'bpOutsourcingPrice',
                    headerText: '외주금액',
                    style: 'grid_textalign_right',
                    width: 90,
                    dataType: 'numeric',
                    autoThousandSeparator: 'true',
                }, {
                    dataField: 'fdState',
                    headerText: '현재상태',
                    width: 90,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
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
                showRowAllCheckBox: false,
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

        addAllData() {
            const checkedRows = grids.f.getCheckedItems(0);
            if(wares.candidateList !== "") {
                grids.f.setData(0, wares.candidateList);
            }
            for(const {item} of checkedRows) {
                grids.f.addCheckedRowByTagNo(item.fdTag);
            }
        },

        removeUncheckedData() {
            const checkedRows = grids.f.getCheckedItems(0);
            const refinedData = [];
            checkedRows.forEach(obj => {
                refinedData.push(obj.item);
            });
            grids.f.setData(0, refinedData);
            AUIGrid.setAllCheckedRows(grids.s.id[0], true);
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

        addUncheckedRowByTagNo(tagNo) {
            AUIGrid.addUncheckedRowsByValue(grids.s.id[0], "fdTag", tagNo);
        },

        // 엑셀 내보내기(Export)
        exportToXlsx() {
            //FileSaver.js 로 로컬 다운로드가능 여부 확인
            if(!AUIGrid.isAvailableLocalDownload(grids.s.id[1])) {
                alertCaution("파일 다운로드가 불가능한 브라우저 입니다.", 1);
                return;
            }

            const frName = wares.currentDetail.frName ? wares.currentDetail.frName + "_" : "";

            AUIGrid.exportToXlsx(grids.s.id[0], {
                fileName : `${wares.title}_${frName}${wares.currentDetail.filterFromDt}_${wares.currentDetail.filterToDt}`,
                progressBar : true,
            });
        },

        updateRowsById(numOfGrid, item) {
            AUIGrid.updateRowsById(grids.s.id[numOfGrid], item);
        },

        restoreEditedRow(numOfGrid, rowIdx) {
            AUIGrid.restoreEditedRows(grids.s.id[numOfGrid], rowIdx);
        },

        focusOn(tagNo) {
            const rowIndex = AUIGrid.getRowIndexesByValue(grids.s.id[0], "fdTag", tagNo)[0];
            AUIGrid.setRowPosition(grids.s.id[0], rowIndex - 2 ? rowIndex - 2 : 0);
            AUIGrid.setSelectionByIndex(grids.s.id[0], rowIndex, 4);
        },

        getItemByTagNo(tagNo) {
            return AUIGrid.getItemsByValue(grids.s.id[0], "fdTag", tagNo)[0];
        }
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    grid() {
        AUIGrid.bind(grids.s.id[0], "rowCheckClick", function (e) {
            listCheckChanged();
        });
    },

    basic() {
        $("#type01").on("click", function () {
            $("#frSelectUI").hide();
            $("#frList option").first().prop("selected", true);
        });

        $("#type02").on("click", function () {
            $("#frSelectUI").show();
        });

        $("#getListBtn").on("click", function () {
            getCandidateList();
        });

        $("#inputTagBtn").on("click", function () {
            inputTag();
        });

        $("#inputTagNo").on("keyup", function (e) {
            if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                inputTag();
            }
        });

        $("#receiveBtn").on("click", function () {
            wares.checkedItems = grids.f.getCheckedItems(0);
            if(wares.checkedItems.length) {
                alertCheck("선택된 상품을 외주입고처리 하시겠습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    receive();
                    $('#popupId').remove();
                });
            } else {
                alertCaution("외주입고처리할 상품을 선택해 주세요.", 1);
            }
        });

        $("#addAll").on("click", function () {
            grids.f.addAllData();
            $("#inputTagNo").focus();
        });

        $("#clearUnchecked").on("click", function () {
            grids.f.removeUncheckedData();
            $("#inputTagNo").focus();
        });

        const $miDegree = $("#miDegree");
        $miDegree.on("keyup", function () {
            $miDegree.val($miDegree.val().substring(0, 2));
        });

        $("#exportXlsx").on("click", function () {
            grids.f.exportToXlsx();
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    candidateList: "",
    checkedItems: [],
    title: "외주입고", // 엑셀 다운로드 파일명 생성에 쓰인다.
    currentDetail: { // 디테일 그리드를 뿌리기 위해 선택된 가맹점과 일자의 정보. 엑셀 파일명 생성에 쓰인다.
        filterFromDt: "",
        filterToDt: "",
        frName: "",
    },
    sayLoop: false,
};

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    trigs.grid();
    trigs.basic();

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

function getCandidateList() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
        frId: 0,
    };
    wares.currentDetail.filterFromDt = searchCondition.filterFromDt;
    wares.currentDetail.filterToDt = searchCondition.filterToDt;

    if($("#type02").is(":checked")) {
        searchCondition.frId = parseInt($("#frList").val(), 10);
    }
    wares.currentDetail.frName = searchCondition.frId ? $("#frList option:selected").html() : "";

    comms.getCandidateList(searchCondition);
}

/* 택번호 입력을 통해 상품을 선택하려고 할 때 */
function inputTag() {
    if(wares.candidateList === "") {
        CommonUI.toppos.speak("입고된 품목을 먼저 조회해 주세요.");
        return;
    }

    const tagNo = $("#inputTagNo").val().replace(/-/g, "");
    if(tagNo.length < 7) {
        CommonUI.toppos.speak("택번호 7자리를 입력해 주세요.");
        $("#inputTagNo").focus();
        return;
    }

    const checkedItems = grids.f.getCheckedItems(0);
    for(const item of checkedItems) {
        if (item.item.fdTag  === tagNo) {
            CommonUI.toppos.speak("이미 조회한 택번호 입니다.");
            grids.f.focusOn(tagNo);
            $("#inputTagNo").val("");
            $("#inputTagNo").focus();
            return;
        }
    }

    const gridItems = grids.f.getData(0);
    for(const item of gridItems) {
        if (item.fdTag === tagNo) {
            grids.f.addCheckedRowByTagNo(tagNo);
            grids.f.focusOn(tagNo);
            listCheckChanged();
            $("#inputTagNo").val("");
            CommonUI.toppos.speak(CommonUI.toppos.makeSimpleProductName(item));
            $("#inputTagNo").focus();
            return;
        }
    }

    let noExist = true;
    wares.candidateList.forEach(obj => {
        if(obj.fdTag === tagNo) {
            grids.f.addRow(0, obj);
            grids.f.addCheckedRowByTagNo(tagNo);
            grids.f.focusOn(tagNo);
            const item = grids.f.getItemByTagNo(tagNo);
            listCheckChanged();
            noExist = false;
            CommonUI.toppos.speak(CommonUI.toppos.makeSimpleProductName(item));
        }
    });

    if(noExist) {
        CommonUI.toppos.speak("택번호가 리스트에 존재하지 않습니다.");
    }

    $("#inputTagNo").val("");
    $("#inputTagNo").focus();
}

function receive() {

    if(wares.checkedItems.length) {
        const fdIdList = [];

        wares.checkedItems.forEach(obj => {
            fdIdList.push([obj.item.fdId]);
        });

        const sendList = {
            fdIdList,
        };

        comms.receivedOutsource(sendList);
    } else {
        alertCaution("외주입고할 품목을 선택해 주세요.", 1);
    }
}

/* 좌측 체크박스의 체크된 데이터들만 모아서 우측 그리드에 띄운다. */
function listCheckChanged() {
    const items = grids.f.getCheckedItems(0);
    const refinedData = [];

    items.forEach(obj => {
        let isNoMatchName = true;
        for(const refObj of refinedData) {
            if (refObj.frCode === obj.item.frCode) {
                refObj.qty++;
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

/*
function dispatchPrintData(jsonData){
    // 프로젝트명
    const projectName = "toppos";
    // 서식명
    const formName = "dispatchPrint02";

    //데이터셋 Object
    //데이터셋 Object에 생성된 DataSet 추가
    const datasetObject = {
        dataset_0: JSON.stringify(jsonData)
    };
    //파라미터 Object
    const paramObject = {};
    //파라미터
    //paramObject.stDt = "2019-01-31";
    //paramObject.endDt = "2019-12-31";
    //뷰어 오픈 공통 함수 호출
    fn_viewer_open(projectName, formName, datasetObject, paramObject);
}

function fn_viewer_open(projectName, formName, datasetObject, paramObject){
    const _params = {
        projectName,
        formName,
    };
    for (const datasetValue in datasetObject) {
        _params[datasetValue] = encodeURIComponent(datasetObject[datasetValue]);
    }
    for (const paramValue in paramObject) {
        _params[paramValue] = paramObject[paramValue];
    }

    const _url = "https://report.topcleaners.kr:443" + "/UBIFORM/UView5/index.jsp"; //UBIFORM Viewer URL
    //팝업 오픈 Option 해당 설정은 window.open 설정을 참조
    //let windowoption = 'location=0, directories=0,resizable=0,status=0,toolbar=0,menubar=0, width=1280px,height=650px,left=0, top=0,scrollbars=0';  //팝업사이즈 window.open참고
    const windowoption = 'width=1280px,height=650px';
    const name = "printArea";// "UBF_" + n;
    //팝업사이즈 window.open참고
    const form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", _url);
    const params = _params ;
    for (const i in params) {
        if (params.hasOwnProperty(i)) {
            const param = document.createElement('input');
            param.type = 'hidden';
            param.name = i;
            param.value = encodeURI( params[i] );
            form.appendChild(param);
        }
    }
    document.body.appendChild(form);
    form.setAttribute("target", name);
    window.open("", name, windowoption);
    form.submit();
    document.body.removeChild(form);
}

function releaseListPrintClose() {
    $("#releaseListPrint").removeClass("active");
}
*/