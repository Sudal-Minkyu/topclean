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
        url: '/api/manager/headReturnReceiptList',
        targetDate: {
            dataField: 'fdS6Dt',
            headerText: '반품출고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/manager/headReturnReceiptSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: true,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getFrList();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS6Dt'});
};
