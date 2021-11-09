class CommonUI {

    constructor() {

    }

    setDatePicker(targetIdArray) {
        for (const targetId of targetIdArray) {
            $("#"+targetId).datepicker({
                dateFormat: 'yy-mm-dd',
            });
        }
    }

    setDateAToBValidation(targetIdArray) {
        for(let i = 0; i < targetIdArray.length; i++) {
            for(let j = 0; j < targetIdArray[i].length; j++) {
                $("#"+targetIdArray[i][j]).on("change", function() {
                    ComUI.ZRestrictDate(targetIdArray[i][0], targetIdArray[i][1], j);
                });
            }
        }
    }

    ZRestrictDate(fromId, toId, isCallerTo) {
        if(isCallerTo) {
            $("#"+fromId).datepicker("option", "maxDate", $("#"+toId).val());
        }else{
            $("#"+toId).datepicker("option", "minDate", $("#"+fromId).val());
        }
    }

}

const ComUI = new CommonUI();