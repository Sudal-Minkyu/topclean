/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */

import { getTaglostDetail, taglostCheck } from "../module/m_taglostDetail.js";

const dtos = {
    send: {
        tagGalleryCheck: {
            btId: "nr",
            type: "sr", // 체크 혹은 체크해제 1, 최종확인 2
        },

        franchiseInspectionMessageSend: {
            fiId: "nr",
            isIncludeImg: "s",
            fmMessage: "s",
            bcId: "n",
        },

        franchiseInspectionYn: {
            fiId: "nr",
            fiAddAmt: "n",
            type: "sr",
        },
    },
    receive: {
        franchiseInfo: {
            inspeotList: {
                fiId: "n",
                frName: "s", // 2022.03.08 추가
                bcName: "s",
                bgName: "s",
                fdS2Dt: "s",
                fdTag: "s",
                fiCustomerConfirm: "s",
                frYyyymmdd: "s",
            },

            requestHistoryList: {
                typename: "s",
                requestTime: "s",
                bcName: "s",
                bcHp: "s",
            },

            userIndexDto: {
                frReadyCashYn: "s", // 2022.06.07 추가
                slidingText: "a", // 2022.03.02 추가
                brName: "s",
                frName: "s",
                frRpreName: "s",
                frTelNo: "s",
                inCountText: "n"
            },

            tagGalleryList: {
                btBrandName: "s",
                btId: "n",
                btInputDt: "s",
                btMaterial: "s",
            },

            noticeListDtos: {
                hnId: "n",
                hnType: "s",
                insertDateTime: "s",
                insert_id: "s",
                subject: "s",
            },
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    notice: "/api/user/noticeList",
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    franchiseInfo(condition) {
        wares.currentCondition = condition;
        const url = "/api/user/franchiseInfo";
        CommonUI.ajax(url, "GET", condition, function (res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.franchiseInfo, "메인페이지 각종 값 받아오기");
            console.log(data);
            const userIndexDto = data.userIndexDto[0];
            const historyList = data.requestHistoryList;
            const inspectList = data.inspeotList;
            const tagGalleryList = data.tagGalleryList;
            const noticeList = data.noticeListDtos;

            if(userIndexDto.brName===null){
                $("#brName").text("무소속");
            } else {
                $("#brName").text(userIndexDto.brName);
            }
            $("#frName").text(userIndexDto.frName+" 점");
            $("#frRpreName").text(userIndexDto.frRpreName);
            $("#frTelNo").text(CommonUI.formatTel(userIndexDto.frTelNo));

            let slidingText = "";
            if(userIndexDto.slidingText.length) {
                slidingText = $("#brName").html() + " 의 휴무일은 ";
                userIndexDto.slidingText.forEach(date => {
                    slidingText += date.substring(0, 4) + "년 " + date.substring(4, 6) + "월 "
                        + date.substring(6, 8) + "일, ";
                });
                slidingText = slidingText.substring(0, slidingText.length - 2) + " 입니다. ";
            }
            if(userIndexDto.inCountText !== 0) {
                slidingText = slidingText + "현재 입고대기수량이 " + userIndexDto.inCountText + "건 존재합니다.";
            }
            $("#slidingText").html(slidingText);
            if(userIndexDto.frReadyCashYn === "N") {
                $('#readyCashPop').show();
                $("#bcReadyAmt").trigger("focus");
            }

            /* 메인페이지에 나열되는 항목들의 나열 */
            setHistory(historyList);
            setInspect(inspectList);
            setTagGallery(tagGalleryList);
            setNotice(noticeList);
        });
    },

    getBrInspectNeo(target) {
        CommonUI.ajax("/api/user/franchiseInspectionInfo", "GET", target, function (res) {
            const data = res.sendData;
            wares.currentBrInspect = data.inspeotInfoDto;
            wares.currentBrInspect.brFiId = data.inspeotInfoDto.fiId;
            wares.currentBrInspect.brPhotoList = data.photoList;

            openBrInspectPop();
        });
    },

    inspectionJudgement(answer) {
        dv.chk(answer, dtos.send.franchiseInspectionYn, "고객 수락 거부 응답 보내기");
        const url = "/api/user/franchiseInspectionYn";
        CommonUI.ajax(url, "PARAM", answer, function () {
            let resultMsg = "";
            if(answer.type === "1") {
                resultMsg = "세탁진행을 보고 하였습니다.";
            } else {
                resultMsg = "고객반품을 보고 하였습니다.";
            }
            alertSuccess(resultMsg);
            closeBrInspectPop(true);
        });
    },

    sendKakaoMessage(data) {
        dv.chk(data, dtos.send.franchiseInspectionMessageSend, "검품 카카오 메시지 보내기");
        CommonUI.ajax("/api/user/franchiseInspectionMessageSend", "PARAM", data, function () {
            alertSuccess("메시지 전송이 완료되었습니다.");
        });
    },

    /* 영업준비금을 저장하고 영업준비금 그리드를 다시 로딩하기 */
    saveReadyCash(saveData) {
        CommonUI.ajax("/api/user/franchiseReadyCashSave", "PARAM", saveData, function () {
        	alertSuccess("영업준비금이 저장 되었습니다.");
        });
        $('#readyCashPop').hide();
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    basic() {
        const $historyDate = $("#historyDate");
        $historyDate.on("change", function () {
            const condition = {
                date: $historyDate.val().numString(),
            };
            comms.franchiseInfo(condition);
        });

        $("#closeTaglostPop").on("click", function () {
            closeTaglostPop();
        });

        $("#frCheck").on("change", function () {
            const tagNum = $("#brTag").val();
            $("#frCheck").prop("disabled", true);
            const answer = {
                btId: wares.currentRequest.btId,
                brTag: tagNum,
                type: "1",
            };
            taglostCheck(answer);
        });

        $("#frComplete").on("click", function () {
            alertCheck("고객님 으로부터 최종확인을 받으셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                $("#frComplete").prop("disabled", true);
                const answer = {
                    btId: wares.currentRequest.btId,
                    type: "2",
                };
                taglostCheck(answer);
                $('#popupId').remove();
            });
        });

        // 확인품 팝업 기능
        $("#brCustomerConfirmed").on("click", function () {
            alertCheck("고객께서 진행수락 하셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentBrInspect.brFiId,
                    fiAddAmt: wares.currentBrInspect.fiAddAmt,
                    type: "2",
                };
                comms.inspectionJudgement(answer);
                $('#popupId').remove();
            });
        });
        $("#brCustomerDenied").on("click", function () {
            alertCheck("고객께서 진행거부 하셨습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const answer = {
                    fiId: wares.currentBrInspect.brFiId,
                    fiAddAmt: wares.currentBrInspect.fiAddAmt,
                    type: "3",
                };
                comms.inspectionJudgement(answer);
                $('#popupId').remove();
            });
        });
        $("#closeBrInspectPop").on("click", function () {
            closeBrInspectPop(false);
        });

        $("#brSendKakaoMsg").on("click", function(e) {
            alertCheck("고객님께 해당 내용을 알리고,<br>세탁진행과 반품 여부를 묻는<br>메시지를 발송하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                sendInspectMessage(e.target.id);
                $('#popupId').remove();
            });
        });

        $("#readyCashConfirm").on("click", function () {
            saveReadyCash();
        });

        $("#readyCashLater").on("click", function () {
            $("#readyCashPop").hide();
        });

        $("#bcReadyAmt").on("keyup", function () {
            $(this).val($(this).val().positiveNumberInput());
        });

        const bcReadyAmtKeyboardProp = {
            callback: function () {
                $("#bcReadyAmt").val(parseInt($("#bcReadyAmt").val()).toLocaleString());
            },
        };

        $("#bcReadyAmtKeyboard").on("click", function () {
            vkey.showKeypad("bcReadyAmt", bcReadyAmtKeyboardProp);
        });

        $("#bcReadyAmt").on("keypress", function (e) {
            if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                saveReadyCash();
            }
        });
    }
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
window.wares = {
    fiCustomerConfirmName: {
        "1": "(미처리)",
        "2": "(수락)",
        "3": "(거부)",
    },
    currentRequest: {},
    currentBrInspect: {},
    currentCondition: {},
};

// 메인페이지 슬라이딩 텍스트
function marquee(speed) {
    // text width
    const initWidth = $(".marquee__text").width();

    // position
    $(".marquee__text").css('margin-left', function() {
        return($(".marquee").width() - initWidth) / 2;
    });

    // resetting
    function resMarqueeRight () {
        $(".marquee__text").css('margin-left', $('.marquee').width());
    }

    //marquee function
    function marqueeRight () {
        $(".marquee__text").css('margin-left', function(_index, val) {
            return parseInt(val, 10) - speed + 'px';
        });

        //reset the element if it's out of it's container
        if (parseInt($(".marquee__text").css('margin-left'), 10) < -1 * $(".marquee__text").width()) {
            resMarqueeRight();
        }
    }

    setInterval(marqueeRight, 10);
}

function closeTaglostPop() {
    $("#taglostPop").removeClass("active");
}

const showTaglost = function (btId) {
    const getCondition = {
        btId,
    };

    getTaglostDetail(getCondition);
}

function getBrInspectInfo(fiId) {
    wares.currentBrInspect = {};

    const target = {
        fiId,
    };
    comms.getBrInspectNeo(target);
}

function openBrInspectPop() {
    let totAmt = wares.currentBrInspect.fdTotAmt ? wares.currentBrInspect.fdTotAmt : 0 ;
    if(wares.currentBrInspect.brFiCustomerConfirm === "2") {
        totAmt -= wares.currentBrInspect.fiAddAmt;
    }
    $("#brFdTotAmtInPut").val(totAmt.toLocaleString());

    $("#brFiComment").val(wares.currentBrInspect.fiComment
        ? wares.currentBrInspect.fiComment : "");
    $("#brFiAddAmt").val(wares.currentBrInspect.fiAddAmt
        ? wares.currentBrInspect.fiAddAmt.toLocaleString() : "0");

    if(wares.currentBrInspect.brPhotoList) {
        if(wares.currentBrInspect.brPhotoList.length) {
            $("#brInspectPhotoPanel").show();
        } else {
            $("#brInspectPhotoPanel").hide();
        }
        for (const photo of wares.currentBrInspect.brPhotoList) {
            const photoHtml = `
                <li class="tag-imgs__item">
                    <a href="${photo.ffPath + photo.ffFilename}" data-lightbox="images" data-title="이미지 확대">
                        <img src="${photo.ffPath + "s_" + photo.ffFilename}" class="tag-imgs__img" alt=""/>
                    </a>
                </li>
                `;
            $("#brPhotoList").append(photoHtml);
        }
    }

    if(wares.currentBrInspect.fiCustomerConfirm === "1") {
        $("#brCustomerDenied").parents("li").show();
        $("#brCustomerConfirmed").parents("li").show();
        $("#brSendKakaoMsg").parents("li").show();
    } else {
        $("#brCustomerDenied").parents("li").hide();
        $("#brCustomerConfirmed").parents("li").hide();
        $("#brSendKakaoMsg").parents("li").hide();
    }

    // fiCustomerConfirm 에 따른 수락거부 여부 추가?
    $("#brInspectPop").addClass("active");
}

function closeBrInspectPop(doRefresh = false) {
    if(doRefresh) {
        comms.franchiseInfo(wares.currentCondition);
    }
    $("#brInspectPop").removeClass("active");
    resetBrInspectPop();
}

function resetBrInspectPop() {
    $("#brFdTotAmtInPut").val("");
    $("#brFiAddAmt").val("0");
    $("#brFiComment").val("");
    $("#brPhotoList").html("");
}

function sendInspectMessage(sender) {
    let data = {};
    if (sender === "brSendKakaoMsg") {
        data = {
            isIncludeImg: "Y",
            fmMessage: "",
            fiId: wares.currentBrInspect.brFiId,
            bcId: wares.currentBrInspect.bcId,
        };
    }
    comms.sendKakaoMessage(data);
}

function setHistory(historyList) {
    if(historyList) {
        const field = $("#historyList").children("li").children("a");
        field.children(".main__board-badge").children("span").html("");
        field.children(".main__board-time").children("span").html("");
        field.children(".main__board-title").children("span").html("");
        field.children(".main__board-phone").children("span").html("");
        $(field).children(".main__board-badge").children("span").removeClass();

        for(let i = 0; i < historyList.length; i++) {
            $(field[i]).children(".main__board-badge").children("span").html(historyList[i].typename);
            switch(historyList[i].typename) {
                case "접수" :
                    $(field[i]).children(".main__board-badge").children("span").addClass("badge green");
                    break;
                case "출고" :
                    $(field[i]).children(".main__board-badge").children("span").addClass("badge red");
                    break;
            }
            $(field[i]).children(".main__board-time").children("span").html(historyList[i].requestTime);
            $(field[i]).children(".main__board-title").children("span").html(historyList[i].bcName);
            $(field[i]).children(".main__board-phone").children("span").html(CommonUI.formatTel(historyList[i].bcHp));
        }
    }
}

function setInspect(inspectList) {
    if(inspectList) {
        const field = $("#inspectList").children("li").children("a");
        field.children(".main__board-bcname").children("span").html("");
        field.children(".main__board-bgname").children("span").html("");
        field.children(".main__board-afttag").children("span").html("");
        field.children(".main__board-confirm").children("span").html("");

        for(let i = 0; i < inspectList.length; i++) {
            $(field[i]).on('click', function () {
                getBrInspectInfo(inspectList[i].fiId);
            });

            $(field[i]).children(".main__board-bcname").children("span").html(inspectList[i].bcName);
            $(field[i]).children(".main__board-bgname").children("span").html(inspectList[i].bgName);
            $(field[i]).children(".main__board-afttag").children("span")
                .html(CommonData.formatFrTagNo(inspectList[i].fdTag, frTagInfo.frTagType));
            $(field[i]).children(".main__board-confirm").children("span")
                .html(wares.fiCustomerConfirmName[inspectList[i].fiCustomerConfirm]);
        }
    }
}

function setTagGallery(tagGalleryList) {
    if(tagGalleryList) {
        const field = $("#taglostList").children("li").children("a");
        for(let i = 0; i < tagGalleryList.length; i++) {
            $(field[i]).children(".main__board-title").children("span:nth-child(1)")
                .html(tagGalleryList[i].btBrandName);
            $(field[i]).children(".main__board-title").children("span:nth-child(2)")
                .html("&nbsp;(" + tagGalleryList[i].btMaterial + ")");
            $(field[i]).children(".main__board-date").children("span")
                .html(tagGalleryList[i].btInputDt);
            $(field[i]).on('click', function () {
                showTaglost(tagGalleryList[i].btId);
            });
        }
    }
}

function setNotice(noticeList) {
    if(noticeList) {
        const field = $("#noticeList").children("li").children("a");
        for(let i = 0; i < noticeList.length; i++) {
            $(field[i]).attr("href", `./user/noticeview?id=${noticeList[i].hnId}`);
            $(field[i]).children(".main__board-bcname").children("span")
                .html(CommonData.name.hnType[noticeList[i].hnType]);
            $(field[i]).children(".main__board-title").children("span").html(noticeList[i].subject);
            $(field[i]).children(".main__board-date").children("span").html(noticeList[i].insertDateTime);
        }
    }
}

function saveReadyCash() {
    const bcYyyymmdd = new Date().format("yyyyMMdd");
    const bcReadyAmt = $("#bcReadyAmt").val().toInt();

    if(isNaN(bcReadyAmt)) {
        alertCaution("올바른 영업준비금액을 입력해 주세요.", 1);
        return;
    }

    const saveData = {
        bcYyyymmdd: bcYyyymmdd,
        bcReadyAmt: bcReadyAmt,
    }
    comms.saveReadyCash(saveData);
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    const today = new Date().format("yyyy-MM-dd");
    const condition = {
        date: today.numString(),
    };
    comms.franchiseInfo(condition);

    CommonUI.setDatePicker(["historyDate"]);

    const $historyDate = $("#historyDate");
    $historyDate.val(today);
    $historyDate.datepicker("option", "maxDate", today);
    $historyDate.datepicker("option", "changeYear", false);
    $historyDate.datepicker("option", "changeMonth", false);
    $("#ui-datepicker-div").hide();

    window.vkey = new VKeyboard();
    trigs.basic();

    // 슬라이딩 텍스트 실행
    marquee(1);

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
}