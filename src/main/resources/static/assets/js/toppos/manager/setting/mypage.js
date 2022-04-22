/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchInfoSave: {
            brName: "s",
            brTelNo: "s",
        },
        branchMyInfoSave: {
            userEmail: "s",
            userTel: "s",
            nowPassword: "s",
            newPassword: "s",
            checkPassword: "s", // 값이 갈 경우 암호 변경, 빈값이 갈 경우 필드값의 변경 X
        },
    },
    receive: {
        myInfo: {
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
        CommonUI.ajax("/api/manager/myInfo", "GET", false, function (res) {
            const data = res.sendData.branchInfoDto;
            console.log(data);
            dv.chk(data, dtos.receive.myInfo, "지사, 유저 정보 받아오기");
            putInData(data);
        });
    },
    setBrInfo(brData) {
        CommonUI.ajax("/api/manager/branchInfoSave", "PARAM", brData, function (res) {
            alertSuccess("지사 정보 변경 완료");
        });
    },
    setUserInfo(userData) {
        console.log(userData);
        CommonUI.ajax("/api/manager/branchMyInfoSave", "PARAM", userData, function (res) {
            alertSuccess("나의 정보 변경 완료");
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $("#saveBrBtn").on("click", function () {
            if($("#brName").val() === "") {
                alertCaution("지사명을 입력해 주세요.", 1);
                return false;
            }
            alertCheck("지사 정보를 저장하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                saveBr();
            });
        });

        $("#saveUserBtn").on("click", function () {
            if($("#userTel").val() === "") {
                alertCaution("전화번호를 입력해 주세요.", 1);
                return false;
            }
            // 현재 비밀번호 길이
            const $nowPwLength = $("#nowPassword").val().length;
            // 변결할 비밀번호 길이
            const $newPwLength = $("#newPassword").val().length;
            // 변경할 비밀번호 체크 길이
            const $checkPwLength = $("#checkPassword").val().length;
            if(($nowPwLength > 0 || $newPwLength > 0 || $checkPwLength > 0)
                && ($nowPwLength === 0 || $newPwLength === 0 || $checkPwLength === 0)) {
                alertCaution("비밀번호 변경 시 현재 비밀번호, 비밀번호 변경, 비밀번호 확인란 모두 입력해 주세요", 1);
                return false;
            }
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

// 지사, 유저 정보 넣기
function putInData(myInfoData) {
    $("input[name='brCode']").val(myInfoData.brCode);
    $("input[name='brName']").val(myInfoData.brName);
    $("input[name='brTelNo']").val(CommonUI.formatTel(myInfoData.brTelNo));
    $("input[name='userId']").val(myInfoData.userId);
    $("input[name='userEmail']").val(myInfoData.userEmail);
    $("input[name='userTel']").val(CommonUI.formatTel(myInfoData.userTel));
}

function saveBr() {
    const brData = {
        brName: $("#brName").val(),
        brTelNo: $("#brTelNo").val().numString(),
    }

    comms.setBrInfo(brData);
}

function saveUser() {
    const userData = {
        userEmail: $("#userEmail").val(),
        userTel: $("#userTel").val().numString(),
        nowPassword: $("#nowPassword").val(),
        newPassword: $("#newPassword").val(),
        checkPassword: $("#checkPassword").val(),
    }
    if(userData.newPassword != userData.checkPassword) {
        alertCaution("변경할 비밀번호와 확인이 일치하지 않습니다.", 1);
        return false;
    }
    comms.setUserInfo(userData);
}