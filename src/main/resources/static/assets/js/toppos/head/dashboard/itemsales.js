import {activateBrFrListInputs} from '../../module/m_setBrFrList.js'

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        headItemSaleStatus: {
            branchId: "s",
            franchiseId: "s",
            filterYear: "s",
        },
        팝업그리드: {
            branchId: "s",
            franchiseId: "s",
            filterYear: "s",
            bgCode: "",
        }
    },
    receive: {
        headItemSaleStatus: {
            bgCode: "s",
            bgName: "s",
            amt01: "n",
            amt02: "n",
            amt03: "n",
            amt04: "n",
            amt05: "n",
            amt06: "n",
            amt07: "n",
            amt08: "n",
            amt09: "n",
            amt10: "n",
            amt11: "n",
            amt12: "n",
            amtTotal: "n",
            rate01: "n",
            rate02: "n",
            rate03: "n",
            rate04: "n",
            rate05: "n",
            rate06: "n",
            rate07: "n",
            rate08: "n",
            rate09: "n",
            rate10: "n",
            rate11: "n",
            rate12: "n",
            rateTotal: "n",
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getheadItemSaleStatus(condition) {
        CommonUI.ajax("/api/head/headItemSaleStatus", "GET", condition, function(res) {
            const data = res.sendData.gridListData;
            console.log(data);
            dv.chk(data, dtos.receive.headItemSaleStatus, "품목매출리스트 받아오기");
            grids.f.clear(0);
            grids.f.set(0, data);
            grids.f.setSorting(0, 'amtTotal', -1);
        })
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
                    dataField: 'bgName',
                    headerText: '품목명',
                    style: "grid_textalign_left",
                    width: 150,
                }, {
                    headerText: '1월',
                    children: [
                        {
                            dataField: 'amt01',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate01',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '2월',
                    children: [
                        {
                            dataField: 'amt02',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate02',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '3월',
                    children: [
                        {
                            dataField: 'amt03',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate03',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '4월',
                    children: [
                        {
                            dataField: 'amt04',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate04',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '5월',
                    children: [
                        {
                            dataField: 'amt05',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate05',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '6월',
                    children: [
                        {
                            dataField: 'amt06',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate06',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '7월',
                    children: [
                        {
                            dataField: 'amt07',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate07',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '8월',
                    children: [
                        {
                            dataField: 'amt08',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate08',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '9월',
                    children: [
                        {
                            dataField: 'amt09',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate09',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '10월',
                    children: [
                        {
                            dataField: 'amt10',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate10',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '11월',
                    children: [
                        {
                            dataField: 'amt11',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate11',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '12월',
                    children: [
                        {
                            dataField: 'amt12',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rate12',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
                        },
                    ]
                }, {
                    headerText: '전체',
                    children: [
                        {
                            dataField: 'amtTotal',
                            headerText: '매출액',
                            style: "grid_textalign_right",
                            width: 90,
                            dataType: "numeric",
                            autoThousandSeparator: "true",
                        }, {
                            dataField: 'rateTotal',
                            headerText: '매출비중',
                            width: 70,
                            style: "grid_textalign_right",
                            labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                                return roundPercent(value);
                            },
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
                    dataField: '',
                    headerText: '세부품목명',
                }, {
                    dataField: '',
                    headerText: '1월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '2월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '3월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '4월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '5월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '6월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '7월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '8월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '9월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '10월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '11월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '12월',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                }, {
                    dataField: '',
                    headerText: '전체',
                    children: [
                        {
                            dataField: '',
                            headerText: '매출액'
                        }, {
                            dataField: '',
                            headerText: '매출비중'
                        },
                    ]
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
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
        // 조회년도 변경
        $('#filterYear').on('change', function () {
            setDataByYear();
        });

        // 품목별 매출 현황 조회
        $('#getItemList').on('click', function () {
            getItemListCondition();
        });

        // 세부 품목 매출 현황 조회
        AUIGrid.bind(grids.s.id[0], 'cellClick', function (e) {
            if (e.dataField == '품목명') {
                const 품목명 = e.item.품목명;
                const condition = {
                    branchId: wares.조회시저장.branchId,
                }
            }
            console.log(e.item);
        });

        // 품목별 매출액 다운로드
        $('#itemSales').on('click', function() {
            exportToXlsx(0, '품목별매출액');
        });

        // 세부 품목별 매출액 다운로드
        $("#detailItemSales").on('click', function() {
            const 품목명 = wares.품목명 + "_세부품목별매출액";
            exportToXlsx(1, 품목명);
        })
    },};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    품목명: {

    },
    조회시저장: {

    },
};

// 조회년도
function getYear() {
    const startYear = 2022;
    const currentYear = new Date().getFullYear()
    const $filterYear = $("#filterYear");
    for (let i = currentYear; i >= startYear; i--) {
        const htmlText = `<option value="${i}">${i}</option>`;
        $filterYear.append(htmlText);
    }
};

// 품목별 매출 현황 조회 조건
function getItemListCondition() {
    const condition = {
        branchId: parseInt($('#brList').val()),
        franchiseId: parseInt($('#frList').val()),
        filterYear: parseInt($('#filterYear').val()),
    };
    console.log(condition);
    comms.getheadItemSaleStatus(condition);
};

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

};

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
};

// 소수점 2자리 반올림 후 %
function roundPercent(num) {
    const currentNum = Number((Math.abs(num) * 100).toPrecision(6));
    const roundNum = Math.round(currentNum) / 100 * Math.sign(num);
    return roundNum + "%";
}


/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    getYear();
    roundPercent(9.965);

    grids.f.initialization();
    grids.f.create();

    trigs.basic();
    activateBrFrListInputs();
};