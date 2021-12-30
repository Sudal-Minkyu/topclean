class DtoValidator {
    forceTotalInspection = true;
    devMode = true;

    title;
    ruleObj;
    obj;
    ruleKeys;
    trashKeys;
    objKeys;
    isArray;
    loopNo;

    constructor() {
    }

    chk(obj, ruleObj, title = "", totalInspection = false) {
        this.obj = obj;
        this.title = title;
        this.ruleObj = ruleObj;
        if(this.isRuleTypeWrong()) return true;
        this.ruleKeys = Object.keys(this.ruleObj);

        if(Array.isArray(this.obj)) {
            this.isArray = true;

            const loopTimes = (totalInspection || this.forceTotalInspection) ? this.obj.length : 1;
            for(this.loopNo = 0; this.loopNo < loopTimes; this.loopNo++) {
                if(this.isObjNotRight(this.obj[this.loopNo])) {
                    console.log("문제가 발생한 검사대상 배열번호 = " + this.loopNo);
                    return true;
                }
            }
            if(loopTimes === 1) {
                this.removeRemainTrashKeyValue();
            }
        }else{
            this.isArray = false;
            if(this.isObjNotRight(this.obj)) {
                return true;
            }
        }
        return false;
    }

    isObjNotRight(anObj) {
        if(anObj === null || anObj === undefined || anObj.constructor !== ({}).constructor) {
            console.log("=== 검사 객체 ===");
            console.log(anObj);
            console.log("=== " + this.title + " ===");
            if(this.devMode) alert(this.title +" 의 대상 형태가 json 이 아닙니다. 콘솔참조");
            return true;
        }
        this.objKeys = Object.keys(anObj);

        if(this.compareKeys()){
            console.log("=== " + this.title + " ===");
            if(this.devMode) alert(this.title + " 의 검사대상과 규칙간의 키가 일치하지 않습니다. 콘솔참조");
            return true;
        }

        if(this.isTypeWrong(anObj)) {
            console.log("=== " + this.title + " ===");
            return true;
        }

        return false;
    }

    isTypeWrong(anObj) {
        for(let i = 0; i < this.ruleKeys.length; i++) {
            const chkRule = this.ruleObj[this.ruleKeys[i]];
            const chkLength = chkRule.length;
            const chkValue = anObj[this.ruleKeys[i]];
            for(let j = 0; j < chkLength; j++) {
                switch (chkRule[j]) {
                    case "s":
                        if(typeof chkValue !== "string" && chkValue !== null && chkValue !== undefined) {
                            console.log("=== 해당 검사값은 문자이어야 한다 ===");
                            console.log(this.ruleKeys[i]);
                            console.log(chkValue);
                            if(this.devMode) alert(this.title + " 의 검사값이 규칙(문자)에 부합하지 않습니다. 콘솔참조 ");
                            return true;
                        }
                        break;
                    case "n":
                        if(typeof chkValue !== "number" && chkValue !== null && chkValue !== undefined) {
                            console.log("=== 해당 검사값은 숫자이어야 한다 ===");
                            console.log(this.ruleKeys[i]);
                            console.log(chkValue);
                            if(this.devMode) alert(this.title + " 의 검사값이 규칙(숫자)에 부합하지 않습니다. 콘솔참조 ");
                            return true;
                        }
                        break;
                    case "r":
                        if(!chkValue && chkValue !== 0) {
                            console.log("=== 해당 검사값은 필수 항목이다 ===");
                            console.log(this.ruleKeys[i]);
                            if(this.devMode) alert(this.title + " 의 검사값이 규칙(필수)에 부합하지 않습니다. 콘솔참조 ");
                            return true;
                        }
                        break;
                    case "d":
                        if(this.isArray) {
                            delete this.obj[this.loopNo][this.ruleKeys[i]];
                        }else{
                            delete this.obj[this.ruleKeys[i]];
                        }
                        j = chkLength;
                        break;
                }
            }
        }
        return false;
    }

    isRuleTypeWrong() {
        if(this.ruleObj === null || this.ruleObj === undefined || this.ruleObj.constructor !== ({}).constructor) {
            console.log("=== 규칙 객체 ===");
            console.log(this.ruleObj);
            console.log("=== " + this.title + " ===");
            if(this.devMode) alert(this.title + " 의 검사 규칙을 정하는 dto 인자가 json 형태가 아닙니다. 콘솔참조");
            return true;
        }
        return false;
    }

    compareKeys() {
        let result = false;
        let thisKeysLeft = "";
        let ruleKeysLeft = "";
        const thisLength = this.objKeys.length;
        const ruleLength = this.ruleKeys.length;

        for(let i = 0; i < thisLength; i++) {
            if(!this.ruleKeys.includes(this.objKeys[i])) {
                thisKeysLeft += this.objKeys[i] + ", ";
            }
        }

        for(let i = 0; i < ruleLength; i++) {
            if(!this.objKeys.includes(this.ruleKeys[i])) {
                ruleKeysLeft += this.ruleKeys[i] + ", ";
            }
        }


        if(thisKeysLeft) {
            console.log("=== 규칙 dto에 없고, 검사대상에만 존재하는 키 ===");
            console.log(thisKeysLeft.substr(0, thisKeysLeft.length - 2));
            result = true;
        }

        if(ruleKeysLeft) {
            console.log("=== 검사대상에 없고, 규칙 dto에만 존재하는 키 ===");
            console.log(ruleKeysLeft.substr(0, ruleKeysLeft.length - 2));
            result = true;
        }

        return result;
    }

    removeRemainTrashKeyValue() {
        let targetKeys = [];
        for(let i = 0; i < this.ruleKeys.length; i++) {
            const chkRule = this.ruleObj[this.ruleKeys[i]];
            if(chkRule.includes("d")) {
                targetKeys.push(this.ruleKeys[i]);
            }
        }
        for(let i = 1; i < this.obj.length; i++) {
            for(let j = 0; j < targetKeys.length; j++) {
                delete this.obj[i][targetKeys[j]];
            }
        }
    }
}

const dv = new DtoValidator();