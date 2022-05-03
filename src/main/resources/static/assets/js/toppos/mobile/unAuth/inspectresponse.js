const dtos = {
    send: {
        franchiseInspectionYn: {
            fiId: "nr",
            fiAddAmt: "n",
            type: "sr",
        },
    },
    receive: {

    }
};

const comms = {
    getInspect(target) {
        CommonUI.ajax("/api/mobile/unAuth/franchiseInspectionMobileInfo", "GET", target, function (res) {
            console.log(res);
            const data = res.sendData;
            wares.currentInspect = data.inspeotInfoDto;
            wares.currentInspect.photoList = data.photoList;

            if(data.fiCustomerConfirm === "2") { // 고객 응답상태일 때 응답을 할 수 없도록 한다.
                $("#btnCustomerConfirm, #btnCustomerDeny").remove();
                $("#introduce").html(`
                    검수과정 중 <span id="fiComment">오염, 탈색, 찍힘</span>이 발견되었습니다.<br>
                    고객님 께서는 세탁 진행을 요청 하셨습니다.
                `);
            } else if(data.fiCustomerConfirm === "3") {
                $("#btnCustomerConfirm, #btnCustomerDeny").remove();
                $("#introduce").html(`
                    검수과정 중 <span id="fiComment">오염, 탈색, 찍힘</span>이 발견되었습니다.<br>
                    고객님 께서는 반품을 요청 하셨습니다.
                `);
            }

            $("#fiComment").html(wares.currentInspect.fiComment);

            for(const photo of wares.currentInspect.photoList) {
                const photoHtml = `
                    <li>
                        <a href="${photo.ffPath + photo.ffFilename}" class="inspect__img-link">
                            <img src="${photo.ffPath + "s_" + photo.ffFilename}" alt="" />
                        </a>
                    </li>
                `;
                $("#photoList").append(photoHtml);
            }

            $("#btnCallFranchise").html(
                `가맹점 전화하기<br>(${CommonUI.formatTel(wares.currentInspect.frTelNo)})`);
        });
    },

    inspectionJudgement(answer) {
        dv.chk(answer, dtos.send.franchiseInspectionYn, "고객 수락 거부 응답 보내기");
        console.log(answer);
        CommonUI.ajax("/api/mobile/unAuth/franchiseInspectionYn", "PARAM", answer, function (res) {
            let resultMsg = "";
            if(answer.type === "2") {
                resultMsg = "세탁진행을 요청 하였습니다.";
            } else {
                resultMsg = "반품을 요청 하였습니다.";
            }
            alertSuccess(resultMsg);
        });
    },
};

const trigs = {
    basic() {
        $("#btnCustomerConfirm").on("click", function () {
            alertCheck("세탁진행 하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentInspect.fiId,
                    fiAddAmt: wares.currentInspect.fiAddAmt,
                    type: "2",
                };
                comms.inspectionJudgement(answer);
                $('#popupId').remove();
            });
        });

        $("#btnCustomerDeny").on("click", function () {
            alertCheck("반품요청 하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentInspect.fiId,
                    fiAddAmt: wares.currentInspect.fiAddAmt,
                    type: "3",
                };
                comms.inspectionJudgement(answer);
                $('#popupId').remove();
            });
        });

        $("#btnCallFranchise").on("click", function () {
            location.href = "tel:" + wares.currentInspect.frTelNo;
        });
    },
}

const wares = {
    currentInspect: {},
}

$(function() {
    onPageLoad();
});

function onPageLoad() {
    trigs.basic();
    chkParams();
}

function chkParams() {
    const url = new URL(window.location.href);
    wares.params = url.searchParams;

    if(wares.params.has("fiid")) {
        const fiId = wares.params.get("fiid");

        const target = {
            fiId: parseInt(fiId),
        }

        comms.getInspect(target);
    } else {
        $("div .content").html(`
            정상적이지 않은 접근을 감지하였습니다.<br>
            창을 닫아주세요.
        `);
    }
}