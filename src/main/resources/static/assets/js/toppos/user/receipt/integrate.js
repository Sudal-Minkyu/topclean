$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseInspectionSave: {
            fdId: "nr",
            fiAddAmt: "nr",
            fiComment: "s",
            fiType: "sr",
            source: "", // 검품사진파일
        },
        
        franchiseInspectionList: { // fdId가 일치하는 모든 검품 리스트 type 1: 검품등록시, 2: 검품확인시 
            fdId: "nr",
            type: "s",
        },

        franchiseInspectionDelete: {
            fiId: "nr",
            fiPhotoYn: "sr",
        },

        franchiseInspectionYn: {
            fiId: "nr",
            fiAddAmt: "n",
            type: "sr",
        },

        franchiseDetailCencelDataList: { // 결제 조회창 열릴 때
            frId: "nr",
        },

        franchiseRequestDetailCencel: { // 결제취소시
            fpId: "nr",
            type: "sr", // 1 = 취소, 2 = 적립금전환
        },

        franchiseReceiptCancel: {
            fdId: "nr",
        },

        franchiseLeadCancel: {
            fdId: "nr",
        },

        franchiseRequestDetailUpdate: {
            fdAgreeType: "s", // 2022.03.10 추가
            fdSignImage: "s", // 2022.03.10 추가
            bcName: "d",
            frYyyymmdd: "d",
            fdId: "n",
            frId: "n",
            frNo: "s",
            fdTag: "d",
            biItemcode: "s",
            fdPreState: "d",
            fdState: "d",
            fdS2Dt: "d",
            fdS3Dt: "d",
            fdS4Dt: "d",
            fdS5Dt: "d",
            fdS6Dt: "d",
            fdCancel: "d",
            fdCacelDt: "d",
            fdColor: "",
            fdPattern: "",
            fdPriceGrade: "",
            fdOriginAmt: "",
            fdNormalAmt: "",
            fdPollution: "",
            fdDiscountGrade: "",
            fdDiscountAmt: "",
            fdQty: "",
            fdRequestAmt: "",
            fdSpecialYn: "",
            fdTotAmt: "",
            fdRemark: "",
            fdEstimateDt: "d",
            fdRetryYn: "",
            fdUrgentYn: "",
            fdPressed: "",
            fdAdd1Amt: "",
            fdAdd1Remark: "",
            fdAdd2Amt: "d",
            fdAdd2Remark: "d",
            fdRepairAmt: "",
            fdRepairRemark: "",
            fdWhitening: "",
            fdPollutionLevel: "",
            fdPollutionLocFcn: "s",
            fdPollutionLocFcs: "s",
            fdPollutionLocFcb: "s",
            fdPollutionLocFlh: "s",
            fdPollutionLocFrh: "s",
            fdPollutionLocFlf: "s",
            fdPollutionLocFrf: "s",
            fdPollutionLocBcn: "s",
            fdPollutionLocBcs: "s",
            fdPollutionLocBcb: "s",
            fdPollutionLocBrh: "s",
            fdPollutionLocBlh: "s",
            fdPollutionLocBrf: "s",
            fdPollutionLocBlf: "s",
            fdWaterRepellent: "",
            fdStarch: "",
            _$uid: "d",
            sumName: "d",
            blueBtn1: "d",
            blueBtn2: "d",
            greenBtn1: "d",
            redBtn1: "d",
            redBtn2: "d",
            redBtn3: "d",
            totAddCost: "d",
            frRefType: "sr",
            frEstimateDate: "d",
            fpCancelYn: "s",
        },

        franchiseRequestDetailSearch: {
            bcId: "n", // 선택된 고객. 없을 경우 null
            searchTag: "s", // 택번호 검색문자
            filterCondition: "s", // "" 전체, "S1" 고객접수, "S2" 자사입고, "S3" 가맹점입고, "S4" 입고완료, "B" 확인품, "F" 가맹검품
            filterFromDt: "sr", // 시작 조회기간
            filterToDt: "sr", // 종료 조회기간
        },

        customerInfo: {
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소
            searchString: "sr", // 검색문자
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

        franchiseDetailCencelDataList: { // 현 가맹점의 코드와 이름, 그리고 접수결제 테이블의 결제요소들
            fdTag: "s",
            gridListData: {
                frCode: "",
                frName: "",
                fpId: "nr",
                fpType: "sr",
                fpAmt: "nr",
                fpCatIssuername: "s",
                fpCatApprovalno: "s",
                fpCatApprovaltime: "s",
                fpCatTotamount: "s",
                fpCatVatamount: "s",
                fpMonth: "",
                insertDt: "",
            },
        },

        franchiseRequestDetailSearch: { // 접수 세부 테이블의 거의 모든 요소와, 고객이름
            fdAgreeType: "s", // 2022.03.10 추가
            fdSignImage: "s", // 2022.03.10 추가
            frRefType: "sr",
            bcName: "s", // 고객의 이름
            frYyyymmdd: "s", // 접수일자
            fdId: "n",
            frId: "n",
            frNo: "s",
            fdTag: "s",
            fdPreState: "s",
            fdState: "s",
            biItemcode: "s",
            fdS2Dt: "s",
            fdS3Dt: "s",
            fdS4Dt: "s",
            fdS5Dt: "s",
            fdS6Dt: "s",
            fdCancel: "s",
            fdCacelDt: "s",
            fdColor: "s",
            fdPattern: "s",
            fdPriceGrade: "s",
            fdOriginAmt: "n",
            fdNormalAmt: "n",
            fdPollution: "n",
            fdDiscountGrade: "s",
            fdDiscountAmt: "n",
            fdQty: "n",
            fdRequestAmt: "n",
            fdSpecialYn: "s",
            fdUrgentYn: "s",
            fdTotAmt: "n",
            fdRemark: "s",
            fdEstimateDt: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdAdd2Amt: "n",
            fdAdd2Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "n",
            fdPollutionLevel: "n",
            fdPollutionLocFcn: "s",
            fdPollutionLocFcs: "s",
            fdPollutionLocFcb: "s",
            fdPollutionLocFlh: "s",
            fdPollutionLocFrh: "s",
            fdPollutionLocFlf: "s",
            fdPollutionLocFrf: "s",
            fdPollutionLocBcn: "s",
            fdPollutionLocBcs: "s",
            fdPollutionLocBcb: "s",
            fdPollutionLocBrh: "s",
            fdPollutionLocBlh: "s",
            fdPollutionLocBrf: "s",
            fdPollutionLocBlf: "s",
            fdWaterRepellent: "n",
            fdStarch: "n",
            fpCancelYn: "sr",
        },

        customerInfo: { // 접수 페이지의 고객 정보 가져오는 부분과 동일
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

        itemGroupAndPriceList: { // 접수페이지 시작때 호출되는 API와 같은 API, 이건 dto검증기를 다차원 검증 가능하도록 개량후 검증.
            addAmountData: {
                baSort: "n",
                baName: "s",
                baRemark: "",
            },
            addCostData: {
                bcChildRt: "nr",
                bcHighRt: "nr",
                bcPollution1: "nr",
                bcPollution2: "nr",
                bcPollution3: "nr",
                bcPollution4: "nr",
                bcPollution5: "nr",
                bcPremiumRt: "nr",
                bcPressed: "nr",
                bcStarch: "nr",
                bcVipDcRt: "nr",
                bcVvipDcRt: "nr",
                bcWaterRepellent: "nr",
                bcWhitening: "nr",
            },
            etcData: {
                frMultiscreenYn: "s", // 2022.03.10 추가
                fdTag: "s",
                frBusinessNo: "s",
                frCode: "s",
                frEstimateDate: "s",
                frName: "s",
                frRpreName: "s",
                frTelNo: "s",
            },
            repairListData: {
                baSort: "n",
                baName: "s",
                baRemark: "",
            },
            userItemGroupSListData: {
                bgItemGroupcode: "s",
                bsItemGroupcodeS: "s",
                bsName: "s",
            },
            userItemGroupSortData: {
                bgSort: "n",
                bgItemGroupcode: "s",
                bgName: "s",
                bgIconFilename: "s",
                bgFavoriteYn: "s",
            },
            userItemPriceSortData: {
                bfSort: "n",
                bgName: "s",
                biItemcode: "s",
                biName: "s",
                bsName: "s",
                price: "n",
            }
        },
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const comms = {
    getInitialData() {
        CommonUI.ajax("/api/user/itemGroupAndPriceList", "GET", false, function (req){
            initialData = req.sendData;
            dv.chk(initialData, dtos.receive.itemGroupAndPriceList, "initialData 받아오기");
            console.log(initialData);
            setPreDefinedKeywords();
        });
    },

    searchCustomer(params) {
        dv.chk(params, dtos.send.customerInfo, "고객검색조건 보내기");
        CommonUI.ajax(grids.s.url.read[1], "GET", params, function (res) {
            const items = res.sendData.gridListData;
            dv.chk(items, dtos.receive.customerInfo, "고객 리스트 받아오기");
            $("#searchCustomerType").val(0);
            $("#searchString").val("");
            if(items.length === 1) {
                wares.selectedCustomer = items[0];
                putCustomer(wares.selectedCustomer);
                delete initialData.etcData["frNo"];
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

    franchiseRequestDetailSearch(condition) {
        wares.currentCondition = condition;
        dv.chk(condition, dtos.send.franchiseRequestDetailSearch, "메인그리드 필터링 조건 보내기");
        CommonUI.ajax(grids.s.url.read[0], "GET", condition, function(res) {
            const gridData = res.sendData.gridListData;
            const cancelList = res.sendData.cencelList;
            const inspectListF = res.sendData.inspeotListF;
            const inspectListB = res.sendData.inspeotListB;
            gridData.forEach(item => {
                if(cancelList.includes(item.frId)) {
                    item.fpCancelYn = "N"
                }
                if(inspectListF.includes(item.fdId) && item.fdState === "S1") {
                    item.fdState = "F";
                }
                if(inspectListB.includes(item.fdId) && item.fdState === "S2") {
                    item.fdState = "B";
                }
            });


            dv.chk(gridData, dtos.receive.franchiseRequestDetailSearch, "메인그리드 조회 결과 리스트");
            grids.f.setData(0, gridData);
        });
    },

    saveModifiedOrder(data) {
        console.log(data);
        dv.chk(data, dtos.send.franchiseRequestDetailUpdate, "상품 수정내용 저장");
        CommonUI.ajax(grids.s.url.update[0], "MAPPER", data, function(res) {
            onCloseAddOrder();
            grids.f.updateCurrentModifyRequest();
            alertSuccess("상품 수정 내용이 반영되었습니다.");
        });
    },

    getPaymentList(frId) {
        const condition = {frId: frId};
        dv.chk(condition, dtos.send.franchiseDetailCencelDataList, "결제 리스트 받아오기");
        const url = "/api/user/franchiseDetailCencelDataList";
        CommonUI.ajax(url, "GET", condition, function(res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.franchiseDetailCencelDataList);
            collectedCancelPaymentProcess(data.fdTag);
            grids.f.setData(2, data.gridListData);
        });
    },

    cancelPayment(target) {
        dv.chk(target, dtos.send.franchiseRequestDetailCencel, "한 항목 결제 취소");
        const url = "/api/user/franchiseRequestDetailCencel";
        CommonUI.ajax(url, "PARAM", target, function(res) {
            if(target.type === "1") {
                alertSuccess("결제 취소를 완료하였습니다.");
            }else if(target.type === "2") {
                alertSuccess("적립금 전환을 완료하였습니다.");
            }
            comms.getPaymentList(currentRequest.frId);
        });
    },

    cancelOrder(fdId) {
        const condition = {fdId: fdId};
        dv.chk(condition, dtos.send.franchiseReceiptCancel, "접수 취소");
        CommonUI.ajax(grids.s.url.delete[0], "PARAM", condition, function(res) {
            AUIGrid.removeRowByRowId(grids.s.id[0], currentRequest._$uid);
            AUIGrid.removeSoftRows(grids.s.id[0]);
            alertSuccess("접수 취소를 완료하였습니다.");
        });
    },

    cancelDeliverState(fdId) { // 현재 접수취소와 같은 url을 사용중
        const condition = {fdId: fdId};
        dv.chk(condition, dtos.send.franchiseLeadCancel, "인도 취소");
        const url = "/api/user/franchiseLeadCancel";
        CommonUI.ajax(url, "PARAM", condition, function(res) {
            const tempVar = currentRequest.fdPreState;
            currentRequest.fdPreState = currentRequest.fdState;
            currentRequest.fdState = tempVar;
            AUIGrid.updateRowsById(grids.s.id[0], currentRequest);
            alertSuccess("인도 취소를 완료하였습니다.");
        });
    },

    putNewInspect(formData) {
        const testObj = Object.fromEntries(formData);
        testObj.fdId = parseInt(testObj.fdId);
        testObj.fiAddAmt = parseInt(testObj.fiAddAmt) | 0;
        
        dv.chk(testObj, dtos.send.franchiseInspectionSave, "검품 등록");

        CommonUI.ajax("/api/user/franchiseInspectionSave", "POST", formData, function (res) {
            const searchCondition = {
                fdId: testObj.fdId,
                type: "1"
            };
            currentRequest.fdState = "F";
            comms.getInspectionList(searchCondition);
            $("#fiComment").val("");
            $("#fiAddAmt").val("0");
            alertSuccess("검품내역이 저장되었습니다.");
        });
    },

    getInspectionList(condition) {
        dv.chk(condition, dtos.send.franchiseInspectionList, "등록된 검품조회 조건");
        let gridNum = 7;
        if(condition.type === "1") {
            gridNum = 3;
        }else if(condition.type ==="0") {
            gridNum = 4;
        }

        CommonUI.ajax(grids.s.url.read[gridNum], "GET", condition, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.franchiseInspectionList, "등록된 검품의 조회");
            grids.f.clearData(gridNum);
            grids.f.setData(gridNum, data);
        });
    },

    deleteInspection(targets, fdId) {
        dv.chk(targets, dtos.send.franchiseInspectionDelete, "검품 삭제 목록");
        const data = {list: targets};
        const url = "/api/user/franchiseInspectionDelete";
        CommonUI.ajax(url, "MAPPER", data, function(res) {
            const searchCondition = {
                fdId: fdId,
                type: "1"
            };
            comms.getInspectionList(searchCondition);
        });
    },

    franchiseInspectionYn(target) {
        dv.chk(target, dtos.send.franchiseInspectionYn, "고객 수락 거부 응답 보내기");
        const url = "/api/user/franchiseInspectionYn";
        console.log(target);
        CommonUI.ajax(url, "PARAM", target, function(res) {
            const searchCondition = {
                fdId: currentRequest.fdId,
                type: "0",
            }
            comms.getInspectionList(searchCondition);
            const cautionText = ["고객 수락 승인 완료되었습니다.", " 고객 수락 거부 완료되었습니다."];
            alertSuccess(cautionText[target.type - 2]);
        });
    },
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
* */
const grids = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_main", "grid_customerList", "grid_paymentList", "grid_putInspect", "grid_confirmInspect"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/", "/api/", "/api/", "/api/", "/api/"
            ],
            read: [
                "/api/user/franchiseRequestDetailSearch",
                "/api/user/customerInfo",
                "/api/user/",
                "/api/user/franchiseInspectionList",
                "/api/user/franchiseInspectionList"
            ],
            update: [
                "/api/user/franchiseRequestDetailUpdate", "/api/", "/api/", "/api/", "/api/"
            ],
            delete: [
                "/api/user/franchiseReceiptCancel", "/api/", "/api/", "/api/", "/api/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
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
                    dataField: "fdS6Dt",
                    headerText: "인도일자",
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
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}"></span>`;
                        CommonUI.toppos.makeProductName(item, initialData.userItemPriceSortData);
                        return colorSquare + ` <span>` + item.sumName + `</span>`;
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
                }, {
                    dataField: "blueBtn1",
                    headerText: "검품등록",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(["S1", "F"].includes(item.fdState)) {
                            template = `
                                <button class="c-state">등록</button>
                            `;
                            item.blueBtn1 = true;
                        }else{
                            item.blueBtn1 = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "blueBtn2",
                    headerText: "검품확인",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(["F", "B"].includes(item.fdState)) {
                            template = `
                                <button class="c-state">확인</button>
                            `;
                            item.blueBtn2 = true;
                        }else{
                            item.blueBtn2 = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "greenBtn1",
                    headerText: "상품수정",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(["S1", "F"].includes(item.fdState)) {
                            template = `
                                <button class="c-state c-state--modify">수정</button>
                            `;
                            item.greenBtn1 = true;
                        }else{
                            item.greenBtn1 = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "redBtn1",
                    headerText: "접수취소",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(item.fpCancelYn === "Y" && ["S1", "F", "B"].includes(item.fdState)) {
                            template = `
                                <button class="c-state c-state--cancel">취소</button>
                            `;
                            item.redBtn1 = true;
                        }else{
                            item.redBtn1 = false;
                        } 
                        return template;
                    },
                }, {
                    dataField: "redBtn2",
                    headerText: "결제취소",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(item.fpCancelYn === "N") {
                            template = `
                                <button class="c-state c-state--cancel">취소</button>
                            `;
                            item.redBtn2 = true;
                        }else{
                            item.redBtn2 = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "redBtn3",
                    headerText: "인도취소",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(item.fdState === "S6") {
                            template = `
                                <button class="c-state c-state--cancel">취소</button>
                            `;
                            item.redBtn3 = true;
                        }else{
                            item.redBtn3 = false;
                        }
                        return template;
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
                    headerText: "승인일자",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fpAmt",
                    headerText: "승인금액",
                    style: "grid_textalign_right",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fpType",
                    headerText: "결제방식",
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let resultText = "";
                        if(item.fpCatIssuername) {
                            resultText = item.fpCatIssuername;
                        }else{
                            resultText = CommonData.name.fpType[value];
                        }
                        return resultText;
                    }
                },
            ];

            grids.s.prop[2] = {
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

            grids.s.columnLayout[3] = [
                {
                    dataField: "insertDt",
                    headerText: "등록일시",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fiComment",
                    headerText: "검품내용",
                    style: "grid_textalign_left",
                }, {
                    dataField: "fiSendMsgYn",
                    headerText: "메세지",
                    styleFunction: ynStyle,
                }, {
                    dataField: "fiPhotoYn",
                    headerText: "이미지",
                    styleFunction: ynStyle,
                },
            ];

            grids.s.prop[3] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[4] = [
                {
                    dataField: "insertDt",
                    headerText: "등록일시",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fiType",
                    headerText: "유형",
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        return CommonData.name.fiType[value];
                    },
                }, {
                    dataField: "fiComment",
                    headerText: "검품내용",
                    style: "grid_textalign_left",
                }, {
                    dataField: "fiSendMsgYn",
                    headerText: "메시지",
                    styleFunction: ynStyle,
                }, {
                    dataField: "fiPhotoYn",
                    headerText: "이미지",
                    styleFunction: ynStyle,
                }, {
                    dataField: "fiCustomerConfirm",
                    headerText: "고객수락",
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        return CommonData.name.fiCustomerConfirm[value];
                    },
                },
            ];

            grids.s.prop[4] = {
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

        setData(numOfGrid, data) {
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clearData(numOfGrid) {
            AUIGrid.clearGridData(numOfGrid);
        },

        switchModifyMode(isModifyMode) {
            const modColumn = {
                modify: ["blueBtn1", "blueBtn2", "greenBtn1", "redBtn1", "redBtn2", "redBtn3"],
                normal: ["type", "fdS5Dt", "fdRemark", "fdS2Dt", "fdS3Dt", "fdS4Dt"],
            }

            if(isModifyMode) {
                AUIGrid.showColumnByDataField(grids.s.id[0], modColumn.modify);
                AUIGrid.hideColumnByDataField(grids.s.id[0], modColumn.normal);
            }else{
                AUIGrid.showColumnByDataField(grids.s.id[0], modColumn.normal);
                AUIGrid.hideColumnByDataField(grids.s.id[0], modColumn.modify);
            }
        },

        getSelectedCustomer() {
            wares.selectedCustomer = AUIGrid.getSelectedRows(grids.s.id[1])[0];
        },

        getSelectedItem(gridNum) {
            return AUIGrid.getSelectedRows(grids.s.id[gridNum])[0];
        },

        clearGrid(numOfGrid) {
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(numOfGrid) {
            AUIGrid.resize(grids.s.id[numOfGrid]);
        },

        getRemovedCheckRows(numOfGrid) {
            if(AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]).length){
                AUIGrid.removeCheckedRows(grids.s.id[numOfGrid]);
            }else{
                AUIGrid.removeRow(grids.s.id[numOfGrid], "selectedIndex");
            }
            return AUIGrid.getRemovedItems(grids.s.id[numOfGrid]);
        },

        resetChangedStatus(numOfGrid) {
            AUIGrid.resetUpdatedItems(grids.s.id[numOfGrid]);
        },

        updateCurrentModifyRequest() {
            comms.franchiseRequestDetailSearch(wares.currentCondition);
        },

    },
};

/* dto가 아닌 일반적인 데이터들 정의 */
const wares = {
    url: window.location.href,
    params: "", // url에 내포한 파라메터들을 담는다.
    currentCondition: {},
    selectedCustomer: {
        bcId: null,
    },
    selectedLaundry: {},
    startPrice: 0,
    cameraStream: null,
    isCameraExist: false,
    keypadNum: 0,
}

/* 모듈 사용을 위해 data 밖에 선언된 변수들 */
let currentRequest = {};
let initialData = {};


/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다.*/
const trigs = {
    gridEvent() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
            currentRequest = e.item;
            switch (e.dataField) {
                case "blueBtn1":
                    // 가맹점 검품등록창 진입
                    if(e.item.blueBtn1) {
                        openPutInspectPop(e);
                    }
                    break;
                case "blueBtn2":
                    if(e.item.blueBtn2) {
                        confirmInspect(e);
                    }
                    // 검품확인창 진입
                    break;
                case "greenBtn1":
                    // 수정모드 진입
                    if(e.item.greenBtn1) {
                        modifyOrder(e.rowIndex);
                    }
                    break;
                case "redBtn1":
                    // 확인후 ajax 호출하며 그리드에서 삭제
                    if(e.item.redBtn1) {
                        alertCheck("선택한 품목을 접수취소하시겠습니까?" 
                            + "<br>접수취소 후에는 신규접수를 통해<br>재등록 가능합니다.");
                        $("#checkDelSuccessBtn").on("click", function() {
                            comms.cancelOrder(e.item.fdId);
                            $("#popupId").remove();
                        });
                    }
                    break;
                case "redBtn2":
                    if(e.item.redBtn2) {
                        cancelPaymentPop(e.item.frId);
                    }
                    // 결제취소창 진입
                    break;
                case "redBtn3":
                    // 확인후 가맹점 입고로 상태변경하여 ajax 호출
                    if(e.item.redBtn3) {
                        alertCheck("선택한 품목을 인도취소하시겠습니까?");
                        $("#checkDelSuccessBtn").on("click", function() {
                            comms.cancelDeliverState(e.item.fdId);
                            $("#popupId").remove();
                        });
                    }
                    break;
            }
        });

        AUIGrid.bind(grids.s.id[4], "cellClick", function (e) {
            $("#fiAddAmtInConfirm").val(e.item.fiAddAmt.toLocaleString());
            $("#fiCommentInConfirm").val(e.item.fiComment);
            if(e.item.fiPhotoYn === "Y") {
                $("#imgThumb").attr("src", e.item.ffPath + "s_" + e.item.ffFilename);
                $("#imgFull").attr("href", e.item.ffPath + e.item.ffFilename);
                $("#imgFull").show();
            }else{
                $("#imgFull").hide();
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
        $("#vkeyboard2").on("click", function() {
            onShowVKeyboard(2);
        });
        $("#vkeyboard3").on("click", function() {
            onShowVKeyboard(3);
        });
        $("#vkeyboard4").on("click", function() {
            onShowVKeyboard(4);
        });
        $("#vkeypad0").on("click", function () {
            wares.keypadNum = 0;
            vkey.showKeypad("fiAddAmt", {callback: onKeypadConfirm});
        });
    },
    main() {
        $("#searchCustomer").on("click", function() {
            mainSearch();
        });

        $("#selectCustomer").on("click", function() {
            grids.f.getSelectedCustomer();
            selectCustomerFromList();
        });

        $("#resetCustomer").on("click", function() {
            wares.selectedCustomer = {
                bcId: null,
                beforeUncollectMoney: 0,
                saveMoney: 0,
                bcAddress: "",
                bcRemark: "",
            };
            putCustomer();
        });

        $("#filter").on("click", function() {
            filterMain();
        });

        $("#modifyOn").on("click", function() {
            grids.f.switchModifyMode(true);
            $("#modifyOff").show();
            $("#modifyOn").hide();
        });

        $("#modifyOff").on("click", function() {
            grids.f.switchModifyMode(false);
            $("#modifyOn").show();
            $("#modifyOff").hide();
        });

        $("#searchString").on("keypress", function(e) {
            if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                searchCustomer();
            }
        });

        $("#printReceipt").on("click", function() {
            const selectedItem = grids.f.getSelectedItem(0);
            if(selectedItem) {
                alertCheck("영수증을 인쇄 하시겠습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    printReceipt(selectedItem.frNo);
                    $('#popupId').remove();
                });
            } else {
                alertCaution("영수증을 출력할 상품을 선택해 주세요.", 1);
            }
        });
    },
    modify() {
        $("#inReceiptPop input[type='radio']").on("change", function(){
            calculateItemPrice();
        });

        $("#fdRepair").on("click", function () {
            $("#fdRepairPop").addClass("active");
            enableKeypad();
        });

        // 접수 세부 우측 하단 각종 처리 버튼의 온오프에 따른 없음버튼 온오프
        const $processInput = $("#processCheck input[type='checkbox'], #processCheck input[type='radio']");
        $processInput.on("change", function(e) {
            if($("#waterNone").is(":checked")) {
                $("#waterBtn").removeClass("choice-drop__btn--active");
            }else{
                $("#waterBtn").addClass("choice-drop__btn--active");
            }
            const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
            if(!$("#processCheck input[type='checkbox']:checked").length && !$isEtcProcessChecked) {
                $processInput.first().prop("checked", true);
            }else if($processInput.first().is(":checked") || $isEtcProcessChecked) {
                $processInput.first().prop("checked", false);
            }

            calculateItemPrice();
        });

        // 긴급항목 버튼의 온오프에 따른 없음버튼 온오프
        const $urgentInput = $("#urgentCheck input[type='checkbox']");
        $urgentInput.on("change", function(e) {
            if(!$("#urgentCheck input[type='checkbox']:checked").length) {
                $urgentInput.first().prop("checked", true);
            }else{
                $urgentInput.first().prop("checked", false);
            }
        });

        $("#etcNone").on("click", function () {
            $processInput.prop("checked", false);
            $("#waterNone").prop("checked", true);
            $("#waterBtn").removeClass("choice-drop__btn--active");
            resetPollutionPop();
            resetFdRepairInputs();
            resetFdAddInputs();
            // 각 내부 항목들의 초기화
            setTimeout(function () {
                calculateItemPrice();
            }, 0);
        });
    
        $("#urgentNone").on("click", function () {
            $urgentInput.prop("checked", false);
            calculateItemPrice();
        });

        $("#fdAdd1").on("click", function () {
            $("#fdAddPop").addClass("active");
            enableKeypad();
        });

        $("#fdPollution").on("click", function () {
            $("#foreLoc").trigger("click");
            $("#fdPollutionPop").addClass("active");
        });

        $("#fdRepairCancel").on("click", function () {
            resetFdRepairInputs();
            calculateItemPrice();
            disableKeypad();
        });

        $("#fdRepairComplete").on("click", function () {
            currentRequest.fdRepairAmt = $("#fdRepairAmt").val().toInt();
            currentRequest.fdRepairRemark = $("#fdRepairRemark").val();
            if(currentRequest.fdRepairAmt || currentRequest.fdRepairRemark.length) {
                $("#fdRepair").prop("checked", true);
            }else{
                $("#fdRepair").prop("checked", false);
            }
            calculateItemPrice();
            disableKeypad();
        });

        $("#fdAddCancel").on("click", function () {
            calculateItemPrice();
            disableKeypad();
        });

        $("#fdAddComplete").on("click", function () {
            currentRequest.fdAdd1Amt = $("#fdAdd1Amt").val().toInt();
            currentRequest.fdAdd1Remark = $("#fdAdd1Remark").val();
            if(currentRequest.fdAdd1Amt || currentRequest.fdAdd1Remark.length) {
                $("#fdAdd1").prop("checked", true);
            }else{
                $("#fdAdd1").prop("checked", false);
            }
            calculateItemPrice();
            disableKeypad();
        });

        $('.choice-drop__btn').on('click', function(e) {
            $(".choice-drop__content--active").removeClass("choice-drop__content--active");
            $(this).next('.choice-drop__content').toggleClass('choice-drop__content--active');
        });

        $('.choice-drop__item').on('change', function(e) {
            $(this).parents('.choice-drop__content').removeClass('choice-drop__content--active');
            if(this.className === "choice-drop__item closDrop"){
                $(this).parents('.choice-drop').children('.choice-drop__btn').removeClass('choice-drop__btn--active');
            }else{
                $(this).parents('.choice-drop').children('.choice-drop__btn').addClass('choice-drop__btn--active');
            }
        });
    },
    subMenu() {
        $("#commitInspect").on("click", function() {
            putInspect();
        });

        $("#closePutInspectPop").on("click", function () {
            AUIGrid.updateRowsById(grids.s.id[0], currentRequest);
            $("#putInspectPop").removeClass("active");
            if(wares.isCameraExist) {
                try {
                    wares.cameraStream.getTracks().forEach(function (track) {
                        track.stop();
                    });
                }catch (e) {
                    CommonUI.toppos.underTaker(e, "integrate : 카메라 스트림 트랙 감지하여 취소");
                }
            }
        });

        $("#transferPoint").on("click", function () {
            alertCheck("해당 내역을 적립금으로 전환하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                cancelPayment("2");
            });
        });

        $("#refundPayment").on("click", function () {
            alertCheck("해당 내역을 환불 하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                cancelPayment("1");
            });
        });

        $("#fiAddAmt").on("keyup", function () {
            $(this).val($(this).val().toInt().toLocaleString());
        });
        
        $("#deleteInspection").on("click", function () { // 3번테이블(검품등록) 의 삭제될 대상들을 가져와서 삭제 요청
            const targetRowsItem = grids.f.getRemovedCheckRows(3);
            let refinedTargetList = [];
            let isAlreadyDone = false;
            targetRowsItem.forEach(item => {
                if(item.fiSendMsgYn === "Y" || item.fiCustomerConfirm !== "1") {
                    isAlreadyDone = item.fiComment;

                }
                refinedTargetList.push({
                    fiId: item.fiId,
                    fiPhotoYn: item.fiPhotoYn,
                });
            });
            if(isAlreadyDone) {
                alertCaution("고객에게 정보가 전해진 검품내역은<br>삭제할 수 없습니다.<br>( 검품내용 : " 
                + isAlreadyDone + " )", 1);
                grids.f.resetChangedStatus(3);
                return false;
            }

            if(targetRowsItem.length) {
                comms.deleteInspection(refinedTargetList, targetRowsItem[0].fdId);
                grids.f.resetChangedStatus(3);
            }
        });

        $("#customerConfirmed").on("click", function () {
            if(grids.f.getSelectedItem(4)) {
                alertCheck("고객께서 진행 수락 하셨습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    customerJudgement("2");
                });
            }else{
                alertCaution("검품확인내역을 선택하세요.", 1);
            }
        });

        $("#customerDenied").on("click", function () {
            if(grids.f.getSelectedItem(4)) {
                alertCheck("고객께서 진행 거부 하셨습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    customerJudgement("3");
                });
            }else{
                alertCaution("검품확인내역을 선택하세요.", 1);
            }
        });

        $("#closePaymentPop").on("click", function () {
            if(!AUIGrid.getGridData(grids.s.id[2]).length) { // 결제내역 전부 사라질 경우 해당 마스터테이블 결제취소 버튼 제거, 접수취소 버튼 일괄 부여
                const frId = currentRequest.frId;
                const gridData = AUIGrid.getGridData(grids.s.id[0]);
                gridData.forEach(item => {
                    if(item.frId === frId) {
                        item.fpCancelYn = "Y"
                        AUIGrid.updateRowsById(grids.s.id[0], item);
                    }
                });
                //AUIGrid.refresh(grids.s.id[0]);
            }
            $("#paymentListPop").removeClass("active");
        });

        $("#closeConfirmInspectPop").on("click", function () {
            const items = AUIGrid.getGridData(grids.s.id[4]);
            let isInspectionDone = true;
            let lastFdState = "S1"; 
            items.forEach(item => {
                if(item.fiType === "B") {
                    lastFdState = "S2";
                }
                if(item.fiCustomerConfirm === "1") {
                    isInspectionDone = false;
                }
            });
            if(isInspectionDone) {
                currentRequest.fdState = lastFdState;
                AUIGrid.updateRowsById(grids.s.id[0], currentRequest);
            }
            $("#confirmInspectPop").removeClass("active");
        });

        $("#foreLoc").on("click", function() {
            $(".foreLoc").show();
            $(".pop__pollution-content").removeClass("back");
            $(".backLoc").hide();
        });
    
        $("#backLoc").on("click", function() {
            $(".backLoc").show();
            $(".pop__pollution-content").addClass("back");
            $(".foreLoc").hide();
        });
    },
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    comms.getInitialData();
    enableDatepicker();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    grids.f.initialization();
    grids.f.create();
    grids.f.switchModifyMode(false);
    trigs.gridEvent();

    trigs.main();
    trigs.vkeys();
    trigs.modify();
    trigs.subMenu();

    chkParams();


    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });

    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');
    })
}

function modifyOrder(rowIndex) {
    currentRequest.sumName = undefined;
    wares.startPrice = currentRequest.fdTotAmt;
    wares.selectedLaundry.bgCode = currentRequest.biItemcode.substr(0, 3);

    let bsType = ["N", "L", "S"];
    initialData.userItemGroupSListData.forEach(el => {
        for(let i = 0; i < bsType.length; i++) {
            if(el.bgItemGroupcode === wares.selectedLaundry.bgCode && el.bsItemGroupcodeS === bsType[i]) {
                $("#size" + bsType[i]).css("display", "block");
                $("#size" + bsType[i] +" label").html(el.bsName);
                delete bsType[i];
                break;
            }
        }
    });
    bsType.forEach(type => {
        $("#size" + type).css("display", "none");
    });

    setBiItemList(currentRequest.biItemcode.substr(3, 1));

    wares.selectedLaundry.bgCode = currentRequest.biItemcode.substr(0, 3);
    wares.selectedLaundry.bsCode = currentRequest.biItemcode.substr(3, 1);
    $("input[name='bsItemGroupcodeS']:input[value='" + wares.selectedLaundry.bsCode + "']").prop("checked", true);
    $("#" + currentRequest.biItemcode).prop("checked", true);
    $(".choice-color__input[value='" + currentRequest.fdColor + "']").prop("checked", true);
    $("input[name='fdPattern']:input[value='" + currentRequest.fdPattern +"']").prop("checked", true);
    $("input[name='fdPriceGrade']:input[value='" + currentRequest.fdPriceGrade +"']").prop("checked", true);
    $("input[name='fdDiscountGrade']:input[value='" + currentRequest.fdDiscountGrade +"']").prop("checked", true);

    if(currentRequest.fdPressed) {
        $("#fdPress").prop("checked", true);
    }
    if(currentRequest.fdRetryYn === "Y") {
        $("#fdRetry").prop("checked", true);
    }

    if(currentRequest.fdRepairRemark.length || currentRequest.fdRepairAmt) {
        $("#fdRepair").prop("checked", true);
        $("#fdRepairAmt").val(currentRequest.fdRepairAmt);
        $("#fdRepairRemark").val(currentRequest.fdRepairRemark);
    }

    if(currentRequest.fdAdd1Remark.length || currentRequest.fdAdd1Amt) {
        $("#fdAdd1").prop("checked", true);
        $("#fdAdd1Amt").val(currentRequest.fdAdd1Amt);
        $("#fdAdd1Remark").val(currentRequest.fdAdd1Remark);
    }

    if(currentRequest.fdSpecialYn === "Y") {
        $("#fdSpecialYn").prop("checked", true);
    }else{
        $("#fdSpecialYn").prop("checked", false);
    }

    if(currentRequest.fdUrgentYn === "Y") {
        $("#fdUrgentYn").prop("checked", true);
    }else{
        $("#fdUrgentYn").prop("checked", false);
    }

    if(currentRequest.fdWhitening) {
        $("#fdWhitening").prop("checked", true);
    }

    $("input[name='cleanDirt']:input[value='" + currentRequest.fdPollutionLevel +"']").prop("checked", true);
    if($("#pollution00").is(":checked")) {
        $("#fdPollution").prop("checked", false);
    }else{
        $("#fdPollution").prop("checked", true);
    }

    const pollutionLocKeys = [
        "fdPollutionLocFcn", "fdPollutionLocFcs", "fdPollutionLocFcb",
        "fdPollutionLocFlh", "fdPollutionLocFrh", "fdPollutionLocFlf", "fdPollutionLocFrf",
        "fdPollutionLocBcn", "fdPollutionLocBcs", "fdPollutionLocBcb",
        "fdPollutionLocBrh", "fdPollutionLocBlh", "fdPollutionLocBrf", "fdPollutionLocBlf",
    ];

    pollutionLocKeys.forEach(key => {
        if(currentRequest[key] === "Y") {
            $("#" + key).prop("checked", true);
        }
    });

    if(currentRequest.fdWaterRepellent) {
        $("#fdWaterRepellent").prop("checked", true);
    }
    if(currentRequest.fdStarch) {
        $("#fdStarch").prop("checked", true);
    }
    if($("#waterNone").is(":checked")) {
        $("#waterBtn").removeClass("choice-drop__btn--active");
    }else{
        $("#waterBtn").addClass("choice-drop__btn--active");
    }



    if(currentRequest.fdRemark.length) {
        $("#fdRemark").val(currentRequest.fdRemark);
    }

    if($("#processCheck input:checked").length > 2 || $("#processCheck .choice-drop__btn--active").length) {
        $("#etcNone").prop("checked", false);
    }

    if($("#urgentCheck input:checked").length > 1) {
        $("#urgentNone").prop("checked", false);
    }

    calculateItemPrice();
    $('#productPop').addClass('active');
    $("#biItemList").scrollTop(0);
}

function setBiItemList(bsCode) {
    wares.selectedLaundry.bsCode = bsCode;

    const $biItemList = $("#biItemList");
    $biItemList.html("");

    initialData.userItemPriceSortData.forEach(el => {
        if(el.biItemcode.substr(0,4) === wares.selectedLaundry.bgCode + bsCode) {
            $biItemList.append(`
                            <li>
                                <div class="choice choice--material">
                                    <input type="radio" name="material" id="${el.biItemcode}" 
                                            value="${el.price}" onclick="onSelectBiItem('${el.biItemcode}', ${el.price})" />
                                    <label for="${el.biItemcode}">
                                        <span class="choice__name">${el.biName}</span>
                                        <span class="choice__cost">${parseInt(el.price).toLocaleString()}</span><span>원</span>
                                    </label>
                                </div>
                            </li>
            `);
        }
    });
}

function onCloseAddOrder() {
    currentRequest = {};
    $("input[name='bsItemGroupcodeS']").first().prop("checked", true);
    $("input[name='fdColor']").first().prop("checked", true);
    $("input[name='fdPattern']").first().prop("checked", true);
    $("input[name='fdPriceGrade']").first().prop("checked", true);
    $("input[name='fdDiscountGrade']").first().prop("checked", true);

    $("input[name='material']").first().prop("checked", true);
    $(".choice input[type='checkbox']").prop("checked", false);
    $("input[name='cleanDirt']").first().prop("checked", true);
    $("input[name='waterProcess']").first().prop("checked", true);
    $(".choice-drop__btn.etcProcess").removeClass('choice-drop__btn--active');

    $(".keypad_remark").val("");
    $(".keypad_field").val(0);
    $("#foreLoc").prop("checked", true);
    $("input[name='pollutionLoc']").prop("checked", false);
    $("input[name='etcNone']").first().prop("checked", true);
    $("input[name='urgentNone']").first().prop("checked", true);
    $("#fdRemark").val("");

    $("#addProductPopChild").parents('.pop').removeClass('active');
}

function onAddOrder() {
    if(!currentRequest.biItemcode.length) {
        alertCaution("소재를 선택해 주세요", 1);
        return false;
    }

    if(currentRequest.redBtn2 && wares.startPrice > currentRequest.fdTotAmt) {
        alertCaution("이미 결제가 완료된 접수내역은<br>더 낮은금액으로 수정이 불가합니다." 
            + "<br>결제취소 후 수정해주세요.", 1);
        return false;
    }

    currentRequest.fdColor = $("input[name='fdColor']:checked").val();
    currentRequest.fdPattern = $("input[name='fdPattern']:checked").val();
    currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();
    currentRequest.fdRemark = $("#fdRemark").val();
    currentRequest.frEstimateDate = initialData.etcData.frEstimateDate.numString();
    currentRequest.fdSpecialYn = $("#fdSpecialYn").is(":checked") ? "Y" : "N";
    currentRequest.fdUrgentYn = $("#fdUrgentYn").is(":checked") ? "Y" : "N";

    const pollutionLoc = $("input[name='pollutionLoc']");
    for(let i = 0; i < pollutionLoc.length; i++) {
        if($(pollutionLoc[i]).is(":checked")) {
            currentRequest[pollutionLoc[i].id] = "Y";
        }else {
            currentRequest[pollutionLoc[i].id] = "N";
        }
    }
    
    comms.saveModifiedOrder(currentRequest);
}

function confirmPollutionPop() {

    const isLocChecked = $("input[name='pollutionLoc']:checked").length;
    const isSizeChecked = !$("#pollution00").is(":checked");

    if(isLocChecked && !isSizeChecked) {
        alertCaution("오염위치가 선택되어 있습니다.<br>오염크기도 선택해 주세요.", 1);
        return false;
    }
    if(!isLocChecked && isSizeChecked) {
        alertCaution("오염크기가 선택되어 있습니다.<br>오염위치도 선택해 주세요.", 1);
        return false;
    }

    if($("#pollution00").is(":checked") && $("#fdPollution").is(":checked")) {
        $("#fdPollution").trigger("click");
    }else if(!$("#pollution00").is(":checked") && !$("#fdPollution").is(":checked")){
        $("#fdPollution").trigger("click");
    }
    $("#fdPollutionPop").removeClass("active");

    calculateItemPrice();
}

function resetPollutionPop() {
    $("#pollution00").prop("checked", true);
    $("input[name='pollutionLoc']").prop("checked", false);
    if($("#fdPollution").is(":checked")) {
        $("#fdPollution").trigger("click");
    }
    $("#fdPollutionPop").removeClass("active");

    calculateItemPrice();
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

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["searchString", "fdRemark", "fdRepairRemark", "fdAdd1Remark", "fiComment"];

    vkeyProp[0] = {
        title: $("#searchType option:selected").html() + " (검색)",
        callback: mainSearch,
    };

    vkeyProp[1] = {
        title: "브랜드, 특이사항 입력",
    };

    vkeyProp[2] = {
        title: "수선 리스트 입력",
    };

    vkeyProp[3] = {
        title: "추가요금 리스트 입력",
    };

    vkeyProp[4] = {
        title: "검품내용 입력",
    };

    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function mainSearch() {
    if($("#searchType").val() === "4") {
        filterMain();
    }else{
        searchCustomer();
    }
}

function searchCustomer() {
    const $searchString = $("#searchString");
    if($searchString.val() === "") {
        alertCaution("검색어를 입력해주세요.",1);
        return false;
    }
    const params = {
        searchType : $("#searchType").val(),
        searchString : $searchString.val()
    };

    comms.searchCustomer(params);
}

function selectCustomerFromList() {
    if(wares.selectedCustomer) {
        putCustomer();
        delete initialData.etcData["frNo"];
        $("#customerListPop").removeClass("active");
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function filterMain() {
    const condition = CommonUI.newDto(dtos.send.franchiseRequestDetailSearch);
    condition.bcId = wares.selectedCustomer.bcId;
    condition.filterCondition = $("input[name='filterCondition']:checked").val();
    condition.filterToDt = $("#filterFromDt").val().numString();
    condition.filterFromDt = $("#filterToDt").val().numString();
    if($("#searchType").val() === "4") {
        condition.searchTag = $("#searchString").val();
    }
    comms.franchiseRequestDetailSearch(condition);
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

    // 상품수정기능 도입까지 주석
    // $("#class02, #class03").parents("li").css("display", "none");
    // $("#class" + wares.selectedCustomer.bcGrade).parents("li").css("display", "block");
    grids.f.clearGrid(0);
}

function onSelectBiItem(biCode, price) {
    currentRequest.biItemcode = biCode;
    currentRequest.fdOriginAmt = price;
    calculateItemPrice();
}

function enableKeypad() {
    const $keypadBtn = $(".add-cost .keypad_btn");
    const $keypadBackspace = $(".add-cost .keypad_btn_backspace");
    const $keypadBoilerplate = $(".add-cost .add-cost__example-btn");

    $keypadBtn.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost__keypad").find(".keypad_field");
        $keypad_field.val(parseInt($keypad_field.val().numString() + this.value).toLocaleString());
    });

    $keypadBackspace.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost__keypad").find(".keypad_field");
        const currentValue = $keypad_field.val().numString();
        if(currentValue.length > 1) {
            $keypad_field.val(parseInt(currentValue.substr(0,
                currentValue.numString().length - 1)).toLocaleString());
        }else{
            $keypad_field.val("0");
        }
    });

    $keypadBoilerplate.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost").find(".keypad_remark");
        $keypad_field.val($keypad_field.val() + this.innerHTML);
    });
}

function disableKeypad() {
    const $keypadBtn = $(".add-cost .keypad_btn");
    const $keypadBackspace = $(".add-cost .keypad_btn_backspace");
    const $keypadBoilerplate = $(".add-cost .add-cost__example-btn");

    for(let i = 0; i < $keypadBtn.length; i++) {
        removeEventsFromElement($keypadBtn[i]);
    }

    for(let i = 0; i < $keypadBackspace.length; i++) {
        removeEventsFromElement($keypadBackspace[i]);
    }

    for(let i = 0; i < $keypadBoilerplate.length; i++) {
        removeEventsFromElement($keypadBoilerplate[i]);
    }
}

function removeEventsFromElement(element) {
    const elementClone = element.cloneNode(true);
    element.parentNode.replaceChild(elementClone, element);
}

function confirmInspect(e) {
    $("#fiCommentInConfirm").val("");
    $("#fiAddAmtInConfirm").val("");
    $("#imgFull").hide();
    $("#fdRequestAmtInConfirm").val(currentRequest.fdRequestAmt.toLocaleString());

    $("#confirmInspectPop").addClass("active");
    grids.f.resize(4);
    const searchCondition = {
        fdId: e.item.fdId,
        type: "0"
    }
    comms.getInspectionList(searchCondition);
}

function cancelPaymentPop(frId) {
    comms.getPaymentList(frId);
    $("#paymentListPop").addClass("active");
    grids.f.resize(2);
}

function cancelPayment(cancelType) {
    const item = grids.f.getSelectedItem(2);
    if(item) {
        const target = {
            fpId: item.fpId,
            type: cancelType
        }
        if(cancelType === "1" && item.fpType === "02") {
            try {
                const params = {
                    approvalTime: item.fpCatApprovaltime,
                    approvalNo: item.fpCatApprovalno,
                    totalAmount: item.fpCatTotamount,
                    vatAmount: item.fpCatVatamount,
                    franchiseNo: item.frCode
                };
                
                CAT.CatCreditCancel(params, function(res) {
                    const jsonRes = JSON.parse(res);
                    if(jsonRes.STATUS === "SUCCESS") {
                        comms.cancelPayment(target);
                    }else if(jsonRes.STATUS === "FAILURE") {
                        console.log(res);
                        alertCancel("카드결제 취소중 에러 발생<br>단말기 처리");
                        return false;
                    }
                });
            } catch (e) {
                CommonUI.toppos.underTaker(e, "integrate : 결제취소");
                return false;
            }
        }else{
            comms.cancelPayment(target);
        }
    }else{
        alertCaution("결제내역을 선택해주세요.", 1);
    }
}


/* 웹 카메라와 촬영 작업중 */
async function openPutInspectPop(e) {
    try {
        wares.isCameraExist = true;
        // const cameraList = document.getElementById("cameraList"); 복수 카메라를 사용할 경우 해제하여 작업
        wares.cameraStream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: {
                width: {ideal: 4096},
                height: {ideal: 2160}
            },
        });

        const screen = document.getElementById("cameraScreen");
        screen.srcObject = wares.cameraStream;
    }catch (e) {
        if(!(e instanceof DOMException)) {
            CommonUI.toppos.underTaker(e, "integrate : 카메라 감지");
        }
        wares.isCameraExist = false;
    }

    $("#fiComment").val("");
    $("#fiAddAmt").val("0");
    $("#fdRequestAmtInPut").val(currentRequest.fdRequestAmt.toLocaleString());
    if(wares.isCameraExist) {
        $("#isIncludeImg").prop("checked", true);
        $("#isIncludeImg").prop("disabled", false);
    }else{
        $("#isIncludeImg").prop("checked", false);
        $("#isIncludeImg").prop("disabled", true);
    }
    $("#putInspectPop").addClass("active");
    grids.f.resize(3);
    const searchCondition = {
        fdId: e.item.fdId,
        type: "1"
    }
    comms.getInspectionList(searchCondition);
}

/* 고객의 응답 승낙, 거부에 따른 처리 */
function customerJudgement(typeAnswer) {
    const selectedItem = grids.f.getSelectedItem(4);
    const target = {
        fiId: selectedItem.fiId,
        fiAddAmt: selectedItem.fiAddAmt,
        type: typeAnswer,
    };
    comms.franchiseInspectionYn(target);
}

function ynStyle(rowIndex, columnIndex, value, headerText, item, dataField) {
    return value === "Y" ? "yesBlue" : "noRed"
}

function putInspect() {
    const $fiComment = $("#fiComment");
    if (!$fiComment.val().length) {
        alertCaution("검품내용을 입력해 주세요", 1);
        return false;
    }
    if(wares.isCameraExist && wares.cameraStream.active) {
        try {
            const formData = new FormData();
            formData.append("fdId", currentRequest.fdId);

            if($("#isIncludeImg").is(":checked")) {
                const video = document.getElementById("cameraScreen");
                const canvas = document.getElementById('cameraCanvas');
                canvas.width = video.videoWidth;
                canvas.height = video.videoHeight;
                const context = canvas.getContext('2d');
                context.drawImage(video, 0, 0, canvas.width, canvas.height);
    
                const takenPic = canvas.toDataURL();
    
                const blob = b64toBlob(takenPic);
                formData.append("source", blob);
            }else{
                formData.append("source", null);
            }

            formData.append("fiComment", $fiComment.val());
            formData.append("fiType", "F");
            formData.append("fiAddAmt", $("#fiAddAmt").val().numString());

            comms.putNewInspect(formData);

        } catch (e) {
            CommonUI.toppos.underTaker(e, "integrate : 검품등록");
        }
    }else{
        const formData = new FormData();
        formData.append("fdId", currentRequest.fdId);
        formData.append("fiComment", $fiComment.val());
        formData.append("fiType", "F");
        formData.append("fiAddAmt", $("#fiAddAmt").val().numString());
        formData.append("source", null);
        comms.putNewInspect(formData);
    }
}

function b64toBlob(dataURI) { // 파일을 ajax 통신에 쓰기 위해 변환
    const byteString = atob(dataURI.split(',')[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: 'image/jpeg' });
}

function onKeypadConfirm() {
    const targetId = ["fiAddAmt"];
    $("#" + targetId[wares.keypadNum]).val(parseInt($("#" + targetId[wares.keypadNum]).val()).toLocaleString());
}

function printReceipt(frNo) {
    const condition = {
        frNo: frNo,
        frId: "",
    }

    CommonUI.toppos.printReceipt(condition);
}

/* 넘어온 fdTag값이 있다면 해당 정보를 통해 검색한다. */
function chkParams() {
    const url = new URL(wares.url);
    wares.params = url.searchParams;

    if(wares.params.has("fdTag") && wares.params.has("frYyyymmdd")) {
        const dateNum = wares.params.get("frYyyymmdd");
        const date = dateNum.substring(0, 4) + "-" + dateNum.substring(4, 6) + "-" + dateNum.substring(6, 8);
        $("#searchType").val("4");
        $("#searchString").val(wares.params.get("fdTag"));
        $("#filterFromDt").val(date);
        $("#filterToDt").val(date);
        $("#searchCustomer").trigger("click");
    }
}

/* 추가요금과 수선항목의 상용구 버튼 나열 */
function setPreDefinedKeywords() {
    const $addCostBoilerList = $("#addCostBoilerList");
    const $repairBoilerList = $("#repairBoilerList");

    for(const [i, obj] of initialData.addAmountData.entries()) {
        $addCostBoilerList.append(`
            <li class="add-cost__example-item">
                <button class="add-cost__example-btn">${obj.baName}</button>
            </li>
        `);
        if(i === 11) break;
    }

    for(const [i, obj] of initialData.repairListData.entries()) {
        $repairBoilerList.append(`
            <li class="add-cost__example-item">
                <button class="add-cost__example-btn">${obj.baName}</button>
            </li>
        `);
        if(i === 11) break;
    }
}

/* 해당 결제건이 미수금 완납과 연관되어 있는 경우 취소를 막기 위함 */
function collectedCancelPaymentProcess(tagNo) {
    const $tranferPoint = $("#transferPoint");
    const $refundPayment = $("#refundPayment");
    const $collectedWarning = $("#collectedWarning");
    const $collectedTagNo = $(".collectedTagNo");
    console.log(tagNo);
    if(tagNo) {
        $tranferPoint.hide();
        $refundPayment.hide();
        $collectedTagNo.html(tagNo.substring(0, 3) + "-" + tagNo.substring(3,7));
        $collectedWarning.show();
    } else {
        $tranferPoint.show();
        $refundPayment.show();
        $collectedWarning.hide();
    }
}

function resetFdRepairInputs() {
    currentRequest.fdRepairAmt = 0;
    currentRequest.fdRepairRemark = "";
    $("#fdRepair").prop("checked", false);
    $("#fdRepairAmt").val(0);
    $("#fdRepairRemark").val("");
}

function resetFdAddInputs() {
    currentRequest.fdAdd1Amt = 0;
    currentRequest.fdAdd1Remark = "";
    $("#fdAdd1").prop("checked", false);
    $("#fdAdd1Amt").val(0);
    $("#fdAdd1Remark").val("");
}