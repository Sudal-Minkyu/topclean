let targetInputField;

document.addEventListener('DOMContentLoaded', function() {
    targetInputField = document.getElementById("targetInputField");

    targetInputField.addEventListener('keyup', checkCaret);
    targetInputField.addEventListener('mousedown', checkCaret);
    targetInputField.addEventListener('touchstart', checkCaret);
    targetInputField.addEventListener('selectstart', checkCaret);

}, false);


let caretPos = 0;

/* true = 한글 삭제시 마지막 입력자부터 분해됨, false = 한글 삭제시 글자 단위로 삭제됨 (ex. 첫글자는 분해하고 계속 한글 지우기 할 때) */
let divideOrDelete = false;

/* true = 한글 입력시 조합이 됨, false = 한글 입력시 별개로 입력됨 (ex. 한글 모음만 입력하고 커서를 이동했다가 다시 위치시킨 경우 */
let compoundOrAdd = false;

/* 초성 */
const INITIAL_CONSONANT = [
    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
    'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

/* 쌍자음 가능성이 있는 초성 */
const INITIAL_CONSONANT_POTENTIAL = [
    'ㄱ', 'ㄷ', 'ㅂ', 'ㅅ', 'ㅈ'
];

/* 이외 완성된 초성 */
const INITIAL_CONSONANT_COMPLETE = [
    'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ', 'ㄴ', 'ㄹ',
    'ㅁ', 'ㅇ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

/* 중성 */
const VOWEL = [
    'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ',
    'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
];

/* 미완성 가능성이 있는 중성 */
const VOWEL_POTENTIAL = [
    'ㅗ', 'ㅜ', 'ㅡ'
];

/* 이외 완성된 중성 */
const VOWEL_COMPLETE = [
    'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅘ',
    'ㅙ', 'ㅚ', 'ㅛ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅢ'
];

/* 중성 규칙 */
const VOWEL_ORDER = [
    [ 8, 0, 9 ], [ 8, 1, 10 ], [ 8, 20, 11 ],
    [ 13, 4, 14 ], [ 13, 5, 15 ], [ 13, 20, 16 ],
    [ 18, 20, 19 ]
];

/* 종성 */
const FINAL_CONSONANT = [
    '', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ',
    'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

/* 복자음 가능성이 있는 종성 */
const FINAL_CONSONANT_POTENTIAL = [
    'ㄱ', 'ㄴ', 'ㄹ', 'ㅂ', 'ㅅ'
];


/* 이외 완성된 종성 */
const FINAL_CONSONANT_COMPLETE = [
    'ㄲ', 'ㄳ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ',
    'ㅀ', 'ㅁ', 'ㅄ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

/* 종성 규칙 */
const FINAL_CONSONANT_ORDER = [
    [ 1, 1, 2 ], [ 1, 19, 3 ],
    [ 4, 22, 5 ], [ 4, 27, 6 ],
    [ 8, 1, 9 ], [ 8, 16, 10 ], [ 8, 17, 11 ], [ 8, 19, 12 ], [ 8, 25, 13 ], [ 8, 26, 14 ], [ 8, 27, 15 ],
    [ 17, 19, 18 ],
    [ 19, 19, 20 ]
];

/* 복자음으로 구성된 종성 IDX */
const FINAL_CONSONANT_DOUBLE = [
    'ㄲ', 'ㄳ', 'ㄵ', 'ㄶ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅄ', 'ㅆ'
];

function checkCaret() {
    const NEW_POS = targetInputField.selectionStart;
    if (NEW_POS !== caretPos) {
        divideOrDelete = false;
        compoundOrAdd = false;
        caretPos = NEW_POS;
    }
}

function whenClickedVirtualKeyChar(INPUT_CHAR){

    /* 입력값이 한글의 자음이나 모음인 경우 한글 처리 과정에 들어가고, 아닐 경우 그대로 입력된다. */
    if(INPUT_CHAR.charCodeAt(0) > 12592 && INPUT_CHAR.charCodeAt(0) < 12644){
        korAlphabetProcess(INPUT_CHAR);
    }else{
        addCharToTargetField(INPUT_CHAR);
    }

}

async function korAlphabetProcess(INPUT_CHAR){

    const TARGET_FIELD_VALUE = targetInputField.value;
    /* 커서 기준으로 한칸 앞 자리의 문자 (즉 마지막으로 입력되었다 상정할 수 있는 문자) 인식 */
    const LAST_CHAR = TARGET_FIELD_VALUE.substr(targetInputField.selectionStart-1,1);
    const LAST_CHAR_CODE = LAST_CHAR.charCodeAt(0);
    const INPUT_CHAR_CODE = INPUT_CHAR.charCodeAt(0);
    const IS_COMPLETE_KOREAN = (LAST_CHAR_CODE > 44031 && LAST_CHAR_CODE < 55204);

    /* 여기는 한글자가 아닌 글자일 경우에 동작하는 부분을 추가 */
    const LAST_CHAR_SEQUENCE = LAST_CHAR_CODE - 44032;
    const LAST_INITIAL_CONSONANT_IDX = Math.floor(LAST_CHAR_SEQUENCE / 588 );
    const LAST_VOWEL_IDX = Math.floor(LAST_CHAR_SEQUENCE / 28 ) % 21;
    const LAST_FINAL_CONSONANT_IDX = LAST_CHAR_SEQUENCE % 28;
    let divideResultCHAR = "";

    if( INPUT_CHAR_CODE < 12623 && compoundOrAdd ){ // 입력받은 값이 자음일 경우

        switch(true){

            case ( INITIAL_CONSONANT_POTENTIAL.includes(LAST_CHAR) ) : /* 기존의 값이 쌍자음 가능성이 있는 초성일 경우 */
                /* 기존의 존재하는 자음과 입력받은 자음이 같을 경우 */
                if(LAST_CHAR_CODE===INPUT_CHAR_CODE){
                    await removeCharFromTargetField("INPUT");
                    addCharToTargetField(String.fromCharCode(INPUT_CHAR_CODE+1));
                }else{
                    addCharToTargetField(INPUT_CHAR);
                }
                break;

            case ( INITIAL_CONSONANT_COMPLETE.includes(LAST_CHAR) ) : /* 기존의 값이 완성된 자음일 경우 */
                addCharToTargetField(INPUT_CHAR);
                break;

            /* 기존의 값이 변화 가능성 있는 종성을 포함한 완전한 한글일 경우 */
            case ( FINAL_CONSONANT_POTENTIAL.includes(
                FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]) && IS_COMPLETE_KOREAN ) :

                let FINAL_CONSONANT_IDX = makeTargetConsonantIdx(FINAL_CONSONANT_ORDER,
                    LAST_FINAL_CONSONANT_IDX, FINAL_CONSONANT.indexOf(INPUT_CHAR));
                if(FINAL_CONSONANT_IDX) {
                    await removeCharFromTargetField("INPUT");
                    addCharToTargetField(compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                        LAST_VOWEL_IDX, FINAL_CONSONANT_IDX));
                }else{
                    addCharToTargetField(INPUT_CHAR);
                }

                break;

            // 기존의 값이 중성까지 완성되어 있는 완전한 한글일 경우 (종성 값이 0일 경우)
            case ( LAST_FINAL_CONSONANT_IDX === 0 && IS_COMPLETE_KOREAN ) :
                await removeCharFromTargetField("INPUT");
                addCharToTargetField(compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                    LAST_VOWEL_IDX, FINAL_CONSONANT.indexOf(INPUT_CHAR)));
                break;

            default :
                addCharToTargetField(INPUT_CHAR);
                break;
        }

    }else if(compoundOrAdd){ // 입력받은 값이 모음일 경우

        switch(true){

            // 기존의 값이 초성일 경우

            case ( INITIAL_CONSONANT.includes(LAST_CHAR) ) :
                await removeCharFromTargetField("INPUT");
                addCharToTargetField(String.fromCharCode(44032 + INITIAL_CONSONANT.indexOf(LAST_CHAR) * 588
                    + VOWEL.indexOf(INPUT_CHAR)*28));
                break;

            // 기존의 값이 미완성 가능성 있는 중성을 포함한 경우
            case ( VOWEL_POTENTIAL.includes(VOWEL[LAST_VOWEL_IDX]) && LAST_FINAL_CONSONANT_IDX === 0 ) :
                console.log(LAST_FINAL_CONSONANT_IDX);
                let VOWEL_IDX = makeTargetConsonantIdx(VOWEL_ORDER, LAST_VOWEL_IDX,
                    VOWEL.indexOf(INPUT_CHAR));
                if(VOWEL_IDX) {
                    await removeCharFromTargetField("INPUT");
                    addCharToTargetField(compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                        VOWEL_IDX, 0));
                }else{
                    addCharToTargetField(INPUT_CHAR);
                }
                break;

            /* 기존의 값의 종성이 복자음으로 구성되어 있는 경우 */
            case ( FINAL_CONSONANT_DOUBLE.includes(FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]) ) :

                let tempConsonants = "";
                FINAL_CONSONANT_ORDER.forEach(element => {
                    if (element[2] === LAST_FINAL_CONSONANT_IDX) {
                        divideResultCHAR = compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                            LAST_VOWEL_IDX, element[0]);
                        tempConsonants = INITIAL_CONSONANT.indexOf(FINAL_CONSONANT[element[1]]);
                    }
                });
                await  removeCharFromTargetField("INPUT");
                addCharToTargetField(divideResultCHAR
                    + String.fromCharCode(44032 + tempConsonants * 588
                        + VOWEL.indexOf(INPUT_CHAR)*28), 1 );
                break;

            case (LAST_FINAL_CONSONANT_IDX > 0) :
                console.log(LAST_FINAL_CONSONANT_IDX);
                divideResultCHAR = compoundCharacter(LAST_INITIAL_CONSONANT_IDX, LAST_VOWEL_IDX, 0);
                const NEW_COMPOUND_CHAR = compoundCharacter(INITIAL_CONSONANT.indexOf(
                    FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]), VOWEL.indexOf(INPUT_CHAR), 0);
                await removeCharFromTargetField("INPUT");
                addCharToTargetField(divideResultCHAR + NEW_COMPOUND_CHAR, 1);
                break;


            default :
                addCharToTargetField(INPUT_CHAR);
                break;
        }
    }else{ // 글자 조합이 아닌 글자 추가 플래그로 세팅되어 있는 경우 (모음한자를 입력하고 다른곳에 커서를 놓았다 다시 왔다거나.....)
        addCharToTargetField(INPUT_CHAR);
    }
    /* 한글이 입력된 상태이므로 이후 입력이나 삭제시에는 조합되어 입력되거나, 분리되어 하나씩 삭제되는 플래그를 on 시켜주어야 한다. */
    divideOrDelete = true;
    compoundOrAdd = true;
}

function addCharToTargetField(INPUT_CHAR, addCaretPosition = 0){
    let targetFieldCaretStartPosition = targetInputField.selectionStart;
    const targetFieldCaretEndPosition = targetInputField.selectionEnd;
    const TARGET_FIELD_VALUE = targetInputField.value;
    targetInputField.value = TARGET_FIELD_VALUE.substr(0, targetFieldCaretStartPosition)
        + INPUT_CHAR+TARGET_FIELD_VALUE.substr(targetFieldCaretEndPosition);
    targetInputField.focus();
    targetInputField.setSelectionRange(targetFieldCaretStartPosition + addCaretPosition + 1,
        targetFieldCaretStartPosition + addCaretPosition + 1);
}

/* 마지막 글자와 입력받은 글자의 배열 번호가 일치하는 규칙을 찾아 결과값을 리턴한다. */
function makeTargetConsonantIdx (ORDER, LAST_IDX, INPUT_IDX) {
    let resultCode = false;
    ORDER.forEach( element => {
        if( LAST_IDX === element[0] && INPUT_IDX === element[1] ){
            resultCode = element[2];
        }
    });
    return resultCode;
}

/* 초성, 중성, 종성 코드를 이용해 결과 조합 한글을 리턴한다. */
function compoundCharacter (INITIAL_CONSONANT_IDX, VOWEL_IDX, FINAL_CONSONANT_IDX) {
    let KR_UNICODE = 44032 + INITIAL_CONSONANT_IDX * 588 + VOWEL_IDX * 28 + FINAL_CONSONANT_IDX;
    return String.fromCharCode(KR_UNICODE);
}

/* 가상 키보드 필드의 선택한 캐럿 영역만큼 혹은 캐럿의 한칸 앞을 지우도록 한다. */
function removeCharFromTargetField (role = "DELETE") {

    const TARGET_FIELD_VALUE = targetInputField.value;
    let targetFieldCaretStartPosition = targetInputField.selectionStart;
    let targetFieldCaretSetPosition = targetFieldCaretStartPosition;
    const TARGET_FIELD_CARET_END_POSITION = targetInputField.selectionEnd;
    const TARGET_CHAR = TARGET_FIELD_VALUE.substr(targetFieldCaretStartPosition - 1, 1);
    const TARGET_CHAR_CODE = TARGET_CHAR.charCodeAt(0);
    let divideResultCHAR = "";

    switch (role) {

        case "INPUT" :
            if (targetFieldCaretStartPosition === TARGET_FIELD_CARET_END_POSITION) {
                targetFieldCaretSetPosition = --targetFieldCaretStartPosition;
            }
            break;

        case "DELETE":
            if (targetFieldCaretStartPosition === TARGET_FIELD_CARET_END_POSITION) {
                if (TARGET_CHAR_CODE > 44031 && TARGET_CHAR_CODE < 55204 && divideOrDelete) {
                    const TARGET_CHAR_SEQUENCE = TARGET_CHAR_CODE - 44032;
                    const TARGET_INITIAL_CONSONANT_IDX = Math.floor(TARGET_CHAR_SEQUENCE / 588);
                    const TARGET_VOWEL_IDX = Math.floor(TARGET_CHAR_SEQUENCE / 28) % 21;
                    const TARGET_FINAL_CONSONANT_IDX = TARGET_CHAR_SEQUENCE % 28;

                    targetFieldCaretStartPosition--;
                    switch (true) {

                        case (TARGET_FINAL_CONSONANT_IDX > 0) :
                            divideResultCHAR = compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                TARGET_VOWEL_IDX, 0);
                            FINAL_CONSONANT_ORDER.forEach(element => {
                                if (element[2] === TARGET_FINAL_CONSONANT_IDX) {
                                    divideResultCHAR = compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                        TARGET_VOWEL_IDX, element[0]);
                                }
                            });
                            break;

                        case (TARGET_VOWEL_IDX > -1) :
                            VOWEL_ORDER.forEach(element => {
                                if (element[2] === TARGET_VOWEL_IDX) {
                                    divideResultCHAR = compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                        element[0], 0);
                                }
                            });
                            divideResultCHAR = (divideResultCHAR === "")
                                ? INITIAL_CONSONANT[TARGET_INITIAL_CONSONANT_IDX] : divideResultCHAR;
                            break;
                    }
                }else{

                    // 한글이 아니거나 한글 낱자를 지웠으므로 이 다음 삭제 대상은 마지막 입력하던 글자가 아니다. 그러므로.....
                    divideOrDelete = false;
                    compoundOrAdd = false;
                    targetFieldCaretSetPosition = --targetFieldCaretStartPosition;
                }
            }
            break;
    }

    targetInputField.value = TARGET_FIELD_VALUE.substr(0, targetFieldCaretStartPosition)
        + divideResultCHAR + TARGET_FIELD_VALUE.substr(TARGET_FIELD_CARET_END_POSITION);
    targetInputField.focus();
    targetInputField.setSelectionRange(targetFieldCaretSetPosition,targetFieldCaretSetPosition);

}