class CommonDataClass {
    name = {};

    constructor() {
        this.nameList();
    }

    nameList() {

        this.name.frRefType = {
            "01": "-",
            "02": "무",
            "03": "배",
        };

        this.name.fdState = {
            S1: "접수",
            S2: "지사입고",
            S3: "지사반송",
            S4: "지사출고",
            S5: "가맹점입고",
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

    }
}

const CommonData = new CommonDataClass();