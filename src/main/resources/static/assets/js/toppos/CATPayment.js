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
    CatPrint_Multi(params,creditData,cancelYN) {

        let message="";
        message = CatCreate_MultiPrint(params,creditData,cancelYN)

        CatPrintCommunication(message);

    }

    CatCashdrawer() {

        let message="";
        message = CatCreate_Cashdrawer()

        CatPrintCommunication(message);

    }

    CatPrintTest() {

        let message="";
        message = CatCreate_PrintTest()

        CatPrintCommunication(message);

    }
}

// 테스트 프린트 내용전송
function CatCreate_PrintTest(){
    let message = "";

    let cancelYN = "N";
    let month = "일시불";
    let type ="card";

    let cardNo = "815755******8874" ; // 카드번호
    let cardName = "우리카드"; // 카드번호
    let approvalNo = ("54571234").trim(); // 승인번호
    let tmpStr = "20211115150145515";
    let approvalTime = tmpStr.substr(0,4) + "-" + tmpStr.substr(4,2) + "-" + tmpStr.substr(6,2)+ " " + tmpStr.substr(8,2) + ":" + tmpStr.substr(10,2); // 승인일시

    let params =
        {
            "type":"card",
            "franchiseNo":"123",
            "totalAmount":10500,
            "month":0,
            "items":[
                {"tagno":"1231234","color":"없음","itemname":"면 상의","price":2500},
                {"tagno":"1231235","color":"남색","itemname":"청바지 하의","price":3500},
                {"tagno":"1241236","color":"검정","itemname":"롱 오리털 코트","price":4500}
            ]
        };

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
    message += "[고객용 영수증]";
    message += String.fromCharCode(10);
    message += String.fromCharCode(10);
    //**************************************************
    // 정렬: ESC a (0: 왼쪽, 1: 가운데, 2: 오른쪽 )
    //**************************************************
    message += String.fromCharCode(27);
    message += String.fromCharCode(97);
    message += String.fromCharCode(0);
    // //**************************************************
    // // Double-width : 해제 ESC !
    // //**************************************************
    // message += String.fromCharCode(27);
    // message += String.fromCharCode(33);
    // message += String.fromCharCode(0);
    // //**************************************************
    // // 문자열 출력
    // //**************************************************
    //
    //
    // message += "영수증 - 접수증(고객용)";
    // message += String.fromCharCode(10);
    // message += "대리점명   서정마을점";
    // message += String.fromCharCode(10);
    // message += "영업시간   평일:09:30~20:00 까지";
    // message += String.fromCharCode(10);
    // message += "           공휴일(토요일):10:30~18:00 까지";
    // message += String.fromCharCode(10);
    // message += "사업자번호 124-25-55555";
    // message += String.fromCharCode(10);
    // message += "대표자     홍대표  | 전화    031-2222-5555";
    // message += String.fromCharCode(10);
    // message += "고객성명   김길동 님";
    // message += String.fromCharCode(10);
    // message += "고객전화   ";
    // message += String.fromCharCode(27);
    // message += String.fromCharCode(33);
    // message += String.fromCharCode(8);
    // message += "010-****-1234";
    // message += String.fromCharCode(10);
    // message += String.fromCharCode(27);
    // message += String.fromCharCode(33);
    // message += String.fromCharCode(0);
    // message += "접수일자 : 2021-11-25 13:20               ";
    // message += String.fromCharCode(10);
    // message += "==========================================";
    // message += String.fromCharCode(10);
    // message += "택번호  상품                  색상   금액 ";
    // message += String.fromCharCode(10);
    // message += "==========================================";
    // message += String.fromCharCode(10);
    // params.items.forEach((item,idx)=>{
    //     message += String.fromCharCode(27);
    //     message += String.fromCharCode(33);
    //     message += String.fromCharCode(8);
    //     message += item.tagno.substr(3,1);
    //     message += "-" + item.tagno.substr(4,3);
    //     message += String.fromCharCode(27);
    //     message += String.fromCharCode(33);
    //     message += String.fromCharCode(0);
    //     if (cancelYN==="Y"){
    //         message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5)+ " " + numberWithCommasCat(-1 * item.price).padStart(8);
    //     }else {
    //         message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5) + " " + numberWithCommasCat(item.price).padStart(8);
    //     }
    //     message += String.fromCharCode(10);
    //
    // });
    // message += "------------------------------------------";
    // message += String.fromCharCode(10);
    //
    //
    //
    // if (cancelYN==="Y"){
    //     message += "수   량 : " + ("" + params.items.length).padStart(3) + "         취소금액 :" + (" " + numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    // }else {
    //     message += "소계  수량    :" + ("" + params.items.length).padStart(8) + "  접수금액:" + (" " + numberWithCommasCat(params.totalAmount)).padStart(8);
    //     message += String.fromCharCode(10);
    //     message += "      추가금액:" + ("" + numberWithCommasCat(0)).padStart(8) + "  할인금액:" + (" " + numberWithCommasCat(0)).padStart(8);
    //     message += String.fromCharCode(10);
    //     message += "      인도예정일:";
    //     message += String.fromCharCode(27);
    //     message += String.fromCharCode(33);
    //     message += String.fromCharCode(8);
    //     message += "2021-11-21";
    //     message += String.fromCharCode(27);
    //     message += String.fromCharCode(33);
    //     message += String.fromCharCode(0);
    //     message += String.fromCharCode(10);
    //     message += "      (인도예정일이 공휴일일때는 익일인도)";
    // }
    // message += String.fromCharCode(10);
    //
    // message += "==========================================";
    // message += String.fromCharCode(10);
    // message += "<결제정보>";
    // message += String.fromCharCode(10);
    // message += "접수일자 2021-11-25 13:20                 ";
    // message += String.fromCharCode(10);
    // message += "총접수수량 (일반 :3 / 특수:0 )         2건";
    // message += String.fromCharCode(10);
    // message += "총접수금액  "
    // message += String.fromCharCode(27);
    // message += String.fromCharCode(33);
    // message += String.fromCharCode(32);
    // message += (numberWithCommasCat(10500)).padStart(15);
    // message += String.fromCharCode(27);
    // message += String.fromCharCode(33);
    // message += String.fromCharCode(0);
    // message += String.fromCharCode(10);
    // message += "------------------------------------------";
    // message += String.fromCharCode(10);
    // message += "<미수금정보>";
    // message += String.fromCharCode(10);
    // message += "기존미수금:" + ("" + numberWithCommasCat(5000)).padStart(8) + "    당일미수금:" + (" " + numberWithCommasCat(0)).padStart(8);
    // message += String.fromCharCode(10);
    // message += "미수금상환:" + ("" + numberWithCommasCat(0)).padStart(8) + "      총미수금:" + (" " + numberWithCommasCat(5000)).padStart(8);
    // message += String.fromCharCode(10);
    // if (type ==='card') {
    //     message += "==========================================";
    //     message += String.fromCharCode(10);
    //     message += "결제구분                              카드";
    //     message += String.fromCharCode(10);
    //     message += "승인일시                  " + approvalTime.padStart(16);
    //     message += String.fromCharCode(10);
    //     if (cancelYN==="Y") {
    //         message += ("할부 : " + month).fillSpaceUnicode(14) + "    취소금액".fillSpaceUnicode(19) + (numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    //     }else {
    //         message += ("할부 : " + month).fillSpaceUnicode(14) + "    결제금액".fillSpaceUnicode(19) + (numberWithCommasCat(params.totalAmount)).padStart(9);
    //     }
    //     message += String.fromCharCode(10);
    //     message += cardName.fillSpaceUnicode(24) +" " + cardNo.padStart(17);
    //     message += String.fromCharCode(10);
    //     message += "승인번호                       " + approvalNo.padStart(11);
    //     message += String.fromCharCode(10);
    //     message += "==========================================";
    //     message += String.fromCharCode(10);
    // }


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

// 프린트 내용 생성
function CatCreate_Print(params,creditResult,cancelYN){
    let message = "";


    let type = params.type
    let month = "일시불";
    let cardNo = "";
    let cardName = "";
    let approvalNo = "";
    let tmpStr = "";
    let approvalTime = "";
    if (type === 'card') {
        if ((creditResult.month) > 1 ){
            month = params.month + "개월"
        }
        cardNo = "" + creditResult.cardNo; // 카드번호
        cardName = "" + creditResult.cardName; // 카드번호
        approvalNo = ("" + creditResult.approvalNo).trim(); // 승인번호
        tmpStr = "20" + creditResult.approvalTime;
        approvalTime = tmpStr.substr(0, 4) + "-" + tmpStr.substr(4, 2) + "-" + tmpStr.substr(6, 2) + " " + tmpStr.substr(8, 2) + ":" + tmpStr.substr(10, 2); // 승인일시
    }

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
    message += "[고객용 영수증]";
    message += String.fromCharCode(10);
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
    message += "영수증 - 접수증(고객용)";
    message += String.fromCharCode(10);
    message += "대리점명   " + params.franchiseName;
    message += String.fromCharCode(10);
    message += "영업시간   평일:09:30~20:00 까지";
    message += String.fromCharCode(10);
    message += "           공휴일(토요일):10:30~18:00 까지";
    message += String.fromCharCode(10);
    message += "사업자번호 " + params.businessNO;
    message += String.fromCharCode(10);
    message += "대표자     " + params.repreName+ "  | 전화  " + params.franchiseTel;
    message += String.fromCharCode(10);
    message += "고객성명   " + params.customerName + " 님";
    message += String.fromCharCode(10);
    message += "고객전화   ";
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(8);
    message += params.customerTel;
    message += String.fromCharCode(10);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += " 택번호   상품              색상     금액 ";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);
    let specialCnt = 0;
    params.items.forEach((item,idx)=>{
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += item.tagno.substr(3,1);
        message += "-" + item.tagno.substr(4,3);
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        if (item.specialyn =="Y") specialCnt += 1;
        if (cancelYN==="Y"){
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5)+ " " + numberWithCommasCat(-1 * item.price).padStart(8);
        }else {
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5) + " " + numberWithCommasCat(item.price).padStart(8);
        }
        message += String.fromCharCode(10);

    });
    message += "------------------------------------------";
    message += String.fromCharCode(10);



    if (cancelYN==="Y"){
        message += "수   량 : " + ("" + params.items.length).padStart(3) + "         취소금액 :" + (" " + numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    }else {
        message += "소계  수량    :" + ("" + params.items.length).padStart(8) + "  합계금액:" + (" " + numberWithCommasCat(params.totalAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      기본금액:" + ("" + numberWithCommasCat(params.normalAmount)).padStart(8) + "  추가금액:" + (" " + numberWithCommasCat(params.changeAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      인도예정일:";
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += params.estimateDt;
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        message += String.fromCharCode(10);
        message += "      (인도예정일이 공휴일일때는 익일인도)";
    }
    message += String.fromCharCode(10);

    message += "==========================================";
    message += String.fromCharCode(10);
    message += "<결제정보>";
    message += String.fromCharCode(10);
    message += "접수일자 "+ params.requestDt;
    message += String.fromCharCode(10);
    message += "총접수수량 (일반 :" + (params.items.length - specialCnt) + " / 특수:" + specialCnt + " )         " + params.items.length + "건";
    message += String.fromCharCode(10);
    message += "총접수금액  "
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(32);
    message += (numberWithCommasCat(params.totalAmount)).padStart(15);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "<미수금정보>";
    message += String.fromCharCode(10);
    message += "전일미수금:" + ("" + numberWithCommasCat(params.preUncollectAmount)).padStart(8) + "    당일미수금:" + (" " + numberWithCommasCat(params.curUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    message += "미수금상환:" + ("" + numberWithCommasCat(params.uncollectPayAmount)).padStart(8) + "      총미수금:" + (" " + numberWithCommasCat(params.totalUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    if (type ==='card') {
        message += "==========================================";
        message += String.fromCharCode(10);
        message += "결제구분                              카드";
        message += String.fromCharCode(10);
        message += "승인일시                  " + approvalTime.padStart(16);
        message += String.fromCharCode(10);
        if (cancelYN==="Y") {
            message += ("할부 : " + month).fillSpaceUnicode(14) + "    취소금액".fillSpaceUnicode(19) + (numberWithCommasCat(-1*params.paymentAmount)).padStart(9);
        }else {
            message += ("할부 : " + month).fillSpaceUnicode(14) + "    결제금액".fillSpaceUnicode(19) + (numberWithCommasCat(params.paymentAmount)).padStart(9);
        }
        message += String.fromCharCode(10);
        message += cardName.fillSpaceUnicode(24) +" " + cardNo.padStart(17);
        message += String.fromCharCode(10);
        message += "승인번호                       " + approvalNo.padStart(11);
        message += String.fromCharCode(10);
        message += "==========================================";
        message += String.fromCharCode(10);
    }
    if (type ==='cash') {
        message += "==========================================";
        message += String.fromCharCode(10);
        message += "결제구분                              현금";
        message += String.fromCharCode(10);
        if (cancelYN==="Y") {
            message += "취소금액".fillSpaceUnicode(33) + (numberWithCommasCat(-1*params.paymentAmount)).padStart(9);
        }else {
            message += "결제금액".fillSpaceUnicode(33) + (numberWithCommasCat(params.paymentAmount)).padStart(9);
        }
        message += String.fromCharCode(10);
        message += "==========================================";
        message += String.fromCharCode(10);
    }

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


    //==========매장용
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
    message += "[매장용 영수증]";
    message += String.fromCharCode(10);
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
    message += "영수증 - 접수증(매장용)";
    message += String.fromCharCode(10);
    message += "고객성명   " + params.customerName + " 님";
    message += String.fromCharCode(10);
    message += "고객전화   ";
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(8);
    message += params.customerTel;
    message += String.fromCharCode(10);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += " 택번호   상품              색상     금액 ";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);

    params.items.forEach((item,idx)=>{
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += item.tagno.substr(3,1);
        message += "-" + item.tagno.substr(4,3);
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        if (cancelYN==="Y"){
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5)+ " " + numberWithCommasCat(-1 * item.price).padStart(8);
        }else {
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5) + " " + numberWithCommasCat(item.price).padStart(8);
        }
        message += String.fromCharCode(10);

    });
    message += "------------------------------------------";
    message += String.fromCharCode(10);



    if (cancelYN==="Y"){
        message += "수   량 : " + ("" + params.items.length).padStart(3) + "         취소금액 :" + (" " + numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    }else {
        message += "소계  수량    :" + ("" + params.items.length).padStart(8) + "  합계금액:" + (" " + numberWithCommasCat(params.totalAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      기본금액:" + ("" + numberWithCommasCat(params.normalAmount)).padStart(8) + "  추가금액:" + (" " + numberWithCommasCat(params.changeAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      인도예정일:";
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += params.estimateDt;
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        message += String.fromCharCode(10);
        message += "      (인도예정일이 공휴일일때는 익일인도)";
    }
    message += String.fromCharCode(10);

    message += "==========================================";
    message += String.fromCharCode(10);
    message += "<결제정보>";
    message += String.fromCharCode(10);
    message += "접수일자 "+ params.requestDt;
    message += String.fromCharCode(10);
    message += "총접수수량 (일반 :" + (params.items.length - specialCnt) + " / 특수:" + specialCnt + " )         " + params.items.length + "건";
    message += String.fromCharCode(10);
    message += "총접수금액  "
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(32);
    message += (numberWithCommasCat(params.totalAmount)).padStart(15);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "<미수금정보>";
    message += String.fromCharCode(10);
    message += "전일미수금:" + ("" + numberWithCommasCat(params.preUncollectAmount)).padStart(8) + "    당일미수금:" + (" " + numberWithCommasCat(params.curUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    message += "미수금상환:" + ("" + numberWithCommasCat(params.uncollectPayAmount)).padStart(8) + "      총미수금:" + (" " + numberWithCommasCat(params.totalUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    if (type ==='card') {
        message += "==========================================";
        message += String.fromCharCode(10);
        message += "결제구분                              카드";
        message += String.fromCharCode(10);
        message += "승인일시                  " + approvalTime.padStart(16);
        message += String.fromCharCode(10);
        if (cancelYN==="Y") {
            message += ("할부 : " + month).fillSpaceUnicode(14) + "    취소금액".fillSpaceUnicode(19) + (numberWithCommasCat(-1*params.paymentAmount)).padStart(9);
        }else {
            message += ("할부 : " + month).fillSpaceUnicode(14) + "    결제금액".fillSpaceUnicode(19) + (numberWithCommasCat(params.paymentAmount)).padStart(9);
        }
        message += String.fromCharCode(10);
        message += cardName.fillSpaceUnicode(24) +" " + cardNo.padStart(17);
        message += String.fromCharCode(10);
        message += "승인번호                       " + approvalNo.padStart(11);
        message += String.fromCharCode(10);
        message += "==========================================";
        message += String.fromCharCode(10);
    }
    if (type ==='cash') {
        message += "==========================================";
        message += String.fromCharCode(10);
        message += "결제구분                              현금";
        message += String.fromCharCode(10);
        if (cancelYN==="Y") {
            message += "취소금액".fillSpaceUnicode(33) + (numberWithCommasCat(-1*params.paymentAmount)).padStart(9);
        }else {
            message += "결제금액".fillSpaceUnicode(33) + (numberWithCommasCat(params.paymentAmount)).padStart(9);
        }
        message += String.fromCharCode(10);
        message += "==========================================";
        message += String.fromCharCode(10);
    }

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
    let totalAmount = ("" + params.paymentAmount).fillZero(9);
    let vatAmount = ("" + Math.floor(params.paymentAmount / 11)).fillZero(9); //부가세 계산 내림으로 처리
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



// 프린트 내용 생성(복합결제 대응)
function CatCreate_Cashdrawer(){
    let message = "";



    //**************************************************
    // 금전함열기
    //**************************************************

    message += String.fromCharCode(27);
    message += String.fromCharCode(112);
    message += String.fromCharCode(48);
    message += String.fromCharCode(64);
    message += String.fromCharCode(80);



    var len = 0;
    for (var i = 0; i < message.length; i++) {
        len += (message.charCodeAt(i) > 128) ? 2 : 1;
    }

    var messageLen = ("" + len).fillZero(4);   // 길이

    message = "CATPRINT" + messageLen + message;	// 길이 추가

    len += 12;	// CATPRINT 8Byte + Length 4Byte 추가

    return "CC" + message;
}


// 프린트 내용 생성(복합결제 대응)
function CatCreate_MultiPrint(params,creditResults,cancelYN){
    let message = "";



    
    let cardNo = "";
    let cardName = "";
    let approvalNo = "";
    let tmpStr = "";
    let approvalTime = "";


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
    message += "[고객용 영수증]";
    message += String.fromCharCode(10);
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
    message += "영수증 - 접수증(고객용)";
    message += String.fromCharCode(10);
    message += "대리점명   " + params.franchiseName;
    message += String.fromCharCode(10);
    message += "영업시간   평일:09:30~20:00 까지";
    message += String.fromCharCode(10);
    message += "           공휴일(토요일):10:30~18:00 까지";
    message += String.fromCharCode(10);
    message += "사업자번호 " + params.businessNO;
    message += String.fromCharCode(10);
    message += "대표자     " + params.repreName+ "  | 전화  " + params.franchiseTel;
    message += String.fromCharCode(10);
    message += "고객성명   " + params.customerName + " 님";
    message += String.fromCharCode(10);
    message += "고객전화   ";
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(8);
    message += params.customerTel;
    message += String.fromCharCode(10);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += " 택번호   상품              색상     금액 ";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);
    let specialCnt = 0;
    params.items.forEach((item,idx)=>{
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += item.tagno.substr(3,1);
        message += "-" + item.tagno.substr(4,3);
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        if (item.specialyn =="Y") specialCnt += 1;
        if (cancelYN==="Y"){
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5)+ " " + numberWithCommasCat(-1 * item.price).padStart(8);
        }else {
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5) + " " + numberWithCommasCat(item.price).padStart(8);
        }
        message += String.fromCharCode(10);

    });
    message += "------------------------------------------";
    message += String.fromCharCode(10);



    if (cancelYN==="Y"){
        message += "수   량 : " + ("" + params.items.length).padStart(3) + "         취소금액 :" + (" " + numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    }else {
        message += "소계  수량    :" + ("" + params.items.length).padStart(8) + "  합계금액:" + (" " + numberWithCommasCat(params.totalAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      기본금액:" + ("" + numberWithCommasCat(params.normalAmount)).padStart(8) + "  추가금액:" + (" " + numberWithCommasCat(params.changeAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      인도예정일:";
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += params.estimateDt;
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        message += String.fromCharCode(10);
        message += "      (인도예정일이 공휴일일때는 익일인도)";
    }
    message += String.fromCharCode(10);

    message += "==========================================";
    message += String.fromCharCode(10);
    message += "<결제정보>";
    message += String.fromCharCode(10);
    message += "접수일자 "+ params.requestDt;
    message += String.fromCharCode(10);
    message += "총접수수량 (일반 :" + (params.items.length - specialCnt) + " / 특수:" + specialCnt + " )         " + params.items.length + "건";
    message += String.fromCharCode(10);
    message += "총접수금액  "
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(32);
    message += (numberWithCommasCat(params.totalAmount)).padStart(15);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "<미수금정보>";
    message += String.fromCharCode(10);
    message += "전일미수금:" + ("" + numberWithCommasCat(params.preUncollectAmount)).padStart(8) + "    당일미수금:" + (" " + numberWithCommasCat(params.curUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    message += "미수금상환:" + ("" + numberWithCommasCat(params.uncollectPayAmount)).padStart(8) + "      총미수금:" + (" " + numberWithCommasCat(params.totalUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    creditResults.forEach((creditResult,idx)=>{
        if (creditResult.type ==='card') {
            let month = "일시불";
            if ((creditResult.month) > 1 ){
                month = creditResult.month + "개월"
            }
            cardNo = "" + creditResult.cardNo; // 카드번호
            cardName = "" + creditResult.cardName; // 카드번호
            approvalNo = ("" + creditResult.approvalNo).trim(); // 승인번호
            tmpStr = "20" + creditResult.approvalTime;
            approvalTime = tmpStr.substr(0, 4) + "-" + tmpStr.substr(4, 2) + "-" + tmpStr.substr(6, 2) + " " + tmpStr.substr(8, 2) + ":" + tmpStr.substr(10, 2); // 승인일시


            message += "==========================================";
            message += String.fromCharCode(10);
            message += "결제구분                              카드";
            message += String.fromCharCode(10);
            message += "승인일시                  " + approvalTime.padStart(16);
            message += String.fromCharCode(10);
            if (cancelYN==="Y") {
                message += ("할부 : " + month).fillSpaceUnicode(14) + "    취소금액".fillSpaceUnicode(19) + (numberWithCommasCat(-1*creditResult.payAmount)).padStart(9);
            }else {
                message += ("할부 : " + month).fillSpaceUnicode(14) + "    결제금액".fillSpaceUnicode(19) + (numberWithCommasCat(creditResult.payAmount)).padStart(9);
            }
            message += String.fromCharCode(10);
            message += cardName.fillSpaceUnicode(24) +" " + cardNo.padStart(17);
            message += String.fromCharCode(10);
            message += "승인번호                       " + approvalNo.padStart(11);
            message += String.fromCharCode(10);

        }
        if (creditResult.type ==='cash') {
            message += "==========================================";
            message += String.fromCharCode(10);
            message += "결제구분                              현금";
            message += String.fromCharCode(10);
            if (cancelYN==="Y") {
                message += "취소금액".fillSpaceUnicode(33) + (numberWithCommasCat(-1*creditResult.payAmount)).padStart(9);
            }else {
                message += "결제금액".fillSpaceUnicode(33) + (numberWithCommasCat(creditResult.payAmount)).padStart(9);
            }
            message += String.fromCharCode(10);

        }
        if (creditResult.type ==='save') {
            message += "==========================================";
            message += String.fromCharCode(10);
            message += "결제구분                            적립금";
            message += String.fromCharCode(10);
            if (cancelYN==="Y") {
                message += "취소금액".fillSpaceUnicode(33) + (numberWithCommasCat(-1*creditResult.payAmount)).padStart(9);
            }else {
                message += "사용금액".fillSpaceUnicode(33) + (numberWithCommasCat(creditResult.payAmount)).padStart(9);
            }
            message += String.fromCharCode(10);

        }

    });
    message += "==========================================";
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


    //==========매장용
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
    message += "[매장용 영수증]";
    message += String.fromCharCode(10);
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
    message += "영수증 - 접수증(매장용)";
    message += String.fromCharCode(10);
    message += "고객성명   " + params.customerName + " 님";
    message += String.fromCharCode(10);
    message += "고객전화   ";
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(8);
    message += params.customerTel;
    message += String.fromCharCode(10);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += "==========================================";
    message += String.fromCharCode(10);
    message += " 택번호   상품              색상     금액 ";
    message += String.fromCharCode(10);
    message += "==========================================";
    message += String.fromCharCode(10);

    params.items.forEach((item,idx)=>{
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += item.tagno.substr(3,1);
        message += "-" + item.tagno.substr(4,3);
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        if (cancelYN==="Y"){
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5)+ " " + numberWithCommasCat(-1 * item.price).padStart(8);
        }else {
            message += " " + item.itemname.fillSpaceUnicode(21) + " " + item.color.fillSpaceUnicode(5) + " " + numberWithCommasCat(item.price).padStart(8);
        }
        message += String.fromCharCode(10);

    });
    message += "------------------------------------------";
    message += String.fromCharCode(10);



    if (cancelYN==="Y"){
        message += "수   량 : " + ("" + params.items.length).padStart(3) + "         취소금액 :" + (" " + numberWithCommasCat(-1*params.totalAmount)).padStart(9);
    }else {
        message += "소계  수량    :" + ("" + params.items.length).padStart(8) + "  합계금액:" + (" " + numberWithCommasCat(params.totalAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      기본금액:" + ("" + numberWithCommasCat(params.normalAmount)).padStart(8) + "  추가금액:" + (" " + numberWithCommasCat(params.changeAmount)).padStart(8);
        message += String.fromCharCode(10);
        message += "      인도예정일:";
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(8);
        message += params.estimateDt;
        message += String.fromCharCode(27);
        message += String.fromCharCode(33);
        message += String.fromCharCode(0);
        message += String.fromCharCode(10);
        message += "      (인도예정일이 공휴일일때는 익일인도)";
    }
    message += String.fromCharCode(10);

    message += "==========================================";
    message += String.fromCharCode(10);
    message += "<결제정보>";
    message += String.fromCharCode(10);
    message += "접수일자 "+ params.requestDt;
    message += String.fromCharCode(10);
    message += "총접수수량 (일반 :" + (params.items.length - specialCnt) + " / 특수:" + specialCnt + " )         " + params.items.length + "건";
    message += String.fromCharCode(10);
    message += "총접수금액  "
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(32);
    message += (numberWithCommasCat(params.totalAmount)).padStart(15);
    message += String.fromCharCode(27);
    message += String.fromCharCode(33);
    message += String.fromCharCode(0);
    message += String.fromCharCode(10);
    message += "------------------------------------------";
    message += String.fromCharCode(10);
    message += "<미수금정보>";
    message += String.fromCharCode(10);
    message += "전일미수금:" + ("" + numberWithCommasCat(params.preUncollectAmount)).padStart(8) + "    당일미수금:" + (" " + numberWithCommasCat(params.curUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);
    message += "미수금상환:" + ("" + numberWithCommasCat(params.uncollectPayAmount)).padStart(8) + "      총미수금:" + (" " + numberWithCommasCat(params.totalUncollectAmount)).padStart(8);
    message += String.fromCharCode(10);

    creditResults.forEach((creditResult,idx)=>{
        if (creditResult.type ==='card') {
            let month = "일시불";
            if ((creditResult.month) > 1 ){
                month = creditResult.month + "개월"
            }
            cardNo = "" + creditResult.cardNo; // 카드번호
            cardName = "" + creditResult.cardName; // 카드번호
            approvalNo = ("" + creditResult.approvalNo).trim(); // 승인번호
            tmpStr = "20" + creditResult.approvalTime;
            approvalTime = tmpStr.substr(0, 4) + "-" + tmpStr.substr(4, 2) + "-" + tmpStr.substr(6, 2) + " " + tmpStr.substr(8, 2) + ":" + tmpStr.substr(10, 2); // 승인일시


            message += "==========================================";
            message += String.fromCharCode(10);
            message += "결제구분                              카드";
            message += String.fromCharCode(10);
            message += "승인일시                  " + approvalTime.padStart(16);
            message += String.fromCharCode(10);
            if (cancelYN==="Y") {
                message += ("할부 : " + month).fillSpaceUnicode(14) + "    취소금액".fillSpaceUnicode(19) + (numberWithCommasCat(-1*creditResult.payAmount)).padStart(9);
            }else {
                message += ("할부 : " + month).fillSpaceUnicode(14) + "    결제금액".fillSpaceUnicode(19) + (numberWithCommasCat(creditResult.payAmount)).padStart(9);
            }
            message += String.fromCharCode(10);
            message += cardName.fillSpaceUnicode(24) +" " + cardNo.padStart(17);
            message += String.fromCharCode(10);
            message += "승인번호                       " + approvalNo.padStart(11);
            message += String.fromCharCode(10);

        }
        if (creditResult.type ==='cash') {
            message += "==========================================";
            message += String.fromCharCode(10);
            message += "결제구분                              현금";
            message += String.fromCharCode(10);
            if (cancelYN==="Y") {
                message += "취소금액".fillSpaceUnicode(33) + (numberWithCommasCat(-1*creditResult.payAmount)).padStart(9);
            }else {
                message += "결제금액".fillSpaceUnicode(33) + (numberWithCommasCat(creditResult.payAmount)).padStart(9);
            }
            message += String.fromCharCode(10);

        }
        if (creditResult.type ==='save') {
            message += "==========================================";
            message += String.fromCharCode(10);
            message += "결제구분                            적립금";
            message += String.fromCharCode(10);
            if (cancelYN==="Y") {
                message += "취소금액".fillSpaceUnicode(33) + (numberWithCommasCat(-1*creditResult.payAmount)).padStart(9);
            }else {
                message += "사용금액".fillSpaceUnicode(33) + (numberWithCommasCat(creditResult.payAmount)).padStart(9);
            }
            message += String.fromCharCode(10);

        }

    });
    message += "==========================================";
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


//카드결제전문생성
function CatCredit_vPOSV1(params) {
    //전문생성
    let totalAmount = ("" + params.paymentAmount).fillZero(9);
    let vatAmount = ("" + Math.floor(params.paymentAmount / 11)).fillZero(9); //부가세 계산 내림으로 처리
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
    let errorDefaultMessage ="단말기 처리 중 에러가 발생하였습니다<br>잠시후 다시 시도해주세요";
    try {

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
                let errMessage = "erroecode:" + request.status + ", " + "error:" + error;
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
        },
        error: function (data) {
            if(data.status === 404) {
                alertCancel("영수증을 출력할 단말기가 감지되지 않습니다.<br>단말기 연결을 확인해 주세요.");
            } else {
                CommonUI.toppos.underTaker(data.status, "인쇄중 404 이외의 에러발생");
            }
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

