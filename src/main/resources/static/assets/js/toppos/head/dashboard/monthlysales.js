/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        조회년도: {
            filterYear: "s",
        },
        지사별매출조회: {
            filterYear: "s",
            brId: "s"
        }
    },
    receive: {
        지사매출순위: {

        },
        가맹점별매출: {

        }
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    // 년도별 지사매출 가져오기
    getBrSalesList() {
        // CommonUI.ajax("지사매출api", "GET", filterYear, function(res) {
        //     const data = res;
        //     console.log(data);
        //     // dv.chk(data, dtos.receive.지사매출순위, "지사매출순위 받아오기");
        //     // grids.f.clear(0);
        //     // grids.f.set(0, data);
        // })
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
                    dataField: '',
                    headerText: '지사명',
                },{
                    dataField: '',
                    headerText: '1월',
                }, {
                    dataField: '',
                    headerText: '2월',
                },
                {
                    dataField: '',
                    headerText: '3월',
                }, {
                    dataField: '',
                    headerText: '4월',
                },
                {
                    dataField: '',
                    headerText: '5월',
                }, {
                    dataField: '',
                    headerText: '6월',
                },
                {
                    dataField: '',
                    headerText: '7월',
                }, {
                    dataField: '',
                    headerText: '8월',
                },
                {
                    dataField: '',
                    headerText: '9월',
                }, {
                    dataField: '',
                    headerText: '10월',
                },
                {
                    dataField: '',
                    headerText: '11월',
                }, {
                    dataField: '',
                    headerText: '12월',
                }, {
                    dataField: '',
                    headerText: '전체',
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
                enableColumnResize : false,
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
                    headerText: '지사명',
                },{
                    dataField: '',
                    headerText: '1월',
                }, {
                    dataField: '',
                    headerText: '2월',
                },
                {
                    dataField: '',
                    headerText: '3월',
                }, {
                    dataField: '',
                    headerText: '4월',
                },
                {
                    dataField: '',
                    headerText: '5월',
                }, {
                    dataField: '',
                    headerText: '6월',
                },
                {
                    dataField: '',
                    headerText: '7월',
                }, {
                    dataField: '',
                    headerText: '8월',
                },
                {
                    dataField: '',
                    headerText: '9월',
                }, {
                    dataField: '',
                    headerText: '10월',
                },
                {
                    dataField: '',
                    headerText: '11월',
                }, {
                    dataField: '',
                    headerText: '12월',
                }, {
                    dataField: '',
                    headerText: '전체',
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
                enableColumnResize : false,
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
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.s.id[0], 'cellClick', function (e) {
            console.log(e.item);
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

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
    console.log(filename + "_" + getDate(year));
}

function makeChart() {
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

        var data = [
            {
                "yyyymm": "202201",
                "amt": 0,
                "sum_amt": 0,
                "month": "1월",
            },
            {
                "yyyymm": "202202",
                "amt": 478000,
                "sum_amt": 478000,
                "month": "2월",
            },
            {
                "yyyymm": "202203",
                "amt": 0,
                "sum_amt": 478000,
                "month": "3월",
            },
            {
                "yyyymm": "202204",
                "amt": 975722,
                "sum_amt": 1453722,
                "month": "4월",
            },
            {
                "yyyymm": "202205",
                "amt": 161455,
                "sum_amt": 1615177,
                "month": "5월",
            },
            {
                "yyyymm": "202206",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "6월",
            },
            {
                "yyyymm": "202207",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "7월",
            },
            {
                "yyyymm": "202208",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "8월",
            },
            {
                "yyyymm": "202209",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "9월",
            },
            {
                "yyyymm": "202210",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "10월",
            },
            {
                "yyyymm": "202211",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "11월",
            },
            {
                "yyyymm": "202212",
                "amt": 0,
                "sum_amt": 1615177,
                "month": "12월",
            },
        ];


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
            valueYField: "amt",
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
            valueYField: "sum_amt",
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

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    getYear();
    getFilterYear();

    grids.f.initialization();

    trigs.basic();
    makeChart();
};