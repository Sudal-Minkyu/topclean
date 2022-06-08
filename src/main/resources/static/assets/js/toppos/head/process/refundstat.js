import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('고객반품인도현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headReturnReceiptList',
        targetDate: {
            dataField: 'fdS6Dt',
            headerText: '반품출고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headReturnReceiptSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: true,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS6Dt'});
};
