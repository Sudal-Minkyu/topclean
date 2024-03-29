/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseUncollectCustomerList: { // searchText가 올 경우에는 검색하여 표기, 아니면 해당 지점의 미수금이 있는 고객 전체
            searchText: "",
            searchType: "", // 0: 통합, 1: 고객명, 2: 전화번호, 3: 주소
        },
        franchiseUncollectRequestList: {
            bcId: "",
        },
        franchiseUncollectRequestDetailList: {
            frId: "",
        },
        franchiseUncollectPayRequestList: {
            bcId: "",
            frIdList: "" // frId들이 담긴 배열형태
        },
        franchiseUncollectPay: {
            frIdList: "", // 배열로 가는 frId 번호들
            data: {
                fpRealAmt: "", // 위 frId의 결제된 총 미수금액
                fpType: "",
                fpMonth: "",
                fpCatApprovalno: "",
                fpCatApprovaltime: "",
                fpCatCardno: "",
                fpCatIssuercode: "",
                fpCatIssuername: "",
                fpCatMuechantnumber: "",
                fpCatMessage1: "",
                fpCatMessage2: "",
                fpCatNotice1: "",
                fpCatTotamount: "",
                fpCatVatamount: "",
                fpCatTelegramflagt: "",
            }
        },
    },
    receive: {
        franchiseUncollectCustomerList: {
            bcId: "nr",
            bcName: "s",
            bcHp: "",
            bcAddress: "",
            saveMoney: "", // 적립금
            uncollectMoney: "", // 미수금전체
        },
        franchiseUncollectRequestList: {
            frId: "nr",
            frYyyymmdd: "sr",
            requestDetailCount: "n", // 외 건수 requestDetailCount가 2일경우 -> 외 1건, 1일경우 그냥 상품이름만 표기하면될듯. 0일경우는 없음.
            bgName: "s",
            bsName: "s",
            biName: "s",
            frTotalAmount: "nr", // 접수금액
            frPayAmount: "nr", // 입금액
            uncollectMoney: "nr", // 미수금액
        },
        franchiseUncollectRequestDetailList: {
            fdPromotionType: "s",
            fdPromotionDiscountRate: "n",
            fdPollutionType: "n",
            fdPollutionBack: "n",
            frYyyymmdd: "s",
            fdTag: "s",
            
            fdColor: "",
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
            fdWhitening: "",
            fdPollution: "",
            fdWaterRepellent: "",
            fdStarch: "",
            fdUrgentYn: "s",

            fdTotAmt: "n",
            fdState: "s",
            fdS6Dt: "s", // 고객출고일로 수정
        },
        franchiseUncollectPayRequestList: {
            gridListData: {
                frPayAmount: "n", // 결제된 금액
                frId: "nr",
                frYyyymmdd: "sr",
                requestDetailCount: "n", // 외 건수 requestDetailCount가 2일경우 -> 외 1건, 1일경우 그냥 상품이름만 표기하면될듯. 0일경우는 없음.
                bgName: "s",
                bsName: "s",
                biName: "s",
                frTotalAmount: "nr", // 접수금액
                uncollectMoney: "nr", // 미수금액
            },
            payInfoData: {
                saveMoney: "n", // 해당 고객 적립금
                frCode: "",
                frName: "",
                frBusinessNo: "",
                frRpreName: "",
                frTelNo: "",
                bcName: "",
                bcHp: "",
            },
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    filterCustomerList: "/api/user/franchiseUncollectCustomerList", // 미수고객리스트
    customersUncollectedList: "/api/user/franchiseUncollectRequestList", // 선택 고객 미수 마스터
    uncollectedListDetail: "/api/user/franchiseUncollectRequestDetailList", // 선택 고객 미수 세부
    setupPaymentPop: "/api/user/franchiseUncollectPayRequestList",
    sendPaidInfo: "/api/user/franchiseUncollectPay",

}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    filterCustomerList(searchCondition = {searchText: "", searchType: ""}) {
        wares.searchCondition = searchCondition;
        dv.chk(searchCondition, dtos.send.franchiseUncollectCustomerList, "메인그리드 고객 필터 조건 보내기", true);
        CommonUI.ajax(urls.filterCustomerList, "GET", searchCondition, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseUncollectCustomerList, "메인그리드 고객 리스트 받아오기", true);
            grids.f.setData(0, data);
            grids.f.clearData(1);
            grids.f.clearData(2);
            $("#totalSelectedUncollectMoney").html("0");
            calculateGridCustomer();
        });
    },
    customersUncollectedList(selectedBcId) {
        dv.chk(selectedBcId, dtos.send.franchiseUncollectRequestList, "미수금 리스트 받기위한 고객 아이디 보내기");
        CommonUI.ajax(urls.customersUncollectedList, "GET", selectedBcId, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseUncollectRequestList, "선택된 고객의 미수금 리스트 받아오기", true);
            CommonUI.toppos.makeSimpleProductNameList(data);
            grids.f.setData(1, data);
            grids.f.clearData(2);
            $("#totalSelectedUncollectMoney").html("0");
            wares.customerBcId = selectedBcId.bcId;
        });
    },
    uncollectedListDetail(selectedFrId) {
        dv.chk(selectedFrId, dtos.send.franchiseUncollectRequestDetailList, "선택된 미수금 접수 아이디 보내기");
        CommonUI.ajax(urls.uncollectedListDetail, "GET", selectedFrId, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseUncollectRequestDetailList, "선택된 접수 아이디의 상세 리스트 받아오기", true);
            CommonUI.toppos.makeSimpleProductNameList(data);
            grids.f.setData(2, data);
        });
    },
    setupPaymentPop(selctedInfo) {
        dv.chk(selctedInfo, dtos.send.franchiseUncollectPayRequestList, "선택한 미수금 마스터 항목 보내기");
        CommonUI.ajax(urls.setupPaymentPop, "GET", selctedInfo, function(res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.franchiseUncollectPayRequestList, "미수 결제창 뜰 때 받아오는 항목");
            openPaymentPop(data);
        });
    },
    sendPaidInfo(paidInfo) {
        dv.chk(paidInfo, dtos.send.franchiseUncollectPay, "미수 결제 성공 후 정보 보내기");
        CommonUI.ajax(urls.sendPaidInfo, "MAPPER", paidInfo, function(res) {
            const frNo = res.sendData.frId.frNo;
            $("#paymentPop").removeClass("active");
            comms.filterCustomerList(wares.searchCondition);
            comms.customersUncollectedList({bcId: wares.customerBcId});

            alertCheck("미수 결제 완료 되었습니다.<br>고객용 영수증을 인쇄 하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                CommonUI.toppos.printReceipt(frNo, "", true, true, "N");
                $('#popupId').remove();
            });
            $("#checkDelCancelBtn").on("click", function () {
                CommonUI.toppos.printReceipt(frNo, "", false, true, "N");
            });
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
            "grid_customer", "grid_request", "grid_detail", "grid_payment"
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
                    width: 70,
                }, {
                    dataField: "bcHp",
                    headerText: "전화번호",
                    width: 120,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.formatTel(value);
                    },
                }, {
                    dataField: "bcAddress",
                    headerText: "주소",
                    style: "grid_textalign_left",
                }, {
                    dataField: "saveMoney",
                    headerText: "적립금",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "uncollectMoney",
                    headerText: "미수금",
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
                showAutoNoDataMessage: true,
                noDataMessage : "출력할 데이터가 없습니다.",
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
                    dataField: "frYyyymmdd",
                    headerText: "접수일",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "requestDetailCount",
                    headerText: "상품내역",
                    style: "grid_textalign_left",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        let rearText = "";
                        if(value > 1) {
                            rearText = " 외 " + (value - 1) + "건";
                        }
                        return CommonUI.toppos.makeSimpleProductName(item) + rearText;
                    },
                }, {
                    dataField: "frTotalAmount",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "frPayAmount",
                    headerText: "입금액",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "uncollectMoney",
                    headerText: "미수금액",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "none",
                showAutoNoDataMessage: false,
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[2] = [
                {
                    dataField: "frYyyymmdd",
                    headerText: "접수일",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 80,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
                    },
                }, {
                    dataField: "productName",
                    headerText: "상품명",
                    style: "color_and_name",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                        const productName = CommonUI.toppos.makeSimpleProductName(item);
                        return colorSquare + ` <span style="vertical-align: middle;">` + productName + `</span>`;
                    },
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 90,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
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
                    headerText: "상태",
                    width: 60,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "fdS6Dt",
                    headerText: "고객출고일",
                    width: 90,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                },
            ];

            grids.s.prop[2] = {
                editable : false,
                selectionMode : "singleRow",
                showAutoNoDataMessage: false,
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[3] = [
                {
                    dataField: "frYyyymmdd",
                    headerText: "접수일",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "requestDetailCount",
                    headerText: "상품내역",
                    style: "grid_textalign_left",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        let rearText = "";
                        if(value > 1) {
                            rearText = " 외 " + (value - 1) + "건";
                        }
                        return CommonUI.toppos.makeSimpleProductName(item) + rearText;
                    },
                }, {
                    dataField: "frTotalAmount",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "uncollectMoney",
                    headerText: "미수금액",
                    style: "grid_textalign_right",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
            ];

            grids.s.prop[3] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
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

        clearData(numOfGrid) {
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) {
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) {
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        },

        isCheckedRow(numOfGrid, item) {
            return AUIGrid.isCheckedRowById(grids.s.id[numOfGrid], item._$uid);
        },

        setCheckedRow(numOfGrid, item) {
            AUIGrid.addCheckedRowsByIds(grids.s.id[numOfGrid], [item._$uid]);
        },

        setUncheckedRow(numOfGrid, item) {
            AUIGrid.addUncheckedRowsByIds(grids.s.id[numOfGrid], [item._$uid]);
        },
    },

    t: {
        basic() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                const selectedBcId = {
                    bcId: e.item.bcId
                }
                comms.customersUncollectedList(selectedBcId);
            });

            AUIGrid.bind(grids.s.id[1], "cellClick", function (e) {
                if(grids.f.isCheckedRow(1, e.item)) {
                    grids.f.setUncheckedRow(1, e.item);
                } else {
                    grids.f.setCheckedRow(1, e.item);
                }
                getUncollectedListDetail(e.item);
            });

            AUIGrid.bind(grids.s.id[1], "rowCheckClick", function (e) {
                getUncollectedListDetail(e.item);
                calculateGridRequest();
            });

            AUIGrid.bind(grids.s.id[1], "rowAllCheckClick", function (e) {
                grids.f.clearData(2);
                calculateGridRequest();
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#customerSearchBtn").on("click", function () {
                mainSearch();
            });

            $("#openPaymentPop").on("click", function () {
                let checkedFrId = [];
                const items = grids.f.getCheckedItems(1);
                if(items.length) {
                    items.forEach(obj => {
                        checkedFrId.push(obj.item.frId);
                    });
                    const selctedInfo = {
                        frIdList: checkedFrId,
                        bcId: wares.customerBcId,
                    }
                    comms.setupPaymentPop(selctedInfo);
                }else{
                    alertCaution("결제할 미수금 내역을 선택해 주세요.", 1);
                }
            });

            $("#confirmPayment").on("click", function () {
                if ($("#payType1").is(":checked")) {
                    alertCheck(`미수금 ${$("#totalUncollectAmount").html()}원을 결제 처리 완료합니다.`
                        + `<br>현금 수령 완료하셨습니까?`);
                    $("#checkDelSuccessBtn").on("click", function () {
                        uncollectPaymentStageOne();
                        $('#popupId').remove();
                    });
                } else if ($("#payType2").is(":checked")) {
                    alertCheck(`미수금 ${$("#totalUncollectAmount").html()}원을 결제 처리 하시겠습니까?`);
                    $("#checkDelSuccessBtn").on("click", function () {
                        uncollectPaymentStageOne();
                        $('#popupId').remove();
                    });
                } else if ($("#payType3").is(":checked")) {
                    alertCheck(`미수금 ${$("#totalUncollectAmount").html()}원을 적립금으로<br>결제 처리 하시겠습니까?`);
                    $("#checkDelSuccessBtn").on("click", function () {
                        uncollectPaymentStageOne();
                        $('#popupId').remove();
                    });
                }
            });

            $("#cancelPayment").on("click", function () {
                $("#paymentPop").removeClass("active");
            });

            $("input[name='payType']").on("click", function () {
                const payType = $("input[name=payType]:checked").val();
                if(payType === "02") {
                    $("#payMonth").show();
                } else {
                    $("#payMonth").hide();
                    $("#payMonth").val("0");
                }
            });

            $("#searchCustomerField").on("keypress", function(e) {
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
    customerBcId: 0,
    frPaymentInfo: {},
    searchCondition: {},
    url: window.location.href,
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    grids.t.basic();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    trigs.s.basic();
    trigs.s.vkeys();

    hasBcHp();
}

function calculateGridCustomer() {
    const items = grids.f.getData(0);
    let totalSaveMoney = 0;
    let totalUncollectMoney = 0;
    items.forEach(item => {
        totalSaveMoney += item.saveMoney;
        totalUncollectMoney += item.uncollectMoney;
    });
    $("#totalSaveMoney").html(totalSaveMoney.toLocaleString());
    $("#totalUncollectMoney").html(totalUncollectMoney.toLocaleString());
}

function calculateGridRequest() {
    const items = grids.f.getCheckedItems(1);
    let totalSelectedUncollectMoney = 0;
    items.forEach(obj => {
        totalSelectedUncollectMoney += obj.item.uncollectMoney;
    });
    $("#totalSelectedUncollectMoney").html(totalSelectedUncollectMoney.toLocaleString());
}

function uncollectPaymentStageOne() {
    try {
        let paymentData =
            {
                franchiseNo: wares.frPaymentInfo.frCode,
                franchiseName: wares.frPaymentInfo.frName,
                businessNO: wares.frPaymentInfo.frBusinessNo,
                repreName: wares.frPaymentInfo.frRpreName,
                franchiseTel: wares.frPaymentInfo.frTelNo,
                customerName: wares.frPaymentInfo.bcName,
                customerTel: CommonUI.formatTel(wares.frPaymentInfo.bcHp),
                requestDt: new Date().format("yyyy-MM-dd HH:mm"),
                totalAmount: $("#totalUncollectAmount").html().toInt(),
                paymentAmount: $("#totalUncollectAmount").html().toInt(),
                month: $("#payMonth").val().toInt(),
            };

        const payType = $("input[name=payType]:checked").val();
        paymentData.fpType = payType;
        if (payType === "01") {
            paymentData.type = "cash";
        } else if (payType === "02") {
            paymentData.type = "card";
        } else if (payType === "03") {
            paymentData.type = "save";
        }

        if (paymentData.type ==="card") {
            $('#payStatus').show();
            try {
                CAT.CatCredit(paymentData, function (res) {
                    $('#payStatus').hide();
                    let resjson = JSON.parse(res);

                    // 결제 성공일경우 Print
                    if (resjson.STATUS === "SUCCESS") {
                        uncollectPaymentStageTwo(paymentData, resjson);
                        alertSuccess("카드 결제가 성공하였습니다.<br>단말기에서 카드를 제거해 주세요.");
                    }
                    // 결제 실패의 경우
                    if (resjson.STATUS === "FAILURE") {
                        $('#payStatus').hide();
                        if(resjson.ERRORDATA === "erroecode:404, error:error") {
                            alertCancel("카드결제 단말기 연결이 감지되지 않습니다.<br>연결을 확인해 주세요.");
                        } else if (resjson.ERRORDATA === "erroecode:0, error:timeout") {
                            alertCancel("유효 결제 시간이 지났습니다.<br>다시 결제창 버튼을 이용하여<br>원하시는 기능을 선택 해주세요.");
                        } else if(resjson.ERRORMESSAGE === "단말기에서종료키누름 /                  /                 ") {
                            alertCancel("단말기 종료키를 통해 결제가 중지되었습니다.");
                        } else {
                            alertCancel(resjson.ERRORMESSAGE);
                            console.log("결제 실패 분석:", resjson);
                        }
                    }
                });
            }catch (e) {
                CommonUI.toppos.underTaker(e, "unpaid : 카드 단말 결제");
            }
        }else if (paymentData.type === "cash") {
            uncollectPaymentStageTwo(paymentData);
        }else if (paymentData.type === "save") {
            uncollectPaymentStageTwo(paymentData);
        }
    }catch (e) {
        CommonUI.toppos.underTaker(e, "unpaid : 미수금 결제 1단계");
        return false;
    }
}

function uncollectPaymentStageTwo(paymentData, creditData = {}) {

    const frIdList = [];
    const frItems = grids.f.getData(3);
    frItems.forEach(item => {
        frIdList.push(item.frId);
    });

    const paidInfo = {
        frIdList: frIdList,
        data : {
            fpRealAmt: paymentData.totalAmount,
            fpType: paymentData.fpType,
            fpMonth: paymentData.month,
            fpCatApprovalno: creditData.APPROVALNO ? creditData.APPROVALNO : "",
            fpCatApprovaltime: creditData.APPROVALTIME ? creditData.APPROVALTIME : "",
            fpCatCardno: creditData.CARDNO ? creditData.CARDNO : "",
            fpCatIssuercode: creditData.ISSUERCODE ? creditData.ISSUERCODE : "",
            fpCatIssuername: creditData.ISSUERNAME ? creditData.ISSUERNAME : "",
            fpCatMuechantnumber: creditData.MERCHANTNUMBER ? creditData.MERCHANTNUMBER : "",
            fpCatMessage1: creditData.MESSAGE1 ? creditData.MESSAGE1 : "",
            fpCatMessage2: creditData.MESSAGE2 ? creditData.MESSAGE2 : "",
            fpCatNotice1: creditData.NOTICE1 ? creditData.NOTICE1 : "",
            fpCatTotamount: creditData.TOTAMOUNT ? creditData.TOTAMOUNT : "",
            fpCatVatamount: creditData.VATAMOUNT ? creditData.VATAMOUNT : "",
            fpCatTelegramflagt: creditData.TELEGRAMFLAG ? creditData.TELEGRAMFLAG : "",
        }
    }
    comms.sendPaidInfo(paidInfo);
}

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["searchCustomerField"];

    vkeyProp[0] = {
        title: $("#searchType option:selected").html() + " (검색)",
        callback: mainSearch,
    };

    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function mainSearch() {
    const searchCondition = {
        searchType: $("#searchType").val(),
        searchText: $("#searchCustomerField").val(),
    }
    $("#searchType").val(0);
    $("#searchCustomerField").val("");
    comms.filterCustomerList(searchCondition);
}

function hasBcHp() { // 고객의 전화번호가 주소창을 통해 넘어온 경우 (미수금 바로 조회하기 위함)
    const url = new URL(wares.url);
    wares.params = url.searchParams;

    if(wares.params.has("bchp")) {
        const bcHp = wares.params.get("bchp");
        const searchCondition = {
            searchType: "2",
            searchText: bcHp,
        }
        comms.filterCustomerList(searchCondition);
    }
}

function getUncollectedListDetail(item) {
    const selectedFrId = {
        frId: item.frId
    }
    comms.uncollectedListDetail(selectedFrId);
}

/* 결제창의 상태를 초기화하고 데이터를 계산하여 배치 */
function openPaymentPop(data) {
    const items = data.gridListData;
    grids.f.setData(3, items);
    wares.frPaymentInfo = data.payInfoData[0];

    let totalUncollectAmount = 0;
    items.forEach(item => {
        totalUncollectAmount += item.uncollectMoney;
    });
    $("#totalUncollectAmount").html(totalUncollectAmount.toLocaleString());

    $("#saveMoney").html(wares.frPaymentInfo.saveMoney.toLocaleString());
    if (totalUncollectAmount > wares.frPaymentInfo.saveMoney) {
        $("#payType3").parents("div .unpaid-pop__payment-select").hide();
    } else {
        $("#payType3").parents("div .unpaid-pop__payment-select").show();
    }

    $("#payType1").prop("checked", true);
    $("#payMonth").val("0");
    $("#paymentPop").addClass("active");
    grids.f.resize(3);
}