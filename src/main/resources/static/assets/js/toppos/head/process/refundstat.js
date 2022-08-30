import {grids, runOnlyOnce} from '../../module/m_headstat.js?22081117';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('고객반품현황');

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
