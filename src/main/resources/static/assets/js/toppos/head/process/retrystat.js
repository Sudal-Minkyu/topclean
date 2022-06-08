import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('재세탁입고현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headRetryList',
        targetDate: {
            dataField: 'frYyyymmdd',
            headerText: '접수일자',
        },
        isRetry: true,
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headRetrySubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'frYyyymmdd'});
};
