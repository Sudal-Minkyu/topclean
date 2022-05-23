import {grids, runOnlyOnce} from '../../module/m_managerStatus.js';

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {

}

const comms = {
    searchSummaryData(searchCondition) {
        console.log(searchCondition);
        // CommonUI.ajax('', 'GET', searchCondition, function (res) {
        //     console.log(res);
        // });
    },

    getDetailData(condition) {
        console.log(condition);
        // CommonUI.ajax('', 'GET', condition, function (res) {
        //     console.log(res);
        // });
    }
}

const gridElemets = {
    id: ['grid_summary'],
    columnLayout: [],
    prop: [],

    initialization() {

        const processChkChar = function (_rowIndex, _columnIndex, value, _headerText, _item) {
            return value ? '√' : '';
        };
        const dateFormat = 'yyyy-mm-dd';
        const dateTimeFormat = 'yyyy-mm-dd hh:mm';

        this.columnLayout[0] = [
            {
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: '',
                headerText: '외주출고일',
                width: 100,
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: '',
                headerText: '출고<br>건수',
                width: 40,
            }, {
                dataField: '',
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
                dataField: '',
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
            enableColumnResize : false,
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
        AUIGrid.bind(grids.id[0], 'cellClick', function (e) {
            const condition = {
                fdO1Dt: e.item.fdO1Dt,
            };
            comms.getDetailData(condition);
        });
    },
};

const trigs = {
    basic() {
        $('#searchListBtn').on('click', searchSummaryList);
    },
};

const searchSummaryList = function () {
    const searchCondition = {
        frId: parseInt($('#frList').val(), 10),
        filterFromDt: $('#filterFromDt').val().numString(),
        filterToDt: $('#filterToDt').val().numString(),
    }
    comms.searchSummaryData(searchCondition);
}

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    gridElemets.initialization();
    grids.create(gridElemets.id[0], gridElemets.columnLayout[0], gridElemets.prop[0]);
    /* 세부 내역 그리드는 재사용 가능성이 높아 공통모듈을 사용해 그린다. */
    runOnlyOnce.makeDetailGrid();

    runOnlyOnce.enableDatepicker();
    runOnlyOnce.getFrList();

    trigs.basic();
};