$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dto = {
    send: {

    },
    receive: {

    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const data = {

}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const ajax = {
    setDataIntoGrid: function(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function (req) {
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
            "targetHtmlId"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/"
            ],
            read: [
                "/api/"
            ],
            update: [
                "/api/"
            ],
            delete: [
                "/api/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grid.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grid.s.columnLayout[0] = [
                {
                    dataField: "",
                    headerText: "",
                }, {
                    dataField: "",
                    headerText: "",
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
                showStateColumn : true,
                enableFilter : true
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grid.s.columnLayout) {
                grid.s.id[i] = AUIGrid.create(grid.s.targetDiv[i], grid.s.columnLayout[i], grid.s.prop[i]);
            }
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            ajax.setDataIntoGrid(numOfGrid, grid.s.read[numOfGrid]);
        },
    },

    e: {
        basicEvent() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(gridId[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grid.f.initialization();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    // grid.f.setInitialData(0);

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grid.e.basicEvent();
}

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grid.e에 위치 */
const event = {
    s: { // 이벤트 설정

    },
    r: { // 이벤트 해제

    }
}

/* jest 기본 동작을 테스트 하기 위한 함수, 기본 테스트를 통과하면 아래의 module.exports 부분을 수정하고 이 함수는 지울 것 */
function testForJest() {
    return "hi";
}

/* jest 테스트를 위해 nodejs 의 요소에 테스트가 필요한 기능을 탑재하여 내보내기 한다. 보통의 실행 환경에서는 무시된다. */
try {
    module.exports = {testForJest};
}catch (e) {
    if(!(e instanceof ReferenceError)) {
        console.log(e);
    }
}


/*  이전 버전의 표준 형식
$(function() {
    /!* 배열내의 각 설정만 넣어 빈 그리드 생성 *!/
    createGrids();

    /!* 그리드에 데이터를 집어넣음 반복문은 그리드숫자만큼(혹은 목표그리드 범위만큼) 돌 수 있도록 한다. *!/
    for(let i=0; i<2; i++) {
        setDataIntoGrid(i, gridCreateUrl[i]);
    }

    /!* 0번그리드 내의 셀 클릭시 이벤트 *!/
    AUIGrid.bind(gridId[0], "cellClick", function (e) {
        console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
    });
});


/!* 그리드 생성과 운영에 관한 중요 변수들. grid라는 이름으로 시작하도록 통일했다. *!/
let gridId = [];
let gridTargetDiv = [];
let gridData = [];
let gridColumnLayout = [];
let gridProp = [];
let gridCreateUrl = [];
let gridSaveUrl = [];

/!* 그리드를 뿌릴 대상 div의 id들 *!/
gridTargetDiv = [
    "", ""
];

/!* 그리드를 받아올 때 쓰이는 api 배열 *!/
gridCreateUrl = [
    "/api/a", "/api/b"
]

/!* 그리드를 저장할 때 쓰이는 api 배열 *!/
gridSaveUrl = [
    "/api/a", "/api/b"
]

/!* 0번 그리드의 레이아웃 *!/
gridColumnLayout[0] = [
    {
        dataField: "",
        headerText: "",
    }, {
        dataField: "",
        headerText: "",
    },
];

/!* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* *!/
gridProp[0] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    enableColumnResize : false,
    showStateColumn : true,
    enableFilter : true
};

gridColumnLayout[1] = [
    {
        dataField: "",
        headerText: "",
    }, {
        dataField: "",
        headerText: "",
    },
];

gridProp[1] = {
    editable : false,
    selectionMode : "singleRow",
    noDataMessage : "출력할 데이터가 없습니다.",
    enableColumnResize : false,
    showStateColumn : true,
    enableFilter : true
};


/!* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 *!/
function createGrids() {
    for (const i in gridColumnLayout) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout[i], gridProp[i]);
    }
}

/!* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. *!/
function setDataIntoGrid(numOfGrid, url) {
    CommonUI.ajax(url, "GET", false, function (req) {
        gridData[numOfGrid] = req.sendData.gridListData;
        AUIGrid.setGridData(gridId[numOfGrid], gridData[numOfGrid]);
    });
}
*/
