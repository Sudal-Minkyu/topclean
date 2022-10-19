/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseStateChange: {
            smsBcIdList: "a",
            fdIdList: "a",
            stateType: "sr",
        },

        customerInfo: { // integrate 의 customerInfo와 같은 구성이지만 택번호도 포함
            searchType: "sr", // 0 통합검색, 1 고객명, 2 전화번호, 3 주소
            searchString: "sr", // 검색문자
        },

        franchiseReceiptForceList: {
            bcId: "",
            fdTag: "s",
        },
    },
    receive: {

        customerInfo: { // integrate 의 customerInfo와 같은 구성
            tempSaveBcName: "s",
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
        },

        franchiseReceiptForceList: {
            fdPromotionType: "s",
            fdPromotionDiscountRate: "n",
            fdPollutionType: "n",
            fdPollutionBack: "n",
            fdId: "nr",
            fdState: "s",
            bcName: "sr",
            fdTag: "sr",

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
            fdWhitening: "",
            fdPollution: "",
            fdWaterRepellent: "",
            fdStarch: "",
            fdUrgentYn: "s",
            fdTotAmt: "n",
            fdRemark: "s",

            frYyyymmdd: "sr",
            fdS2Dt: "s",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    searchCustomer: "/api/user/customerInfo", // 고객 검색
    getForceList: '/api/user/franchiseReceiptForceList', // 강제입고할 수 있는 세탁물 리스트 가져오기
    changeClosedList: '/api/user/franchiseStateChange', // 강제입고될 항목 보내기
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    searchCustomer(searchCondition) {
        dv.chk(searchCondition, dtos.send.customerInfo, "고객 검색 조건 보내기");
        CommonUI.ajax(urls.searchCustomer, "GET", searchCondition, function (res) {
            $("#searchType").val(0);
            $("#searchString").val("");
            const items = res.sendData.gridListData;
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

    getForceList(condition) {
        dv.chk(condition, dtos.send.franchiseReceiptForceList, "가맹점 강제입고 가능한 품목 가져오기 위한 고객아이디");
        CommonUI.ajax(urls.getForceList, "GET", condition, function (res) {
            const data = res.sendData;
            dv.chk(data.gridListData, dtos.receive.franchiseReceiptForceList, '강제입고 항목 받아오기');
            CommonUI.toppos.makeSimpleProductNameList(data.gridListData);
            grids.f.setData(0, data.gridListData);
        });
    },

    // 강제입고 항목 보내기
    changeClosedList(saveData) {
        dv.chk(saveData, dtos.send.franchiseStateChange, '강제입고 항목 보내기');
    
        CommonUI.ajax(urls.changeClosedList, "PARAM", saveData, function(res) {
            alertSuccess("강제입고 완료");
            grids.f.clearData(0);
    
            comms.getForceList(wares.condition);
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
            "forceList", "grid_customerList"
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
                    dataField: "frYyyymmdd",
                    headerText: "접수일자",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                    width: 80,
                },
                {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 90,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
                    },
                }, 
                {
                    dataField: "productName",
                    headerText: "상품명",
                    width: 150,
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
                },
                {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 90,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdState[value];
                    },
                }, 
                {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 100,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    },
                },
                {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    width: 120,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdUrgentYn",
                    headerText: "급세탁",
                    width: 60,
                },
                {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                }, {
                    dataField: "frYyyymmdd",
                    headerText: "고객접수일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fdS2Dt",
                    headerText: "지사입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
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
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
                rowCheckColumnWidth: 40,
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
        
        // 그리드 체크된 로우
        getCheckedItems(numOfGrid) {
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        },

        // 그리드 데이터 클리어
        clearData(numOfGrid) {
			AUIGrid.clearGridData(grids.s.id[numOfGrid]);
		},

        getSelectedCustomer() {
            return AUIGrid.getSelectedRows(grids.s.id[1])[0];
        },
    },

    t: {
        basicTrigger() {
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basicTrigger() {
            $('.aui-checkbox').on('click', function () {
                const checkedItems = grids.f.getCheckedItems(0);
                const checkedLength = checkedItems.length;
                let totalAmount = 0;
                
                checkedItems.forEach(checkedItem => {
                    // 접수총액
                    totalAmount += checkedItem.item.fdTotAmt;
                });

                $('#selectItems').val(checkedLength);
                $('#selectAmount').val(totalAmount.toLocaleString());
            });
            
            $('#forceIn').on('click', function() {
                const checkedItems = grids.f.getCheckedItems(0);
                if (checkedItems.length) {
                    alertThree("선택된 물품을 가맹점 강제입고 하시고<br>고객께 메시지를 보내시겠습니까?", "메시지전송", "강제입고만", "취소");
                    $("#popFirstBtn").on("click", function () {
                        const saveDataset = makeSaveDataset(checkedItems, true);
                        comms.changeClosedList(saveDataset);
                        $('#popupId').remove();
                    });
                    $("#popSecondBtn").on("click", function () {
                        const saveDataset = makeSaveDataset(checkedItems, false);
                        comms.changeClosedList(saveDataset);
                        $('#popupId').remove();
                    });
                    $("#popThirdBtn").on("click", function () {
                        $('#popupId').remove();
                    });
                } else {
                    alertCaution("강제입고 할 리스트를 선택해주세요", 1);
                }
            });

            $("#searchCustomer").on("click", function () {
                mainSearch();
            });

            $("#selectCustomer").on("click", function () {
                const selectedItem = grids.f.getSelectedCustomer();
                selectCustomerFromList(selectedItem);
            });

            $("#resetCustomer").on("click", function () {
                resetCustomer();
            });

            $("#closeCustomerPop").on("click", function () {
                closeCustomerPop();
            });

            // 검색 엔터 이벤트
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
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    selectedCustomer: {},
    condition: {},
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();

    trigs.s.basicTrigger();
}

/**
 * 강제입고 처리를 위한 데이터셋 만들기
 * @param checkedItems : array<object> - 보낼 대상 아이템 리스트
 * @param isSendMsg : boolean - 메시지 보내기 여부
 * @returns {{stateType: string, fdIdList: *[], smsBcIdList: *[]}} - 완성된 데이터셋
 */
function makeSaveDataset(checkedItems, isSendMsg) {
    let fdIdList = [];
    let smsBcIdList = [];
    if (isSendMsg) {
        smsBcIdList.push(wares.selectedCustomer.bcId);
    }

    checkedItems.forEach(data => {
        fdIdList.push(data.item.fdId);
    });

    return {
        stateType: "S7",
        fdIdList: fdIdList,
        smsBcIdList: smsBcIdList,
    };
}

function mainSearch() {
    if($("#searchType").val() === "4") {
        resetCustomer();
        filterMain();
    }else{
        searchCustomer();
    }
}

function filterMain() {
    const condition = {
        bcId: "",
        fdTag: $("#searchString").val().numString(),
    }
    comms.getForceList(condition);
}

function searchCustomer() {
    const searchCondition = {
        searchType: $("#searchType").val(),
        searchString: $("#searchString").val(),
    }
    if(searchCondition.searchString === "") {
        alertCaution("검색조건을 입력해 주세요.", 1);
    } else {
        comms.searchCustomer(searchCondition);
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

    if(wares.selectedCustomer.bcId !== null) {
        const condition = {
            bcId: wares.selectedCustomer.bcId,
            fdTag: "",
        };
        wares.condition = condition;
        comms.getForceList(condition);
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

function closeCustomerPop() {
    $("#customerListPop").removeClass("active");
}

function resetCustomer() {
    wares.selectedCustomer = {
        bcId: null,
        uncollectMoney: 0,
        saveMoney: 0,
        bcAddress: "",
        bcRemark: "",
    };
    putCustomer();
}