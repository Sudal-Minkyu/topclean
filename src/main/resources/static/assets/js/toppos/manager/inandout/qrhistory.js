/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        좌측조회: {
            filterFromDt: 's',    // 조회 시작일
            filterToDt: 's',    // 조회 종료일
        },
        우측디테일: {
            insertYyyymmdd: 's,'    // 등록일자
        },
    },
    receive: {
        좌측조회: {
            insertYyyymmdd: 's',    // 등록일자
            fqCloseCnt: 'n'    // 수기건수
        },
        우측디테일: {
            insertDt: 's',    // 등록시간
            frName: 's',    // 가맹점명
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getSummaryList(searchCondition) {
        dv.chk(searchCondition, dtos.send.좌측조회, "좌측 누계 그리드 검색 조건 보내기");
        CommonUI.ajax('/api/manager/branchQrCloseCntList', "GET", searchCondition, function (res) {
            console.log(res);
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.좌측조회, "좌측 누계 그리드 검색 결과 리스트 받기");
            grids.clear(1);
            grids.set(0, data);
        });
    },

    getDetailList(searchCondition) {
        dv.chk(searchCondition, dtos.send.우측디테일, "디테일 그리드 검색 조건 보내기");
        CommonUI.ajax('/api/manager/branchQrCloseCntSubList', "GET", searchCondition, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.우측디테일, "디테일 그리드 검색 결과 리스트 받기");
            grids.set(1, data);
        });
    },
};

/* AUI 그리드 관련 설정과 함수들 */
const grids = {
    id: [
        'grid_summary', 'grid_detail'
    ],
    columnLayout: [],
    prop: [],

    /* 가시성을 위해 grids 의 일부 요소를 여기서 선언한다. */
    initialization() {

        /* 0번 그리드의 레이아웃 */
        grids.columnLayout[0] = [
            {
                dataField: 'insertYyyymmdd',
                headerText: '일자',
                dataType: "date",
                formatString: "yyyy-mm-dd",
            }, {
                dataField: 'fqCloseCnt',
                headerText: '스캔건수',
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

        grids.columnLayout[1] = [
            {
                dataField: 'insertDt',
                headerText: '시간',
            }, {
                dataField: 'frName',
                headerText: '가맹점명',
            },
        ];

        grids.prop[1] = {
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

    /* 디테일 그리드에 출력할 값이 있는지 검사 */
    hasGrid1Data() {
        const result = grids.get(1).length;
        if(!result) {
            alertCaution("엑셀 다운로드를 실행할 데이터가 없습니다.<br>조회후 왼쪽 표에서 데이터를 선택해 주세요.", 1);
        }
        return result;
    },

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[1])) {
            alertCaution("파일 다운로드가 불가능한 브라우저 입니다.", 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[1], {
            fileName : `${wares.title}_${wares.currentDetail.insertYyyymmdd}`,
            progressBar : true,
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.id[0], 'cellClick', function (e) {
            showDetail(e.item);
        });

        $("#searchListBtn").on("click", function () {
            searchOrder();
        });

        $("#exportXlsx").on("click", function () {
            if(grids.hasGrid1Data()) {
                grids.exportToXlsx();
            }
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    title: 'QR 스캔 이력',
    currentDetail: {
        insertYyyymmdd: '',
    },
};

const enableDatepicker = function () {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 6);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(fromday);
    $("#" + datePickerTargetIds[1]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

function searchOrder() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
    };

    comms.getSummaryList(searchCondition);
}

function showDetail(item) {
    const searchCondition = {
        insertYyyymmdd: item.insertYyyymmdd.numString(),
    };

    /* 선택된 가맹점과 날짜 항목에 대한 기억 */
    wares.currentDetail.insertYyyymmdd = searchCondition.insertYyyymmdd;

    comms.getDetailList(searchCondition);
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    grids.initialization();
    grids.create();

    enableDatepicker();
    trigs.basic();
};
