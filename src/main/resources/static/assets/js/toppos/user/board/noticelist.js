/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        페이지에따른게시판데이터받아오기: {
            page: "nr",
            searchString: "s", // 빈 문자일 경우 일반적인 조회
            filterFromDt: "s", // 검색조건 from 오지 않을경우 시작기간은 전체기간
            filterToDt: "s", // 검색조건 to 오지 않을 경우 끝기간은 전체기간
        },
    },
    receive: {
        페이지에따른게시판데이터받아오기: {
            작성글의리스트: {
                htId: "nr", // 게시판 글 id
                subject: "s",
                insertDateTime: "s", // yyyymmdd만
                insert_id: "s",
            },
            페이징필요정보: {
                totalRowCount: "nr", // 전체 게시물 건수
            },
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getList: "/api/user/lostNoticeList",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getList() {
        const condition = {
            page: wares.page - 1,
            size: 10,
            searchString: wares.searchString,
            filterFromDt: wares.filterFromDt,
            filterToDt: wares.filterToDt,
        };

        console.log(condition);
        CommonUI.ajax(urls.getList, "PARAM", condition, function (res) {
            wares.totalPage = res.total_page;
            createPagingNavigator(wares.page);
            grids.f.setData(0, res.datalist);
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
            "grid_list"
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
                    dataField: "subject",
                    headerText: "제목",
                }, {
                    dataField: "insertDateTime",
                    headerText: "작성일",
                    width: 150,
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "none",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : false,
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

        clearData(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) { // 해당 배열 번호 그리드의 엑스트라 체크박스 선택된 (아이템 + 행번호) 객체 반환
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        },

        
    },

    t: {
        basicTrigger() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                location.href = "./taglostview?htId=" + e.item.htId + "&prevPage=" + wares.page 
                    + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
                    + "&prevFilterToDt=" + wares.filterToDt;
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basicTrigger() {
            $("#searchBtn").on("click", function () {
                mainSearch();
            });

            $("#vkeyboard0").on("click", function () {
                onShowVKeyboard(0);
            });

            $("#searchString").on("keypress", function (e) {
                if(e.originalEvent.code === "Enter") {
                    mainSearch();
                }
            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    url: window.location.href,
    params: "", // url에 내포한 파라메터들을 담는다.
    rowCount: 0, // 1페이지에서 보여줄 행 수
    pageButtonCount: 0, // 페이지 네비게이션에서 보여줄 페이지의 수
    page: 0, // 현재 페이지
    totalPage: 0, // 전체 페이지
    searchString: "",
    filterFromDt: "",
    filterToDt: "",
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    grids.t.basicTrigger();
    trigs.s.basicTrigger();
    enableDatepicker();

    getParams();
    setInputs();
    comms.getList();
    createPagingNavigator(wares.page);

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();
}

function getParams() {
    wares.params = new URL(wares.url).searchParams;

    if(wares.params.has("page")) {
        wares.page = wares.params.get("page");
    } else {
        wares.page = "1";
    }

    if(wares.params.has("filterFromDt")) {
        wares.filterFromDt = wares.params.get("filterFromDt");
    } else {
        wares.filterFromDt = $("#filterFromDt").val();
    }

    if(wares.params.has("filterToDt")) {
        wares.filterToDt = wares.params.get("filterToDt");
    } else {
        wares.filterToDt = $("#filterToDt").val();
    }

    if(wares.params.has("searchString")) {
        wares.searchString = wares.params.get("searchString");
    } else {
        wares.searchString = "";
    }
}

function setInputs() {
    if(wares.filterFromDt) $("#filterFromDt").val(wares.filterFromDt);
    if(wares.filterToDt) $("#filterToDt").val(wares.filterToDt);
}

/* 페이지내이션 적절한 값 완성후 wares의 데이터를 이용하도록 수정 */
var rowCount = 20;	// 1페이지에서 보여줄 행 수
var pageButtonCount = 10;		// 페이지 네비게이션에서 보여줄 페이지의 수
var page = 1;	// 현재 페이지

// 페이징 네비게이터를 동적 생성합니다.
function createPagingNavigator(goPage) {
	var retStr = "";
	var prevPage = parseInt((goPage - 1)/pageButtonCount) * pageButtonCount;
	var nextPage = ((parseInt((goPage - 1)/pageButtonCount)) * pageButtonCount) + pageButtonCount + 1;

	prevPage = Math.max(0, prevPage);
	nextPage = Math.min(nextPage, wares.totalPage);
	
	// 처음
	retStr += "<a href='javascript:moveToPage(1)'><span class='aui-grid-paging-number aui-grid-paging-first'>first</span></a>";

	// 이전
	retStr += "<a href='javascript:moveToPage(" + Math.max(1, prevPage) + ")'><span class='aui-grid-paging-number aui-grid-paging-prev'>prev</span></a>";

	for (var i=(prevPage+1), len=(pageButtonCount+prevPage); i<=len; i++) {
		if (goPage == i) {
			retStr += "<span class='aui-grid-paging-number aui-grid-paging-number-selected'>" + i + "</span>";
		} else {
			retStr += "<a href='javascript:moveToPage(" + i + ")'><span class='aui-grid-paging-number'>";
			retStr += i;
			retStr += "</span></a>";
		}
		
		if (i >= wares.totalPage) {
			break;
		}

	}

	// 다음
	retStr += "<a href='javascript:moveToPage(" + nextPage + ")'><span class='aui-grid-paging-number aui-grid-paging-next'>next</span></a>";

	// 마지막
	retStr += "<a href='javascript:moveToPage(" + wares.totalPage + ")'><span class='aui-grid-paging-number aui-grid-paging-last'>last</span></a>";

	document.getElementById("grid_paging").innerHTML = retStr;
}

function moveToPage(goPage) {
	var url = "./taglostlist?page=" + goPage + "&searchString=" + wares.searchString + "&filterFromDt=" + wares.filterFromDt + "&filterToDt=" + wares.filterToDt;
    location.href = url;
}

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["searchString"];

    vkeyProp[0] = {
        title: "검색어 입력",
        callback: mainSearch,
    };

    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function mainSearch() {
    wares.searchString = $("#searchString").val();
    wares.filterFromDt = $("#filterFromDt").val();
    wares.filterToDt = $("#filterToDt").val();
    moveToPage(1);
}

function enableDatepicker() {
    
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 363);
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