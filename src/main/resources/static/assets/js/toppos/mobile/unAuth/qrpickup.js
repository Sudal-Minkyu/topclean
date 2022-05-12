const dtos = {
    send: {
        info: {
            frCode: "s",
        },

        process: {
            frCode: "s",
        },
    },
    receive: {
        info: {
            frName: "s",
        }
    }
};

const urls = {
    getFrName: "/api/collection/info",
    confirmPickUp: "/api/collection/process",
};

const comms = {
    /* 가맹점 코드를 통해서 가맹점명을 받아온다. */
    getFrName(condition) {
        dv.chk(condition, dtos.send.info, "가맹점명 받아오기 위한 가맹점 코드 보내기");
        CommonUI.ajax(urls.getFrName, "GET", condition, function (res) {
            dv.chk(res.sendData, dtos.receive.info, "받아온 가맹점명");
            wares.frName = res.sendData.frName;
            $(".frName").html(wares.frName);
        });
    },

    /* 가맹점 세탁물 수거완료신호 보내기 */
    confirmPickUp(condition) {
        dv.chk(condition, dtos.send.process, "세탁물 수거완료 처리를 위한 가맹점 코드 보내기");
        CommonUI.ajax(urls.confirmPickUp, "GET", condition, function () {
            alertSuccess("세탁물 수거완료 처리 하였습니다.");
            $("#pickUpBtn").hide();
            $("#pickStatus").html("세탁물을 수거 완료 하였습니다.");
            /* 완료이후 get주소에 완료했음을 알리는 신호 추가하여 히스토리 바꿔치기 */
            history.replaceState({}, "", `qrpickup?frcode=${wares.frCode}&isdone=1`);
        });
    },
};

const trigs = {
    basic() {
        $("#pickUpBtn").on("click", function() {
            alertCheck(`${wares.frName}점의 현재시간까지 접수된<br>세탁물을 수거완료 하시겠습니까?`);
            $("#checkDelSuccessBtn").on("click", function () {
                comms.confirmPickUp({frCode: wares.frCode});
            });
        });
    },
};


const wares = {
    url: window.location.href,
    params: "",
    frCode: "",
    frName: "",
    isDone: 0,
};

$(function() {
    onPageLoad();
});

function onPageLoad() {
    trigs.basic();
    updateTime();
    setInterval(updateTime, 1000);

    getParams();
    setInputs();
    comms.getFrName({frCode: wares.frCode});
}

/* 시계 */
function updateTime() {
    const now = new Date().toLocaleString();
    $("#timeNow").html(now);
}

/* 겟 주소로 오는 파라메터들 저장 */
function getParams() {
    const url = new URL(wares.url);
    wares.params = url.searchParams;

    if(wares.params.has("frcode")) {
        wares.frCode = wares.params.get("frcode");
    } else {
        wares.frCode = "";
    }

    if(wares.params.has("isdone")) {
        wares.isDone = wares.params.get("isdone").toInt();
    } else {
        wares.isDone = 0;
    }
}

/* 히스토리 기준으로 다시 페이지에 들렀을 때, 수거완료된 페이지일 경우 버튼을 숨기고 완료 알림 */
function setInputs() {
    if(wares.isDone) {
        $("#pickUpBtn").hide();
        $("#pickStatus").html("세탁물을 수거 완료 하였습니다.");
    }
}
