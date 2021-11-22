/* 가상키보드 사용 선언 */
let vkey;
$(function () {
    vkey = new VKeyboard();
});

/* 가상키보드 입력 대상이 되는 텍스트 필드나 텍스트 에어리어 */
let targetFieldID = ["bcName", "bcHp", "bcAddress", "bcRemark"];

/* 각 가상 키보드의 제목 */
let targetFieldSubject = [
    "고객명",
    "휴대폰(숫자만 입력해 주세요)",
    "주소",
    "특이사항"
];

/* 각 가상 키보드의 상용구 배열 */
let boilerArray = [];
boilerArray[0] = ["A", "호호", "% 테스트"];
boilerArray[1] = ["B", "호호", "% 테스트"];
boilerArray[2] = ["C", "호호", "% 테스트"];
boilerArray[3] = ["D", "호호", "% 테스트"];

/* 호출하여 목표 가상 키보드 띄우기, 0번부터 배열 순서대로 */
function openVKeyboard(keyboardNum) {
    if(keyboardNum === 1) {
        /* 휴대폰 번호 수정 후 콜백을 이용하여 번호 유효성 검사를 한다 */
        vkey.showKeyboard(targetFieldID[keyboardNum], targetFieldSubject[keyboardNum], boilerArray[keyboardNum],
            function (){
                onPhonenumChange($("#bcHp")[0]);
            });
    }else{
        vkey.showKeyboard(targetFieldID[keyboardNum], targetFieldSubject[keyboardNum], boilerArray[keyboardNum]);
    }

}

/* 입력된 폼 정보 저장 */
function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val()+$("#bcBirthMM").val()+$("#bcBirthDD").val();

    if(!CommonUI.regularValidator(birthday, "dateExist") && birthday !== "") {
        alertCaution("존재할 수 없는 생년월일 입니다.", 1);
        return false;
    };

    formData.set("bcHp", formData.get("bcHp").replace(/[^0-9]/g, ""));
    formData.append("bcBirthday", birthday);
    const url = "/api/user/customerSave";
    CommonUI.ajax(url, "POST", formData, function (){
       alertSuccess("고객 데이터 저장 성공");
    });

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
function onPhonenumChange (element) {
    let phoneNumber = element.value;
    phoneNumber = phoneNumber.replace(/[^0-9]/g, "");
    let formatNum = "";
    if(phoneNumber.length==11){
        formatNum = phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
    }else if(phoneNumber.length==8){
        formatNum = phoneNumber.replace(/(\d{4})(\d{4})/, '$1-$2');
    }else{
        if(phoneNumber.indexOf('02')==0){
            formatNum = phoneNumber.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
        }else{
            formatNum = phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
        }
    }
    element.value = formatNum;
}
