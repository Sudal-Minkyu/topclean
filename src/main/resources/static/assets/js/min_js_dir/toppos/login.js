let vkey;

let targetFieldID = ["userid", "password"];

let vkeyProp = [];

vkeyProp[0] = {
    title : "ID를 입력해주세요",
}

vkeyProp[1] = {
title : "비밀번호를 입력해주세요",
defaultKeyboard : 2,
}

$(function() {

    // 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
    const key = getCookie("key");
    const $userid = $("#userid");
    const $idSaveCheck = $("#idSaveCheck");
    // console.log("")
    $userid.val(key);
    if($userid.val() !== ""){ // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
        $idSaveCheck.attr("checked", true); // ID 저장하기를 체크 상태로 두기.
    }

    $idSaveCheck.change(function(){ // 체크박스에 변화가 있다면,
        if($idSaveCheck.is(":checked")){ // ID 저장하기 체크했을 때,
            setCookie("key", $userid.val(), 7); // 7일 동안 쿠키 보관
        }else{
            // ID 저장하기 체크 해제 시,
            deleteCookie("key");
        }
    });

    // ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
    $userid.keyup(function(){ // ID 입력 칸에 ID를 입력할 때,
        if($idSaveCheck.is(":checked")){ // ID 저장하기를 체크한 상태라면,
            setCookie("key", $userid.val(), 7); // 7일 동안 쿠키 보관
        }
    });


    vkey = new VKeyboard();

    $("#exitProgram").on("click", function () {
        alertCheck("프로그램을 종료 하시겠습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            cAPI.pExit();
        });
    });

    $("#password").on("keypress", function (e) {
        if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
            loginActive();
        }
    });

});

function setCookie(cookieName, value, exdays){
    const exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    const cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}

function deleteCookie(cookieName){
    const expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}

function getCookie(cookieName) {
    cookieName = cookieName + '=';
    const cookieData = document.cookie;
    let start = cookieData.indexOf(cookieName);
    let cookieValue = '';
    if(start !== -1){
        start += cookieName.length;
        let end = cookieData.indexOf(';', start);
        if(end === -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}

function loginActive() {

    const $userid = $("#userid");
    const $password = $("#password");

    if ($userid.val().trim() === '') {
        alertCaution("아이디를 입력하세요.");
        $userid.trigger('focus');
        return false;
    }
    if ($password.val().trim() === '') {
        alertCaution("비밀번호을 입력하세요.");
        $password.trigger('focus');
        return false;
    }

    const params = {
        userid : $userid.val(),
        password : $password.val()
    };

    const jsonString = JSON.stringify(params);

    // console.log("로그인 실행");
    $.ajax({
        url: '/auth/login',
        type: 'post',
        data: jsonString,
        contentType: 'application/json',
        cache: false,
        error(req) {
            if(req.status === 401){
                $('.l-popup').removeClass('open');
                alertCancel("비밀번호가 틀렸습니다.");
                $("#password").val("");
            }else{
                ajaxErrorMsg(req);
            }
        },
        success(res) {
            // console.log("로그인 되었습니다.");
            // console.log("AccessToken : "+res.sendData.tokenDto.accessToken);
            if(res.status === 500){
                $("#password").val("");
                if (res.err_msg2 === null && res.err_msg) {
                    alertCancel(res.err_msg);
                } else {
                    alertCancel(res.err_msg + "<br>" + res.err_msg2);
                }
            }else{
                localStorage.setItem("Authorization",res.sendData.tokenDto.accessToken); // 로컬 웹스토리지안에 토큰을 저장한다.
                mainPage();
            }
        }
    });
}

// 현재 소속의 대한 메인페이지 이동 함수
function mainPage(){
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });
    $.ajax({
        url: '/',
        type: 'get',
        contentType: 'application/json',
        cache: false,
        error: ajaxErrorMsg,
        success(res) {
            location.href= res.sendData.link;
            // console.log("이동완료");
        }
    });
}

function openVKeyboard(num) {
    vkey.showKeyboard(targetFieldID[num], vkeyProp[num]);
}