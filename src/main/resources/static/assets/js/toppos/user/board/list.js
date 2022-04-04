/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        getPostList: {
            page: "nr",
            size: "n", // 한 페이지에 띄울 글의 숫자 
            searchString: "s", // 빈 문자일 경우 일반적인 조회
            filterFromDt: "s", // 검색조건 from 오지 않을경우 시작기간은 전체기간
            filterToDt: "s", // 검색조건 to 오지 않을 경우 끝기간은 전체기간
        },
    },
    receive: {
        getPostList: {
            datalist: {
                subject: "s",
                insertDateTime: "s", // yyyymmdd만
                insert_id: "s",
            },
            total_page: "n", // 전체 페이지수

            total_rows: "n",
            current_page: "n",
            current_rows: "n",
            err_code: "s",
            err_msg: "s",
            message: "s",
            status: "n",
            timestamp: "n",
            type: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    taglost: "/api/user/lostNoticeList",
    notice: "/api/user/noticeList",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getList() {
        const condition = {
            page: wares.page - 1,
            size: 13,
            searchString: wares.searchString,
            filterFromDt: wares.filterFromDt,
            filterToDt: wares.filterToDt,
        };
        if(wares.boardType === "notice") {
            condition.hnType = wares.hnType
            dtos.send.getPostList.hnType = "s";
        }
        dv.chk(condition, dtos.send.getPostList, "게시판 조회 속성과, 검색값 보내기");

        console.log(condition);
        CommonUI.ajax(urls[wares.boardType], "PARAM", condition, function (res) {
            console.log(res);
            dtos.receive.getPostList.datalist[wares[wares.boardType].idKeyName] = "n"; // 검사 조건에 임의로 각 게시판의 아이디 추가
            if(!["notice"].includes(wares.boardType)) dtos.receive.getPostList.datalist.numOfComment = "n";
            if(["notice"].includes(wares.boardType)) dtos.receive.getPostList.datalist.hnType = "s";
            dv.chk(res, dtos.receive.getPostList, "게시글들 받아오기");
            wares.totalPage = res.total_page;
            createPagingNavigator(wares.page);
            grids.f.setData(0, res.datalist);
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
                    style: "grid_textalign_left",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let result = "<div class='list_subject'><span>" + value + "</span>";
                        if(item.numOfComment) {
                            result += ` <span class="numOfComment">(${item.numOfComment})</span>`
                        }
                        result += "</div>";
                        return result;
                    }
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

        noticeSetting() {
            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: "hnType",
                    headerText: "작성자",
                    width: 150,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.hnType[value];
                    }
                },
                {
                    dataField: "subject",
                    headerText: "제목",
                    style: "grid_textalign_left",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        let result = "<div clasee='list_subject'><span>" + value + "</span>";
                        if(item.numOfComment) {
                            result += ` <span class="numOfComment">(${item.numOfComment})</span>`
                        }
                        result += "</div>";
                        return result;
                    }
                }, {
                    dataField: "insertDateTime",
                    headerText: "작성일",
                    width: 150,
                },
            ];
        },

        
    },

    t: {
        basicTrigger() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                let specialCondition = "";
                if(wares.boardType === "notice") specialCondition = "&hnType=" + wares.hnType;
                location.href = `./${wares.boardType}view?id=` + e.item[wares[wares.boardType].idKeyName] + "&prevPage=" + wares.page 
                    + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
                    + "&prevFilterToDt=" + wares.filterToDt + specialCondition;
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
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
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
    pageButtonCount: 10, // 페이지 네비게이션에서 보여줄 페이지의 수
    page: 1, // 현재 페이지
    totalPage: 0, // 전체 페이지
    searchString: "",
    filterFromDt: "",
    filterToDt: "",
    hnType: "",

    boardType: "",
    taglost: {
        title: "택 분실 게시판",
        idKeyName: "htId",
    },
    notice: {
        title: "공지사항",
        idKeyName: "hnId",
    },

}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    enableDatepicker();
    getParams();
    setInputs();
    if(wares.boardType === "notice") {
        grids.f.noticeSetting();
        $("#hnTypeComp").show();
    }
    grids.f.create();
    grids.t.basicTrigger();
    trigs.s.basicTrigger();
    comms.getList();
    createPagingNavigator(wares.page);

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();
}

function getParams() {
    const url = new URL(wares.url);
    const tokenedPath = url.pathname.split("/");
    const lastUrl = tokenedPath[tokenedPath.length - 1];
    wares.boardType = lastUrl.substring(0, lastUrl.length - 4);
    wares.params = url.searchParams;

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

    if(wares.params.has("hnType")) {
        wares.hnType = wares.params.get("hnType");
    } else {
        wares.hnType = "00";
    }
}

function setInputs() {
    if(wares.filterFromDt) $("#filterFromDt").val(wares.filterFromDt);
    if(wares.filterToDt) $("#filterToDt").val(wares.filterToDt);
    if(wares.searchString) $("#searchString").val(wares.searchString);
    if(wares.hnType) $("#hnType").val(wares.hnType);
    $("#boardTitle").html(wares[wares.boardType].title);
    $("#boardLink").attr("href", `./${wares.boardType}list`);
}

/* 페이지내이션 적절한 값 완성후 wares의 데이터를 이용하도록 수정 */
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
    let specialCondition = "";
    if(wares.boardType === "notice") specialCondition = "&hnType=" + wares.hnType;
	var url = `./${wares.boardType}list?page=` + goPage + "&searchString=" + wares.searchString + "&filterFromDt="
        + wares.filterFromDt + "&filterToDt=" + wares.filterToDt + specialCondition;
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
    wares.hnType = $("#hnType").val();
    moveToPage(1);
}

function enableDatepicker() {
    
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 90);
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