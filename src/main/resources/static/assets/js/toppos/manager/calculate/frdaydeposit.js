import {runOnlyOnce} from '../../module/m_managerbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        managerFranchiseReceiptMonthlyList: {
            filterFromDt: 's',    // 조회기간 시작
            filterToDt: 's',    // 조회기간 끝
            franchiseId: 's',    // 선택된 지사
        },
        managerFranchiseMonthlySummarySave: {
            hsYyyymmdd: 's',    // 일정산일자
            frCode: 's',    // 가맹점코드
            hrReceiptYyyymmdd: 's',    // 입금등록일자
            hrReceiptSaleAmt: 'n',    // 입금액 (가맹점의 지사 입금액)
            hrReceiptRoyaltyAmt: 'n',    // 입금액 (가맹점 본사 로열티)
        },
    },
    receive: {
        managerFranchiseReceiptMonthlyList: {
            frName: 's',    // 가맹점명
            frCode: 's',    // 가맹점코드
            hsYyyymmdd: 's',    // 정산월
            hsSettleAmtBr: 'n',    // 지사 매출액 (정산 대상 금액)
            hsRolayltyAmtFr: 'n',    // 가맹점 로열티 정산액
            hrReceiptSaleAmt: 'n',    // 입금액(가맹점의 지사 입금액)
            hrReceiptRoyaltyAmt: 'n',    // 입금액 (가맹점 본사 로열티)
        }
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    saveDepositState(saveData) {
        CommonUI.ajax('/api/manager/branchDailySummarySave', 'PARAM', saveData, function () {
            alertSuccess('입력 금액을 저장 완료하였습니다.');
            comms.getDepositList(wares.searchCondition);
        });
    },

    getDepositList(searchCondition) {
        wares.searchCondition = searchCondition;
        dv.chk(searchCondition, dtos.send.managerFranchiseReceiptMonthlyList, '입금 등록 내역 조회조건 보내기');
        CommonUI.ajax('/api/manager/branchReceiptDailyList', 'GET', searchCondition, function (res) {
            wares.xlsxNaming.filterFromDt = searchCondition.filterFromDt;
            wares.xlsxNaming.filterToDt = searchCondition.filterToDt;
            wares.xlsxNaming.franchiseName = $('#frList option:selected').html();
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.managerFranchiseReceiptMonthlyList, '입금 등록 내역 리스트 받아오기');
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
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: 'hsYyyymmdd',
                headerText: '정산 일자',
                dataType: "date",
                formatString: "yyyy-mm-dd",
            }, {
                dataField: 'hsSettleAmtBr',
                headerText: '정산 대상 금액',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hrReceiptSaleAmt',
                headerText: '정산액 지사 입금',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hsRolayltyAmtFr',
                headerText: '본사 로열티',
                style: 'grid_textalign_right',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'hrReceiptRoyaltyAmt',
                headerText: '로열티 입금',
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

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[0])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[0], {
            fileName : wares.xlsxNaming.title + '_' + wares.xlsxNaming.filterFromDt + '_'
                + wares.xlsxNaming.filterToDt + '_' + wares.xlsxNaming.franchiseName,
            progressBar : true,
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.id[0], 'cellClick', function (e) {
            onSelectItem(e.item);
        });

        $('#hrReceiptSaleAmt, #hrReceiptRoyaltyAmt').on("keyup", function () {
            this.value = this.value.numberInput();
        });

        $('#saveDepositState').on('click', function () {
            saveDepositState();
        });

        $('#searchListBtn').on('click', function () {
            getDepositList();
        });

        $("#exportXlsx").on("click", function () {
            if(grids.get(0).length) {
                grids.exportToXlsx();
            } else {
                alertCaution("엑셀 다운로드를 실행할 데이터가 없습니다.<br>먼저 조회를 해주세요.", 1);
            }
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    selectedItem: {},
    xlsxNaming: {
        title: '가맹점일정산입금등록',
        filterFromDt: '',
        filterToDt: '',
        franchiseName: '',
    },
};

/* 날짜 입력관련 인풋 초기값 할당 및 데이트피커 활성화 */
function enableDatepicker() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 30);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt", "hrReceiptYyyymmdd"
    ];


    $("#" + datePickerTargetIds[0]).val(fromday);
    $("#" + datePickerTargetIds[1]).val(today);
    $("#" + datePickerTargetIds[2]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

/* 입력받은 입금내역을 저장 */
function saveDepositState() {
    if (!wares.selectedItem.hsYyyymmdd) {
        alertCaution('먼저, 입력을 원하는 항목을 선택해 주세요.', 1);
        return;
    }

    const saveData = {
        hsYyyymmdd: wares.selectedItem.hsYyyymmdd,
        frCode: wares.selectedItem.frCode,
        hrReceiptYyyymmdd: $('#hrReceiptYyyymmdd').val().numString(),
        hrReceiptSaleAmt: $('#hrReceiptSaleAmt').val().toInt(),
        hrReceiptRoyaltyAmt: $('#hrReceiptRoyaltyAmt').val().toInt(),
    }

    comms.saveDepositState(saveData);
}

/* 조회 */
function getDepositList() {
    const searchCondition = {
        filterFromDt: $('#filterFromDt').val().numString(),
        filterToDt: $('#filterToDt').val().numString(),
        franchiseId: parseInt($('#frList').val()),
    }
    if (!searchCondition.franchiseId) {
        alertCaution('조회하실 가맹점을 선택해 주세요', 1);
        return;
    }
    comms.getDepositList(searchCondition);
}

const onSelectItem = function (item) {
    wares.selectedItem = item;
    $('#hsYyyymmdd').val(item.hsYyyymmdd.substring(0, 4) + '-' + item.hsYyyymmdd.substring(4, 6)
        + '-' + item.hsYyyymmdd.substring(6, 8));
    $('#hsSettleAmtBr').val(item.hsSettleAmtBr.toLocaleString());
    $('#hsRolayltyAmtFr').val(item.hsRolayltyAmtFr.toLocaleString());

    const today = new Date().format("yyyy-MM-dd");
    $("#hrReceiptYyyymmdd").val(today);
    $('#hrReceiptSaleAmt').val('');
    $('#hrReceiptRoyaltyAmt').val('');
};

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    runOnlyOnce.getFrList();
    grids.initialization();
    grids.create();
    enableDatepicker();

    trigs.basic();
};
