/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
import {activateBrFrListInputs} from '../../module/m_setBrFrList.js'

const dtos = {
    send: {
        templateSave: { // 6칸짜리 배열에 아래의 내용들이 세팅되어 API로 전송됩니다.
            id: "n",
            num: "n", // 각 가맹점의 템플릿 번호. 1~6번까지 있음
            subject: "s",
            message: "s",
        },
        messageCustomerList: {
            visitDayRange: "n", // 예를들어 7이 들어가면 일주일안에 이용했던 고객, 30이 들어가면 30일 안에 이용했던 고객, 단 0일경우 전체검색
        },
        messageSendCustomer: {
            bcIdList: "a", // 문자 발송의 대상이 되는 고객들의 bcId들이 담긴 리스트
            msgType: "s", // s는 일반 sms L은 lms (SMSQ_SEND 테이블의 msg_type에 넣어야 할 문자)
            hmMessage: "s",
            hmSendreqtimeDt: "n", // 예약발송인 경우에는 타임스탬프 숫자 형태로 전달 예정입니다. 아닐 경우 0을 넣어서 보낼 예정입니다.
        },
    },
    receive: {
        templateList: { // 템플릿 저장 API와 동일한 구조로 배열에 아래의 파라메터들이 옵니다. 해당 대리점에 등록된 데이터가 없다면 오류가 아닌 빈값이 오면 됩니다.
            hmNum: "n",
            hmSubject: "s",
            hmMessage: "s",
        },
        messageCustomerList: {
            bcId: "n",
            bcName: "s",
            bcHp: "s",
        },
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {

    getTemplate() {
        CommonUI.ajax("/api/head/templateList", "GET", false, function (res) {
            const templateList = res.sendData.gridListData;

            const $tmpHmSubject = $(".tmpHmSubject");
            const $tmpHmMessage = $(".tmpHmMessage");
            const $tmpByte = $(".tmpByte");
            const $tmpInputHmSubject = $(".tmpInputHmSubject");
            const $tmpInputHmMessage = $(".tmpInputHmMessage");
            const $tmpInputByte = $(".tmpInputByte");
            for(const obj of templateList) {
                const byte = getByteInfo(obj.hmMessage).byte;
                $tmpHmSubject.eq(parseInt(obj.hmNum, 10) - 1).val(obj.hmSubject);
                $tmpHmMessage.eq(parseInt(obj.hmNum, 10) - 1).val(obj.hmMessage);
                $tmpByte.eq(parseInt(obj.hmNum, 10) - 1).html(byte);
                $tmpInputHmSubject.eq(parseInt(obj.hmNum, 10) - 1).val(obj.hmSubject);
                $tmpInputHmSubject.eq(parseInt(obj.hmNum, 10) - 1).attr("data-id"
                    , obj.hmId ? obj.hmId : 0);
                $tmpInputHmMessage.eq(parseInt(obj.hmNum, 10) - 1).val(obj.hmMessage);
                $tmpInputByte.eq(parseInt(obj.hmNum, 10) - 1).html(byte);
            }
        });
    },
    saveTemplate(saveDataList) {
        CommonUI.ajax("/api/head/templateSave", "MAPPER", saveDataList, function () {
            alertSuccess("템플릿 저장에 성공하였습니다.");
            comms.getTemplate();
        });
    },

    sendMessage(message) {
        CommonUI.ajax("/api/head/messageSendCustomer", "PARAM", message, function () {
            alertSuccess("문자메시지 발송이 완료되었습니다.");
            grids.f.clear(0);
            grids.f.clear(1);
            $("#numberOfTargetCustomers").html("00000");
            comms.getCustomers(wares.getCondition);
        });
    },

    getCustomers(getCondition) {
        wares.getCondition = getCondition;
        CommonUI.ajax("/api/head/messageCustomerList", "GET", getCondition, function (res) {
            const data = res.sendData.gridListData;
            grids.f.set(0, data);
        });
    },
};

/*
*  .s : AUI 그리드 관련 설정들, 같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  .f : 그리드 관련 함수들 배치
* */
const grids = {
    s: { // 그리드 세팅
        id: [
            "grid_customersList", "grid_selectedCustomer"
        ],
        columnLayout: [],
        prop: [],
    },

    f: { // 그리드 펑션
        initialization() {
            grids.s.columnLayout[0] = [
                {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                }, {
                    dataField: "bcHp",
                    headerText: "전화번호",
                    width: 120,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonUI.formatTel(value);
                    }
                },
            ];

            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : true,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "bcName",
                    headerText: "고객명",
                    style: "grid_textalign_left",
                }, {
                    dataField: "bcHp",
                    headerText: "전화번호",
                    width: 120,
                    labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                        return CommonUI.formatTel(value);
                    }
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : false,
                rowHeight : 48,
                headerHeight : 48,
            };
        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grids.s.columnLayout) {
                AUIGrid.create(grids.s.id[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        get(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        set(numOfGrid, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clear(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
            AUIGrid.resize(grids.s.id[num]);
        },

        addRow(num, data) {
            AUIGrid.addRow(grids.s.id[num], data, "last");
        },

        getCheckedItems(num) {
            if(AUIGrid.getCheckedRowItems(grids.s.id[num]).length) {
                return AUIGrid.getCheckedRowItemsAll(grids.s.id[num]);
            }else{
                return AUIGrid.getSelectedRows(grids.s.id[num]);
            }
        },

        removeCheckedRow(num) {
            if(AUIGrid.getCheckedRowItems(grids.s.id[num]).length) {
                AUIGrid.removeCheckedRows(grids.s.id[num]);
            }else{
                AUIGrid.removeRow(grids.s.id[num], "selectedIndex");
            }
            AUIGrid.removeSoftRows(grids.s.id[num]);
        },

        clearFilter(num) {
            AUIGrid.clearFilterAll(grids.s.id[num]);
        },

        setFilter(num, type, string) {
            AUIGrid.setFilter(grids.s.id[num], "bcName", function(_dataField, _value, item) {
                let result = false;
                switch (type) {
                    case "0" :
                        result = item.bcName.includes(string) || item.bcHp.includes(string.replace(/-/g, ""));
                        break;
                    case "1" :
                        result = item.bcName.includes(string);
                        break;
                    case "2" :
                        result = item.bcHp.includes(string.replace(/-/g, ""));
                        break;
                }
                return result;
            });
        },

        getRowCount(num) {
            return AUIGrid.getRowCount(grids.s.id[num]);
        },
    },
};

const trigs = {
    basic() {
        AUIGrid.bind(grids.s.id[2], "cellClick", function (e) {
            const searchCondition = {
                insertYyyymmdd: e.item.insertYyyymmdd.numString(),
            };
            comms.getSendRecordDetail(searchCondition);
        });

        // 탭
        const $tabsBtn = $('.c-tabs__btn');
        const $tabsContent = $('.c-tabs__content');

        $tabsBtn.on('click', function() {
            const idx = $(this).index();

            $tabsBtn.removeClass('active');
            $tabsBtn.eq(idx).addClass('active');
            $tabsContent.removeClass('active');
            $tabsContent.eq(idx).addClass('active');
        });

        /* 메시지를 입력할 때 SMS문자 바이트 계산 및, 넘칠 때 자르기 */
        const $tmpInputHmMessage = $(".tmpInputHmMessage");
        const $tmpInputByte = $(".tmpInputByte");
        const $tmpHmMessage = $(".tmpHmMessage");
        const $tmpByte = $(".tmpByte");
        const $tmpRadio = $(".tmpRadio");
        for (let i = 0; i < $tmpInputHmMessage.length; i++) {
            $tmpInputHmMessage.eq(i).on("keyup", function () {
                const byteInfo = getByteInfo(this.value);
                if(byteInfo.cutLength) {
                    this.value = this.value.substring(0, byteInfo.cutLength);
                    byteInfo.byte = byteInfo.cutByte;
                }
                $tmpInputByte.eq(i).html(byteInfo.byte.toLocaleString());
            });

            $tmpRadio.eq(i).on("click", function () {
                $("#hmMessage").val($tmpHmMessage.eq(i).val());
                $("#byte").html($tmpByte.eq(i).html());
            });
        }

        $("#hmMessage").on("keyup", function () {
            const byteInfo = getByteInfo(this.value);
            if(byteInfo.cutLength) {
                this.value = this.value.substring(0, byteInfo.cutLength);
                byteInfo.byte = byteInfo.cutByte;
            }
            $("#byte").html(byteInfo.byte.toLocaleString());
        });

        $("#saveTemplateBtn").on("click", saveTemplate);

        $("#sendMessageBtn").on("click", function () {
            askSend();
        });

        $("#getCustomersBtn").on("click", function () {
            getCustomers();
        });

        $("#setCustomerFilterBtn").on("click", function () {
            setCustomerFilter();
        });

        $("#resetCustomerFilterBtn").on("click", function () {
            grids.f.clearFilter(0);
        });

        $("#filterString").on("keypress", function (e) {
            if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                setCustomerFilter();
            }
        });

        $("#insertCustomer").on("click", function () {
            const customerList = grids.f.getCheckedItems(0);
            grids.f.addRow(1, customerList);
            grids.f.removeCheckedRow(0);
            $("#numberOfTargetCustomers").html(grids.f.getRowCount(1).toString().padStart(5, "0"));
        });

        $("#removeCustomer").on("click", function () {
            const customerList = grids.f.getCheckedItems(1);
            grids.f.addRow(0, customerList);
            grids.f.removeCheckedRow(1);
            $("#numberOfTargetCustomers").html(grids.f.getRowCount(1).toString().padStart(5, "0"));
        });

        $("#getSendRecordSummaryBtn").on("click", function () {
            getSendRecordSummary();
        });

        $("input[name='isRangeSearch']").on("change", function (e) {
            if (e.currentTarget.value === "1") {
                $("#dateRangeSet").show();
            } else {
                $("#dateRangeSet").hide();
            }
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    getCondition: {},
};

function enableDatepicker() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 6);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt", "hmSendreqtimeDate"
    ];

    $("#" + datePickerTargetIds[0]).val(fromday);
    $("#" + datePickerTargetIds[1]).val(today);
    $("#" + datePickerTargetIds[2]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

/* SMS 기준 문자열의 byte 용량을 리턴 */
function getByteInfo(text) {
    let byte = 0;
    let testChar = "";
    let cutLength = 0;
    let cutByte = 0;

    for (let i=0; i < text.length; i++) {
        testChar = text.charAt(i);
        if (escape(testChar).length > 4) {
            byte += 2;
        } else {
            byte++;
        }
        if (byte === 1999 || byte === 2000) {
            cutLength = i + 1;
            cutByte = byte;
        }
    }

    return {
        byte,
        cutLength,
        cutByte,
    };
}

function saveTemplate() {
    const $tmpInputHmSubject = $(".tmpInputHmSubject");
    const $tmpInputHmMessage = $(".tmpInputHmMessage");

    const saveDataList = [];

    for(let i = 0; i < $tmpInputHmSubject.length; i++) {
        saveDataList.push({
            hmId: parseInt($tmpInputHmSubject.eq(i).attr("data-id"), 10),
            hmNum: i + 1,
            hmSubject: $tmpInputHmSubject.eq(i).val(),
            hmMessage: $tmpInputHmMessage.eq(i).val(),
        });
    }

    comms.saveTemplate(saveDataList);
}

function askSend() {
    const isLms = parseInt($("#byte").html(), 10) > 90;
    const numCustomer = $("#numberOfTargetCustomers").html().toInt();
    if (numCustomer) {
        const msg = isLms ? `${numCustomer.toLocaleString()}명의 고객께 문자를 전송합니다.<br>문자 길이가 90byte를 초과하므로<br>LMS로 문자를 전송하시겠습니까?`
            : `${numCustomer.toLocaleString()}명의 고객께 문자를 전송합니다.<br>SMS 문자 메시지를 발송하시겠습니까?`;
        alertCheck(msg);
        $("#checkDelSuccessBtn").on("click", function () {
            sendMessage();
            $('#popupId').remove();
        });
    } else {
        alertCaution("문자를 보낼 고객을 선택해 주세요.", 1);
    }
}

function sendMessage() {
    const dateString = $("#hmSendreqtimeDate").val().numString();
    const year = dateString.substring(0, 4).toInt();
    const month = dateString.substring(4, 6).toInt() - 1;
    const day = dateString.substring(6, 8).toInt();
    const hour = $("#hmSendreqtimeHour").val().toInt();
    const minute = $("#hmSendreqtimeMinute").val().toInt();
    const hmSendreqtimeDt = new Date(year, month, day, hour, minute).getTime();

    const isBookSend = $("#isBookSend").is(":checked");
    const bcIdList = [];
    const gridData = grids.f.get(1);
    for(const {bcId} of gridData) {
        bcIdList.push(bcId);
    }

    const sendData = {
        bcIdList,
        msgType: parseInt($("#byte").html(), 10) < 90 ? "S" : "L",
        hmMessage: $("#hmMessage").val(),
        hmSendreqtimeDt: isBookSend ? hmSendreqtimeDt : 0,
    };

    comms.sendMessage(sendData);
}

function getCustomers() {
    const isRangeSearch = $("input[name=isRangeSearch]:checked").val() === "1";
    const getCondition = {
        branchId: parseInt($('#brList').val()),
        franchiseId: parseInt($("#frList").val()),
        visitDayRange: isRangeSearch ? $("#visitDayRange").val() : "0",
    };
    comms.getCustomers(getCondition);
}

function setCustomerFilter() {
    const $filterString = $("#filterString");
    const type = $("#filterType").val();
    const string = $filterString.val();
    grids.f.setFilter(0, type, string);
    $filterString.val("");
    $filterString.trigger("focus");
}

function getSendRecordSummary() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
    };

    comms.getSendRecordSummary(searchCondition);
}

$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();

    trigs.basic();
    enableDatepicker();
    activateBrFrListInputs();
    comms.getTemplate();
}