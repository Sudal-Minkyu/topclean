/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        지사정보저장하기: {
            brName: "s",
            brTelNo: "s",
        },
        유저정보저장하기: {
            userEmail: "s",
            userTel: "s",
            userPassword: "s",
        },
    },
    receive: {
        유저와지사정보받아오기: {
            brCode: "s",
            brName: "s",
            brTelNo: "s",

            userId: "s",
            userEmail: "s",
            userTel: "s",
        },
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getMyInfo() { // 지사와 로그인한 유저의 정보를 가져온다.
        CommonUI.ajax("/api/", "GET", false, function (res) {

        });
    },
    setBrInfo(brData) {
        console.log(brData);
        CommonUI.ajax("/api/", "GET", brData, function (res) {

        });
    },
    setUserInfo(userData) {
        console.log(userData);
        CommonUI.ajax("/api/", "GET", userData, function (res) {

        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $("#saveBrBtn").on("click", function () {
            alertCheck("지사 정보를 저장하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                saveBr();
            });
        });

        $("#saveUserBtn").on("click", function () {
            alertCheck("내 정보를 저장하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                saveUser();
            });
        });
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    trigs.basic();

    comms.getMyInfo();
}

function saveBr() {
    const brData = {
        brName: $("#brName").val(),
        brTelNo: $("#brTelNo").val(),
    }
    if(!brData.brName) {
        alertCaution("지사명을 입력해 주세요.", 1);
        return false;
    }

    comms.setBrInfo(brData);
}

function saveUser() {
    const userData = {
        userEmail: $("#userEmail").val(),
        userTel: $("#userTel").val(),
        userPassword: $("#userPassword").val(),
    }
    if(userData.userPassword !== $("#checkPassword").val()) {
        alertCaution("변경하실 비밀번호와 비밀번호 확인이 일치하지 않습니다.", 1);
        return false;
    }

    comms.setUserInfo(userData);
}