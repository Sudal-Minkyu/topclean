/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseStateChange: {
            fdIdList: "a",
            stateType: "sr",
        }
    },
    receive: {
        getReturnList: {
            fdId: "nr",
            frYyyymmdd: "sr",
            bcName: "sr",
            fdTag: "sr",

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
            fdColor: "s",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getReturnList: '/api/user/franchiseReceiptReturnList',
    changeClosedList: '/api/user/franchiseStateChange',
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    // 반송 리스트 받기
    getReturnList() { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(urls.getReturnList, "GET", false, function (res) {
            const data = res.sendData;
            const dataLength = data.gridListData.length;

            dv.chk(data.gridListData, dtos.receive.getReturnList, '반송 리스트 항목 받아오기');
            CommonUI.toppos.makeSimpleProductNameList(data.gridListData);
            grids.f.setData(0, data.gridListData);
            
            $('#totalNum').text(dataLength);
        });
    },

    // 지사 전송
    changeClosedList(saveData) {
        dv.chk(saveData, dtos.send.franchiseStateChange, '반송 리스트 항목 보내기');
    
        CommonUI.ajax(urls.changeClosedList, "PARAM", saveData, function(res) {
            alertSuccess("지사전송 완료");
            grids.f.clearData();
            comms.getReturnList();

            $('#checkedItems').val(0);
            $('#totalAmount').val(0);
            $('#fastItems').val(0);
            $('#retryItems').val(0);
        })
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
            "returnList"
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
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    style: "datafield_tag",
                    width: 90,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatFrTagNo(value, frTagInfo.frTagType);
                    },
                }, {
                    dataField: "productName",
                    headerText: "상품명",
                    style: "color_and_name",
                    width: 150,
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
                    width: 100,
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    },
                }, {
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
                }, {
                    dataField: "fdRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
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
    },

    t: {
        basicTrigger() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
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
                let fastItemCount = 0;
                let retryItemCount = 0;
                
                checkedItems.forEach(checkedItem => {
                    // 접수총액
                    totalAmount += checkedItem.item.fdTotAmt;
                    // 급세탁건수
                    if (checkedItem.item.fdUrgentYn == "Y") {
                        fastItemCount = fastItemCount + 1;
                    }
                    // 재세탁건수
                    if (checkedItem.item.fdRetryYn == "Y") {
                        retryItemCount = retryItemCount + 1;
                    }
                });

                $('#checkedItems').val(checkedLength);
                $('#totalAmount').val(totalAmount.toLocaleString());
                $('#fastItems').val(fastItemCount);
                $('#retryItems').val(retryItemCount);
            });

            $('#returnit').on('click', function() {
                const checkedItems = grids.f.getCheckedItems(0);
                const saveDataset = makeSaveDataset(checkedItems);
                if (checkedItems.length) {
                    comms.changeClosedList(saveDataset);
                } else {
                    alertCaution("반송세탁물 리스트를 선택해주세요", 1);
                }
            });

            $("#refresh").on("click", function () {
                grids.f.clearData(0);
                comms.getReturnList();
            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();

    trigs.s.basicTrigger();

    comms.getReturnList();
}

function makeSaveDataset(checkedItems) { // 저장 데이터셋 만들기
    let fdIdList = [];
    checkedItems.forEach(data => {
        fdIdList.push(data.item.fdId);
    });
    const changeData = {
        stateType: "S3",
        fdIdList: fdIdList,
    };
    changeData.fdIdList = fdIdList;
    return changeData;
}