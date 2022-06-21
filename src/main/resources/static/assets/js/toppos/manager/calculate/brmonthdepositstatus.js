import {runOnlyOnce} from '../../module/m_managerbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        headBranchReceiptMonthlyList: {
            filterFromYearMonth: 's',    // 조회기간 시작
            filterToYearMonth: 's',    // 조회기간 끝
            branchId: 'n',    // 선택된 지사
        },
    },
    receive: {
        headBranchReceiptMonthlyList: {
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
    getDepositList(searchCondition) {
        wares.searchCondition = searchCondition;
        dv.chk(searchCondition, dtos.send.headBranchReceiptMonthlyList, '입금 등록 내역 조회조건 보내기');
        CommonUI.ajax('/api/head/headBranchReceiptMonthlyList', 'GET', searchCondition, function (res) {
            wares.xlsxNaming.filterFromYearMonth = searchCondition.filterFromYearMonth;
            wares.xlsxNaming.filterToYearMonth = searchCondition.filterToYearMonth;
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.headBranchReceiptMonthlyList, '입금 등록 내역 리스트 받아오기');
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

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[0])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[0], {
            fileName : wares.xlsxNaming.title + '_' + wares.xlsxNaming.filterFromYearMonth + '_'
                + wares.xlsxNaming.filterToYearMonth,
            progressBar : true,
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
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
        title: '지사월정산입금현황',
        filterFromYearMonth: '',
        filterToYearMonth: '',
    },
};

/* 조회 */
function getDepositList() {
    const searchCondition = {
        filterFromYearMonth: $('#filterFromYear').val() + $('#filterFromMonth').val(),
        filterToYearMonth: $('#filterToYear').val() + $('#filterToMonth').val(),
        branchId: window.branchId,
    }

    comms.getDepositList(searchCondition);
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    /* runOnlyOnce.getFrList() 를 통해 현재 로그인한 지사 id에 접근하기 위한 수단 */
    window.branchId = 0;
    runOnlyOnce.initializeDateSelectBox(true);
    runOnlyOnce.getFrList();
    grids.initialization();
    grids.create();

    trigs.basic();
};
