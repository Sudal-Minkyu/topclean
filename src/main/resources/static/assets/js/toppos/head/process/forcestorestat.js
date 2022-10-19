import {grids, runOnlyOnce} from '../../module/m_headstat.js?22081117';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('강제입고현황(가맹점)');

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
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS8Dt'});
};
