import {grids, runOnlyOnce} from '../../module/m_managerstat.js';

window.grids = grids;

window.wares = {
    frList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('일반처리현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/manager/headNormalReceiptList',
        targetDate: {
            dataField: 'frYyyymmdd',
            headerText: '접수일자',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/manager/headNormalReceiptSubList',
        fdUrgentTypeVisible: false,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getFrList();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'frYyyymmdd'});
};
