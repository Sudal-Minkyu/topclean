import {runOnlyOnce} from '../../module/m_headbalance.js';

/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {

    },
    receive: {

    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    searchDepositListData(searchCondition) {
        console.log(searchCondition);
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
                dataField: '',
                headerText: '지사명',
            }, {
                headerText: '1월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '2월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '3월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '4월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '5월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '6월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '7월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '8월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '9월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '10월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '11월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
            }, {
                headerText: '12월',
                children: [
                    {
                        dataField: '',
                        headerText: '입금여부',
                    }, {
                        dataField: '',
                        headerText: '미입금액',
                    },
                ],
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
            rowHeight : 30,
            headerHeight : 30,
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
        $('#searchListBtn').on('click', function () {
            searchDepositListData();
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

};

const searchDepositListData = function () {
    const searchCondition = {
        filterYear: $('#filterYear').val(),
    }
    comms.searchDepositListData(searchCondition);
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    runOnlyOnce.initializeDateSelectBox(false);
    grids.initialization();
    grids.create();

    trigs.basic();
};
