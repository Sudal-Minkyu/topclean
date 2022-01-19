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
            return this.toString() ? parseInt(this.replace(/[^0-9-]/g, "")) : 0;
        }
        String.prototype.zeroToNine = function () {
            return this.toString() ? this.replace(/[^0-9-]/g, "") : "";
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

    onPhoneNumChange(phoneNumber) {
        let formatNum = "";
        if(phoneNumber) {
            phoneNumber = phoneNumber.replace(/[^0-9]/g, "");
            if (phoneNumber.length == 11) {
                formatNum = phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
            } else if (phoneNumber.length == 8) {
                formatNum = phoneNumber.replace(/(\d{4})(\d{4})/, '$1-$2');
            } else {
                if (phoneNumber.indexOf('02') == 0) {
                    formatNum = phoneNumber.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
                } else {
                    formatNum = phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
                }
            }
        }
        return formatNum;
    }

    /* ajax 통신의 자주 쓰는 패턴을 간단하게 쓰기 위함 */
    ajax(url, method, data, func) {

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
                        data: data,
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            if (req.status === 200) {
                                return func(req);
                            }else {
                                if (req.err_msg2 === null) {
                                    alertCaution(req.err_msg, 1);
                                } else {
                                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                                }
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
                                return func(req);
                            }else {
                                if (req.err_msg2 === null) {
                                    alertCaution(req.err_msg, 1);
                                } else {
                                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                                }
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
                                return func(req);
                            } else {
                                if (req.err_msg2 === null) {
                                    alertCaution(req.err_msg, 1);
                                } else {
                                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                                }
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
                        error: function (req) {
                            ajaxErrorMsg(req);
                        },
                        success: function (req) {
                            if (req.status === 200) {
                                return func(req);
                            } else {
                                if (req.err_msg2 === null) {
                                    alertCaution(req.err_msg, 1);
                                } else {
                                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                                }
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
                            return func(req);
                        }
                    });
                    break;
            }
        }
    }

    /* json 보내는 통신의 자주 쓰는 패턴을 간단하게 쓰기 위함 --임시 */
    ajaxjsonPost(url, data, func) {
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
        });
        $.ajax({
            url: url,
            data : data,
            type : 'post',
            cache:false,
            error: function (req) {
                ajaxErrorMsg(req);
            },
            success: function (req) {
                if (req.status === 200) {
                    return func(req);
                } else {
                    if (req.err_msg2 === null) {
                        alertCaution(req.err_msg, 1);
                    } else {
                        alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                    }
                }
            }
        });
    }

    /* json 보내는 통신의 자주 쓰는 패턴을 간단하게 쓰기 위함 --임시 */
    ajaxjson(url, data, func) {
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader("Authorization", localStorage.getItem('Authorization'));
        });
        $.ajax({
            url: url,
            type: "POST",
            cache: false,
            data: data,
            contentType: "application/json; charset=utf-8",
            error: function (req) {
                ajaxErrorMsg(req);
            },
            success: function (req) {
                if (req.status === 200) {
                    return func(req);
                } else {
                    if (req.err_msg2 === null) {
                        alertCaution(req.err_msg, 1);
                    } else {
                        alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                    }
                }
            }
        });
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

    /* 드래그 이벤트, 터치시 좌표얻는 알고리즘 포함. 아직 사용 가능성이 적으므로 이벤트로 사용을 위해서는 터치시 마우스다운과 다르게 AUIGrid의 셀을
    *  선택하지 않는 문제의 해결이 필요하다. 또한 이벤트 사용을 위해서는 AUI에는 셀 클릭 이벤트를 걸고, activeDragDropEvent 불린값을 조건으로
    *  주어 이벤트를 구동해야 한다. */
    gridDragDropDblclick() {
        $(document).on("touchstart mousedown", function (e) {
            CommonUI.mouseDownTarget = e.target;

            if(e.type == 'touchstart'){
                const touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                CommonUI.mouseDownPos = [touch.pageX, touch.pageY];
            } else if (e.type == 'mousedown') {
                CommonUI.mouseDownPos = [e.originalEvent.clientX, e.originalEvent.clientY];
            }
        });
        $(document).on("touchend mouseup", function (e) {
            if(e.type == 'touchend'){
                const touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                CommonUI.mouseUpPos = [touch.pageX, touch.pageY];
            } else if (e.type == 'mouseup') {
                CommonUI.mouseUpPos = [e.originalEvent.clientX, e.originalEvent.clientY];
            }
            const moveDistance = Math.sqrt(Math.pow(Math.abs(CommonUI.mouseDownPos[0] - CommonUI.mouseUpPos[0]), 2) +
                Math.pow(Math.abs(CommonUI.mouseDownPos[1] - CommonUI.mouseUpPos[1]), 2));
            if(moveDistance > 100) {
                const targetClassName = CommonUI.mouseDownTarget.className;
                if(targetClassName.includes("aui-grid-renderer-base") ||
                        targetClassName.includes("aui-grid-default-column")) {
                    CommonUI.activeDragDropEvent = true;
                    $(CommonUI.mouseDownTarget).trigger("click");
                }
            }
            CommonUI.activeDragDropEvent = false;
        });
    }
}

let CommonUI = new CommonUIClass();