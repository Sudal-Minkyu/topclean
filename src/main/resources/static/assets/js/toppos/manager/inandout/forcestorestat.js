import {grids, runOnlyOnce} from '../../module/m_managerstat.js?22081117';

window.grids = grids;

window.wares = {
    frList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('강제입고현황(가맹점)');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/manager/branchForceStoreInputList',
        targetDate: {
            dataField: 'fdS8Dt',
            headerText: '강제입고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/manager/branchForceStoreInputSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.getFrList();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS8Dt'});
};
