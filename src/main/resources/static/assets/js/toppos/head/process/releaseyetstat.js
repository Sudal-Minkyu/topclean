import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('미출고 현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headNoReleaseInputList',
        targetDate: {
            dataField: 'fdEstimateDt',
            headerText: '출고예정일자',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headReceiptSubList',
        fdUrgentTypeVisible: true,
        fiProgressStateDtVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'fdEstimateDt'});
};
