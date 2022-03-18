/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchReceiptReturnList: {
            tagNo: "s",
            filterFromDt: "s",
            filterToDt: "s",
            franchiseId: "n", // 가맹점 Id
        },

        branchInspectionList: { // fdId가 일치하는 모든 검품 리스트 type 1: 검품등록시, 2: 검품확인시 
            fdId: "nr",
            type: "s",
        },

        branchInspectionSave: {
            fdId: "nr",
            fiAddAmt: "nr",
            fiComment: "s",
            fiType: "sr",
            source: "", // 검품사진파일
        },

        branchInspectionDelete: {
            fiId: "nr",
            fiPhotoYn: "sr",
        },
    },

    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr", // 가맹점 Id
            frName: "s",
            frTagNo: "s",
        },

        branchReceiptReturnList: {
            fdId: "n",
            frName: "s",
			insertDt: "s",
            fdS2Time: "s",
          
            fdTag: "s",
            fdColor: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",
            fdPriceGrade: "s",
            fdRetryYn: "s",
            fdPressed: "n",
            fdAdd1Amt: "n",
            fdAdd1Remark: "s",
            fdRepairAmt: "n",
            fdRepairRemark: "s",
            fdWhitening: "n",
            fdPollution: "n",
            fdWaterRepellent: "n",
            fdStarch: "n",
            fdUrgentYn: "s",
            bcName: "s",
            fdTotAmt: "n",
            fdState: "s",
            fdPreState: "s",
        },

        branchInspectionList: {
            fiId: "nr",
            fdId: "nr",
            fiType: "s",
            fiComment: "s",
            fiAddAmt: "n",
            fiPhotoYn: "s",
            fiSendMsgYn: "s",
            fiCustomerConfirm: "s",
            insertDt: "s",

            ffPath: "s",
            ffFilename: "s",
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getFrList: "/api/manager/branchBelongList",
    getMainGridList: "/api/manager/branchInspection",
    getInspectionList: "/api/manager/branchInspectionList",
    putNewInspect: "/api/manager/branchInspectionSave",
    deleteInspection: "/api/manager/branchInspectionDelete",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax(urls.getFrList, "GET", false, function (res) {
            const data = res.sendData.franchiseList;
            console.log(res);
            dv.chk(data, dtos.receive.managerBelongList, "지점에 속한 가맹점 받아오기");
            const $frList = $("#frList");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.frId}" data-tagno="${obj.frTagNo}">${obj.frName}</option>`
                $frList.append(htmlText);
            });
        });
    },

    getMainGridList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchReceiptReturnList, "메인 그리드 검색 조건 보내기");
        CommonUI.ajax(urls.getMainGridList, "GET", searchCondition, function (res) {
            const data = CommonUI.toppos.killNullFromArray(res.sendData.gridListData);
            console.log(data);
            grids.f.setData(0, data);
        });
    },

    getInspectionList(condition) {
        dv.chk(condition, dtos.send.branchInspectionList, "등록된 검품조회 조건");
        CommonUI.ajax(urls.getInspectionList, "GET", condition, function(res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.branchInspectionList, "등록된 검품의 조회");
            grids.f.clearData(1);
            grids.f.setData(1, data);
        });
    },

    deleteInspection(targets, fdId) {
        dv.chk(targets, dtos.send.branchInspectionDelete, "검품 삭제 목록");
        const data = {list: targets};
        CommonUI.ajax(urls.deleteInspection, "MAPPER", data, function(res) {
            const searchCondition = {
                fdId: fdId,
                type: "3"
            };
            comms.getInspectionList(searchCondition);
        });
    },

    putNewInspect(formData) {
        const testObj = Object.fromEntries(formData);
        testObj.fdId = parseInt(testObj.fdId);
        testObj.fiAddAmt = parseInt(testObj.fiAddAmt) | 0;
        
        dv.chk(testObj, dtos.send.branchInspectionSave, "확인품 등록");

        CommonUI.ajax(urls.putNewInspect, "POST", formData, function (res) {
            const searchCondition = {
                fdId: testObj.fdId,
                type: "3"
            };
            wares.currentRequest.fdState = "B";
            comms.getInspectionList(searchCondition);
            $("#fiComment").val("");
            $("#fiAddAmt").val("0");
            alertSuccess("확인품내역이 저장되었습니다.");
        });
    },
};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grids = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_main", "grid_check"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: "frName",
                    headerText: "가맹점",
                    style: "grid_textalign_left",
                }, {
                    dataField: "bcName",
                    headerText: "고객",
                    style: "grid_textalign_left",
                    width: 100,
                }, {
                    dataField: "insertDt",
                    headerText: "접수일시",
                    width: 150,
                    dataType: "date",
                }, {
                    dataField: "fdS2Time",
                    headerText: "지점입고일시",
                    width: 150,
                    dataType: "date",
                }, {
                    dataField: "fdTag",
                    headerText: "택번호",
                    width: 90,
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.formatTagNo(value);
                    },
                }, {
                    dataField: "",
                    headerText: "상품명",
                    style: "grid_textalign_left",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        const colorSquare =
                            `<span class="colorSquare" style="background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;"></span>`;
                        const sumName = CommonUI.toppos.makeSimpleProductName(item);
                        return colorSquare + ` <span style="vertical-align: middle;">` + sumName + `</span>`;
                    },
                }, {
                    dataField: "",
                    headerText: "처리내역",
                    style: "grid_textalign_left",
                    width: 130,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonUI.toppos.processName(item);
                    }
                }, {
                    dataField: "fdTotAmt",
                    headerText: "접수금액",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 90,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "fdPreState",
                    headerText: "이전상태",
                    width: 90,
                    labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "checkPopBtn",
                    headerText: "확인품",
                    width: 80,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        let template = `
                                <button type="button" class="c-button c-button--supersmall">등록</button>
                            `;
                        return template;
                    },
                }
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "존재하는 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : true,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "insertDt",
                    headerText: "등록일시",
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "fiComment",
                    headerText: "검품내용",
                    style: "grid_textalign_left",
                }, {
                    dataField: "fiSendMsgYn",
                    headerText: "메세지",
                    styleFunction: ynStyle,
                }, {
                    dataField: "fiPhotoYn",
                    headerText: "이미지",
                    styleFunction: ynStyle,
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "존재하는 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : true,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grids.s.columnLayout) {
                grids.s.id[i] = AUIGrid.create(grids.s.targetDiv[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        getData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        setData(numOfGrid, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clearData(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) { // 해당 배열 번호 그리드의 엑스트라 체크박스 선택된 (아이템 + 행번호) 객체 반환
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        },

        updateRowsById(numOfGrid, data) {
            AUIGrid.updateRowsById(numOfGrid, data);
        },

        getRemovedCheckRows(numOfGrid) {
            if(AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]).length){
                AUIGrid.removeCheckedRows(grids.s.id[numOfGrid]);
            }else{
                AUIGrid.removeRow(grids.s.id[numOfGrid], "selectedIndex");
            }
            return AUIGrid.getRemovedItems(grids.s.id[numOfGrid]);
        },

        resetChangedStatus(numOfGrid) {
            AUIGrid.resetUpdatedItems(grids.s.id[numOfGrid]);
        },
    },

    t: {
        basic() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
                if(e.dataField === "checkPopBtn") {
                    wares.currentRequest = e.item;
                    openCheckPop(e);
                }
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    s: { // 이벤트 설정
        basic() {
            $("#frList").on("change", function () {
                $("#foreTag").val($("#frList option:selected").attr("data-tagno"));
            });

            $("#searchListBtn").on("click", function () {
                searchOrder();
            });

            const $aftTag = $("#aftTag");
            $aftTag.on("keyup", function (e) {
                $aftTag.val($aftTag.val().numString());
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                    searchOrder();
                }
            });

            $("#commitInspect").on("click", function() {
                putInspect();
            });

            $("#closeCheckPop").on("click", function () {
                grids.f.updateRowsById(0, wares.currentRequest);
                $("#checkPop").removeClass("active");
                if(wares.isCameraExist) {
                    try {
                        wares.cameraStream.getTracks().forEach(function (track) {
                            track.stop();
                        });
                    }catch (e) {
                        CommonUI.toppos.underTaker(e, "checkregist : 카메라 스트림 찾아서 끄기");
                    }
                }
            });

            $("#fiAddAmt").on("keyup", function () {
                $(this).val($(this).val().toInt().toLocaleString());
            });

            $("#deleteInspection").on("click", function () { // 3번테이블(검품등록) 의 삭제될 대상들을 가져와서 삭제 요청
                const targetRowsItem = grids.f.getRemovedCheckRows(1);
                let refinedTargetList = [];
                let isAlreadyDone = false;
                targetRowsItem.forEach(item => {
                    if(item.fiSendMsgYn === "Y" || item.fiCustomerConfirm !== "1") {
                        isAlreadyDone = item.fiComment;

                    }
                    refinedTargetList.push({
                        fiId: item.fiId,
                        fiPhotoYn: item.fiPhotoYn,
                    });
                });
                if(isAlreadyDone) {
                    alertCaution("고객에게 정보가 전해진 검품내역은<br>삭제할 수 없습니다.<br>( 검품내용 : " 
                    + isAlreadyDone + " )", 1);
                    grids.f.resetChangedStatus(1);
                    return false;
                }

                if(targetRowsItem.length) {
                    comms.deleteInspection(refinedTargetList, targetRowsItem[0].fdId);
                    grids.f.resetChangedStatus(1);
                }
            });
        },
    },
    r: { // 이벤트 해제

    }
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    currentRequest: {},
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    grids.t.basic();
    enableDatepicker();
    comms.getFrList();
    trigs.s.basic();
}

function enableDatepicker() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 6);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(fromday);
    $("#" + datePickerTargetIds[1]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
} 

function searchOrder() {
    const frId = $("#frList").val();
    if(frId === "") {
        alertCaution("가맹점을 선택해 주세요.", 1);
        return false;
    }

    const searchCondition = {
        tagNo: $("#aftTag").val().numString(),
        filterFromDt: $("#filterFromDt").val(),
        filterToDt: $("#filterToDt").val(),
        franchiseId: parseInt(frId),
    };

    if(searchCondition.tagNo.length !== 0 && searchCondition.tagNo.length !== 4) {
        alertCaution("택번호는 완전히 입력하거나,<br>입력하지 말아주세요.(전체검색)", 1);
        return false;
    }

    comms.getMainGridList(searchCondition);
}

async function openCheckPop(e) {
    resetCheckPop();

    try {
        wares.isCameraExist = true;
        wares.cameraStream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: {
                width: {ideal: 4096},
                height: {ideal: 2160}
            },
        });

        const screen = document.getElementById("cameraScreen");
        screen.srcObject = wares.cameraStream;
    }catch (e) {
        if(!(e instanceof DOMException)) {
            CommonUI.toppos.underTaker(e, "checkregist : 카메라 찾기");
        }
        wares.isCameraExist = false;
    }

    
    $("#fdTotAmtInPut").val(wares.currentRequest.fdTotAmt.toLocaleString());
    if(wares.isCameraExist) {
        $("#isIncludeImg").prop("checked", true);
        $("#isIncludeImg").prop("disabled", false);
    }else{
        $("#isIncludeImg").prop("checked", false);
        $("#isIncludeImg").prop("disabled", true);
    }
    grids.f.resize(1);
    const searchCondition = {
        fdId: e.item.fdId,
        type: "3"
    }
    comms.getInspectionList(searchCondition);
    $("#checkPop").addClass("active");
}

function resetCheckPop() {
    $("#fiComment").val("");
    $("#fiAddAmt").val("0");
}

function ynStyle(rowIndex, columnIndex, value, headerText, item, dataField) {
    return value === "Y" ? "yesBlue" : "noRed"
}

function putInspect() {
    const $fiComment = $("#fiComment");
    if (!$fiComment.val().length) {
        alertCaution("검품내용을 입력해 주세요", 1);
        return false;
    }
    if(wares.isCameraExist && wares.cameraStream.active) {
        try {
            const formData = new FormData();
            formData.append("fdId", wares.currentRequest.fdId);

            if($("#isIncludeImg").is(":checked")) {
                const video = document.getElementById("cameraScreen");
                const canvas = document.getElementById('cameraCanvas');
                canvas.width = video.videoWidth;
                canvas.height = video.videoHeight;
                const context = canvas.getContext('2d');
                context.drawImage(video, 0, 0, canvas.width, canvas.height);
    
                const takenPic = canvas.toDataURL();
    
                const blob = b64toBlob(takenPic);
                formData.append("source", blob);
            }else{
                formData.append("source", null);
            }

            formData.append("fiComment", $fiComment.val());
            formData.append("fiType", "B");
            formData.append("fiAddAmt", $("#fiAddAmt").val().numString());

            comms.putNewInspect(formData);

        } catch (e) {
            CommonUI.toppos.underTaker(e, "checkregist : 카메라 촬영하려 검품등록");
        }
    }else{
        const formData = new FormData();
        formData.append("fdId", wares.currentRequest.fdId);
        formData.append("fiComment", $fiComment.val());
        formData.append("fiType", "B");
        formData.append("fiAddAmt", $("#fiAddAmt").val().numString());
        formData.append("source", null);
        comms.putNewInspect(formData);
    }
}

function b64toBlob(dataURI) { // 파일을 ajax 통신에 쓰기 위해 변환
    const byteString = atob(dataURI.split(',')[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: 'image/jpeg' });
}