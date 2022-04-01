class CommonDataClass {
    name = {};

    constructor() {
        this.nameList();
    }

    /* 코드를 통해서 출력할 값을 구하는 작업들에 쓰임 */
    nameList() {

        this.name.frRefType = {
            "01": "-",
            "02": "무",
            "03": "배",
        };

        this.name.fdState = {
            S1: "접수",
            S2: "지사입고",
            S3: "반품입고",
            S4: "지사출고",
            S5: "가맹입고",
            S6: "고객인도",
            S7: "강제출고",
            S8: "강제입고",
            F: "가맹검품",
            B: "확인품",
        };

        this.name.fdColorCode = { // 컬러코드에 따른 실제 색상
            "00": "#D4D9E1", "01": "#D4D9E1", "02": "#3F3C32", "03": "#D7D7D7", "04": "#F54E50", "05": "#FB874B",
            "06": "#F1CE32", "07": "#349A50", "08": "#55CAB7", "09": "#398BE0", "10": "#DE9ACE", "11": "#FF9FB0",
        };

        this.name.bcGradeName = {
            "01": "일반",
            "02": "VIP",
            "03": "VVIP",
        };

        this.name.fiType = {
            F: "가맹검품",
            B: "확인품",
        };

        this.name.fpType = {
            "01": "현금",
            "02": "카드",
            "03": "적립금",
            "04": "미수결제",
        };

        this.name.fiCustomerConfirm = {
            "1": "미확인",
            "2": "고객수락",
            "3": "고객거부",
        };

        this.name.fdS2Type = {
            "01": "수기",
            "02": "자동",
            "03": "반송",
        };

        this.name.fdS4Type = {
            "01": "일반",
            "02": "강제",
            "03": "가맹점강제입고출고",
            "04": "반품",
            "05": "확인(출)",
            "06": "확인(반)",
            "07": "확인(수)",
            "08": "확인(거)",
            "99": "확인(미)",
        };

        this.name.ffState = {
            "01": "요청",
            "02": "확인",
        }
    }

    formatTagNo(tagNo) {
        let result = "";
        if(tagNo) {
            result = tagNo.substr(0, 3) + "-" + tagNo.substr(-4);
        }
        return result;
    }
}

const CommonData = new CommonDataClass();