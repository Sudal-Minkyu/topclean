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
        /* 우선 값이 들어갈 필드들을 초기화 시킨다. */
        setFieldsByEachJsonKey(wares.fieldResetJson);
        wares.currentData = {};
        CommonUI.ajax('/api/user/franchiseReceiptDaysList', 'GET', targetDate, function (res) {
            const data = res.sendData.monthlySummaryDaysDtos[0];
            console.log(data);
            if (data) {
                wares.currentData = data;
                setFieldsByEachJsonKey(data);
                calculateFields(data);
            } else {
                alertCaution('조회하신 날짜의 정산 내역이 없습니다.', 1);
                return false;
            }
        });
    }
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#searchBtn').on('click', function () {
            getDailyAcoountData();
        });

        $('#printBtn').on('click', function () {
            openDayBalancePop();
        });

        $('.pop__close').on('click', function () {
            $(this).parents('.pop').removeClass('active');
        })
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    fieldResetJson: {
        a1: '', a2: '', a3: '', a4: '', a5: '', a6: '',
        b0: '', b1: '', b2: '', b3: '', b4: '', b5: '', b6: '', b7: '', b8: '', b9: '',
        b10: '', b11: '', b12: '', b13: '', b14: '', b15: '', b16: '', b17: '',
        c1: '', c2: '', c3: '', c4: '', c5: '', c6: '', c7: '', c8: '', c9: '',
        c10: '', c11: '', c12: '', c13: '',
        d1: '', d2: '',
        add1: '', add2: '', add3: '', add4: '', add5: '', add6: '', add7: '',
        add8: '', add9: '', add11: '', add12: '', add10: '', add13: '',
    },
    currentData: {

    },
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

/* 서버에서 가져온 값들을 활용하여 표안의 계산 요소들의 값을 채워넣기 */
const calculateFields = function (data) {
    const calculatedData = {
        add1: data.a1 + data.a2 + data.a3,
        add2: data.a4 + data.a5,
        add3: data.a6,
        add4: data.a1,
        add5: data.a2 + data.a4 + data.a6,
        add6: data.a3 + data.a5,
        add7: data.a1 + data.a2 + data.a3 + data.a4 + data.a5 + data.a6,

        add8: data.b1 + data.b2 + data.b3 + data.b4 + data.b5,
        add9: data.b9 + data.b10 + data.b11 + data.b12 + data.b13,
        add11: data.b15,
        add12: data.b16 - data.b17,
        add10: data.b15 + data.b16,
        add13: data.b15 + data.b16 - data.b17,
    }

    setFieldsByEachJsonKey(calculatedData);

    /* 인쇄를 위해 계산된 데이터를 현재의 데이터 객체에 추가해두기 */
    const keys = Object.keys(calculatedData);
    for (const key of keys) {
        wares.currentData[key] = calculatedData[key];
    }
};

function openDayBalancePop() {
    const _params = {
        projectName: "toppos",
        formName: "franchiseDays2",
    };

    const _url = "https://report.topcleaners.kr:443/UBIFORM/UView5/index.jsp";
    let fullURL = _url + "?projectName=" + _params.projectName + "&formName=" + _params.formName + "&";
    fullURL += new URLSearchParams(wares.currentData).toString();

    $("#dayBalance").attr("src", fullURL);
    $("#balancePop").addClass("active");
}


/* 키값에 맞춰 해당하는 id에 값 넣기 */
const setFieldsByEachJsonKey = function (jsonObj) {
    jsonObj.d1 = jsonObj.d1 === "" ? "접속정보없음" : jsonObj.d1;
    jsonObj.d2 = jsonObj.d2 === "" ? "접속정보없음" : jsonObj.d2;
    const keys = Object.keys(jsonObj);
    for(const key of keys) {
        console.log("key", key);
        if (Number.isInteger(jsonObj[key])) {
            $('#' + key).html(jsonObj[key].toLocaleString());
        } else if (jsonObj[key].length === 19) {
            $('#' + key).html(jsonObj[key].substring(11, 19));
        } else {
            $('#' + key).html(jsonObj[key]);
        }
    }
};

const openReportTool = function () {

    if (!Object.keys(wares.currentData).length) {
        alertCaution('먼저, 정산이 완료된 날짜의 일일정산서 데이터를 조회해 주세요.');
        return;
    }

    // 프로젝트명
    const projectName = "toppos";
    // 서식명
    const formName = "franchiseDays2";

    //데이터셋 Object
    //데이터셋 Object에 생성된 DataSet 추가
    const datasetObject = {
        dataset_0: JSON.stringify(wares.currentData),
    };

    //파라미터 Object
    const paramObject = {};
    //파라미터
    //뷰어 오픈 공통 함수 호출
    fn_viewer_open(projectName, formName, datasetObject, paramObject);
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    enableDatepicker();
    trigs.basic();
};
