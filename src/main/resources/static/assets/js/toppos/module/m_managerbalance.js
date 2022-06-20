const dtos = {
    send: {

    },
    receive: {
        managerBelongList: {
            frId: 'n',
            frName: 's',
            frTagNo: 's',
        },
    }
}

const comms = {
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

const runOnlyOnce = {
    getFrList: comms.getFrList,
};

export {runOnlyOnce};
