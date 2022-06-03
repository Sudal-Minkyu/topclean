import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('강제 입고 현황 (가맹점)');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headForceStoreInputList',
        targetDate: {
            dataField: 'fdS8Dt',
            headerText: '강제입고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headForceStoreInputSubList',
        fdUrgentTypeVisible: true,
        fiProgressStateDtVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS8Dt'});
};
