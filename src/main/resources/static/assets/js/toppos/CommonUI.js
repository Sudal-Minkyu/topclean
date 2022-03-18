/*
* 자주 쓰이는 UI용 함수를 모듈화
* CommonUI.기능 으로 호출하여 사용
*
* 제작 : 성낙원
* 수정 : 2022-01-27
* */
class CommonUIClass {

    constructor() {
        /* 숫자만 남긴 후 인트형으로 전환 */
        String.prototype.toInt = function () {
            return this.toString() ? parseInt(this.replace(/[^0-9]/g, "")) : 0;
        }

        /* 0~9까지만 남긴 문자를 반환, 앞자리가 0으로 시작할 수 있음. */
        String.prototype.numString = function () {
            return this.toString() ? this.replace(/[^0-9]/g, "") : "";
        }
    }

    toppos = {
        /* 아이템 코드와 이름 데이터 조합에 쓰이는 배열을 사용하여 최종 이름을 산출 */
        makeProductName(item, nameArray) {
            if(!item.sumName) {
                const isNotSizeNormal = !(item.biItemcode.substr(3, 1) === "N");
                let sumName = "";
                for(let i = 0; i < nameArray.length; i++) {
                    if(nameArray[i].biItemcode === item.biItemcode) {
                        if(isNotSizeNormal) {
                            sumName += nameArray[i].bsName + " ";
                        }
                        sumName += nameArray[i].biName + " " + nameArray[i].bgName;
                        break;
                    }
                }
                item.sumName = sumName;
            }
        },

        /* 대중소분류 이름을 가져다가 최종 이름을 조합하는 방식 */
        makeSimpleProductName(item) {
            const finalBsName = item.bsName === "일반" ? "" : item.bsName;
            return finalBsName + " " + item.biName + " " + item.bgName;
        },

        /* 처리내역의 표시를 결정하는 공통함수 */
        processName(item) {
            try {
                let statusText = "";
                statusText += item.fdUrgentYn === "Y" ? "급" : ""; 
                statusText += item.fdPriceGrade === "3" ? "명" : "";
                statusText += item.fdRetryYn === "Y" ? "재" : "";
                statusText += item.fdPressed ? "다" : "";
                statusText += item.fdAdd1Amt || item.fdAdd1Remark.length ? "추" : "";
                statusText += item.fdRepairAmt || item.fdRepairRemark.length ? "수" : "";
                statusText += item.fdWhitening ? "표" : "";
                statusText += item.fdPollutionLevel ? "오" : "";
                statusText += item.fdWaterRepellent || item.fdStarch ? "발" : "";
                return statusText;
            } catch (e) {
                this.toppos.underTaker(e, "CommonUI : 처리내역 문자조합");
            }
        },

        killNullFromArray(array) {
            let resultArray = [];
            for(const obj of array) {
                if(obj !== null) resultArray.push(obj);
            }
            return resultArray;
        },

        printReceipt(condition) {
            try {
                const url = "/api/user/requestPaymentPaper";
                
                CommonUI.ajax(url, "GET", condition, function (res) {
                    console.log(res.sendData);

                    const typeTrans = {
                        "01": "cash",
                        "02": "card",
                        "03": "save",
                        "04": "미수결제",
                    };

                    const colorName = { // 컬러코드에 따른 실제 색상
                        "00": "없음", "01": "흰색", "02": "검정", "03": "회색", "04": "빨강", "05": "주황",
                        "06": "노랑", "07": "초록", "08": "파랑", "09": "남색", "10": "보라", "11": "핑크",
                    };

                    const paymentData = res.sendData.paymentData;
                    paymentData.items = res.sendData.items;
                    paymentData.items.forEach(obj => {
                        obj.color = colorName[obj.color];
                    });

                    paymentData.estimateDt = paymentData.items[0].estimateDt;
                    paymentData.franchiseTel = CommonUI.formatTel(paymentData.franchiseTel);
                    paymentData.customerTel = CommonUI.formatTel(paymentData.customerTel);
                    paymentData.businessNO = CommonUI.formatBusinessNo(paymentData.businessNO);

                    const creditData = res.sendData.creditData;
                    creditData.forEach(obj => {
                        obj.type = typeTrans[obj.type];
                    });

                    CAT.CatPrint_Multi(paymentData, creditData, "N");
                });
            } catch (e) {
                this.toppos.underTaker(e, "CommonUI : 영수증 프린트");
            }
        },

        speak(text) {
            if(typeof speechSynthesis === 'undefined') {
                return;
            }
            
            const voices = speechSynthesis.getVoices();

            const voiceProp = new SpeechSynthesisUtterance(text);

            let recommendNotExist = true;
            for(var i = 0; i < voices.length ; i++) {
                if(voices[i].name === "Google 한국의") {
                    voiceProp.voice = voices[i];
                    voiceProp.pitch = 1;
                    voiceProp.rate = 1.1;
                    recommendNotExist = false;
                    break;
                }
            }

            if(recommendNotExist) {
                for(var i = 0; i < voices.length ; i++) {
                    if(voices[i].lang.indexOf('ko-KR') >= 0 || voices[i].lang.indexOf('ko_KR') >= 0) {
                        voiceProp.voice = voices[i];
                        voiceProp.pitch = 1;
                        voiceProp.rate = 1;
                        break;
                    }
                }
            }

            voiceProp.lang = 'ko-KR';
            window.speechSynthesis.speak(voiceProp);
        },

        /* js단 에러메시지를 db에 등록하기 위해 */
        underTaker(erMsg, erTitle) {
            const data = {
                erMsg: erMsg,
                erTitle: erTitle,
            };
            const url = "/api/";
            // this.ajax(url, "PARAM", data);
        }
    }

    /*
    * 배열에 담긴 DOM ID들에 JqueryUI의 datepicker 를 적용한다.
    * 기본 데이터 포맷 = yy-mm-dd
    * */
    setDatePicker(targetIdArray, dateFormat = "yy-mm-dd") {
        for (const targetId of targetIdArray) {
            $("#"+targetId).datepicker({
                dateFormat: dateFormat,
            });
        }
    }

    /*
    * datepicker A에서 B까지의 기간을 설정할 때 서로간의 입력 날자를 제한한다.
    * targetIdArray = [ ['from대상id1', 'to대상id2'], ['from대상id1', 'to대상id2'], .... ]
    * */
    restrictDateAToB(targetIdArray) {
        for(let i = 0; i < targetIdArray.length; i++) {
            for(let j = 0; j < targetIdArray[i].length; j++) {
                $("#"+targetIdArray[i][j]).on("change", function() {
                    CommonUI.restrictDate(targetIdArray[i][0], targetIdArray[i][1], j);
                });
                this.restrictDate(targetIdArray[i][0], targetIdArray[i][1], j);
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
        const dateExist = /^(?:(?:(?:(?:(?:[13579][26]|[2468][048])00)|(?:[0-9]{2}(?:(?:[13579][26])|(?:[2468][048]|0[48]))))(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:0[1-9]|1[0-9]|2[0-9]))))|(?:[0-9]{4}(?:(?:(?:09|04|06|11)(?:0[1-9]|1[0-9]|2[0-9]|30))|(?:(?:01|03|05|07|08|10|12)(?:0[1-9]|1[0-9]|2[0-9]|3[01]))|(?:02(?:[01][0-9]|2[0-8])))))$/

        switch (testMethod) {
            case "email" : // 이메일 형식이 맞는지 검사.
                return email.test(testValue);
                break;
            case "dateExist" : // 존재하는 날짜인지 검사.
                return dateExist.test(testValue);
                break;
            default :
                break;
        }
    }

    /* 국내 전화 번호 양식화, 적절한 위치에 - 추가, 최종 형태 잡을 때도, 입력할 때 마다 쓸 수도 있음 */
    formatTel(telNumber) {
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
        } else if(parseInt(testForeCode) > 129 && parseInt(testForeCode) < 200) {
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
            midNum = telNumber.substring(foreNumType, foreNumType + 3);
            backNum = telNumber.substring(foreNumType + 3, telLength);
        } else {
            midNum = telNumber.substring(foreNumType, foreNumType + 4);
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
    ajax(url, method, data, successFn = function () {}, errorFn = function () {}) {

        if(data) {
            switch (method) {
                case "GET" :
                    $(document).ajaxSend(function (e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url: url,
                        type: 'GET',
                        cache: false,
                        traditional: true,
                        data: data,
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            if (req.status === 200) {
                                return successFn(req);
                            }else {
                                if (req.err_msg2 === null && req.err_msg) {
                                    alertCancel(req.err_msg);
                                } else {
                                    alertCancel(req.err_msg + "<br>" + req.err_msg2);
                                }
                                return errorFn(req);
                            }
                        }
                    });
                    break;

                case "POST" :
                case "PUT" :
                case "DELETE" :
                    $(document).ajaxSend(function (e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url: url,
                        type: method,
                        cache: false,
                        data: data,
                        processData: false,
                        contentType: false,
                        enctype: 'multipart/form-data',
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            if (req.status === 200) {
                                return successFn(req);
                            }else {
                                if (req.err_msg2 === null && req.err_msg) {
                                    alertCancel(req.err_msg);
                                } else {
                                    alertCancel(req.err_msg + "<br>" + req.err_msg2);
                                }
                                return errorFn(req);
                            }
                        }
                    });
                    break;

                case "MAPPER" :
                    $(document).ajaxSend(function (e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url: url,
                        type: "POST",
                        cache: false,
                        data: JSON.stringify(data),
                        contentType: "application/json; charset=utf-8",
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            if (req.status === 200) {
                                return successFn(req);
                            } else {
                                if (req.err_msg2 === null && req.err_msg) {
                                    alertCancel(req.err_msg);
                                } else {
                                    alertCancel(req.err_msg + "<br>" + req.err_msg2);
                                }
                                return errorFn(req);
                            }
                        }
                    });
                    break;

                case "PARAM" :
                    $(document).ajaxSend(function (e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url: url,
                        data : data,
                        type : 'post',
                        cache:false,
                        traditional: true,
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            if (req.status === 200) {
                                return successFn(req);
                            } else {
                                if (req.err_msg2 === null && req.err_msg) {
                                    alertCancel(req.err_msg);
                                } else {
                                    alertCancel(req.err_msg + "<br>" + req.err_msg2);
                                }
                                return errorFn(req);
                            }
                        }
                    });
                    break;
            }
        }else{
            switch (method) {
                case "GET" :
                    $(document).ajaxSend(function (e, xhr) {
                        xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
                    });
                    $.ajax({
                        url: url,
                        type: method,
                        cache: false,
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            return successFn(req);
                        }
                    });
                    break;
            }
        }
    }

    /* 규칙 dtos와 같은 구조로 새로운 dto 생성하기 위함 (규칙 dto의 키값을 품은 빈 깡통 만들기) */
    newDto(dto) {
        const keys = Object.keys(dto);
        let babyDto = {};
        for(let key in keys) {
            babyDto[keys[key]] = null;
        }
        return babyDto;
    }

    cloneObj(dto) {
        const keys = Object.keys(dto);
        let babyDto = {};
        for(let key in keys) {
            babyDto[keys[key]] = dto[keys[key]];
        }
        return babyDto;
    }
}

let CommonUI = new CommonUIClass();