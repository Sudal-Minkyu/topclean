/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {

    },
    receive: {
        franchiseInfo: {
            slidingText: "s", // 2022.03.02 추가
            brName: "s",
            frName: "s",
            username: "s",
            usertel: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    taglost: "/api/user/lostNoticeList",
    notice: "/api/user/noticeList",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    franchiseInfo(condition) {
        console.log(condition);
        const url = "/api/user/franchiseInfo";
        CommonUI.ajax(url, "GET", condition, function (res) {
            const userIndexDto = res.sendData.userIndexDto[0];
            const historyList = res.sendData.requestHistoryList;
            console.log(res);

            if(userIndexDto.brName===null){
                $("#brName").text("무소속");
            }else{
                $("#brName").text(userIndexDto.brName);
            }
            $("#frName").text(userIndexDto.frName+" 점");
            $("#userName").text(userIndexDto.username);
            $("#userTel").text(CommonUI.formatTel(userIndexDto.usertel));

            if(userIndexDto.slidingText.length) {
                let slidingText = $("#brName").html() + " 의 휴무일은 ";
                userIndexDto.slidingText.forEach(date => {
                    slidingText += date.substring(0, 4) + "년 " + date.substring(4, 6) + "월 "
                        + date.substring(6, 8) + "일, ";
                });
                slidingText = slidingText.substring(0, slidingText.length - 2) + " 입니다.";
                $("#slidingText").html(slidingText);
            }

            if(historyList) {
                const field = $("#historyList").children("li").children("a");
                field.children(".main__board-badge").children("span").html("");
                field.children(".main__board-time").children("span").html("");
                field.children(".main__board-title").children("span").html("");
                field.children(".main__board-phone").children("span").html("");

                for(let i = 0; i < historyList.length; i++) {
                    $(field[i]).children(".main__board-badge").children("span").html(historyList[i].typename);
                    switch(historyList[i].typename) {
                        case "접수" :
                            $(field[i]).children(".main__board-badge").children("span").addClass("badge green");
                            break;
                        case "인도" :
                            $(field[i]).children(".main__board-badge").children("span").addClass("badge red");
                            break;
                    }
                    $(field[i]).children(".main__board-time").children("span").html(historyList[i].requestTime);
                    $(field[i]).children(".main__board-title").children("span").html(historyList[i].bcName);
                    $(field[i]).children(".main__board-phone").children("span").html(CommonUI.formatTel(historyList[i].bcHp));
                }
            }
        });
    },

    notice() {
        CommonUI.ajax(urls.notice, "PARAM", wares.boardConditionThree, function (res) {
            const data = res.datalist;
            if(data) {
                const field = $("#noticeList").children("li").children("a");
                for(let i = 0; i < data.length; i++) {
                    $(field[i]).attr("href", `./user/noticeview?id=${data[i].hnId}`);
                    $(field[i]).children(".main__board-title").children("span").html(data[i].subject);
                    $(field[i]).children(".main__board-date").children("span").html(data[i].insertDateTime);
                }
            }
        });
    },

    taglost() {
        CommonUI.ajax(urls.taglost, "PARAM", wares.boardConditionThree, function (res) {
            const data = res.datalist;
            if(data) {
                const field = $("#taglostList").children("li").children("a");;
                for(let i = 0; i < data.length; i++) {
                    $(field[i]).attr("href", `./user/taglostview?id=${data[i].htId}`);
                    $(field[i]).children(".main__board-title").children("span:nth-child(1)").html(data[i].subject);
                    $(field[i]).children(".main__board-title").children("span:nth-child(2)").html("(" + data[i].numOfComment + ")");
                    $(field[i]).children(".main__board-date").children("span").html(data[i].insertDateTime);
                }
            }
        });
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    basic() {
        const $historyDate = $("#historyDate");
        $historyDate.on("change", function () {
            const condition = {
                date: $historyDate.val().numString(),
            }
            comms.franchiseInfo(condition);
        });
    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    boardConditionThree : {
        page: 0,
        size: 3,
        searchString: "",
        filterFromDt: "",
        filterToDt: "",
    }
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 365);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");
    wares.boardConditionThree.filterFromDt = fromday;
    wares.boardConditionThree.filterToDt = today;

    const condition = {
        date: today.numString(),
    }

    comms.franchiseInfo(condition);
    comms.notice();
    comms.taglost();
    const datePickerId = ["historyDate"];
    CommonUI.setDatePicker(datePickerId);
	$("#historyDate").val(today);
    $("#historyDate").datepicker("option", "maxDate", today);
    trigs.basic();
}