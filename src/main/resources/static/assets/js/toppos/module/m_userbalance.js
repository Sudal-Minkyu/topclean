const runOnlyOnce = {
    // 조회시점 셀렉트 박스 채우기
    initializeDateSelectBox(isDuration = false) {
        const fillYearSelectBox = function (selectBoxElementId) {
            const selectBox = $('#' + selectBoxElementId);
            for (let i = currentYear; i >= startYear; i--) {
                const htmlText = `<option value='${i}'>${i}년</option>`;
                selectBox.append(htmlText);
            }
        }
        const startYear = 2022;
        const today = new Date();
        const currentYear = today.getFullYear();
        const currentMonth = today.getMonth();

        if (isDuration) {
            fillYearSelectBox('filterFromYear');
            fillYearSelectBox('filterToYear');
            $('#filterToMonth option').eq(currentMonth).prop('selected', true);
        } else {
            fillYearSelectBox('filterYear');
            $('#filterMonth option').eq(currentMonth).prop('selected', true);
        }
    },
};

export {runOnlyOnce};
