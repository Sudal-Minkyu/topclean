import {grids, runOnlyOnce} from '../../module/m_headstat.js';

window.grids = grids;

window.wares = {
    brFrList: {},
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.setXlsxTitleName('일반처리현황');

    runOnlyOnce.makeSummaryGrid({
        url: '/api/head/headNormalReceiptList',
        targetDate: {
            dataField: 'frYyyymmdd',
            headerText: '접수일자',
        },
    });

    runOnlyOnce.makeDetailGrid({
        url: '/api/head/headNormalReceiptSubList',
        fdUrgentTypeVisible: false,
        fdS6TypeVisible: false,
    });

    runOnlyOnce.enableDatepicker();

    runOnlyOnce.activateBrFrListInputs();

    runOnlyOnce.setCommonEvents();

    runOnlyOnce.setGridEvents({targetDateName: 'frYyyymmdd'});
};
