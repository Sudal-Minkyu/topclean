/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */

import {activateBrFrListInputs} from '../../module/m_setBrFrList.js';

const dtos = {
    send: {

    },
    receive: {

    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getRatioData(filterCondition) {
        CommonUI.ajax('/api/head/headCustomerGenderRateStatus', "GET", filterCondition, function (res) {
            const data = res.sendData.listData;
            makeDonut('genderChart', data, 'gender', 'rate');
        });
        CommonUI.ajax('/api/head/headCustomerAgeRateStatus', "GET", filterCondition, function (res) {
            const data = res.sendData.listData;
            makeDonut('ageChart', data, 'age', 'rate');
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#searchListBtn').on('click', function () {
            searchSummaryData();
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
};

const makeDonut = function (chartDivId, data, categoryName, valueName) {
    am5.ready(function() {
        if (wares[chartDivId]) {
            wares[chartDivId].root.dispose();
        }

        wares[chartDivId] = {};

        // Create root element
        // https://www.amcharts.com/docs/v5/getting-started/#Root_element
        wares[chartDivId].root = am5.Root.new(chartDivId);

        const root = wares[chartDivId].root;

        // Set themes
        // https://www.amcharts.com/docs/v5/concepts/themes/
        root.setThemes([
            am5themes_Animated.new(root)
        ]);

        // Create chart
        // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/
        var chart = root.container.children.push(
            am5percent.PieChart.new(root, {
                layout: root.verticalLayout,
                innerRadius: am5.percent(50)
            })
        );

        // Create series
        // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series
        var series = chart.series.push(
            am5percent.PieSeries.new(root, {
                valueField: valueName,
                categoryField: categoryName,
                endAngle: 270
            })
        );

        series.states.create("hidden", {
            endAngle: -90
        });

        /* 성별차트의 색 조합과 연령대차트의 색 조합 리스트를 설정하고 할당 */
        const pieChartColorSet = {
            genderChart: [
                am5.color(0x289fac),
                am5.color(0xdbbf26),
            ],
        }
        if (pieChartColorSet[chartDivId]) {
            series.get("colors").set("colors", pieChartColorSet[chartDivId]);
        }

        // Set data
        // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data
        series.data.setAll(data);

        series.appear(1000, 100);
    });
};

/* 조회 조건에 따라 조회가 되도록 한다 */
const searchSummaryData = function () {
    const filterCondition = {
        branchId: parseInt($('#brList').val(), 10),
        franchiseId: parseInt($('#frList').val(), 10),
    };

    comms.getRatioData(filterCondition);
};

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    activateBrFrListInputs();
    comms.getRatioData({
        branchId: 0,
        franchiseId: 0,
    });
    trigs.basic();
};
