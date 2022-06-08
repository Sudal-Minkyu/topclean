import {grids, runOnlyOnce} from '../../module/m_managerstat.js';

window.grids = grids;

window.wares = {
    frList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('고객반품인도현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/manager/managerReturnReceiptList',
        targetDate: {
            dataField: 'fdS6Dt',
            headerText: '반품출고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/manager/managerReturnReceiptSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: true,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getFrList();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS6Dt'});
};
