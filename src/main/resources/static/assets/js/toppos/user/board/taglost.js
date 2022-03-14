/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        택분실게시판조회: {
            filterFromDt: "s",
            filterToDt: "s",
            type: "s", // 1 미완료, 2 전체, 3 내 가맹점
        }
    },
    receive: {
        택분실게시판조회: {
            btId: "n",
            insertDt: "s",
            btBrandName: "s",
            btInputDate: "s",
            btMaterial: "s",
            btRemark: "s",
            사진리스트: { // 썸네일 경로의 경우 이전처럼 파일이름 부분에 s_ 붙여서 오는 방식으로 처리하면 좋을듯 합니다.
                bfPath: "s",
                bfFilename: "s",
            },
            가맹응답상태: { // 가맹점 체크시 인서트, 해제시 딜리트가 된다.
                frName: "s",
                brCompleteYn: "s", // 확인완료시 Y가 된다.
            },
            
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getMainList: "",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getMainList(searchCondition) {
        dv.chk(searchCondition, dtos.send.택분실게시판조회);
        CommonUI.ajax(urls.getMainList, "GET", searchCondition, function (res) {
            console.log(res);
        });
    }
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grids = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_main"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: "insertDt",
                    headerText: "등록일시",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "btBrandName",
                    headerText: "추정브랜드",
                    style: "grid_textalign_left",
                }, {
                    dataField: "btInputDate",
                    headerText: "예상지사<br>입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "btMaterial",
                    headerText: "소재",
                    style: "grid_textalign_left",
                }, {
                    dataField: "thumbnail1", // src 내용이 와야 함
                    headerText: "사진1",
                    renderer : {
                        type : "ImageRenderer",
                        imgHeight : 100, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
                        srcFunction : function(rowIndex, columnIndex, value, item) {
                            return value ? value : ""; // 값이 없으면 기본 빈 이미지를 리턴
                        }
                    }
                }, {
                    dataField: "thumbnail2",
                    headerText: "사진2",
                    renderer : {
                        type : "ImageRenderer",
                        imgHeight : 100, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
                        srcFunction : function(rowIndex, columnIndex, value, item) {
                            return value ? value : ""; // 값이 없으면 기본 빈 이미지를 리턴
                        }
                    }
                }, {
                    dataField: "thumbnail3",
                    headerText: "사진3",
                    renderer : {
                        type : "ImageRenderer",
                        imgHeight : 100, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
                        srcFunction : function(rowIndex, columnIndex, value, item) {
                            return value ? value : ""; // 값이 없으면 기본 빈 이미지를 리턴
                        }
                    }
                }, {
                    dataField: "",
                    headerText: "가맹응답상태",
                    style: "grid_textalign_left",
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        // 온 데이터 확인하여 가공필요
                        return value;
                    },
                }, {
                    dataField: "",
                    headerText: "상세보기",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        return `<button class="c-state">보기</button>`;
                    },
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : false,
                rowHeight : 100,
                headerHeight : 48,
            };
        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grids.s.columnLayout) {
                grids.s.id[i] = AUIGrid.create(grids.s.targetDiv[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        getData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        setData(numOfGrid, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clearData(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) { // 해당 배열 번호 그리드의 엑스트라 체크박스 선택된 (아이템 + 행번호) 객체 반환
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        }
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
            console.log(e);
        });

        $("#searchBtn").on("click", function () {
            searchOrder();
        });

        $("#closeTaglostPop").on("click", function () {
            closeTaglostPop();
        });
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    enableDatepicker();
    trigs.basic();

    $("#taglostPop").addClass("active");


    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
}

function enableDatepicker() {
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
        type: $("#type").val(), // 1 미완료, 2 전체, 3 내 가맹점
    };

    comms.getMainList(searchCondition);
}

function closeTaglostPop() {
    $("#taglostPop").removeClass("active");
}