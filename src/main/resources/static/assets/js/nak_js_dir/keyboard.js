/*
* 이해를 위해서는 유니코드 한글 자음, 모음, 조합문자의 초성, 중성, 종성 순서에 대해 지식이 필요하다.
* */

let editableField;

/* 문서 로드 후에 객체가 탑재되고, 텍스트필드의 캐렛(커서)가 움직이는 것을 감지하기 위한 각종 이벤트 리스너를 활성화한다. */
document.addEventListener('DOMContentLoaded', function() {
    editableField = document.getElementById("editableField");

    document.getElementById("upperCase").style.visibility = "hidden";
    document.getElementById("lowerCase").style.visibility = "hidden";
}, false);

/* 캐렛의 위치가 이동했던 것을 감지하기 위해 저장해두는 곳, 포커싱할 때 해당 위치를 기준으로 한다.
* 선택 시작점은 [0]  끝점은 [1] 에 놓인다.*/
let lastCaretPos = [0, 0];

/* 캐랫의 위치와는 별개로 innerHTML 상에서 선택된 캐럿이 위치할 장소, 캐럿과 함께 편집점을 기준잡는 역할을 한다.
* 선택 시작점은 [0]  끝점은 [1] 에 놓인다. */
let lastActualCaretPos = [0, 0];

/* true = 한글 삭제시 마지막 입력자부터 분해됨, false = 한글 삭제시 글자 단위로 삭제됨 (ex. 첫글자는 분해하고 계속 한글 지우기 할 때) */
let divideOrDelete = false;

/* true = 한글 입력시 조합이 됨, false = 한글 입력시 별개로 입력됨 (ex. 한글 모음만 입력하고 커서를 이동했다가 다시 위치시킨 경우 */
let compoundOrAdd = false;

/* 받침이 쌍시옷으로 왔을 때 다음 모음 입력시 별도의 규칙이 필요하기 때문에, 해딩 상황의 임시 기억이 필요하다. */
let wasLastInputSsangsiot = false;

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

/* 쌍자음 초성 */
const INITIAL_CONSONANT_DOUBLE = [
    'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ'
]

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

const EXCEPTION_SYMBOL = [
    ' ', '&', '<', '>'
];

/* 입력시 별도의 규칙이 필요한 심벌 규칙, 심벌코드, 표시되는심벌, 심벌이 차지하는 문자의 길이이다.  */
const EXCEPTION_SYMBOL_ORDER = [
    [ ' ', '&nbsp;', 5 ], [ '&', '&amp;', 4 ], [ '<', '&lt;', 3 ], [ '>', '&gt;', 3 ]
];

// !!!!!!!!!!!!!!!!!!!!
/* 디스플레이에 담을 값을 인수와 함께 넘긴다. */
function whenClickedVirtualKeyChar(INPUT_CHAR){

    /* 입력값이 한글의 자음이나 모음인 경우 한글 처리 과정에 들어가고, 아닐 경우 그대로 입력된다. */
    if(INPUT_CHAR.charCodeAt(0) > 12592 && INPUT_CHAR.charCodeAt(0) < 12644){
        korAlphabetProcess(INPUT_CHAR);
        /* 입력시 별도의 처리가 필요한 특수문자의 경우 */
    }else if(EXCEPTION_SYMBOL.includes(INPUT_CHAR)){
        exceptionSymbolProcess(INPUT_CHAR);
    }else{
        addCharToTargetField(INPUT_CHAR);
    }
}

/* 한글 처리를 담당하는 기능 눌려진 한글 문자를 인수로 받는다. */
async function korAlphabetProcess(INPUT_CHAR){

    /* 목표 텍스트 필드 객체를 담는 곳 */
    const FIELD_VALUE = editableField.innerHTML;
    /* 커서 기준으로 한칸 앞 자리의 문자 (즉 마지막으로 입력되었다 상정할 수 있는 문자) 인식 */
    const LAST_CHAR = FIELD_VALUE.substr(lastActualCaretPos[0]-1,1);
    /* 위 문자의 유니코드 */
    const LAST_CHAR_CODE = LAST_CHAR.charCodeAt(0);
    /* 입력한 문자의 유니코드 */
    const INPUT_CHAR_CODE = INPUT_CHAR.charCodeAt(0);
    /* 기존 입력된 코드가 유니코드의 조합형 한글 범주에 드는지 확인한다. */
    const IS_COMPLETE_KOREAN = (LAST_CHAR_CODE > 44031 && LAST_CHAR_CODE < 55204);

    /* 단일 자음, 모음이 아닌 조합형 한글일 경우에 동작하는 부분일 경우 초성 중성 종성의 산술적 코드를 알아낸다. (유니코드 한글 배열 참조) */
    const LAST_CHAR_SEQUENCE = LAST_CHAR_CODE - 44032;
    const LAST_INITIAL_CONSONANT_IDX = Math.floor(LAST_CHAR_SEQUENCE / 588 );
    const LAST_VOWEL_IDX = Math.floor(LAST_CHAR_SEQUENCE / 28 ) % 21;
    const LAST_FINAL_CONSONANT_IDX = LAST_CHAR_SEQUENCE % 28;

    /* 조합된 문자를 끝에서 부터 분해시 임시 저장될 변수 */
    let divideResultChar = "";

    if( INPUT_CHAR_CODE < 12623 && compoundOrAdd ){ // 입력받은 값이 자음일 경우

        switch(true){

            //
            // case ( INITIAL_CONSONANT_POTENTIAL.includes(LAST_CHAR) ) : /* 기존의 값이 쌍자음 가능성이 있는 초성일 경우 */
            //     /* 기존의 존재하는 자음과 입력받은 자음이 같을 경우 쌍자음으로 변환하여 준다. */
            //     if(LAST_CHAR_CODE===INPUT_CHAR_CODE) {
            //         await removeCharFromTargetField("INPUT");
            //         addCharToTargetField(String.fromCharCode(INPUT_CHAR_CODE+1));
            //     }else{ // 앞의 자음과 조합 불가능한 자음일 경우 조합하지 않고 단순히 입력받은 문자를 추가.
            //         addCharToTargetField(INPUT_CHAR);
            //     }
            //     break;
            //
            // case ( INITIAL_CONSONANT_COMPLETE.includes(LAST_CHAR) ) : // 기존의 값이 완성된 자음일 경우
            //     addCharToTargetField(INPUT_CHAR);
            //     break;

            /* 기존의 종성값이 변화 가능성 있는 종성을 포함한 완전한 한글일 경우 */
            case ( FINAL_CONSONANT_POTENTIAL.includes(
                FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]) && IS_COMPLETE_KOREAN ) :

                /* 종성 규칙에 따라 조합될 수 있는 종성이면 결과값을 반환한다. */
                let FINAL_CONSONANT_IDX = makeTargetConsonantIdx(FINAL_CONSONANT_ORDER,
                    LAST_FINAL_CONSONANT_IDX, FINAL_CONSONANT.indexOf(INPUT_CHAR));

                /* 종성 규칙에 따라 조합된 종성이 오면 반영하고, 그렇지 않으면 별개의 자음이라는 뜻으므로 입력받은 값을 그대로 출력한다. */
                if(FINAL_CONSONANT_IDX) {
                    await removeCharFromTargetField("INPUT");
                    addCharToTargetField(compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                        LAST_VOWEL_IDX, FINAL_CONSONANT_IDX));
                }else{
                    addCharToTargetField(INPUT_CHAR);
                }

                break;


            /* 기존의 값이 중성까지 완성되어 있는 조합된 한글일 경우 (종성 값이 0일 경우 혹은) 종성값을 추가한 값을 출력  */
            case ( LAST_FINAL_CONSONANT_IDX === 0 && IS_COMPLETE_KOREAN
                && !INITIAL_CONSONANT_DOUBLE.includes(INPUT_CHAR) || INPUT_CHAR_CODE === 12614 ) :
                await removeCharFromTargetField("INPUT");
                addCharToTargetField(compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                    LAST_VOWEL_IDX, FINAL_CONSONANT.indexOf(INPUT_CHAR)));
                /* 마지막 받침으로 ㅆ을 사용한 경우라면 다음 모음이 올 때 ㅅ이 아닌 ㅆ과 조합된다. */
                wasLastInputSsangsiot = INPUT_CHAR_CODE === 12614;
                break;

            default :
                addCharToTargetField(INPUT_CHAR);
                break;
        }

    }else if(compoundOrAdd){ // 입력받은 값이 모음일 경우

        switch(true){

            /* 기존의 값이 초성일 경우 조합하여 출력 */
            case ( INITIAL_CONSONANT.includes(LAST_CHAR) ) :
                await removeCharFromTargetField("INPUT");
                addCharToTargetField(compoundCharacter(
                    INITIAL_CONSONANT.indexOf(LAST_CHAR), VOWEL.indexOf(INPUT_CHAR), 0));
                break;

            /* 기존의 값이 미완성 가능성 있는 중성을 포함한 경우 */
            case ( VOWEL_POTENTIAL.includes(VOWEL[LAST_VOWEL_IDX]) && LAST_FINAL_CONSONANT_IDX === 0 ) :
                /* 중성 규칙에 따라 조합될 수 있는 중성이면 결과값을 반환한다. */
                let VOWEL_IDX = makeTargetConsonantIdx(VOWEL_ORDER, LAST_VOWEL_IDX,
                    VOWEL.indexOf(INPUT_CHAR));
                if(VOWEL_IDX) { // 조합된 중성이면 초성과 중성을 조합하여 출력
                    await removeCharFromTargetField("INPUT");
                    addCharToTargetField(compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                        VOWEL_IDX, 0));
                }else{ // 아닐 경우 입력받은 문자를 별개로 출력
                    addCharToTargetField(INPUT_CHAR);
                }
                break;

            /* 기존의 값의 종성이 복자음으로 구성되어 있는 경우 */
            case ( FINAL_CONSONANT_DOUBLE.includes(FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]) ) :

                /* 모음이 올 경우 종성의 두 자는 앞과 뒤로 찢어지는데 앞부분의 인덱스를 담을 곳 */
                let tempConsonantIdx = "";
                let secondChar = "";

                /* 마지막 받침입력이 ㅆ 이었다면 특별 규칙을 적용해 이전 글자는 받침이 사라지고, 새 글자에는 모음이 ㅆ이 된다. */
                if(wasLastInputSsangsiot) {
                    /* 찢어진 앞의 문자 */
                    divideResultChar = compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                        LAST_VOWEL_IDX, 0);
                    /* 찢어진 뒤의 문자 */
                    secondChar = compoundCharacter(
                        10, VOWEL.indexOf(INPUT_CHAR), 0);
                }else{
                    /* 종성의 앞부분을 분리해내기 */
                    FINAL_CONSONANT_ORDER.forEach(element => {
                        if (element[2] === LAST_FINAL_CONSONANT_IDX) {
                            /* 찢어진 앞의 문자 */
                            divideResultChar = compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                                LAST_VOWEL_IDX, element[0]);
                            tempConsonantIdx = INITIAL_CONSONANT.indexOf(FINAL_CONSONANT[element[1]]);
                        }
                    });
                    /* 찢어진 뒤의 문자 */
                    secondChar = compoundCharacter(tempConsonantIdx, VOWEL.indexOf(INPUT_CHAR), 0);
                }

                await  removeCharFromTargetField("INPUT");

                /* addCaretPosition 을 1로 준 이유는 두자가 추가되어 캐랫 위치를 한칸 더 뒤로 당길 필요가 있기 때문 */
                addCharToTargetField(divideResultChar + secondChar, 1, 2 );
                break;

            /* 기존의 값이 종성까지 포함하고 있는 경우 */
            case (LAST_FINAL_CONSONANT_IDX > 0) :

                /* 찢어진 앞부분의 문자 */
                divideResultChar = compoundCharacter(
                    LAST_INITIAL_CONSONANT_IDX, LAST_VOWEL_IDX, 0);
                /* 찢어진 뒷부분의 문자 */
                const NEW_COMPOUND_CHAR = compoundCharacter(INITIAL_CONSONANT.indexOf(
                    FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]), VOWEL.indexOf(INPUT_CHAR), 0);
                await removeCharFromTargetField("INPUT");

                /* addCaretPosition 을 1로 준 이유는 두자가 추가되어 캐랫 위치를 한칸 더 뒤로 당길 필요가 있기 때문 */
                addCharToTargetField(divideResultChar + NEW_COMPOUND_CHAR, 1, 2);
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

function exceptionSymbolProcess(INPUT_CHAR) {
    let actualCaretLength = 1;
    EXCEPTION_SYMBOL_ORDER.forEach( element => {
        if( element[0] == INPUT_CHAR ) {
            INPUT_CHAR = element[1];
            lastActualCaretPos[0] += element[2];
            lastActualCaretPos[1] += element[2];
        }
    });
    addCharToTargetField(INPUT_CHAR, 0, actualCaretLength);
}

/* 목표 텍스트 필드에 인수로 받은 문자를 추가, addCaretPosition 을 설정할 경우 입력후의 캐랫 위치를 임의로 설정할 수 있다. */
function addCharToTargetField(INPUT_CHAR, addCaretPosition = 0, actualCaretLength = 1){

    /* 사용자가 직접 캐럿의 위치를 선택한 경우에만 필요하다. */
    /*
    lastCaretPos = getCaretPosition();
    */

    const FIELD_VALUE = editableField.innerHTML;
    const HEAD_WORDS = FIELD_VALUE.substr(0, lastActualCaretPos[0]);
    const TAIL_WORDS = FIELD_VALUE.substr(lastActualCaretPos[1]);
    const SCROLL_TO = (HEAD_WORDS + INPUT_CHAR).inPixels(300);

    /* 캐랫이 있던 위치에 맞추어 입력시킬 문자를 끼워 출력시킨다. */
    editableField.innerHTML = HEAD_WORDS + INPUT_CHAR + TAIL_WORDS;
    setCaretPosition(lastCaretPos[0]+1+addCaretPosition, lastCaretPos[0]+1+addCaretPosition);
    lastActualCaretPos[0] += actualCaretLength;
    lastActualCaretPos[1] += actualCaretLength;
    editableField.scrollLeft = SCROLL_TO;
}

/* 캐랫 위치를 반환한다 배열 0번은 시작 포지션, 배열 1번은 종료 포지션 */
function getCaretPosition() {
    editableField.focus();
    const CARET_SELECTION = window.getSelection();
    let editableFieldCaretStartPosition = CARET_SELECTION.focusOffset;
    let editableFieldCaretEndPosition = CARET_SELECTION.anchorOffset;
    if(editableFieldCaretStartPosition > editableFieldCaretEndPosition) {
        const TEMP_POSITION = editableFieldCaretStartPosition;
        editableFieldCaretStartPosition = editableFieldCaretEndPosition;
        editableFieldCaretEndPosition = TEMP_POSITION;
    }
    return [editableFieldCaretStartPosition, editableFieldCaretEndPosition];
}

/* editableField의 캐랫 위치를 설정한다. */  // 실제 캐랫의 위치를 계산해주는 작업도 필요하다.
function setCaretPosition(start, end) {
    if(editableField.innerHTML) {
        editableField.focus();
        const CARET_SELECTION = window.getSelection();
        const NEW_RANGE = CARET_SELECTION.getRangeAt(0);
        NEW_RANGE.setStart(editableField.childNodes[0], start);
        NEW_RANGE.setEnd(editableField.childNodes[0], end);
        const selection = document.getSelection();
        selection.removeAllRanges();
        selection.addRange(NEW_RANGE);
        lastCaretPos = [start, end];
    }
}

/* 사용자가 editableField 바깥쪽을 누르면 포커스가 해지되는데, 그 순간 editableField 의 캐랫 위치 정보는 상실된다.
* 이 함수를 사용해서 포커스를 해줘야 원하던 곳의 캐럿을 마지막 존재하던 위치에 설정할 수 있다. */
function focusWithCaret() {
    setCaretPosition(lastCaretPos[0], lastCaretPos[1]);
}

String.prototype.inPixels = function (fieldWidth){

    const span = document.createElement("span");
    span.innerHTML = this.valueOf();
    Object.assign(span.style, {
        position: 'absolute',
        visibility: 'hidden',
        fontSize: '30px'
    });
    document.querySelector('html').prepend(span);
    let resultPixel = Math.max(span.getBoundingClientRect().width)-fieldWidth/2;
    resultPixel = resultPixel < 0 ? 0 : resultPixel;

    return resultPixel;
}

/* 주어진 규칙에 따라 마지막 글자와 입력받은 글자의 배열 번호가 일치하는 해당 규칙의 IDX를 찾아 결과값을 리턴한다. */
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

/* 목표 텍스트 필드의 문자를 지우거나, 입력시 조합되는 문자의 필요없어지는 기존 부분을 지우는 데에도 쓰인다.
*  가상 키보드 필드의 선택한 캐랫 영역만큼 혹은 캐랫의 한칸 앞을 지우도록 한다.
*  role : DELETE 기본값  INPUT 입력시
* */  // 지울 때 실제 캐럿 위치의 변동량도 신경써줘야 함.
function removeCharFromTargetField (role = "DELETE") {

    /* 가상 키보드 입력삭제 대상이 될 목표 텍스트 필드의 값을 담는다 */
    const FIELD_VALUE = editableField.innerHTML;

    /* 삭제 작업이 일어나고 나서 최종적으로 캐랫이 위치하게 될 포지션 */
    let targetFieldCaretSetPosition = lastCaretPos[0];

    /* 임시 */
    let stepBackLastActualCaretPos = 0;
    /* 실제로 삭제되게 될 innerHTML 의 길이설정 */
    let actualDeleteLength = 1;

    /* 한글조합을 분해하며 지우게 될 경우 분해 대상이 될 문자 */
    const TARGET_CHAR = FIELD_VALUE.substr(lastActualCaretPos[0] - 1, 1);
    /* 위 분해 대상이 될 문자의 유니코드 */
    const TARGET_CHAR_CODE = TARGET_CHAR.charCodeAt(0);

    /* 조합된 문자를 끝에서 부터 분해시 임시 저장될 변수 */
    let divideResultChar = "";

    const IS_SINGLE_CARET = lastCaretPos[0] === lastCaretPos[1];

    /* 인수로 전달받은 역할에 따라 나뉜다. */
    switch (role) {

        /*
        * 입력 작업시 문자가 조합될 때 기존의 문자를 없애고 조합된 문자를 남기기 위해 입력 문자열을 자를 캐랫의 시작위치 조정이 필요
        * 캐랫의 시작과 끝 위치가 같으면 캐랫이 단일 칸에 머물러 깜빡이고 있음을 의미하고, 다르면 문자열의 영역이 선택된 것임을 의미한다.
        * */
        case "INPUT" :

            if(TARGET_CHAR == ";" & IS_SINGLE_CARET) {
                lastActualCaretPos[0] -= exceptionSymbolLength(
                    FIELD_VALUE.substr(lastActualCaretPos[0], -6));
            }else if(IS_SINGLE_CARET) {
                targetFieldCaretSetPosition = --lastCaretPos[0];
                lastActualCaretPos[0]--;
            }
            break;

        /* 삭제 동작을 할 경우 동작이 좀 더 복잡해 진다. */
        case "DELETE":

            /* 캐랫이 단일위치에서 깜빡이고, 조합된 한글이며, 직전까지 한글이 입력중이던 상태여서 한글조합의 분리가 필요한 상황임을 감지 */
            if (IS_SINGLE_CARET && editableField.innerHTML) {
                if (TARGET_CHAR_CODE > 44031 && TARGET_CHAR_CODE < 55204 && divideOrDelete) {
                    /* 분해 대상이 될 문자의 유니코드의 초성, 중성, 종성의 IDX를 구한다. */
                    const TARGET_CHAR_SEQUENCE = TARGET_CHAR_CODE - 44032;
                    const TARGET_INITIAL_CONSONANT_IDX = Math.floor(TARGET_CHAR_SEQUENCE / 588);
                    const TARGET_VOWEL_IDX = Math.floor(TARGET_CHAR_SEQUENCE / 28) % 21;
                    const TARGET_FINAL_CONSONANT_IDX = TARGET_CHAR_SEQUENCE % 28;

                    lastActualCaretPos[0]--;
                    switch (true) {

                        /* 분해할 문자의 종성이 존재할 때 */
                        case (TARGET_FINAL_CONSONANT_IDX > 0) :
                            /* 종성이 두자의 자음 조합으로 되어있을 때 앞자리의 자음만 남긴다. */
                            FINAL_CONSONANT_ORDER.forEach(element => {
                                if (element[2] === TARGET_FINAL_CONSONANT_IDX) {
                                    divideResultChar = compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                        TARGET_VOWEL_IDX, element[0]);
                                }
                            });
                            /* 종성이 한자의 자음일 경우 종성을 날린다. */
                            if(!divideResultChar){
                                divideResultChar = compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                    TARGET_VOWEL_IDX, 0);
                            }
                            break;

                        /* 중성이 존재할 경우 */
                        case (TARGET_VOWEL_IDX > -1) :
                            /* 중성이 두자의 모음 조합으로 되어있을 때 먼저입력되는 모음만 남긴다. */
                            VOWEL_ORDER.forEach(element => {
                                if (element[2] === TARGET_VOWEL_IDX) {
                                    divideResultChar = compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                        element[0], 0);
                                }
                            });

                            /* 중성이 한자의 모음 조합으로 되어있을 때, 자음부만 남긴다. */
                            divideResultChar = (divideResultChar === "")
                                ? INITIAL_CONSONANT[TARGET_INITIAL_CONSONANT_IDX] : divideResultChar;
                            break;
                    }
                    stepBackLastActualCaretPos = 1;
                }else if(IS_SINGLE_CARET && FIELD_VALUE.substr(lastActualCaretPos[0] - 1, 1) == ";"){
                    targetFieldCaretSetPosition = --lastCaretPos[0];
                    lastActualCaretPos[0] -=
                        exceptionSymbolLength(FIELD_VALUE.substr(lastActualCaretPos[0]-6, 6))+1;
                }else{
                    /* 한 글자를 완전히 지우기 위해서는 자를 위치를 한칸 당기고, 캐랫의 포지션은 한칸 더 앞으로 이동해야 한다. */
                    targetFieldCaretSetPosition = --lastCaretPos[0];
                    lastActualCaretPos[0]--;

                    /* 지울 대상이 쌍자음일 경우, 단자음으로 바꾸어 준다. */
                    if(INITIAL_CONSONANT_DOUBLE.includes(TARGET_CHAR)){
                        divideResultChar = String.fromCharCode(TARGET_CHAR_CODE-1);
                        targetFieldCaretSetPosition++;
                        lastActualCaretPos[0]++;
                    }else{
                        /* 한글이 아니거나 한글 낱자를 지웠으므로 이 다음 삭제 대상은 마지막 입력하던 글자가 아니다. 그러므로..... */
                        divideOrDelete = false;
                        compoundOrAdd = false;
                    }
                }
            }else if(IS_SINGLE_CARET) {
                targetFieldCaretSetPosition--;
                lastActualCaretPos[0]--;
            }
            focusWithCaret();
            break;
    }


    /* 위에서 적용된 캐랫을 기준으로 문자의 앞 부분과 뒷 부분을 자르고, 그 양측의 문자열 사이에 추가될 결과문자를(있을경우) 집어넣는다. */
    editableField.innerHTML = FIELD_VALUE.substr(0, lastActualCaretPos[0])
        + divideResultChar + FIELD_VALUE.substr(lastActualCaretPos[1]);
    /* 위 텍스트 필드의 문자열 치환 작업을 하게 되면 다시 텍스트 필드에 포커스를 주고 캐랫의 위치를 지정해 주어야 한다. */
    setCaretPosition(targetFieldCaretSetPosition, targetFieldCaretSetPosition);
    lastActualCaretPos[0] += stepBackLastActualCaretPos;
    lastActualCaretPos[1] = lastActualCaretPos[0];
    editableField.focus();
}

/* 삭제작업이나 ...에서 심벌 표현 문자열의 길이를 추정하여 반환  */
function exceptionSymbolLength (assumeSymbolCode) {

    let resultLength = 1;
    EXCEPTION_SYMBOL_ORDER.forEach( element => {
        if(assumeSymbolCode.includes(element[1])){
            resultLength = element[2];
        }

    });
    return resultLength;
}

/* 입력 필드에서 키보드 방향키의 역할을 함 */  //추가로 실제 캐럿의 위치의 변동도 필요하다.
function moveCaretPosition (direction) {

    const IS_SINGLE_CARET = lastCaretPos[0] === lastCaretPos[1];
    const FIELD_VALUE = editableField.innerHTML;

    /* 입력 필드 시작과 종료 포지션이 같다면 일반적인 방향키의 움직임을 구현 */
    if(IS_SINGLE_CARET) {
        let examChar;
        const FIELD_LENGTH = FIELD_VALUE.length;
        switch (direction){
            case "right" :
                if (lastActualCaretPos[0] < FIELD_LENGTH) {
                    lastCaretPos[0]++;
                    lastCaretPos[1]++;
                    lastActualCaretPos[0]++;
                    lastActualCaretPos[1]++;

                    examChar = FIELD_VALUE.substr(lastActualCaretPos[0]-1, 1);
                    if(examChar == "&") {
                        lastActualCaretPos[0] += exceptionSymbolLength(
                            FIELD_VALUE.substr(lastActualCaretPos[0]-1, 6));
                        lastActualCaretPos[1] = lastActualCaretPos[0];
                    }
                }
                break;
            case "left" :
                if (lastActualCaretPos[0] > 0) {
                    lastCaretPos[0]--;
                    lastCaretPos[1]--;
                    lastActualCaretPos[0]--;
                    lastActualCaretPos[1]--;

                    examChar = FIELD_VALUE.substr(lastActualCaretPos[0], 1);
                    if(examChar == ";") {
                        lastActualCaretPos[0] -= exceptionSymbolLength(
                            FIELD_VALUE.substr(lastActualCaretPos[0]-5, 6))
                        lastActualCaretPos[1] = lastActualCaretPos[0];
                    }
                }
                break;
        }
    }else{ // 캐랫의 시작과 종료 위치가 다르면 선택된 구간이 있다는 뜻 이므로 최종 캐랫의 포지션은 상대적이다.
        switch (direction){ // 범위내의 특수문자들의 길이까지 고려해주는 기능과 적용이 필요함.
            case "right" :
                lastCaretPos[0] = lastCaretPos[1];
                lastActualCaretPos[0] = lastActualCaretPos[1];
                break;
            case "left" :
                lastCaretPos[1] = lastCaretPos[0];
                lastActualCaretPos[1] = lastActualCaretPos[0];
                break;
        }
    }

    wasLastInputSsangsiot = false;
    divideOrDelete = false;
    compoundOrAdd = false;
    focusWithCaret();
}

function changeKeyboard(type){
    const FLIP = document.getElementsByClassName("flip");
    switch (type) {
        case 1 :
            for (const element of FLIP) {
                element.style.visibility = "hidden";
            }
            document.getElementById("kor").style.visibility = "visible";
            break;
        case 2 :
            for (const element of FLIP) {
                element.style.visibility = "hidden";
            }
            document.getElementById("upperCase").style.visibility = "visible";
            break;
        case 3 :
            for (const element of FLIP) {
                element.style.visibility = "hidden";
            }
            document.getElementById("lowerCase").style.visibility = "visible";
            break;
    }
}
