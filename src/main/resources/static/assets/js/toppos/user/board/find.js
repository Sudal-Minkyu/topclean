/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        물건찾기요청: {
            fdIdList: "a",
        },

        franchiseObjectFind: { // 통합조회 페이지와 비슷하지만, fdState에 대한 조건이 빠진 형태
            bcId: "n", // 선택된 고객. 없을 경우 null
            searchTag: "s", // 택번호 검색문자
            filterFromDt: "sr", // 시작 조회기간
            filterToDt: "sr", // 종료 조회기간
        },

        /* 해당 검색은 기존 customerInfo API를 사용해 고객을 선택할 예정 */
        customerInfo: { // integrate 의 customerInfo와 같은 구성이지만 택번호도 포함
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소
            searchString: "sr", // 검색문자
        },
    },
    receive: {
        franchiseObjectFind: {
            fdId: "n",
            bcName: "s",
            frYyyymmdd: "s",
            fdEstimateDt: "s",
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
            fdS2Dt: "s",
            ffState: "s",
        },

        customerInfo: { // integrate 의 customerInfo와 같은 구성
            bcWeddingAnniversary: "d",
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
            tempSaveFrNo: "n",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    searchCustomer: "/api/user/customerInfo", // 고객 검색
    getDetailList: "/api/user/franchiseObjectFind",
    sendSearchRequest: "/api/user/franchiseObjectFindSave",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    searchCustomer(searchCondition) {
        dv.chk(searchCondition, dtos.send.customerInfo, "고객 검색 조건 보내기");
        CommonUI.ajax(urls.searchCustomer, "GET", searchCondition, function (req) {
            const items = req.sendData.gridListData;
            $("#searchType").val(0);
            $("#searchString").val("");
            dv.chk(items, dtos.receive.customerInfo, "검색된 고객에 해당하는 리스트 받기");
            if(items.length === 1) {
                wares.selectedCustomer = items[0];
                wares.searchTag = null;
                putCustomer();
            }else if(items.length > 1) {
                grids.f.setData(1, items);
                $("#customerListPop").addClass("active");
            }else{
                alertCheck("일치하는 고객 정보가 없습니다.<br>신규고객으로 등록 하시겠습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    location.href="/user/customerreg";
                });
            }

        });
    },

    getDetailList(filterCondition) {
        console.log(filterCondition);
        wares.filterCondition = filterCondition;
        dv.chk(filterCondition, dtos.send.franchiseObjectFind, "물건찾기 리스트 필터링조건 보내기");
        CommonUI.ajax(urls.getDetailList, "GET", filterCondition, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseObjectFind, "물건찾기 리스트 받기");
            grids.f.setData(0, data);
        });
    },

    sendSearchRequest(target) {
        dv.chk(target, dtos.send.물건찾기요청, "물건찾기를 요청할 품목들 보내기");
        console.log(target);
        CommonUI.ajax(urls.sendSearchRequest, "PARAM", target, function (res) {
            alertSuccess("요청하신 품목을 찾을 물건으로 등록하였습니다.");
            comms.getDetailList(wares.filterCondition);
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
            "grid_main", "grid_customerList"
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
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                    width: 100,
                }, {
                    dataField: "frYyyymmdd",
                    headerText: "접수일자",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdEstimateDt",
                    headerText: "인도예정일",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    width: 80,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatTagNo(value);
                    },
                }, {
                    dataField: "productName",
                    headerText: "상품명",
                    style: "grid_textalign_left",
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
                    width: 120,
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
                    width: 85,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                    width: 120,
                }, {
                    dataField: "fdS2Dt",
                    headerText: "지사입고일",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "ffState",
                    headerText: "찾기상태",
                    width: 85,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.ffState[value];
                    },
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
                enableFilter : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "bcName",
                    headerText: "고객명",
                }, {
                    dataField: "bcHp",
                    headerText: "전화번호",
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.formatTel(value);
                    }
                }, {
                    dataField: "bcAddress",
                    headerText: "주소",
                    style: "grid_textalign_left",
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                rowNumHeaderText : "순번",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                width : 830,
                height : 480,
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

        getSelectedCustomer() {
            return AUIGrid.getSelectedRows(grids.s.id[1])[0];
        },
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $("#searchCustomer").on("click", function () {
            mainSearch();
        });

        $("#selectCustomer").on("click", function () {
            const selectedItem = grids.f.getSelectedCustomer();
            selectCustomerFromList(selectedItem);
        });

        $("#resetCustomer").on("click", function () {
            resetAll();
        });

        $("#filterDetail").on("click", function () {
            const filterCondition = {
                bcId: wares.selectedCustomer.bcId ? wares.selectedCustomer.bcId : 0,
                searchTag: wares.searchTag,
                filterFromDt: $("#filterFromDt").val(),
                filterToDt: $("#filterToDt").val(),
            }
            comms.getDetailList(filterCondition);
        });

        $("#searchString").on("keypress", function(e) {
            if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                mainSearch();
            }
        });

        $("#vkeyboard0").on("click", function() {
            onShowVKeyboard(0);
        });

        $("#requestSearch").on("click", function () {
            const checkedItems = grids.f.getCheckedItems(0);
            const target = makeTargetDataset(checkedItems);
            if (checkedItems.length) {
                alertCheck("선택된 상품을 찾을 물건으로 등록 하시겠습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    comms.sendSearchRequest(target);
                    $('#popupId').remove();
                });
            } else {
                alertCaution("찾을 물건을 선택해주세요", 1);
            }
        });
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    selectedCustomer: {},
    searchTag: null,
    selectedItem: {},
    filterCondition: {},
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    trigs.basic();

    enableDatepicker();
}

function selectCustomerFromList(selectedItem) {
    if(selectedItem) {
        wares.selectedCustomer = selectedItem;
        wares.searchTag = null;
        putCustomer();
        $("#customerListPop").removeClass("active");
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function putCustomer() {
    let bcGradeName = "";
    $(".client__badge").removeClass("active");
    if(wares.selectedCustomer.bcGrade) {
        $(".client__badge:nth-child(" + wares.selectedCustomer.bcGrade.substr(1, 1) + ")").addClass("active");
        bcGradeName = CommonData.name.bcGradeName[wares.selectedCustomer.bcGrade];
    }
    $("#bcGrade").html(bcGradeName);

    if(wares.selectedCustomer.bcName) {
        $("#bcName").html(wares.selectedCustomer.bcName + "님");
    }else{
        $("#bcName").html("");
    }

    if(wares.selectedCustomer.bcValuation) {
        $("#bcValuation").attr("class",
            "propensity__star propensity__star--" + wares.selectedCustomer.bcValuation)
            .css('display','block');
    }else{
        $("#bcValuation").css('display','none');
    }

    $("#bcAddress").html(wares.selectedCustomer.bcAddress);
    $("#bcHp").html(CommonUI.formatTel(wares.selectedCustomer.bcHp));
    $("#beforeUncollectMoneyMain").html(wares.selectedCustomer.beforeUncollectMoney.toLocaleString());
    $("#saveMoneyMain").html(wares.selectedCustomer.saveMoney.toLocaleString());
    $("#bcRemark").html(wares.selectedCustomer.bcRemark);
    if(wares.selectedCustomer.bcLastRequestDt) {
        $("#bcLastRequestDt").html(
            wares.selectedCustomer.bcLastRequestDt.substr(0, 4) + "-"
            + wares.selectedCustomer.bcLastRequestDt.substr(4, 2) + "-"
            + wares.selectedCustomer.bcLastRequestDt.substr(6, 2)
        );
    }else if(wares.selectedCustomer.bcId){
        $("#bcLastRequestDt").html("없음");
    }else{
        $("#bcLastRequestDt").html("");
    }

    grids.f.clearData(0);
}

function enableDatepicker() {
    const today = new Date().format("yyyy-MM-dd");
    const tempDate = new Date();
    let fromday = new Date(new Date(tempDate.setMonth(tempDate.getMonth() - 1)).setDate(1)).format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(fromday);
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

function resetAll() {
    wares.selectedCustomer = {
        bcId: 0,
        beforeUncollectMoney: 0,
        saveMoney: 0,
        bcAddress: "",
        bcRemark: "",
    };
    wares.searchTag = null;
    putCustomer();
    grids.f.clearData(0);
    $("#searchType").val("0");
    $("#searchString").val("");
}

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["searchString"];

    vkeyProp[0] = {
        title: $("#searchType option:selected").html() + " (검색)",
        callback: mainSearch,
    };

    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function mainSearch() {
    const searchType = $("#searchType").val();
    const searchString = $("#searchString").val();
    console.log(searchType);

    if(searchString.length) {
        if(searchType === "4") {
            wares.selectedCustomer = {
                bcId: 0,
                beforeUncollectMoney: 0,
                saveMoney: 0,
                bcAddress: "",
                bcRemark: "",
            };
            putCustomer();
            const filterCondition = {
                bcId: 0,
                searchTag: searchString,
                filterFromDt: $("#filterFromDt").val(),
                filterToDt: $("#filterToDt").val(),
            }
            wares.searchTag = searchString;
            comms.getDetailList(filterCondition);
        } else {
            const searchCondition = {
                searchType: searchType,
                searchString: searchString
            }
            comms.searchCustomer(searchCondition);
        }
    } else {
        alertCaution("검색어를 입력해 주세요", 1);
    }
}

function makeTargetDataset(checkedItems) { // 저장 데이터셋 만들기
    let fdIdList = [];
    checkedItems.forEach(data => {
        fdIdList.push(data.item.fdId);
    });
    const target = {
        fdIdList: fdIdList,
    };
    return target;
}