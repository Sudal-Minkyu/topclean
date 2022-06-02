/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        sendYear: {
            filterYear: "s",
            brId: "s"
        },
    },
    receive: {
        차트가져오기: {

        },
        headCustomTransactionStatus: {
            TotalData: {
                avgPriceTotal: "n",
                pcsPriceTotal: "n",
            },
            gridListData : {
                brCode: "s",
                brName: "s",
                avgPrice01: "n",
                avgPrice02: "n",
                avgPrice03: "n",
                avgPrice04: "n",
                avgPrice05: "n",
                avgPrice06: "n",
                avgPrice07: "n",
                avgPrice08: "n",
                avgPrice09: "n",
                avgPrice10: "n",
                avgPrice11: "n",
                avgPrice12: "n",
                avgPriceTotal: "n",
                pcsPrice01: "n",
                pcsPrice02: "n",
                pcsPrice03: "n",
                pcsPrice04: "n",
                pcsPrice05: "n",
                pcsPrice06: "n",
                pcsPrice07: "n",
                pcsPrice08: "n",
                pcsPrice09: "n",
                pcsPrice10: "n",
                pcsPrice11: "n",
                pcsPrice12: "n",
                pcsPriceTotal: "n",
            },
        },
        headCustomTransactionDetailStatus: {
            frCode: "s",
            frName: "s",
            avgPrice01: "n",
            avgPrice02: "n",
            avgPrice03: "n",
            avgPrice04: "n",
            avgPrice05: "n",
            avgPrice06: "n",
            avgPrice07: "n",
            avgPrice08: "n",
            avgPrice09: "n",
            avgPrice10: "n",
            avgPrice11: "n",
            avgPrice12: "n",
            avgPriceTotal: "n",
            pcsPrice01: "n",
            pcsPrice02: "n",
            pcsPrice03: "n",
            pcsPrice04: "n",
            pcsPrice05: "n",
            pcsPrice06: "n",
            pcsPrice07: "n",
            pcsPrice08: "n",
            pcsPrice09: "n",
            pcsPrice10: "n",
            pcsPrice11: "n",
            pcsPrice12: "n",
            pcsPriceTotal: "n",
        }
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    // 지사별단가리스트
    getHeadCustomTransactionStatus(filterYear) {
        CommonUI.ajax('/api/head/headCustomTransactionStatus', 'GET', filterYear, function(res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.headCustomTransactionStatus, "지사별 단가 리스트 받아오기");

            const $avgPrice = $('#avgPrice');
            countUp($avgPrice[0], Math.round(data.TotalData.avgPriceTotal));

            const $pcsPrice = $('#pcsPrice');
            countUp($pcsPrice[0], Math.round(data.TotalData.pcsPriceTotal));

            grids.f.clear(0);
            grids.f.set(0, data.gridListData);
            grids.f.setSorting(0, 'avgPriceTotal', -1);
        });
    },

    // 가맹점별단가리스트
    getHeadCustomTransactionDetailStatus(condition) {
        CommonUI.ajax('/api/head/headCustomTransactionDetailStatus', 'GET', condition, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.headCustomTransactionDetailStatus, '가맹정별 단가 리스트 받아오기');
            grids.f.resize(1);
            grids.f.clear(1);
            grids.f.set(1, data);
            grids.f.setSorting(1, 'pcsPriceTotal', -1);
        });
    }
};

/*
*  .s : AUI 그리드 관련 설정들, 같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  .f : 그리드 관련 함수들 배치
* */
const grids = {
    /* 그리드 세팅 */
    s: {
        id: [
            'grid_main',
            'grid_pop',

        ],
        columnLayout: [],
        prop: [],
    },

    /* 그리드 펑션 */
    f: {
        /* 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다. */
        initialization() {

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: 'brName',
                    headerText: '지사명',
                    style: "grid_textalign_left",
                    width: 150,
                }, {
                    headerText: '1월',
                    children: [
                        {
                            dataField: 'avgPrice01',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice01',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '2월',
                    children: [
                        {
                            dataField: 'avgPrice02',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice02',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '3월',
                    children: [
                        {
                            dataField: 'avgPrice03',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice03',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '4월',
                    children: [
                        {
                            dataField: 'avgPrice04',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice04',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '5월',
                    children: [
                        {
                            dataField: 'avgPrice05',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice05',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '6월',
                    children: [
                        {
                            dataField: 'avgPrice06',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice06',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '7월',
                    children: [
                        {
                            dataField: 'avgPrice07',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice07',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '8월',
                    children: [
                        {
                            dataField: 'avgPrice08',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice08',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '9월',
                    children: [
                        {
                            dataField: 'avgPrice09',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice09',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '10월',
                    children: [
                        {
                            dataField: 'avgPrice10',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice10',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '11월',
                    children: [
                        {
                            dataField: 'avgPrice11',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice11',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '12월',
                    children: [
                        {
                            dataField: 'avgPrice12',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice12',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '전체',
                    children: [
                        {
                            dataField: 'avgPriceTotal',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPriceTotal',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
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
                rowHeight : 48,
                headerHeight : 48,
            };

            /* 1번 그리드의 레이아웃 */
            grids.s.columnLayout[1] = [
                {
                    dataField: 'frName',
                    headerText: '가맹점명',
                    style: "grid_textalign_left",
                    width: 150,
                }, {
                    headerText: '1월',
                    children: [
                        {
                            dataField: 'avgPrice01',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice01',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '2월',
                    children: [
                        {
                            dataField: 'avgPrice02',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice02',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '3월',
                    children: [
                        {
                            dataField: 'avgPrice03',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice03',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '4월',
                    children: [
                        {
                            dataField: 'avgPrice04',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice04',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '5월',
                    children: [
                        {
                            dataField: 'avgPrice05',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice05',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '6월',
                    children: [
                        {
                            dataField: 'avgPrice06',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice06',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '7월',
                    children: [
                        {
                            dataField: 'avgPrice07',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice07',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '8월',
                    children: [
                        {
                            dataField: 'avgPrice08',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice08',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '9월',
                    children: [
                        {
                            dataField: 'avgPrice09',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice09',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '10월',
                    children: [
                        {
                            dataField: 'avgPrice10',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice10',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '11월',
                    children: [
                        {
                            dataField: 'avgPrice11',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice11',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '12월',
                    children: [
                        {
                            dataField: 'avgPrice12',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPrice12',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                }, {
                    headerText: '전체',
                    children: [
                        {
                            dataField: 'avgPriceTotal',
                            headerText: '객단가',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'pcsPriceTotal',
                            headerText: 'PCS단가',
                            width: 90,
                            style: "grid_textalign_right",
                            dataType: "numeric",
                            formatString: "#,##0",
                            autoThousandSeparator: "true",
                        },
                    ]
                },
            ];

            /* 1번 그리드의 프로퍼티(옵션) */
            grids.s.prop[1] = {
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
                rowHeight : 48,
                headerHeight : 48,
            };
        },

        /* 그리드 동작 처음 빈 그리드를 생성 */
        create() {
            for (const i in grids.s.columnLayout) {
                AUIGrid.create(grids.s.id[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
        get(numOfGrid) {
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
        set(numOfGrid, data) {
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        /* 해당 배열 번호 그리드의 데이터 비우기 */
        clear(numOfGrid) {
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        /* 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절 */
        resize(num) {
            AUIGrid.resize(grids.s.id[num]);
        },

        // 그리드 소팅
        setSorting(numOfgrid, dataField, type) {
            // dataField = 데이터 필드명, type = 오름차순 : 1, 내림차순 : -1
            const sortingInfo = {
                dataField: dataField,
                sortType: type,
            };
            AUIGrid.setSorting(grids.s.id[numOfgrid], sortingInfo);
        }
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.s.id[0], 'cellClick', function (e) {
            if(e.dataField == "brName") {
                const brName = e.item.brName;
                const condition = {
                    filterYear: $("#filterYear").val(),
                    brCode: e.item.brCode,
                };
                $('#popFrUnitPrice').addClass('active');
                wares.brName = brName;
                $("#brName").text(brName);
                comms.getHeadCustomTransactionDetailStatus(condition);
            } else {
                return;
            }
        });

        // 팝업닫기
        $(".pop__close").on('click', function() {
            $(this).parents(".pop").removeClass('active');
        });

        // 지사별 단가 현황 다운로드
        $('#downloadBrUnitPrice').on('click', function() {
            exportToXlsx(0, '지사별단가순위');
        });

        // 가맹점별 단가 현황 다운로드
        $("#downloadFrUnitPrice").on('click', function() {
            const frName = wares.brName + "_가맹점별단가순위";
            exportToXlsx(1, frName);
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    brName: {

    }
};

// 년도 보내기
function getFilterYear() {
    const filterYear = {
        filterYear: $("#filterYear").val(),
    }
    comms.getHeadCustomTransactionStatus(filterYear)
}

// 조회년도
function getYear() {
    const startYear = 2022;
    const currentYear = new Date().getFullYear()
    const $filterYear = $("#filterYear");
    for (let i = currentYear; i >= startYear; i--) {
        const htmlText = `<option value="${i}">${i}</option>`;
        $filterYear.append(htmlText);
    }
}

// 파일 다운로드에 사용 할 날짜형식 yyyymmdd
function getDate(selectYear) {
    const date = new Date();
    const year = date.getFullYear();
    const month = ("0" + (1 + date.getMonth())).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    if(selectYear == year) {
        return year + month + day;
    } else {
        return selectYear;
    }
}

// 엑셀 내보내기(Export)
function exportToXlsx(i, filename) {
    const year = $("#filterYear").val();
    //FileSaver.js 로 로컬 다운로드가능 여부 확인
    if(!AUIGrid.isAvailableLocalDownload(grids.s.id[i])) {
        alertCaution("파일 다운로드가 불가능한 브라우저 입니다.", 1);
        return;
    }
    AUIGrid.exportToXlsx(grids.s.id[i], {
        fileName : filename + "_" + getDate(year),
        progressBar : true,
    });
    console.log(filename + "_" + getDate(year));
}

// 전체 객단가 평균
function getAvgPriceAverage(avgPriceTotal) {
    let sum = 0;
    let length = avgPriceTotal.length;
    for (const price of avgPriceTotal) {
        sum += price;
        if (!price) {
            length--;
        }
    }
    const result = length ? Math.round(sum / length) : 0;
    const $avgPrice = $('#avgPrice');
    countUp($avgPrice[0], result);
};
// 전체 PCS 단가 평균
function getPcsPriceAverage(pcsPriceTotal) {
    let sum = 0;
    let length = pcsPriceTotal.length;
    for (const price of pcsPriceTotal) {
        sum += price;
        if (!price) {
            length--;
        }
    }
    const $pcsPrice = $('#pcsPrice');
    const result = length ? Math.round(sum / length) : 0;
    countUp($pcsPrice[0], result);
};

function countUp(id, count) {
    let current = 0;
    const total = count;
    const increment = Math.ceil(total / 100);

    function step() {
        current += increment;
        if(current > total) {
            current = total;
        }
        id.textContent = current.toLocaleString();
        if(current !== total) {
            requestAnimationFrame(step);
        }
    }
    step();
}


/* 차트 생성 */
function makeChart(data) {
    am5.ready(function() {

        // Create root element
        // https://www.amcharts.com/docs/v5/getting-started/#Root_element
        var root = am5.Root.new("summaryChart");


        // Set themes
        // https://www.amcharts.com/docs/v5/concepts/themes/
        root.setThemes([
            am5themes_Animated.new(root)
        ]);


        // Create chart
        // https://www.amcharts.com/docs/v5/charts/xy-chart/
        var chart = root.container.children.push(am5xy.XYChart.new(root, {
            panX: false,
            panY: false,
            wheelX: "panX",
            wheelY: "zoomX",
            layout: root.verticalLayout
        }));


        // Add legend
        // https://www.amcharts.com/docs/v5/charts/xy-chart/legend-xy-series/
        var legend = chart.children.unshift(
            am5.Legend.new(root, {
                centerX: am5.p50,
                x: am5.p50
            })
        );

        var data = [{
            "month": "1월",
            "personAmt": 5100,
            "pcsAmt": 4800,
        }, {
            "month": "2월",
            "personAmt": 9478,
            "pcsAmt": 5879,
        }, {
            "month": "3월",
            "personAmt": 16879,
            "pcsAmt": 5692,
        }, {
            "month": "4월",
            "personAmt": 11615,
            "pcsAmt": 6829,
        }, {
            "month": "5월",
            "personAmt": 7038,
            "pcsAmt": 4692,
        }, {
            "month": "6월",
            "personAmt": 4579,
            "pcsAmt": 4061,
        }, {
            "month": "7월",
            "personAmt": 5100,
            "pcsAmt": 4800,
        }, {
            "month": "8월",
            "personAmt": 9478,
            "pcsAmt": 5879,
        }, {
            "month": "9월",
            "personAmt": 16879,
            "pcsAmt": 5692,
        }, {
            "month": "10월",
            "personAmt": 11615,
            "pcsAmt": 6829,
        }, {
            "month": "11월",
            "personAmt": 7038,
            "pcsAmt": 4692,
        }, {
            "month": "12월",
            "personAmt": 4579,
            "pcsAmt": 4061,
        },];


        // Create axes
        // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
        var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
            categoryField: "month",
            renderer: am5xy.AxisRendererX.new(root, {
                cellStartLocation: 0.1,
                cellEndLocation: 0.9
            }),
            tooltip: am5.Tooltip.new(root, {})
        }));

        xAxis.data.setAll(data);

        var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            renderer: am5xy.AxisRendererY.new(root, {})
        }));


        // Add series
        // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
        function makeSeries(name, fieldName) {
            var series = chart.series.push(am5xy.ColumnSeries.new(root, {
                name: name,
                xAxis: xAxis,
                yAxis: yAxis,
                valueYField: fieldName,
                categoryXField: "month"
            }));

            series.columns.template.setAll({
                tooltipText: "{valueY}",
                width: am5.percent(90),
                tooltipY: 0
            });

            series.data.setAll(data);

            // Make stuff animate on load
            // https://www.amcharts.com/docs/v5/concepts/animations/
            series.appear();

            series.bullets.push(function () {
                return am5.Bullet.new(root, {
                    locationY: 0,
                    sprite: am5.Label.new(root, {
                        text: "{valueY}",
                        fill: root.interfaceColors.get("alternativeText"),
                        centerY: 0,
                        centerX: am5.p50,
                        populateText: true
                    })
                });
            });

            legend.data.push(series);
        }

        makeSeries("객단가", "personAmt");
        makeSeries("PCS단가", "pcsAmt");


    // Make stuff animate on load
    // https://www.amcharts.com/docs/v5/concepts/animations/
        chart.appear(1000, 100);

    }); // end am5.ready()
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    getYear();
    getFilterYear();

    grids.f.initialization();
    grids.f.create();

    trigs.basic();
    makeChart();
};
