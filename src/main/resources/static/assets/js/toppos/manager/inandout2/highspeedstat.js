import {grids, runOnlyOnce} from '../../module/m_managerstat.js';

window.grids = grids;

window.wares = {
    frList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('급세탁처리현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/manager/managerQuickReceiptList',
        targetDate: {
            dataField: 'frYyyymmdd',
            headerText: '접수일자',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/manager/managerQuickReceiptSubList',
        fdUrgentTypeVisible: false,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getFrList();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'frYyyymmdd'});
};
