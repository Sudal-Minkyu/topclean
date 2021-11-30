const CAT_URL = "http://127.0.0.1:27098";
const CAT_TIMEOUT_DURATION = 15000

class CATPayment {

    constructor() {


    }


    CatCredit(params, func) {

        let reqmsg = CatCredit_vPOSV1(params);
        Communication(reqmsg,func);

    }
    CatCreditCancel(params, func) {
        let reqmsg = CatCreditCancel_vPOSV1(params);
        Communication(reqmsg,func);

    }



    CatPrint(params,creditData,cancelYN) {

        let message="";
        message = CatCreate_Print(params,creditData,cancelYN)

        CatPrintCommunication(message);

    }
}
// 프린트 내용 생성
function CatCreate_Print(params,creditResult,cancelYN){
    let message = "";


    let type = params.type
    let month = "일시불";
    if ((params.month) > 1 ){
        month = params.month + "개월"
    }
    let cardNo = "" + creditResult.cardNo; // 카드번호
    let cardName = "" + creditResult.cardName; // 카드번호
    let approvalNo = ("" + creditResult.approvalNo).trim(); // 승인번호
    let tmpStr = "20" + creditResult.approvalTime;
    let approvalTime = tmpStr.substr(0,4) + "-" + tmpStr.substr(4,2) + "-" + tmpStr.substr(6,2)+ " " + tmpStr.substr(8,2) + ":" + tmpStr.substr(10,2); // 승인일시

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
    message += String.fromCharCode(48);
    //message += String.fromCharCode(48);	// 가로 세로 확대 4배 32 두배
    //**************************************************
    // 문자열 출력
    //**************************************************
    message += "접수증( 고객용 )";
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
    message += "==========================================";
    message += String.fromCharCode(10);
    message += "(주)크리닝업";
    message += String.fromCharCode(10);
    message += "전화번호 : 1899-6322";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += "접수일자 : 2021-11-25 13:20               ";
    message += String.fromCharCode(10);
    message += "출고예정 : 2021-11-27                     ";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += "  택번호             상품             금액";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);
    params.items.forEach((item,idx)=>{
        if (cancelYN==="Y"){
            message += item.tagno + " " + item.itemname.fillSpaceUnicode(25) + " " + numberWithCommasCat(-1 * item.price).padStart(7);
        }else {
            message += item.tagno + " " + item.itemname.fillSpaceUnicode(25) + " " + numberWithCommasCat(item.price).padStart(7);
        }
        message += String.fromCharCode(10);
        message += "------------------------------------------";
        message += String.fromCharCode(10);
    });



    //**************************************************
    // Double-height: ESC !
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(16);
    if (cancelYN==="Y"){
        message += "수   량 : " + ("" + params.items.length).padStart(3) + "         취소금액 :" + (" " + numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    }else {
        message += "수   량 : " + ("" + params.items.length).padStart(3) + "         접수금액 :" + (" " + numberWithCommasCat(params.totalAmount)).padStart(9);
    }
    message += String.fromCharCode(10);

    //**************************************************
    // Double-height: 해제 ESC !
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);


    if (type ==='card') {
        message += "==========================================";
        message += String.fromCharCode(10);
        message += "승인일시                  " + approvalTime.padStart(16);
        message += String.fromCharCode(10);
        if (cancelYN==="Y") {
            message += ("할부 : " + month).fillSpaceUnicode(14) + "    취소금액".fillSpaceUnicode(19) + (numberWithCommasCat(-1*params.totalAmount)).padStart(9);
        }else {
            message += ("할부 : " + month).fillSpaceUnicode(14) + "    승인금액".fillSpaceUnicode(19) + (numberWithCommasCat(params.totalAmount)).padStart(9);
        }
        message += String.fromCharCode(10);
        message += cardName.fillSpaceUnicode(24) +" " + cardNo.padStart(17);
        message += String.fromCharCode(10);
        message += "승인번호                       " + approvalNo.padStart(11);
        message += String.fromCharCode(10);
        message += "==========================================";
        message += String.fromCharCode(10);
    }


    // // message += String.fromCharCode(10);
    // // message += "홍길동 고객님 감사합니다 ^^     (11740011)";
    // // message += String.fromCharCode(10);
    // // message += "적립포인트                           4,000";
    // // message += String.fromCharCode(10);
    // // message += "잔여포인트                          30,000";
    // // message += String.fromCharCode(10);
    // // message += "------------------------------------------";
    // // message += String.fromCharCode(10);
    // // message += "현금영수증 승인번호 :            170052811";
    // // message += String.fromCharCode(10);
    // // message += "식별번호 :                   010-****-1234";
    // // message += String.fromCharCode(10);
    // // message += "문의:국번없이 126     http://현금영수증.kr";
    // // message += String.fromCharCode(10);
    // // message += "==========================================";
    // // message += String.fromCharCode(10);
    // // message += "구입일로부터 7일 이내에 본 영수증을       ";
    // // message += String.fromCharCode(10);
    // // message += "지참하셔야 교환 및 환불 가능합니다.       ";
    // message += String.fromCharCode(10);
    // message += "[서   명]                                 ";
    // message += String.fromCharCode(10);
    // message += "%Sign%                                    ";
    // message += String.fromCharCode(10);
    // message += String.fromCharCode(10);
    // message += String.fromCharCode(10);

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
//카드결제전문생성
function CatCredit_vPOSV1(params) {
    //전문생성
    let totalAmount = ("" + params.totalAmount).fillZero(9);
    let vatAmount = ("" + Math.floor(params.totalAmount / 11)).fillZero(9); //부가세 계산 내림으로 처리
    let month = ("" + params.month).fillZero(2);
    let franchiseNo = ("" + params.franchiseNo).fillZero(4);


    let request_msg = "";
    // 전문길이 마지막에 입력
    request_msg += "a1";                                       // 전문구분코드
    request_msg += month;                                       // 할부개월 2
    request_msg += " ";                                     	 // 현금영수증 소비자 구분
    request_msg += " ";                                        // 현금영수증 취소 사유
    request_msg += totalAmount;              // "000001004";    // 총금액
    request_msg += vatAmount;              //"000000091";     // 세금
    request_msg += "000000000";                             	 // 면세금액
    request_msg += "000000000";                                // 봉사료
    request_msg += "      ";                		 							 // 원거래 일시
    request_msg += "         ";                     				 // 원거래 승인번호
    request_msg += franchiseNo;             //"0001";               // 거래일련번호
    request_msg += String.fromCharCode(3);                    // ETX

    var telegramLen = ("" + request_msg.length).fillZero(4);   // 길이

    request_msg = String.fromCharCode(2) + telegramLen + request_msg;	// STX 추가 + 전문 길이 + 전송 전문

    return "CC" + request_msg;
}
//카드취소
function CatCreditCancel_vPOSV1(params) {
    let request_msg = "";
    let approvalTime = ("" + params.approvalTime).fillSpace(6).substr(0, 6);
    let approvalNo = ("" + params.approvalNo).fillSpace(9).substr(0, 9);
    let totalAmount = ("" + params.totalAmount).fillZero(9);
    let vatAmount = ("" + params.vatAmount).fillZero(9);
    let franchiseNo = ("" + params.franchiseNo).fillZero(4);

    // 전문길이 마지막에 입력
    request_msg += "b1";                                       // 전문구분코드
    request_msg += "00";                                       // 할부개월
    request_msg += " ";                                        // 현금영수증 소비자 구분
    request_msg += " ";                                        // 현금영수증 취소 사유
    request_msg += totalAmount;              //"000003000";                                // 총금액
    request_msg += vatAmount;               //"000000272";                                // 세금
    request_msg += "000000000";                                // 면세금액
    request_msg += "000000000";                                // 봉사료
    request_msg += approvalTime;            //("211124").fillSpace(6);    // 원거래 일시
    request_msg += approvalNo;           // ("74684254").fillSpace(9).substr(0, 9);
    request_msg += franchiseNo;             //"0002";                                     // 거래일련번호
    request_msg += String.fromCharCode(3);                    // ETX

    var telegramLen = ("" + request_msg.length).fillZero(4);   // 길이

    request_msg = String.fromCharCode(2) + telegramLen + request_msg;	// STX 추가 + 전문 길이 + 전송 전문

    return "CC" + request_msg;
}

//Ajax 호출
function Communication(reqmsg, func){
    let errorDefaultMessage ="단말기 처리 중 에러가 발생하였습니다. 다시 시도해주세요"
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
                let resultData = '{"STATUS":"FAILURE","ERRORMESSAGE":"'+ errorDefaultMessage +'"}';
                var trans = reqmsg.substr(0, 2);

                if (FindJSONtoString("RES", data) !== "0000") {
                    //returnmsg= FindJSONtoString("MSG", data);
                } else {
                    //document.getElementById("txtMsg").value = "";
                }

                console.log('========debug1=:' + trans);
                if (trans === "CC") {
                    // 정상 처리인 경우에만 Fair가 맞으면서..전문구분 코드가 NE가 아닌 경우.. (a1으로 전송 시 a1으로 응답)
                    if (FindJSONtoString("REQ", data) === "CC" && FindJSONtoString("RES", data) === "0000" && FindJSONtoString("TELEGRAMFLAG", data) !== "NE") {
                        //document.getElementById("txtCatApprDate").value = FindJSONtoString("APPROVALTIME", data).substr(0, 6);	// 승인일시
                        //document.getElementById("txtCatApprNo").value = FindJSONtoString("APPROVALNO", data);
                        resultData = '{ ';
                        resultData += '"STATUS":"SUCCESS",';
                        resultData += '"APPROVALTIME":"' +FindJSONtoString("APPROVALTIME", data) + '",';
                        resultData += '"APPROVALNO":"' +FindJSONtoString("APPROVALNO", data) + '",';
                        resultData += '"CARDNO":"' +FindJSONtoString("CARDNO", data) + '",';
                        resultData += '"ISSUERCODE":"' +FindJSONtoString("ISSUERCODE", data) + '",';
                        resultData += '"ISSUERNAME":"' +FindJSONtoString("ISSUERNAME", data) + '",';
                        resultData += '"MERCHANTNUMBER":"' +FindJSONtoString("MERCHANTNUMBER", data) + '",';
                        resultData += '"MESSAGE1":"' +FindJSONtoString("MESSAGE1", data) + '",';
                        resultData += '"MESSAGE2":"' +FindJSONtoString("MESSAGE2", data) + '",';
                        resultData += '"NOTICE1":"' +FindJSONtoString("NOTICE1", data) + '",';
                        resultData += '"TOTAMOUNT":"' +FindJSONtoString("TOTAMOUNT", data) + '",';
                        resultData += '"VATAMOUNT":"' +FindJSONtoString("VATAMOUNT", data) + '",';
                        resultData += '"TELEGRAMFLAG":"' +FindJSONtoString("TELEGRAMFLAG", data) + '"';
                        resultData += '}';
                        return func(resultData);
                    }else{
                        let errmessage = FindJSONtoString("HELPDESK", data);
                        errmessage += " / " + FindJSONtoString("MESSAGE1", data);
                        errmessage += " / " + FindJSONtoString("MESSAGE2", data);

                        resultData = '{ ';
                        resultData += '"STATUS":"FAILURE",';
                        resultData += '"ERRORMESSAGE":"' +  errmessage + '"';
                        resultData += '}';
                        return func(resultData);
                    }
                }

                //document.getElementById("taResponse").innerText = JSONtoString(data);
            },
            error: function (request, status, error) {
                //alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                let errMessage = "erroecode:" + request.status + "\n" + "error:" + error;
                return func('{"STATUS":"FAILURE", "ERRORMESSAGE":"'+ errorDefaultMessage +'", "ERRORDATA":"'+ errMessage+'"}');
            }


        });
    } catch (e) {
        return func('{"STATUS":"FAILURE", "ERRORMESSAGE":"'+ errorDefaultMessage+'"}');

    }

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

String.prototype.fillSpaceUnicode = function (n) {
    var str = this;
    var space = "";
    var charLength = 0;
    var ch1 ="";
    //한글 일경우 문자열갯수를 1개로 인식하여계산한다.

    for(var i = 0;i<str.length;i++){
        ch1 = str.charAt(i);
        if(escape(ch1).length>4){
            charLength += 2;
        }else{
            charLength += 1;
        }
    }

    if (charLength < n) {
        for (i = 0; i < n - charLength; i++) {
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
function numberWithCommasCat(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

