const dtos = {
    send: {

    },
    receive: {
        branchBelongList: {
            branchId: 'n',
            franchiseList: {
                frId: 'n',
                frName: 's',
                frTagNo: 's',
            },
        },
    }
}

const comms = {
    /* 셀렉트 박스에 현 지점에 속한 가맹점들을 표시 */
    getFrList() {
        CommonUI.ajax('/api/manager/branchBelongList', 'GET', false, function (res) {
            const data = res.sendData;
            if (window.branchId === 0) {
                window.branchId = data.branchId;
            }
            dv.chk(data, dtos.receive.branchBelongList, '가맹점 선택출고에 필요한 지점에 속한 가맹점 받아오기');
            const $frList = $('#frList');
            data.franchiseList.forEach(obj => {
                const htmlText = `<option value=${obj.frId}>${obj.frName}</option>`;
                $frList.append(htmlText);
            });
        });
    },
};

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

    getFrList: comms.getFrList,
};

export {runOnlyOnce};
