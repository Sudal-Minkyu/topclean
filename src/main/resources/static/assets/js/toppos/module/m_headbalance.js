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

    /* 조회쪽의 지사와 가맹점의 셀렉트 박스의 내용물들을 구성하고 동작을 통제하는 함수 */
    activateBrFrListInputs: activateBrFrListInputs,
};

export {runOnlyOnce};
