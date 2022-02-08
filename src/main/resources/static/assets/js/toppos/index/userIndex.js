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
    taglost: "/api/user/lostNoticeList",
    notice: "/api/user/noticeList",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    franchiseInfo() {
        const url = "/api/user/franchiseInfo";
        CommonUI.ajax(url, "GET", null, function (req) {
            const userIndexDto = req.sendData.userIndexDto;
            // console.log(userIndexDto);
            if(userIndexDto.brName===null){
                $("#brName").text("무소속");
            }else{
                $("#brName").text(userIndexDto.brName);
            }
            $("#frName").text(userIndexDto.frName+" 점");
            $("#userName").text(userIndexDto.username);
            $("#userTel").text(CommonUI.formatTel(userIndexDto.usertel));
        });
    },
    notice() {
        CommonUI.ajax(urls.notice, "PARAM", wares.boardConditionThree, function (res) {
            const data = res.datalist;
            const field = $("#noticeList").children("li");
            for(let i = 0; i < data.length; i++) {
                $(field[i]).children(".main__board-title").on("click", function() {
                    location.href = `./user/noticeview?id=${data[i].hnId}`;
                });
                $(field[i]).children(".main__board-title").children("span").html(data[i].subject);
                $(field[i]).children(".main__board-date").children("span").html(data[i].insertDateTime);
            }
        });
    },
    taglost() {
        CommonUI.ajax(urls.taglost, "PARAM", wares.boardConditionThree, function (res) {
            const data = res.datalist;
            const field = $("#taglostList").children("li");
            for(let i = 0; i < data.length; i++) {
                $(field[i]).children(".main__board-title").on("click", function() {
                    location.href = `./user/taglostview?id=${data[i].htId}`;
                });
                $(field[i]).children(".main__board-title").children("span").html(data[i].subject);
                $(field[i]).children(".main__board-date").children("span").html(data[i].insertDateTime);
            }
        });
    },
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
    fromday.setDate(fromday.getDate() - 363);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");
    wares.boardConditionThree.filterFromDt = fromday;
    wares.boardConditionThree.filterToDt = today;

    comms.franchiseInfo();
    comms.notice();
    comms.taglost();
    const datePickerId = ["indexDate"];
    CommonUI.setDatePicker(datePickerId);
	$("#indexDate").val(new Date().format("yyyy-MM-dd"));
}