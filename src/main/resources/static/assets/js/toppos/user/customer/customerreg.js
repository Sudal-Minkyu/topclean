/* 가상키보드 사용 선언 */
let vkey;
$(function () {
    vkey = new VKeyboard();
    $("#signImage").hide();
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

// 접수화면으로 이동
function receiptMove(){
    location.href="/user/receiptreg"
}

// 입력항목 클리어
function init(){
    const $signImage = $("#signImage");
    $("#bcName").val("");
    $("#bcHp").val("");
    $("#female").prop("checked",true);
    $("#bcAddress").val("");
    $("#bcBirthYYYY").val("");
    $("#bcBirthMM").val("");
    $("#bcBirthDD").val("");
    $("#bcAge").val("");
    $("#bcValuation").val("3");
    $("#yes2").prop("checked",true);
    $("#bcAgreeType").val("1");
    $("#bcRemark").val("");
    $("#bcSignImage").val("");
    $signImage.hide();
    $signImage.attr("src", "");
    $("#windowMask").hide();
    $("#mask").hide();
}

/* 입력된 폼 정보 저장 */
function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val()+$("#bcBirthMM").val()+$("#bcBirthDD").val();

    if(!$("#bcName").val()) {
        alertCaution("고객명을 입력해 주세요", 1);
        return false;
    }
    if($("#bcHp").val().length < 4) {
        alertCaution("휴대폰 번호를 입력해 주세요", 1);
        return false;
    }
    if(!CommonUI.regularValidator(birthday, "dateExist") && birthday !== "") {
        alertCaution("존재할 수 없는 생년월일 입니다", 1);
        return false;
    }

    formData.set("bcHp", formData.get("bcHp").replace(/[^0-9]/g, ""));
    formData.append("bcBirthday", birthday);
    formData.append("bcGrade", "01");

    if($("#bcAgreeType").val()==="1"){
        console.log("온라인 입니다.");
        if($("#SignImage").val().length){
            formData.append("bcSignImage", $("#bcSignImage").val());
        }else{
            alertCaution("사인을 해주세요",1);
            return false;
        }
    }else{
        console.log("서면 입니다.");
    }

    const url = "/api/user/customerSave";
    CommonUI.ajax(url, "POST", formData, function (){
        init();
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
    element.value = CommonUI.formatTel(phoneNumber);
}

function onAgreeTypeChange(type) {
    const $reqSign = $("#reqSign");
    const $signImage = $("#signImage");
    if(type === "1") {
        $reqSign.attr("disabled", false);
        if($signImage.attr("src")) $signImage.show();
    }else{
        $reqSign.attr("disabled", true);
        $signImage.hide();
    }
}

/* 서명을 요청할 때 고객측의 모니터에 서명하도록 뜬다. */
function requestSign() {
    try {
        const protocol = location.protocol;
        const hostName = location.hostname;
        const port = location.port;

        cAPI.approvalCall(protocol + '//' + hostName + ':' + port + '/user/sign');
    }catch (e) {
        console.log(e);
        return;
    }
    // $("#resultmsg").text(": 승인중 메세지- 고객이 서명중입니다. ------전체화면으로 가리기 ");

    const maskHeight = $(document).height();
    const maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#windowMask').css({'width':maskWidth,'height':maskHeight}).show();
    $('#mask').show();

}

function resultFunction(msg){
    $("#windowMask").hide();
    $("#mask").hide();
    $("#resultmsg").text(msg);
    $("#bcSignImage").val(msg);
    document.getElementById('signImage').src = msg;
    document.getElementById('signImage').style.border = "2px solid black";
    $("#signImage").show();
}