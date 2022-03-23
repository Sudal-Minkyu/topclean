
가상 키보드 사용 설명서


준비
1. 우선 웹 페이지에 Vkeyboard.js 와 Vkeyboard.css 를 탑재시킨다.

2. 웹페이지가 로드되고 나서 vkey = new VKeyboard(); 하는 식으로 객체에 담아 사용 준비를 한다.


키보드 사용
3. 2번의 예시 기준으로
vkey.showKeyboard("html 대상 텍스트필드나 텍스트에어리어의 id", 옵션객체);
를 통해서 키보드를 띄울 수 있다.
여기서 옵션 객체는 이런 식으로 구성된다.

옵션객체 = {
    subject : "검색어를 입력하세요", // 가상키보드의 상단에 표시할 제목
    boilerplate : ["곤지종합병원", "곡성빌라", "양산로 3번지", ....], // 상용구 배열 현재 32개까지 표시되도록 되어있음.
    defaultKeyboard : 0 // 키보드가 표시될 때 기본 선택될 키보드 0 = 한글 부터 1 = A, .....a특특2 순서대로
    calladvance : // 가상키보드를 띄우기 전에 실행될 기능
    postprocess : // 가상키보드에 텍스트를 옮기기 전에 값의 처리가 필요한 경우 값을 인자로 받아 가공하여 리턴값을 가상키보드 입력창으로 보낸다.
    endprocess : // 가상키보드 동작을 마칠 때 결과값이 인자로 오고 리턴값이 필드에 입력된다.
    callback : drinkingbeer // 가상키보드 동작을 성공적으로 마쳤키패 때 실행될 기능 (여기 기준 function drinkingbeeer(result) 함수)
        // 콜백 함수의 인자로 키보드의 최종 입력 결과값이 전달되므로, 필요시 인자로 받아서 사용하면 된다.
}

여기서 입력하지 않는 옵션객체의 키+벨류는 아래의 기본 값으로 동작하게 된다.
defaultProp = {
    subject: "내용 입력",
    boilerplate: [],
    defaultKeyboard: 0,
    calladvance: function () {},
    postprocess: function(text) {return text;},
    endprocess: function(text) {return text;},
    callback: function() {},
}


키패드 사용
3. 2번의 예시 기준으로
vkey.showKeypad("html 대상 텍스트필드나 텍스트에어리어의 id", 옵션객체);
로 구성된다.
옵션 객체는 아래와 같이 구성된다.

옵션객체 = {
    type: // 가상키패드의 형태, "default" 기본, "plusminus" +-버튼으로 양수나 음수구분
    midprocess: // 키 입력마다 페이지를 가공하는 형태, "default" 일반숫자, "none" 없음, "tel" 전화번호, "business" 사업자번호
    maxlength: // 키패드 필드의 최대 자릿수 제한 (숫자, 뒤의 입력을 자르기로 제한됨)
    clrfirst: // 키패드가 처음눌릴 때 모든 값을 삭제하고 들어가도록 할지의 여부를 결정
    callback: function() { // 가상키패드 동작이 다 끝나고 나서 실행될 기능
    }, 
}

defaultKeypadProp = {
    type: "default",
    midprocess: "default",
    maxlength: 9999,
    clrfirst: false,
    callback: function() {
    },
}