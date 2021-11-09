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
    setDateAToBValidation(targetIdArray) {
        for(let i = 0; i < targetIdArray.length; i++) {
            for(let j = 0; j < targetIdArray[i].length; j++) {
                $("#"+targetIdArray[i][j]).on("change", function() {
                    CommonUI.ZRestrictDate(targetIdArray[i][0], targetIdArray[i][1], j);
                });
            }
        }
    }

    /*
    * setDateAToBValidation 과 연동되는 함수.
    * Element에 걸린 datepicker 속성을 수정하여 날짜 최소범위, 최대범위를 지정한다.
    * */
    ZRestrictDate(fromId, toId, isCallerTo) {
        if(isCallerTo) {
            $("#"+fromId).datepicker("option", "maxDate", $("#"+toId).val());
        }else{
            $("#"+toId).datepicker("option", "minDate", $("#"+fromId).val());
        }
    }

}

const CommonUI = new CommonUIClass();