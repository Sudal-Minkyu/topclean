import {grids, runOnlyOnce} from '../../module/m_managerStatus.js?22081117';

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {

};

const wares = {
    condition: {},
};

const comms = {
    searchSummaryData(searchCondition) {
        CommonUI.ajax('/api/manager/branchReceiptOutsouringList', 'GET', searchCondition, function (res) {
            const data = res.sendData.gridListData;
            grids.setData(gridElements.id[0], data);
            grids.clear(grids.id[0]);
        });
    },

    getDetailData(condition) {
        CommonUI.ajax('/api/manager/branchReceiptOutsouringSubList', 'GET', condition, function (res) {
            wares.condition = condition;
            const data = res.sendData.gridListData;
            grids.setData(grids.id[0], data);
        });
    },
};

const gridElements = {
    id: ['grid_summary'],
    columnLayout: [],
    prop: [],

    initialization() {
        const dateFormat = 'yyyy-mm-dd';

        this.columnLayout[0] = [
            {
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: 'fdO1Dt',
                headerText: '외주출고일',
                width: 100,
                dataType: 'date',
                formatString: dateFormat,
            }, {
                dataField: 'deliveryCount',
                headerText: '출고<br>건수',
                width: 40,
            }, {
                dataField: 'receiptCount',
                headerText: '입고<br>건수',
                width: 40,
            }, {
                dataField: 'fdTotAmt',
                headerText: '접수총액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdOutsourcingAmt',
                headerText: '외주총액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            },
        ];

        this.prop[0] = {
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

    setEvenets() {
        AUIGrid.bind(gridElements.id[0], 'cellClick', function (e) {
            const condition = {
                fdO1Dt: e.item.fdO1Dt.numString(),
            };
            comms.getDetailData(condition);
        });
    },

    // 엑셀 내보내기(Export), 외부모듈에서 구현한 디테일 그리드의 데이터를 출력한다.
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[0])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[0], {
            fileName : '외주입출고현황_' + wares.condition.fdO1Dt,
            progressBar : true,
        });
    },
};

const trigs = {
    basic() {
        $('#searchListBtn').on('click', searchSummaryList);

        $('#exportXlsx').on('click', function () {
            if (wares.condition.fdO1Dt) {
                gridElements.exportToXlsx();
            } else {
                alertCaution('다운로드할 정보가 없습니다.<br>먼저 상세 항목을 조회해 주세요.', 1);
            }
        });
    },
};

const searchSummaryList = function () {
    const searchCondition = {
        franchiseId: parseInt($('#frList').val(), 10),
        filterFromDt: $('#filterFromDt').val().numString(),
        filterToDt: $('#filterToDt').val().numString(),
    };
    comms.searchSummaryData(searchCondition);
};

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    gridElements.initialization();
    grids.create(gridElements.id[0], gridElements.columnLayout[0], gridElements.prop[0]);
    /* 세부 내역 그리드는 재사용 가능성이 높아 공통모듈을 사용해 그린다. */
    runOnlyOnce.makeDetailGrid();

    runOnlyOnce.enableDatepicker();
    runOnlyOnce.getFrList();

    trigs.basic();
    gridElements.setEvenets();
};
