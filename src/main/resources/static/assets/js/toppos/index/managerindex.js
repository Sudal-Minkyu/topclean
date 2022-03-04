/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {

    },
    receive: {
        branchInfo: {
            noticeData: {
                hnId: "n",
                insertDateTime: "s",
                insert_id: "s",
                subject: "s",
            },
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    branchInfo: "/api/manager/branchInfo",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    /* 페이지 초기 화면에 뿌려지는 정보들을 받아온다. */
    branchInfo() {
        CommonUI.ajax(urls.branchInfo, "GET", null, function (res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.branchInfo, "인덱스페이지 데이터 받기");
            pieGraph(data.chartFranchOpenData);
            barGraph(data.requestWeekAmountData,"0");
            console.log(data);
            if(data.noticeData) {
                const field = $("#noticeList").children("li").children("a");
                for(let i = 0; i < data.noticeData.length; i++) {
                    $(field[i]).attr("href", `./manager/noticeview?id=${data.noticeData[i].hnId}`);
                    $(field[i]).children(".main__board-title").children("span").html(data.noticeData[i].subject);
                    $(field[i]).children(".main__board-date").children("span").html(data.noticeData[i].insertDateTime);
                }
            }
        });
    },

    taglost() { // 참고용
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

    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
    comms.branchInfo();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    trigs.basic();
}