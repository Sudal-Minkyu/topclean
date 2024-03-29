/*
* 자주 쓰이는 UI용 함수를 모듈화
* CommonUI.기능 으로 호출하여 사용
*
* 제작 : 성낙원
* 수정 : 2022-09-20
* */

/**
 * 사용자의 입력을 막는 스크린 블로커 기능을 담은 클래스.
 * 여러 메시지로 블로커를 띄우면 모든 메시지가 다 꺼져야 화면을 막던 블로커가 내려간다.
 */
class ScreenBlocker {
    balance;
    keys;

    constructor() {
        this.balance = {};
        this.keys = [];
    }

    /**
     * 해당 문자를 알림 메시지로 하는 스크린 블로커를 작동시킨다.
     * @param screenText : string - 출력하고자 하는 스크린블로커의 문자.
     */
    on(screenText = '로딩중 입니다.') {
        document.getElementById('blocker-loading-text').innerHTML = screenText;
        if (this.balance[screenText] && this.balance[screenText] > 0) {
            this.balance[screenText]++;
        } else {
            this.balance[screenText] = 1;
            document.getElementById('blocker-loading').style.display = 'block';
            if (!this.keys.includes(screenText)) {
                this.keys.push(screenText);
            }
        }
    }

    /**
     * 해당 문자를 알림 메시지로 하는 스크린 블로커를 끈다.
     * @param screenText : string - 끄고자 하는 스크린블로커의 문자.
     */
    off(screenText = '로딩중 입니다.') {
        this.balance[screenText]--;
        if (this.balance[screenText] === 0) {
            for (const key of this.keys) {
                if (this.balance[key]) {
                    document.getElementById('blocker-loading-text').innerHTML = key;
                    return;
                }
            }
            document.getElementById('blocker-loading').style.display = 'none';
        }
    }
}

/**
 * 사용자의 화면을 막는 스크린 블로커 클래스를 준비한다. 해당 클래스의 on, off 펑션을 사용해 키고 끈다.
 */
window.broscreener = new ScreenBlocker();

class CommonUIClass {
    commsErrMsg = true;

    constructor() {
        /* 숫자만 남긴 후 인트형으로 전환 */
        String.prototype.toInt = function () {
            return this.toString() ? parseInt(this.replace(/[^0-9-]/g, ""), 10) : 0;
        };

        /* 0~9까지만 남긴 문자를 반환, 앞자리가 0으로 시작할 수 있음. */
        String.prototype.numString = function () {
            return this.toString() ? this.replace(/[\D]/g, "") : "";
        };

        /* 입력창 숫자 입력 검증용 : -를 포함한 숫자 입력, 천단위 쉼표, 빈문자일 때 유지 */
        String.prototype.numberInput = function () {
            const filteredValue = this.replace(/[^0-9-]/g, "");
            if (["", "-"].includes(filteredValue)) {
                return filteredValue;
            }
            return parseInt(filteredValue, 10).toLocaleString();
        }

        /* 입력창 숫자 입력 검증용 : -를 포함한 숫자 입력, 천단위 쉼표, 빈문자일 때 유지, 양수 입력 전용 */
        String.prototype.positiveNumberInput = function () {
            const filteredValue = this.replace(/[^0-9]/g, "");
            if (filteredValue === "") {
                return "";
            }
            return parseInt(filteredValue, 10).toLocaleString();
        }

        /* 통신 에러의 경우 최초 단 1회만 보고한다. (무한루프 가능성 때문) */
        this.commsErrMsg = true;
        this.setKrDatepicker();
    }

    toppos = {
        colorName: { // 컬러코드에 따른 실제 색상
            "00": "", "01": "흰색", "02": "검정", "03": "회색", "04": "빨강", "05": "주황",
            "06": "노랑", "07": "초록", "08": "파랑", "09": "남색", "10": "보라", "11": "핑크",
        },
        payTypeDic: {
            "01": "cash",
            "02": "card",
            "03": "save",
            "04": "미수결제",
        },

        /* 아이템 코드와 이름 데이터 조합에 쓰이는 배열을 사용하여 최종 이름을 산출 */
        makeProductName(item, nameArray) {
            if(!item.productName) {
                const isNotSizeNormal = (item.biItemcode.substr(3, 1) !== "N");
                let productName = "";
                for(const obj of nameArray) {
                    if (obj.biItemcode === item.biItemcode) {
                        if (isNotSizeNormal) {
                            productName += obj.bsName + " ";
                        }
                        productName += obj.biName + " " + obj.bgName;
                        break;
                    }
                }
                item.productName = productName;
            }
        },

        /* 대중소분류 이름을 가져다가 최종 이름을 조합하는 방식 */
        makeSimpleProductName(item) {
            const finalBsName = item.bsName === "일반" ? "" : item.bsName;
            return finalBsName + (finalBsName ? " " : "") + item.biName + " " + item.bgName;
        },

        /* 조합되는 이름을 그리드에 뿌려지기 전 단계에 완성하여 데이터에 포함시킨다. */
        makeSimpleProductNameList(items) {
            items.forEach(item => {
                item.productName = this.makeSimpleProductName(item);
            });
        },

        /* 처리내역의 표시를 결정하는 공통함수 */
        processName(item) {
            try {
                let statusText = "";
                let pollution = "";
                let promotion = "";

                const promotionTypeName = {
                    "H1": "행(수기)",
                    "01": `행(${item.fdPromotionDiscountRate}%)`,
                    "02": `행(1+1${item.fdPromotionDiscountRate === 100 ? "*" : ""})`,
                    "03": `행(2+1${item.fdPromotionDiscountRate === 100 ? "*" : ""})`,
                }
                if (item.fdPromotionType) {
                    promotion = promotionTypeName[item.fdPromotionType];
                }

                if (item.fdPollution) {
                    if (item.fdPollutionType === 1 && !item.fdPollutionBack) {
                        pollution = "오(앞)";
                    } else if (item.fdPollutionType === 1 && item.fdPollutionBack) {
                        pollution = "오(뒤)";
                    } else if (item.fdPollutionType === 2) {
                        pollution = "오(앞뒤)";
                    } else {
                        pollution = "오";
                    }
                }

                statusText += promotion;
                statusText += item.fdUrgentYn === "Y" ? (statusText ? "," : "") + "급" : "";
                statusText += item.fdPriceGrade === "3" ? (statusText ? "," : "") + "명" : "";
                statusText += item.fdRetryYn === "Y" ? (statusText ? "," : "") + "재" : "";
                statusText += item.fdPressed ? (statusText ? "," : "") + "다" : "";
                statusText += item.fdAdd1Amt || item.fdAdd1Remark.length ? (statusText ? "," : "") + "추" : "";
                statusText += item.fdRepairAmt || item.fdRepairRemark.length ? (statusText ? "," : "") + "수" : "";
                statusText += item.fdWhitening ? (statusText ? "," : "") + "표" : "";
                statusText += item.fdWaterRepellent || item.fdStarch ? (statusText ? "," : "") + "발" : "";
                statusText += pollution ? (statusText ? "," : "") + pollution : "";
                statusText += item.fdS6CancelYn === "Y" ? (statusText ? "," : "") + "고객출고취소" : "";
                return statusText;
            } catch (e) {
                this.toppos.underTaker(e, "CommonUI : 처리내역 문자조합");
            }
            return '';
        },

        killNullFromArray(array) {
            const resultArray = [];
            for(const obj of array) {
                if(obj !== null) {
                    resultArray.push(obj);
                }
            }
            return resultArray;
        },

        formatBusinessHoursForReceipt(paymentData) {
            function isPrintBusinessTimeOn(testStat) {
                return ["1", "2", "3"].includes(testStat);
            }

            const timeFormatTargetList = ['frOpenWeekday', 'frOpenSaturday', 'frOpenHoliday'
                , 'frCloseWeekday', 'frCloseSaturday', 'frCloseHoliday'];

            for (const targetName of timeFormatTargetList) {
                if (paymentData[targetName] && paymentData[targetName].length === 4) {
                    paymentData[targetName] = paymentData[targetName].substring(0, 2) + ":"
                        + paymentData[targetName].substring(2, 4);
                }
            }

            paymentData.isPrintBusinessTimeOn = isPrintBusinessTimeOn(paymentData.frStatWeekday) || isPrintBusinessTimeOn(paymentData.frStatSaturday)
                || isPrintBusinessTimeOn(paymentData.frStatHoliday);

            return paymentData;
        },

        refineReceiptDataForPrint(receiptData) {
            receiptData.paymentData.items = receiptData.items;
            receiptData.paymentData.items.forEach(obj => {
                obj.color = CommonUI.toppos.colorName[obj.color];
                if (obj.priceGrade === "3") {
                    obj.fdRemark = "명품 " + obj.fdRemark;
                }
                obj.tagno = CommonData.formatFrTagNo(obj.tagno, window.frTagInfo.frTagType);
            });

            receiptData.paymentData.estimateDt
                = receiptData.paymentData.items.length ? receiptData.paymentData.items[0].estimateDt : "";
            receiptData.paymentData.franchiseTel = CommonUI.formatTel(receiptData.paymentData.franchiseTel);
            receiptData.paymentData.customerTel
                = CommonUI.formatTel(receiptData.paymentData.customerTel, true);
            receiptData.paymentData.businessNO = CommonUI.formatBusinessNo(receiptData.paymentData.businessNO);

            receiptData.paymentData = CommonUI.toppos.formatBusinessHoursForReceipt(receiptData.paymentData);

            receiptData.creditData.forEach(obj => {
                obj.type = CommonUI.toppos.payTypeDic[obj.type];
            });

            return receiptData;
        },

        printReceipt(frNo = "", frId = "", printCustomers = false, printOwners = false, cancelYN = "N") {
            const condition = {
                frNo,
                frId,
            };
            try {
                const url = "/api/user/requestPaymentPaper";

                CommonUI.ajax(url, "GET", condition, function (res) {
                    const refinedReceiptData = CommonUI.toppos.refineReceiptDataForPrint(res.sendData);

                    CAT.CatPrint_Multi(refinedReceiptData.paymentData, refinedReceiptData.creditData
                        , cancelYN, printCustomers, printOwners);
                });
            } catch (e) {
                this.toppos.underTaker(e, "CommonUI : 접수 영수증 프린트");
            }
        },

        printReleaseReceipt(releaseInfo) {
            const condition = {
                frNo: releaseInfo.frNo,
                frId: "",
            };
            try {
                const url = "/api/user/requestPaymentPaper";

                CommonUI.ajax(url, "GET", condition, function (res) {

                    let paymentData = res.sendData.paymentData;
                    paymentData = CommonUI.toppos.formatBusinessHoursForReceipt(paymentData);
                    releaseInfo.releaseDateTime = new Date().format('yyyy-MM-dd hh:mm');
                    releaseInfo.franchiseTel = CommonUI.formatTel(paymentData.franchiseTel);
                    releaseInfo.customerTel = CommonUI.formatTel(paymentData.customerTel, true);
                    releaseInfo.businessNO = CommonUI.formatBusinessNo(paymentData.businessNO);

                    releaseInfo.customerName = paymentData.customerName;
                    releaseInfo.franchiseName = paymentData.franchiseName;
                    releaseInfo.repreName = paymentData.repreName;
                    releaseInfo.frOpenWeekday = paymentData.frOpenWeekday;
                    releaseInfo.frOpenSaturday = paymentData.frOpenSaturday;
                    releaseInfo.frOpenHoliday = paymentData.frOpenHoliday;
                    releaseInfo.frCloseWeekday = paymentData.frCloseWeekday;
                    releaseInfo.frCloseSaturday = paymentData.frCloseSaturday;
                    releaseInfo.frCloseHoliday = paymentData.frCloseHoliday;
                    releaseInfo.frStatWeekday = paymentData.frStatWeekday;
                    releaseInfo.frStatSaturday = paymentData.frStatSaturday;
                    releaseInfo.frStatHoliday = paymentData.frStatHoliday;
                    releaseInfo.isPrintBusinessTimeOn = paymentData.isPrintBusinessTimeOn;

                    releaseInfo.items.forEach(obj => {
                        obj.color = CommonUI.toppos.colorName[obj.color];
                        if (obj.priceGrade === "3") {
                            obj.fdRemark = "명품 " + obj.fdRemark;
                        }
                        obj.tagno = CommonData.formatFrTagNo(obj.tagno, window.frTagInfo.frTagType);
                    });

                    CAT.CatPrint_Release(releaseInfo);
                });
            } catch (e) {
                this.toppos.underTaker(e, "CommonUI : 고객 출고증 프린트");
            }
        },

        // printRepaymentReceipt(condition, cancelYN = "N") { // 미수금 상환용 영수증 출력
        //     try {
        //         const url = "/api/user/requestPaymentPaper";
        //
        //         CommonUI.ajax(url, "GET", condition, function (res) {
        //             const refinedReceiptData = CommonUI.toppos.refineReceiptDataForPrint(res.sendData);
        //
        //             CAT.CatPrint_Repayment(refinedReceiptData.paymentData, refinedReceiptData.creditData, cancelYN);
        //         });
        //     } catch (e) {
        //         this.toppos.underTaker(e, "CommonUI : 미수금 영수증 프린트");
        //     }
        // },

        publishCashRceipt(paymentData, callback = function () {/* 빈기능 */}) {
            CAT.CatSetCashReceipt(paymentData, function (res) {
                $('#payStatus').hide();
                res = JSON.parse(res);
                if(res.STATUS === "SUCCESS") {
                    paymentData.fcCatApprovaltime = res.APPROVALTIME;
                    paymentData.fcCatApprovalno = res.APPROVALNO;
                    paymentData.fcCatCardno = res.CARDNO;
                    paymentData.fcCatMessage1 = res.MESSAGE1;
                    paymentData.fcCatMessage2 = res.MESSAGE2;
                    paymentData.fcCatNotice1 = res.NOTICE1;
                    paymentData.fcCatTotamount = res.TOTAMOUNT;
                    paymentData.fcCatVatamount = res.VATAMOUNT;
                    paymentData.fcCatTelegramflagt = res.TELEGRAMFLAG;

                    paymentData.fcCatIssuercode = res.ISSUERCODE;
                    paymentData.fcCatIssuername = res.ISSUERNAME;
                    paymentData.fcMuechantnumber = res.MERCHANTNUMBER;

                    CommonUI.ajax("/api/user/requestPaymentCashPaper", "PARAM", paymentData, function () {
                        alertSuccess("현금영수증이 정상 발행 되었습니다.");
                    });
                    return callback();
                } else if (res.STATUS === "FAILURE") {
                    if (res.ERRORDATA === "erroecode:404, error:error") {
                        alertCancel("카드결제 단말기 연결이 감지되지 않습니다.<br>연결을 확인해 주세요.");
                    } else if (res.ERRORDATA === "erroecode:0, error:timeout") {
                        alertCancel("유효 등록 시간이 지났습니다.<br>다시 결제창 버튼을 이용하여<br>원하시는 기능을 선택 해주세요.");
                    } else if (res.ERRORMESSAGE === "단말기에서종료키누름 /                  /                 ") {
                        alertCancel("단말기 종료키를 통해 결제가 중지되었습니다.");
                    } else if (res.ERRORMESSAGE === " /  / ") {
                        alertCancel("단말기가 사용중입니다.<br>확인 후 다시 시도해 주세요.");
                    } else {
                        alertCancel(res.ERRORMESSAGE);
                    }
                }
                return function () {/* 빈기능 */};
            });
        },

        /* 현금영수증 취소 */
        cancelCashReceipt(paymentData, successCall = function () {/* 빈기능 */}) {
            CAT.CatCancelCashReceipt(paymentData, function (res) {
                $('#payStatus').hide();
                res = JSON.parse(res);
                if(res.STATUS === "SUCCESS"
                    || res.ERRORMESSAGE === "                     / 기취소자료       / 취소불가        ") {
                    const targetReceipt = {
                        fcId: paymentData.fcId,
                    }
                    /* 현금영수증 취소 여부를 알리는 API */
                    CommonUI.ajax("/api/user/requestCashReceiptCencel",
                        "PARAM", targetReceipt, function (res) {
                        alertSuccess("현금영수증을 취소 하였습니다.");
                    });
                    return successCall();
                } else if (res.STATUS === "FAILURE") {
                    if (res.ERRORDATA === "erroecode:404, error:error") {
                        alertCancel("카드결제 단말기 연결이 감지되지 않습니다.<br>연결을 확인해 주세요.");
                    } else if (res.ERRORDATA === "erroecode:0, error:timeout") {
                        alertCancel("유효 시간이 지났습니다.<br>다시 결제취소창을 이용하여<br>원하시는 기능을 선택 해주세요.");
                    } else if (res.ERRORMESSAGE === "단말기에서종료키누름 /                  /                 ") {
                        alertCancel("단말기 종료키를 통해 결제가 중지되었습니다.");
                    } else if (res.ERRORMESSAGE === " /  / ") {
                        alertCancel("단말기가 사용중입니다.<br>확인 후 다시 시도해 주세요.");
                    } else {
                        alertCancel(res.ERRORMESSAGE);
                    }
                }
                return function () {/* 빈기능 */};
            });
        },

        /* 등록된 단말기 고유 터미널 번호 정보를 가져와 콜백 함수의 인자에 넣어 반환한다. */
        getTID(callback = function () {/* 빈함수 */}) {
            CAT.CatGetCurrentTID(function (res) {
                res = JSON.parse(res);
                return callback(res);
            });
        },

        speak(text, callback = function() {/* 빈기능 */}) {
            if(typeof speechSynthesis === 'undefined') {
                return;
            }

            const voices = speechSynthesis.getVoices();

            const voiceProp = new SpeechSynthesisUtterance(text);

            let recommendNotExist = true;
            for(const voice of voices) {
                if (voice.name === "Google 한국의") {
                    voiceProp.voice = voice;
                    voiceProp.pitch = 1;
                    voiceProp.rate = 1.1;
                    recommendNotExist = false;
                    break;
                }
            }

            if(recommendNotExist) {
                for(const voice of voices) {
                    if (voice.lang.indexOf('ko-KR') >= 0 || voice.lang.indexOf('ko_KR') >= 0) {
                        voiceProp.voice = voice;
                        voiceProp.pitch = 1;
                        voiceProp.rate = 1;
                        break;
                    }
                }
            }

            voiceProp.lang = 'ko-KR';
            voiceProp.onend = callback;

            window.speechSynthesis.speak(voiceProp);
        },

        /* js단 에러메시지를 db에 등록하기 위해 */
        underTaker(erMsg, erTitle) {
            const data = {
                erMsg: erMsg.toString().substring(0, 245),
                erTitle,
            };
            const url = "/api/error/errorSave";
            CommonUI.ajax(url, "PARAM", data);
        },
    };

    validation = {
        /* 날짜가 필수값인지 체크하지는 않고, 입력되었을 경우 올바른 8자리인지를 체크한다. */
        dateEightDigit(dateArray) { // 시간날 때 로직 개선 필요
            for(const date of dateArray) {
                const dLength = date.numString();
                if(dLength !== 0 && dLength !== 8) {
                    alertCaution("올바른 날짜 8자리를 입력해 주세요.", 1);
                    return true;
                }
            }
            return false;
        },
    };


    /*
    * 배열에 담긴 DOM ID들에 JqueryUI의 datepicker 를 적용한다.
    * 기본 데이터 포맷 = yy-mm-dd
    * */
    setDatePicker(targetIdArray, dateFormat = "yy-mm-dd") {
        for (const targetId of targetIdArray) {
            const $target = $("#"+targetId);
            $target.datepicker({
                dateFormat,
                changeMonth: true,
                changeYear: true,
            });

            $target.on("keyup", function () {
                const numValue = $target.val().numString().substring(0, 8);
                let formattedValue = "";
                if(numValue.length > 6) {
                    formattedValue = numValue.substring(0, 4) + "-" + numValue.substring(4, 6)
                        + "-" + numValue.substring(6, 8);
                } else if(numValue.length > 4) {
                    formattedValue = numValue.substring(0, 4) + "-" + numValue.substring(4, 6);
                } else {
                    formattedValue = numValue;
                }
                $target.val(formattedValue);
            });
        }
    }

    /*
    * datepicker A에서 B까지의 기간을 설정할 때 서로간의 입력 날자를 제한한다.
    * targetIdArray = [ ['from대상id1', 'to대상id2'], ['from대상id1', 'to대상id2'], .... ]
    * */
    restrictDateAToB(targetIdArray) {
        for(const targetFromAndTo of targetIdArray) {
            for (let i = 0; i < targetFromAndTo.length; i++) {
                $("#" + targetFromAndTo[i]).on("change", function () {
                    CommonUI.restrictDate(targetFromAndTo[0], targetFromAndTo[1], i);
                });
                this.restrictDate(targetFromAndTo[0], targetFromAndTo[1], i);
            }
        }
    }

    /*
    * restrictDateAToB 와 연동되거나 단독 사용되는 함수.
    * ~부터의 Id, ~까지의  Id, 함수를 부르는 객체가 ~까지의 객체인지를 판단하여 지정해 준다.
    * Element에 걸린 datepicker 속성을 수정하여 날짜 최소범위, 최대범위를 지정한다.
    * */
    restrictDate(fromId, toId, isCallerTo) {
        if(isCallerTo) {
            $("#"+fromId).datepicker("option", "maxDate", $("#"+toId).val());
            $("#ui-datepicker-div").hide();
        }else{
            $("#"+toId).datepicker("option", "minDate", $("#"+fromId).val());
            $("#ui-datepicker-div").hide();
        }
    }

    /* 자주 쓸 정규표현형 유효성 검사를 편하게 쓰기 위함 */
    regularValidator(testValue, testMethod) {
        const email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
        /* 8자리 숫자를 통해 검사 */
        const dateExist = /^(?:(?:(?:(?:(?:[13579][26]|[2468][048])00)|(?:[0-9]{2}(?:(?:[13579][26])|(?:[2468][048]|0[48]))))(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:0[1-9]|1[0-9]|2[0-9]))))|(?:[0-9]{4}(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:[01][0-9]|2[0-8])))))$/;

        switch (testMethod) {
            case "email" : // 이메일 형식이 맞는지 검사.
                return email.test(testValue);
            case "dateExist" : // 존재하는 날짜인지 검사.
                return dateExist.test(testValue);
            default :
                break;
        }
        return '';
    }

    /* 국내 전화 번호 양식화, 적절한 위치에 - 추가, 최종 형태 잡을 때도, 입력할 때 마다 쓸 수도 있음 */
    formatTel(telNumber, privacyMode = false) {
        let formatNum = "";
        if(telNumber) {
            telNumber = telNumber.numString();
        } else {
            telNumber = "";
        }
        const telLength = telNumber.length;

        let foreNumType;
        const testForeCode = telNumber.substring(0, 3);
        if(testForeCode.substring(0, 2) === "02") {
            foreNumType = 2;
        } else if(parseInt(testForeCode, 10) > 129 && parseInt(testForeCode, 10) < 200) {
            foreNumType = 4;
        } else if(testForeCode === "014") {
            foreNumType = 5;
        } else {
            foreNumType = 3;
        }

        let midNum;
        let backNum;

        const foreNum = telNumber.substring(0, foreNumType);
        if(telLength < 8 + foreNumType && foreNumType !== 4) {
            if(privacyMode) {
                midNum = "***";
            } else {
                midNum = telNumber.substring(foreNumType, foreNumType + 3);
            }
            backNum = telNumber.substring(foreNumType + 3, telLength);
        } else {
            if(privacyMode) {
                midNum = "****";
            } else {
                midNum = telNumber.substring(foreNumType, foreNumType + 4);
            }
            backNum = telNumber.substring(foreNumType + 4, telLength);
        }

        if(backNum) {
            formatNum = foreNum + "-" + midNum + "-" + backNum;
        } else if (midNum) {
            formatNum = foreNum + "-" + midNum;
        } else if (foreNum) {
            formatNum = foreNum;
        } else {
            formatNum = telNumber;
        }

        return formatNum;
    }

    /* 사업자 번호 양식화, 숫자 10자리, 적절한 위치에 - 추가, 최종 형태 잡을 때도, 입력할 때 마다 쓸 수도 있음 */
    formatBusinessNo(businessNum) {
        let formatNum = "";
        businessNum = businessNum.numString();

        const foreNum = businessNum.substring(0, 3);
        const midNum = businessNum.substring(3, 5);
        const backNum = businessNum.substring(5, 10);

        if (backNum) {
            formatNum = foreNum + "-" + midNum + "-" + backNum;
        } else if (midNum) {
            formatNum = foreNum + "-" + midNum;
        } else if (foreNum) {
            formatNum = foreNum;
        }

        return formatNum;
    }

    /* ajax 통신의 자주 쓰는 패턴을 간단하게 쓰기 위함
    * (apiUrl, 통신방식(혹은 컨트롤러에서 받는 방식), 보낼데이터, 성공시 콜백, 실패시 콜백)
    * */
    ajax(url, method, data, successFn = function () {/* 빈기능 */}, errorFn = function () {/* 빈기능 */}
         , netFailFn = function () {/* 빈기능 */}) {
        window.broscreener.on("서버와 통신중 입니다.");
        if(data) {
            switch (method) {
                case "GET" :
                    $(document).ajaxSend(function (_e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url,
                        data,
                        type: 'GET',
                        cache: false,
                        traditional: true,
                        error: errorResponse,
                        success: successResponse,
                    });
                    break;

                case "POST" :
                case "PUT" :
                case "DELETE" :
                    $(document).ajaxSend(function (_e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url,
                        data,
                        type: method,
                        cache: false,
                        processData: false,
                        contentType: false,
                        enctype: 'multipart/form-data',
                        error: errorResponse,
                        success: successResponse,
                    });
                    break;

                case "MAPPER" :
                    $(document).ajaxSend(function (_e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url,
                        type: "POST",
                        cache: false,
                        data: JSON.stringify(data),
                        contentType: "application/json; charset=utf-8",
                        error: errorResponse,
                        success: successResponse,
                    });
                    break;

                case "PARAM" :
                    $(document).ajaxSend(function (_e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url,
                        data,
                        type : 'post',
                        cache:false,
                        traditional: true,
                        error: errorResponse,
                        success: successResponse,
                    });
                    break;
            }
        }else if(method === "GET") {
            $(document).ajaxSend(function (_e, xhr) {
                xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
            });
            $.ajax({
                url,
                method,
                cache: false,
                error: errorResponse,
                success: successResponse,
            });
        }

        function successResponse(res) {
            if (res.status === 200) {
                return successFn(res);
            } else {
                if (res.err_msg2 === null && res.err_msg) {
                    alertCancel(res.err_msg);
                } else {
                    alertCancel(res.err_msg + "<br>" + res.err_msg2);
                }
                CommonUI.toppos.underTaker("status : " + res.status + " || msg : "
                    + res.err_msg + res.err_msg2, "통신성공 에러코드");
                return errorFn(res);
            }
        }

        function errorResponse(res) {
            if(CommonUI.commsErrMsg) {
                CommonUI.toppos.underTaker(res.responseJSON.path + " |||| " + res.status + " |||| "
                    + res.responseJSON.message, "통신실패 에러");
                CommonUI.commsErrMsg = false;
            }
            ajaxErrorMsg(res);
            return netFailFn(res);
        }

        window.broscreener.off("서버와 통신중 입니다.");
    }



    /* 규칙 dtos와 같은 구조로 새로운 dto 생성하기 위함 (규칙 dto의 키값을 품은 빈 깡통 만들기) */
    newDto(dto) {
        const keys = Object.keys(dto);
        const babyDto = {};
        for(const key in keys) {
            babyDto[keys[key]] = null;
        }
        return babyDto;
    }

    cloneObj(dto) {
        const keys = Object.keys(dto);
        const babyDto = {};
        for(const key in keys) {
            babyDto[keys[key]] = dto[keys[key]];
        }
        return babyDto;
    }

    setKrDatepicker() {
        $.datepicker.regional.kr = {
            closeText: "닫기",
            prevText: "이전달",
            nextText: "다음달",
            currentText: "오늘",
            monthNames: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
            monthNamesShort: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
            dayNames: [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
            dayNamesShort: [ "일", "월", "화", "수", "목", "금", "토" ],
            dayNamesMin: [ "일", "월", "화", "수", "목", "금", "토" ],
            weekHeader: "주",
            dateFormat: "yy-mm-dd",
            firstDay: 0,
            isRTL: false,
            showMonthAfterYear: true,
            yearSuffix: "년",
            selectMonthLabel: "월선택",
            selectYearLabel: "년선택"
        };
        $.datepicker.setDefaults($.datepicker.regional.kr);
    }
}

window.CommonUI = new CommonUIClass();


