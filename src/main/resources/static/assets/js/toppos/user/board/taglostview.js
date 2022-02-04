/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        lostNoticeView: {
            htId: "n",
        }
    },
    receive: {
        lostNoticeView: {

        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getData: "/api/user/lostNoticeView",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData(condition) {
        CommonUI.ajax(urls.getData, "GET", condition, function (res) {
            const data = res.sendData.tagNoticeViewDto;
            console.log(data);
            setFields(data);
        });
    },
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
    url: window.location.href,
    htId: "", // 글의 아이디
    params: "",
    page: "",
    searchString: "",
    filterFromDt: "",
    filterToDt: "",
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    getParams();
    comms.getData({htId: wares.htId});
}

function getParams() {
    wares.params = new URL(wares.url).searchParams;
    if(wares.params.has("htId")) {
        wares.htId = wares.params.get("htId");
    } else {
        wares.htId = "";
    }

    wares.page = wares.params.get("prevPage");
    wares.searchString = wares.params.get("prevSearchString");
    wares.filterFromDt = wares.params.get("prevFilterFromDt");
    wares.filterToDt = wares.params.get("prevFilterToDt");
}

function setFields(data) {
    $("#subject").html(data.subject);
    $("#name").html(data.name);
    $("#insertDateTime").html(data.insertDateTime);
    $("#content").html(data.content);
    $("#prevSubject").html(data.prevSubject);
    $("#prevInsertDateTime").html(data.prevInsertDateTime);
    $("#nextSubject").html(data.nextSubject);
    $("#nextInsertDateTime").html(data.nextvInsertDateTime);

    if(data.prevId) {
        $("#linkPrev").attr("href", "./taglostview?htId=" + data.prevId);
    }

    if(data.nextId) {
        $("#linkNext").attr("href", "./taglostview?htId=" + data.nextId);
    }

    $("#linkReply").attr("href", "./taglostreply?htId=" + data.htId + "&prevPage=" + wares.page 
    + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
    + "&prevFilterToDt=" + wares.filterToDt);

    // 페이지 정보 가져와서 해당 페이지로 바로 갈 수 있도록 조치
    $("#linkPrevPage").attr("href", "./taglostlist?page=" + wares.page + "&searchString=" + wares.searchString
        + "&filterFromDt=" + wares.filterFromDt + "&filterToDt" + wares.filterToDt);
}