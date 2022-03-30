$(function() {

    // 삭제여부
    $(document).on("click","#checkYesBtn",function(){
        checkYesOrNo(true)
    });
    $(document).on("click","#checkNoBtn",function(){
        checkYesOrNo(false)
    });

    $(document).on("click","#cautionBtn1",function(){
        $('#popupId').remove();
    });

    $(document).on("click","#cautionBtn2",function(){
        location.href="/login";
        $('#popupId').remove();
    });

    $(document).on("click","#successBtn",function(){
        $('#popupId').remove();
    });

    $(document).on("click", "#checkNegativeBtn", function(){
        $('#popupId').remove();
    });

    $(document).on("click","#checkDelCancelBtn",function(){
        $('#popupId').remove();
    });

});

//Ajax 호출시 에러가났을경우의 메세지 함수
function ajaxErrorMsg(req) {
    if(req.status ===403){
        console.log("403 접근권한이 없습니다.");
        location.href="/error/403"
    }else if(req.status ===404){
        console.log("404 존재하지 않은 페이지");
        location.href="/error/404"
    }else if(req.status ===400){
        $('.l-popup').removeClass('open');
        console.log("400 데이터에러");
        alertCaution("400 에러 데이터가 존재하지않습니다.", 1);
    }else if(req.status ===500){
        $('.l-popup').removeClass('open');
        console.log("500 데이터에러");
        alertCaution("사용할 수 없는 데이터입니다.<br>다시 시도해주세요.", 1);
    }else if(req.status ===405){
        $('.l-popup').removeClass('open');
        console.log("405 데이터에러");
        alertCaution("405 에러 사용할 수 없는 데이터", 1);
    }else{
        $('.l-popup').removeClass('open');
        console.log("토큰이 만료됨. 재로그인시도 바람.");
        alertCaution("토큰이 만료되었습니다.<BR>다시 로그인해주세요.", 2);
    }
}

function alertSuccess(text) { //성공창(삭제성공시),저장성공시
    let html = '';
    html +='<div id="popupId" class="popup popup--dim">';
    	html +='<div class="popup__box">';
    		html +='<div class="popup__content">';
    			html +='<div class="popup__stat success"></div>';
    				html +='<div class="popup__text">'+text+'</div>';
				html +='</div>';
			html +='<div class="popup__buttons">';
				html +='<button id="successBtn" class="popup__btn popup__btn--solid">확인</button>';
			html +='</div>';
		html +='</div>';
	html +='</div>';

    $('#alertpop').html(html);
    $('#successBtn').trigger("focus"); // 엔터치면 바로 확인버튼이 눌릴 수 있게

}

function alertCancel(text) { //에러창(로그인만료),오류

    let html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat cancel"></div>';
                html +='<div class="popup__text">'+text+'</div>';
           html +='</div>';
            html +='<div class="popup__buttons">';
               html +='<button id="successBtn" class="popup__btn popup__btn--solid">확인</button>';
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
    $('#successBtn').trigger("focus");

}

function alertCaution(text,type) { //경고창
    let html = '';

    html +='<div id="popupId" class="popup popup--dim">';
        html +='<div class="popup__box">';
            html +='<div class="popup__content">';
                html +='<div class="popup__stat caution"></div>';
                html +='<div class="popup__text">'+text+'</div>';
            html +='</div>';
            html +='<div class="popup__buttons">';
            if(type===1){
                html +='<button id="cautionBtn1" class="popup__btn popup__btn--solid">확인</button>';
            }else{
                html +='<button id="cautionBtn2" class="popup__btn popup__btn--solid">확인</button>';
            }
            html +='</div>';
        html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
    $('#cautionBtn'+type).trigger("focus");
}

function alertCheck(text) { // 확인 취소 물음창
    let html = '';

    html +='<div id="popupId" class="popup popup--dim">';
    html +='<div class="popup__box">';
    html +='<div class="popup__content">';
    html +='<div class="popup__stat check"></div>';
    html +='<div class="popup__text">'+text+'</div>';
    html +='</div>';
    html +='<div class="popup__buttons">';
    html +='<button id="checkDelSuccessBtn" class="popup__btn popup__btn--solid">확인</button>';
    html +='<button id="checkDelCancelBtn" class="popup__btn">취소</button>';
    html +='</div>';
    html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
}

function alertThree(text, btn1 = "예", btn2 = "아니오", btn3 = "취소") { // 삼지선다 물음창 세번째 버튼은 취소용
    let html = '';

    html +='<div id="popupId" class="popup popup--dim"">';
    html +='<div class="popup__box popup__box--three">';
    html +='<div class="popup__content">';
    html +='<div class="popup__stat check"></div>';
    html +='<div class="popup__text">'+text+'</div>';
    html +='</div>';
    html +='<div class="popup__buttons">';
    html +='<button id="popFirstBtn" class="popup__btn popup__btn--solid">' + btn1 +'</button>';
    html +='<button id="popSecondBtn" class="popup__btn popup__btn--solid">' + btn2 +'</button>';
    html +='<button id="popThirdBtn" class="popup__btn">' + btn3 +'</button>';
    html +='</div>';
    html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
}

function alertStrongThree(text, btn1 = "예", btn2 = "아니오", btn3 = "취소") { // 삼지선다 물음창 세번째 버튼은 취소용
    let html = '';

    html +='<div id="popupId" class="popup popup--warning"">';
    html +='<div class="popup__box popup__box--three">';
    html +='<div class="popup__content">';
    html +='<div class="popup__stat cancel"></div>';
    html +='<div class="popup__text">'+text+'</div>';
    html +='</div>';
    html +='<div class="popup__buttons">';
    html +='<button id="popFirstBtn" class="popup__btn popup__btn--solid">' + btn1 +'</button>';
    html +='<button id="popSecondBtn" class="popup__btn popup__btn--red">' + btn2 +'</button>';
    html +='<button id="popThirdBtn" class="popup__btn">' + btn3 +'</button>';
    html +='</div>';
    html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
}

function alertDeleteCheck(text) { // 정말작성할껀지 확인하는창
    let html = '';

    html +='<div id="popupId" class="popup popup--dim">';
    html +='<div class="popup__box">';
    html +='<div class="popup__content">';
    html +='<div class="popup__stat check"></div>';
    html +='<div class="popup__text">'+text+'</div>';
    html +='</div>';
    html +='<div class="popup__buttons">';
    html +='<button id="checkYesBtn" class="popup__btn popup__btn--solid">예</button>';
    html +='<button id="checkNoBtn" class="popup__btn">아니오</button>';
    html +='</div>';
    html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
}

function alertContinueSuccess(text) { //성공창(삭제성공시),저장성공시

    var html = '';

    html +='<div id="popupId" class="popup popup--dim">';
    html +='<div class="popup__box">';
    html +='<div class="popup__content">';
    html +='<div class="popup__stat success"></div>';
    html +='<div class="popup__text">'+text+'</div>';
    html +='</div>';
    html +='<div class="popup__buttons">';
    html +='<button id="continueSuccessBtn" class="popup__btn popup__btn--solid">확인</button>';
    html +='</div>';
    html +='</div>';
    html +='</div>';

    $('#alertpop').html(html);
    $('#continueSuccessBtn').trigger("focus");
}

function readyPage() {
    alertCaution("아직 준비중인 페이지입니다.",1);
    return false;
}