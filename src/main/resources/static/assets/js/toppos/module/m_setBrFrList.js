/*
 * html 내부의 #brList, #frList 아이디가 붙은 셀렉트 박스에
 * 서버로부터 받은 지사와 가맹점의 리스트를 세팅하고,
 * #brList의 내용이 바뀔 때 의도된 동작을 할 수 있도록 해준다.
 * */

let brFrListData = {};

/* 지사와 가맹점의 이름과 id들을 콜백으로 반환한다. */
const getBrFrList = function () {
    CommonUI.ajax('/api/head/headBrFrInfoList', 'GET', false, function (res) {
        brFrListData = res.sendData;
        setBrFrList(brFrListData, 0, true);
    });
};

/* 처음 시작시 지사 리스트를 셀렉트박스에 세팅하고, 지사 선택시 가맹점 리스트를 세팅 */
const setBrFrList = function (brFrList, selectedBranchId, isInitialization = false) {
    if (isInitialization) {
        for (const {branchId, brName} of brFrList.branchList) {
            $('#brList').append(`
                <option value='${branchId}'>${brName}</option>
            `);
        }
    } else {
        const $frList = $('#frList');
        $frList.html(`<option value='0'>전체선택</option>`);
        if (selectedBranchId) {
            for (const {franchiseId, branchId, frName} of brFrList.franchiseList) {
                if (branchId === selectedBranchId) {
                    $frList.append(`
                        <option value='${franchiseId}'>${frName}</option>
                    `);
                }
            }
            $frList.prop('disabled', false);
        } else {
            $frList.val('0').prop('disabled', true);
        }
    }
};

const activateBrFrListInputs = function () {
    getBrFrList();

    $('#brList').on('change', function () {
        setBrFrList(brFrListData, parseInt($('#brList').val(), 10));
    });
};

export {activateBrFrListInputs};
