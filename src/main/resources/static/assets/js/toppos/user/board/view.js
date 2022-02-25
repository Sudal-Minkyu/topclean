/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        getPost: {},
        getReply: {},
    },
    receive: {
        getPost: { // 아이디는 동적으로 추가
            content: "s",
            subject: "s",
            insertDateTime: "s",
            isWritter: "s",
            name: "s",
            nextId: "",
            nextSubject: "s",
            nextvInsertDateTime: "s",
            prevId: "",
            prevSubject: "s",
            prevInsertDateTime: "s",
            fileList: {
                fileId: "n",
                fileVolume: "n",
                filePath: "s",
                fileOriginalFilename: "s",
                fileFileName: "s",
            }
        },

        getReply: {
            isWritter: "s",
            modifyDt: "s",
            name: "s",
            preId: "",
            type: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    taglost: "/api/user/lostNoticeView",
    notice: "/api/user/noticeView",
    taglostReplyList: "/api/user/lostNoticeCommentList",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData(condition) {
        dtos.send.getPost[wares[wares.boardType].idKeyName] = "n"; // 게시판마다 id가 다르므로 dtos의 항목도 동적 추가
        dv.chk(condition, dtos.send.getPost, "게시글의 아이디를 보내기");
        dtos.receive.getPost[wares[wares.boardType].idKeyName] = "n"; // 받는 dtos도 위와 마찬가지
        CommonUI.ajax(urls[wares.boardType], "GET", condition, function (res) {
            const data = res.sendData[wares[wares.boardType].dataKeyName];
            dv.chk(data, dtos.receive.getPost, "게시글 받기");
            setFields(data);
        });
    },

    getReplyList(condition) {
        dtos.send.getReply[wares[wares.boardType].idKeyName] = "n"; // 게시판마다 id가 다르므로 dtos의 항목도 동적 추가
        dv.chk(condition, dtos.send.getReply, "덧글 조회를 위한 게시글의 아이디 보내기");
        dtos.receive.getReply[wares[wares.boardType].commentIdKeyName] = "n";
        dtos.receive.getReply[wares[wares.boardType].commentKeyName] = "s";

        CommonUI.ajax(urls[wares.boardType + "ReplyList"], "GET", condition, function (res) {
            const data = res.sendData.commentListDto;
            dv.chk(data, dtos.receive.getReply, "조회한 덧글 가져오기");
            data.forEach(obj => {
                createReplyHtml(obj[wares[wares.boardType].commentIdKeyName], obj.name, obj.modifyDt, obj.hcComment, obj.type, obj.isWritter, obj.preId);
            });
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
    id: "", // 글의 아이디
    params: "",
    page: "",
    searchString: "",
    filterFromDt: "",
    filterToDt: "",

    boardType: "", // 아래 게시판 종류의 이름이 담길 곳
    /* 게시판 종류에 따른 데이터들 */
    taglost: {
        idKeyName: "htId",
        dataKeyName: "tagNoticeViewDto",
        commentIdKeyName: "hcId",
        commentKeyName: "hcComment",
    },
    notice: {
        idKeyName: "hnId",
        dataKeyName: "noticeViewDto",
    },
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    getParams();
    let condition = {};
    condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
    comms.getData(condition);
    if(wares.boardType !== "notice") {
        condition = {};
        condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
        comms.getReplyList(condition);
    }
}

function getParams() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 365);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    const url = new URL(wares.url);
    const tokenedPath = url.pathname.split("/");
    const lastUrl = tokenedPath[tokenedPath.length - 1];
    wares.boardType = lastUrl.substring(0, lastUrl.length - 4);
    wares.params = url.searchParams;

    if(wares.params.has("id")) {
        wares.id = wares.params.get("id");
    } else {
        wares.id = "";
    }

    if(wares.params.has("prevPage")) {
        wares.page = wares.params.get("prevPage");
    } else {
        wares.page = "1";
    }

    if(wares.params.has("prevSearchString")) {
        wares.searchString = wares.params.get("prevSearchString");
    } else {
        wares.searchString = "";
    }

    if(wares.params.has("prevFilterFromDt")) {
        wares.filterFromDt = wares.params.get("prevFilterFromDt");
    } else {
        wares.filterFromDt = fromday;
    }

    if(wares.params.has("prevFilterToDt")) {
        wares.filterToDt = wares.params.get("prevFilterToDt");
    } else {
        wares.filterToDt = today;
    }
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
        $("#linkPrev").attr("href", `./${wares.boardType}view?id=` + data.prevId + "&prevPage=" + wares.page 
        + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
        + "&prevFilterToDt=" + wares.filterToDt);
    }

    if(data.nextId) {
        $("#linkNext").attr("href", `./${wares.boardType}view?id=` + data.nextId + "&prevPage=" + wares.page 
        + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
        + "&prevFilterToDt=" + wares.filterToDt);
    }

    /* 덧글 본문 처리방식 결정되면 수정할 것 */
    if(wares.boardType !== "notice"){
        $("#linkReply").show();
        $("#replyList").show();
        $("#replyList").css("height", "300px");
        $("#content").css("height", "200px");
    }

    $("#linkReply").attr("href", `./${wares.boardType}reply?id=` + data[wares[wares.boardType].idKeyName] + "&prevPage=" + wares.page 
    + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
    + "&prevFilterToDt=" + wares.filterToDt);

    // 페이지 정보 가져와서 해당 페이지로 바로 갈 수 있도록 조치
    $("#linkPrevPage").attr("href", `./${wares.boardType}list?page=` + wares.page + "&searchString=" + wares.searchString
        + "&filterFromDt=" + wares.filterFromDt + "&filterToDt=" + wares.filterToDt);
}

function createReplyHtml(commentId, name, modifyDt, comment, type, isWriter, preId) {

    let htmlText = `
    <div class="reply__info">
        <span class="reply__name">${name}</span>
        <div class="reply__date">${modifyDt}</div>
    </div>
    <div class="reply__comment">${comment}</div>
    `;

    if(type === "1") {
        htmlText = `<li class="reply__item replyItem" data-id="${commentId}">` + htmlText + `</li>`;
        $("#replyList").append(htmlText);
    }else if(type === "2") {
        const target = `.replyItem[data-id='${preId}']`;
        htmlText = `<div class="reply__item">` + htmlText + `</div>`;
        $(target).append(htmlText);
    }
}