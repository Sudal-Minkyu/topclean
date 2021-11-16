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
    vkey.showKeyboard(targetFieldID[keyboardNum], targetFieldSubject[keyboardNum], boilerArray[keyboardNum]);
}

function saveRegister() {
    const formData = new FormData(document.getElementById('userregForm'));
    const birthday = $("#bcBirthYYYY").val()+$("#bcBirthMM").val()+$("#bcBirthDD").val();

    if(!CommonUI.regularValidator(birthday, "dateExist")) {
        alertCaution("존재하지 않는 생년월일 입니다.", 1);
        return false;
    };

    formData.append("bcBirthday", birthday);
    const url = "/api/";
}

function onYearChange() {
    
}