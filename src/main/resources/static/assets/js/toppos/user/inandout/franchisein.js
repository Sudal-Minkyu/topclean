/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {

    },
    receive: {
        프랜차이즈입고: {
            fdId: "nr",
            fdS4Dt: "sr",
            bcName: "sr",
            fdTag: "sn",

            fdColor: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            biItemcode: "s",

            fdPriceGrade: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "",
            fdPollution: "",
            fdWaterRepellent: "",
            fdStarch: "",
            fdUrgentYn: "s",
            fdTotAmt: "n",
            fdRemark: "s",

            frYyyymmdd: "sr",
            fdS2Dt: "sr",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    
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
            "targetHtmlId"
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
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : true,
                enableFilter : true,
                rowHeight : 48,
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
    },

    e: {
        basicEvent() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정

    },
    r: { // 이벤트 해제

    }
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

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grids.e.basicEvent();
}