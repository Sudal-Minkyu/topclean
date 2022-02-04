/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {

    },
    receive: {

    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    
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

}

function createReplyHtml(id, name, date, comment, depth, isWriter) {
    let btnReply = "";
    let btnModify = "";
    if(depth === "1") {
        btnReply = `<button type="button" class="c-button c-button--small c-button--darkline" onclick="reply(${id})">답글</button>`;
    }
    if(isWriter) {
        btnModify = `<button type="button" class="c-button c-button--small" onclick="modify(${id})">수정</button>`;
    }

    const text = `
    <div class="reply__info">
        <span class="reply__name">${name}</span>
        <div class="reply__date">${date}</div>
    </div>
    <div class="reply__comment">${comment}</div>
    <div class="reply__console">
        ${btnReply}
        ${btnModify}
    </div>
    `;
}

function reply(id) {

}

function modify(id) {
    
}