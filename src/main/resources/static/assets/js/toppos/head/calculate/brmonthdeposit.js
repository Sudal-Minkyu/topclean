import {runOnlyOnce} from '../../module/m_headbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        메인그리드조회: {
            filterFromYearMonth: 's',    // 조회기간 시작
            filterToYearMonth: 's',    // 조회기간 끝
            branchId: 's',    // 선택된 지사
        },
        입금액저장수정: {
            hsYyyymm: 's',    // 정산월
            brCode: 's',    // 지사코드
            hrReceiptYyyymmdd: 's',    // 입금등록일자
            hrReceiptBrRoyaltyAmt: 'n',    // 입금액(지사로열티)
            hrReceiptFrRoyaltyAmt: 'n',    // 입금액(가맹점로열티)
        },
    },
    receive: {
        메인그리드조회: {
            brName: 's',    // 지사명
            brCode: 's',    // 지사코드
            hsYyyymm: 's',    // 정산월
            hsRolayltyAmtBr: 'n',    // 지사 로열티 정산액
            hsRolayltyAmtFr: 'n',    // 가맹점 로열티 정산액
            hrReceiptBrRoyaltyAmt: 'n',    // 입금액(지사로열티)
            hrReceiptFrRoyaltyAmt: 'n',    // 입금액(가맹점로열티)
        }
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    saveDepositState(saveData) {
        CommonUI.ajax('/api/head/headBranchMonthlySummarySave', 'PARAM', saveData, function (res) {
            alertSuccess('입력 금액을 저장 완료하였습니다.');
            comms.getDepositList(wares.searchCondition);
        });
    },

    getDepositList(searchCondition) {
        wares.searchCondition = searchCondition;
        CommonUI.ajax('/api/head/headBranchReceiptMonthlyList', 'GET', searchCondition, function (res) {
            const data = res.sendData.gridListData;
            grids.set(0, data);
        });
    },
};

/* AUI 그리드 관련 설정과 함수들 */
const grids = {
    id: [
        'grid_main',
    ],
    columnLayout: [],
    prop: [],

    /* 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다. */
    initialization() {

        /* 0번 그리드의 레이아웃 */
        grids.columnLayout[0] = [
            {
                dataField: 'brName',
                headerText: '지사명',
            }, {
                dataField: 'hsYyyymm',
                headerText: '정산월',
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return value.substring(0, 4) + '-' + value.substring(4, 6);
                },
            }, {
                dataField: 'hsRolayltyAmtBr',
                headerText: '지사 로열티',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hrReceiptBrRoyaltyAmt',
                headerText: '지사 로열티 입금',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsRolayltyAmtFr',
                headerText: '가맹점 로열티',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hrReceiptFrRoyaltyAmt',
                headerText: '가맹점 로열티 입금',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            },
        ];

        /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
        * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
        * */
        grids.prop[0] = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: false,
            showRowCheckColumn: false,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            rowHeight : 48,
            headerHeight : 48,
        };

    },

    /* 그리드 동작 처음 빈 그리드를 생성 */
    create() {
        for (const i in grids.columnLayout) {
            AUIGrid.create(grids.id[i], grids.columnLayout[i], grids.prop[i]);
        }
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    get(gridNum) {
        return AUIGrid.getGridData(grids.id[gridNum]);
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    set(gridNum, data) {
        AUIGrid.setGridData(grids.id[gridNum], data);
    },

    /* 해당 배열 번호 그리드의 데이터 비우기 */
    clear(gridNum) {
        AUIGrid.clearGridData(grids.id[gridNum]);
    },

    /* 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절 */
    resize(gridNum) {
        AUIGrid.resize(grids.id[gridNum]);
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.id[0], 'cellClick', function (e) {
            onSelectItem(e.item);
        });

        $('#hrReceiptBrRoyaltyAmt, #hrReceiptFrRoyaltyAmt').on("keyup", function () {
            this.value = this.value.numberInput();
        });

        $('#saveDepositState').on('click', function () {
            saveDepositState();
        });

        $('#searchListBtn').on('click', function () {
            getDepositList();
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    selectedItem: {},
};

/* 날짜 입력관련 인풋 초기값 할당 및 데이트피커 활성화 */
function enableDatepicker() {
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "hrReceiptYyyymmdd"
    ];

    $("#" + datePickerTargetIds[0]).val(today);

    CommonUI.setDatePicker(datePickerTargetIds);
}

/* 입력받은 입금내역을 저장 */
function saveDepositState() {
    if (!wares.selectedItem.hsYyyymm) {
        alertCaution('먼저, 입력을 원하는 항목을 선택해 주세요.', 1);
        return;
    }

    const saveData = {
        hsYyyymm: wares.selectedItem.hsYyyymm,
        brCode: wares.selectedItem.brCode,
        hrReceiptYyyymmdd: $('#hrReceiptYyyymmdd').val().numString(),
        hrReceiptBrRoyaltyAmt: $('#hrReceiptBrRoyaltyAmt').val().toInt(),
        hrReceiptFrRoyaltyAmt: $('#hrReceiptFrRoyaltyAmt').val().toInt(),
    }

    comms.saveDepositState(saveData);
}

/* 조회 */
function getDepositList() {
    const searchCondition = {
        filterFromYearMonth: $('#filterFromYear').val() + $('#filterFromMonth').val(),
        filterToYearMonth: $('#filterToYear').val() + $('#filterToMonth').val(),
        branchId: parseInt($('#brList').val()),
    }
    if (!searchCondition.branchId) {
        alertCaution('조회하실 지사를 선택해 주세요', 1);
        return;
    }
    comms.getDepositList(searchCondition);
}

const onSelectItem = function (item) {
    wares.selectedItem = item;
    $('#hsYyyymm').val(item.hsYyyymm.substring(0, 4) + '-' + item.hsYyyymm.substring(4, 6));
    $('#hsRolayltyAmtBr').val(item.hsRolayltyAmtBr.toLocaleString());
    $('#hsRolayltyAmtFr').val(item.hsRolayltyAmtFr.toLocaleString());

    const today = new Date().format("yyyy-MM-dd");
    $("#hrReceiptYyyymmdd").val(today);
    $('#hrReceiptBrRoyaltyAmt').val('');
    $('#hrReceiptFrRoyaltyAmt').val('');
};

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    runOnlyOnce.initializeDateSelectBox(true);
    runOnlyOnce.activateBrFrListInputs();
    grids.initialization();
    grids.create();
    enableDatepicker();

    trigs.basic();
};
