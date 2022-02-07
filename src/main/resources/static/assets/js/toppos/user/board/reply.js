/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        덧글리스트불러오기: {
            htId: "", // 글의 id
        },
        덧글달기와수정: {
            htId: "", // 글의 id
            hcId: "", // 덧글 수정일 경우 필요
            type: "", // 덧글 타입
            comment: "", // 덧글의 내용
            preId: "", // 덧글의 덧글일 경우 원댓글의 아이디
        }
    },
    receive: {
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
    getReplyList: "/api/user/lostNoticeCommentList",
    putReply: "/api/user/lostNoticeCommentSave",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getReplyList(condition) {
        CommonUI.ajax(urls.getReplyList, "GET", condition, function (res) {
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
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#vkeyboard0").on("click", function () {
                onShowVKeyboard(0);
            });

            $("#commitReply").on("click", function () {
                commitReply("1", "#replyField");
            });

            // 페이지 정보 가져와서 해당 페이지로 바로 갈 수 있도록 조치
            $("#linkPrevPage").attr("href", `./${wares.boardType}view?id=${wares.id}&prevPage=` + wares.page + "&prevSearchString=" + wares.searchString
                + "&prevFilterFromDt=" + wares.filterFromDt + "&prevFilterToDt=" + wares.filterToDt);
        }
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    url: window.location.href,
    id: "", // 글의 아이디
    params: "",
    btnRow: {},
    commentRow: {},
    page: "",
    searchString: "",
    filterFromDt: "",
    filterToDt: "",

    boardType: "",
    taglost: {
        masterKeyName: "htId",
        commentKeyName: "hcId",
    },
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    getParams();
    const condition = {};
    condition[wares[wares.boardType].masterKeyName] = wares.id;
    comms.getReplyList(condition);

    trigs.s.basic();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();
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

function getParams() {
    const url = new URL(wares.url);
    const tokenedPath = url.pathname.split("/");
    const lastUrl = tokenedPath[tokenedPath.length - 1];
    wares.boardType = lastUrl.substring(0, lastUrl.length - 5);
    wares.params = url.searchParams;

    if(wares.params.has("id")) {
        wares.id = wares.params.get("id");
    } else {
        wares.id = "";
    }

    wares.page = wares.params.get("prevPage");
    wares.searchString = wares.params.get("prevSearchString");
    wares.filterFromDt = wares.params.get("prevFilterFromDt");
    wares.filterToDt = wares.params.get("prevFilterToDt");
}

// 덧글내 답글 달기를 클릭했을 때
function reply(commentId, obj) {
    $(obj).hide();
    $(obj).siblings("button").hide();
    const $targetReply = $(obj).parents(".replyItem");
    const targetId = $targetReply.attr("data-id");

    const newTextarea = `
        <div class="reply__item newItem">
            <div class="reply__comment">
                <div class="c-textarea">
                    <textarea id="newarea${commentId}"></textarea>
                    <button onclick="openVKeyboard(${commentId}, 0)" class="keyboardBtn">키보드</button>
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
                <button onclick="openVKeyboard(${commentId}, 1)" class="keyboardBtn">키보드</button>
            </div>
        </div>
    `;

    $btnRow.replaceWith(modBtns);
    $commentRow.replaceWith(modTextarea);

}

function modifyComp(commentId) {
    commitReply("", "#tarea" + commentId, "", commentId);
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

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["replyField"];

    vkeyProp[0] = {
        title: "덧글 입력",
    };
    
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function openVKeyboard(commentId, type) { // 0 더덧글 달기의 textarea id, 1 수정
    const typeName = ["newarea", "tarea"];
    vkey.showKeyboard(typeName[type] + commentId, {title: "덧글 입력"});
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