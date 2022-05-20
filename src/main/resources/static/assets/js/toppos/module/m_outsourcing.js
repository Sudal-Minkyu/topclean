/*
 * 지사의 외주 관리 메뉴에서 공통 적으로 쓰이는 함수들의 정리 (외주관리가격설정 제외)
 * */

const dtos = {
    send: {
    },
    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr",
            frName: "s",
            frTagNo: "s",
        },
    },
};

const wares = {

};

const grids = {
    create(id, layout, property) {
        AUIGrid.create(id, layout, property);
    },

    setData(id, data) {
        AUIGrid.setGridData(id, data);
    },
};

const runOnlyOnce = {
    /* 데이트 피커의 가동과 날짜 선택의 제한 */
    enableDatepicker() {
        const today = new Date().format('yyyy-MM-dd');

        /* datepicker를 적용시킬 대상들의 dom id들 */
        const datePickerTargetIds = [
            'filterFromDt', 'filterToDt',
        ];

        $('#' + datePickerTargetIds[0]).val(today);
        $('#' + datePickerTargetIds[1]).val(today);

        const dateAToBTargetIds = [
            ['filterFromDt', 'filterToDt'],
        ];

        CommonUI.setDatePicker(datePickerTargetIds);
        CommonUI.restrictDateAToB(dateAToBTargetIds);
    },

    /* 셀렉트 박스에 현 지점에 속한 가맹점들을 표시 */
    getFrList() {
        CommonUI.ajax('/api/manager/branchBelongList', 'GET', false, function (res) {
            const data = res.sendData.franchiseList;
            dv.chk(data, dtos.receive.managerBelongList, '가맹점 선택출고에 필요한 지점에 속한 가맹점 받아오기');
            const $frList = $('#frList');
            data.forEach(obj => {
                const htmlText = `<option value=${obj.frId}>${obj.frName}</option>`;
                $frList.append(htmlText);
            });
        });
    },
};

export {grids, runOnlyOnce};


