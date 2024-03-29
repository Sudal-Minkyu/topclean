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
        franchiseInspectionMessageSend: {
            fiId: "nr",
            isIncludeImg: "s",
            fmMessage: "s",
            bcId: "n",
        },

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
            bcId: "n", // 고객 아이디
            fpId: "nr",
            type: "sr", // 1 = 취소, 2 = 적립금전환
        },

        franchiseReceiptCancel: {
            fdId: "nr",
            bcId: "n",
        },

        franchiseLeadCancel: {
            fdId: "nr",
        },

        franchiseRequestDetailUpdate: {
            fpId: "n", // 미수금의 결제를 다른 세탁물이 대신한 경우 해당 결제 id
            bcName: "s", // 고객명
            frYyyymmdd: "s", // 등록일자
            fdTag: "s", // 택번호
            fdPreState: "s", // 이전 상태
            fdState: "s", // 현재 상태
            fdS2Dt: "s", // 지사 입고일
            fdS3Dt: "s", // 강제 출고 입고일
            fdS4Dt: "s", // 지사 출고일
            fdS5Dt: "s", // 가맹점 입고일
            fdS6Dt: "s", // 고객 인도일
            fdCancel: "s", // 접수 취소 여부
            fdCacelDt: "s", // 접수 취소일
            fdEstimateDt: "s", // 출고 예정일
            _$uid: "s", // AUI 그리드 고유 ID
            productName: "s", // 접수 세탁물의 완성된 이름
            frInspectBtn: "", // 가맹검품 버튼의 활성화 여부
            brInspectBtn: "", // 확인품 버튼의 활성화 여부
            greenBtn1: "", // 상품수정 버튼의 활성화 여부
            redBtn1: "", // 접수취소 버튼의 활성화 여부
            redBtn2: "", // 결제취소 버튼의 활성화 여부
            redBtn3: "", // 출고취소 버튼의 활성화 여부
            totAddCost: "n", // 추가금액의 합계
            fdS6Type: "s",
            fdPromotionType: "s",
            bcGrade: "s",
            fdPromotionDiscountAmt: "n",
            fdPromotionDiscountRate: "n",
            fdModifyAmtYn: "s", // 2022.05.20 추가
            fdModifyOriginalAmt: "n", // 2022.05.20 추가
            photoList: {

            },
            fdS6Time: "s",
            fdS6CancelYn: "s",
            fdS6CancelTime: "s",
            fdPollutionType: "n",
            fdPollutionBack: "n",
            frInsertDt: "n",
            cellColor: "n",
            frFiId: "n",
            brFiId: "n",
            frFiCustomerConfirm: "s",
            brFiCustomerConfirm: "s",
            fdAgreeType: "s",
            fdSignImage: "s",
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
            fdUrgentAmt: "",
            fdUrgentType: "",
            fdPressed: "",
            fdAdd1Amt: "",
            fdAdd1Remark: "",
            fdAdd2Amt: "",
            fdAdd2Remark: "",
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
            productName: "d",
            frInspectBtn: "d",
            brInspectBtn: "d",
            greenBtn1: "d",
            redBtn1: "d",
            redBtn2: "d",
            redBtn3: "d",
            totAddCost: "d",
            frRefType: "sr",
            frEstimateDate: "d",
            fpCancelYn: "s", // 해당 값이 N인 경우 결제취소가 가능하여야 한다. E일 경우 전부 취소된 상태이므로 열람만 가능하다.
        },

        franchiseRequestDetailSearch: {
            bcId: "n", // 선택된 고객. 없을 경우 null
            searchTag: "s", // 택번호 검색문자
            filterCondition: "s", // "" 전체, "S1" 고객접수, "S2" 자사입고, "S3" 가맹점입고, "N4" 지사미출고, "S4" 지사출고, "B" 확인품, "F" 가맹검품
            filterFromDt: "sr", // 시작 조회기간
            filterToDt: "sr", // 종료 조회기간
        },

        customerInfo: {
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소
            searchString: "sr", // 검색문자
        },

        putNewInspectNeo: {
            fdId: "nr", // 수정시에는 fiId도
            fiId: "n",
            fiAddAmt: "nr",
            fiComment: "s",
            fiType: "sr",
            addPhotoList: "",
            deletePhotoList: "",
        },
    },
    receive: {
        requestCashReceiptData: {
            frCode: "s",
            fcId: "n",
            fcYyyymmdd: "s",
            fcType: "s",
            fcCatApprovalno: "s",
            fcCatApprovaltime: "s",
            fcCatTotamount: "s",
        },

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
                fpCancelYn: "s",
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
            frInsertDtFormatted: "s",
            frInsertDtFormattedXlsx: "s",
            fdS6TimeFormatted: "s",
            fdS6TimeFormattedXlsx: "s",
            productName: "s",
            processName: "s",
            fpId: "n", // 미수금의 결제를 다른 세탁물이 대신한 경우 해당 결제 id
            hpId: "n", // 적용된 행사의 id, 없을경우 0
            fdS6Type: "s",
            fdCancel: "s",
            bcGrade: "s",
            fdPromotionType: "s",
            fdPromotionDiscountAmt: "n",
            fdPromotionDiscountRate: "n",
            fdModifyAmtYn: "s", // 2022.05.20 추가
            fdModifyOriginalAmt: "n", // 2022.05.20 추가
            fdUrgentType: "s",
            fdUrgentAmt: "n",
            fdS6CancelYn: "s",
            fdS6CancelTime: "n",
            fdPollutionBack: "n",
            photoList: { // 2022.04.13 추가
                ffId: "n",
                ffPath: "s",
                ffFilename: "s",
                ffRemark: "s",
            },
            fdPollutionType: "n",
            frInsertDt: "n", // 2022.04.13 추가
            fdS6Time: "n", // 2022.04.13 추가
            frFiId: "n", // 2022.04.05 추가
            brFiId: "n", // 2022.04.05 추가
            frFiCustomerConfirm: "s", // 2022.04.05 추가
            brFiCustomerConfirm: "s", // 2022.04.05 추가
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
            fdCacelDt: "n",
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
            fpCancelYn: "sr", // 해당 값이 N인 경우 결제취소가 가능하여야 한다. E일 경우 전부 취소된 상태이므로 열람만 가능하다.
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
            uncollectMoney: "nr",
            saveMoney: "nr",
            tempSaveFrNo: "s",
            tempSaveBcName: "s",
        },

        itemGroupAndPriceList: { // 접수페이지 시작때 호출되는 API와 같은 API, 이건 dto검증기를 다차원 검증 가능하도록 개량후 검증.
            promotionData: { // 현재 적용해야할 행사가 있을 경우에 오는 모든 행사의 데이터 (이 데이터를 한번 정재하여 중복되는 biItemcode당 하나의 행사만 남겨야 함)
                hpId: "n", // 행사의 아이디
                hpType: "s", // 행사 유형
                hpName: "s", // 행사명
                biItemcode: "s", // 적용할 세탁물 상품코드
                hiDiscountRt: "n" // 적용할 할인율
            },
            addAmountData: {
                baSort: "n",
                baName: "s",
                baRemark: "",
            },
            addCostData: {
                bcProgramFeeBr: "n",
                bcProgramFeeFr: "n",
                bcSmsUnitPrice: "n",
                bcUrgentRate1: "n",
                bcUrgentRate2: "n",
                bcUrgentAmt1: "n",
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
                frManualPromotionYn: "s", // 가맹점의 수기 행사 할인 사용 가능 여부
                frCardTid: "s",
                frUrgentDayYn: "s",
                frTagNo: "s",
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

            if (initialData.etcData.frManualPromotionYn !== "Y") {
                $("#manualDiscountChk").parents("li").hide();
            }

            setAddMenu();
            setPreDefinedKeywords();
            getParamsAndAction();
        });
    },

    /**
     * 고객을 검색조건에 따라 검색하고, 결과가 하나일 경우 즉시선택, 여러개일 경우 팝업에 표현, 없을 경우 신규등록 물음
     *
     * @param params : JSON - 검색조건
     */
    searchCustomer(params) {
        dv.chk(params, dtos.send.customerInfo, "고객검색조건 보내기");
        CommonUI.ajax(grids.s.url.read[1], "GET", params, function (res) {
            const items = res.sendData.gridListData;
            dv.chk(items, dtos.receive.customerInfo, "고객 리스트 받아오기");
            $("#searchCustomerType").val(0);
            $("#searchString").val("");
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

    franchiseRequestDetailSearch(condition) {
        wares.currentCondition = condition;
        dv.chk(condition, dtos.send.franchiseRequestDetailSearch, "메인그리드 필터링 조건 보내기");
        CommonUI.ajax(grids.s.url.read[0], "GET", condition, function(res) {
            const gridData = res.sendData.gridListData;
            gridData.forEach(item => {
                if(item.fdState === "S1" && item.frFiId && item.frFiCustomerConfirm === "1") {
                    item.fdState = "F";
                }
                if(item.fdState === "S2" && item.brFiId && item.brFiCustomerConfirm === "1") {
                    item.fdState = "B";
                }

                item.frInsertDtFormatted = formatDateTime(item.frInsertDt);
                item.frInsertDtFormattedXlsx = formatDateTimeForXlsx(item.frInsertDt);
                item.fdS6TimeFormatted = formatDateTime(item.fdS6Time);
                item.fdS6TimeFormattedXlsx = formatDateTimeForXlsx(item.fdS6Time);

                CommonUI.toppos.makeProductName(item, initialData.userItemPriceSortData);
                item.processName = CommonUI.toppos.processName(item);
            });

            dv.chk(gridData, dtos.receive.franchiseRequestDetailSearch, "메인그리드 조회 결과 리스트");

            grids.f.setData(0, gridData);
            addDistinguishableCellColor();
        });
    },

    saveModifiedOrder(data) {
        dv.chk(data, dtos.send.franchiseRequestDetailUpdate, "상품 수정내용 저장");
        const abc ={requestDetailUpdateList: [data]};
        console.log(abc);
        CommonUI.ajax(grids.s.url.update[0], "MAPPER", abc, function(res) {
            onCloseAddOrder();
            grids.f.updateCurrentModifyRequest();
            alertSuccess("상품 수정 내용이 반영되었습니다.");
        });
    },

    getPaymentList(frId) {
        const condition = {frId: frId};
        dv.chk(condition, dtos.send.franchiseDetailCencelDataList, "결제 리스트 받아오는 조건 보내기");
        CommonUI.ajax("/api/user/franchiseDetailCencelDataList", "GET", condition, function(res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.franchiseDetailCencelDataList, "받아온 결제 리스트");
            collectedCancelPaymentProcess(data.fdTag);
            grids.f.setData(2, data.gridListData);
        });

        CommonUI.ajax("/api/user/requestCashReceiptData", "GET", condition, function(res) {
            const data = res.sendData.gridListData;
            if (data) {
                dv.chk(data, dtos.receive.requestCashReceiptData, "현금 영수증 리스트 받아오기");
                $(".cashReceiptPanel").show();
                grids.f.resize(3);
                grids.f.setData(3, data);
            }
        });
    },

    cancelPayment(target) {
        dv.chk(target, dtos.send.franchiseRequestDetailCencel, "한 항목 결제 취소");
        const url = "/api/user/franchiseRequestDetailCencel";
        CommonUI.ajax(url, "PARAM", target, function(res) {
            const data = res.sendData;
            if (wares.selectedCustomer.bcId) {
                wares.selectedCustomer.saveMoney = data.saveMoney;
                wares.selectedCustomer.uncollectMoney = data.uncollectMoney;
                $("#uncollectMoneyMain").html(wares.selectedCustomer.uncollectMoney.toLocaleString());
                $("#saveMoneyMain").html(wares.selectedCustomer.saveMoney.toLocaleString());
            }
            if(target.type === "1") {
                alertSuccess("결제 취소를 완료하였습니다.");
            }else if(target.type === "2") {
                alertSuccess("취소 후 적립금 전환을 완료하였습니다.");
            }
            comms.getPaymentList(currentRequest.frId);
        });
    },

    cancelOrder(fdId) {
        const condition = {
            fdId: fdId,
            bcId: wares.selectedCustomer.bcId ? wares.selectedCustomer.bcId : 0,
        };
        dv.chk(condition, dtos.send.franchiseReceiptCancel, "접수 취소");
        CommonUI.ajax(grids.s.url.delete[0], "PARAM", condition, function(res) {
            const data = res.sendData;
            if (wares.selectedCustomer.bcId) {
                wares.selectedCustomer.saveMoney = data.saveMoney;
                wares.selectedCustomer.uncollectMoney = data.uncollectMoney;
                $("#uncollectMoneyMain").html(wares.selectedCustomer.uncollectMoney.toLocaleString());
                $("#saveMoneyMain").html(wares.selectedCustomer.saveMoney.toLocaleString());
            }

            AUIGrid.removeRowByRowId(grids.s.id[0], currentRequest._$uid);
            AUIGrid.removeSoftRows(grids.s.id[0]);
            alertSuccess("접수 취소를 완료하였습니다.");
        });
    },

    cancelDeliverState(fdId) { // 현재 접수취소와 같은 url을 사용중
        const condition = {fdId: fdId};
        dv.chk(condition, dtos.send.franchiseLeadCancel, "고객출고 취소");
        const url = "/api/user/franchiseLeadCancel";
        CommonUI.ajax(url, "PARAM", condition, function(res) {
            const tempVar = currentRequest.fdPreState;
            currentRequest.fdPreState = currentRequest.fdState;
            currentRequest.fdState = tempVar;
            AUIGrid.updateRowsById(grids.s.id[0], currentRequest);
            alertSuccess("고객출 취소를 완료하였습니다.");
        });
    },


//////////////// 구 검품 관련 항목 /////////////////////
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

///////////////// 신검품 관련항목 ///////////////////
    putNewInspectNeo(formData) {
        const testObj = Object.fromEntries(formData);
        testObj.fdId = parseInt(testObj.fdId);
        testObj.fiId = parseInt(testObj.fiId);
        testObj.fiAddAmt = parseInt(testObj.fiAddAmt) | 0;
        if(!testObj.addPhotoList) testObj.addPhotoList = []; // btId값이 없는 신규등록건
        if(!testObj.deletePhotoList) testObj.deletePhotoList = []; // btId값이 없는 신규등록건
        if(!testObj.fiId) testObj.fiId = 0;

        dv.chk(testObj, dtos.send.putNewInspectNeo, "확인품 등록");

        CommonUI.ajax("/api/user/franchiseInspectionSave", "POST", formData, function (res) {
            alertSuccess("가맹검품 내역이 저장되었습니다.");
            $("#successBtn").on("click", function () {
                closeFrInspectEditPop(true);
            });
        });
    },

    getFrInspectNeo(target) {
        CommonUI.ajax("/api/user/franchiseInspectionInfo", "GET", target, function (res) {
            const data = res.sendData;

            wares.currentFrInspect.inspeotInfoDto = data.inspeotInfoDto;
            wares.currentFrInspect.frPhotoList = data.photoList;
            wares.currentFrInspect.fdTotAmt = data.inspeotInfoDto.fdTotAmt;
            wares.currentFrInspect.fiAddAmt = data.inspeotInfoDto.fiAddAmt;
            wares.currentFrInspect.fiComment = data.inspeotInfoDto.fiComment;

            openFrInspectPop();
        });
    },

    getBrInspectNeo(target) {
        CommonUI.ajax("/api/user/franchiseInspectionInfo", "GET", target, function (res) {
            const data = res.sendData;

            wares.currentBrInspect.inspeotInfoDto = data.inspeotInfoDto;
            wares.currentBrInspect.brPhotoList = data.photoList;
            wares.currentBrInspect.fdTotAmt = data.inspeotInfoDto.fdTotAmt;
            wares.currentBrInspect.fiAddAmt = data.inspeotInfoDto.fiAddAmt;
            wares.currentBrInspect.fiComment = data.inspeotInfoDto.fiComment;

            openBrInspectPop();
        });
    },

    inspectionJudgement(answer, isFromFrPop) {
        dv.chk(answer, dtos.send.franchiseInspectionYn, "고객 수락 거부 응답 보내기");
        const url = "/api/user/franchiseInspectionYn";
        CommonUI.ajax(url, "PARAM", answer, function (res) {
            let resultMsg = "";
            if (answer.type === "2") {
                resultMsg = "세탁진행을 보고 하였습니다.";
            } else if (answer.type === "3") {
                resultMsg = "고객반품을 보고 하였습니다.";
            }
            alertSuccess(resultMsg);

            if(isFromFrPop) {
                closeFrInspectEditPop(true);
            } else {
                closeBrInspectPop(true);
            }
        });
    },

    deleteInspectionNeo(target) {
        dv.chk(target, dtos.send.franchiseInspectionDelete, "등록된 가맹검품 삭제 대상 보내기");
        const url = "/api/user/franchiseInspectionDelete";
        CommonUI.ajax(url, "PARAM", target, function(res) {
            alertSuccess("가맹검품내역이 삭제되었습니다.");
            $("#successBtn").on("click", function () {
                closeFrInspectEditPop(true);
            });
        });
    },

    sendKakaoMessage(data) {
        dv.chk(data, dtos.send.franchiseInspectionMessageSend, "검품 카카오 메시지 보내기");
        CommonUI.ajax("/api/user/franchiseInspectionMessageSend", "PARAM", data, function (res) {
            alertSuccess("메시지 전송이 완료되었습니다.");
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
            "grid_main", "grid_customerList", "grid_paymentList", "grid_cashReceipt"
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
                "/api/user/",
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
                    dataField: "cellColor",
                    headerText: "",
                    style: "grid_grouper",
                    width: 4,
                    cellMerge: true,
                    renderer: {
                        type: "TemplateRenderer"
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return `<span class="bgcolor${value}" style="display: none">${value}</span>`;
                    },
                }, {
                    dataField: "frRefType",
                    headerText: "구분",
                    width: 40,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.frRefType[value];
                    },
                }, {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                    width: 140,
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 65,
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
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}"></span>`;
                        CommonUI.toppos.makeProductName(item, initialData.userItemPriceSortData);
                        return colorSquare + ` <span>` + item.productName + `</span>`;
                    }
                }, {
                    dataField: "processName",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 90,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
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
                    width: 80,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 70,
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.getFdStateName(value, item.fdS6Type);
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
                    width: 174,
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
                    dataField: "frInsertDtFormattedXlsx",
                    headerText: "접수일자",
                    width: 68,
                    renderer : {
                        type : "TemplateRenderer"
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        return item.frInsertDtFormatted;
                    },
                }, {
                    dataField: "fdS6TimeFormattedXlsx",
                    headerText: "고객출고",
                    width: 68,
                    renderer : {
                        type : "TemplateRenderer"
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        return item.fdS6TimeFormatted;
                    },
                }, {
                    dataField: "fdS2Dt",
                    headerText: "지사입고",
                    width: 68,
                    dataType: "date",
                    formatString: "yy.mm.dd",
                }, {
                    dataField: "fdS4Dt",
                    headerText: "지사출고",
                    width: 68,
                    dataType: "date",
                    formatString: "yy.mm.dd",
                }, {
                    dataField: "fdS5Dt",
                    headerText: "완성일자",
                    width: 68,
                    dataType: "date",
                    formatString: "yy.mm.dd",
                }, {
                    dataField: "frInspectBtn",
                    headerText: "가맹<br>검품",
                    width: 63,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        item.frInspectBtn = true;
                        if(item.fdState === "S1" && !item.frFiId && item.fdCancel === "N") {
                            template = `<button class="c-state">등록</button>`;
                        } else if(item.frFiId && item.frFiCustomerConfirm === "1") {
                            template = `<button class="c-state c-state--yellow">확인</button>`;
                        } else if(item.frFiId && item.frFiCustomerConfirm === "2") {
                            template = `<button class="c-state c-state--modify">수락</button>`;
                        } else if(item.frFiId && item.frFiCustomerConfirm === "3") {
                            template = `<button class="c-state c-state--cancel">거부</button>`;
                        } else {
                            item.frInspectBtn = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "brInspectBtn",
                    headerText: "확인품",
                    width: 63,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        item.brInspectBtn = true;
                        if(item.fdState === "B" && item.brFiId && item.brFiCustomerConfirm === "1") {
                            template = `<button class="c-state c-state--yellow">확인</button>`;
                        } else if(item.brFiId && item.brFiCustomerConfirm === "2") {
                            template = `<button class="c-state c-state--modify">수락</button>`;
                        } else if(item.brFiId) {
                            template = `<button class="c-state c-state--cancel">거부</button>`;
                        } else {
                            item.brInspectBtn = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "greenBtn1",
                    headerText: "상품<br>수정",
                    width: 63,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(["S1", "F"].includes(item.fdState) && item.fdCancel === "N") {
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
                    headerText: "접수<br>취소",
                    width: 63,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if( item.fpCancelYn === "Y" && ["S1", "F", "B"].includes(item.fdState) && item.fdCancel === "N" && !item.fpId) {
                            template = `
                                <button class="c-state c-state--cancel">취소</button>
                            `;
                            item.redBtn1 = true;
                        } else {
                            item.redBtn1 = false;
                        } 
                        return template;
                    },
                }, {
                    dataField: "redBtn2",
                    headerText: "결제<br>취소",
                    width: 63,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(isCancelableItem(item)) {
                            template = `
                                <button class="c-state c-state--cancel">취소</button>
                            `;
                            item.redBtn2 = true;
                        } else if (item.fpCancelYn === "Y" || item.fpCancelYn === "N") {
                            template = `
                                <button class="c-state c-state--yellow">보기</button>
                            `;
                            item.redBtn2 = true;
                        } else {
                            item.redBtn2 = false;
                        }
                        return template;
                    },
                }, {
                    dataField: "redBtn3",
                    headerText: "출고<br>취소",
                    width: 63,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        if(item.fdState === "S6" && item.fdCancel === "N") {
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
                enableColumnResize : true,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : false,
                enableCellMerge : true,
                rowHeight : 48,
                headerHeight : 48,
                rowStyleFunction(rowIndex, item) {
                    return "grid_cellcolor" + item.cellColor;
                },
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "bcName",
                    headerText: "고객명",
                }, {
                    dataField: "bcHp",
                    headerText: "전화번호",
                    width: 150,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
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
                    dataField: "fpCancelYn",
                    headerText: "취소여부",
                    width: 80,
                }, {
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
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
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
                enableColumnResize : true,
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
                    dataField: "fcCatApprovaltime",
                    headerText: "승인일자",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return "20" + value.substring(0,2) + "-" + value.substring(2, 4) + "-" + value.substring(4, 6);
                    }
                }, {
                    dataField: "fcCatTotamount",
                    headerText: "승인금액",
                    style: "grid_textalign_right",
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fcType",
                    headerText: "영수증 구분",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return wares.fcTypeName[value];
                    }
                }, {
                    dataField: "cancelBtn",
                    headerText: "영수증 취소",
                    width: 150,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        return '<button class="c-state c-state--cancel">취소</button>';
                    },
                },
            ];

            grids.s.prop[3] = {
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

            grids.s.columnLayout[4] = [
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

            grids.s.prop[4] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
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

            grids.s.columnLayout[5] = [
                {
                    dataField: "insertDt",
                    headerText: "등록일시",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fiType",
                    headerText: "유형",
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
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
                    labelFunction(rowIndex, columnIndex, value, headerText, item ) {
                        return CommonData.name.fiCustomerConfirm[value];
                    },
                },
            ];

            grids.s.prop[5] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: true,
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

        setData(gridNum, data) {
            AUIGrid.setGridData(grids.s.id[gridNum], data);
        },

        getData(gridNum) {
            return AUIGrid.getGridData(grids.s.id[gridNum]);
        },

        clearData(gridNum) {
            AUIGrid.clearGridData(gridNum);
        },

        switchModifyMode(isModifyMode) {
            const modColumn = {
                modify: ["frInspectBtn", "brInspectBtn", "greenBtn1", "redBtn1", "redBtn2", "redBtn3"],
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

        clearGrid(gridNum) {
            AUIGrid.clearGridData(grids.s.id[gridNum]);
        },

        resize(gridNum) {
            AUIGrid.resize(grids.s.id[gridNum]);
        },

        getRemovedCheckRows(gridNum) {
            if(AUIGrid.getCheckedRowItems(grids.s.id[gridNum]).length){
                AUIGrid.removeCheckedRows(grids.s.id[gridNum]);
            }else{
                AUIGrid.removeRow(grids.s.id[gridNum], "selectedIndex");
            }
            return AUIGrid.getRemovedItems(grids.s.id[gridNum]);
        },

        resetChangedStatus(gridNum) {
            AUIGrid.resetUpdatedItems(grids.s.id[gridNum]);
        },

        updateCurrentModifyRequest() {
            comms.franchiseRequestDetailSearch(wares.currentCondition);
        },

        getItemByRowIndex(rowId) {
            return AUIGrid.getItemByRowIndex(grids.s.id[0], rowId);
        },

        // 엑셀 내보내기(Export)
        exportToXlsx() {
            //FileSaver.js 로 로컬 다운로드가능 여부 확인
            if(!AUIGrid.isAvailableLocalDownload(grids.s.id[0])) {
                alertCaution("파일 다운로드가 불가능한 브라우저 입니다.", 1);
                return;
            }
            let customerNamePart = "";
            if (wares.selectedCustomer.bcId) {
                customerNamePart = wares.selectedCustomer.bcName + "_";
            }
            AUIGrid.exportToXlsx(grids.s.id[0], {
                fileName : wares.title + "_" + customerNamePart + $("input[name='filterCondition']" +
                        "[value='" + wares.currentCondition.filterCondition + "']").siblings("label").html()
                    + `_${wares.currentCondition.filterFromDt}_${wares.currentCondition.filterToDt}`,
                progressBar : true,
            });
        },

        /* 그리드에 표시되는 아이템 갯수의 반환 */
        getCount(gridNum) {
            return AUIGrid.getRowCount(grids.s.id[gridNum]);
        },
    },
};

/* dto가 아닌 일반적인 데이터들 정의 */
const wares = {
    title: "통합조회",
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
    currentFrInspect: {},
    currentBrInspect: {},
    fcTypeName: {
        "1": "소비자소득공제",
        "2": "사업자지출증빙",
        "3": "자진발급",
    }
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
                case "frInspectBtn":
                    // 가맹점 검품등록창 진입
                    if(e.item.frInspectBtn) {
                        openFrInspectEditPop(e.item);
                    }
                    break;
                    /* 지사 확인품 확인창 진입 */
                case "brInspectBtn":
                    if(e.item.brInspectBtn) {
                        wares.currentBrInspect = e.item;
                        if(e.item.brFiId) {
                            const target = {
                                fiId: e.item.brFiId,
                            }
                            comms.getBrInspectNeo(target);
                        }
                    }
                    // 검품확인창 진입
                    break;
                case "greenBtn1":
                    // 수정모드 진입
                    if(e.item.greenBtn1) {
                        modifyOrder();
                    }
                    break;
                case "redBtn1":
                    // 확인후 ajax 호출하며 그리드에서 삭제
                    if(e.item.redBtn1) {
                        alertCheck("선택한 품목을 접수취소하시겠습니까?<br>접수취소 후에는 신규접수를 통해<br>재등록 가능합니다.");
                        $("#checkDelSuccessBtn").on("click", function() {
                            comms.cancelOrder(e.item.fdId);
                            $("#popupId").remove();
                        });
                    }
                    break;
                case "redBtn2":
                    if(e.item.redBtn2) {
                        cancelPaymentPop(e.item);
                    }
                    // 결제취소창 진입
                    break;
                case "redBtn3":
                    // 확인후 가맹점 입고로 상태변경하여 ajax 호출
                    if(e.item.redBtn3) {
                        alertCheck("선택한 품목을 고객출고 취소하시겠습니까?");
                        $("#checkDelSuccessBtn").on("click", function() {
                            comms.cancelDeliverState(e.item.fdId);
                            $("#popupId").remove();
                        });
                    }
                    break;
            }
        });

        AUIGrid.bind(grids.s.id[3], "cellClick", function (e) {
            if (e.dataField === "cancelBtn") {
                cancelCashReceipt();
            }
        });

        $("#uncollectMoneyMain").parents("li").on("click", function () {
            if (wares.selectedCustomer && wares.selectedCustomer.bcHp) {
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
        $("#vkeyboard2").on("click", function() {
            onShowVKeyboard(2);
        });
        $("#vkeyboard3").on("click", function() {
            onShowVKeyboard(3);
        });
        $("#vkeyboard4").on("click", function() {
            onShowVKeyboard(4);
        });

        $("#vkeypad0").on("click", function() {
            onShowVKeypad(0);
        });

        $("#vkeypad1").on("click", function() {
            onShowVKeypad(1);
        });

        // 구 검품 이벤트
        // $("#vkeyboard4").on("click", function() {
        //     onShowVKeyboard(4);
        // });
        // $("#vkeypad0").on("click", function () {
        //     wares.keypadNum = 0;
        //     vkey.showKeypad("fiAddAmt", {callback: onKeypadConfirm});
        // });
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
                uncollectMoney: 0,
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
                mainSearch();
            }
        });

        $("#printReceipt").on("click", function() {
            const selectedItem = grids.f.getSelectedItem(0);
            if(selectedItem) {
                alertThree("영수증을 인쇄 하시겠습니까?", "고객용", "매장용", "취소");
                $("#popFirstBtn").on("click", function () {
                    CommonUI.toppos.printReceipt(selectedItem.frNo, "", true, false, "N");
                    $('#popupId').remove();
                });
                $("#popSecondBtn").on("click", function () {
                    CommonUI.toppos.printReceipt(selectedItem.frNo, "", false, true, "N");
                    $('#popupId').remove();
                });
                $("#popThirdBtn").on("click", function () {
                    $('#popupId').remove();
                });
            } else {
                alertCaution("영수증을 출력할 상품을 선택해 주세요.", 1);
            }
        });

        $("#printReceiptAll").on("click", function() {
            const selectedItems = grids.f.getData(0);
            const isADay = wares.currentCondition.filterFromDt === wares.currentCondition.filterToDt;
            if(selectedItems.length && isADay) {
                alertThree("조회된 영수증을 전체 인쇄 하시겠습니까?<br>인쇄 전 영수증 용지가 충분한지 확인해 주세요.<br>(부족할 경우 인쇄가 중단됩니다.)", "고객용", "매장용", "취소");
                $("#popFirstBtn").on("click", function () {
                    printReceiptAll(selectedItems, true, false);
                    $('#popupId').remove();
                });
                $("#popSecondBtn").on("click", function () {
                    printReceiptAll(selectedItems, false, true);
                    $('#popupId').remove();
                });
                $("#popThirdBtn").on("click", function () {
                    $('#popupId').remove();
                });
            } else if (!selectedItems.length){
                alertCaution("영수증을 출력할 상품을 조회해 주세요.", 1);
            } else if (!isADay) {
                alertCaution("조회단위가 하루인 경우에만 전체 영수증 인쇄가 가능합니다.", 1);
            }
        });

        $("#changeDateRange").on("change", function () {
            changeSearchPeriod($(this).val());
        });

        $("#closePhotoListPop").on("click", function () {
            $("#receiptPhotoList").html("");
            $("#receiptPhotoPop").removeClass("active");
        });

        const urgentTypeElement = $("input[name='fdUrgentType']");
        urgentTypeElement.on("click", function (e) {
            urgentTypeElement.prop("checked", false);
            $(e.currentTarget).prop("checked", true);
            calculateItemPrice();
        });

        $("#frSendKakaoMsg, #brSendKakaoMsg").on("click", function(e) {
            alertCheck("고객님께 해당 내용을 알리고,<br>세탁진행과 반품 여부를 묻는<br>메시지를 발송하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                sendInspectMessage(e.target.id);
                $('#popupId').remove();
            });
        });

        $("#exportXlsx").on("click", function () {
            if (grids.f.getData(0).length) {
                grids.f.exportToXlsx();
            } else {
                alertCaution("다운로드할 데이터가 없습니다.<br>먼저 조회를 해주세요.", 1);
            }
        });

        $("#filterConditionBox input").on("click", function (e) {
            const criterionName = $(e.target).attr("data-criterion");
            if (criterionName) {
                $("#criterionName").html(criterionName);
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
            judgeEtcNoneOnOff();
            const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
            if($("#fdRetry").is(":checked") &&
                ($("#processCheck input[type='checkbox']:checked").length > 1 || $isEtcProcessChecked)) {
                $("#fdRetry").prop("checked", false);
            }

            calculateItemPrice();
        });

        /* 수기 할인 적용, 해제시 가격 계산 */
        $("#manualDiscountChk").on("change", function(e) {
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

        $("#etcNone, #fdRetry").on("click", function (e) {
            $processInput.prop("checked", false);
            $("#waterNone").prop("checked", true);
            $("#waterBtn").removeClass("choice-drop__btn--active");
            resetPollutionPop();
            resetFdRepairInputs();
            resetFdAddInputs();
            if (e.target.id === "fdRetry") {
                $("#fdRetry").prop("checked", true);
            }
            // 각 내부 항목들의 초기화
            setTimeout(function () {
                calculateItemPrice();
            }, 0);
        });
    
        $("#urgentNone").on("click", function () {
            $urgentInput.prop("checked", false);
            calculateItemPrice();
        });

        $("#manualDiscountPercent").on("keyup", function () {
            validateManualDiscountPercent();
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
        $("#transferPoint").on("click", function () {
            const item = grids.f.getSelectedItem(2);

            if (item) {
                if (item.fpCancelYn === "Y") {
                    alertCaution("이미 취소 완료된 결제 입니다.", 1);
                    return;
                }
                alertCheck("해당 내역을 적립금으로 전환하시겠습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    cancelPayment("2", item);
                    $('#popupId').remove();
                });
            } else {
                alertCaution("적립금 전환할 결제내역을 선택해 주세요", 1);
            }
        });

        $("#refundPayment").on("click", function () {
            const item = grids.f.getSelectedItem(2);

            if (item) {
                if (item.fpCancelYn === "Y") {
                    alertCaution("이미 취소 완료된 결제 입니다.", 1);
                    return;
                }

                alertCheck("해당 내역을 환불 하시겠습니까?");
                $("#checkDelSuccessBtn").on("click", function () {
                    const cancelType = item.fpType === "03" ? "2" : "1";
                    cancelPayment(cancelType, item);
                    $('#popupId').remove();
                });
            } else {
                alertCaution("환불하실 결제내역을 선택해 주세요", 1);
            }
        });

        $("#frEditFiAddAmt").on("keyup", function () {
            $(this).val($(this).val().positiveNumberInput());
        });

        $("#closePaymentPop").on("click", function () {
            // 결제 취소 가능한 목록이 하나도 없을 경우 버튼을 취소 -> 보기 버튼으로 바꾸고 접수취소가 가능하게 한다.
            const paymentGridData = AUIGrid.getGridData(grids.s.id[2]);
            let isAllCanceled = true;
            for (const item of paymentGridData) {
                if (item.fpCancelYn === "N") {
                    isAllCanceled = false;
                    break;
                }
            }

            if(isAllCanceled) {
                const frId = currentRequest.frId;
                const gridData = AUIGrid.getGridData(grids.s.id[0]);
                gridData.forEach(item => {
                    if(item.frId === frId) {
                        item.fpCancelYn = "Y"
                        AUIGrid.updateRowsById(grids.s.id[0], item);
                    }
                });
            }
            $(".cashReceiptPanel").hide();

            $("#paymentListPop").removeClass("active");
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


        // 가맹검품 팝업 기능
        $("#takePhotoBtn").on("click", function () {
            takePhoto();
        });
        $("#commitInspect").on("click", function () {
            saveInspect();
        });
        $("#deleteInspect").on("click", function () {
            alertCheck("현재 가맹검품 내역을 삭제하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const target = {
                    fiId: wares.currentFrInspect.frFiId,
                    fiPhotoYn: wares.currentFrInspect.inspeotInfoDto.fiPhotoYn,
                }
                comms.deleteInspectionNeo(target);
                $('#popupId').remove();
            });
        });
        $("#closeFrInspectEditPop").on("click", function () {
            closeFrInspectEditPop();
        });
        $("#closeFrInspectViewPop").on("click", function () {
            closeFrInspectViewPop();
        });
        $("#frCustomerConfirmed").on("click", function () {
            alertCheck("고객께서 진행수락 하셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentFrInspect.frFiId,
                    fiAddAmt: wares.currentFrInspect.fiAddAmt,
                    type: "2",
                };
                comms.inspectionJudgement(answer, true);
                $('#popupId').remove();
            });
        });
        $("#frCustomerDenied").on("click", function () {
            alertCheck("고객께서 진행거부 하셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentFrInspect.frFiId,
                    fiAddAmt: wares.currentFrInspect.fiAddAmt,
                    type: "3",
                };
                comms.inspectionJudgement(answer, true);
                $('#popupId').remove();
            });
        });
        $("#frPhotoList").on("click", ".deletePhotoBtn", function() {
            const ffId = $(this).attr("data-ffId");
            const addIdx = $(this).attr("data-addIdx");
            if(ffId) {
                if(!wares.currentFrInspect.deletePhotoList) wares.currentFrInspect.deletePhotoList = [];
                wares.currentFrInspect.deletePhotoList.push(parseInt(ffId));
            }
            if(addIdx) {
                delete wares.currentFrInspect.addPhotoList[addIdx];
            }
            $(this).parents(".motherLi").remove();
        });

        // 버튼 클릭시 문구 추가
        $(".word-btn").on("click", function () {
            const currentHtml = $("#frEditFiComment").val();
            let htmlText;
            if(currentHtml !== "") {
                htmlText = ", " + this.innerHTML;
            }else{
                htmlText = this.innerHTML;
            }
            $("#frEditFiComment").val(currentHtml + htmlText);
        })

        // 확인품 팝업 기능
        $("#brCustomerConfirmed").on("click", function () {
            alertCheck("고객께서 진행수락 하셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentBrInspect.brFiId,
                    fiAddAmt: wares.currentBrInspect.fiAddAmt,
                    type: "2",
                };
                comms.inspectionJudgement(answer, false);
                $('#popupId').remove();
            });
        });
        $("#brCustomerDenied").on("click", function () {
            alertCheck("고객께서 진행거부 하셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentBrInspect.brFiId,
                    fiAddAmt: wares.currentBrInspect.fiAddAmt,
                    type: "3",
                };
                comms.inspectionJudgement(answer, false);
                $('#popupId').remove();
            });
        });
        $("#closeBrInspectPop").on("click", function () {
            closeBrInspectPop();
        });
    },
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    /* 헤더의 상단 해당메뉴 큰 버튼의 아이콘 및 색 활성화 */
    $("#menuIntegrate").addClass("active")
        .children("img").attr("src", "/assets/images/icon__totalsearch--active.svg");

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

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });

    $('.pop__close').on('click', function(e) {
        $(this).parents('.pop').removeClass('active');
    });
}

function modifyOrder() {
    currentRequest.productName = undefined;
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


    $("#class02, #class03").parents("li").css("display", "none");
    $("#class" + currentRequest.bcGrade).parents("li").css("display", "block");

    wares.selectedLaundry.bgCode = currentRequest.biItemcode.substr(0, 3);
    wares.selectedLaundry.bsCode = currentRequest.biItemcode.substr(3, 1);
    $("input[name='bsItemGroupcodeS']:input[value='" + wares.selectedLaundry.bsCode + "']").prop("checked", true);
    $("#" + currentRequest.biItemcode).prop("checked", true);
    $(".choice-color__input[value='" + currentRequest.fdColor + "']").prop("checked", true);
    $("input[name='fdPattern']:input[value='" + currentRequest.fdPattern +"']").prop("checked", true);
    $("input[name='fdPriceGrade']:input[value='" + currentRequest.fdPriceGrade +"']").prop("checked", true);
    $("input[name='fdDiscountGrade']:input[value='" + currentRequest.fdDiscountGrade +"']").first().prop("checked", true);

    $("#manualDiscountPercent").val(currentRequest.fdPromotionDiscountRate);
    /* 할인이 본사직영의 수기행사할인인 경우 해당 체크박스를 체크 */
    if (currentRequest.fdPromotionType === "H1") {
        $("#manualDiscountChk").prop("checked", true);
    }

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

    if (currentRequest.fdUrgentYn === "Y") {
        $("#fdUrgent" + currentRequest.fdUrgentType).prop("checked", true);
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

    if (["01", "02", "03"].includes(currentRequest.fdPromotionType)) {
        $("#modifyBtn").parents("li").hide();
        alertCaution("행사가 적용중인 상품은 수정할 수 없습니다.<br>필요시 접수취소 후 다시 접수 해주세요.", 1);
    } else {
        $("#modifyBtn").parents("li").show();
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

    /* 결제를 취소할 수 있는 상황이면서, 수정 후 가격이 수정 전 가격보다 올랐을 때 */
    if(currentRequest.fpCancelYn === "N" && currentRequest.fdState !== "S2"
        && currentRequest.fdCancel === "N" && wares.startPrice > currentRequest.fdTotAmt) {
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
    currentRequest.fdUrgentType = $("input[name='fdUrgentType']:checked").val() ?
        $("input[name='fdUrgentType']:checked").val() : "";
    currentRequest.fdUrgentYn = currentRequest.fdUrgentType !== "" ? "Y" : "N";

    const pollutionLoc = $("input[name='pollutionLoc']");
    for(const element of pollutionLoc) {
        if ($(element).is(":checked")) {
            currentRequest[element.id] = "Y";
        } else {
            currentRequest[element.id] = "N";
        }
    }

    checkIncreaseAmt();
    
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

    if (($("#pollution00").is(":checked")
            && $("#fdPollution").is(":checked"))
        || (!$("#pollution00").is(":checked")
            && !$("#fdPollution").is(":checked"))) {
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
    const vkeyProp = [];
    const vkeyTargetId = ["searchString", "fdRemark", "fdRepairRemark", "fdAdd1Remark", "frEditFiComment"];

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

function onShowVKeypad(num) {
    const vkeypadProp = [];
    const vkeypadTargetId = ["frEditFiAddAmt", "manualDiscountPercent"];

    vkeypadProp[0] = {
        title : "추가비용 입력",
        callback() {
            return $("#" + vkeypadTargetId).val(parseInt($("#" + vkeypadTargetId).val()).toLocaleString());
        }
    }

    vkeypadProp[1] = {
        title : "수기할인율 입력",
        callback: validateManualDiscountPercent,
        maxlength: 3,
        clrfirst: true,
    }

    vkey.showKeypad(vkeypadTargetId[num], vkeypadProp[num]);
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

function setAddMenu() {
    if (initialData.etcData.frUrgentDayYn === "N") $("#fdUrgent1").parents("#urgentCheck li").remove();
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

    grids.f.clearGrid(0);

    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 30);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");
    $("#filterFromDt").val(fromday);
    $("#filterToDt").val(today);
    $("#changeDateRange").val("30");

    setTopMenuHref();
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

    for(const element of $keypadBtn) {
        removeEventsFromElement(element);
    }

    for(const element of $keypadBackspace) {
        removeEventsFromElement(element);
    }

    for(const element of $keypadBoilerplate) {
        removeEventsFromElement(element);
    }
}

function removeEventsFromElement(element) {
    const elementClone = element.cloneNode(true);
    element.parentNode.replaceChild(elementClone, element);
}

/* 결제취소가 가능한 접수품인지 판단. */
function isCancelableItem (item) {
    return item.fpCancelYn === "N" && item.fdState !== "S2" && item.fdCancel === "N";
}

function cancelPaymentPop(item) {
    comms.getPaymentList(item.frId);
    $(".cashReceiptPanel").hide();

    if (isCancelableItem(item)) {
        $("#transferPoint, #refundPayment").parents("li").show();
    } else {
        $("#transferPoint, #refundPayment").parents("li").hide();
    }

    $("#paymentListPop").addClass("active");
    grids.f.resize(2);
}

function cancelPayment(cancelType, item) {
    if (!item.fpMonth) {
        item.fpMonth = 0;
    }
    if(item) {
        const target = {
            fpId: item.fpId,
            type: cancelType,
            bcId: wares.selectedCustomer.bcId ? wares.selectedCustomer.bcId : 0,
        }
        if(cancelType === "1" && item.fpType === "02") {
            try {
                const params = {
                    fpMonth: item.fpMonth,
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
                    } else if(jsonRes.ERRORMESSAGE === "TEL)1544-4700        / V 기취소된거래   / 거래내역확인요망") {
                        alertCheck("해당 거래는 단말기를 통해<br>직접 취소된 거래입니다.<br>"
                            + "전산상으로도 취소 하시겠습니까?");
                        $("#checkDelSuccessBtn").on("click", function () {
                            comms.cancelPayment(target);
                            $('#popupId').remove();
                        });
                    }else if(jsonRes.STATUS === "FAILURE") {
                        $('#payStatus').hide();
                        if (jsonRes.ERRORDATA === "erroecode:404, error:error") {
                            alertCancel("카드결제 단말기 연결이 감지되지 않습니다.<br>연결을 확인해 주세요.");
                        } else if (jsonRes.ERRORDATA === "erroecode:0, error:timeout") {
                            alertCancel("유효 시간이 지났습니다.<br>다시 결제취소 버튼을 이용하여<br>취소를 시도해 주세요.");
                        } else if(jsonRes.ERRORMESSAGE === "단말기에서종료키누름 /                  /                 ") {
                            alertCancel("단말기 종료키를 통해 결제가 중지되었습니다.");
                        } else if (jsonRes.ERRORMESSAGE === " /  / ") {
                            alertCancel("단말기가 사용중입니다.<br>확인 후 다시 시도해 주세요.");
                        } else {
                            alertCancel("카드결제 취소중 에러 발생<br>" + jsonRes.ERRORMESSAGE);
                        }

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
    } catch (err) {
        if (!(err instanceof DOMException)) {
            CommonUI.toppos.underTaker("카메라가 없음", "checkregist : 카메라 찾기");
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
    const searchCondition = {
        fdId: e.item.fdId,
        type: "1"
    }
    comms.getInspectionList(searchCondition);
}

function ynStyle(rowIndex, columnIndex, value, headerText, item, dataField) {
    return value === "Y" ? "yesBlue" : "noRed"
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
    } else {
        openFrInspectPop();
    }
}

async function openFrInspectPop() {
    if((wares.currentFrInspect.frFiCustomerConfirm === "1" || !wares.currentFrInspect.frFiCustomerConfirm)
        && wares.currentFrInspect.fdCancel === "N") {
        try {
            wares.isCameraExist = true;
            wares.cameraStream = await navigator.mediaDevices.getUserMedia({
                audio: false,
                video: {
                    width: {ideal: 4096},
                    height: {ideal: 2160}
                },
            });

            const screen = document.getElementById("cameraScreen");
            screen.srcObject = wares.cameraStream;
        } catch (err) {
            if (!(err instanceof DOMException)) {
                CommonUI.toppos.underTaker("카메라가 없음", "checkregist : 카메라 찾기");
            }
            wares.isCameraExist = false;
        }

        let totAmt = wares.currentFrInspect.fdTotAmt ? wares.currentFrInspect.fdTotAmt : 0 ;
        $("#frEditFdTotAmtInPut").val(totAmt.toLocaleString());

        $("#frEditFiComment").val(wares.currentFrInspect.fiComment
            ? wares.currentFrInspect.fiComment : "");
        $("#frEditFiAddAmt").val(wares.currentFrInspect.fiAddAmt
            ? wares.currentFrInspect.fiAddAmt.toLocaleString() : "0");

        if(wares.currentFrInspect.frPhotoList) {
            for (const photo of wares.currentFrInspect.frPhotoList) {
                const photoHtml = `
                <li class="motherLi">
                    <a href="${photo.ffPath + photo.ffFilename}" data-lightbox="images" data-title="이미지 확대">
                        <img src="${photo.ffPath + "s_" + photo.ffFilename}" alt=""/>
                    </a>
                    <button class="checkreg__delete deletePhotoBtn" data-ffId="${photo.ffId}">삭제</button>
                </li>
                `;
                $("#frPhotoList").append(photoHtml);
            }
        }

        if(wares.currentFrInspect.frFiCustomerConfirm === "1") {
            $("#frCustomerDenied").show();
            $("#frCustomerConfirmed").show();
            $("#frSendKakaoMsg").parents("li").show();
            $("#deleteInspect").show();
        } else {
            $("#frCustomerDenied").hide();
            $("#frCustomerConfirmed").hide();
            $("#frSendKakaoMsg").parents("li").hide();
            $("#deleteInspect").hide();
        }

        $("#frInspectEditPop").addClass("active");
    } else {
        let totAmt = wares.currentFrInspect.fdTotAmt ? wares.currentFrInspect.fdTotAmt : 0 ;
        if(wares.currentFrInspect.frFiCustomerConfirm === "2") {
            totAmt -= wares.currentFrInspect.fiAddAmt;
        }
        $("#frViewFdTotAmtInPut").val(totAmt.toLocaleString());

        $("#frViewFiComment").val(wares.currentFrInspect.fiComment
            ? wares.currentFrInspect.fiComment : "");
        $("#frViewFiAddAmt").val(wares.currentFrInspect.fiAddAmt
            ? wares.currentFrInspect.fiAddAmt.toLocaleString() : "0");

        if(wares.currentFrInspect.frPhotoList) {
            if(wares.currentFrInspect.frPhotoList.length) {
                $("#frInspectViewPhotoPanel").show();
            } else {
                $("#frInspectViewPhotoPanel").hide();
            }
            for (const photo of wares.currentFrInspect.frPhotoList) {
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
        // fiCustomerConfirm 에 따른 수락거부 여부 추가?
        $("#frInspectViewPop").addClass("active");
    }
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

    if(wares.currentBrInspect.brPhotoList) {
        if(wares.currentBrInspect.brPhotoList.length) {
            $("#brInspectPhotoPanel").show();
        } else {
            $("#brInspectPhotoPanel").hide();
        }
        for (const photo of wares.currentBrInspect.brPhotoList) {
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

    if(wares.currentBrInspect.brFiCustomerConfirm === "1" && wares.currentBrInspect.fdCancel === "N") {
        $("#brCustomerDenied").parents("li").show();
        $("#brCustomerConfirmed").parents("li").show();
        $("#brSendKakaoMsg").parents("li").show();
    } else {
        $("#brCustomerDenied").parents("li").hide();
        $("#brCustomerConfirmed").parents("li").hide();
        $("#brSendKakaoMsg").parents("li").hide();
    }

    // fiCustomerConfirm 에 따른 수락거부 여부 추가?
    $("#brInspectPop").addClass("active");
}

function takePhoto() {
    if($("#frPhotoList").children().length > 5) {
        alertCaution("사진은 최대 6장 까지 촬영하실 수 있습니다.", 1);
        return false;
    }

    if(wares.isCameraExist && wares.cameraStream.active) {
        try {
            const video = document.getElementById("cameraScreen");
            const canvas = document.getElementById('cameraCanvas');
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            const context = canvas.getContext('2d');
            context.drawImage(video, 0, 0, canvas.width, canvas.height);

            const takenPic = canvas.toDataURL();
            const blob = b64toBlob(takenPic);

            if (!blob.size) {
                return;
            }

            if(!wares.currentFrInspect.addPhotoList) {
                wares.currentFrInspect.addPhotoList = [];
            }
            const photoHtml = `
            <li class="motherLi newPhoto">
                <a href="${takenPic}" data-lightbox="images" data-title="이미지 확대">
                    <img src="${takenPic}" alt=""/>
                </a>
                <button class="checkreg__delete deletePhotoBtn" data-addIdx="${wares.currentFrInspect.addPhotoList.length}">삭제</button>
            </li>
            `;

            wares.currentFrInspect.addPhotoList.push(blob);

            $("#frPhotoList").append(photoHtml);
        } catch (e) {
            CommonUI.toppos.underTaker(e, "tagboard : 사진 촬영");
        }
    } else {
        alertCaution("카메라가 감지되지 않아서<br>촬영을 할 수 없습니다.", 1);
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

/* 브라우저의 get 파라미터들을 가져오고 그에 따른 작업을 반영하기 위해 */
function getParamsAndAction() {
    const url = new URL(window.location.href);
    wares.params = url.searchParams;

    if(wares.params.has("fdTag") && wares.params.has("frYyyymmdd")) {
        const dateNum = wares.params.get("frYyyymmdd");
        const date = dateNum.substring(0, 4) + "-" + dateNum.substring(4, 6) + "-" + dateNum.substring(6, 8);
        $("#searchType").val("4");
        $("#searchString").val(wares.params.get("fdTag"));
        $("#filterFromDt").val(date);
        $("#filterToDt").val(date);
        $("#searchCustomer").trigger("click");
    } else if (wares.params.has("bchp")) {
        const bcHp = wares.params.get("bchp");
        $("#searchType").val("2");
        $("#searchString").val(bcHp);
        searchCustomer();
    } else if (wares.params.has("week")) {
        $("#changeDateRange").val("6");
        changeSearchPeriod("6");
        // filterMain(); // 바로 검색 되도록
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
    if(tagNo) {
        $tranferPoint.hide();
        $refundPayment.hide();
        $collectedTagNo.html(CommonData.formatFrTagNo(tagNo, frTagInfo.frTagType));
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
    judgeEtcNoneOnOff();
    $("#fdRepairAmt").val(0);
    $("#fdRepairRemark").val("");
}

function resetFdAddInputs() {
    currentRequest.fdAdd1Amt = 0;
    currentRequest.fdAdd1Remark = "";
    $("#fdAdd1").prop("checked", false);
    judgeEtcNoneOnOff();
    $("#fdAdd1Amt").val(0);
    $("#fdAdd1Remark").val("");
}

function closeFrInspectEditPop(doRefresh = false) {
    if(wares.isCameraExist) {
        try {
            wares.cameraStream.getTracks().forEach(function (track) {
                track.stop();
            });
        }catch (e) {
            CommonUI.toppos.underTaker(e, "integrate : 카메라 스트림 트랙 감지하여 취소");
        }
    }
    if(doRefresh) {
        comms.franchiseRequestDetailSearch(wares.currentCondition);
    }
    $("#frInspectEditPop").removeClass("active");
    resetFrInspectEditPop();
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

function resetFrInspectEditPop() {
    $("#frEditFdTotAmtInPut").val("");
    $("#frEditFiAddAmt").val("");
    $("#frEditFiComment").val("");
    $("#frPhotoList").html("");
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

function saveInspect() {
    const formData = new FormData();
    if(wares.currentFrInspect.addPhotoList) {
        for(const addPhoto of wares.currentFrInspect.addPhotoList) { // 새로 촬영된 사진들의 추가
            if(addPhoto) formData.append("addPhotoList", addPhoto);
        }
    }

    formData.append("fdId", wares.currentFrInspect.fdId);
    if(wares.currentFrInspect.frFiId) {
        formData.append("fiId", wares.currentFrInspect.frFiId);
    }

    if(wares.currentFrInspect.deletePhotoList) {
        formData.append("deletePhotoList", wares.currentFrInspect.deletePhotoList);
    }

    formData.append("fiComment", $("#frEditFiComment").val());
    formData.append("fiAddAmt", $("#frEditFiAddAmt").val().toInt());
    formData.append("fiType", "F");

    comms.putNewInspectNeo(formData);
}

function addDistinguishableCellColor() {
    const items = AUIGrid.getGridData(grids.s.id[0]);
    let nowId = 0;
    let colorIdx = 0;

    for (const item of items) {
        if (item.frId !== nowId) {
            colorIdx++;
            if (colorIdx === 2) colorIdx = 0;
            nowId = item.frId;
        }
        item.cellColor = colorIdx;
    }

    AUIGrid.updateRowsById(grids.s.id[0], items);
    AUIGrid.refresh(grids.s.id[0]);
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

function sendInspectMessage(sender) {
    let data = {};
    if (sender === "frSendKakaoMsg") {
        data = {
            isIncludeImg: "Y",
            fmMessage: "",
            fiId: wares.currentFrInspect.frFiId,
            bcId: wares.currentFrInspect.inspeotInfoDto.bcId,
        }
    } else if (sender === "brSendKakaoMsg") {
        data = {
            isIncludeImg: "Y",
            fmMessage: "",
            fiId: wares.currentBrInspect.brFiId,
            bcId: wares.currentBrInspect.inspeotInfoDto.bcId,
        }
    }
    comms.sendKakaoMessage(data);
}

function changeSearchPeriod(period) {
    if(period) {
        let fromday = new Date();
        fromday.setDate(fromday.getDate() - period.toInt());
        fromday = fromday.format("yyyy-MM-dd");
        const today = new Date().format("yyyy-MM-dd");

        $("#filterFromDt").val(fromday);
        $("#filterToDt").val(today);
    }
}

function formatDateTime(value) {
    let result = "";
    if(typeof value === "number") {
        result = new Date(value).format("yy.MM.dd<br>HH:mm");
    }
    return result;
}

function formatDateTimeForXlsx(value) {
    let result = "";
    if(typeof value === "number") {
        result = new Date(value).format("yy.MM.dd HH:mm");
    }
    return result;
}

/* 수정 전 총 금액 보다 수정 후 총 금액이 커졌을 경우 기록하기 위함. */
function checkIncreaseAmt() {
    if (currentRequest.fdTotAmt > wares.startPrice) {
        currentRequest.fdModifyAmtYn = 'Y';
        currentRequest.fdModifyOriginalAmt = wares.startPrice;
    } else {
        currentRequest.fdModifyAmtYn = 'N';
    }
}

function cancelCashReceipt() {
    const paymentData = grids.f.getSelectedItem(3);
    if (paymentData) {
        delete paymentData["_$uid"];
        paymentData.fcCatApprovalno = paymentData.fcCatApprovalno.substring(0, 9);
        alertCheck("선택하신 현금 영수증을 취소 하시겠습니까?");
        $('#checkDelSuccessBtn').on('click', function () {
            CommonUI.toppos.cancelCashReceipt(paymentData, function () {
                grids.f.clearGrid(3);
                $("#cancelCashReceipt").hide();
            });
            $('#popupId').remove();
        });
    } else {
        alertCaution("취소하실 현금영수증을 선택해 주세요.", 1);
    }
}

/* 수기행사 할인 퍼센트 유효성 검사동작 */
function validateManualDiscountPercent() {
    const $manualDiscountPercent = $("#manualDiscountPercent");
    let value = $manualDiscountPercent.val().positiveNumberInput();
    if (value && value > 100) {
        value = 100;
    }
    $manualDiscountPercent.val(value);

    calculateItemPrice();
}

/* 처리내역 버튼 동작에 따라 없음 버튼에 불이 들어올지 꺼져야 할지를 판단하여 조치한다. */
function judgeEtcNoneOnOff() {
    const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
    if(!$("#processCheck input[type='checkbox']:checked").length && !$isEtcProcessChecked) {
        $("#etcNone").prop("checked", true);
    }else if($("#etcNone").is(":checked") || $isEtcProcessChecked) {
        $("#etcNone").prop("checked", false);
    }
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
    for (const item of items) {
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