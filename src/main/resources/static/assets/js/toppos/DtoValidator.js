class DtoValidator {
    forceTotalInspection = true;
    devMode = false;

    title;
    totalInspection;


    constructor() {

    }

    chk(obj, ruleObj, title = "", totalInspection = false) {
        // const start = new Date();
        this.title = title;
        this.totalInspection = totalInspection;
        if(this.isRuleTypeWrong(ruleObj)) return true;

        this.chkAnObj(obj, ruleObj, "(검사객체)");

        // const end = new Date();
        // console.log("동작시간 : " + (end - start) + " ms");
        return false;
    }

    chkAnObj(partObj, partRule, objName) {
        const partRuleKeys = Object.keys(partRule);
        if(Array.isArray(partObj)) {

            const loopTimes = (this.totalInspection || this.forceTotalInspection) ? partObj.length : 1;
            for(let loopNo = 0; loopNo < loopTimes; loopNo++) {
                if(this.isObjNotRight(partObj[loopNo], partRule, partRuleKeys, objName)) {
                    console.log("문제가 발생한 검사대상 배열번호 = " + loopNo);
                    return true;
                }
            }
            if(loopTimes === 1) {
                this.removeRemainTrashKeyValue(partObj, partRule, partRuleKeys);
            }
        }else{
            if(this.isObjNotRight(partObj, partRule, partRuleKeys, objName)) {
                return true;
            }
        }
    }

    isObjNotRight(partObj, partRule, partRuleKeys, objName) {
        if(partObj === null || partObj === undefined || partObj.constructor !== ({}).constructor) {
            console.log("=== 검사 객체 ===");
            console.log(partObj);
            console.log("=== " + this.title + " ===");
            if(this.devMode) alert(this.title + " " + objName + " 대상 형태가 json 이 아닙니다. 콘솔참조");
            return true;
        }


        if(this.compareKeys(Object.keys(partObj), partRuleKeys, objName)){
            console.log("=== " + this.title + " ===");
            if(this.devMode) alert(this.title + " " + objName + " 규칙간의 키가 일치하지 않습니다. 콘솔참조");
            return true;
        }

        if(this.isTypeWrong(partObj, partRule, partRuleKeys, objName)) {
            console.log("=== " + this.title + " ===");
            return true;
        }

        return false;
    }

    isTypeWrong(partObj, partRule, partRuleKeys, objName) {

        for(let i = 0; i < partRuleKeys.length; i++) {
            const chkRule = partRule[partRuleKeys[i]];
            const chkLength = chkRule.length;
            const chkValue = partObj[partRuleKeys[i]];

            if(typeof chkRule === "object") {
                this.chkAnObj(chkValue, chkRule, objName + "." + partRuleKeys[i]);
            }else {
                for (let j = 0; j < chkLength; j++) {
                    switch (chkRule[j]) {
                        case "s":
                            if (typeof chkValue !== "string" && chkValue !== null && chkValue !== undefined) {
                                console.log("=== 해당 검사값은 문자형이어야 한다 ===");
                                console.log(objName + "." + partRuleKeys[i]);
                                console.log(chkValue);
                                if (this.devMode) alert(this.title + " 의 검사값이 규칙(문자)에 부합하지 않습니다. 콘솔참조 ");
                                return true;
                            }
                            break;
                        case "n":
                            if ((typeof chkValue !== "number" && chkValue !== null && chkValue !== undefined)
                                    || isNaN(chkValue)) {
                                console.log("=== 해당 검사값은 유효한 숫자형이어야 한다 ===");
                                console.log(objName + "." + partRuleKeys[i]);
                                console.log(chkValue);
                                if (this.devMode) alert(this.title + " 의 검사값이 규칙(숫자)에 부합하지 않습니다. 콘솔참조 ");
                                return true;
                            }
                            break;
                        case "a":
                            if(!Array.isArray(chkValue)) {
                                console.log("=== 해당 검사객체는 배열형이어야 한다 ===");
                                console.log(objName + "." + partRuleKeys[i]);
                                console.log(chkValue);
                                if (this.devMode) alert(this.title + " 의 검사객체가 규칙(배열)에 부합하지 않습니다. 콘솔참조 ");
                            }
                            break;
                        case "r":
                            if (!chkValue && chkValue !== 0) {
                                console.log("=== 해당 검사값은 필수 항목이다 ===");
                                console.log(objName + "." + partRuleKeys[i]);
                                if (this.devMode) alert(this.title + " 의 검사값이 규칙(필수)에 부합하지 않습니다. 콘솔참조 ");
                                return true;
                            }
                            break;
                        case "d":
                            delete partObj[partRuleKeys[i]];
                            j = chkLength;
                            break;
                    }
                }
            }
        }
        return false;
    }

    isRuleTypeWrong(ruleObj) {
        if(ruleObj === null || ruleObj === undefined || ruleObj.constructor !== ({}).constructor) {
            console.log("=== 규칙 객체 ===");
            console.log(ruleObj);
            console.log("=== " + this.title + " ===");
            if(this.devMode) alert(this.title + " 의 검사 규칙을 정하는 dto 인자가 json 형태가 아닙니다. 콘솔참조");
            return true;
        }
        return false;
    }

    compareKeys(partObjKeys, partRuleKeys, objName) {
        let result = false;
        let thisKeysLeft = "";
        let ruleKeysLeft = "";
        const objLength = partObjKeys.length;
        const ruleLength = partRuleKeys.length;

        for(let i = 0; i < objLength; i++) {
            if(!partRuleKeys.includes(partObjKeys[i])) {
                thisKeysLeft += partObjKeys[i] + ", ";
            }
        }

        for(let i = 0; i < ruleLength; i++) {
            if(!partObjKeys.includes(partRuleKeys[i])) {
                ruleKeysLeft += partRuleKeys[i] + ", ";
            }
        }


        if(thisKeysLeft) {
            console.log("=== "+ objName +" 규칙 dto에 없고, 검사대상에만 존재하는 키 ===");
            console.log(thisKeysLeft.substr(0, thisKeysLeft.length - 2));
            result = true;
        }

        if(ruleKeysLeft) {
            console.log("=== "+ objName +" 에 없고, 규칙 dto에만 존재하는 키 ===");
            console.log(ruleKeysLeft.substr(0, ruleKeysLeft.length - 2));
            result = true;
        }

        return result;
    }

    removeRemainTrashKeyValue(partObj, partRule, partRuleKeys) {
        let targetKeys = [];
        for(let i = 0; i < partRuleKeys.length; i++) {
            const chkRule = partRule[partRuleKeys[i]];
            if(Array.isArray(chkRule) && chkRule.includes("d")) {
                targetKeys.push(partRuleKeys[i]);
            }
        }
        for(let i = 1; i < partObj.length; i++) {
            for(let j = 0; j < targetKeys.length; j++) {
                delete partObj[i][targetKeys[j]];
            }
        }
    }
}

const dv = new DtoValidator();