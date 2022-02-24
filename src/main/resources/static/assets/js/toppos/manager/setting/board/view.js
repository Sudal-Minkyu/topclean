/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        taglost: {
            htId: "n",
        },
        notice: {
            hnId: "n",
        },
        덧글리스트불러오기: {
            htId: "", // 글의 id
        },
        덧글달기와수정: {
            htId: "", // 글의 id
            hcId: "", // 덧글 수정일 경우 필요
            type: "", // 덧글 타입
            comment: "", // 덧글의 내용
            preId: "", // 덧글의 덧글일 경우 원댓글의 아이디
        },
        lostNoticeDelete: {
            htId: "n",
        }
    },
    receive: {
        taglost: {
            fileList: {
                fileId: "n",
                filePath: "s",
                fileFileName: "s",
                fileOriginalFileName: "s",
                fileVolume: "n",
            }
        },

        notice: {

        },

        덧글리스트불러오기: { // insertDt 가 빠른 순서대로 불러온다.
            hcId: "", // 덧글의 id
            name: "", // 덧글 작성자 이름
            modifyDt: "", // 덧글의 수정시간(없을 경우 등록시간)
            comment: "", // 덧글 내용
            type: "", // 덧글 타입
            preId: "", // 덧글의 덧글일 경우 원댓글의 아이디
            isWritter: "s", // 1이면 본인글, 2이면 본인글 아님
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    taglost: "/api/manager/lostNoticeView",
    notice: "/api/manager/noticeView",
    taglostReplyList: "/api/manager/lostNoticeCommentList",
    putReply: "/api/manager/lostNoticeCommentSave",
    deletePost: "/api/manager/lostNoticeDelete",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData(condition) {
        console.log(condition);
        CommonUI.ajax(urls[wares.boardType], "GET", condition, function (res) {
            console.log(res);
            const data = res.sendData[wares[wares.boardType].dataKeyName];
            setFields(data);
        });
    },

    getReplyList(condition) {
        CommonUI.ajax(urls[wares.boardType + "ReplyList"], "GET", condition, function (res) {
            const data = res.sendData.commentListDto;
            data.forEach(obj => {
                createReplyHtml(obj[wares[wares.boardType].commentKeyName], obj.name, obj.modifyDt, obj.hcComment, obj.type, obj.isWritter, obj.preId);
            });
        });
    },

    putReply(data) {
        CommonUI.ajax(urls.putReply, "PARAM", data, function (res) {
            $("#replyList").children().remove();
            $("#replyField").val("");
            const condition = {};
            condition[wares[wares.boardType].masterKeyName] = wares.id;
            comms.getReplyList(condition);
        });
    },

    modifyReply(data) {
        console.log(data);
    },

    deletePost(target) {
        dv.chk(target, dtos.send.lostNoticeDelete, "삭제할 글 아이디 보내기");
        CommonUI.ajax(urls.deletePost, "PARAM", target, function (res) {
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
                    htId: parseInt(wares.id),
                }
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
    btnRow: {},
    commentRow: {},

    boardType: "", // 아래 게시판 종류의 이름이 담길 곳
    /* 게시판 종류에 따른 데이터들 */
    taglost: {
        idKeyName: "htId",
        dataKeyName: "tagNoticeViewDto",
        masterKeyName: "htId",
        commentKeyName: "hcId",
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
    setInputs();

    let condition = {};
    condition[wares[wares.boardType].idKeyName] = wares.id;
    comms.getData(condition);

    trigs.basic();

    if(wares.boardType !== "notice") {
        condition = {};
        condition[wares[wares.boardType].masterKeyName] = wares.id;
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

function setInputs() {
    $(".listLink").attr("href", `./${wares.boardType}list?page=` + wares.page + "&searchString=" + wares.searchString
        + "&filterFromDt=" + wares.filterFromDt + "&filterToDt=" + wares.filterToDt);

    $(".modifyLink").attr("href", `./${wares.boardType}write?prevPage=` + wares.page + "&id=" + wares.id + "&prevSearchString=" + wares.searchString
    + "&prevFilterFromDt=" + wares.filterFromDt + "&prevFilterToDt=" + wares.filterToDt);
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

    console.log(data.fileList);
    $("#fileCnt").html(data.fileList.length);

    for(let file of data.fileList) {
        let volume = 0;
        if(file.fileVolume > 1000000) {
            volume = (file.fileVolume / 1048576).toFixed(1).toLocaleString() + "MB"
        } else {
            volume = Math.ceil(file.fileVolume / 1024).toLocaleString() + "KB";
        }

        const element = `
            <li>
                <a href="${file.filePath + file.fileFileName}" class="board-view__file">
                    <span class="board-view__filename">${file.fileOriginalFilename}</span>
                    <span class="board-view__filesize">${volume}</span>
                </a>
            </li>
        `;

        $("#fileList").append(element);
    }
}

function createReplyHtml(commentId, name, modifyDt, comment, type, isWriter, preId) {
    let btnReply = "";
    let btnModify = "";
    if(type === "1") {
        btnReply = `<button type="button" class="c-button c-button--small c-button--darkline" onclick="reply(${commentId}, this)">답글</button>`;
    }
    if(isWriter === "1") {
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
    console.log($btnRow);

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

function commitReply(type = "", fieldId, preId = "", commentId = "") {
    const data = {
        type: type,
        comment: $(fieldId).val(),
        preId: preId,
    };
    data[wares[wares.boardType].masterKeyName] = wares.id;
    data[wares[wares.boardType].commentKeyName] = commentId;

    comms.putReply(data);
}

function modifyCancel(commentId, obj) {
    console.log(this);
    const $btnRow = $(obj).parents(".reply__console");
    const $commentRow = $btnRow.siblings(".reply__comment");
    const text = $commentRow.html();
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