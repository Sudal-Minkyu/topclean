// ****************** 토큰 관련 자바스크립트 ******************//

// 쿠키 값 삭제하기
const deleteCookie = function(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
    //	deleteCookie('name');
}

// 전역변수로 JWT토큰 가져오는 함수
let accessToken;
let refreshToken;
let insert_id;
function JWT_Get(){
    accessToken = getCookie("JwtAccessToken");
    refreshToken = getCookie("JwtRefreshToken");
    insert_id = getCookie("insert_id");

    // console.log("accessToken : "+accessToken);
    // console.log("refreshToken : "+refreshToken);
    // console.log("insert_id : "+insert_id);
}

// 쿠키 값 넣기
const setCookie = function (name, value, exp) {
    const date = new Date();
    // 1000 * 60 * 30 -> 30분
    // 1000 * 60 -> 1분
    date.setTime(date.getTime() + exp * 1000 * 60 * 30);
    document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
};

// 쿠기 값 가져오기
const getCookie = function(name) {
    const value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
};

// 현재 토큰 쿠키 가져오기
function accessTokenCookieGet(){
    // setCookie(변수이름);
    const accessToken = getCookie("JwtAccessToken");
    // console.log("현재 쿠키 JwtAccessToken변수에 저장된 값: "+accessToken);
    if(accessToken==null){
        refreshTokenCookie();
    }
}

// 토큰 Refresh하기
function refreshTokenCookie(){

    console.log("JWT토큰 새로고침이 시작됩니다.");

    const accessToken = getCookie("JwtRefreshAccessToken");
    const refreshToken = getCookie("JwtRefreshToken");
    console.log("refreshAccessToken : "+accessToken);
    console.log("refreshToken : "+refreshToken);

    // let url = "http://192.168.0.144:8012/auth/reissue"; // 호출할 백엔드 API

    let url = $("#security_url").val() + "/auth/reissue"; // 호출할 백엔드 API

    const params = {
        accessToken : accessToken,
        refreshToken : refreshToken
    };

    const jsonString = JSON.stringify(params);

    $.ajax({
        url: url,
        type: 'post',
        data: jsonString,
        contentType: 'application/json',
        cache: false,
        error:function(request){
            ajaxErrorMsg(request);
        },
        success: function (res) {
            if(res.status===500){
                console.log("토큰 새로고침 에러");
                alertCaution("토큰이 만료되었습니다.<BR>다시 로그인해주세요.", 2);
            }else{
                // setCookie(변수이름, 변수값, 유효시간);
                setCookie("JwtAccessToken", res.data.token.accessToken, 2); // 발행시간은 60분으로설정
                setCookie("JwtRefreshAccessToken", res.data.token.accessToken, 4); // 발행시간은 120분으로설정
                setCookie("JwtRefreshToken", res.data.token.refreshToken, 4); // 발행시간은 120분으로설정
                setCookie("insert_id",res.data.token.insert_id, 4); // 발행시간은 120분으로설정

                const test_token = getCookie("JwtAccessToken");
                const refreshToken = getCookie("JwtRefreshToken");
                console.log("새로받은 test_token : " + test_token);
                console.log("새로받은 refreshToken : " + refreshToken);
            }
        }
    });
}
