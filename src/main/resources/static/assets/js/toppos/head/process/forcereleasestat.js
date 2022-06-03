import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('강제 출고 현황 (지사)');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headForceReleaseInputList',
        targetDate: {
            dataField: 'fdS7Dt',
            headerText: '강제출고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headReleaseInputSubList',
        fdUrgentTypeVisible: true,
        fiProgressStateDtVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS7Dt'});
};
