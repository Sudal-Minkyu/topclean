import {grids, runOnlyOnce} from '../../module/m_headstat.js?22081117';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('강제출고현황(지사)');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headForceReleaseInputList',
        targetDate: {
            dataField: 'fdS7Dt',
            headerText: '강제출고일',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headForceReleaseInputSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS7Dt'});
};
