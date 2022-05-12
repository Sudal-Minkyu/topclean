/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchInspectionCurrentList: {
            franchiseId : "n",
            tagNo: "s",
            filterFromDt: "s",
            filterToDt: "s",
        },

        branchInspectionList: { // fdId가 일치하는 모든 검품 리스트 type 1: 검품등록시, 2: 검품확인시
            fdId: "nr",
            type: "s",
        },

        branchInspectionDelete: {
            fiId: "nr",
            fiPhotoYn: "sr",
        },

        branchInspectionInfo: {
            fiId: "nr",
        },
    },

    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr", // 가맹점 Id
            frName: "s",
            frTagNo: "s",
        },

        branchInspectionCurrentList: {
            fiCustomerConfirm: "s",
            fdPollutionType: "n",
            fdPollutionBack: "n",
            fiId: "n",
            fdId: "n", // 출고 처리를 위함
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
            fiAddAmtSum: "n",
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

        branchInspectionInfo: {
            inspeotInfoDto: {
                fiAddAmt: "n",
                fiComment: "s",
                fiCustomerConfirm: "s",
                fiId: "", // 오면 수정모드 안오면 신규등록
                fiPhotoYn: "s",
                fiSendMsgYn: "s",
            },
            photoList: {
                ffFilename: "s",
                ffId: "n",
                ffPath: "s",
                ffRemark: "d",
            }
        },
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getFrList: "/api/manager/branchBelongList",
    getMainGridList: "/api/manager/branchInspectionCurrentList",
    getInspectionList: "/api/manager/branchInspectionList",
    getInspectionNeo: "/api/manager/branchInspectionInfo",
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax(urls.getFrList, "GET", false, function (res) {
            const data = res.sendData.franchiseList;
            dv.chk(data, dtos.receive.managerBelongList, "지점에 속한 가맹점 받아오기");
            const $frList = $("#frList");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.frId}" data-tagno="${obj.frTagNo}">${obj.frName}</option>`;
                $frList.append(htmlText);
            });
            chkParams();
        });
    },

    getMainGridList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchInspectionCurrentList, "메인 그리드 검색 조건 보내기");
        CommonUI.ajax(urls.getMainGridList, "GET", searchCondition, function (res) {
            const data = CommonUI.toppos.killNullFromArray(res.sendData.gridListData);
            dv.chk(data, dtos.receive.branchInspectionCurrentList, "메인 그리드 받아온 리스트");
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

    getInspectionNeo(condition) {
        dv.chk(condition, dtos.send.branchInspectionInfo, "확인품 디테일 정보받기 위한 아이디 보내기");
        CommonUI.ajax(urls.getInspectionNeo, "GET", condition, function (res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.branchInspectionInfo, "선택된 확인품의 디테일 정보 받기");
            const refinedData = {
                fdId: wares.currentRequest.fdId,
                fiId: data.inspeotInfoDto.fiId,
                fiComment: data.inspeotInfoDto.fiComment,
                fiAddAmt: data.inspeotInfoDto.fiAddAmt,
                fiCustomerConfirm: data.inspeotInfoDto.fiCustomerConfirm,
                fiPhotoYn: data.inspeotInfoDto.fiPhotoYn,
                fiSendMsgYn: data.inspeotInfoDto.fiSendMsgYn,
                photoList: data.photoList,
            };

            wares.currentRequest = refinedData;

            $("#fiComment").val(wares.currentRequest.fiComment);
            $("#fiAddAmt").val(wares.currentRequest.fiAddAmt.toLocaleString());
            const $customerResponse = $("#customerResponse");
            $customerResponse.html(CommonData.name.fiCustomerConfirm[wares.currentRequest.fiCustomerConfirm])
                .removeClass().addClass("customerResponse" + wares.currentRequest.fiCustomerConfirm);

            for(const photo of wares.currentRequest.photoList) {
                if(wares.currentRequest.photoList.length) {
                    $("#frInspectViewPhotoPanel").show();
                } else {
                    $("#frInspectViewPhotoPanel").hide();
                }
                const photoHtml = `<li class="tag-imgs__item">
                    <a href="${photo.ffPath + photo.ffFilename}" data-lightbox="images" data-title="이미지 확대">
                        <img src="${photo.ffPath + "s_" + photo.ffFilename}" class="tag-imgs__img" alt=""/>
                    </a>
                </li>`;
                $("#photoList").append(photoHtml);
                $("#noImgScreen").hide();
            }
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
            "grid_main"
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
                    style: "datafield_tag",
                    width: 90,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.formatBrTagNo(value);
                    },
                }, {
                    dataField: "",
                    headerText: "상품명",
                    style: "grid_textalign_left",
                    renderer : {
                        type : "TemplateRenderer",
                    },
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
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
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
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
                    dataField: "fiAddAmtSum",
                    headerText: "추가금액",
                    style: "grid_textalign_right",
                    width: 90,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: "fdState",
                    headerText: "현재상태",
                    width: 90,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonData.name.fdState[value];
                    },
                }, {
                    dataField: "checkPopBtn",
                    headerText: "상세보기",
                    width: 80,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction(_rowIndex, _columnIndex, _value, _headerText, item ) {
                        let template = "";
                        if(item.fiCustomerConfirm === "1") {
                            template = `<button type="button" class="c-button c-button--supersmall c-button--solid">미정</button>`;
                        } else if(item.fiCustomerConfirm === "2") {
                            template = `<button type="button" class="c-button c-button--supersmall c-button--dark">진행</button>`;
                        } else if(item.fiCustomerConfirm === "3") {
                            template = `<button type="button" class="c-button c-button--supersmall c-button--red">반품</button>`;
                        }
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
                    dataField: "fiType",
                    headerText: "유형",
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item ) {
                        return CommonData.name.fiType[value];
                    },
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
                }, {
                    dataField: "fiCustomerConfirm",
                    headerText: "고객수락",
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item ) {
                        return CommonData.name.fiCustomerConfirm[value];
                    },
                },
            ];

            grids.s.prop[1] = {
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
        }
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
            const $aftTag = $("#aftTag");
            $("#frList").on("change", function () {
                $("#foreTag").val($("#frList option:selected").attr("data-tagno"));
                if($("#frList").val() === "") {
                    $aftTag.val("");
                    $aftTag.prop("disabled", true);
                } else {
                    $aftTag.prop("disabled", false);
                    const aftLength = 7 - $("#foreTag").val().length;
                    $("#aftTag").attr("maxlength", aftLength);
                    $("#aftTag").val("");
                }
            });

            $("#searchListBtn").on("click", function () {
                searchOrder();
            });

            $aftTag.on("keyup", function (e) {
                $aftTag.val($aftTag.val().numString());
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                    searchOrder();
                }
            });

            $("#closeCheckPop").on("click", function () {
                $("#checkPop").removeClass("active");
            });
        },
    },
    r: { // 이벤트 해제

    }
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    url: window.location.href,
    params: "", // url에 내포한 파라메터들을 담는다.
    currentRequest: {},
};

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

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
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

    const fullTag = $("#foreTag").val() + $("#aftTag").val().numString();

    const searchCondition = {
        tagNo: fullTag.length === 7 ? fullTag : "",
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
        franchiseId: parseInt(frId, 10),
    };

    if($("#aftTag").val().numString().length !== 0 && searchCondition.tagNo.length !==7) {
        alertCaution("택번호(뒤)는 완전히 입력하거나,<br>입력하지 말아주세요.(전체검색)<br>", 1);
        return;
    }

    comms.getMainGridList(searchCondition);
}

function openCheckPop(e) {
    resetCheckPop();
    $("#fdTotAmtInPut").val(wares.currentRequest.fdTotAmt.toLocaleString());
    const searchCondition = {
        fiId: e.item.fiId,
    };
    comms.getInspectionNeo(searchCondition);

    $("#checkPop").addClass("active");
}

function resetCheckPop() {
    $("#fdTotAmtInPut").val("");
    $("#fiAddAmt").val("");
    $("#fiComment").val("");
    $("#photoList").html("");
    $("#customerResponse").html("");
    $("#noImgScreen").show();
}

function ynStyle(_rowIndex, _columnIndex, value, _headerText, _item, _dataField) {
    return value === "Y" ? "yesBlue" : "noRed";
}

/* 넘어온 fdTag값이 있다면 해당 정보를 통해 검색한다. */
function chkParams() {
    const url = new URL(wares.url);
    wares.params = url.searchParams;

    if(wares.params.has("fdTag") && wares.params.has("fdS2Dt")) {
        $("#aftTag").prop("disabled", false);
        const fdTag = wares.params.get("fdTag");
        const dateNum = wares.params.get("fdS2Dt");
        const date = dateNum.substring(0, 4) + "-" + dateNum.substring(4, 6) + "-" + dateNum.substring(6, 8);
        $("#frList option[data-tagno=" + fdTag.substring(0, 2) + "]").prop("selected", true);
        $("#foreTag").val(fdTag.substring(0, 3));
        $("#aftTag").val(fdTag.substring(3, 7));
        $("#filterFromDt").val(date);
        $("#filterToDt").val(date);
        $("#searchListBtn").trigger("click");
    }
}
