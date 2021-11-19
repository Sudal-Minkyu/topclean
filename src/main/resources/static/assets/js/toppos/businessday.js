
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    setDataIntoGrid();


    $(".aui-grid-default-column").on("mouseenter", function (e){
        if(isDrag) {
            $(e.target).trigger("click");
        }
    });

    $(".aui-grid-default-column").on("mousedown", function (e){
        $(e.target).trigger("click");
    });

    $(window).on("mousedown", function (){
        isDrag = true;
    });

    $(window).on("mouseup", function (){
        isDrag = false;
    });


    for(let i = 0; i <12; i++) {
        AUIGrid.bind(gridId[i], "cellClick", function( event ) {
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

let isDrag = false;
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

/* 0번 그리드의 레이아웃 */
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
    enableFilter : false
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

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid() {

    let targetYear = 2021;
    let firstDayOfWeek = [];
    let lastDayOfMonth = [];
    let processDayOfWeek;
    let processData = {};
    let processDataOfMonth = [];

    for(let m = 1; m <= 12; m++) {
        firstDayOfWeek[m-1] = new Date(targetYear, m-1, 1).getDay();
        lastDayOfMonth[m-1] = new Date(targetYear, m, 0).getDate();
        processDayOfWeek = firstDayOfWeek[m-1];
        for(let d = 1; d <= lastDayOfMonth[m-1]; d++) {
            processData[processDayOfWeek+""] = targetYear+pad2(m)+pad2(d)+"N";
            if(processDayOfWeek >= 6 || d===lastDayOfMonth[m-1]) {
                processDataOfMonth.push(processData);
                processDayOfWeek = 0;
                processData = {};
            }else{
                processDayOfWeek++;
            }
        }
        gridData[m-1] = processDataOfMonth;
        AUIGrid.setGridData(gridId[m-1], gridData[m-1]);
        processDataOfMonth = [];
    }
}



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

function selectedStyle(value, footerValues) {
    const setStyle = isSelected ? "cellRed" : "cellWhite";
    return setStyle;
}

/*  */
function gridSave() {

    let data = [];

    for(let m = 0; m < 12; m++) {
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

    console.log(data);
}