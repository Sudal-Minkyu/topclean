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
                insertDateTime: "s",
                insert_id: "s",
                subject: "s",
            },
            inspeotList: {
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
            barGraph(data.issueWeekAmountDtos, "1");

            if(data.noticeData) {
                const field = $("#noticeList").children("li").children("a");
                for(let i = 0; i < data.noticeData.length; i++) {
                    $(field[i]).attr("href", `./manager/noticeview?id=${data.noticeData[i].hnId}`);
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
                        .html(reformAftTagNo(inspectList[i].fdTag));
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

function reformAftTagNo(fdTag) {
    return fdTag.substring(3, 4) + "-" + fdTag.substring(4, 7);
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
        }else{
            root = am5.Root.new("franchiseWeekAmount");
        }

        root.setThemes([
            am5themes_Animated.new(root)
        ]);

        var chart = root.container.children.push(am5xy.XYChart.new(root, {
            panX: false,
            panY: false,
        }));

        var cursor = chart.set("cursor", am5xy.XYCursor.new(root, {}));
        cursor.lineY.set("visible", false);

        var xRenderer = am5xy.AxisRendererX.new(root, { minGridDistance: 30 });
        xRenderer.labels.template.setAll({
            rotation: 0,
        });

        var xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
            maxDeviation: 0.2,
            categoryField: "name",
            renderer: xRenderer,
            tooltip: am5.Tooltip.new(root, {})
        }));

        var yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            maxDeviation: 0.5,
            renderer: am5xy.AxisRendererY.new(root, {})
        }));

        var series = chart.series.push(am5xy.ColumnSeries.new(root, {
            name: "Series 1",
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: "value",
            sequencedInterpolation: true,
            categoryXField: "name",
            tooltip: am5.Tooltip.new(root, {
                labelText:"{valueY}"
            })
        }));

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