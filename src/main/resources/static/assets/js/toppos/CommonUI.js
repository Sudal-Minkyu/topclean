/*
* 자주 쓰이는 UI용 함수를 모듈화
* CommonUI.기능 으로 호출하여 사용
*
* 제작 : 성낙원
* 수정 : 2021-11-18
* */
class CommonUIClass {

    mouseDownTarget;
    mouseDownPos = [0, 0];
    mouseUpPos = [0, 0];
    activeDragDropEvent = false;
    abc = 777;

    constructor() {
        String.prototype.toInt = function () {
            return this.toString() ? parseInt(this.replace(/[^0-9]/g, "")) : 0;
        }
        String.prototype.numString = function () {
            return this.toString() ? this.replace(/[^0-9]/g, "") : "";
        }
    }

    toppos = {
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
        makeSimpleProductName(item) {
            const finalBsName = item.bsName === "일반" ? "" : item.bsName;
            return finalBsName + " " + item.biName + " " + item.bgName;
        },
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
                console.log(e);
            }
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
            case "email" :
                return email.test(testValue);
                break;
            case "dateExist" :
                return dateExist.test(testValue);
                break;
            default :
                break;
        }
    }

    formatTel(telNumber) {
        let formatNum = "";
        telNumber = telNumber.numString();
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

    /* ajax 통신의 자주 쓰는 패턴을 간단하게 쓰기 위함 */
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

    /* 새로운 dto 생성용 */
    newDto(dto) {
        const keys = Object.keys(dto);
        let babyDto = {};
        for(let key in keys) {
            babyDto[keys[key]] = null;
        }
        return babyDto;
    }
}

let CommonUI = new CommonUIClass();