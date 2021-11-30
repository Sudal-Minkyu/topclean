
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    /* 대상년도 text창에 년도를 세팅 */
    $("#target_year").val(targetYear);

    /* 입력되어 있는 년도를 기반으로 프론트 페이지 달려 제목 구성 및 기본 그리드 데이터를 생성하여 뿌린다 */
    setGridByYear();


    /* 아래 4개 이벤트 들은 AUI 그리드에서 일반적인 마우스 드래그 감지용 이벤트가 존재하지 않아 구현함 */
    $(".aui-grid-default-column").on("mouseenter", function (e){
        if(isDrag) {
            isClick = false;
            $(e.target).trigger("click");
        }
    });

    $(".aui-grid-default-column").on("mousedown", function (e){
        isClick = false;
        $(e.target).trigger("click");
        isClick = true;
    });

    $(window).on("mousedown", function (){
        isDrag = true;
    });

    $(window).on("mouseup", function (){
        isDrag = false;
    });

    /*
    * 달력 그리드의 셀을 클릭할 경우 셀이 선택되어있는지의 여부를 변경해 주어야 한다.
    * 이 때 각 그리드에 들어간 값은 19991231Y 20211119N 과 같은 형식인데 마지막 문자가 선택 여부를 뜻한다.
    * */
    for(let i = 0; i <12; i++) {
        AUIGrid.bind(gridId[i], "cellClick", function( event ) {
            if(isClick) {
                return false;
            }
            let item = event.item;
            let value = item[event.columnIndex];
            if(value !== undefined) {
                let isSelected = value[8];
                if(isSelected !== undefined) {
                    if (isSelected === "Y") {
                        item[event.columnIndex] = value.substr(0, 8) + "N";
                    } else {
                        item[event.columnIndex] = value.substr(0, 8) + "Y";
                    }
                    AUIGrid.updateRow(gridId[i], item, event.rowIndex);
                }
            }
        });
    }

});

/* 달력이 뿌려질 년도, 기본적으로 해당 년도를 따른다. */
let targetYear = new Date().getFullYear();

/* 지금이 드래그 중인 상태인지 판별을 위해 */
let isDrag = false;
/* 클릭상태인 경우 이벤트가 두번 동작하는걸 막는다. */
let isClick = false;
/* selectedStyle 과 showDay 함수가 연계하여 해당 대상이 선택유무에 따라 css 클래스를 바꿔주는 역할을 한다. */
let isSelected = false;

/* 그리드 생성과 운영에 관한 중요 변수들. grid라는 이름으로 시작하도록 통일했다. */
let gridId = [];
let gridTargetDiv = [];
let gridData = [];
/* 그리드를 뿌릴 대상 div의 id들 */
gridTargetDiv = [
    "grid_calendar_0", "grid_calendar_1", "grid_calendar_2", "grid_calendar_3", "grid_calendar_4", "grid_calendar_5",
    "grid_calendar_6", "grid_calendar_7", "grid_calendar_8", "grid_calendar_9", "grid_calendar_10", "grid_calendar_11"
];

/* 그리드를 받아올 때 쓰이는 api 배열 */
let gridCreateUrl = "";

/* 그리드의 레이아웃 */
let gridColumnLayout = [
    {
        dataField: "0",
        headerText: "일",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }, {
        dataField: "1",
        headerText: "월",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }, {
        dataField: "2",
        headerText: "화",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }, {
        dataField: "3",
        headerText: "수",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }, {
        dataField: "4",
        headerText: "목",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }, {
        dataField: "5",
        headerText: "금",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }, {
        dataField: "6",
        headerText: "토",
        labelFunction : showDay,
        styleFunction : selectedStyle,
    }
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp = {
    editable : false,
    selectionMode : "none",
    noDataMessage : "출력할 데이터가 없습니다.",
    enableColumnResize : false,
    showRowNumColumn : false,
    enableFilter : false,
    enableSorting : false,
    defaultColumnWidth : 40,
    width : 400,
};


/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids() {
    for (const i in gridTargetDiv) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout, gridProp);
    }
}

/* 한자리 숫자일 경우 앞에 0을 붙여 두자리로 해준다. */
function pad2(number) {
    return (number < 10 ? '0' : '') + number;
}

/* 각 필드에 19991231Y 같은 형식으로 담기는 데이터를 날짜만 남기기 위한 컬럼 속성(gridProp)내의 필터링 함수
*  여기서 isSelected 변수는 아래의 selectedStyle 함수에서 선택여부를 판별하기 위해 쓰인다. */
function showDay(rowIndex, columnIndex, value, headerText, item) {
    let resultValue = "";
    if(value !== undefined) {
        resultValue = parseInt(value.substr(6, 2), 10) + "";
        isSelected = value[8] === "Y";
    }else{
        isSelected = false;
    }
    return resultValue;
}

/* 컬럼의 스타일을 값의 상태에 따라 변경하기 위한 함수. 컬럼 속성(gridProp)내의 요소와 연동된다. */
function selectedStyle(rowIndex, columnIndex, value, headerText, item, dataField) {
    const setStyle = isSelected ? "cellSelected" : "cellNoSelected";
    return setStyle;
}

/* 선택 되어있는 날짜만 담아서 서버에 전달한다. */
function gridSave() {

    const aJsonArray = [];
    const aJson = {};
    aJson.year = $("#target_year").val();
    aJson.bcDate = "";
    aJson.bcDayoffYn = "";
    aJsonArray.push(aJson);

    for(let m = 0; m < 12; m++) { // 선택된 날짜만 json 형태로 담는다.
        const rawData = AUIGrid.getGridData(gridId[m]);

        for(let w = 0; w < rawData.length; w++) {
            for(let d = 0; d < 7; d++) {
                if(rawData[w][d] !== undefined && rawData[w][d].substr(8, 1) === "Y") {
                    const aJson = {};
                    aJson.bcDate = rawData[w][d].substr(0, 8);
                    aJson.bcDayoffYn = "Y";
                    aJsonArray.push(aJson);
                }
            }
        }
    }

    const sJson = JSON.stringify(aJsonArray);

    const url = "/api/manager/calendarSave";
    CommonUI.ajaxjson(url, sJson,function (){

    });


    for(let m = 0; m < 12; m++) {
        AUIGrid.resetUpdatedItems(gridId[m]);
    }

    /*
    for(let m = 0; m < 12; m++) { // 업데이트 된 데이터만 호출하여 통신하는 방식
        const updatedItems = AUIGrid.getEditedRowColumnItems(gridId[m]);
        for(let i = 0; i < updatedItems.length; i++) {
            for(let j = 0; j < 7; j++) {
                const rawData = updatedItems[i][j];
                if(rawData !== undefined) {
                    data.push({date : rawData.substr(0,8), selected : rawData[8]});
                }
            }
        }
        AUIGrid.resetUpdatedItems(gridId[m]);
    }
    */
}

/* 년도에 맞춰서 달력의 제목부분을 뿌려주고, 이어서 데이터를 받아오는 함수를 호출한다. */
function setGridByYear() {
    targetYear = $("#target_year").val();
    let calendarLabels = $(".calendar_label");
    for(let m = 0; m < 12; m++) {
        calendarLabels.eq(m).html(targetYear + "년 " + (m + 1) + "월");
    }

    // 서버로 보낼 데이터 작성
    const params = {
        "targetYear" : targetYear,
    };
    const url = "/api/manager/calendarInfo";
    CommonUI.ajax(url, "GET", params, function (req) {
        const daysData = req.sendData.gridListData;
        if(daysData.length) {
            setDataIntoGrid(daysData);
        }else{
            alertMiddleSaveCheck("기존 데이터가 없습니다.<br> 기본 데이터를 생성하시겠습니까?");

            $("#checkYesBtn").on("click", function() {
                setDataIntoGrid({});
                gridSave();
                $('#popupId').remove();
            });

            $("#checkNoBtn").on("click", function() {
                for(let m = 0; m < 12; m++) {
                    AUIGrid.clearGridData(gridId[m]);
                }
                $('#popupId').remove();
            });
        }
    });
}

function setDataIntoGrid(daysData) {
    /* 아래의 변수와 반복 문 내 작업으로 기본적인 달력 데이터들을 생성해 준다. */
    let firstDayOfWeek = [];
    let lastDayOfMonth = [];
    let processDayOfWeek;
    let processData = {};
    let processDataOfMonth = [];

    let isSaveExists = daysData.length > 0;
    let dayOfYear = 0;

    /* 해당 년도의 각 월마다 며칠부터 며칠까지 존재하는지를 판별하여 달력을 만든다. */
    for(let m = 0; m < 12; m++) {
        /* 월의 시작 날 */
        firstDayOfWeek[m] = new Date(targetYear, m, 1).getDay();
        /* 월의 마지막 날 */
        lastDayOfMonth[m] = new Date(targetYear, m + 1, 0).getDate();

        /* 그리드에 넣기 위해 각 요일와 열에 맞추어 세팅 */
        processDayOfWeek = firstDayOfWeek[m];
        for(let d = 1; d <= lastDayOfMonth[m]; d++) {

            /* 기존 데이터가 존재하면 기존 데이터를 기반으로 설정하고, 아닐 경우 날짜를 생성하여 담는다. */
            if(isSaveExists) {
                processData[processDayOfWeek+""] = daysData[dayOfYear].bcDate + daysData[dayOfYear++].bcDayoffYn;
            }else{
                processData[processDayOfWeek+""] = targetYear + pad2(m + 1) + pad2(d) + (processDayOfWeek === 0 ? "Y" : "N");
            }

            if(processDayOfWeek >= 6 || d===lastDayOfMonth[m]) {
                processDataOfMonth.push(processData);
                processDayOfWeek = 0;
                processData = {};
            }else{
                processDayOfWeek++;
            }
        }
        gridData[m] = processDataOfMonth;
        AUIGrid.setGridData(gridId[m], gridData[m]);
        processDataOfMonth = [];
    }
}