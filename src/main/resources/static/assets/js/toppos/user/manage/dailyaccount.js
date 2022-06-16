/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {

    },
    receive: {

    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getDailyAcoountData(targetDate) {
        CommonUI.ajax('/api/user/franchiseReceiptDaysList', 'GET', targetDate, function (res) {
            console.log(res);
        });
    }
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#searchBtn').on('click', function () {
            getDailyAcoountData();
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {

};

const enableDatepicker = function () {
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "hsYyyymmdd"
    ];

    $("#" + datePickerTargetIds[0]).val(today);

    CommonUI.setDatePicker(datePickerTargetIds);
}

const getDailyAcoountData = function () {
    const targetDate = {
        hsYyyymmdd: $('#hsYyyymmdd').val().numString(),
    }

    comms.getDailyAcoountData(targetDate);
}

/* 서버에서 온 데이터로 표 안의 값들을 채워넣기 */
const setUpFields = function (data) {

}

/* 서버에서 가져온 값들을 활용하여 표안의 계산 요소들의 값을 채워넣기 */
const calculateFields = function (data) {
    const calculatedData = {
        suma14: data.a1 + data.a2 + data.a3,
        suma24: data.a4 + data.a5,
        suma34: data.a6,
        suma41: data.a1,
        suma42: data.a2 + data.a4 + data.a6,
        suma43: data.a3 + data.a5,
        suma44: data.a1 + data.a2 + data.a3 + data.a4 + data.a5 + data.a6,

        sumb17: data.b1 + data.b20 + data.b3 + data.b4 + data.b5,
        sumb27: data.b9 + data.b10 + data.b11 + data.b12 + data.b13,
        sumb211: data.b15,
        sumb311: data.b16 - data.b17,
        sumb49: data.b15 + data.b16,
        sumb411: data.b15 + data.b16 - data.b17,
    }

    setFieldsByEachJsonKey(calculatedData, true);
};

/* 키값에 맞춰 해당하는 id에 값 넣기 */
const setFieldsByEachJsonKey = function (jsonObj, isNumbers) {
    const setNumber = function (key) {
        $('#' + key).html(jsonObj[key].toInt().toLocaleString());
    };
    const setData = function (key) {
        $('#' + key).html(jsonObj[key]);
    };
    const setFunction = isNumbers ? setNumber : setData;

    const keys = Object.keys(jsonObj);
    for(const key of keys) {
        setFunction(key);
    }
};


/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    enableDatepicker();
    trigs.basic();
};
