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
            console.log(data);
            dv.chk(data, dtos.receive.franchiseReceiptDeliveryList, "고객의 인도대상 세탁물 리스트 받아오기");
            grids.f.setData(0, data);
            calculateTotStatus();
            calculateCheckedStatus();
        });
    },

    deliverLaundry(selectedLaundry, goToUnpaid) {
        dv.chk(selectedLaundry, dtos.send.franchiseStateChange, "인도된 고객의 세탁물 리스트 보내기");
        CommonUI.ajax(urls.deliverLaundry, "PARAM", selectedLaundry, function(res) {
            alertSuccess("세탁물 인도 완료");
            const customerId = {
                bcId: wares.selectedCustomer.bcId
            };
            comms.getCustomersRequest(customerId);
            if(goToUnpaid) { // 미수금 메뉴로 넘어가기 상태일 경우 인도완료후 2초 뒤 이동
                if(wares.selectedCustomer.bcHp) {
                    setTimeout(function () {
                        location.href = "./unpaid?bchp=" + wares.selectedCustomer.bcHp;
                    }, 2000);
                }
            }
        });
    },

    setupPaymentPop(selctedInfo) {
        dv.chk(selctedInfo, dtos.send.franchiseUncollectPayRequestList, "선택한 미수금 마스터 항목 보내기");
        CommonUI.ajax("/api/user/franchiseUncollectPayRequestList", "GET", selctedInfo, function(res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.franchiseUncollectPayRequestList, "미수 결제창 뜰 때 받아오는 항목");
            grids.f.setData(2, data.gridListData);
            calculateGridPayment();
            wares.frPaymentInfo = data.payInfoData[0];
        });
        $("#paymentPop").addClass("active");
        grids.f.resize(2);
    },

    sendPaidInfo(paidInfo) {
        dv.chk(paidInfo, dtos.send.franchiseUncollectPay, "미수 결제 성공 후 정보 보내기");
        CommonUI.ajax("/api/user/franchiseUncollectPay", "MAPPER", paidInfo, function(res) {
            alertSuccess("미수 결제 완료 되었습니다.");
            wares.selectedCustomer.uncollectMoney = 0;
            putCustomer();
            $("#paymentPop").removeClass("active");
            $("#payMonth").val("0");
            $("#payType1").prop("checked", true);
        });
    },

    getFrInspectNeo(target) {
        CommonUI.ajax("/api/user/franchiseInspectionInfo", "GET", target, function (res) {
            console.log(res);
            const data = res.sendData;

            wares.currentFrInspect.inspeotInfoDto = data.inspeotInfoDto;
            wares.currentFrInspect.photoList = data.photoList;
            wares.currentFrInspect.fiAddAmt = data.inspeotInfoDto.fiAddAmt;
            wares.currentFrInspect.fiComment = data.inspeotInfoDto.fiComment;

            openFrInspectPop();
        });
    },

    getBrInspectNeo(target) {
        CommonUI.ajax("/api/user/franchiseInspectionInfo", "GET", target, function (res) {
            console.log(res);
            const data = res.sendData;

            wares.currentBrInspect.inspeotInfoDto = data.inspeotInfoDto;
            wares.currentBrInspect.photoList = data.photoList;
            wares.currentBrInspect.fiAddAmt = data.inspeotInfoDto.fiAddAmt;
            wares.currentBrInspect.fiComment = data.inspeotInfoDto.fiComment;

            openBrInspectPop();
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
            "grid_laundryList", "grid_customerList"
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
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 80,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value);
                    },
                }, {
                    dataField: "frRefType",
                    headerText: "구분",
                    width: 40,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.frRefType[value];
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
                    dataField: "frInsertDt",
                    headerText: "접수일자",
                    width: 90,
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        let result = "";
                        if(typeof value === "number") {
                            result = new Date(value).format("yyyy-MM-dd<br>hh:mm");
                        }
                        return result;
                    },
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 80,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let template = "";
                        if(item.photoList && item.photoList.length) {
                            template = `<img src="/assets/images/icon__picture.svg" onclick="openReceiptPhotoPop(${rowIndex})">`;
                        }

                        return template + CommonUI.toppos.processName(item);
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
                    dataField: "fdS4Type",
                    headerText: "출고타입",
                    width: 85,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdS4Type[value];
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
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        let template = "";
                        if(item.frFiId && item.frFiCustomerConfirm === "1") {
                            template = `<button class="c-state c-state--yellow" `
                                + `onclick="openFrInspectPopFromRemark(${rowIndex})">검품</button>`;
                        } else if(item.frFiId && item.frFiCustomerConfirm === "2") {
                            template = `<button class="c-state c-state--modify" `
                                + `onclick="openFrInspectPopFromRemark(${rowIndex})">검품</button>`;
                        } else if(item.frFiId && item.frFiCustomerConfirm === "3") {
                            template = `<button class="c-state c-state--cancel" `
                                + `onclick="openFrInspectPopFromRemark(${rowIndex})">검품</button>`;
                        }

                        if (item.fdState === "B" && item.brFiId && item.brFiCustomerConfirm === "1") {
                            template += `<button class="c-state c-state--yellow" `
                                + `onclick="openBrInspectPopFromRemark(${rowIndex})">확인품</button>`;
                        } else if (item.brFiId && item.brFiCustomerConfirm === "2") {
                            template += `<button class="c-state c-state--modify" `
                                + `onclick="openBrInspectPopFromRemark(${rowIndex})">확인품</button>`;
                        } else if (item.brFiId && item.brFiCustomerConfirm === "3") {
                            template += `<button class="c-state c-state--cancel" `
                                + `onclick="openBrInspectPopFromRemark(${rowIndex})">확인품</button>`;
                        }

                        return template + value;
                    },
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
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
                independentAllCheckBox: true,
                rowStyleFunction : function(rowIndex, item) {
                    let returnClass = "";
                    if(["S5", "S8"].includes(item.fdState)) {
                        returnClass = "grid_checkable_row";
                    }
                    return returnClass;
                },
                rowCheckVisibleFunction : function(rowIndex, isChecked, item) {
                    let isVisible = false;
                    if(["S5", "S8"].includes(item.fdState)) {
                        isVisible = true;
                    }
                    return isVisible;
                },
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
                    style: "grid_textalign_left",
                    headerText: "주소",
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

        getItemByRowIndex(rowId) {
            return AUIGrid.getItemByRowIndex(grids.s.id[0], rowId);
        },
    },

    t: {
        basicTrigger() {
            AUIGrid.bind(grids.s.id[0], "rowCheckClick", function (e) {
                calculateCheckedStatus();
            });

            AUIGrid.bind(grids.s.id[0], "rowAllCheckClick", function (checked) {
                if (checked) {
                    AUIGrid.addCheckedRowsByValue(grids.s.id[0], "fdState", ["S5", "S8"]);
                } else {
                    AUIGrid.addUncheckedRowsByValue(grids.s.id[0], "fdState", ["S5", "S8"]);
                }
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
                    uncollectMoney: 0,
                    saveMoney: 0,
                    bcAddress: "",
                    bcRemark: "",
                };
                putCustomer();
            });

            $("#deliverLaundry").on("click", function () {
                wares.checkedItems = grids.f.getCheckedItems(0);
                if(wares.checkedItems.length) {
                    if(wares.selectedCustomer.uncollectMoney) {
                        alertCheck(`미수금이 ${wares.selectedCustomer.uncollectMoney.toLocaleString()}원`
                            + ` 존재합니다.<br>인도완료 후 미수금 결제를 하시겠습니까?`);
                        $("#checkDelSuccessBtn").on("click", function () { // 인도완료후 미수금결제
                            $('#popupId').remove();
                            giveLaundry(true);
                        });
                        $("#checkDelCancelBtn").on("click", function () {
                            setTimeout(function () {
                                alertCheck("선택된 세탈물을 인도 하고<br>미수금 결제는 하지 않으시겠습니까?");
                                $("#checkDelSuccessBtn").on("click", function () { // 인도완료후 미수금결제
                                    $('#popupId').remove();
                                    giveLaundry();
                                });
                            }, 0);
                        });

                    } else {
                        alertCheck("선택된 세탁물들을 인도 하시겠습니까?");
                        $("#checkDelSuccessBtn").on("click", function () {
                            $('#popupId').remove();
                            giveLaundry();
                        });
                    }
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

            $("#uncollectMoneyMain").parents("li").on("click", function () {
                if(wares.selectedCustomer && wares.selectedCustomer.bcHp) {
                    location.href = "/user/unpaid?bchp=" + wares.selectedCustomer.bcHp;
                } else {
                    location.href = "/user/unpaid";
                }
            });

            $("#repay").on("click", function () {
                if(wares.selectedCustomer.bcId) {
                    if(wares.selectedCustomer.uncollectMoney) {
                        const target = {
                            bcId: wares.selectedCustomer.bcId,
                            frIdList: [],
                        }
                        comms.setupPaymentPop(target);
                    } else {
                        alertCaution("결제하실 미수금이 없습니다.", 1);
                    }
                } else {
                    alertCaution("먼저 고객을 선택해 주세요.", 1);
                }
            });

            $("#cancelPayment").on("click", function () {
                $("#paymentPop").removeClass("active");
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
                    alertCheck(`미수금 ${$("#totalUncollectAmount").html()}원을 결제 처리 완료합니다.`);
                    $("#checkDelSuccessBtn").on("click", function () {
                        uncollectPaymentStageOne();
                        $('#popupId').remove();
                    });
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
    checkedItems: {},
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

    getParamsAndAction();

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
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

    if(wares.selectedCustomer.bcId !== null) {
        const customerId = {
            bcId: wares.selectedCustomer.bcId
        };
        comms.getCustomersRequest(customerId);
    }

    setTopMenuHref();
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

function giveLaundry(goToUnpaid = false) {
    let laundry = [];
    let type1 = false;
    let type2 = false;
    let type3 = false;
    wares.checkedItems.forEach(obj => {
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
        comms.deliverLaundry(selectedLaundry, goToUnpaid);
    }else{
        alertCaution("현재 구분:일반 외의 항목은 세탁물 인도가 불가능합니다.", 1);
    }
}

/* 브라우저의 get 파라미터들을 가져오고 그에 따른 작업을 반영하기 위해 */
function getParamsAndAction() {
    const url = new URL(window.location.href);
    const params = url.searchParams;

    if(params.has("bchp")) {
        const bcHp = params.get("bchp");
        $("#searchType").val("2");
        $("#searchString").val(bcHp);
        mainSearch();
    }
}

function setTopMenuHref() {
    if(wares.selectedCustomer.bcHp) {
        $("#menuReceiptreg").attr("href", `/user/receiptreg?bchp=${wares.selectedCustomer.bcHp}`);
        $("#menuDelivery").attr("href", `/user/delivery?bchp=${wares.selectedCustomer.bcHp}`);
        $("#menuIntegrate").attr("href", `/user/integrate?bchp=${wares.selectedCustomer.bcHp}`);
    } else {
        $("#menuReceiptreg").attr("href", `/user/receiptreg`);
        $("#menuDelivery").attr("href", `/user/delivery`);
        $("#menuIntegrate").attr("href", `/user/integrate`);
    }
}

function calculateGridPayment() {
    const items = grids.f.getData(2);
    let totalUncollectAmount = 0;
    items.forEach(item => {
        totalUncollectAmount += item.uncollectMoney;
    });
    $("#totalUncollectAmount").html(totalUncollectAmount.toLocaleString());
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
        }

        if (paymentData.type ==="card") {
            $('#payStatus').show();
            try {
                CAT.CatCredit(paymentData, function (res) {
                    $('#payStatus').hide();
                    let resjson = JSON.parse(res);

                    // 결제 성공일경우
                    if (resjson.STATUS === "SUCCESS") {
                        uncollectPaymentStageTwo(paymentData, resjson);
                    }
                    // 결제 실패의 경우
                    if (resjson.STATUS === "FAILURE") {
                        console.log(resjson);
                        $('#payStatus').hide();
                        alertCancel("단말기 처리 중 에러가 발생하였습니다<br>잠시후 다시 시도해주세요");
                    }
                });
            }catch (e) {
                CommonUI.toppos.underTaker(e, "unpaid : 카드 단말 결제");
            }
        }else if (paymentData.type ==="cash") {
            uncollectPaymentStageTwo(paymentData);
        }
    }catch (e) {
        CommonUI.toppos.underTaker(e, "unpaid : 미수금 결제 1단계");
        return false;
    }
}

function uncollectPaymentStageTwo(paymentData, creditData = {}) {

    const frIdList = [];
    const frItems = grids.f.getData(2);
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

function openReceiptPhotoPop(rowId) {
    const photoList = grids.f.getItemByRowIndex(rowId).photoList;
    for(const {ffPath, ffFilename, ffRemark} of photoList) {
        const htmlCast = `
            <li class="tag-imgs__item">
                <a href="${ffPath + ffFilename}" data-lightbox="images" data-title="이미지 확대">
                    <img src="${ffPath + "s_" + ffFilename}" alt="" class="tag-imgs__img"/>
                    <span class="tag-imgs__title">${ffRemark}</span>
                </a>
            </li>
        `;
        $("#receiptPhotoList").append(htmlCast);
    }
    $("#receiptPhotoPop").addClass("active");
}

function openFrInspectPopFromRemark(rowIndex) {
    const item = grids.f.getItemByRowIndex(rowIndex);
    openFrInspectEditPop(item);
}

function openBrInspectPopFromRemark(rowIndex) {
    const item = grids.f.getItemByRowIndex(rowIndex);
    wares.currentBrInspect = item;
    if(item.brFiId) {
        const target = {
            fiId: item.brFiId,
        }
        comms.getBrInspectNeo(target);
    }
}

function openFrInspectEditPop(item) {
    wares.currentFrInspect = item;
    if(item.frFiId) {
        const target = {
            fiId: item.frFiId,
        }
        comms.getFrInspectNeo(target);
    }
}

async function openFrInspectPop() {
    $("#frViewFdTotAmtInPut").val(wares.currentFrInspect.fdTotAmt
        ? wares.currentFrInspect.fdTotAmt.toLocaleString() : "0");
    $("#frViewFiComment").val(wares.currentFrInspect.fiComment
        ? wares.currentFrInspect.fiComment : "");
    $("#frViewFiAddAmt").val(wares.currentFrInspect.fiAddAmt
        ? wares.currentFrInspect.fiAddAmt.toLocaleString() : "0");

    if(wares.currentFrInspect.photoList) {
        if(wares.currentFrInspect.photoList.length) {
            $("#frInspectViewPhotoPanel").show();
        } else {
            $("#frInspectViewPhotoPanel").hide();
        }
        for (const photo of wares.currentFrInspect.photoList) {
            const photoHtml = `
            <li class="tag-imgs__item">
                <a href="${photo.ffPath + photo.ffFilename}" data-lightbox="images" data-title="이미지 확대">
                    <img src="${photo.ffPath + "s_" + photo.ffFilename}" class="tag-imgs__img" alt=""/>
                </a>
            </li>
            `;
            $("#frViewPhotoList").append(photoHtml);
        }
    }

    $("#frInspectViewPop").addClass("active");
}

function openBrInspectPop() {
    $("#brFdTotAmtInPut").val(wares.currentBrInspect.fdTotAmt
        ? wares.currentBrInspect.fdTotAmt.toLocaleString() : "0");
    $("#brFiComment").val(wares.currentBrInspect.fiComment
        ? wares.currentBrInspect.fiComment : "");
    $("#brFiAddAmt").val(wares.currentBrInspect.fiAddAmt
        ? wares.currentBrInspect.fiAddAmt.toLocaleString() : "0");

    if(wares.currentBrInspect.photoList) {
        if(wares.currentBrInspect.photoList.length) {
            $("#brInspectPhotoPanel").show();
        } else {
            $("#brInspectPhotoPanel").hide();
        }
        for (const photo of wares.currentBrInspect.photoList) {
            const photoHtml = `
                <li class="tag-imgs__item">
                    <a href="${photo.ffPath + photo.ffFilename}" data-lightbox="images" data-title="이미지 확대">
                        <img src="${photo.ffPath + "s_" + photo.ffFilename}" class="tag-imgs__img" alt=""/>
                    </a>
                </li>
                `;
            $("#brPhotoList").append(photoHtml);
        }
    }

    if(wares.currentBrInspect.brFiCustomerConfirm === "1") {
        $("#brCustomerDenied").parents("li").show();
        $("#brCustomerConfirmed").parents("li").show();
    } else {
        $("#brCustomerDenied").parents("li").hide();
        $("#brCustomerConfirmed").parents("li").hide();
    }

    // fiCustomerConfirm 에 따른 수락거부 여부 추가?
    $("#brInspectPop").addClass("active");
}