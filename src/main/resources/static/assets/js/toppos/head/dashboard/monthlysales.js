/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        headBranchMonthlySale: {
            filterYear: "s",
            brId: "s"
        }
    },
    receive: {
        headBranchMonthlySale: {
            brCode: "s",
            brName: "s",
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
        },
        headFranchiseRankSale: {
            frCode: "s",
            frName: "s",
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
        }
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    // 년도별 지사매출 가져오기
    getBrSalesList(filterYear) {
        CommonUI.ajax("/api/head/headBranchRankSale", "GET", filterYear, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.headBranchMonthlySale, "지사매출순위 받아오기");
            grids.f.clear(0);
            grids.f.set(0, data);
            grids.f.setSorting(0, 'amtTotal', -1);
        })
    },

    // 지정년도의 월별 매출 그래프 데이터 가져오기
    getSalesChartData(condition) {
        CommonUI.ajax('/api/head/headBranchMonthlySale', 'GET', condition, function (res) {
            const data = chartDataRefinary(res.sendData.gridListData);
            makeChart(data);
        });
    },

    // 지사명 클릭시 가맹점별 월 매출 데이터 가져오기
    getHeadFranchiseRankSale(condition) {
        CommonUI.ajax("/api/head/headFranchiseRankSale", "GET", condition, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.headFranchiseRankSale)
            grids.f.resize(1);
            grids.f.clear(1);
            grids.f.set(1, data);
            grids.f.setSorting(1, 'amtTotal', -1);
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
            'grid_pop'
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
                },{
                    dataField: 'amt01',
                    headerText: '1월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt02',
                    headerText: '2월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt03',
                    headerText: '3월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt04',
                    headerText: '4월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt05',
                    headerText: '5월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt06',
                    headerText: '6월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt07',
                    headerText: '7월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt08',
                    headerText: '8월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt09',
                    headerText: '9월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt10',
                    headerText: '10월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt11',
                    headerText: '11월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt12',
                    headerText: '12월',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amtTotal',
                    headerText: '전체',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
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
                },{
                    dataField: 'amt01',
                    headerText: '1월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt02',
                    headerText: '2월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt03',
                    headerText: '3월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt04',
                    headerText: '4월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt05',
                    headerText: '5월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt06',
                    headerText: '6월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt07',
                    headerText: '7월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt08',
                    headerText: '8월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt09',
                    headerText: '9월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt10',
                    headerText: '10월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
                {
                    dataField: 'amt11',
                    headerText: '11월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amt12',
                    headerText: '12월',
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'amtTotal',
                    headerText: '전체',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                },
            ];

            /* 1번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
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
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.s.id[0], 'cellClick', function (e) {
            if (e.dataField == "brName") {
                const brName = e.item.brName;
                const condition = {
                    filterYear: $("#filterYear").val(),
                    brCode: e.item.brCode,
                };
                $("#popFrSales").addClass("active");
                wares.brName = brName;
                $("#brName").text(brName);
                comms.getHeadFranchiseRankSale(condition);
            } else {
                return;
            }
        });

        // 팝업닫기
        $(".pop__close").on('click', function() {
            $(this).parents(".pop").removeClass('active');
        });

        // 조회년도 변경
        $('#filterYear').on('change', function () {
            setDataByYear();
        });

        // 지사 매출 순위 다운로드
        $("#brSalesDown").on('click', function() {
            exportToXlsx(0, '지사매출순위');
        });

        $("#frSalesDown").on('click', function() {
            const brName = wares.brName + "_가맹점매출순위";
            exportToXlsx(1, brName);
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    brName: {

    },
};

// 년도 보내기
function getFilterYear() {
    const filterYear = {
        filterYear: $("#filterYear").val(),
    }
    comms.getBrSalesList(filterYear)
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
}

// chart
function makeChart(data) {
    am5.ready(function() {

        // Create root element
        var root = am5.Root.new('summaryChart');


        // Set themes
        root.setThemes([
            am5themes_Animated.new(root)
        ]);


        // Create chart
        var chart = root.container.children.push(am5xy.XYChart.new(root, {
            panX: false,
            panY: false,
            wheelX: "panX",
            wheelY: "zoomX",
            layout: root.verticalLayout
        }));

        var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
            behavior: "zoomX"
        }));
        cursor.lineY.set("visible", false);

        var colors = chart.get("colors");

        // var data = [
        //     {
        //         "yyyymm": "202201",
        //         "amt": 0,
        //         "sum_amt": 0,
        //         "month": "1월",
        //     },
        // ];


        // Create axes
        var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
            categoryField: "month",
            renderer: am5xy.AxisRendererX.new(root, {
                minGridDistance: 30,
            }),
        }));

        xAxis.get("renderer").labels.template.setAll({
            paddingTop: 20
        });

        xAxis.data.setAll(data);

        var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            min:0,
            renderer: am5xy.AxisRendererY.new(root, {})
        }));

        var yAxisRightsideRenderer = am5xy.AxisRendererY.new(root, {opposite:true});
        var yAxisRightside = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            min:0,
            renderer: yAxisRightsideRenderer,
        }));

        yAxisRightsideRenderer.grid.template.set("forceHidden", true);


        // Add series
        var series = chart.series.push(am5xy.ColumnSeries.new(root, {
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: "cumulationAmt",
            categoryXField: "month",
            tooltip: am5.Tooltip.new(root, {
                labelText: "{valueY}"
            }),
        }));

        series.columns.template.setAll({
            strokeOpacity: 0,
            cornerRadiusTL: 6,
            cornerRadiusTR: 6
        });

        series.columns.template.adapters.add("fill", function(fill, target) {
            return colors.getIndex(series.dataItems.indexOf(target.dataItem));
        })


        let seriesRightsideTooltip = am5.Tooltip.new(root, {
            getFillFromSprite: false,
            labelText: "{valueY}",
        });

        seriesRightsideTooltip.get("background").setAll({
            fill: am5.color(0x9999cc),
            fillOpacity: 0.7
        });

        // pareto series
        var seriesRightside = chart.series.push(am5xy.LineSeries.new(root, {
            xAxis: xAxis,
            yAxis: yAxisRightside,
            valueYField: "monthAmt",
            categoryXField: "month",
            stroke: root.interfaceColors.get("alternativeBackground"),
            maskBullets:false,
            tooltip: seriesRightsideTooltip,
        }));

        seriesRightside.bullets.push(function() {
            return am5.Bullet.new(root, {
                locationY: 1,
                sprite: am5.Circle.new(root, {
                    radius: 5,
                    fill: series.get("fill"),
                    stroke:root.interfaceColors.get("alternativeBackground")
                })
            })
        });

        series.data.setAll(data);
        seriesRightside.data.setAll(data);

        // Make stuff animate on load
        series.appear(1000);
        chart.appear(1000, 100);

    });
}

const chartDataRefinary = function (listData) {
    for(let i = 0; i < listData.length; i++) {
        listData[i].month = listData[i].yyyymm.substring(4, 6).toInt() + '월';
    }
    return listData;
}

/* 년도에 맞춰 상태를 설정하고 각 데이터를 가져와서 배치하기 위한 함수 */
const setDataByYear = function () {
    comms.getSalesChartData({
        filterYear: parseInt($('#filterYear').val()),
    });
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
    setDataByYear();
};