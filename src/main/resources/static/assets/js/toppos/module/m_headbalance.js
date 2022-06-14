import {activateBrFrListInputs} from './m_setBrFrList.js';

const runOnlyOnce = {
    // 조회시점 셀렉트 박스 채우기
    initializeDateSelectBox() {
        const startYear = 2022;
        const today = new Date();

        const currentYear = today.getFullYear();
        const $filterYear = $('#filterYear');
        for (let i = currentYear; i >= startYear; i--) {
            const htmlText = `<option value='${i}'>${i}년</option>`;
            $filterYear.append(htmlText);
        }

        const currentMonth = today.getMonth();
        $('#filterMonth option').eq(currentMonth).prop('selected', true);
    },

    activateBrFrListInputs: activateBrFrListInputs,
};

export {runOnlyOnce};
