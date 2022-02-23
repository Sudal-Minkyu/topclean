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
        franchiseReceiptDeliveryList: {
            bcId: "n"
        },

        franchiseStateChange: {
            fdIdList: "",
            stateType: "sr",
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

        franchiseReceiptDeliveryList: {
            fdEstimateDt: "s",
            frRefType: "sr", // 1월 19일 추가
            bcName: "s", // 고객의 이름
            frYyyymmdd: "s", // 접수일자
            fdId: "n",
            fdTag: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            fdS2Dt: "s",
            fdS4Dt: "s",
            fdS5Dt: "s",
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
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    searchCustomer: "/api/user/customerInfo", // 고객 검색
    getCustomersRequest: "/api/user/franchiseReceiptDeliveryList", // 고객에게 인도할 수 있는 세탁리스트 가져오기
    deliverLaundry: "/api/user/franchiseStateChange", // 인도된 고객의 세탁물 리스트 보내기
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    searchCustomer(searchCondition) {
        dv.chk(searchCondition, dtos.send.customerInfo, "고객 검색 조건 보내기");
        CommonUI.ajax(urls.searchCustomer, "GET", searchCondition, function (req) {
            const items = req.sendData.gridListData;
            $("#searchCustomerType").val(0);
            $("#searchCustomerField").val("");
            dv.chk(items, dtos.receive.customerInfo, "검색된 고객 리스트 받기");
            if(items.length === 1) {
                wares.selectedCustomer = items[0];
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

    getCustomersRequest(customerId) {
        dv.chk(customerId, dtos.send.franchiseReceiptDeliveryList, "선택된 고객 아이디 보내기");
        CommonUI.ajax(urls.getCustomersRequest, "GET", customerId, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseReceiptDeliveryList, "고객의 인도대상 세탁물 리스트 받아오기");
            grids.f.setData(0, data);
            calculateTotStatus();
            calculateCheckedStatus();
        });
    },

    deliverLaundry(selectedLaundry) {
        dv.chk(selectedLaundry, dtos.send.franchiseStateChange, "인도된 고객의 세탁물 리스트 보내기");
        CommonUI.ajax(urls.deliverLaundry, "PARAM", selectedLaundry, function(res) {
            alertSuccess("세탁물 인도 완료");
            const customerId = {
                bcId: wares.selectedCustomer.bcId
            };
            comms.getCustomersRequest(customerId);
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
            "grid_laundryList", "grid_customerList" ,"grid_selectedLaundry"
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
                    width: 70,
                }, {
                    dataField: "frYyyymmdd",
                    headerText: "접수일자",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdEstimateDt",
                    headerText: "출고예정",
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
                    width: 80,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    },
                }, {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
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
                    width: 100,
                }, {
                    dataField: "fdS2Dt",
                    headerText: "지사입고",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
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
                },
            ];

            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "인도할 세탁물이 존재하지 않습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
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
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                rowNumHeaderText : "순번",
                enableColumnResize : false,
                enableFilter : true,
                width : 830,
                height : 480,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[2] = [
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

            grids.s.prop[2] = {
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

        clearData(numOfGrid) {
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) {
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) {
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        },

        getSelectedCustomer() {
            return AUIGrid.getSelectedRows(grids.s.id[1])[0];
        },
    },

    t: {
        basicTrigger() {
            AUIGrid.bind(grids.s.id[0], "rowCheckClick", function (e) {
                calculateCheckedStatus();
            });

            AUIGrid.bind(grids.s.id[0], "rowAllCheckClick", function (checked) {
                calculateCheckedStatus();
            });

        }
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

            $("#resetCustomer").on("click", function () {
                wares.selectedCustomer = {
                    bcId: null,
                    beforeUncollectMoney: 0,
                    saveMoney: 0,
                    bcAddress: "",
                    bcRemark: "",
                };
                putCustomer();
            });

            $("#deliverLaundry").on("click", function () {
                const items = grids.f.getCheckedItems(0);
                if(items.length) {
                    alertCheck("선택된 세탁물들을 인도 하시겠습니까?");
                    $("#checkDelSuccessBtn").on("click", function () {
                        let laundry = [];
                        let type1 = false;
                        let type2 = false;
                        let type3 = false;
                        items.forEach(obj => {
                            laundry.push(obj.item.fdId);
                            if(obj.item.frRefType === "01") type1 = true;
                            if(obj.item.frRefType === "02") type2 = true;
                            if(obj.item.frRefType === "03") type3 = true;
                        });

                        if(type1 && !type2 && !type3) {
                            const selectedLaundry = {
                                fdIdList: laundry,
                                stateType: "S5",
                            }
                            comms.deliverLaundry(selectedLaundry);
                            $('#popupId').remove();
                        }else{
                            alertCaution("현재 구분:일반 외의 항목은 세탁물 인도가 불가능합니다.", 1);
                        }
                    });
                } else {
                    alertCaution("인도할 세탁물을 선택해 주세요.", 1);
                }
            });

            // 팝업 닫기
			$('.pop__close').on('click', function(e) {
				$(this).parents('.pop').removeClass('active');
				
				e.preventDefault()
			});

            $("#searchString").on("keypress", function(e) {
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                    mainSearch();
                }
            });
        },
        vkeys() {
            $("#vkeyboard0").on("click", function() {
                onShowVKeyboard(0);
            });
        },
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    selectedCustomer: {},
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

    if(wares.selectedCustomer.bcId !== null) {
        const customerId = {
            bcId: wares.selectedCustomer.bcId
        };
        comms.getCustomersRequest(customerId);
    }
}

function selectCustomerFromList(selectedItem) {
    if(selectedItem) {
        wares.selectedCustomer = selectedItem;
        putCustomer();
        $("#customerListPop").removeClass("active");
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function calculateTotStatus() {
    const items = grids.f.getData(0);
    let count = 0;
    let totAmt = 0;
    items.forEach(item => {
        count++;
        totAmt += item.fdTotAmt;
    });
    $("#totCount").val(count.toLocaleString());
    $("#totAmount").val(totAmt.toLocaleString());
}

function calculateCheckedStatus() {
    const items = grids.f.getCheckedItems(0);
    let chkCount = 0;
    let chkAmt = 0;
    items.forEach(obj => {
        chkCount++;
        chkAmt += obj.item.fdTotAmt;
    });
    $("#chkCount").val(chkCount.toLocaleString());
    $("#chkAmount").val(chkAmt.toLocaleString());
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
    const searchCondition = {
        searchType: $("#searchType").val(),
        searchString: $("#searchString").val(),
    }
    if(searchCondition.searchString === "") {
        alertCaution("검색조건을 입력해 주세요.", 1);
    } else {
        $("#searchType").val(0);
        $("#searchString").val("");
        comms.searchCustomer(searchCondition);
    }
}