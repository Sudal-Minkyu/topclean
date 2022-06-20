import {runOnlyOnce} from '../../module/m_headbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        headBranchMonthlyStatusList: {
            filterYear: 's',
        }
    },
    receive: {
        headBranchMonthlyStatusList: {
            brName: "s",
            amt01: "s",
            inamt01: "n",
            amt02: "s",
            inamt02: "n",
            amt03: "s",
            inamt03: "n",
            amt04: "s",
            inamt04: "n",
            amt05: "s",
            inamt05: "n",
            amt06: "s",
            inamt06: "n",
            amt07: "s",
            inamt07: "n",
            amt08: "s",
            inamt08: "n",
            amt09: "s",
            inamt09: "n",
            amt10: "s",
            inamt10: "n",
            amt11: "s",
            inamt11: "n",
            amt12: "s",
            inamt12: "n",
            total: "n",
        }
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    searchDepositListData(searchCondition) {
        dv.chk(searchCondition, dtos.send.headBranchMonthlyStatusList, '조회 조건 보내기');
        CommonUI.ajax('/api/head/headBranchMonthlyStatusList', 'GET', searchCondition, function (res) {
            wares.xlsxNaming.filterYear = searchCondition.filterYear;
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.headBranchMonthlyStatusList, '받은 그리드 데이터')
            grids.set(0, data);
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
                dataField: 'brName',
                headerText: '지사명',
                width: 120,
            }, {
                headerText: '1월',
                children: [
                    {
                        dataField: 'amt01',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt01',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '2월',
                children: [
                    {
                        dataField: 'amt02',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt02',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '3월',
                children: [
                    {
                        dataField: 'amt03',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt03',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '4월',
                children: [
                    {
                        dataField: 'amt04',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt04',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '5월',
                children: [
                    {
                        dataField: 'amt05',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt05',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '6월',
                children: [
                    {
                        dataField: 'amt06',
                        headerText: '입금여부',
                    }, {
                        dataField: 'amt06',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '7월',
                children: [
                    {
                        dataField: 'amt07',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt07',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '8월',
                children: [
                    {
                        dataField: 'amt08',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt08',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '9월',
                children: [
                    {
                        dataField: 'amt09',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt09',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '10월',
                children: [
                    {
                        dataField: 'amt10',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt10',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '11월',
                children: [
                    {
                        dataField: 'amt11',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt11',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                headerText: '12월',
                children: [
                    {
                        dataField: 'amt12',
                        headerText: '입금여부',
                    }, {
                        dataField: 'inamt12',
                        headerText: '미입금액',
                        dataType: "numeric",
                        autoThousandSeparator: "true",
                    },
                ],
            }, {
                dataField: 'total',
                headerText: '총 미입금액',
                dataType: "numeric",
                autoThousandSeparator: "true",
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

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[0])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[0], {
            fileName : wares.xlsxNaming.title + '_' + wares.xlsxNaming.filterYear,
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

        $("#exportXlsx").on("click", function () {
            if(grids.get(0).length) {
                grids.exportToXlsx();
            } else {
                alertCaution("엑셀 다운로드를 실행할 데이터가 없습니다.<br>먼저 조회를 해주세요.", 1);
            }
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    xlsxNaming: {
        title: '지사월정산입금현황',
        filterYear: '',
    }
};

const searchDepositListData = function () {
    const searchCondition = {
        filterYear: $('#filterYear').val(),
    }
    comms.searchDepositListData(searchCondition);
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    runOnlyOnce.initializeDateSelectBox(false);
    grids.initialization();
    grids.create();

    trigs.basic();
};
