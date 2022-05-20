import {grids, runOnlyOnce, setBrFrList} from '../../module/m_headstat.js';

window.wares = {
    brFrList: {},
}

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('실시간 접수 현황');

    runOnlyOnce.makeSummaryGrid({
        targetDate: {
            dataField: 'frYyyymmdd',
            headerText: '접수일자',
        },
        targetQty: {
            dataField: 'requestCount',
            headerText: '접수수량',
        },
    });

    runOnlyOnce.makeDetailGrid();

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getBrFrList(function (brFrList) {
        wares.brFrList = brFrList;
        setBrFrList(wares.brFrList, 0, true);
    });

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'frYyyymmdd'});
};
