/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {

    },
    receive: {
        branchInfo: {
            noticeData: {
                hnId: "n",
                hnType: "s",
                insertDateTime: "s",
                insert_id: "s",
                subject: "s",
            },
            inspeotList: {
                fiId: "n",
                frName: "s", // 2022.03.08 추가
                bcName: "s",
                bgName: "s",
                fdS2Dt: "s",
                fdTag: "s",
                fiCustomerConfirm: "s",
                frYyyymmdd: "s",
            },
            issueWeekAmountDtos: {
                name: "s",
                value: "n",
            },
            chartFranchOpenData: {
                category: "s",
                value: "n",
            },
            requestWeekAmountData: {
                name: "s",
                value: "n",
            },
            requestWeekDaysAmountDtos: {
                name: "s",
                value: "n",
            },
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    branchInfo: "/api/manager/branchInfo",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    /* 페이지 초기 화면에 뿌려지는 정보들을 받아온다. */
    branchInfo() {
        CommonUI.ajax(urls.branchInfo, "GET", null, function (res) {
            const data = res.sendData;
            const inspectList = data.inspeotList;
            dv.chk(data, dtos.receive.branchInfo, "인덱스페이지 데이터 받기");
            pieGraph(data.chartFranchOpenData);
            barGraph(data.requestWeekAmountData, "0");
            // console.log(data.requestWeekAmountData);
            barGraph(data.requestWeekDaysAmountDtos, "1");
            // console.log(data.requestWeekDaysAmountDtos);
            barGraph(data.issueWeekAmountDtos, "2");
            // console.log(data.issueWeekAmountDtos);

            // clusteredBarGraph(); // 현재는 더미데이터 이므로 실제 데이터가 와야 한다.

            if(data.noticeData) {
                const field = $("#noticeList").children("li").children("a");
                for(let i = 0; i < data.noticeData.length; i++) {
                    $(field[i]).attr("href", `./manager/noticeview?id=${data.noticeData[i].hnId}`);
                    $(field[i]).children(".main__board-frname").children("span")
                        .html(CommonData.name.hnType[data.noticeData[i].hnType]);
                    $(field[i]).children(".main__board-title").children("span").html(data.noticeData[i].subject);
                    $(field[i]).children(".main__board-date").children("span").html(data.noticeData[i].insertDateTime);
                }
            }

            if(inspectList) {
                const field = $("#inspectList").children("li").children("a");
                field.children(".main__board-frname").children("span").html("");
                field.children(".main__board-bcname").children("span").html("");
                field.children(".main__board-bgname").children("span").html("");
                field.children(".main__board-afttag").children("span").html("");
                field.children(".main__board-confirm").children("span").html("");

                for(let i = 0; i < inspectList.length; i++) {
                    $(field[i]).attr("href", `./manager/checkstate?fdTag=${inspectList[i].fdTag}&fdS2Dt=${
                        inspectList[i].fdS2Dt}`);
                    $(field[i]).children(".main__board-frname").children("span")
                        .html("&lt;" + inspectList[i].frName + "&gt;");
                    $(field[i]).children(".main__board-bcname").children("span").html(inspectList[i].bcName);
                    $(field[i]).children(".main__board-bgname").children("span").html(inspectList[i].bgName);
                    $(field[i]).children(".main__board-afttag").children("span")
                        .html(CommonData.formatBrTagNo(inspectList[i].fdTag));
                    $(field[i]).children(".main__board-confirm").children("span")
                        .html(wares.fiCustomerConfirmName[inspectList[i].fiCustomerConfirm]);
                }
            }
        });
    },

    taglost() {
        CommonUI.ajax(urls.taglost, "PARAM", wares.boardConditionThree, function (res) {
            const data = res.datalist;
            if(data) {
                const field = $("#taglostList").children("li").children("a");;
                for(let i = 0; i < data.length; i++) {
                    $(field[i]).attr("href", `./user/taglostview?id=${data[i].htId}`);
                    $(field[i]).children(".main__board-title").children("span:nth-child(1)").html(data[i].subject);
                    $(field[i]).children(".main__board-title").children("span:nth-child(2)").html("(" + data[i].numOfComment + ")");
                    $(field[i]).children(".main__board-date").children("span").html(data[i].insertDateTime);
                }
            }
        });
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    basic() {

    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    fiCustomerConfirmName: {
        "1": "(미처리)",
        "2": "(수락)",
        "3": "(거부)",
    },
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    comms.branchInfo();
    trigs.basic();
}

function pieGraph(data) {
    am5.ready(function () {

        var root = am5.Root.new("franchiseOpen");

        root.setThemes([
            am5themes_Animated.new(root)
        ]);

        var chart = root.container.children.push(am5percent.PieChart.new(root, {
            layout: root.verticalLayout
        }));

        var series = chart.series.push(am5percent.PieSeries.new(root, {
            valueField: "value",
            categoryField: "category"
        }));

        series.data.setAll(data);

        var legend = chart.children.push(am5.Legend.new(root, {
            centerX: am5.percent(50),
            x: am5.percent(50),
            marginTop: 15,
            marginBottom: 15
        }));

        legend.data.setAll(series.dataItems);
        series.appear(1000, 100);

    }); // end am5.ready()
}

function barGraph(data, num) {
    am5.ready(function () {

        let root;
        if(num === "0"){
            root = am5.Root.new("franchiseWeekReceipt");
        }else if(num === "1"){
            root = am5.Root.new("franchiseDayReceipt");
        }else{
            root = am5.Root.new("franchiseWeekAmount");
        }

        root.setThemes([
            am5themes_Animated.new(root)
        ]);

        const chart = root.container.children.push(am5xy.XYChart.new(root, {
            panX: false,
            panY: false,
        }));

        const cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
        cursor.lineY.set("visible", false);

        const xRenderer = am5xy.AxisRendererX.new(root, { minGridDistance: 30 });
        xRenderer.labels.template.setAll({
            rotation: 0,
        });

        const xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
            maxDeviation: 0.2,
            categoryField: "name",
            renderer: xRenderer,
            tooltip: am5.Tooltip.new(root, {})
        }));

        const yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            maxDeviation: 0.5,
            renderer: am5xy.AxisRendererY.new(root, {})
        }));

        const series = chart.series.push(am5xy.ColumnSeries.new(root, {
            name: "Series 1",
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: "value",
            sequencedInterpolation: true,
            categoryXField: "name",
            tooltip: am5.Tooltip.new(root, {
                labelText: "{valueY}"
            })
        }));

        // series.columns.template.events.on("click", function(e) {
        //     console.log(e.target);
        // });

        series.columns.template.setAll({ cornerRadiusTL: 5, cornerRadiusTR: 5 });
        series.columns.template.adapters.add("fill", function(fill, target) {
            return chart.get("colors").getIndex(series.columns.indexOf(target));
        });

        series.columns.template.adapters.add("stroke", function(stroke, target) {
            return chart.get("colors").getIndex(series.columns.indexOf(target));
        });

        xAxis.data.setAll(data);
        series.data.setAll(data);

        series.appear(1000);
        chart.appear(1000, 100);

    });
}

// function clusteredBarGraph(data) {
//     am5.ready(function() {
//
//         // Create root element
//         // https://www.amcharts.com/docs/v5/getting-started/#Root_element
//         var root = am5.Root.new("franchiseDayReceipt");
//
//
//         // Set themes
//         // https://www.amcharts.com/docs/v5/concepts/themes/
//         root.setThemes([
//           am5themes_Animated.new(root)
//         ]);
//
//
//         // Create chart
//         // https://www.amcharts.com/docs/v5/charts/xy-chart/
//         var chart = root.container.children.push(am5xy.XYChart.new(root, {
//           panX: false,
//           panY: false,
//           wheelX: "panX",
//           wheelY: "zoomX",
//           layout: root.verticalLayout
//         }));
//
//
//         // Add legend
//         // https://www.amcharts.com/docs/v5/charts/xy-chart/legend-xy-series/
//         var legend = chart.children.push(
//           am5.Legend.new(root, {
//             centerX: am5.p50,
//             x: am5.p50
//           })
//         );
//
//         var data = [{
//           "date": "03.17",
//           "브로드웨이브": 250000,
//           "서정마을": 250000,
//           "테스트가맹": 210000,
//         }, {
//           "date": "03.18",
//           "브로드웨이브": 260000,
//           "서정마을": 270000,
//           "테스트가맹": 220000,
//         }, {
//           "date": "03.19",
//           "브로드웨이브": 280000,
//           "서정마을": 290000,
//           "테스트가맹": 240000,
//         }];
//
//
//         // Create axes
//         // https://www.amcharts.com/docs/v5/charts/xy-chart/axes/
//         var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
//           categoryField: "date",
//           renderer: am5xy.AxisRendererX.new(root, {
//             cellStartLocation: 0.1,
//             cellEndLocation: 0.9
//           }),
//           tooltip: am5.Tooltip.new(root, {})
//         }));
//
//         xAxis.data.setAll(data);
//
//         var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
//           renderer: am5xy.AxisRendererY.new(root, {})
//         }));
//
//
//         // Add series
//         // https://www.amcharts.com/docs/v5/charts/xy-chart/series/
//         function makeSeries(name, fieldName) {
//           var series = chart.series.push(am5xy.ColumnSeries.new(root, {
//             name: name,
//             xAxis: xAxis,
//             yAxis: yAxis,
//             valueYField: fieldName,
//             categoryXField: "date"
//           }));
//
//           series.columns.template.setAll({
//             tooltipText: "{name}, {categoryX}:{valueY}",
//             width: am5.percent(90),
//             tooltipY: 0
//           });
//
//           series.data.setAll(data);
//
//           // Make stuff animate on load
//           // https://www.amcharts.com/docs/v5/concepts/animations/
//           series.appear();
//
//           series.bullets.push(function () {
//             return am5.Bullet.new(root, {
//               locationY: 0,
//               sprite: am5.Label.new(root, {
//                 text: "{valueY}",
//                 fill: root.interfaceColors.get("alternativeText"),
//                 centerY: 0,
//                 centerX: am5.p50,
//                 populateText: true
//               })
//             });
//           });
//
//           legend.data.push(series);
//         }
//
//         makeSeries("브로드웨이브", "브로드웨이브");
//         makeSeries("서정마을", "서정마을");
//         makeSeries("테스트가맹", "테스트가맹");
//
//         // Make stuff animate on load
//         // https://www.amcharts.com/docs/v5/concepts/animations/
//         chart.appear(1000, 100);
//         }); // end am5.ready()
// }

