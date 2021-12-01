/*
* 이해를 위해서는 유니코드 한글 자음, 모음, 조합문자의 초성, 중성, 종성 순서에 대해 지식이 필요하다.
* */
class VKeyboardJK {

    /* 입력의 편집 대상이 되는 필드 */
    targetField;

    /* 캐렛의 위치가 이동했던 것을 감지하기 위해 저장해두는 곳, 포커싱할 때 해당 위치를 기준으로 한다.
    * 선택 시작점은 [0]  끝점은 [1] 에 놓인다.*/
    lastCaretPos = [0, 0];

    /* 캐랫의 위치와는 별개로 innerHTML 상에서 선택된 캐럿이 위치할 장소, 캐럿과 함께 편집점을 기준잡는 역할을 한다.
    * 선택 시작점은 [0]  끝점은 [1] 에 놓인다. */
    lastActualCaretPos = [0, 0];

    /* true = 한글 삭제시 마지막 입력자부터 분해됨, false = 한글 삭제시 글자 단위로 삭제됨 (ex. 첫글자는 분해하고 계속 한글 지우기 할 때) */
    divideOrDelete = false;

    /* true = 한글 입력시 조합이 됨, false = 한글 입력시 별개로 입력됨 (ex. 한글 모음만 입력하고 커서를 이동했다가 다시 위치시킨 경우 */
    compoundOrAdd = false;

    /* 받침이 쌍시옷으로 왔을 때 다음 모음 입력시 별도의 규칙이 필요하기 때문에, 해딩 상황의 임시 기억이 필요하다. */
    wasLastInputSsangsiot = false;

    /* 초성 */
    INITIAL_CONSONANT = [
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    ];

    /* 쌍자음 가능성이 있는 초성 */
    INITIAL_CONSONANT_POTENTIAL = [
        'ㄱ', 'ㄷ', 'ㅂ', 'ㅅ', 'ㅈ'
    ];

    /* 이외 완성된 초성 */
    INITIAL_CONSONANT_COMPLETE = [
        'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ', 'ㄴ', 'ㄹ',
        'ㅁ', 'ㅇ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    ];

    /* 쌍자음 초성 */
    INITIAL_CONSONANT_DOUBLE = [
        'ㄲ', 'ㄸ', 'ㅃ', 'ㅆ', 'ㅉ'
    ]

    /* 중성 */
    VOWEL = [
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ',
        'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    ];

    /* 미완성 가능성이 있는 중성 */
    VOWEL_POTENTIAL = [
        'ㅗ', 'ㅜ', 'ㅡ'
    ];

    /* 이외 완성된 중성 */
    VOWEL_COMPLETE = [
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅘ',
        'ㅙ', 'ㅚ', 'ㅛ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅢ'
    ];

    /* 중성 규칙 */
    VOWEL_ORDER = [
        [ 8, 0, 9 ], [ 8, 1, 10 ], [ 8, 20, 11 ],
        [ 13, 4, 14 ], [ 13, 5, 15 ], [ 13, 20, 16 ],
        [ 18, 20, 19 ]
    ];

    /* 종성 */
    FINAL_CONSONANT = [
        '', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ',
        'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    ];

    /* 복자음 가능성이 있는 종성 */
    FINAL_CONSONANT_POTENTIAL = [
        'ㄱ', 'ㄴ', 'ㄹ', 'ㅂ', 'ㅅ'
    ];


    /* 이외 완성된 종성 */
    FINAL_CONSONANT_COMPLETE = [
        'ㄲ', 'ㄳ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ',
        'ㅀ', 'ㅁ', 'ㅄ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    ];

    /* 종성 규칙 */
    FINAL_CONSONANT_ORDER = [
        [ 1, 1, 2 ], [ 1, 19, 3 ],
        [ 4, 22, 5 ], [ 4, 27, 6 ],
        [ 8, 1, 9 ], [ 8, 16, 10 ], [ 8, 17, 11 ], [ 8, 19, 12 ], [ 8, 25, 13 ], [ 8, 26, 14 ], [ 8, 27, 15 ],
        [ 17, 19, 18 ],
        [ 19, 19, 20 ]
    ];

    /* 복자음으로 구성된 종성 IDX */
    FINAL_CONSONANT_DOUBLE = [
        'ㄲ', 'ㄳ', 'ㄵ', 'ㄶ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅄ', 'ㅆ'
    ];

    EXCEPTION_SYMBOL = [
        ' ', '&', '<', '>'
    ];

    /* 입력시 별도의 규칙이 필요한 심벌 규칙, 심벌코드, 표시되는심벌, 심벌이 차지하는 문자의 길이이다.  */
    EXCEPTION_SYMBOL_ORDER = [
        [ '&', '&amp;', 5 ], [ '<', '&lt;', 4 ], [ '>', '&gt;', 4 ], [ ' ', '&nbsp;', 6 ]
    ];


    constructor () {

        /* 가상키보드 작동시 완료될 때 까지 사용자가 사용할 contenteditable 입력창 */
        this.editableField = document.getElementById("VKEY_field_editable");

        /* 한,영,특수문자 전환과 관련한 키보드 종류들이 담긴다. */
        this.keyboardBundle = document.getElementsByClassName("VKEY_bundle_keyboard");

        /* 상용구의 각 페이지가 담긴다. */
        this.boilerList = document.getElementsByClassName("VKEY_pad_boiler");

        /* 일반적인 입력용 가상 키보드 버튼들이 담긴다. */
        this.normalBtn = document.getElementsByClassName("VKEY_btn_normal");

        /* 상용구의 각 버튼이 담긴다. */
        this.boilerBtn = document.getElementsByClassName("VKEY_btn_boiler");

        this.changeKeyboardBtn = document.getElementsByClassName("VKEY_btn_change_keyboard");

        this.changeBoilerpadBtn = document.getElementsByClassName("VKEY_btn_change_boilerpad");

        /* 스크롤링 처리 과정에 사용하기 위해, 가상키보드 입력창의 너비를 구한다. */
        this.editableFieldWidth = this.editableField.clientWidth;

        for (let i = 0; i < this.normalBtn.length; i++){
            this.normalBtn[i].addEventListener("click", () => {
                this.pushNormalBtn(this.normalBtn[i].value);
            });
        }

        for (let i = 0; i < this.boilerBtn.length; i++){
            this.boilerBtn[i].addEventListener("click", () => {
                this.pushBoilerBtn(this.boilerBtn[i].value);
            });
        }

        for (let i = 0; i < this.changeKeyboardBtn.length; i++){
            this.changeKeyboardBtn[i].addEventListener("click", () => {
                this.changeKeyboard([i]);
            });
        }

        for (let i = 0; i < this.changeBoilerpadBtn.length; i++){
            this.changeBoilerpadBtn[i].addEventListener("click", () => {
                this.changeBoilerplate([i]);
            });
        }

        document.getElementById("VKEY_btn_spacer").addEventListener("click", () => {
            this.pushNormalBtn(" ");
        });

        document.getElementById("VKEY_btn_cancel").addEventListener("click", () => {
            this.cancelEdit();
        })

        document.getElementById("VKEY_btn_complete").addEventListener("click", () => {
            this.completeEdit();
        })


        document.getElementById("VKEY_btn_caretleft").addEventListener("click", () => {
            this.moveCaretPosition("left");
        })

        document.getElementById("VKEY_btn_caretright").addEventListener("click", () => {
            this.moveCaretPosition("right");
        })

        document.getElementById("VKEY_btn_delete").addEventListener("click", () => {
            this.removeCharFromTargetField();
        })

        document.getElementById("VKEY_btn_clear").addEventListener("click", () => {
            this.clearInputField();
        })


    }

    /*
    * elementId 에 지정된 객체에 맞추어 가상 키보드를 띄운다.
    * subject : 가상키보드에 띄울 제목, boilerplate : 상용구 배열, callback :  키보드 편집 완료후 실행될 function
    *  */
    showKeyboard(elementId, subject = "기본 제목", boilerplate = [], callback = function(){}) {
        this.callback = callback;
        const LOOP_BOILER  = boilerplate.length > 31 ? 32 : boilerplate.length;

        for (let i = 0; i < LOOP_BOILER; i++){
            this.boilerBtn[i].value = boilerplate[i];
        }

        this.targetField = document.getElementById(elementId);
        let targetValue = this.targetField.value;

        if(targetValue === "") {
            this.lastCaretPos = [0, 0];
            this.lastActualCaretPos = [0, 0];
        }else{
            this.lastCaretPos[0] = targetValue.length;
            this.lastCaretPos[1] = this.lastCaretPos[0];

            targetValue = this.exceptionSymbolEncryption(this.targetField.value, true)[0];

            this.lastActualCaretPos[0] = targetValue.length;
            this.lastActualCaretPos[1] = this.lastActualCaretPos[0];
        }

        this.editableField.innerHTML = targetValue;
        document.getElementById("VKEY_field_subject").innerHTML = subject;
        document.getElementById("VKEY_VKEYMAIN").style.visibility = "visible";
        this.keyboardBundle[0].style.visibility = "visible";
        this.boilerList[0].style.visibility = "visible";
        this.focusWithCaret();
        this.editableField.scrollLeft = 99999;

    }

    showKeyPad(elementId, subject = "기본제목", callback = function (){}) {
        this.callback = callback;
        this.targetField = document.getElementById(elementId);
        let targetValue = this.targetField.value;

        this.keypadBtn = document.getElementsByClassName("VKEY_btn_normal");
        for (let i = 0; i < this.keypadBtn.length; i++){
            this.keypadBtn[i].addEventListener("click", () => {
                this.pushKeypadBtn(this.keypadBtn[i].value);
            });
        }
    }


    /* 디스플레이에 담을 값을 인수와 함께 넘긴다. */
    pushNormalBtn(INPUT_CHAR){

        /* 입력값이 한글의 자음이나 모음인 경우 한글 처리 과정에 들어가고, 아닐 경우 그대로 입력된다. */
        if(INPUT_CHAR.charCodeAt(0) > 12592 && INPUT_CHAR.charCodeAt(0) < 12644){
            this.korAlphabetProcess(INPUT_CHAR);
            /* 입력시 별도의 처리가 필요한 특수문자의 경우 */
        }else if(this.EXCEPTION_SYMBOL.includes(INPUT_CHAR)){
            this.exceptionSymbolProcess(INPUT_CHAR);
        }else{
            this.addCharToTargetField(INPUT_CHAR);
        }

    }


    pushBoilerBtn(INPUT_SENTENCE) {
        const INPUT_LENGTH = INPUT_SENTENCE.length;
        INPUT_SENTENCE = this.exceptionSymbolEncryption(INPUT_SENTENCE, true)[0];
        this.addCharToTargetField(INPUT_SENTENCE, INPUT_LENGTH-1, INPUT_SENTENCE.length);
    }

    /* 한글 처리를 담당하는 기능 눌려진 한글 문자를 인수로 받는다. */
    async korAlphabetProcess(INPUT_CHAR) {

        /* 목표 텍스트 필드 객체를 담는 곳 */
        const FIELD_VALUE = this.editableField.innerHTML;
        /* 커서 기준으로 한칸 앞 자리의 문자 (즉 마지막으로 입력되었다 상정할 수 있는 문자) 인식 */
        const LAST_CHAR = FIELD_VALUE.substr(this.lastActualCaretPos[0]-1,1);
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

        if( INPUT_CHAR_CODE < 12623 && this.compoundOrAdd ){ // 입력받은 값이 자음일 경우

            switch(true){

                /* 기존의 종성값이 변화 가능성 있는 종성을 포함한 완전한 한글일 경우 */
                case ( this.FINAL_CONSONANT_POTENTIAL.includes(
                    this.FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]) && IS_COMPLETE_KOREAN ) :

                    /* 종성 규칙에 따라 조합될 수 있는 종성이면 결과값을 반환한다. */
                    let FINAL_CONSONANT_IDX = this.makeTargetConsonantIdx(this.FINAL_CONSONANT_ORDER,
                        LAST_FINAL_CONSONANT_IDX, this.FINAL_CONSONANT.indexOf(INPUT_CHAR));

                    /* 종성 규칙에 따라 조합된 종성이 오면 반영하고, 그렇지 않으면 별개의 자음이라는 뜻으므로 입력받은 값을 그대로 출력한다. */
                    if(FINAL_CONSONANT_IDX) {
                        await this.removeCharFromTargetField("INPUT");
                        this.addCharToTargetField(this.compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                            LAST_VOWEL_IDX, FINAL_CONSONANT_IDX));
                    }else{
                        this.addCharToTargetField(INPUT_CHAR);
                    }

                    break;


                /* 기존의 값이 중성까지 완성되어 있는 조합된 한글일 경우 (종성 값이 0일 경우 혹은) 종성값을 추가한 값을 출력  */
                case ( LAST_FINAL_CONSONANT_IDX === 0 && IS_COMPLETE_KOREAN
                    && (!this.INITIAL_CONSONANT_DOUBLE.includes(INPUT_CHAR) || INPUT_CHAR_CODE === 12614) ) :
                    await this.removeCharFromTargetField("INPUT");
                    this.addCharToTargetField(this.compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                        LAST_VOWEL_IDX, this.FINAL_CONSONANT.indexOf(INPUT_CHAR)));
                    /* 마지막 받침으로 ㅆ을 사용한 경우라면 다음 모음이 올 때 ㅅ이 아닌 ㅆ과 조합된다. */
                    this.wasLastInputSsangsiot = INPUT_CHAR_CODE === 12614;
                    break;

                default :
                    this.addCharToTargetField(INPUT_CHAR);
                    break;
            }

        }else if(this.compoundOrAdd){ // 입력받은 값이 모음일 경우

            switch(true){

                /* 기존의 값이 초성일 경우 조합하여 출력 */
                case ( this.INITIAL_CONSONANT.includes(LAST_CHAR) ) :
                    await this.removeCharFromTargetField("INPUT");
                    this.addCharToTargetField(this.compoundCharacter(
                        this.INITIAL_CONSONANT.indexOf(LAST_CHAR), this.VOWEL.indexOf(INPUT_CHAR), 0));
                    break;

                /* 기존의 값이 미완성 가능성 있는 중성을 포함한 경우 */
                case ( this.VOWEL_POTENTIAL.includes(this.VOWEL[LAST_VOWEL_IDX]) && LAST_FINAL_CONSONANT_IDX === 0 ) :
                    /* 중성 규칙에 따라 조합될 수 있는 중성이면 결과값을 반환한다. */
                    let VOWEL_IDX = this.makeTargetConsonantIdx(this.VOWEL_ORDER, LAST_VOWEL_IDX,
                        this.VOWEL.indexOf(INPUT_CHAR));
                    if(VOWEL_IDX) { // 조합된 중성이면 초성과 중성을 조합하여 출력
                        await this.removeCharFromTargetField("INPUT");
                        this.addCharToTargetField(this.compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                            VOWEL_IDX, 0));
                    }else{ // 아닐 경우 입력받은 문자를 별개로 출력
                        this.addCharToTargetField(INPUT_CHAR);
                    }
                    break;

                /* 기존의 값의 종성이 복자음으로 구성되어 있는 경우 */
                case ( this.FINAL_CONSONANT_DOUBLE.includes(this.FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]) ) :

                    /* 모음이 올 경우 종성의 두 자는 앞과 뒤로 찢어지는데 앞부분의 인덱스를 담을 곳 */
                    let tempConsonantIdx = "";
                    let secondChar = "";

                    /* 마지막 받침입력이 ㅆ 이었다면 특별 규칙을 적용해 이전 글자는 받침이 사라지고, 새 글자에는 모음이 ㅆ이 된다. */
                    if(this.wasLastInputSsangsiot) {
                        /* 찢어진 앞의 문자 */
                        divideResultChar = this.compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                            LAST_VOWEL_IDX, 0);
                        /* 찢어진 뒤의 문자 */
                        secondChar = this.compoundCharacter(
                            10, this.VOWEL.indexOf(INPUT_CHAR), 0);
                    }else{
                        /* 종성의 앞부분을 분리해내기 */
                        this.FINAL_CONSONANT_ORDER.forEach(element => {
                            if (element[2] === LAST_FINAL_CONSONANT_IDX) {
                                /* 찢어진 앞의 문자 */
                                divideResultChar = this.compoundCharacter(LAST_INITIAL_CONSONANT_IDX,
                                    LAST_VOWEL_IDX, element[0]);
                                tempConsonantIdx = this.INITIAL_CONSONANT.indexOf(this.FINAL_CONSONANT[element[1]]);
                            }
                        });
                        /* 찢어진 뒤의 문자 */
                        secondChar = this.compoundCharacter(tempConsonantIdx, this.VOWEL.indexOf(INPUT_CHAR), 0);
                    }

                    await this.removeCharFromTargetField("INPUT");

                    /* addCaretPosition 을 1로 준 이유는 두자가 추가되어 캐랫 위치를 한칸 더 뒤로 당길 필요가 있기 때문 */
                    this.addCharToTargetField(divideResultChar + secondChar, 1, 2 );
                    break;

                /* 기존의 값이 종성까지 포함하고 있는 경우 */
                case (LAST_FINAL_CONSONANT_IDX > 0) :

                    /* 찢어진 앞부분의 문자 */
                    divideResultChar = this.compoundCharacter(
                        LAST_INITIAL_CONSONANT_IDX, LAST_VOWEL_IDX, 0);
                    /* 찢어진 뒷부분의 문자 */
                    const NEW_COMPOUND_CHAR = this.compoundCharacter(this.INITIAL_CONSONANT.indexOf(
                        this.FINAL_CONSONANT[LAST_FINAL_CONSONANT_IDX]), this.VOWEL.indexOf(INPUT_CHAR), 0);
                    await this.removeCharFromTargetField("INPUT");

                    /* addCaretPosition 을 1로 준 이유는 두자가 추가되어 캐랫 위치를 한칸 더 뒤로 당길 필요가 있기 때문 */
                    this.addCharToTargetField(divideResultChar + NEW_COMPOUND_CHAR, 1, 2);
                    break;


                default :
                    this.addCharToTargetField(INPUT_CHAR);
                    break;
            }
        }else{ // 글자 조합이 아닌 글자 추가 플래그로 세팅되어 있는 경우 (모음한자를 입력하고 다른곳에 커서를 놓았다 다시 왔다거나.....)
            this.addCharToTargetField(INPUT_CHAR);
        }
        /* 한글이 입력된 상태이므로 이후 입력이나 삭제시에는 조합되어 입력되거나, 분리되어 하나씩 삭제되는 플래그를 on 시켜주어야 한다. */
        this.divideOrDelete = true;
        this.compoundOrAdd = true;
    }

    exceptionSymbolProcess(INPUT_CHAR) {

        const RESULT = this.exceptionSymbolEncryption(INPUT_CHAR, true);
        INPUT_CHAR = RESULT[0]
        const ACTUAL_CARET_LENGTH = RESULT[1];

        this.addCharToTargetField(INPUT_CHAR, 0, ACTUAL_CARET_LENGTH);
        this.divideOrDelete = false;
        this.compoundOrAdd = false;
    }


    /* 목표 텍스트 필드에 인수로 받은 문자를 추가, addCaretPosition 을 설정할 경우 입력후의 캐랫 위치를 임의로 설정할 수 있다. */
    addCharToTargetField(INPUT_CHAR, addCaretPosition = 0, actualCaretLength = 1) {

        /* 사용자가 직접 캐럿의 위치를 선택한 경우에만 필요하다. */
        /*
        lastCaretPos = getCaretPosition();
        */

        const FIELD_VALUE = this.editableField.innerHTML;
        const HEAD_WORDS = FIELD_VALUE.substr(0, this.lastActualCaretPos[0]);
        const TAIL_WORDS = FIELD_VALUE.substr(this.lastActualCaretPos[1]);

        /* 캐랫이 있던 위치에 맞추어 입력시킬 문자를 끼워 출력시킨다. */
        this.editableField.innerHTML = HEAD_WORDS + INPUT_CHAR + TAIL_WORDS;
        this.setCaretPosition(this.lastCaretPos[0]+1+addCaretPosition, this.lastCaretPos[0]+1+addCaretPosition);
        this.lastActualCaretPos[0] += actualCaretLength;
        this.lastActualCaretPos[1] += actualCaretLength;
        this.scrollCenter();
    }


    /* 캐랫 위치를 반환한다 배열 0번은 시작 포지션, 배열 1번은 종료 포지션 */
    getCaretPosition() {
        this.editableField.focus();
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
    setCaretPosition(start, end) {
        if(this.editableField.innerHTML) {
            this.editableField.focus();
            const CARET_SELECTION = window.getSelection();
            const NEW_RANGE = CARET_SELECTION.getRangeAt(0);
            NEW_RANGE.setStart(this.editableField.childNodes[0], start);
            NEW_RANGE.setEnd(this.editableField.childNodes[0], end);
            const selection = document.getSelection();
            selection.removeAllRanges();
            selection.addRange(NEW_RANGE);
            this.lastCaretPos = [start, end];
            // 계산작업 전 까지 임시코드
        }
    }


    /* 사용자가 editableField 바깥쪽을 누르면 포커스가 해지되는데, 그 순간 editableField 의 캐랫 위치 정보는 상실된다.
    * 이 함수를 사용해서 포커스를 해줘야 원하던 곳의 캐럿을 마지막 존재하던 위치에 설정할 수 있다. */
    focusWithCaret() {
        this.setCaretPosition(this.lastCaretPos[0], this.lastCaretPos[1]);
    }


    measurePixel(targetText, fieldWidth) {
        /* 가상 DOM을 만들어서 길이를 재어 스크롤링에 이용한다. */
        const span = document.createElement("span");
        span.innerHTML = targetText;
        Object.assign(span.style, {
            position: 'absolute',
            visibility: 'hidden',
            fontSize: this.editableField.style.fontSize,
            whiteSpace: 'nowrap'
        });
        document.querySelector('html').prepend(span);
        let resultPixel = span.offsetWidth - fieldWidth / 2;
        resultPixel = resultPixel < 0 ? 0 : resultPixel;
        span.remove();

        return resultPixel;
    }


    /* 주어진 규칙에 따라 마지막 글자와 입력받은 글자의 배열 번호가 일치하는 해당 규칙의 IDX를 찾아 결과값을 리턴한다. */
    makeTargetConsonantIdx (ORDER, LAST_IDX, INPUT_IDX) {
        let resultCode = false;
        ORDER.forEach( element => {
            if( LAST_IDX === element[0] && INPUT_IDX === element[1] ){
                resultCode = element[2];
            }
        });
        return resultCode;
    }


    /* 초성, 중성, 종성 코드를 이용해 결과 조합 한글을 리턴한다. */
    compoundCharacter (INITIAL_CONSONANT_IDX, VOWEL_IDX, FINAL_CONSONANT_IDX) {
        let KR_UNICODE = 44032 + INITIAL_CONSONANT_IDX * 588 + VOWEL_IDX * 28 + FINAL_CONSONANT_IDX;
        return String.fromCharCode(KR_UNICODE);
    }


    /* 목표 텍스트 필드의 문자를 지우거나, 입력시 조합되는 문자의 필요없어지는 기존 부분을 지우는 데에도 쓰인다.
    *  가상 키보드 필드의 선택한 캐랫 영역만큼 혹은 캐랫의 한칸 앞을 지우도록 한다.
    *  role : DELETE 기본값  INPUT 입력시
    * */  // 지울 때 실제 캐럿 위치의 변동량도 신경써줘야 함.
    removeCharFromTargetField (role = "DELETE") {

        /* 가상 키보드 입력삭제 대상이 될 목표 텍스트 필드의 값을 담는다 */
        const FIELD_VALUE = this.editableField.innerHTML;

        /* 삭제 작업이 일어나고 나서 최종적으로 캐랫이 위치하게 될 포지션 */
        let targetFieldCaretSetPosition = this.lastCaretPos[0];

        /* 임시 */
        let stepBackLastActualCaretPos = 0;

        /* 한글조합을 분해하며 지우게 될 경우 분해 대상이 될 문자 */
        const TARGET_CHAR = FIELD_VALUE.substr(this.lastActualCaretPos[0] - 1, 1);

        /* 위 분해 대상이 될 문자의 유니코드 */
        const TARGET_CHAR_CODE = TARGET_CHAR.charCodeAt(0);

        /* 조합된 문자를 끝에서 부터 분해시 임시 저장될 변수 */
        let divideResultChar = "";

        const IS_SINGLE_CARET = this.lastCaretPos[0] === this.lastCaretPos[1];

        /* 인수로 전달받은 역할에 따라 나뉜다. */
        switch (role) {

            /*
            * 입력 작업시 문자가 조합될 때 기존의 문자를 없애고 조합된 문자를 남기기 위해 입력 문자열을 자를 캐랫의 시작위치 조정이 필요
            * 캐랫의 시작과 끝 위치가 같으면 캐랫이 단일 칸에 머물러 깜빡이고 있음을 의미하고, 다르면 문자열의 영역이 선택된 것임을 의미한다.
            * */
            case "INPUT" :

                if(IS_SINGLE_CARET) {
                    targetFieldCaretSetPosition = --this.lastCaretPos[0];
                    this.lastActualCaretPos[0]--;
                }
                break;

            /* 삭제 동작을 할 경우 동작이 좀 더 복잡해 진다. */
            case "DELETE":

                /* 캐랫이 단일위치에서 깜빡이고, 조합된 한글이며, 직전까지 한글이 입력중이던 상태여서 한글조합의 분리가 필요한 상황임을 감지 */
                if (IS_SINGLE_CARET && this.editableField.innerHTML && this.lastActualCaretPos[0] > 0) {
                    if (TARGET_CHAR_CODE > 44031 && TARGET_CHAR_CODE < 55204 && this.divideOrDelete) {
                        /* 분해 대상이 될 문자의 유니코드의 초성, 중성, 종성의 IDX를 구한다. */
                        const TARGET_CHAR_SEQUENCE = TARGET_CHAR_CODE - 44032;
                        const TARGET_INITIAL_CONSONANT_IDX = Math.floor(TARGET_CHAR_SEQUENCE / 588);
                        const TARGET_VOWEL_IDX = Math.floor(TARGET_CHAR_SEQUENCE / 28) % 21;
                        const TARGET_FINAL_CONSONANT_IDX = TARGET_CHAR_SEQUENCE % 28;

                        this.lastActualCaretPos[0]--;
                        switch (true) {

                            /* 분해할 문자의 종성이 존재할 때 */
                            case (TARGET_FINAL_CONSONANT_IDX > 0) :
                                /* 종성이 두자의 자음 조합으로 되어있을 때 앞자리의 자음만 남긴다. */
                                this.FINAL_CONSONANT_ORDER.forEach(element => {
                                    if (element[2] === TARGET_FINAL_CONSONANT_IDX) {
                                        divideResultChar = this.compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                            TARGET_VOWEL_IDX, element[0]);
                                    }
                                });
                                /* 종성이 한자의 자음일 경우 종성을 날린다. */
                                if(!divideResultChar){
                                    divideResultChar = this.compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                        TARGET_VOWEL_IDX, 0);
                                }
                                break;

                            /* 중성이 존재할 경우 */
                            case (TARGET_VOWEL_IDX > -1) :
                                /* 중성이 두자의 모음 조합으로 되어있을 때 먼저입력되는 모음만 남긴다. */
                                this.VOWEL_ORDER.forEach(element => {
                                    if (element[2] === TARGET_VOWEL_IDX) {
                                        divideResultChar = this.compoundCharacter(TARGET_INITIAL_CONSONANT_IDX,
                                            element[0], 0);
                                    }
                                });

                                /* 중성이 한자의 모음 조합으로 되어있을 때, 자음부만 남긴다. */
                                divideResultChar = (divideResultChar === "")
                                    ? this.INITIAL_CONSONANT[TARGET_INITIAL_CONSONANT_IDX] : divideResultChar;
                                break;
                        }
                        stepBackLastActualCaretPos = 1;
                    }else if(IS_SINGLE_CARET && FIELD_VALUE.substr(this.lastActualCaretPos[0] - 1, 1) === ";") {
                        targetFieldCaretSetPosition = --this.lastCaretPos[0];
                        const START_ACTUAL_CARET_POS = this.lastActualCaretPos[0] > 5 ? this.lastActualCaretPos[0]-6 : 0;
                        this.lastActualCaretPos[0] -=
                            this.exceptionSymbolLength(FIELD_VALUE.substr(START_ACTUAL_CARET_POS, 6));
                    }else{

                        /* 한 글자를 완전히 지우기 위해서는 자를 위치를 한칸 당기고, 캐랫의 포지션은 한칸 더 앞으로 이동해야 한다. */
                        targetFieldCaretSetPosition = --this.lastCaretPos[0];
                        this.lastActualCaretPos[0]--;

                        /* 지울 대상이 쌍자음일 경우, 단자음으로 바꾸어 준다. */
                        if(this.INITIAL_CONSONANT_DOUBLE.includes(TARGET_CHAR) && this.divideOrDelete){
                            divideResultChar = String.fromCharCode(TARGET_CHAR_CODE-1);

                            targetFieldCaretSetPosition++;
                            stepBackLastActualCaretPos = 1;
                        }else{
                            /* 한글이 아니거나 한글 낱자를 지웠으므로 이 다음 삭제 대상은 마지막 입력하던 글자가 아니다. 그러므로..... */
                            this.divideOrDelete = false;
                            this.compoundOrAdd = false;
                        }
                    }
                }else if(IS_SINGLE_CARET && this.lastActualCaretPos[0] > 0) {
                    targetFieldCaretSetPosition--;
                    this.lastActualCaretPos[0]--;
                }
                this.focusWithCaret();
                break;
        }


        /* 위에서 적용된 캐랫을 기준으로 문자의 앞 부분과 뒷 부분을 자르고, 그 양측의 문자열 사이에 추가될 결과문자를(있을경우) 집어넣는다. */
        this.editableField.innerHTML = FIELD_VALUE.substr(0, this.lastActualCaretPos[0])
            + divideResultChar + FIELD_VALUE.substr(this.lastActualCaretPos[1]);
        /* 위 텍스트 필드의 문자열 치환 작업을 하게 되면 다시 텍스트 필드에 포커스를 주고 캐랫의 위치를 지정해 주어야 한다. */
        this.setCaretPosition(targetFieldCaretSetPosition, targetFieldCaretSetPosition);
        this.lastActualCaretPos[0] += stepBackLastActualCaretPos;
        this.lastActualCaretPos[1] = this.lastActualCaretPos[0];
        this.scrollCenter();
        this.focusWithCaret();
    }


    /* 입력 필드 전체 삭제 */
    clearInputField() {
        this.editableField.innerHTML = "";
        this.lastCaretPos = [0, 0];
        this.lastActualCaretPos = [0, 0];
        this.editableField.focus();
        this.divideOrDelete = false;
        this.compoundOrAdd = false;
    }


    /* 삭제작업이나 ...에서 심벌 표현 문자열의 길이를 추정하여 반환  */
    exceptionSymbolLength (assumeSymbolCode) {

        let resultLength = 1;
        this.EXCEPTION_SYMBOL_ORDER.forEach( element => {
            if(assumeSymbolCode.includes(element[1])){
                resultLength = element[2];
            }

        });
        return resultLength;
    }


    /* 입력 필드에서 키보드 방향키의 역할을 함 */  //추가로 실제 캐럿의 위치의 변동도 필요하다.
    moveCaretPosition (direction) {

        const IS_SINGLE_CARET = this.lastCaretPos[0] === this.lastCaretPos[1];
        const FIELD_VALUE = this.editableField.innerHTML;
        const FIELD_LENGTH = FIELD_VALUE.length;

        /* 입력 필드 시작과 종료 포지션이 같다면 일반적인 방향키의 움직임을 구현 */
        if(IS_SINGLE_CARET) {
            let examChar;
            switch (direction) {
                case "right" :
                    if (this.lastActualCaretPos[0] < FIELD_LENGTH) {
                        this.lastCaretPos[0]++;
                        this.lastCaretPos[1]++;
                        this.lastActualCaretPos[0]++;
                        this.lastActualCaretPos[1]++;

                        examChar = FIELD_VALUE.substr(this.lastActualCaretPos[0]-1, 1);
                        if(examChar === "&") {
                            this.lastActualCaretPos[0] += this.exceptionSymbolLength(
                                FIELD_VALUE.substr(this.lastActualCaretPos[0]-1, 6))-1;
                            this.lastActualCaretPos[1] = this.lastActualCaretPos[0];
                        }
                    }

                    break;
                case "left" :
                    if (this.lastActualCaretPos[0] > 0) {
                        this.lastCaretPos[0]--;
                        this.lastCaretPos[1]--;
                        this.lastActualCaretPos[0]--;
                        this.lastActualCaretPos[1]--;

                        examChar = FIELD_VALUE.substr(this.lastActualCaretPos[0], 1);
                        if(examChar === ";") {
                            const START_ACTUAL_CARET_POS = this.lastActualCaretPos[0] > 4 ? this.lastActualCaretPos[0]-5 : 0;
                            this.lastActualCaretPos[0] -= this.exceptionSymbolLength(
                                FIELD_VALUE.substr(START_ACTUAL_CARET_POS, 6))-1;
                            this.lastActualCaretPos[1] = this.lastActualCaretPos[0];
                        }
                    }
                    break;
            }
        }else{ // 캐랫의 시작과 종료 위치가 다르면 선택된 구간이 있다는 뜻 이므로 최종 캐랫의 포지션은 상대적이다.
            switch (direction){ // 범위내의 특수문자들의 길이까지 고려해주는 기능과 적용이 필요함.
                case "right" :
                    if(this.lastActualCaretPos[0] < FIELD_LENGTH) {
                        this.lastCaretPos[0] = this.lastCaretPos[1];
                        this.lastActualCaretPos[0] = this.lastActualCaretPos[1];
                    }
                    break;
                case "left" :
                    if (this.lastActualCaretPos[0] > FIELD_LENGTH) {
                        this.lastCaretPos[1] = this.lastCaretPos[0];
                        this.lastActualCaretPos[1] = this.lastActualCaretPos[0];
                    }
                    break;
            }
        }

        this.wasLastInputSsangsiot = false;
        this.divideOrDelete = false;
        this.compoundOrAdd = false;

        this.scrollCenter();
        this.focusWithCaret();
    }


    changeKeyboard(typeNo){
        for (let i=0; i<5; i++) {
            this.keyboardBundle[i].style.visibility = "hidden";
        }
        this.keyboardBundle[typeNo].style.visibility = "visible";
    }

    changeBoilerplate(typeNo) {
        for (let i=0; i<4; i++) {
            this.boilerList[i].style.visibility = "hidden";
        }
        this.boilerList[typeNo].style.visibility = "visible";
    }

    cancelEdit() {
        document.getElementById("VKEY_VKEYMAIN").style.visibility = "hidden";
        for (let i=0; i<4; i++) {
            this.boilerList[i].style.visibility = "hidden";
            this.keyboardBundle[i].style.visibility = "hidden";
        }
        this.keyboardBundle[4].style.visibility = "hidden";
        this.clearInputField();
    }

    completeEdit() {
        this.targetField.value = this.exceptionSymbolEncryption(this.editableField.innerHTML, false)[0];
        document.getElementById("VKEY_VKEYMAIN").style.visibility = "hidden";
        for (let i=0; i<4; i++) {
            this.boilerList[i].style.visibility = "hidden";
            this.keyboardBundle[i].style.visibility = "hidden";
        }
        this.keyboardBundle[4].style.visibility = "hidden";
        this.clearInputField();
        this.callback();
    }

    /* 입력필드 전체를 encryptOrDecrypt 예외문자 html에 맞게 변환 true, 다시 일반적인 텍스트의 문자로 변환 false */
    exceptionSymbolEncryption(targetText, encryptOrDecrypt) {

        let index = encryptOrDecrypt ? [0 , 1] : [1, 0];
        let length = 0;

        /* 입력필드의 html 문법과 예외문자 사이를 변환 */
        this.EXCEPTION_SYMBOL_ORDER.forEach( element => {
            if (targetText.includes(element[index[0]])) {
                targetText = targetText.replaceAll(element[index[0]], element[index[1]]);
                length += element[2];
            }
        });

        return [targetText, length];
    }

    scrollCenter () {
        this.editableField.scrollLeft = this.measurePixel(this.editableField.innerHTML.substr(0, this.lastActualCaretPos[0]), this.editableFieldWidth);
    }
}
