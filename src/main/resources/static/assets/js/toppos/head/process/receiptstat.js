import {grids, runOnlyOnce} from '../../module/m_headstat.js';

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.makeSummaryGrid({
        targetDate: {
            dataField: '',
            headerText: '접수일자',
        },
        targetQty: {
            dataField: '',
            headerText: '접수수량',
        }
    });
};
