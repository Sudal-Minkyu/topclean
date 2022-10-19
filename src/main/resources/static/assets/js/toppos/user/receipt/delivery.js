/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseUncollectPayRequestList: {
            bcId: "",
            frIdList: "" // frId들이 담긴 배열형태
        },

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
            tempSaveFrNo: "s",
            tempSaveBcName: "s",
        },

        franchiseReceiptDeliveryList: {
            frNo: "s",
            fdPromotionType: "s",
            fdPromotionDiscountRate: "n",
            fdPollutionBack: "n",
            photoList: "a",
            brFiId: "n",
            fdS4Type: "s",
            frFiId: "n",
            frFiCustomerConfirm: "s",
            frInsertDt: "n",
            brFiCustomerConfirm: "s",
            fdPollutionType: "n",

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
    getCustomersRequest: "/api/user/franchiseReceiptDeliveryList", // 고객에게 출고할 수 있는 세탁리스트 가져오기
    deliverLaundry: "/api/user/franchiseStateChange", // 출고된 고객의 세탁물 리스트 보내기
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
                let additionalCondition = params.searchString;
                if (additionalCondition.numString().length) {
                    additionalCondition = "?bchp=" + additionalCondition.numString();
                } else {
                    additionalCondition = "?bcname=" + additionalCondition;
                }
                $("#checkDelSuccessBtn").on("click", function () {
                    location.href="/user/customerreg" + additionalCondition;
                });
            }
        });
    },

    getCustomersRequest(customerId) {
        dv.chk(customerId, dtos.send.franchiseReceiptDeliveryList, "선택된 고객 아이디 보내기");
        CommonUI.ajax(urls.getCustomersRequest, "GET", customerId, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseReceiptDeliveryList, "고객의 출고대상 세탁물 리스트 받아오기");
            CommonUI.toppos.makeSimpleProductNameList(data);
            grids.f.setData(0, data);
            calculateTotStatus();
            calculateCheckedStatus();
        });
    },

    deliverLaundry(selectedLaundry) {
        dv.chk(selectedLaundry, dtos.send.franchiseStateChange, "출고된 고객의 세탁물 리스트 보내기");
        CommonUI.ajax(urls.deliverLaundry, "PARAM", selectedLaundry, function(res) {
            if ($("#isPrintReleaseReceipt").is(":checked")) {
                const releaseItems = refineCheckedItemsForReleaseReceipt(wares.checkedItems);
                CommonUI.toppos.printReleaseReceipt(releaseItems);
            }
            alertSuccess("세탁물 고객출고 완료");
            const customerId = {
                bcId: wares.selectedCustomer.bcId
            };
            comms.getCustomersRequest(customerId);
            if(wares.selectedCustomer.uncollectMoney && $("#isPrintReceivableReceipt").is(":checked")) {
                if(wares.selectedCustomer.bcHp) {
                    // 미수금 결제팝업 1초 뒤 띄운다.
                    setTimeout(function () {
                        if(wares.selectedCustomer.bcId) {
                            if(wares.selectedCustomer.uncollectMoney) {
                                const target = {
                                    bcId: wares.selectedCustomer.bcId,
                                    frIdList: [],
                                }
                                comms.setupPaymentPop(target);
                            }
                        }
                        $('#popupId').remove();
                    }, 1000);
                }
            }
        });
    },

    setupPaymentPop(selctedInfo) {
        dv.chk(selctedInfo, dtos.send.franchiseUncollectPayRequestList, "선택한 미수금 마스터 항목 보내기");
        CommonUI.ajax("/api/user/franchiseUncollectPayRequestList", "GET", selctedInfo, function(res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.franchiseUncollectPayRequestList, "미수 결제창 뜰 때 받아오는 항목");
            openPaymentPop(data);
        });
    },

    sendPaidInfo(paidInfo) {
        dv.chk(paidInfo, dtos.send.franchiseUncollectPay, "미수 결제 성공 후 정보 보내기");
        CommonUI.ajax("/api/user/franchiseUncollectPay", "MAPPER", paidInfo, function(res) {
            const frNo = res.sendData.frId.frNo;
            wares.selectedCustomer.uncollectMoney = 0;
            putCustomer();
            $("#paymentPop").removeClass("active");

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

    getFrInspectNeo(target) {
        CommonUI.ajax("/api/user/franchiseInspectionInfo", "GET", target, function (res) {
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
            "grid_laundryList", "grid_customerList", "grid_payment"
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
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
                    },
                }, {
                    dataField: "frRefType",
                    headerText: "구분",
                    width: 40,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.frRefType[value];
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
                    }
                }, {
                    dataField: "frInsertDt",
                    headerText: "접수일자",
                    width: 90,
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
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
                    width: 120,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        let template = "";
                        if(item.photoList && item.photoList.length) {
                            template = `<img src="/assets/images/icon__picture.svg" onclick="openReceiptPhotoPop(${rowIndex})"> `;
                        }

                        return template + CommonUI.toppos.processName(item);
                    },
                }, {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    width: 80,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 85,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "fdS4Type",
                    headerText: "출고타입",
                    width: 95,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdS4Type[value];
                    },
                }, {
                    dataField: "fdUrgentYn", // 급세탁이면 Y
                    headerText: "급",
                    width: 30,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return value === "Y" ? "√" : "";
                    },
                }, {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                    width: 180,
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
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
                        } else if (item.brFiId) {
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
                noDataMessage : "고객출고할 세탁물이 존재하지 않습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : true,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
                independentAllCheckBox: true,
                rowStyleFunction(rowIndex, item) {
                    let returnClass = "";
                    if(["S5", "S8"].includes(item.fdState)) {
                        returnClass = "grid_checkable_row";
                    }
                    return returnClass;
                },
                rowCheckVisibleFunction(rowIndex, isChecked, item) {
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
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
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
                enableColumnResize : true,
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

            grids.s.prop[2] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
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

        getData(gridNum) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[gridNum]);
        },

        setData(gridNum, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[gridNum], data);
        },

        clearData(gridNum) {
            AUIGrid.clearGridData(grids.s.id[gridNum]);
        },

        resize(num) {
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(gridNum) {
            return AUIGrid.getCheckedRowItems(grids.s.id[gridNum]);
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
                calculateTotStatus();
            });

            $("#deliverLaundry").on("click", function () {
                wares.checkedItems = grids.f.getCheckedItems(0);
                if(wares.checkedItems.length) {
                    alertCheck("선택된 세탁물들을 고객출고 하시겠습니까?");
                    $("#checkDelSuccessBtn").on("click", function () {
                        $('#popupId').remove();
                        giveLaundry();
                    });
                } else {
                    alertCaution("고객출고할 세탁물을 선택해 주세요.", 1);
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

            $("#closeFrInspectViewPop").on("click", function () {
                closeFrInspectViewPop();
            });

            $("#closeBrInspectPop").on("click", function () {
                closeBrInspectPop();
            });

            $("#closePhotoListPop").on("click", function () {
                $("#receiptPhotoList").html("");
                $("#receiptPhotoPop").removeClass("active");
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

            $("#printReceipt").on("click", function() {
                wares.checkedItems = grids.f.getCheckedItems(0);
                if(wares.checkedItems.length) {
                    alertThree("영수증을 인쇄 하시겠습니까?", "고객용", "매장용", "취소");
                    $("#popFirstBtn").on("click", function () {
                        printReceiptAll(wares.checkedItems, true, false);
                        $('#popupId').remove();
                    });
                    $("#popSecondBtn").on("click", function () {
                        printReceiptAll(wares.checkedItems, false, true);
                        $('#popupId').remove();
                    });
                    $("#popThirdBtn").on("click", function () {
                        $('#popupId').remove();
                    });
                } else {
                    alertCaution("영수증을 출력할 상품을 선택해 주세요.", 1);
                }
            });

            $("#isPrintReleaseReceipt, #isReceiveOutstandingCollection").on("click", function (e) {
                console.log(e);
                setCookie(e.target.id, e.target.checked);
                e.target.id
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
    /* 헤더의 상단 해당메뉴 큰 버튼의 아이콘 및 색 활성화 */
    $("#menuDelivery").addClass("active")
        .children("img").attr("src", "/assets/images/icon__send--active.svg");

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

function giveLaundry() {
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
        comms.deliverLaundry(selectedLaundry);
    }else{
        alertCaution("현재 구분:일반 외의 항목은 고객출고가 불가능합니다.", 1);
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
        } else if (payType === "03") {
            paymentData.type = "save";
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
        } else if (paymentData.type ==="cash") {
            uncollectPaymentStageTwo(paymentData);
        } else if (paymentData.type === "save") {
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
    let totAmt = wares.currentFrInspect.fdTotAmt ? wares.currentFrInspect.fdTotAmt : 0 ;
    if(wares.currentFrInspect.frFiCustomerConfirm === "2") {
        totAmt -= wares.currentFrInspect.fiAddAmt;
    }
    $("#frViewFdTotAmtInPut").val(totAmt.toLocaleString());

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
    let totAmt = wares.currentBrInspect.fdTotAmt ? wares.currentBrInspect.fdTotAmt : 0 ;
    if(wares.currentBrInspect.brFiCustomerConfirm === "2") {
        totAmt -= wares.currentBrInspect.fiAddAmt;
    }
    $("#brFdTotAmtInPut").val(totAmt.toLocaleString());

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

function closeFrInspectViewPop(doRefresh = false) {
    if(doRefresh) {
        comms.franchiseRequestDetailSearch(wares.currentCondition);
    }
    $("#frInspectViewPop").removeClass("active");
    resetFrInspectViewPop();
}

function closeBrInspectPop(doRefresh = false) {
    if(doRefresh) {
        comms.franchiseRequestDetailSearch(wares.currentCondition);
    }
    $("#brInspectPop").removeClass("active");
    resetBrInspectPop();
}

function resetFrInspectViewPop() {
    $("#frViewFdTotAmtInPut").val("");
    $("#frViewFiAddAmt").val("0");
    $("#frViewFiComment").val("");
    $("#frViewPhotoList").html("");
}

function resetBrInspectPop() {
    $("#brFdTotAmtInPut").val("");
    $("#brFiAddAmt").val("0");
    $("#brFiComment").val("");
    $("#brPhotoList").html("");
}

/**
 * 영수증 다중 인쇄 기능
 * @param items : array<json> - 인쇄를 할 아이템이 담긴 리스트
 * @param doPrintCustomers : boolean - 고객용 인쇄 여부
 * @param doPrintOwners : boolean - 매장용 인쇄 여부
 */
function printReceiptAll(items, doPrintCustomers, doPrintOwners) {
    const printScreen = $("#printScreen");
    printScreen.show();
    const frNoList = [];
    for (const {item} of items) {
        if (!frNoList.includes(item.frNo)) {
            frNoList.push(item.frNo);
        }
    }

    let i = 0;
    wares.interval = setInterval(function () {
        CommonUI.toppos.printReceipt(frNoList[i++], "", doPrintCustomers, doPrintOwners, "N");
        if (i === frNoList.length) {
            printScreen.hide();
            clearInterval(wares.interval);
        }
    }, 1200);
}

/* 결제창의 상태를 초기화하고 데이터를 계산하여 배치 */
function openPaymentPop(data) {
    const items = data.gridListData;
    grids.f.setData(2, items);
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
    grids.f.resize(2);
}

/**
 * 선택된 세탁물들을 바탕으로, 출고증에 필요한 형식의 데이터를 구성하여 반환한다.
 * @returns {{totalAmount: number, frNo: string, items: *[]}}
 */
function refineCheckedItemsForReleaseReceipt (checkedItems) {
    const colorName = { // 컬러코드에 따른 실제 색상
        "00": "", "01": "흰색", "02": "검정", "03": "회색", "04": "빨강", "05": "주황",
        "06": "노랑", "07": "초록", "08": "파랑", "09": "남색", "10": "보라", "11": "핑크",
    };

    const refinedData = {
        frNo: "",
        totalAmount: 0,
        items: [],
    };

    for (const {item} of checkedItems) {
        const refinedItem = {
            color: colorName[item.fdColor],
            fdRemark: item.fdRemark,
            itemname: item.productName,
            price: item.fdTotAmt,
            priceGrade: item.fdPriceGrade,
            tagno: item.fdTag,
        };

        refinedData.frNo = item.frNo;
        refinedData.totalAmount += item.fdTotAmt;
        refinedData.items.push(refinedItem);
    }

    return refinedData;
}

function setCookie(key, value, exdays = 7) {
    let cookieValue = getCookie();
    cookieValue[key] = value;
    const today = new Date();
    today.setTime(today.getTime() + (exdays * 24 * 60 * 60 * 1000));
    let expires = "expires="+today.toUTCString();
    document.cookie = "delivery=" + JSON.stringify(cookieValue) + ";" + expires + ";path=/user/delivery";
}

function getCookie() {
    const name = "delivery=";
    let ca = document.cookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            let cookieValue = c.substring(name.length, c.length);
            try {
                cookieValue = JSON.parse(cookieValue ? cookieValue : "{}");
            } catch (e) {
                document.cookie = name + ";  expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                return {};
            }
            return cookieValue;
        }
    }
    return {};
}