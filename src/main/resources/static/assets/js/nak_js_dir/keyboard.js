let VDISPLAY;
document.addEventListener('DOMContentLoaded', function() {
    VDISPLAY = document.getElementById("vdisplay");
}, false);

// 초성
const CHO = [
    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
    'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

// 쌍자음 가능성이 있는 초성
const CHO_POSS = [
    'ㄱ', 'ㄷ', 'ㅂ', 'ㅅ', 'ㅈ'
];

// 이외 완성된 초성
const CHO_COMP = [
    'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ', 'ㄴ', 'ㄹ',
    'ㅁ', 'ㅇ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

// 중성
const JUNG = [
    'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ',
    'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
];

// 미완성 가능성이 있는 중성
const JUNG_POSS = [
    'ㅗ', 'ㅜ', 'ㅡ'
];

// 이외 완성된 중성
const JUNG_COMP = [
    'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅘ',
    'ㅙ', 'ㅚ', 'ㅛ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅢ'
];

/* 중성 규칙 */
const JUNG_ORDER = [
    [ 8, 0, 9 ], [ 8, 1, 10 ], [ 8, 20, 11 ],
    [ 13, 4, 14 ], [ 13, 5, 15 ], [ 13, 20, 16 ],
    [ 18, 20, 19 ]
];

// 종성
const JONG = [
    '', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ',
    'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

// 복자음 가능성이 있는 종성
const JONG_POSS = [
    'ㄱ', 'ㄴ', 'ㄹ', 'ㅂ', 'ㅅ'
];

// 이외 완성된 종성
const JONG_COMP = [
    'ㄲ', 'ㄳ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ',
    'ㅀ', 'ㅁ', 'ㅄ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
];

/* 종성 규칙 */
const JONG_ORDER = [
    [ 1, 1, 2 ], [ 1, 19, 3 ],
    [ 4, 22, 5 ], [ 4, 27, 6 ],
    [ 8, 1, 9 ], [ 8, 16, 10 ], [ 8, 17, 11 ], [ 8, 19, 12 ], [ 8, 25, 13 ], [ 8, 26, 14 ], [ 8, 27, 15 ],
    [ 17, 19, 18 ],
    [ 19, 19, 20 ]
];

function vKeyIn(INPUT_CHAR){

    //입력값이 한글의 자음이나 모음인 경우 한글 처리 과정에 들어가고, 아닐 경우 그대로 입력된다.
    if(INPUT_CHAR.charCodeAt(0) > 12592 && INPUT_CHAR.charCodeAt(0) < 12644){
        korAlphabetProcess(INPUT_CHAR);
    }else{
        addToDisplay(INPUT_CHAR);
    }

}

async function korAlphabetProcess(INPUT_CHAR){

    const VDVALUE = VDISPLAY.value;
    /* 커서 기준으로 한칸 앞 자리의 문자 인식 */
    const LAST_CHAR = VDVALUE.substr(VDISPLAY.selectionStart-1,1);
    console.log(LAST_CHAR);
    const LAST_CHAR_CODE = LAST_CHAR.charCodeAt(0);
    const INPUT_CHAR_CODE = INPUT_CHAR.charCodeAt(0);
    const IS_COMP_KOREAN = (LAST_CHAR_CODE > 44031 && LAST_CHAR_CODE < 55204);

    // 여기는 한글자가 아닌 글자일 경우에 동작하는 부분을 추가
    const LAST_CHAR_SEQUENCE = LAST_CHAR_CODE - 44032;
    const LAST_CHO_IDX = Math.floor(LAST_CHAR_SEQUENCE / 588 );
    const LAST_JUNG_IDX = Math.floor(LAST_CHAR_SEQUENCE / 28 ) % 21;
    const LAST_JONG_IDX = LAST_CHAR_SEQUENCE % 28;
    let inputCharIdx = -1;

    /* 입력받은 값이 자음일 경우 */
    if( INPUT_CHAR_CODE < 12623 ){

        switch(true){

            case ( CHO_POSS.includes(LAST_CHAR) ) : // 기존의 값이 쌍자음 가능성이 있는 초성일 경우
                // 기존의 존재하는 자음과 입력받은 자음이 같을 경우
                if(LAST_CHAR_CODE===INPUT_CHAR_CODE){
                    await removeLastChar();
                    addToDisplay(String.fromCharCode(INPUT_CHAR_CODE+1));
                }else{
                    addToDisplay(INPUT_CHAR);
                }
                break;

            case ( CHO_COMP.includes(LAST_CHAR) ) : // 기존의 값이 완성된 자음일 경우
                addToDisplay(INPUT_CHAR);
                break;

            // 기존의 값이 변화 가능성 있는 종성을 포함한 완전한 한글일 경우
            case ( JONG_POSS.includes(JONG[LAST_JONG_IDX]) && IS_COMP_KOREAN ) :

                let JONG_IDX = alphabetComplex(JONG_ORDER, LAST_JONG_IDX, JONG.indexOf(INPUT_CHAR));
                if(JONG_IDX) {
                    await removeLastChar();
                    addToDisplay(stringComplex(LAST_CHO_IDX, LAST_JUNG_IDX, JONG_IDX));
                }else{
                    addToDisplay(INPUT_CHAR);
                }

                break;

            // 기존의 값이 중성까지 완성되어 있는 완전한 한글일 경우 (종성 값이 0일 경우)
            case ( LAST_JONG_IDX === 0 && IS_COMP_KOREAN ) :
                await removeLastChar();
                addToDisplay(stringComplex(LAST_CHO_IDX, LAST_JUNG_IDX, JONG.indexOf(INPUT_CHAR)));
                break;

            default :
                addToDisplay(INPUT_CHAR);
                break;
        }
    }

    /* 입력받은 값이 모음일 경우 */
    else {

        inputCharIdx = JUNG.indexOf(INPUT_CHAR);

        switch(true){

            // 기존의 값이 초성일 경우

            case ( CHO.includes(LAST_CHAR) ) :
                removeLastChar();
                addToDisplay(String.fromCharCode(44032+CHO.indexOf(LAST_CHAR)*588+JUNG.indexOf(INPUT_CHAR)*28));
                break;

            // 기존의 값이 완성된 중성일 경우
            case ( JUNG_COMP.includes(JUNG[LAST_JUNG_IDX]) ) :
                addToDisplay(INPUT_CHAR);
                break;

            // 기존의 값이 미완성 가능성 있는 중성을 포함한 경우
            case ( JUNG_POSS.includes(JUNG[LAST_JUNG_IDX]) ) :
                let JUNG_IDX = alphabetComplex(JUNG_ORDER, LAST_JUNG_IDX, inputCharIdx);
                if(JUNG_IDX) {
                    removeLastChar();
                    addToDisplay(stringComplex(LAST_CHO_IDX, JUNG_IDX, 0));
                }else{
                    addToDisplay(INPUT_CHAR);
                }
                break;

            default :
                addToDisplay(INPUT_CHAR);
                break;

        }
    }
}

function addToDisplay(INPUT_CHAR){

    let VDSTART = VDISPLAY.selectionStart;
    const VDEND = VDISPLAY.selectionEnd;
    const VDVALUE = VDISPLAY.value;
    VDISPLAY.value = VDVALUE.substr(0, VDSTART)+INPUT_CHAR+VDVALUE.substr(VDEND);
    VDISPLAY.focus();
    VDISPLAY.setSelectionRange(VDSTART+1,VDSTART+1);

}

function removeLastChar(){


    const VDVALUE = VDISPLAY.value;
    let VDSTART = VDISPLAY.selectionStart;
    const VDEND = VDISPLAY.selectionEnd;

    if( VDSTART === VDEND ) VDSTART--;

    VDISPLAY.value = VDVALUE.substr(0, VDSTART)+VDVALUE.substr(VDEND);
    VDISPLAY.focus();
    VDISPLAY.setSelectionRange(VDSTART,VDSTART);

}

/* 마지막 글자와 입력받은 글자의 배열 번호가 일치하는 규칙을 찾아 결과값을 리턴한다. */
function alphabetComplex (ORDER, LAST_IDX, INPUT_IDX) {

    let compCode = false;
    ORDER.forEach( element => {
        if( LAST_IDX === element[0] && INPUT_IDX === element[1] ){
            compCode = element[2];
        }
    });

    return compCode;

}

function stringComplex (CHO_IDX, JUNG_IDX, JONG_IDX) {

    let KR_UNICODE = 44032 + CHO_IDX * 588 + JUNG_IDX * 28 + JONG_IDX;
    return String.fromCharCode(KR_UNICODE);

}

/* 가상 키보드 필드의 선택한 캐럿 영역만큼 혹은 캐럿의 한칸 앞을 지우도록 한다. */
function vKeyRm () {
    const VDVALUE = VDISPLAY.value;
    let VDSTART = VDISPLAY.selectionStart;
    const VDEND = VDISPLAY.selectionEnd;

    if( VDSTART === VDEND ) VDSTART--;

    VDISPLAY.value = VDVALUE.substr(0, VDSTART)+VDVALUE.substr(VDEND);
    VDISPLAY.focus();
    VDISPLAY.setSelectionRange(VDSTART,VDSTART);
}