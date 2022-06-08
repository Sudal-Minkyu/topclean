import {grids, runOnlyOnce} from '../../module/m_managerstat.js';

window.grids = grids;

window.wares = {
    frList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('재세탁입고현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/manager/managerRetryList',
        targetDate: {
            dataField: 'frYyyymmdd',
            headerText: '접수일자',
        },
        isRetry: true,
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/manager/managerRetrySubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getFrList();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'frYyyymmdd'});
};
