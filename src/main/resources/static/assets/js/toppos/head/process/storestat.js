import {grids, runOnlyOnce} from '../../module/m_headstat.js?22081117';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('지사입고현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headStoreInputList',
        targetDate: {
            dataField: 'fdS2Dt',
            headerText: '입고일자',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headStoreInputSubList',
        fdUrgentTypeVisible: true,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdS2Dt'});
};
