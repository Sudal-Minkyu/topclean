/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        템플릿6개저장: { // 6칸짜리 배열에 아래의 내용들이 세팅되어 API로 전송됩니다.
            fmNum: "n", // 각 가맹점의 템플릿 번호. 1~6번까지 있음
            fmSubject: "s",
            fmMessage: "s",
        },
        고객리스트조회: {
            visitDayRange: "n", // 예를들어 7이 들어가면 일주일안에 이용했던 고객, 30이 들어가면 30일 안에 이용했던 고객, 단 0일경우 전체검색
        },
        문자보내기: {
            bcIdList: "a", // 문자 발송의 대상이 되는 고객들의 bcId들이 담긴 리스트
            fmMessage: "s",
            fmSendreqtimeDt: "n", // 예약발송인 경우에는 타임스탬프 숫자 형태로 전달 예정입니다. 아닐 경우 0을 넣어서 보낼 예정입니다.
        },
        전송내역좌측요약정보조회: {
            filterFromDt: "s",
            filterToDt: "s",
        },
        전송내역우측세부정보조회: {
            전송일자: "s", // 8자리의 년월일까지만 나온 정보
        },
    },
    receive: {
        템플릿6개가져오기: { // 템플릿 저장 API와 동일한 구조로 배열에 아래의 파라메터들이 옵니다.
            fmNum: "n",
            fmSubject: "s",
            fmMessage: "s",
        },
        고객리스트조회: {
            bcId: "n",
            bcName: "s",
            bcHp: "s",
        },
        전송내역좌측요약정보조회: {
            전송일자: "s", // 8자리의 년월일까지만 나온 정보
            총건수: "n",
            수동건수: "n",
            검품확인: "n",
            완성품메시지: "n",
            영수중: "n",
        },
        전송내역우측세부정보조회: {
            fmType: "s",
            fmSendreqtimeDt: "n", // 타임스템프 형태로 전달 요청드립니다.
            bcName: "s",
            bcHp: "s",
            fmMessage: "s",
        },
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {

};

/*
*  .s : AUI 그리드 관련 설정들, 같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  .f : 그리드 관련 함수들 배치
* */
const grids = {
    s: { // 그리드 세팅
        id: [
            "grid_main"
        ],
        columnLayout: [],
        prop: [],
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

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grids.s.columnLayout) {
                AUIGrid.create(grids.s.id[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        get(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        set(numOfGrid, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clear(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
            AUIGrid.resize(grids.s.id[num]);
        },
    },
};

const trigs = {
    basic() {
        AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
            console.log(e.item);
        });

        const $tabsBtn = $('.c-tabs__btn');
        const $tabsContent = $('.c-tabs__content');

        $tabsBtn.on('click', function () {
            const idx = $(this).index();

            $tabsBtn.removeClass('active');
            $tabsBtn.eq(idx).addClass('active');
            $tabsContent.removeClass('active');
            $tabsContent.eq(idx).addClass('active');
        });
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

}

$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();

    trigs.basic();
}
