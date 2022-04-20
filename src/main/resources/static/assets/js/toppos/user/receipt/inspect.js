/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseInspectionMessageSend: {
            fiId: "nr",
            isIncludeImg: "s",
            fmMessage: "s",
            bcId: "n",
        },

        inspectList: { // 통합조회 페이지와 비슷하지만, fdState에 대한 조건이 빠진 형태
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

        franchiseInspectionList: { // fdId가 일치하는 모든 검품 리스트 type 1: 검품등록시, 2: 검품확인시 
            fdId: "nr",
            type: "s",
        },
    },
    receive: {
        franchiseInspectionList: {
            fiId: "nr",
            fdId: "nr",
            fiType: "s",
            fiComment: "s",
            fiAddAmt: "n",
            fiPhotoYn: "s",
            fiSendMsgYn: "s",
            fiCustomerConfirm: "s",
            insertDt: "s",

            ffPath: "s",
            ffFilename: "s",
        },

        inspectList: { // 인계페이지와 거의 비슷하지만, estimateDt가 빠지고, fdS6Dt가 들어간 형태
            frRefType: "sr",
            bcId: "n",
            bcName: "s",
            frYyyymmdd: "s",
            fdId: "n",
            fdTag: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            fdS2Dt: "s",
            fdS4Dt: "s",
            fdS5Dt: "s",
            fdS6Dt: "s",
            fdState: "s",
            fdColor: "s",

            fdPriceGrade: "s",
            fdPollution: "n",
            fdUrgentYn: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "n",
            fdWaterRepellent: "n",
            fdStarch: "n",
            fdTotAmt: "n",
            fdRemark: "s"
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
            uncollectMoney: "nr",
            saveMoney: "nr",
            tempSaveFrNo: "n",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    searchCustomer: "/api/user/customerInfo", // 고객 검색
    getDetailList: "/api/user/inspectList", // 검품이 있는 리스트 필터링하여 가져오기
    getInspectionList: "/api/user/franchiseInspectionList", // 메시지 팝업이 뜰 때 선택한 아이템에 대해서 검품내역 세팅하기
    sendKakaoMessage: "/api/user/franchiseInspectionMessageSend",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {

    getDetailList(filterCondition) {
        dv.chk(filterCondition, dtos.send.inspectList, "검품리스트 필터링조건 보내기");
        CommonUI.ajax(urls.getDetailList, "GET", filterCondition, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.inspectList, "검품리스트 받기");
            grids.f.setData(0, data);
        });
    },

    searchCustomer(searchCondition) {
        dv.chk(searchCondition, dtos.send.customerInfo, "고객 검색 조건 보내기");
        CommonUI.ajax(urls.searchCustomer, "GET", searchCondition, function (req) {
            const items = req.sendData.gridListData;
            $("#searchCustomerType").val(0);
            $("#searchCustomerField").val("");
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

    getInspectionList(condition) {
        dv.chk(condition, dtos.send.franchiseInspectionList, "등록된 검품조회 조건");
        CommonUI.ajax(urls.getInspectionList, "GET", condition, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseInspectionList, "등록된 검품의 조회");
            grids.f.clearData(2);
            grids.f.setData(2, data);
        });
    },

    sendKakaoMessage(data) {
        dv.chk(data, dtos.send.franchiseInspectionMessageSend, "검품 카카오 메시지 보내기");
        CommonUI.ajax(urls.sendKakaoMessage, "PARAM", data, function (res) {
            const searchCondition = {
                fdId: wares.selectedInspect.fdId,
                type: "0"
            }
            comms.getInspectionList(searchCondition);
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
            "grid_main", "grid_customerList", "grid_msgPop"
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
                    dataField: "frRefType",
                    headerText: "구분",
                    width: 40,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.frRefType[value];
                    },
                }, {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                    width: 70,
                }, {
                    dataField: "frYyyymmdd",
                    headerText: "접수일자",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fd6Dt",
                    headerText: "인도일자",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 80,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
                    },
                }, {
                    dataField: "sumName",
                    headerText: "상품명",
                    style: "color_and_name",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                        `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                            const sumName = CommonUI.toppos.makeSimpleProductName(item);
                            return colorSquare + ` <span style="vertical-align: middle;">` + sumName + `</span>`;
                    }
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 80,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    },
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
                    dataField: "fdUrgentYn", // 급세탁이면 Y
                    headerText: "급",
                    width: 35,
                }, {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                    width: 100,
                }, /* { // UI가 완전히 확정될 때 까지 임시로 숨겨둠
                    dataField: "fdS2Dt",
                    headerText: "지사입고",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, */ {
                    dataField: "fdS4Dt",
                    headerText: "지사출고",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdS5Dt",
                    headerText: "완성일자",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "sendMsgBtn",
                    headerText: "메시지",
                    style: "grid_textalign_left",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = `
                                <button type="button" class="c-button c-button--supersmall">메시지</button>
                            `;
                        return template;
                    },
                },
            ];

            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : false,
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

            grids.s.columnLayout[2] = [
                {
                    dataField: "insertDt",
                    headerText: "등록일시",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fiType",
                    headerText: "유형",
                    width: 60,
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        return CommonData.name.fiType[value];
                    },
                }, {
                    dataField: "fiComment",
                    headerText: "검품내용",
                    style: "grid_textalign_left",
                }, {
                    dataField: "fiAddAmt",
                    headerText: "추가비용",
                    style: "grid_textalign_right",
                    width: 80,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fiSendMsgYn",
                    headerText: "메시지",
                    style: "grid_textalign_left",
                    width: 55,
                    styleFunction: ynStyle,
                }, {
                    dataField: "fiPhotoYn",
                    headerText: "이미지",
                    width: 55,
                    styleFunction: ynStyle,
                }, {
                    dataField: "fiCustomerConfirm",
                    headerText: "고객수락",
                    width: 65,
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        return CommonData.name.fiCustomerConfirm[value];
                    },
                },
            ];

            grids.s.prop[2] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : false,
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

        getSelectedCustomer() {
            return AUIGrid.getSelectedRows(grids.s.id[1])[0];
        },

        getSelectedInspection() {
            return AUIGrid.getSelectedRows(grids.s.id[2])[0];
        }
    },

    t: {
        basicTrigger() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                if(e.dataField === "sendMsgBtn") {
                    wares.selectedItem = e.item;
                    setupMsgPop(e);
                }
            });

            AUIGrid.bind(grids.s.id[2], "cellClick", function (e) {
                wares.selectedInspect = e.item;
                $("#messageField").val(e.item.fiComment);
                if(e.item.fiPhotoYn === "Y") {
                    $("#imgThumb").attr("src", e.item.ffPath + "s_" + e.item.ffFilename);
                    $("#imgFull").attr("href", e.item.ffPath + e.item.ffFilename);
                    $("#imgFull").show();
                    $("#isIncludeImgLabel").show();
                } else {
                    $("#imgFull").hide();
                    $("#isIncludeImgLabel").hide();
                }
            });
        },
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#searchCustomer").on("click", function () {
                mainSearch();
            });

            $("#selectCustomer").on("click", function () {
                const selectedItem = grids.f.getSelectedCustomer();
                selectCustomerFromList(selectedItem);
            });

            // 팝업 닫기
			$('.pop__close').on('click', function(e) {
				$(this).parents('.pop').removeClass('active');
				
				e.preventDefault();
			});

            $("#resetCustomer").on("click", function () {
                resetAll();
            });

            $("#filterDetail").on("click", function () {
                const filterCondition = {
                    bcId: wares.selectedCustomer.bcId,
                    searchTag: wares.searchTag,
                    filterFromDt: $("#filterFromDt").val().numString(),
                    filterToDt: $("#filterToDt").val().numString(),
                }
                comms.getDetailList(filterCondition);
            });

            $("#searchString").on("keypress", function(e) {
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                    mainSearch();
                }
            });

            $("#sendKakaoMsg").on("click", function(e) {
                const selectedInspect = grids.f.getSelectedInspection();
                wares.selectedInspect = selectedInspect;
                if(wares.selectedInspect) {
                    const data = {
                        isIncludeImg: "N",
                        fmMessage: $("#messageField").val(),
                        fiId: wares.selectedInspect.fiId,
                        bcId: wares.selectedItem.bcId,
                    }
                    if(wares.selectedInspect.fiPhotoYn === "Y") {
                        data.isIncludeImg = $("#isIncludeImg").is(":checked") ? "Y" : "N";
                    }
                    $("#messageField").val("");
                    comms.sendKakaoMessage(data);
                } else {
                    alertCaution("메시지를 보낼 검품내역을 선택해 주세요.", 1);
                }
            });

            $("#uncollectMoneyMain").parents("li").on("click", function () {
                if(wares.selectedCustomer && wares.selectedCustomer.bcHp) {
                    location.href = "/user/unpaid?bchp=" + wares.selectedCustomer.bcHp;
                } else {
                    location.href = "/user/unpaid";
                }
            });
        },

        vkeys() {
            $("#vkeyboard0").on("click", function() {
                onShowVKeyboard(0);
            });
            $("#vkeyboard1").on("click", function() {
                onShowVKeyboard(1);
            });
        },
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    selectedCustomer: {bcId:null},
    searchTag: null,
    selectedItem: {},
    selectedInspect: {},
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    grids.t.basicTrigger();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    trigs.s.basic();
    trigs.s.vkeys();
    
    enableDatepicker();

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
}

function ynStyle(rowIndex, columnIndex, value, headerText, item, dataField) {
    return value === "Y" ? "yesBlue" : "noRed"
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
    $("#uncollectMoneyMain").html(wares.selectedCustomer.uncollectMoney.toLocaleString());
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

function resetAll() {
    wares.selectedCustomer = {
        bcId: null,
        uncollectMoney: 0,
        saveMoney: 0,
        bcAddress: "",
        bcRemark: "",
    };
    wares.searchTag = null;
    putCustomer();
    grids.f.clearData(0);
    $("#searchType").val("0");
    $("#searchString").val("");

    const today = new Date().format("yyyy-MM-dd");
    $("#filterFromDt").val(today);
    $("#filterToDt").val(today);
}

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["searchString", "messageField"];

    vkeyProp[0] = {
        title: $("#searchType option:selected").html() + " (검색)",
        callback: mainSearch,
    };

    vkeyProp[1] = {
        title: "카카오 전달 메시지 입력",
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
                bcId: null,
                uncollectMoney: 0,
                saveMoney: 0,
                bcAddress: "",
                bcRemark: "",
            };
            putCustomer();
            const filterCondition = {
                bcId: null,
                searchTag: searchString,
                filterFromDt: $("#filterFromDt").val().numString(),
                filterToDt: $("#filterToDt").val().numString(),
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

function setupMsgPop(e) {
    resetMsgPop();
    $("#messagePop").addClass("active");
    grids.f.resize(2);
    const searchCondition = {
        fdId: e.item.fdId,
        type: "0"
    }
    comms.getInspectionList(searchCondition);
}

function resetMsgPop() {
    $("#isIncludeImg").prop("checked", true);
    $("#messageField").val("");
    $("#imgFull").attr("src", "");
    $("#imgFull").hide();
}

