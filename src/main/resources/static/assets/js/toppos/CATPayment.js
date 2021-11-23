const CAT_URL = "http://127.0.0.1:27098";
const CAT_TIMEOUT_DURATION = 15000

class CATPayment {

    constructor() {

    }

    CatCredit(params, func) {


        let reqmsg = CatCredit_vPOSV1(params);
        //return func(req);
        try {
            console.log("start=======")

            $.ajax
            ({
                url: CAT_URL,
                type: "POST",
                dataType: "jsonp",
                jsonp: "callback",
                timeout: CAT_TIMEOUT_DURATION,
                data: {"REQ": reqmsg},
                success: function (data) {
                    console.log("call==========1")
                    let returnmsg = "";
                    var trans = reqmsg.substr(0, 2);

                    if (FindJSONtoString("RES", data) !== "0000") {
                        //returnmsg= FindJSONtoString("MSG", data);
                    } else {
                        //document.getElementById("txtMsg").value = "";
                    }

                    if (trans === "CC") {
                        // 정상 처리인 경우에만 Fair가 맞으면서..전문구분 코드가 NE가 아닌 경우.. (a1으로 전송 시 a1으로 응답)
                        if (FindJSONtoString("REQ", data) === "CC" && FindJSONtoString("RES", data) === "0000" && FindJSONtoString("TELEGRAMFLAG", data) !== "NE") {
                            //document.getElementById("txtCatApprDate").value = FindJSONtoString("APPROVALTIME", data).substr(0, 6);	// 승인일시
                            //document.getElementById("txtCatApprNo").value = FindJSONtoString("APPROVALNO", data);
                        }
                    }
                    return func(JSONtoString(data));
                    //document.getElementById("taResponse").innerText = JSONtoString(data);
                },
                error: function (request, status, error) {
                    alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                    console.log("에러1==========1" + error)
                    let errMessage = "code:" + request.status + "\n" + "error:" + error;
                    return func(errMessage);
                }


            });
        } catch (e) {
            console.log("error==========")
            return func("결제 실패 잠시후 다시 시도 하세요");
        }


    }

    CatPrint(params) {

        let message="";
        message = CatCreate_Print(params)

        CatPrintCommunication(message);

    }
}
// 프린트 내용 생성
function CatCreate_Print(params){
    var message = "";

    //message += String.fromCharCode(10);
    //message += String.fromCharCode(10);
    //message += String.fromCharCode(10);

    //**************************************************
    // 초기화: ESC @
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(64);
    //**************************************************
    // 정렬: ESC a (0: 왼쪽, 1: 가운데, 2: 오른쪽 )
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(97);
    message += String.fromCharCode(1);		// LOGO 출력을 위해 가운데 정렬
    message += "%Logo%";

    //**************************************************
    // 정렬: ESC a (0: 왼쪽, 1: 가운데, 2: 오른쪽 )
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(97);
    message += String.fromCharCode(1);      // LOGO 출력 이후에 정렬 초기화 됨
    //**************************************************
    // Double-width: ESC !
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(32);
    //message += String.fromCharCode(48);	// 가로 세로 확대 2배
    //**************************************************
    // 문자열 출력
    //**************************************************
    message += "현금( 소득공제 )";
    message += String.fromCharCode(10);
    //**************************************************
    // 정렬: ESC a (0: 왼쪽, 1: 가운데, 2: 오른쪽 )
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(97);
    message += String.fromCharCode(0);
    //**************************************************
    // Double-width : 해제 ESC !
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    //**************************************************
    // 문자열 출력
    //**************************************************
    message += "(주)청산리 가산점";
    message += String.fromCharCode(10);
    message += "대표 : 홍길동 211-11-56117  T:02-2011-0111";
    message += String.fromCharCode(10);
    message += "구매   2014-03-20 13:20   거래번호:01-2047";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "     상품명             수량          금액";
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += "베이스팬츠#1            1EA         85,000";
    message += String.fromCharCode(10);
    message += "                쿠폰상품            -5,000";
    message += String.fromCharCode(10);
    message += "1BYPNF304-46-101                          ";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    //**************************************************
    // Double-height: ESC !
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(16);
    message += "합    계                            80,000";
    message += String.fromCharCode(10);
    //**************************************************
    // Double-height: 해제 ESC !
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += "공급가액                            72,727";
    message += String.fromCharCode(10);
    message += "부가세액                             7,273";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "현      금                           5,000";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "                                   2017/12";
    message += String.fromCharCode(10);
    message += "카드결제액                          45,000";
    message += String.fromCharCode(10);
    message += "삼성                      5411********0066";
    message += String.fromCharCode(10);
    message += "승인번호             testdata       일시불";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "                                   2015/02";
    message += String.fromCharCode(10);
    message += "카드결제액                          30,000";
    message += String.fromCharCode(10);
    message += "신한                      3731********0066";
    message += String.fromCharCode(10);
    message += "승인번호             testdata       일시불";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "홍길동 고객님 감사합니다 ^^     (11740011)";
    message += String.fromCharCode(10);
    message += "적립포인트                           4,000";
    message += String.fromCharCode(10);
    message += "잔여포인트                          30,000";
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "현금영수증 승인번호 :            170052811";
    message += String.fromCharCode(10);
    message += "식별번호 :                   010-****-1234";
    message += String.fromCharCode(10);
    message += "문의:국번없이 126     http://현금영수증.kr";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += "구입일로부터 7일 이내에 본 영수증을       ";
    message += String.fromCharCode(10);
    message += "지참하셔야 교환 및 환불 가능합니다.       ";
    message += String.fromCharCode(10);
    message += "[서   명]                                 ";
    message += String.fromCharCode(10);
    message += "%Sign%                                    ";
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    //**************************************************
    // 바코드 출력
    //**************************************************
    // GS H - 바코드 숫자가 출력될 위치 (0: 출력 안함, 1: 상단, 2: 하단, 3: 상하단)
    message += String.fromCharCode(29);
    message += String.fromCharCode(72);
    message += String.fromCharCode(2);
    // GS h - 바코드 높이 설정 (1~255)
    message += String.fromCharCode(29);
    message += String.fromCharCode(104);
    message += String.fromCharCode(64);
    // GS w - 바코드 수평선 굵기 (1~6)
    message += String.fromCharCode(29);
    message += String.fromCharCode(119);
    message += String.fromCharCode(2);
    // GS k - 바코드 출력 (ex. CODE39 - 숫자, 영문 대문자, 공백, $, %, +, -, ., /만 가능)
    message += String.fromCharCode(29);
    message += String.fromCharCode(107);
    message += String.fromCharCode(4);
    // 바코드 DATA
    message += "14051410296020058";
    message += String.fromCharCode(0);
    message += String.fromCharCode(0);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    //**************************************************
    // 용지 절단 - GS V NUL
    //**************************************************

    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);

    message += String.fromCharCode(27);
    message += String.fromCharCode(105);


    var len = 0;
    for (var i = 0; i < message.length; i++) {
        len += (message.charCodeAt(i) > 128) ? 2 : 1;
    }

    var messageLen = ("" + len).fillZero(4);   // 길이

    message = "CATPRINT" + messageLen + message;	// 길이 추가

    len += 12;	// CATPRINT 8Byte + Length 4Byte 추가

    return "CC" + message;
}
//전문생성
function CatCredit_vPOSV1(data) {
    //전문생성
    var request_msg = "";

    // 전문길이 마지막에 입력
    request_msg += "a1";                                       // 전문구분코드
    request_msg += "00";                                       // 할부개월
    request_msg += " ";                                     	 // 현금영수증 소비자 구분
    request_msg += " ";                                        // 현금영수증 취소 사유
    request_msg += "000001004";                                // 총금액
    request_msg += "000000091";                                // 세금
    request_msg += "000000000";                             	 // 면세금액
    request_msg += "000000000";                                // 봉사료
    request_msg += "      ";                		 							 // 원거래 일시
    request_msg += "         ";                     				 // 원거래 승인번호
    request_msg += "0001";                                     // 거래일련번호
    request_msg += String.fromCharCode(3);                    // ETX

    var telegramLen = ("" + request_msg.length).fillZero(4);   // 길이

    request_msg = String.fromCharCode(2) + telegramLen + request_msg;	// STX 추가 + 전문 길이 + 전송 전문

    return "CC" + request_msg;
}


//프린터 출력
function CatPrintCommunication(printData) {
    $.ajax
    ({
        url: CAT_URL,
        type: "POST",
        dataType: "jsonp",
        jsonp: "callback",
        data: {"REQ": printData},
        success: function (data) {

            // var trans = document.getElementById("taRequest").value.substr(0, 2);
            //
            // if (FindJSONtoString("RES", data) !== "0000") {
            //     document.getElementById("txtMsg").value = FindJSONtoString("MSG", data);
            // } else {
            //     document.getElementById("txtMsg").value = "";
            // }
            //
            // document.getElementById("taResponse").innerText = JSONtoString(data);
        }
    });
}

const CAT = new CATPayment();


// 하단은 전문생성및 문자열가공에 필요한 함수
String.prototype.fillZero = function (n) {
    var str = this;
    var zeros = "";


    if (str.length < n) {
        for (i = 0; i < n - str.length; i++) {
            zeros += '0';
        }
    }

    return zeros + str;
}

String.prototype.fillSpace = function (n) {
    var str = this;
    var space = "";

    if (str.length < n) {
        for (i = 0; i < n - str.length; i++) {
            space += ' ';
        }
    }

    return str + space;
}

String.prototype.byteLength = function () {
    var len = 0;

    for (var i = 0; i < this.length; i++) {
        len += (this.charCodeAt(i) > 127) ? 2 : 1;
    }

    return len;
}

String.prototype.substrKor = function (idx, len) {
    if (!this.valueOf()) return "";

    var str = this;
    var pos = 0;

    for (var i = 0; pos < idx; i++) {
        pos += (str.charCodeAt(i) > 127) ? 2 : 1;
    }

    var beg = i;
    var byteLen = str.byteLength();
    var lim = 0;

    for (var i = beg; i < byteLen; i++) {
        lim += (str.charCodeAt(i) > 127) ? 2 : 1;

        if (lim > len) {
            str = str.substring(beg, i);
            break;
        }
    }

    return str;
}

function JSONtoString(object) {
    var results = [];
    for (var property in object) {
        var value = object[property];
        if (value)
            results.push(property.toString() + ': ' + value);
    }

    return '{' + results.join(', ') + '}';
}

function FindJSONtoString(Key, object) {
    var results = "";
    for (var property in object) {
        var value = object[property];
        if (value) {
            if (Key == property.toString()) {
                results = value;
                break;
            }
        }
    }

    return results;
}

stringformat = function (text) {
    if (arguments.length <= 1) return text;

    for (var i = 0; i <= arguments.length - 2; i++) {
        text = text.replace(new RegExp("\\{" + i + "\\}", "gi"), arguments[i + 1]);
    }

    return text;
}