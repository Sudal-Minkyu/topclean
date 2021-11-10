/*
* 자주 쓰이는 UI용 함수를 모듈화
* CommonUI.기능 으로 호출하여 사용
*
* 제작 : 성낙원
* 수정 : 2021-11-09
* */
class CommonUIClass {

    constructor() {

    }

    /*
    * 배열에 담긴 DOM ID들에 JqueryUI의 datepicker 를 적용한다.
    * 기본 데이터 포맷 = yy-mm-dd
    * */
    setDatePicker(targetIdArray, dateFormat = "yy-mm-dd") {
        for (const targetId of targetIdArray) {
            $("#"+targetId).datepicker({
                dateFormat: dateFormat,
            });
        }
    }

    /*
    * datepicker A에서 B까지의 기간을 설정할 때 서로간의 입력 날자를 제한한다.
    * targetIdArray = [ ['from대상id1', 'to대상id2'], ['from대상id1', 'to대상id2'], .... ]
    * */
    restrictDateAToB(targetIdArray) {
        for(let i = 0; i < targetIdArray.length; i++) {
            for(let j = 0; j < targetIdArray[i].length; j++) {
                $("#"+targetIdArray[i][j]).on("change", function() {
                    CommonUI.restrictDate(targetIdArray[i][0], targetIdArray[i][1], j);
                });
            }
        }
    }

    /*
    * restrictDateAToB 와 연동되거나 단독 사용되는 함수.
    * ~부터의 Id, ~까지의  Id, 함수를 부르는 객체가 ~까지의 객체인지를 판단하여 지정해 준다.
    * Element에 걸린 datepicker 속성을 수정하여 날짜 최소범위, 최대범위를 지정한다.
    * */
    restrictDate(fromId, toId, isCallerTo) {
        if(isCallerTo) {
            $("#"+fromId).datepicker("option", "maxDate", $("#"+toId).val());
        }else{
            $("#"+toId).datepicker("option", "minDate", $("#"+fromId).val());
        }
    }

    /*
    * 배열에 담긴 객체의 datepicker 날짜 범위를 지정했던 것을 해제한다.
    * */
    restrictDateReset(targetIdArray) {
        for (const targetId of targetIdArray) {
            $("#"+targetId).datepicker("option", "minDate", "");
            $("#"+targetId).datepicker("option", "maxDate", "");
        }
    }

    regularValidator(testValue, testMethod) {

        const email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;

        switch (testMethod) {
            case "email" :
                return email.test(testValue);
                break;
            default :
                break;
        }
    }
}

const CommonUI = new CommonUIClass();