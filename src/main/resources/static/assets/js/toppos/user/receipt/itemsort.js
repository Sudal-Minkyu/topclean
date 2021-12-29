$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "g" 쓰레기 데이터
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dto = {
    send: {
        franchiseItemGroupUpdate : {
            bgItemGroupcode: "sr",
            bgSort: "nr"
        },
    },
    receive: {
        franchiseItemGroupList: {
            bgSort: "nr",
            bgItemGroupcode: "sr",
            bgName: "s"
        },

    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const data = {

}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const ajax = {
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function (req) {
            console.log(req);
            grid.s.data[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(grid.s.id[numOfGrid], grid.s.data[numOfGrid]);
        });
    },
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grid = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_bgOrder", "grid_bsList", "grid_biOrder"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/head/", "/api/head/", "/api/head/"
            ],
            read: [
                "/api/user/franchiseItemGroupList", "/api/head/", "/api/head/"
            ],
            update: [
                "/api/head/", "/api/head/", "/api/head/"
            ],
            delete: [
                "/api/head/", "/api/head/", "/api/head/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grid.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grid.s.columnLayout[0] = [
                {
                    dataField: "bgItemGroupcode",
                    headerText: "분류코드",
                }, {
                    dataField: "bgName",
                    headerText: "분류명칭",
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grid.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : false,
                enableFilter : true,
                showRowNumColumn : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grid.s.columnLayout[1] = [
                {
                    dataField: "",
                    headerText: "분류코드",
                }, {
                    dataField: "",
                    headerText: "분류명칭",
                },
            ];

            grid.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : false,
                enableFilter : true,
                showRowNumColumn : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grid.s.columnLayout[2] = [
                {
                    dataField: "",
                    headerText: "분류코드",
                }, {
                    dataField: "",
                    headerText: "분류명칭",
                },
            ];

            grid.s.prop[2] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : false,
                enableFilter : true,
                showRowNumColumn : false,
                rowHeight : 48,
                headerHeight : 48,
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성 후 창에 맞게 리사이징
            for (const i in grid.s.columnLayout) {
                grid.s.id[i] = AUIGrid.create(grid.s.targetDiv[i], grid.s.columnLayout[i], grid.s.prop[i]);
            }
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            ajax.setDataIntoGrid(numOfGrid, grid.s.url.read[numOfGrid]);
        },

        resizeOnTab(type) {
            if(type === 0) {
                AUIGrid.resize(grid.s.id[0]);
            }else if(type === 1) {
                AUIGrid.resize(grid.s.id[1]);
                AUIGrid.resize(grid.s.id[2]);
            }
        }
    },

    e: {
        basicEvent() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grid.s.id[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grid.e에 위치 */
const event = {
    s: { // 이벤트 설정
        switchTab() {
            const $tabsBtn = $('.c-tabs__btn');
            const $tabsContent = $('.c-tabs__content');

            $tabsBtn.on('click', function() {
                const idx = $(this).index();

                $tabsBtn.removeClass('active');
                $tabsBtn.eq(idx).addClass('active');
                $tabsContent.removeClass('active');
                $tabsContent.eq(idx).addClass('active');

                grid.f.resizeOnTab(idx);
            });
        },
        pushUpDown() {
            $("#bgUp").on("click", function() {
                pushUp(0)
            });
            $("#bgDown").on("click", function() {
                pushDown(0)
            });
            $("#biUp").on("click", function() {
                pushUp(2)
            });
            $("#biDown").on("click", function() {
                pushDown(2)
            });
        },
        save() {
            $("#bgSortSave").on("click", function() {

            });

            $("#biSortSave").on("click", function() {

            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {

    event.s.switchTab();
    event.s.pushUpDown();
    grid.f.initialization();
    grid.f.create();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    grid.f.setInitialData(0);

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    grid.e.basicEvent();

}

function pushUp(numOfGrid) {
    const selectedRowId = AUIGrid.getSelectedIndex(grid.s.id[numOfGrid]);
    AUIGrid.moveRowsToUp(grid.s.id[numOfGrid], selectedRowId);
}

function pushDown(numOfGrid) {
    const selectedRowId = AUIGrid.getSelectedIndex(grid.s.id[numOfGrid]);
    AUIGrid.moveRowsToDown(grid.s.id[numOfGrid], selectedRowId);

}

function saveSort(numOfGrid) {

}

function testForJest() {
    return "hi";
}


/* jest 테스트를 위해 nodejs 의 요소에 테스트가 필요한 기능을 탑재하여 내보내기 한다. 브라우저 실행 환경에서는 무시 처리 된다. */
try {
    ajax.setDataIntoGrid = jest.fn();
    module.exports = {testForJest};
}catch (e) {
    if(!(e instanceof ReferenceError)) {
        console.log(e);
    }
}