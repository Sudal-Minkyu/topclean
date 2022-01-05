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
            biItemcode: "s",
            fdState: "s",
            fdS2Dt: "s",
            fdS3Dt: "s",
            fdS4Dt: "s",
            fdS5Dt: "s",
            fdCancel: "s",
            fdCancelDt: "s",
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
        CommonUI.ajaxjsonPost(grid.s.url.read[0], condition, function(res) {
            console.log(res);
        });
    },
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
                }, {
                    dataField: "bcName",
                    headerText: "고객명",
                }, {
                    dataField: "frYyyymmdd",
                    headerText: "접수일자",
                }, {
                    dataField: "fdS5Dt",
                    headerText: "인도일자",
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                }, {
                    dataField: "sumName",
                    headerText: "상품명",
                    style: "colorColumn",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<div class="colorSquare" style="background-color: ${data.fdColorCode['C'+item.fdColor]}"></div>`;
                        if(!item.sumName) {
                            const nameArray = data.initialData.userItemPriceSortData;
                            const isNotSizeNormal = !(item.biItemcode.substr(3, 1) === "N");
                            let sumName = "";
                            for(let i = 0; i < nameArray.length; i++) {
                                if(nameArray[i].biItemcode === item.biItemcode) {
                                    if(isNotSizeNormal) {
                                        sumName += nameArray[i].bsName + " ";
                                    }
                                    sumName += nameArray[i].biName + " " + nameArray[i].bgName;
                                    break;
                                }
                            }
                            item.sumName = sumName;
                        }
                        return colorSquare +" "+ item.sumName;
                    }
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let statusText = "";
                        statusText += item.fdRetryYn === "Y" ? "재" : "";
                        statusText += item.fdPressed ? "다" : "";
                        statusText += item.fdAdd1Amt || item.fdAdd1Remark.length ? "추" : "";
                        statusText += item.fdRepairAmt || item.fdRepairRemark.length ? "수" : "";
                        statusText += item.fdWhitening ? "표" : "";
                        statusText += item.fdPollutionLevel ? "오" : "";
                        statusText += item.fdWaterRepellent || item.fdStarch ? "발" : "";
                        return statusText;
                    }
                }, {
                    dataField: "fdRequestAmt",
                    headerText: "접수금액",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                }, {
                    dataField: "urgent", // 급세탁이면 Y
                    headerText: "급세탁",
                }, {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                }, {
                    dataField: "fdS2Dt",
                    headerText: "지사입고",
                }, {
                    dataField: "fdS3Dt",
                    headerText: "지사출고",
                }, {
                    dataField: "fdS4Dt",
                    headerText: "완성일자",
                }, {
                    dataField: "btn1",
                    headerText: "가맹검품",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state">등록</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "btn2",
                    headerText: "검품확인",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state">확인</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "btn3",
                    headerText: "상품수정",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--modify">수정</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "btn4",
                    headerText: "접수취소",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "btn5",
                    headerText: "결제취소",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
                        return template;
                    },
                }, {
                    dataField: "btn6",
                    headerText: "인도취소",
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        const template = `
                            <button class="c-state c-state--cancel">취소</button>
                        `;
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
                enableColumnResize : false,
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
                modify: ["btn1", "btn2", "btn3", "btn4", "btn5", "btn6"],
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
            AUIGrid.bind(gridId[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const data = {
    initialData: {

    },
    selectedCustomer: {
        bcId: null,
    },
    fdColorCode: { // 컬러코드에 따른 실제 색상
        C00: "#D4D9E1", C01: "#D4D9E1", C02: "#3F3C32", C03: "#D7D7D7", C04: "#F54E50", C05: "#FB874B",
        C06: "#F1CE32", C07: "#349A50", C08: "#55CAB7", C09: "#398BE0", C10: "#DE9ACE", C11: "#FF9FB0",
    }
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

    event.s.main();
    event.s.vkeys();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    // grid.f.setInitialData(0);

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grid.e.basicEvent();
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
    condition.filterFromDt = $("#filterFromDt").val().replace(/[^0-9]/g, "");
    condition.filterToDt = $("#filterToDt").val().replace(/[^0-9]/g, "");
    if($("#searchType").val() === "4") {
        condition.searchTag = $("#searchString").val();
    }
    console.log(condition);
    //ajax.franchiseRequestDetailSearch(condition);
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

/* jest 기본 동작을 테스트 하기 위한 함수, 기본 테스트를 통과하면 아래의 module.exports 부분을 수정하고 이 함수는 지울 것 */
function testForJest() {
    return "hi";
}

/* jest 테스트를 위해 nodejs 의 요소에 테스트가 필요한 기능을 탑재하여 내보내기 한다. 보통의 실행 환경에서는 무시된다. */
try {
    module.exports = {testForJest};
}catch (e) {
    if(!(e instanceof ReferenceError)) {
        console.log(e);
    }
}