let gridID = [];
let targetDiv = [];
let gridData = [];
let columnLayout = [];

let gridOption = {
    editable : false,
    selectionMode : "multipleCells",
    noDataMessage : "출력할 데이터가 없습니다."
};

targetDiv = [
    "grid_contract_one", "grid_contract_two", "grid_contract_three", "grid_contract_four"
];


columnLayout[0] = [
    {
        dataField: "",
        headerText: "순번",
    }, {
        dataField: "brName",
        headerText: "지사명",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    },
];

columnLayout[1] = [
    {
        dataField: "",
        headerText: "순번",
    }, {
        dataField: "frName",
        headerText: "가맹점명",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "",
        headerText: "배정지사명",
    },
];

columnLayout[2] = [
    {
        dataField: "",
        headerText: "순번",
    }, {
        dataField: "brName",
        headerText: "지사명",
    }, {
        dataField: "brContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "brContractToDt",
        headerText: "계약종료일",
    },
];

columnLayout[3] = [
    {
        dataField: "",
        headerText: "순번",
    }, {
        dataField: "frName",
        headerText: "가맹점명",
    }, {
        dataField: "frContractFromDt",
        headerText: "계약시작일",
    }, {
        dataField: "frContractToDt",
        headerText: "계약종료일",
    }, {
        dataField: "",
        headerText: "배정지사명",
    },
];

function createGrid(columnLayout, gridOption) {
    for (const i in columnLayout) {
        gridID[i] = AUIGrid.create(targetDiv[i], columnLayout[i], gridOption);
    }

    /* <=========================> */
    /* 각 그리드의 url */
    // const gridUrl = [
    // 		"url1", "url2", "url3", "url4"
    // ]
    //
    // for (let i=0; i<2; i++) {
    // 	setData(gridUrl[i], i);
    // }
}

function setData(url, numOfGrid) {
    $(document).ajaxSend(function(e, xhr)
    { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });
    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            gridData[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(gridID[numOfGrid], gridData[numOfGrid]);
        }
    });
}

// 지사, 가맹점 코드 중복확인 체크함수
function codeOverlap(num){

    let url;
    let params;

    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",
            localStorage.getItem('Authorization')); });


    if(num===1){
        console.log("지사 코드 중복확인");
        url = "/api/head/branohOverlap";
        params = {
            brCode: $("#brCode").val()
        };
    }else{
        console.log("가맹점 코드 중복확인");
        url = "/api/head/franohiseOverlap";
        params = {
            frCode: $("#frCode").val()
        };
    }

    $.ajax({
        url: url,
        type: 'GET',
        cache: false,
        data: params,
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            if (req.status === 200) {
                if(num===1){
                    $("#brCodeChecked").val("1");
                }else{
                    $("#frCodeChecked").val("1");
                }
                console.log("중복환인 완료");
                alertSuccess("사용할 수 있는  코드입니다.");
            }else{
                if (req.err_msg2 === null) {
                    alertCaution(req.err_msg, 1);
                }else{
                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                }
            }
        }
    });
}

// 가맹점 저장함수
function franohiseSave(){

    const $frCodeChecked = $("#frCodeChecked").val();
    if($frCodeChecked==="0"){
        alertCaution("가맹점코드 중복확인을 해주세요.",1);
        return false;
    }
    $(document).ajaxSend(function(e, xhr) { xhr.setRequestHeader("Authorization",localStorage.getItem('Authorization')); });

    const formData = new FormData(document.getElementById('frFormData'));

    let url = "/api/head/franohiseSave";

    $.ajax({
        url: url,
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        error: function (req) {
            ajaxErrorMsg(req);
        },
        success: function (req) {
            if (req.status === 200) {
                console.log("저장 완료");
                alertSuccess("가맹점 저장완료");
            }else {
                if (req.err_msg2 === null) {
                    alertCaution(req.err_msg, 1);
                } else {
                    alertCaution(req.err_msg + "<br>" + req.err_msg2, 1);
                }
            }
        }
    });
}

function fromToValidation(){

    let from = Date.parse($("#brContractFromDt").val());
    let to = $("#brContractToDt").val();


    console.log(from);
    console.log(to);
}