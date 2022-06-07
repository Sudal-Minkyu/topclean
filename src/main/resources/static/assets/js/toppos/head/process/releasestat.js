import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('지사 출고 현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headReleaseInputList',
        targetDate: {
            dataField: 'fdS4Dt',
            headerText: '출고일자',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headReleaseInputSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS4Dt'});
};
