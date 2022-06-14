import {runOnlyOnce} from '../../module/m_headbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        getMainData: {
            filterYearMonth: 's',    // 조회조건 년월
            franchiseId: 'n',    // 가맹점 아이디
        },
    },
    receive: {
        getMainData: {
            hsYyyymmdd: 's',    // 정산일자
            hsNormalAmt: 'n',    // 정상금액
            hsPressed: 'n',    // 다림질 요금
            hsWaterRepellent: 'n',    // 발수가공요금
            hsStarch: 'n',    // 풀먹임 요금
            hsAdd1Amt: 'n',    // 추가비용1(접수시점)
            hsAdd2Amt: 'n',    // 추가비용2(검품후 추가비용 발생)
            hsRepairAmt: 'n',    // 수선금액
            hsWhitening: 'n',    // 표백 요금
            hsPollution: 'n',    // 오염 추가요금
            hsUrgentAmt: 'n',    // 급세탁 추가비용
            hsDiscountAmt: 'n',    // 할인금액
            hsTotAmt: 'n',    // 정산 - 총매출액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
            hsExceptAmt: 'n',    // 정산 - 정산대상 제외 총매출액
            hsSettleTotAmt: 'n',    // 정산 - 정산대상 총매출액(A)
            hsSettleReturnAmt: 'n',    // 일정산 - 정산대상  총반품액(B)
            hsSettleAmt: 'n',    // 정산 - 총매출액(A - B)
            hsSettleAmtBr: 'n',    // 정산 - 지사 매출액
            hsSettleAmtFr: 'n',    // 정산 - 가맹 매출액
            hsSmsAmt: 'n',    // 정산 - SMS 요금
            hsRolayltyAmtFr: 'n',    // 정산 - 지사 로열티 금액
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getMainData(searchCondition) {
        dv.chk(searchCondition, dtos.send.getMainData, '메인 그리드 데이터 검색조건 보내기');
        CommonUI.ajax('/api/head/headFranchiseDaliySummaryList', 'GET', searchCondition, function (res) {
            dv.chk(res.sendData.gridListData, dtos.receive.getMainData, '메인 그리드 데이터 검색 결과 받아오기');
            const data = summaryDataRefinery(res.sendData.gridListData);
            grids.set(0, data);

            /* 엑셀 제목 구성용 */
            wares.xlsxNaming.filterYearMonth = searchCondition.filterYearMonth;
            wares.xlsxNaming.frName = $('#frList option:selected').html();
        });
    },
};

/*
*  .s : AUI 그리드 관련 설정들, 같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  .f : 그리드 관련 함수들 배치
* */
const grids = {
    /* 그리드 세팅 */
    id: [
        'grid_main',
    ],
    columnLayout: [],
    footerLayout: [],
    prop: [],

    initialization() {

        /* 0번 그리드의 레이아웃 */
        grids.columnLayout[0] = [
            {
                dataField: 'hsYyyymmdd',
                headerText: '기준일',
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: 'hsNormalAmt',
                headerText: '기본 매출<br>(일반세탁)',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                headerText: '추가매출',
                children: [
                    {
                        dataField: 'addAmt',
                        headerText: '추가요금',
                        style: 'grid_textalign_right',
                        width: 80,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    }, {
                        dataField: 'hsRepairAmt',
                        headerText: '수선',
                        style: 'grid_textalign_right',
                        width: 80,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    }, {
                        dataField: 'whiteningPollution',
                        headerText: '표백/오염',
                        style: 'grid_textalign_right',
                        width: 80,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    }, {
                        dataField: 'hsUrgentAmt',
                        headerText: '긴급',
                        style: 'grid_textalign_right',
                        width: 80,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    }, {
                        dataField: 'hsDiscountAmt',
                        headerText: '할인금액',
                        style: 'grid_textalign_right',
                        width: 80,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    },
                ],
            }, {
                dataField: 'totAddSale',
                headerText: '추가 매출<br>전체',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsTotAmt',
                headerText: '총 매출',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsExceptAmt',
                headerText: '정산 제외<br>매출',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsSettleTotAmt',
                headerText: '정산 매출',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsSettleReturnAmt',
                headerText: '반품',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsSettleAmt',
                headerText: '최종 매출',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsSettleAmtBr',
                headerText: '지사 입금액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsSettleAmtFr',
                headerText: '가맹 매출',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsSmsAmt',
                headerText: 'SMS<br>발송비용',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsRolayltyAmtFr',
                headerText: '본사 입금액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'frProfit',
                headerText: '가맹점 마진',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            },
        ];

        grids.footerLayout[0] = [
            {
                labelText: '합계',
                positionField: 'hsYyyymmdd',
            }, {
                dataField: 'hsNormalAmt',
                operation: 'SUM',
                positionField: 'hsNormalAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'addAmt',
                operation: 'SUM',
                positionField: 'addAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsRepairAmt',
                operation: 'SUM',
                positionField: 'hsRepairAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'whiteningPollution',
                operation: 'SUM',
                positionField: 'whiteningPollution',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsUrgentAmt',
                operation: 'SUM',
                positionField: 'hsUrgentAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsDiscountAmt',
                operation: 'SUM',
                positionField: 'hsDiscountAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'totAddSale',
                operation: 'SUM',
                positionField: 'totAddSale',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsTotAmt',
                operation: 'SUM',
                positionField: 'hsTotAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsExceptAmt',
                operation: 'SUM',
                positionField: 'hsExceptAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsSettleTotAmt',
                operation: 'SUM',
                positionField: 'hsSettleTotAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsSettleReturnAmt',
                operation: 'SUM',
                positionField: 'hsSettleReturnAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsSettleAmt',
                operation: 'SUM',
                positionField: 'hsSettleAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsSettleAmtBr',
                operation: 'SUM',
                positionField: 'hsSettleAmtBr',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsSettleAmtFr',
                operation: 'SUM',
                positionField: 'hsSettleAmtFr',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsSmsAmt',
                operation: 'SUM',
                positionField: 'hsSmsAmt',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'hsRolayltyAmtFr',
                operation: 'SUM',
                positionField: 'hsRolayltyAmtFr',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            }, {
                dataField: 'frProfit',
                operation: 'SUM',
                positionField: 'frProfit',
                style: 'grid_textalign_right',
                formatString: '#,##0',
            },
        ];

        /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
        * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
        * */
        grids.prop[0] = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: false,
            showRowCheckColumn: false,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            rowHeight : 30,
            headerHeight : 30,
            showFooter : true,
            footerHeight: 30,
        };
    },

    /* 그리드 동작 처음 빈 그리드를 생성 */
    create() {
        for (const i in grids.columnLayout) {
            AUIGrid.create(grids.id[i], grids.columnLayout[i], grids.prop[i]);
            AUIGrid.setFooter(grids.id[i], grids.footerLayout[i]);
        }
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    get(numOfGrid) {
        return AUIGrid.getGridData(grids.id[numOfGrid]);
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    set(numOfGrid, data) {
        AUIGrid.setGridData(grids.id[numOfGrid], data);
    },

    /* 해당 배열 번호 그리드의 데이터 비우기 */
    clear(numOfGrid) {
        AUIGrid.clearGridData(grids.id[numOfGrid]);
    },

    /* 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절 */
    resize(num) {
        AUIGrid.resize(grids.id[num]);
    },

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[0])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[0], {
            fileName : wares.xlsxNaming.title + '_' + wares.xlsxNaming.filterYearMonth
                + '_' + wares.xlsxNaming.frName,
            progressBar : true,
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#searchListBtn').on('click', function () {
            searchList();
        });

        $('#exportXlsx').on('click', function () {
            if (wares.xlsxNaming.filterYearMonth) {
                grids.exportToXlsx();
            } else {
                alertCaution('먼저, 정산 현황 데이터를 조회해 주세요.', 1);
            }
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    xlsxNaming: {
        title: '가맹점일정산현황',
        filterYearMonth: '',
        frName: '',
    },
};

/* 조회기능 */
const searchList = function () {
    const searchCondition = {
        filterYearMonth: $('#filterYear').val() + $('#filterMonth').val(),
        franchiseId: parseInt($('#frList').val(), 10),
    };

    if (!searchCondition.franchiseId) {
        alertCaution('조회하실 가맹점을 선택해 주세요.', 1);
    }

    comms.getMainData(searchCondition);
};

/* db 컬럼상으로 없지만, 값이 존재할 필요가 있는 몇몇 항목들의 가공, 추가  */
const summaryDataRefinery = function (items) {
    for(let i = 0; i < items.length; i++) {
        items[i].hsPressed = items[i].hsPressed ? items[i].hsPressed : 0;
        items[i].hsWaterRepellent = items[i].hsWaterRepellent ? items[i].hsWaterRepellent : 0;
        items[i].hsStarch = items[i].hsStarch ? items[i].hsStarch : 0;
        items[i].hsAdd1Amt = items[i].hsAdd1Amt ? items[i].hsAdd1Amt : 0;
        items[i].hsAdd2Amt = items[i].hsAdd2Amt ? items[i].hsAdd2Amt : 0;
        /* 추가요금 */
        items[i].addAmt = items[i].hsPressed + items[i].hsWaterRepellent + items[i].hsStarch
            + items[i].hsAdd1Amt + items[i].hsAdd2Amt;

        items[i].hsWhitening = items[i].hsWhitening ? items[i].hsWhitening : 0;
        items[i].hsPollution = items[i].hsPollution ? items[i].hsPollution : 0;
        /* 표백/오염 */
        items[i].whiteningPollution = items[i].hsWhitening + items[i].hsPollution;

        items[i].hsRepairAmt = items[i].hsRepairAmt ? items[i].hsRepairAmt : 0;
        items[i].hsUrgentAmt = items[i].hsUrgentAmt ? items[i].hsUrgentAmt : 0;
        items[i].hsDiscountAmt = items[i].hsDiscountAmt ? items[i].hsDiscountAmt : 0;
        /* 추가 매출 전체 */
        items[i].totAddSale = items[i].hsPressed + items[i].hsWaterRepellent + items[i].hsStarch + items[i].hsAdd1Amt
            + items[i].hsAdd2Amt + items[i].hsRepairAmt + items[i].hsWhitening + items[i].hsPollution
            + items[i].hsUrgentAmt - items[i].hsDiscountAmt;

        items[i].hsSettleAmtFr = items[i].hsSettleAmtFr ? items[i].hsSettleAmtFr : 0;
        items[i].hsRolayltyAmtFr = items[i].hsRolayltyAmtFr ? items[i].hsRolayltyAmtFr : 0;
        /* 가맹점 마진 */
        items[i].frProfit = items[i].hsSettleAmtFr + items[i].hsRolayltyAmtFr;
    }
    return items;
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    runOnlyOnce.initializeDateSelectBox();
    runOnlyOnce.activateBrFrListInputs();

    grids.initialization();
    grids.create();

    trigs.basic();
};
