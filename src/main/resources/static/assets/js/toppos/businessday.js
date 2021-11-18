
$(function() {
    /* 배열내의 각 설정만 넣어 빈 그리드 생성 */
    createGrids();

    setDataIntoGrid();
    /* 0번그리드 내의 셀 클릭시 이벤트 */
    AUIGrid.bind(gridId[0], "cellClick", function (e) {

    });
});


/* 그리드 생성과 운영에 관한 중요 변수들. grid라는 이름으로 시작하도록 통일했다. */
let gridId = [];
let gridTargetDiv = [];

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
    }, {
        dataField: "1",
        headerText: "월",
    }, {
        dataField: "2",
        headerText: "화",
    }, {
        dataField: "3",
        headerText: "수",
    }, {
        dataField: "4",
        headerText: "목",
    }, {
        dataField: "5",
        headerText: "금",
    }, {
        dataField: "6",
        headerText: "토",
    }
];

/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
* */
gridProp = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다.",
    enableColumnResize : false,
    showRowNumColumn : false,
    enableFilter : true
};


/* 인수로 온 배열의 설정에 따라 빈 그리드를 생성 */
function createGrids() {
    for (const i in gridTargetDiv) {
        gridId[i] = AUIGrid.create(gridTargetDiv[i], gridColumnLayout, gridProp);
    }
}

/* 한자리 숫자일 경우 앞에 0을 붙여 두자리로 해준다. */
function pad2(number) {
    return (number < 10 ? '0' : '') + number
}

/* ajax 통신을 통해 그리드 데이터를 받아와 뿌린다. */
function setDataIntoGrid() {

    let gotData = [];
    for(let i = 0; i < 12; i++) {
        for(let j = 0; j < 30; j++) {
            gotData.push({date : "2021" + this.pad2(i) + this.pad2(j)});
        }
    }
    const firstDay = new Date(2021, 1, 1);
    let dayOfWeek = firstDay.getDay();

    let currentMonth = 1;
    let processingData = [];
    let processedData = [];



    for(let i = 1; i < 13; i++) {
        for(let j = 1; j < 32; j++) {
            if(dayOfWeek === 7) {
                processedData[i-1].push(processingData);
                processingData = [];
                dayOfWeek = 0;
            }
            processingData.push();
            parseInt(data[i].date.substr(4, 2), 10);
        }
    }

    /*
    CommonUI.ajax(url, "GET", false, function (req) {
    });
    */
}

/*  */
function gridSave() {

}