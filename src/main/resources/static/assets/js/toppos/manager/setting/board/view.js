/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        getPost: {
        },
        getReply: {
        },
        lostNoticeCommentSave: {
            type: "", // 덧글 타입
            comment: "", // 덧글의 내용
            preId: "", // 덧글의 덧글일 경우 원댓글의 아이디
        },
        lostNoticeDelete: {
            htId: "n",
        }
    },
    receive: {
        getPost: {
            fileList: {
                fileId: "n",
                filePath: "s",
                fileFileName: "s",
                fileOriginalFilename: "s",
                fileVolume: "n",
            },
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
    taglost: "/api/manager/lostNoticeView",
    notice: "/api/manager/noticeView",
    brnotice: "",
    taglostReplyList: "/api/manager/lostNoticeCommentList",
    taglostPutReply: "/api/manager/lostNoticeCommentSave",
    taglostDeletePost: "/api/manager/lostNoticeDelete",
    noticeDeletePost: "/api/manager/noticeDelete",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData(condition) {
        dtos.send.getPost[wares[wares.boardType].idKeyName] = "n"; // 게시판마다 id가 다르므로 dtos의 항목도 동적 추가
        dv.chk(condition, dtos.send.getPost, "읽어올 게시물의 아이디 보내기");
        dtos.receive.getPost[wares[wares.boardType].idKeyName] = "n"; // 받는 dtos도 위와 마찬가지
        CommonUI.ajax(urls[wares.boardType], "GET", condition, function (res) {
            console.log(res);
            const data = res.sendData[wares[wares.boardType].dataKeyName];
            dv.chk(data, dtos.receive.getPost, "받아온 게시물");
            setFields(data);
        });
    },

    getReplyList(condition) {
        dtos.send.getReply[wares[wares.boardType].idKeyName] = "n";
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

    putReply(data) {
        dtos.send.lostNoticeCommentSave[wares[wares.boardType].idKeyName] = "n";
        dtos.send.lostNoticeCommentSave[wares[wares.boardType].commentIdKeyName] = "";
        dv.chk(data, dtos.send.lostNoticeCommentSave, "덧글 달기와 수정 정보 보내기");
        CommonUI.ajax(urls[wares.boardType + "PutReply"], "PARAM", data, function (res) {
            $("#replyList").children().remove();
            $("#replyField").val("");
            const condition = {};
            condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
            comms.getReplyList(condition);
        });
    },

    deletePost(target) {
        dv.chk(target, dtos.send.lostNoticeDelete, "삭제할 글 아이디 보내기");
        CommonUI.ajax(urls[wares.boardType + "DeletePost"], "PARAM", target, function (res) {
            alertSuccess("글을 삭제 하였습니다.");
            $("#successBtn").on("click", function () {
                location.href = $(".listLink").first().attr("href");
            });
        });
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    basic() {
        $("#commitReply").on("click", function () {
            commitReply("1", "#replyField");
        });

        $("#attachSwitch").on("click", function () {
            $("#fileListPanel").toggleClass("active");
        });

        $(".deleteBtn").on("click", function () {
            alertCheck("정말 삭제하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const target = {
                }
                target[wares[wares.boardType].idKeyName] = parseInt(wares.id);
                comms.deletePost(target);
                $('#popupId').remove();
            });
        });
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
    hnType: "",

    btnRow: {},
    commentRow: {},

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
    brnotice: {

    },
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    getParams();
    setInputs();

    let condition = {};
    condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
    comms.getData(condition);

    trigs.basic();

    if(!["notice", "brnotice"].includes(wares.boardType)) {
        condition = {};
        condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
        comms.getReplyList(condition);
    }

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
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
        wares.id = parseInt(wares.params.get("id"));
    } else {
        wares.id = 0;
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

    if(wares.params.has("hnType")) {
        wares.hnType = wares.params.get("hnType");
    } else {
        wares.hnType = "00";
    }
}

function setInputs() {
    let specialCondition = "";
    if(wares.boardType === "notice") specialCondition = "&hnType=" + wares.hnType;
    $(".listLink").attr("href", `./${wares.boardType}list?page=` + wares.page
        + "&searchString=" + wares.searchString + "&filterFromDt=" + wares.filterFromDt + "&filterToDt="
        + wares.filterToDt + specialCondition);

    $(".modifyLink").attr("href", `./${wares.boardType}write?prevPage=` + wares.page
        + "&id=" + wares.id + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt=" + wares.filterFromDt
        + "&prevFilterToDt=" + wares.filterToDt + specialCondition);
}

function setFields(data) {
    console.log(data);
    $("#subject").html(data.subject);
    $("#name").html(wares.boardType === "notice" ? CommonData.name.hnType[data.hnType] : data.name);
    $("#insertDateTime").html(data.insertDateTime);
    $("#content").html(data.content);
    $("#prevSubject").html(data.prevSubject);
    $("#prevInsertDateTime").html(data.prevInsertDateTime);
    $("#nextSubject").html(data.nextSubject);
    $("#nextInsertDateTime").html(data.nextvInsertDateTime);

    let specialCondition = "";
    if(wares.boardType === "notice") specialCondition = "&hnType=" + wares.hnType;

    if(data.prevId) {
        $("#linkPrev").attr("href", `./${wares.boardType}view?id=` + data.prevId
            + "&prevPage=" + wares.page + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt="
            + wares.filterFromDt + "&prevFilterToDt=" + wares.filterToDt + specialCondition);
    }

    if(data.nextId) {
        $("#linkNext").attr("href", `./${wares.boardType}view?id=` + data.nextId
            + "&prevPage=" + wares.page + "&prevSearchString=" + wares.searchString + "&prevFilterFromDt="
            + wares.filterFromDt + "&prevFilterToDt=" + wares.filterToDt + specialCondition);
    }

    /* 공지사항 게시판이 아닌 경우에만 덧글기능 활성화 */
    if(!["notice", "brnotice"].includes(wares.boardType)){
        $(".reply").show();
    }

    if(data.isWritter === "1") {
        $(".modifyLink").show();
        $(".deleteBtn").show();
    }

    $("#fileCnt").html(data.fileList.length);

    for(let file of data.fileList) {
        let volume = 0;
        if(file.fileVolume > 1000000) {
            volume = (file.fileVolume / 1048576).toFixed(1).toLocaleString() + "MB"
        } else {
            volume = Math.ceil(file.fileVolume / 1024).toLocaleString() + "KB";
        }

        /* 파일 확장자가 라이트박스 라이브러리를 지원하는 확장자인지 판단 */
        const isImage = ["PNG", "BMP", "JPG", "JPEG", "GIF", "WEBP", "SVG"]
            .includes(file.fileFileName.split(".").pop().toUpperCase());
        const addProp = isImage ? `data-lightbox="images" data-title="${file.fileFileName}"` : ``;

        const element = `
            <li>
                <a href="${file.filePath + file.fileFileName}" ${addProp} class="board-view__file" download>
                    <span class="board-view__filename">${file.fileOriginalFilename}</span>
                    <span class="board-view__filesize">${volume}</span>
                </a>
            </li>
        `;

        $("#fileList").append(element);
    }
}

function createReplyHtml(commentId, name, modifyDt, comment, type, isWritter, preId) {
    let btnReply = "";
    let btnModify = "";
    if(type === "1") {
        btnReply = `<button type="button" class="c-button c-button--small c-button--darkline" onclick="reply(${commentId}, this)">답글</button>`;
    }
    if(isWritter === "1") {
        btnModify = `<button type="button" class="c-button c-button--small" onclick="modify(${commentId}, this)">수정</button>`;
    }

    let htmlText = `
    <div class="reply__info">
        <span class="reply__name">${name}</span>
        <div class="reply__date">${modifyDt}</div>
    </div>
    <div class="reply__comment">${comment}</div>
    <div class="reply__console">
        ${btnReply}
        ${btnModify}
    </div>
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


// 덧글내 답글 달기를 클릭했을 때
function reply(commentId, obj) {
    $(obj).hide();
    $(obj).siblings("button").hide();
    const $targetReply = $(obj).parents(".replyItem");

    const newTextarea = `
        <div class="reply__item newItem">
            <div class="reply__comment">
                <div class="c-textarea">
                    <textarea id="newarea${commentId}"></textarea>
                </div>
            </div>
            <div class="reply__console">
                <button onclick="newCancel(${commentId}, this)" class="c-button c-button--small reply__console-right">취소</button>
                <button onclick="commitReply('2', '#newarea${commentId}', ${commentId}, '')" class="c-button c-button--small reply__console-right">덧글 달기</button>
            </div>
        </div>
    `

    $targetReply.append(newTextarea);
}

function modify(commentId, obj) {
    const $btnRow = $(obj).parents(".reply__console");
    const $commentRow = $btnRow.siblings(".reply__comment");
    const text = $commentRow.html();

    wares.btnRow[commentId.toString()] = $btnRow;
    wares.commentRow[commentId.toString()] = $commentRow;
    
    const modBtns = `
        <div class="reply__console">
            <button onclick="modifyCancel(${commentId}, this)" class="c-button c-button--small reply__console-right">수정 취소</button>
            <button onclick="modifyComp(${commentId})" class="c-button c-button--small reply__console-right">수정 완료</button>
        </div>
    `;

    const modTextarea = `
        <div class="reply__comment">
            <div class="c-textarea">
                <textarea id="tarea${commentId}">${text}</textarea>
            </div>
        </div>
    `;

    $btnRow.replaceWith(modBtns);
    $commentRow.replaceWith(modTextarea);
}

function commitReply(type, fieldId, preId = "", commentId = "") {
    const data = {
        type: type,
        comment: $(fieldId).val(),
        preId: preId,
    };
    data[wares[wares.boardType].idKeyName] = parseInt(wares.id);
    data[wares[wares.boardType].commentIdKeyName] = commentId;

    comms.putReply(data);
}

function modifyCancel(commentId, obj) {
    console.log(this);
    const $btnRow = $(obj).parents(".reply__console");
    const $commentRow = $btnRow.siblings(".reply__comment");
    $btnRow.replaceWith(wares.btnRow[commentId.toString()]);
    $commentRow.replaceWith(wares.commentRow[commentId.toString()]);
}

function newCancel(commentId, obj) {
    const $targetElement = $(obj).parents(`li[data-id='${commentId}']`);
    $targetElement.children(".reply__console").children().show();
    $targetElement.children("div .newItem").remove();
}

function modifyComp(commentId) {
    commitReply("", "#tarea" + commentId, "", commentId);
}