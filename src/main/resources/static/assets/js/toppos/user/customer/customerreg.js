/* 가상키보드 사용 선언 */
let vkey;
$(function () {
    vkey = new VKeyboard();
});

/* 가상키보드 입력 대상이 되는 텍스트 필드나 텍스트 에어리어 */
let vkeyTargetId = ["bcName", "bcHp", "bcAddress", "bcRemark"];

let vkeyProp = [];

vkeyProp[0] = {
    title : "고객명",
}

vkeyProp[1] = { // 키패드로 변경 필요
    title : "휴대폰(숫자만 입력해 주세요)",
    postprocess : function(text) {
        return text.replace(/[^0-9]/g, "");
    },
    callback : onHpChange,
}

vkeyProp[2] = {
    title : "주소",
}

vkeyProp[3] = {
    title : "특이사항",
}

/* 호출하여 목표 가상 키보드 띄우기, 0번부터 배열 순서대로 */
function openVKeyboard(num) {
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

/* 입력된 폼 정보 저장 */
function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val()+$("#bcBirthMM").val()+$("#bcBirthDD").val();

    if(!CommonUI.regularValidator(birthday, "dateExist") && birthday !== "") {
        alertCaution("존재할 수 없는 생년월일 입니다.", 1);
        return false;
    }

    formData.set("bcHp", formData.get("bcHp").replace(/[^0-9]/g, ""));
    formData.append("bcBirthday", birthday);
    formData.append("bcGrade", "01");

    if($("#bcAgreeType").val()==="1"){
        console.log("온라인 입니다.");
        const $resultmsg = $("#resultmsg");
        if($resultmsg.text()==="결과메세지"){
            alertCaution("사인을 해주세요",1)
        }else{
            formData.append("bcSignImage", $("#bcSignImage").val());
        }
    }else{
        console.log("서면 입니다.");
    }

    const url = "/api/user/customerSave";
    CommonUI.ajax(url, "POST", formData, function (){
       alertSuccess("고객 데이터 저장 성공");
    });

}

// base64를 Blob로 변환하는 함수
function b64toBlob(b64Data, contentType, sliceSize) {
    if( b64Data === "" || b64Data === undefined ) return null;
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    var byteCharacters = atob(b64Data);

    var byteArrays = [];

    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);
        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {type: contentType});
}

/* 생년을 바꿀 때 나이대 자동계산 */
function onYearChange(selectedYear) {
    const currentYear = new Date().getFullYear();
    const currentAge = currentYear - selectedYear + 1;
    let resultAger = "";
    if(currentAge > 9 && currentAge < 100) {
        resultAger = Math.floor(currentAge/10) * 10 + "";
    }
    $("#bcAge").val(resultAger).prop("selected", true);
}

/* 전화번호 입력을 위한 유효성 검사 */
function onHpChange () {
    const element = document.getElementById("bcHp");
    let phoneNumber = element.value;
    element.value = CommonUI.onPhoneNumChange(phoneNumber);
}

function onAgreeTypeChange(type) {
    const $requestSign = $("#requestSign");
    if(type === "1") {
        $requestSign.attr("disabled", false);
    }else{
        $requestSign.attr("disabled", true);
    }
}

/* 회원등록을 취소 */
function cancelRegister() {

}

/* 서명을 요청할 때 고객측의 모니터에 서명하도록 뜬다. */
function requestSign() {

    const protocol = location.protocol;
    const hostName = location.hostname;
    const port = location.port;

    cAPI.approvalCall(protocol +'//'+hostName+ ':' + port + '/user/sign');

    // $("#resultmsg").text(": 승인중 메세지- 고객이 서명중입니다. ------전체화면으로 가리기 ")

    const maskHeight = $(document).height();
    const maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#windowMask').css({'width':maskWidth,'height':maskHeight}).show();
    $('#mask').show();

}

function resultFunction(msg){
    $("#windowMask").hide();
    $("#mask").hide();
    // $("#resultmsg").text(msg);
    $("#bcSignImage").val(msg);
    document.getElementById('signImage').src = msg;
    document.getElementById('signImage').style.border = "2px solid black"
}