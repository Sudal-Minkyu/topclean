/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        덧글리스트불러오기: {
            id: "", // 글의 id
        },
        덧글달기: {
            id: "", // 글의 id
            type: "", // 덧글 타입
            comment: "", // 덧글의 내용
            preId: "", // 덧글의 덧글일 경우 원댓글의 아이디
        },
        덧글수정: {
            id: "", // 덧글의 id (글의 id 아님),
            comment: "", // 덧글의 내용
        },
    },
    receive: {
        덧글리스트불러오기: { // insertDt 가 빠른 순서대로 불러온다.
            덧글배열 : {
                id: "", // 덧글의 id
                name: "", // 덧글 작성자 이름
                modifyDt: "", // 덧글의 수정시간(없을 경우 등록시간)
                comment: "", // 덧글 내용
                type: "", // 덧글 타입
                preId: "", // 덧글의 덧글일 경우 원댓글의 아이디
                insertId: "", // 작서한 사용자의 아이디 번호
            },
            loginId: "", // 로그인한 사용자의 아이디 번호
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getReplyList: "",
    addNewReply: "",
    modifyReply: "",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getReplyList(condition) {
        console.log(condition);
    },

    addNewReply(data) {
        console.log(data);
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
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    getParams();
    comms.getReplyList({id: wares.id});

    trigs.s.basic();

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();
}

function createReplyHtml(id, name, modifyDt, comment, type, isWriter, preId) {
    let btnReply = "";
    let btnModify = "";
    if(depth === "1") {
        btnReply = `<button type="button" class="c-button c-button--small c-button--darkline" onclick="reply(${id})">답글</button>`;
    }
    if(isWriter === "Y") {
        btnModify = `<button type="button" class="c-button c-button--small" onclick="modify(${id})">수정</button>`;
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
        htmlText = `<li class="reply__item replyItem" data-id="${id}">` + htmlText + `</li>`;
        $("#replyList").append(htmlText);
    }else if(type === "2") {
        const target = `.replyItem[data-id='${preId}']`;
        console.log(target);
        htmlText = `<div class="reply__item">` + htmlText + `</div>`;
        $(target).append(htmlText);
    }
}

function getParams() {
    wares.params = new URL(wares.url).searchParams;
    if(wares.params.has("id")) {
        wares.id = wares.params.get("id");
    } else {
        wares.id = "";
    }
}

function reply(id) {
    
}

function modify(id) {
    
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

function commitReply(type, fieldId, preId = "") {
    const data = {
        id: wares.id,
        type: type,
        comment: $(fieldId).val(),
        preId: preId,
    };

    comms.addNewReply(data);
}