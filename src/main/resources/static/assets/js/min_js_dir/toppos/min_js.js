function logout(){
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });
    $.ajax({
        url: '/logoutActive',
        type: 'get',
        contentType: 'application/json',
        cache: false,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function () {
            localStorage.setItem("Authorization",null);
            location.href ="/login";
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
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (res) {
            location.href= res.sendData.link;
            // console.log("이동완료");
        }
    });
}
