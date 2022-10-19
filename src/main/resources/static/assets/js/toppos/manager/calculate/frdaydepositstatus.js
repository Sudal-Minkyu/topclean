import {runOnlyOnce} from '../../module/m_managerbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        headFranchiseDailyStatusList: {
            filterYearMonth: 's',
        },
    },
    receive: {
        headFranchiseDailyStatusList: {
            brName: 's',
            frName: 's',
            total: 'n',
            inamtcnt: 's',
            amt01: 's',
            inamt01: 'n',
            amt02: 's',
            inamt02: 'n',
            amt03: 's',
            inamt03: 'n',
            amt04: 's',
            inamt04: 'n',
            amt05: 's',
            inamt05: 'n',
            amt06: 's',
            inamt06: 'n',
            amt07: 's',
            inamt07: 'n',
            amt08: 's',
            inamt08: 'n',
            amt09: 's',
            inamt09: 'n',
            amt10: 's',
            inamt10: 'n',
            amt11: 's',
            inamt11: 'n',
            amt12: 's',
            inamt12: 'n',
            amt13: 's',
            inamt13: 'n',
            amt14: 's',
            inamt14: 'n',
            amt15: 's',
            inamt15: 'n',
            amt16: 's',
            inamt16: 'n',
            amt17: 's',
            inamt17: 'n',
            amt18: 's',
            inamt18: 'n',
            amt19: 's',
            inamt19: 'n',
            amt20: 's',
            inamt20: 'n',
            amt21: 's',
            inamt21: 'n',
            amt22: 's',
            inamt22: 'n',
            amt23: 's',
            inamt23: 'n',
            amt24: 's',
            inamt24: 'n',
            amt25: 's',
            inamt25: 'n',
            amt26: 's',
            inamt26: 'n',
            amt27: 's',
            inamt27: 'n',
            amt28: 's',
            inamt28: 'n',
            amt29: 's',
            inamt29: 'n',
            amt30: 's',
            inamt30: 'n',
            amt31: 's',
            inamt31: 'n',
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    searchDepositListData(searchCondition) {
        CommonUI.ajax('/api/manager/branchFranchiseDailyStatusList', 'GET', searchCondition, function (res) {
            const data = res.sendData.gridListData;
            grids.clear(0);
            grids.set(0, data);
            hideDayColumn(wares.xlsxNaming.filterYear, wares.xlsxNaming.filterMonth);
        });
    },
};

/* AUI 그리드 관련 설정과 함수들 */
const grids = {
    id: [
        'grid_main',
    ],
    columnLayout: [],
    prop: [],

    /* 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다. */
    initialization() {

        /* 0번 그리드의 레이아웃 */
        grids.columnLayout[0] = [
            {
                dataField: 'frName',
                headerText: '가맹점명',
                width: 150,
            }, {
                dataField: 'total',
                headerText: '미입금액',
                width: 120,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
                style: 'grid_textalign_right',
            }, {
                dataField: 'inamtcnt',
                headerText: '3회 이상<br />미입금',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
                style: 'grid_textalign_right',
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    if (3 > value) {
                        return '';
                    } else {
                        return value + '회';
                    }
                },
            }, {
                dataField: 'day1',
                headerText: '1일',
                children: [
                    {
                        dataField: 'amt01',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt01',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day2',
                headerText: '2일',
                children: [
                    {
                        dataField: 'amt02',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt02',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day3',
                headerText: '3일',
                children: [
                    {
                        dataField: 'amt03',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt03',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day4',
                headerText: '4일',
                children: [
                    {
                        dataField: 'amt04',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt04',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day5',
                headerText: '5일',
                children: [
                    {
                        dataField: 'amt05',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt05',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day6',
                headerText: '6일',
                children: [
                    {
                        dataField: 'amt06',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt06',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day7',
                headerText: '7일',
                children: [
                    {
                        dataField: 'amt07',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt07',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day8',
                headerText: '8일',
                children: [
                    {
                        dataField: 'amt08',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt08',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day9',
                headerText: '9일',
                children: [
                    {
                        dataField: 'amt09',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt09',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day10',
                headerText: '10일',
                children: [
                    {
                        dataField: 'amt10',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt10',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day11',
                headerText: '11일',
                children: [
                    {
                        dataField: 'amt11',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt11',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day12',
                headerText: '12일',
                children: [
                    {
                        dataField: 'amt12',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt12',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day13',
                headerText: '13일',
                children: [
                    {
                        dataField: 'amt13',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt13',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day14',
                headerText: '14일',
                children: [
                    {
                        dataField: 'amt14',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt14',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day15',
                headerText: '15일',
                children: [
                    {
                        dataField: 'amt15',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt15',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day16',
                headerText: '16일',
                children: [
                    {
                        dataField: 'amt16',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt16',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day17',
                headerText: '17일',
                children: [
                    {
                        dataField: 'amt17',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt17',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day18',
                headerText: '18일',
                children: [
                    {
                        dataField: 'amt18',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt18',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day19',
                headerText: '19일',
                children: [
                    {
                        dataField: 'amt19',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt19',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day20',
                headerText: '20일',
                children: [
                    {
                        dataField: 'amt20',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt20',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day21',
                headerText: '21일',
                children: [
                    {
                        dataField: 'amt21',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt21',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day22',
                headerText: '22일',
                children: [
                    {
                        dataField: 'amt22',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt22',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day23',
                headerText: '23일',
                children: [
                    {
                        dataField: 'amt23',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt23',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day24',
                headerText: '24일',
                children: [
                    {
                        dataField: 'amt24',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt24',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day25',
                headerText: '25일',
                children: [
                    {
                        dataField: 'amt25',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt25',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day26',
                headerText: '26일',
                children: [
                    {
                        dataField: 'amt26',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt26',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day27',
                headerText: '27일',
                children: [
                    {
                        dataField: 'amt27',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt27',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day28',
                headerText: '28일',
                children: [
                    {
                        dataField: 'amt28',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt28',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day29',
                headerText: '29일',
                children: [
                    {
                        dataField: 'amt29',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt29',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day30',
                headerText: '30일',
                children: [
                    {
                        dataField: 'amt30',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt30',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
            }, {
                dataField: 'day31',
                headerText: '31일',
                children: [
                    {
                        dataField: 'amt31',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt31',
                        headerText: '미입금액',
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                        style: 'grid_textalign_right',
                    },
                ],
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
        };

    },

    /* 그리드 동작 처음 빈 그리드를 생성 */
    create() {
        for (const i in grids.columnLayout) {
            AUIGrid.create(grids.id[i], grids.columnLayout[i], grids.prop[i]);
        }
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    get(gridNum) {
        return AUIGrid.getGridData(grids.id[gridNum]);
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    set(gridNum, data) {
        AUIGrid.setGridData(grids.id[gridNum], data);
    },

    /* 해당 배열 번호 그리드의 데이터 비우기 */
    clear(gridNum) {
        AUIGrid.clearGridData(grids.id[gridNum]);
    },

    /* 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절 */
    resize(gridNum) {
        AUIGrid.resize(grids.id[gridNum]);
    },

    hideColumnGroup(gridNum, dataField) {
        AUIGrid.hideColumnGroup(grids.id[gridNum], dataField);
    },

    showColumnGroup(gridNum, dataField) {
        AUIGrid.showColumnGroup(grids.id[gridNum], dataField);
    },

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[0])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[0], {
            fileName : wares.xlsxNaming.title + '_' + wares.xlsxNaming.filterYear + wares.xlsxNaming.filterMonth,
            progressBar : true,
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#searchListBtn').on('click', function () {
            searchDepositListData();
        });

        $('#exportXlsx').on('click', function () {
            if(grids.get(0).length) {
                grids.exportToXlsx();
            } else {
                alertCaution("엑셀 다운로드를 실행할 데이터가 없습니다.<br>먼저 조회를 해주세요.", 1);
            }
        })
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    xlsxNaming: {
        title: '가맹점일정산입금현황',
        filterYear: '',
        filterMonth: '',
    },
};

const searchDepositListData = function () {
    const year = $('#filterYear').val();
    const month = $('#filterMonth').val()
    const searchCondition = {
        filterYearMonth: year + month,
    };
    wares.xlsxNaming.filterYear = year;
    wares.xlsxNaming.filterMonth = month;
    $('#year').text(year);
    $('#month').text(month);
    comms.searchDepositListData(searchCondition);
};

// 조회 년/월의 날짜 구하기
function getDays(year, month) {
    return new Date(year, month, 0).getDate();
}

// 조회 날짜의 그리드 숨기기
function hideDayColumn(year, month) {
    const day = getDays(year, month) + 1;
    for (let showDay = 29; showDay < 32; showDay++) {
        const ShowDataField = 'day' + showDay
        grids.showColumnGroup(0, ShowDataField);
    }
    for (let hideDay = day; hideDay < 32; hideDay++) {
        const hideDataField = 'day' + hideDay;
        grids.hideColumnGroup(0, hideDataField)
    }
}

function setYearMonth() {
    const year = $('#filterYear').val();
    const month = $('#filterMonth').val()
    $('#year').text(year);
    $('#month').text(month);
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    runOnlyOnce.initializeDateSelectBox(false);
    setYearMonth();
    grids.initialization();
    grids.create();

    trigs.basic();
};
