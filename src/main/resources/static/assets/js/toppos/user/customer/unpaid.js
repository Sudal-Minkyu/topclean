/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        좌측그리드: { // searchText가 올 경우에는 검색하여 표기, 아니면 해당 지점의 미수금이 있는 고객 전체
            searchText: "",
            searchType: "", // 0: 통합, 1: 고객명, 2: 전화번호, 3: 주소
        },
        상단그리드: {
            bcId: "",
        },
        하단그리드: {
            frId: "",
        },
        결제팝업: {
            bcIdArray: "" // bcId들이 담긴 배열형태
        },
        결제시도: { // 이 부분은 결제가 성공한 뒤에(카드의 경우) 요청이 들어갈 것인데, 필요한 데이터가 제 판단으로는 명확치 않음.

        },
    },
    receive: {
        좌측그리드: {
            bcId: "nr",
            bcName: "s",
            bcHp: "",
            bcAddress: "",
            saveMoney: "", // 적립금
            uncollectMoney: "", // 전체미수금
        },
        상단그리드: { // 백단에서 고심해서 처리해야할 내역들이 많아보임
            frId: "nr",
            frYyyymmdd: "",
            상품내역: "",
            frNormalAmount: "", // 해당 frId 세부 접수 항목의 totAmt 총합인데 이게 맞는지 잘 모르겠음
            받은금액: "",
            uncollectMoney: "",
        },
        하단그리드: {
            frYyyymmdd: "s",
            fdTag: "s",
            
            fdColor: "",
            bgName: "s",
            bsName: "s",
            biName: "s",

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
            fdState: "s",
            출고일: "", // 지사출고, 강제출고 등 여러개여서.....
        },
        결제팝업: {
            bcId: "nr",
            frYyyymmdd: "s",
            상품내역: "",
            frNormalAmount: "", // 해당 frId 세부 접수 항목의 totAmt 총합인데 이게 맞는지 잘 모르겠음
            uncollectMoney: "",
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    abcd: "/api/user/",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function (req) {
            const data = req.sendData.gridListData;
            grids.f.setData(numOfGrid, data);
        });
    },
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
            AUIGrid.getGridData(grids.s.id[numOfGrid]);
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