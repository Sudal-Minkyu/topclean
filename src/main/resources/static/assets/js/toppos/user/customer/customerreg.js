/* 듀얼모니터 여부 기억 */
let hasDuelMonitor = "N";

/* 가상키보드 사용 선언 */
let vkey;
$(function () {
    getBoilerPlate();

    vkey = new VKeyboard();
    $("#signImage").hide();

    presetDuelMonitor();

    getParamsAndAction();
});

/* 가상키보드 입력 대상이 되는 텍스트 필드나 텍스트 에어리어 */
let vkeyTargetId = ["bcName", "bcAddress", "bcRemark"];

let vkeyProp = [];

vkeyProp[0] = {
    title : "고객명",
}

vkeyProp[1] = {
    title : "주소",
}

vkeyProp[2] = {
    title : "특이사항",
}

/* 상용구 받아와서 특정 가상 키보드에 세팅하기 */
function getBoilerPlate() {
    const url = "/api/user/franchiseAddProcessList";
    CommonUI.ajax(url, "GET", {baType: "0"}, function (res) {
        let boilerPlate = [];
        const data = res.sendData.keyWordData;
        for(const {baName} of data) {
            boilerPlate.push(baName);
        }
        vkeyProp[1].boilerplate = boilerPlate;
        vkeyProp[2].boilerplate = boilerPlate;
    });
}


/* 호출하여 목표 가상 키보드 띄우기, 0번부터 배열 순서대로 */
function openVKeyboard(num) {
    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function openHpVKeypad() {
    const keypadProp = { // 키패드로 변경 필요
        title : "휴대폰(숫자만 입력해 주세요)",
        midprocess: "tel",
        callback : onHpChange,
    }
    vkey.showKeypad("bcHp", keypadProp);
}

// 접수화면으로 이동
function receiptMove(){
    location.href="/user/receiptreg"
}

// 입력항목 클리어
function init(){
    const $signImage = $("#signImage");
    $("#bcName").val("");
    $("#bcHp").val("010");
    $("#female").prop("checked",true);
    $("#bcAddress").val("");
    $("#bcBirthYYYY").val("");
    $("#bcBirthMM").val("");
    $("#bcBirthDD").val("");
    $("#bcAge").val("");
    $("#bcWeddingAnniversaryYYYY").val("");
    $("#bcWeddingAnniversaryMM").val("");
    $("#bcWeddingAnniversaryDD").val("");
    $("#bcValuation").val("5");
    $("#no2").prop("checked",true);
    $("#bcAgreeType").val("1");
    $("#bcRemark").val("");
    $("#bcSignImage").val("");
    $signImage.hide();
    $signImage.attr("src", "");
    $("#windowMask").hide();
    $("#mask").hide();

    if(hasDuelMonitor === "N") { // API가 도착하면 작업
        $("#bcAgreeType").val("2");
        $("#bcAgreeType option").first().attr("disabled", "");
        $("#reqSign").attr("disabled", "");
    }
}

/* 입력된 폼 정보 저장 */
function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val() + $("#bcBirthMM").val() + $("#bcBirthDD").val();
    const weddingAnniversary = $("#bcWeddingAnniversaryYYYY").val() + $("#bcWeddingAnniversaryMM").val() 
        + $("#bcWeddingAnniversaryDD").val();

    if(!$("#bcName").val()) {
        alertCaution("고객명을 입력해 주세요", 1);
        return false;
    }
    if($("#bcHp").val().length < 4) {
        alertCaution("휴대폰 번호를 입력해 주세요", 1);
        return false;
    }
    if(!CommonUI.regularValidator(birthday, "dateExist") && birthday !== "") {
        alertCaution("해당 생년월일은 달력에 존재하지 않습니다.", 1);
        return false;
    }
    if(!CommonUI.regularValidator(weddingAnniversary, "dateExist") && weddingAnniversary !== "") {
        alertCaution("해당 결혼기념일은 달력에 존재하지 않습니다.", 1);
        return false;
    }

    const tempBcHp = formData.get("bcHp").numString();
    formData.set("bcHp", tempBcHp);
    formData.append("bcBirthday", birthday);
    formData.append("bcWeddingAnniversary", weddingAnniversary);
    formData.append("bcGrade", "01");

    if($("#bcAgreeType").val()==="1"){
        if($("#bcSignImage").val() && $("#bcSignImage").val().length){
            formData.append("bcSignImage", $("#bcSignImage").val());
        }else{
            alertCaution("고객 서명을 해주세요", 1);
            return false;
        }
    }

    const url = "/api/user/customerSave";
    CommonUI.ajax(url, "POST", formData, function () {
        init();
        alertCheck("고객 데이터 저장을 성공하였습니다.<br>해당 고객으로 바로 접수하시겟습니까?");
        $("#checkDelSuccessBtn").on("click", function () {
            $('#popupId').remove();
            if(tempBcHp) {
                location.href = "./receiptreg?bchp=" + tempBcHp;
            }
        });
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
        CommonUI.toppos.underTaker(e, "customerreg : 서명요청");
        alertCaution("고객 모니터를 발견하지 못했습니다.<br>서면을 통한 사인 요청을 해주세요", 1);
        $("#bcAgreeType").val("2");
        $("#bcAgreeType option").first().attr("disabled", "");
        $("#reqSign").attr("disabled", "");
        $("#signImage").hide();
        return;
    }

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

function presetDuelMonitor() {
    const url = "/api/user/franchiseMultiscreen";
    CommonUI.ajax(url, "GET", false, function (res) {
        hasDuelMonitor = res.sendData.frMultiscreenYn;
        if(hasDuelMonitor === "N") { // API가 도착하면 작업
            $("#bcAgreeType").val("2");
            $("#bcAgreeType option").first().attr("disabled", "");
            $("#reqSign").attr("disabled", "");
        }
    });
}

/* 브라우저의 get 파라미터들을 가져오고 그에 따른 작업을 반영하기 위해 */
function getParamsAndAction() {
    const url = new URL(window.location.href);
    const params = url.searchParams;

    if(params.has("bchp")) {
        const bcHp = params.get("bchp");
        $("#bcHp").val(bcHp);
        onHpChange();
    } else if (params.has("bcname")) {
        const bcName = params.get("bcname");
        $("#bcName").val(bcName);
    }
}