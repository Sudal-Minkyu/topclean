$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dto = {
    send: {
        saveModifiedOrder: {
            bcName: "",
            frYyyymmdd: "",
            fdId: "",
            frId: "",
            fdTag: "d",
            biItemcode: "d",
            fdState: "d",
            fdS2Dt: "d",
            fdS3Dt: "d",
            fdS4Dt: "d",
            fdS5Dt: "d",
            fdS6Dt: "d",
            fdCancel: "",
            fdCacelDt: "",
            fdColor: "",
            fdPattern: "",
            fdPriceGrade: "",
            fdOriginAmt: "",
            fdNormalAmt: "",
            fdAdd2Amt: "",
            fdAdd2Remark: "",
            fdPollution: "",
            fdDiscountGrade: "",
            fdDiscountAmt: "",
            fdQty: "",
            fdRequestAmt: "",
            fdSpecialYn: "",
            fdTotAmt: "",
            fdRemark: "",
            fdEstimateDt: "",
            fdRetryYn: "",
            fdPressed: "",
            fdAdd1Amt: "",
            fdAdd1Remark: "",
            fdRepairAmt: "",
            fdRepairRemark: "",
            fdWhitening: "",
            fdPollutionLevel: "",
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
            totAddCost: "",
            frEstimateDate: "",
            urgent: "",


        },
        고객검색: {
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소
            searchString: "sr", // 검색문자
        },

        franchiseRequestDetailSearch: {
            bcId: "n", // 선택된 고객. 없을 경우 null
            searchTag: "s", // 택번호 검색문자
            filterCondition: "s", // "" 전체, "S1" 고객접수, "S2" 자사입고, "S3" 가맹점입고, "S4" 입고완료, "B" 확인품, "F" 가맹검품
            filterFromDt: "sr", // 시작 조회기간
            filterToDt: "sr", // 종료 조회기간
        },

        customerInfo: {
            searchType: "sr",
            searchString: "sr",
        }
    },
    receive: {
        고객검색: { // 접수 페이지의 고객 정보 가져오는 부분과 동일
            bcId: "n",
            bcName: "s",
            bcGrade: "s",
            bcValuation: "s",
            bcAddress: "s",
            bcHp: "s",
            beforeUncollectMoney: "n",
            saveMoney: "n",
            bcRemark: "s",
            bcLastRequestDt: "s",
        },

        franchiseRequestDetailSearch: { // 접수 세부 테이블의 거의 모든 요소와, 고객이름
            bcName: "s", // 고객의 이름
            frYyyymmdd: "s", // 접수일자
            fdId: "n",
            frId: "n",
            fdTag: "s",
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
            fdAdd2Amt: "n",
            fdAdd2Remark: "s",
            fdPollution: "n",
            fdDiscountGrade: "s",
            fdDiscountAmt: "n",
            fdQty: "n",
            fdRequestAmt: "n",
            fdSpecialYn: "s",
            fdTotAmt: "n",
            fdRemark: "s",
            fdEstimateDt: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "n",
            fdPollutionLevel: "n",
            fdWaterRepellent: "n",
            fdStarch: "n",
        },

        검품정보: { // 검품 내역과 검품사진을 불러온다. 아직 작업 x
            ffPath: "s",
            ffFilename: "s",
            ffRemark: "s",

            fiId: "n",
            fdId: "n",
            frCode: "d",
            brCode: "d",
            fiType: "s",
            fiComment: "s",
            fiAddAmt: "n",
            fiPhotoYn: "s",
            fiSendMsgYn: "s",
            fiCustomerConfirm: "s",
            fiProgressStateDt: "s",
            fiMessage: "s",
            fiMessageSendDt: "s",
            modifyId: "d",
            modifyDt: "d",
            insertId: "d",
            insertDt: "d",
        },

        customerInfo: {
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
                fdTag: "s",
                frBusinessNo: "s",
                frCode: "s",
                frEstimateDate: "s",
                frName: "s",
                frRpreName: "s",
                frTelNo: "s",
            },
            repairListData: {
                baId: "n",
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
const ajax = {
    getInitialData() {
        CommonUI.ajax("/api/user/itemGroupAndPriceList", "GET", false, function (req){
            data.initialData = req.sendData;
            dv.chk(data.initialData, dto.receive.itemGroupAndPriceList, "초기검사");
            console.log(data.initialData);
        });
    },
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function (req) {
            grid.s.data[numOfGrid] = req.sendData.gridListData;
            grid.f.setData(numOfGrid, grid.s.data[numOfGrid]);
        });
    },
    searchCustomer(params) {
        dv.chk(params, dto.send.customerInfo, "고객검색조건 보내기");
        CommonUI.ajax(grid.s.url.read[1], "GET", params, function (res) {
            const items = res.sendData.gridListData;
            dv.chk(items, dto.receive.customerInfo);
            $("#searchCustomerType").val(0);
            $("#searchString").val("");
            console.log(items);
            if(items.length === 1) {
                data.selectedCustomer = items[0];
                putCustomer(data.selectedCustomer);
                delete data.initialData.etcData["frNo"];
            }else if(items.length > 1) {
                grid.f.setData(1, items);
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
        dv.chk(condition, dto.send.franchiseRequestDetailSearch, "메인그리드 검색 조건 보내기");
        console.log(condition);
        CommonUI.ajaxjsonPost(grid.s.url.read[0], condition, function(res) {
            const gridData = res.sendData.gridListData;
            dv.chk(gridData, dto.receive.franchiseRequestDetailSearch, "메인그리드 조회된 리스트");
            grid.f.setData(0, gridData);
        });
    },
    saveModifiedOrder(data) {
        dv.chk(data, dto.send.saveModifiedOrder, "상품 수정내용 저장");
        console.log(data);
        // const url = "/api/user/";
        // CommonUI.ajaxjsonPost(url, data, function(res) {
        //
        // onCloseAddOrder();
        // });
    }
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grid = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_main", "grid_customerList"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/", "/api/"
            ],
            read: [
                "/api/user/franchiseRequestDetailSearch", "/api/user/customerInfo"
            ],
            update: [
                "/api/", "/api/"
            ],
            delete: [
                "/api/", "/api/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grid.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grid.s.columnLayout[0] = [
                {
                    dataField: "type",
                    headerText: "구분",
                    width: 40,
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
                        return value.substr(0, 3) + "-" + value.substr(-4);
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
                            `<span class="colorSquare" style="background-color: ${data.fdColorCode['C'+item.fdColor]}"></span>`;
                        CommonUI.toppos.makeProductName(item, data.initialData.userItemPriceSortData);
                        return colorSquare + ` <span>` + item.sumName + `</span>`;
                    }
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    width: 80,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    }
                }, {
                    dataField: "fdRequestAmt",
                    headerText: "접수금액",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 85,
                }, {
                    dataField: "urgent", // 급세탁이면 Y
                    headerText: "급",
                    width: 35,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return value === "급세탁" ? "Y" : "";
                    }
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
                }, {
                    dataField: "blueBtn1",
                    headerText: "가맹검품",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = "";
                        template = `
                            <button class="c-state">등록</button>
                        `;
                        item.blueBtn1 = true;
                        item.blueBtn1 = false;
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
                        template = `
                            <button class="c-state">확인</button>
                        `;
                        item.blueBtn2 = true;
                        item.blueBtn2 = false;
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
                        template = `
                            <button class="c-state c-state--modify">수정</button>
                        `;
                        item.greenBtn1 = true;
                        item.greenBtn1 = false;
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
                        template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        item.redBtn1 = true;
                        item.redBtn1 = false;
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
                        template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        item.redBtn2 = true;
                        item.redBtn2 = false;
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
                        template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        item.redBtn3 = true;
                        item.redBtn3 = false;
                        return template;
                    },
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grid.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grid.s.columnLayout[1] = [
                {
                    dataField: "bcName",
                    headerText: "고객명",
                }, {
                    dataField: "bcHp",
                    headerText: "전화번호",
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.onPhoneNumChange(value);
                    }
                }, {
                    dataField: "bcAddress",
                    headerText: "주소",
                },
            ];

            grid.s.prop[1] = {
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
        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grid.s.columnLayout) {
                grid.s.id[i] = AUIGrid.create(grid.s.targetDiv[i], grid.s.columnLayout[i], grid.s.prop[i]);
            }
        },

        setData(numOfGrid, data) {
            AUIGrid.setGridData(grid.s.id[numOfGrid], data);
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            ajax.setDataIntoGrid(numOfGrid, grid.s.read[numOfGrid]);
        },

        switchModifyMode(isModifyMode) {
            const modColumn = {
                modify: ["blueBtn1", "blueBtn2", "greenBtn1", "redBtn1", "redBtn2", "redBtn3"],
                normal: ["type", "fdS5Dt", "fdRemark", "fdS2Dt", "fdS3Dt", "fdS4Dt"],
            }

            if(isModifyMode) {
                AUIGrid.showColumnByDataField(grid.s.id[0], modColumn.modify);
                AUIGrid.hideColumnByDataField(grid.s.id[0], modColumn.normal);
            }else{
                AUIGrid.showColumnByDataField(grid.s.id[0], modColumn.normal);
                AUIGrid.hideColumnByDataField(grid.s.id[0], modColumn.modify);
            }
        },

        getSelectedCustomer() {
            data.selectedCustomer = AUIGrid.getSelectedRows(grid.s.id[1])[0];
        },

        clearGrid(numOfGrid) {
            AUIGrid.clearGridData(grid.s.id[numOfGrid]);
        }
    },

    e: {
        basicEvent() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grid.s.id[0], "cellClick", function (e) {
                console.log(e); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
                switch (e.dataField) {
                    case "blueBtn1":
                        // 가맹점 검품등록창 진입
                        putInspect();
                        break;
                    case "blueBtn2":
                        confirmInspect();
                        // 검품확인창 진입
                        break;
                    case "greenBtn1":
                        // 수정모드 진입
                        modifyOrder(e.rowIndex);
                        break;
                    case "redBtn1":
                        // 확인후 ajax 호출하며 그리드에서 삭제
                        break;
                    case "redBtn2":
                        cancelPayment();
                        // 결제취소창 진입
                        break;
                    case "redBtn3":
                        // 확인후 가맹점 입고로 상태변경하여 ajax 호출
                        break;
                }
            });
        }
    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const data = {
    initialData: {},
    selectedCustomer: {
        bcId: null,
    },
    currentRequest: {},
    selectedLaundry: {},
    fdColorCode: { // 컬러코드에 따른 실제 색상
        C00: "#D4D9E1", C01: "#D4D9E1", C02: "#3F3C32", C03: "#D7D7D7", C04: "#F54E50", C05: "#FB874B",
        C06: "#F1CE32", C07: "#349A50", C08: "#55CAB7", C09: "#398BE0", C10: "#DE9ACE", C11: "#FF9FB0",
    },
}

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grid.e에 위치 */
const event = {
    s: { // 이벤트 설정
        vkeys() {
            $("#vkeyboard0").on("click", function() {
                onShowVKeyboard(0);
            });
        },
        main() {
            $("#searchCustomer").on("click", function() {
                mainSearch();
            });
            $("#selectCustomer").on("click", function() {
                grid.f.getSelectedCustomer();
                selectCustomerFromList();
            });
            $("#resetCustomer").on("click", function() {
                data.selectedCustomer = {
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
                grid.f.switchModifyMode(true);
                $("#modifyOff").show();
                $("#modifyOn").hide();
            });
            $("#modifyOff").on("click", function() {
                grid.f.switchModifyMode(false);
                $("#modifyOn").show();
                $("#modifyOff").hide();
            });
            $("#searchString").on("keypress", function(e) {
                if(e.originalEvent.code === "Enter") {
                    searchCustomer();
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

            const $processInput = $("#processCheck input[type='checkbox'], #processCheck input[type='radio']");
            $processInput.on("change", function(e) {
                const $isEtcProcessChecked = $(".choice-drop__btn.etcProcess.choice-drop__btn--active").length;
                if(!$("#processCheck input[type='checkbox']:checked").length && !$isEtcProcessChecked) {
                    $processInput.first().prop("checked", true);
                }else if($processInput.first().is(":checked") || $isEtcProcessChecked) {
                    $processInput.first().prop("checked", false);
                }
                const targetId = e.target.id;
                calculateItemPrice();
            });

            $("#fdAdd1").on("click", function () {
                $("#fdAddPop").addClass("active");
                enableKeypad();
            });

            $("#fdRepairCancel").on("click", function () {
                data.currentRequest.fdRepairAmt = 0;
                data.currentRequest.fdRepairRemark = "";
                $("#fdRepair").prop("checked", false);
                $("#fdRepairAmt").val(0);
                $("#fdRepairRemark").val("");
                calculateItemPrice();
                disableKeypad();
            });

            $("#fdRepairComplete").on("click", function () {
                data.currentRequest.fdRepairAmt = parseInt($("#fdRepairAmt").val().replace(/[^0-9]/g, ""));
                data.currentRequest.fdRepairRemark = $("#fdRepairRemark").val();
                if(data.currentRequest.fdRepairAmt || data.currentRequest.fdRepairRemark.length) {
                    $("#fdRepair").prop("checked", true);
                }else{
                    $("#fdRepair").prop("checked", false);
                }
                calculateItemPrice();
                disableKeypad();
            });

            $("#fdAddCancel").on("click", function () {
                data.currentRequest.fdAdd1Amt = 0;
                data.currentRequest.fdAdd1Remark = "";
                $("#fdAdd1").prop("checked", false);
                $("#fdAdd1Amt").val(0);
                $("#fdAdd1Remark").val("");
                calculateItemPrice();
                disableKeypad();
            });

            $("#fdAddComplete").on("click", function () {
                data.currentRequest.fdAdd1Amt = parseInt($("#fdAdd1Amt").val().replace(/[^0-9]/g, ""));
                data.currentRequest.fdAdd1Remark = $("#fdAdd1Remark").val();
                if(data.currentRequest.fdAdd1Amt || data.currentRequest.fdAdd1Remark.length) {
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
        }
    },
    r: { // 이벤트 해제

    }
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    ajax.getInitialData();

    enableDatepicker();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    grid.f.initialization();
    grid.f.create();
    grid.f.switchModifyMode(false);
    grid.e.basicEvent();

    event.s.main();
    event.s.vkeys();
    event.s.modify();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    // grid.f.setInitialData(0);

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grid.e.basicEvent();
}

function modifyOrder(rowIndex) {

    data.currentRequest = AUIGrid.getItemByRowIndex(grid.s.id[0], rowIndex);
    data.currentRequest.sumName = undefined;
    data.selectedLaundry.bgCode = data.currentRequest.biItemcode.substr(0, 3);

    let bsType = ["N", "L", "S"];
    data.initialData.userItemGroupSListData.forEach(el => {
        for(let i = 0; i < bsType.length; i++) {
            if(el.bgItemGroupcode === data.selectedLaundry.bgCode && el.bsItemGroupcodeS === bsType[i]) {
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

    setBiItemList(data.currentRequest.biItemcode.substr(3, 1));

    data.selectedLaundry.bgCode = data.currentRequest.biItemcode.substr(0, 3);
    data.selectedLaundry.bsCode = data.currentRequest.biItemcode.substr(3, 1);
    $("input[name='bsItemGroupcodeS']:input[value='" + data.selectedLaundry.bsCode + "']").prop("checked", true);
    $("#" + data.currentRequest.biItemcode).prop("checked", true);
    $(".choice-color__input[value='" + data.currentRequest.fdColor + "']").prop("checked", true);
    $("input[name='fdPattern']:input[value='" + data.currentRequest.fdPattern +"']").prop("checked", true);
    $("input[name='fdPriceGrade']:input[value='" + data.currentRequest.fdPriceGrade +"']").prop("checked", true);
    $("input[name='fdDiscountGrade']:input[value='" + data.currentRequest.fdDiscountGrade +"']").prop("checked", true);
    $("input[name='urgent']:input[value='" + data.currentRequest.urgent +"']").prop("checked", true);

    if(data.currentRequest.fdPressed) {
        $("#fdPress").prop("checked", true);
    }
    if(data.currentRequest.fdRetryYn === "Y") {
        $("#fdRetry").prop("checked", true);
    }

    if(data.currentRequest.fdRepairRemark.length || data.currentRequest.fdRepairAmt) {
        $("#fdRepair").prop("checked", true);
        $("#fdRepairAmt").val(data.currentRequest.fdRepairAmt);
        $("#fdRepairRemark").val(data.currentRequest.fdRepairRemark);
    }

    if(data.currentRequest.fdAdd1Remark.length || data.currentRequest.fdAdd1Amt) {
        $("#fdAdd1").prop("checked", true);
        $("#fdAdd1Amt").val(data.currentRequest.fdAdd1Amt);
        $("#fdAdd1Remark").val(data.currentRequest.fdAdd1Remark);
    }

    if(data.currentRequest.fdSpecialYn === "Y") {
        $("#fdSpecialYn").prop("checked", true);
    }else{
        $("#fdSpecialYn").prop("checked", false);
    }

    if(data.currentRequest.fdWhitening) {
        $("#fdWhitening").prop("checked", true);
    }

    $("input[name='cleanDirt']:input[value='" + data.currentRequest.fdPollutionLevel +"']").prop("checked", true);
    if($("#dirt0").is(":checked")) {
        $("#pollutionBtn").removeClass("choice-drop__btn--active");
    }else{
        $("#pollutionBtn").addClass("choice-drop__btn--active");
    }

    if(data.currentRequest.fdWaterRepellent) {
        $("#fdWaterRepellent").prop("checked", true);
    }
    if(data.currentRequest.fdStarch) {
        $("#fdStarch").prop("checked", true);
    }
    if($("#waterNone").is(":checked")) {
        $("#waterBtn").removeClass("choice-drop__btn--active");
    }else{
        $("#waterBtn").addClass("choice-drop__btn--active");
    }



    if(data.currentRequest.fdRemark.length) {
        $("#fdRemark").val(data.currentRequest.fdRemark);
    }

    if($("#processCheck input:checked").length > 3 || $("#processCheck .choice-drop__btn--active").length) {
        $("#etcNone").prop("checked", false);
    }

    /* data.currentRequest의 각 벨류값에 따라 화면의 라디오 세팅을 구성한다. */

    calculateItemPrice();

    $('#productPop').addClass('active');
}

function calculateItemPrice() {
    const ap = data.initialData.addCostData;
    const gradePrice = [100, 100, ap.bcHighRt, ap.bcPremiumRt, ap.bcChildRt];
    const gradeDiscount = [0, 0, ap.bcVipDcRt, ap.bcVvipDcRt];
    data.currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    data.currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();

    data.currentRequest.fdPressed = $("#fdPress").is(":checked") ?
        parseInt(data.initialData.addCostData.bcPressed) : 0;
    data.currentRequest.fdWhitening = $("#fdWhitening").is(":checked") ?
        parseInt(data.initialData.addCostData.bcWhitening) : 0;
    data.currentRequest.fdWaterRepellent = $("#fdWaterRepellent").is(":checked") ?
        parseInt(data.initialData.addCostData.bcWaterRepellent) : 0;
    data.currentRequest.fdStarch = $("#fdStarch").is(":checked") ?
        parseInt(data.initialData.addCostData.bcStarch) : 0;
    data.currentRequest.fdPollutionLevel = $("input[name='cleanDirt']:checked").first().val() | 0;
    data.currentRequest.fdPollution = parseInt(data.initialData.addCostData["bcPollution"+data.currentRequest.fdPollutionLevel]) | 0;

    data.currentRequest.fdRepairAmt = ceil100(data.currentRequest.fdRepairAmt);
    data.currentRequest.fdAdd1Amt = ceil100(data.currentRequest.fdAdd1Amt);

    data.currentRequest.totAddCost = data.currentRequest.fdPressed + data.currentRequest.fdWhitening + data.currentRequest.fdWaterRepellent
        + data.currentRequest.fdStarch + data.currentRequest.fdPollution + data.currentRequest.fdAdd1Amt + data.currentRequest.fdRepairAmt;

    data.currentRequest.fdNormalAmt = ceil100(data.currentRequest.fdOriginAmt * gradePrice[data.currentRequest.fdPriceGrade] / 100);
    let sumAmt = ceil100((data.currentRequest.fdNormalAmt + data.currentRequest.totAddCost)
        * (100 - gradeDiscount[data.currentRequest.fdDiscountGrade]) / 100)
    data.currentRequest.fdRequestAmt = sumAmt * data.currentRequest.fdQty;
    data.currentRequest.fdDiscountAmt = data.currentRequest.fdNormalAmt + data.currentRequest.totAddCost - sumAmt;

    if($("#fdRetry").is(":checked")) {
        data.currentRequest.fdRetryYn = "Y";
        data.currentRequest.fdNormalAmt = 0;
        data.currentRequest.totAddCost = 0;
        data.currentRequest.fdDiscountAmt = 0;
        data.currentRequest.fdRequestAmt = 0;
        sumAmt = 0;
    }else{
        data.currentRequest.fdRetryYn = "N";
    }

    $("#fdNormalAmt").html(data.currentRequest.fdNormalAmt.toLocaleString());
    $("#totAddCost").html(data.currentRequest.totAddCost.toLocaleString());
    $("#fdDiscountAmt").html(data.currentRequest.fdDiscountAmt.toLocaleString());
    $("#sumAmt").html(sumAmt.toLocaleString());
}

function ceil100(num) {
    num = num.toString();
    let ceilAmount = 0;
    if(num.length >= 2 && num.substr(num.length - 2, 2) !== "00") {
        num = num.substr(0, num.length - 2) + "00";
        ceilAmount = 100;
    }
    return parseInt(num) + ceilAmount;
}

function setBiItemList(bsCode) {
    data.selectedLaundry.bsCode = bsCode;

    const $biItemList = $("#biItemList");
    $biItemList.html("");

    data.initialData.userItemPriceSortData.forEach(el => {
        if(el.biItemcode.substr(0,4) === data.selectedLaundry.bgCode + bsCode) {
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
    data.currentRequest = {};
    $("input[name='bsItemGroupcodeS']").first().prop("checked", true);
    $("input[name='fdColor']").first().prop("checked", true);
    $("input[name='fdPattern']").first().prop("checked", true);
    $("input[name='fdPriceGrade']").first().prop("checked", true);
    $("input[name='fdDiscountGrade']").first().prop("checked", true);

    $("input[name='material']").first().prop("checked", true);
    $("input[name='urgent']").first().prop("checked", true);
    $(".choice input[type='checkbox']").prop("checked", false);
    $("input[name='cleanDirt']").first().prop("checked", true);
    $("input[name='waterProcess']").first().prop("checked", true);
    $(".choice-drop__btn.etcProcess").removeClass('choice-drop__btn--active');

    $(".keypad_remark").val("");
    $(".keypad_field").val(0);

    $("input[name='etcNone']").first().prop("checked", true);
    $("#fdRemark").val("");

    $("#addProductPopChild").parents('.pop').removeClass('active');
}

function onAddOrder() {
    if(!data.currentRequest.biItemcode.length) {
        alertCaution("소재를 선택해 주세요", 1);
        return false;
    }

    /*
    const colorName = {
        C00: "없음", C01: "흰색", C02: "검정", C03: "회색", C04: "빨강", C05: "주황",
        C06: "노랑", C07: "초록", C08: "파랑", C09: "남색", C10: "보라", C11: "핑크"
    }
    */

    data.currentRequest.fdColor = $("input[name='fdColor']:checked").val();
    //item.fdColorName = colorName["C" + item.fdColor];
    data.currentRequest.fdPattern = $("input[name='fdPattern']:checked").val();
    data.currentRequest.fdPriceGrade = $("input[name='fdPriceGrade']:checked").val();
    data.currentRequest.fdDiscountGrade = $("input[name='fdDiscountGrade']:checked").val();
    data.currentRequest.fdRemark = $("#fdRemark").val();
    data.currentRequest.frEstimateDate = data.initialData.etcData.frEstimateDate.replace(/[^0-9]/g, "");
    data.currentRequest.fdSpecialYn = $("#fdSpecialYn").is(":checked") ? "Y" : "N";

    /* 상세한 사항이 정해지면 수정할 것 */
    data.currentRequest.urgent = $("input[name='urgent']:checked").val();

    AUIGrid.updateRowsById(grid.s.id[0], data.currentRequest);
    ajax.saveModifiedOrder(data.currentRequest);
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
    const vkeyTargetId = ["searchString"];

    vkeyProp[0] = {
        title: $("#searchType option:selected").html() + " (검색)",
        callback: mainSearch,
    }

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

    ajax.searchCustomer(params);
}

function selectCustomerFromList() {
    if(data.selectedCustomer) {
        putCustomer(data.selectedCustomer);
        delete data.initialData.etcData["frNo"];
        $("#customerListPop").removeClass("active");
    }else{
        alertCaution("고객을 선택해 주세요", 1);
    }
}

function filterMain() {
    const condition = CommonUI.newDto(dto.send.franchiseRequestDetailSearch);
    condition.bcId = data.selectedCustomer.bcId;
    condition.filterCondition = $("input[name='filterCondition']:checked").val();
    condition.filterToDt = $("#filterFromDt").val().replace(/[^0-9]/g, "");
    condition.filterFromDt = $("#filterToDt").val().replace(/[^0-9]/g, "");
    if($("#searchType").val() === "4") {
        condition.searchTag = $("#searchString").val();
    }
    console.log(condition);
    ajax.franchiseRequestDetailSearch(condition);
}

function putCustomer() {
    let bcGradeName = "";
    $(".client__badge").removeClass("active");
    switch (data.selectedCustomer.bcGrade) {
        case "01" :
            $(".client__badge:nth-child(1)").addClass("active");
            bcGradeName = "일반";
            break;
        case "02" :
            $(".client__badge:nth-child(2)").addClass("active");
            bcGradeName = "VIP";
            break;
        case "03" :
            $(".client__badge:nth-child(3)").addClass("active");
            bcGradeName = "VVIP";
            break;
    }
    $("#bcGrade").html(bcGradeName);

    if(data.selectedCustomer.bcName) {
        $("#bcName").html(data.selectedCustomer.bcName + "님");
    }else{
        $("#bcName").html("");
    }

    if(data.selectedCustomer.bcValuation) {
        $("#bcValuation").attr("class",
            "propensity__star propensity__star--" + data.selectedCustomer.bcValuation)
            .css('display','block');
    }else{
        $("#bcValuation").css('display','none');
    }

    $("#bcAddress").html(data.selectedCustomer.bcAddress);
    $("#bcHp").html(CommonUI.onPhoneNumChange(data.selectedCustomer.bcHp));
    $("#beforeUncollectMoneyMain").html(data.selectedCustomer.beforeUncollectMoney.toLocaleString());
    $("#saveMoneyMain").html(data.selectedCustomer.saveMoney.toLocaleString());
    $("#bcRemark").html(data.selectedCustomer.bcRemark);
    if(data.selectedCustomer.bcLastRequestDt) {
        $("#bcLastRequestDt").html(
            data.selectedCustomer.bcLastRequestDt.substr(0, 4) + "-"
            + data.selectedCustomer.bcLastRequestDt.substr(4, 2) + "-"
            + data.selectedCustomer.bcLastRequestDt.substr(6, 2)
        );
    }else if(data.selectedCustomer.bcId){
        $("#bcLastRequestDt").html("없음");
    }else{
        $("#bcLastRequestDt").html("");
    }

    // 상품수정기능 도입까지 주석
    // $("#class02, #class03").parents("li").css("display", "none");
    // $("#class" + data.selectedCustomer.bcGrade).parents("li").css("display", "block");
    grid.f.clearGrid(0);
}

function onSelectBiItem(biCode, price) {
    data.currentRequest.biItemcode = biCode;
    data.currentRequest.fdOriginAmt = price;
    calculateItemPrice();
}

function enableKeypad() {
    const $keypadBtn = $(".add-cost .keypad_btn");
    const $keypadBackspace = $(".add-cost .keypad_btn_backspace");
    const $keypadBoilerplate = $(".add-cost .add-cost__example-btn");

    $keypadBtn.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost__keypad").find(".keypad_field");
        $keypad_field.val(parseInt($keypad_field.val().replace(/[^0-9]/g, "") + this.value).toLocaleString());
    });

    $keypadBackspace.on("click", function (e) {
        const $keypad_field = $(this).parents(".add-cost__keypad").find(".keypad_field");
        const currentValue = $keypad_field.val().replace(/[^0-9]/g, "");
        if(currentValue.length > 1) {
            $keypad_field.val(parseInt(currentValue.substr(0,
                currentValue.replace(/[^0-9]/g, "").length - 1)).toLocaleString())
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

function putInspect() {

}

function confirmInspect() {

}

function cancelPayment() {

}