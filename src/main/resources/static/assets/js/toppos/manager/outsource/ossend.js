import {grids, runOnlyOnce} from '../../module/m_outsourcing.js';

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        branchReceiptBranchInList: {
            frId: "n", // 0이 올 경우는 통합출고, 혹은 가맹점 전체선택인 경우
            filterFromDt: "s",
            filterToDt: "s",
            isOutsourceable: "s" // Y이 올 경우 외주처리대상 항목만, N인 경우 전체항목
        },
        branchStateChange: {
            miDegree: "n", // 몇차 출고인지에 대한 신호
            fdIdList: "a", // fdId의 목록이 담긴 배열(3차원 배열)
            fdS4TypeList: "a",
        },
        branchDispatchPrint: {
            miNoList: "a",
        }
    },
    receive: {
        managerBelongList: { // 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트
            frId: "nr",
            frName: "s",
            frTagNo: "s",
        },
        branchReceiptBranchInList: {
            fdId: "n", // 출고 처리를 위함
            frName: "s",
            frCode: "s",
            fdS2Dt: "s",
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
            fdEstimateDt: "s",
            fdTotAmt: "n",
            fdState: "s",
        },
        branchStateChange: {
            miNoList: "a",
        }
    }
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getFrList() {
        CommonUI.ajax('/api/manager/branchBelongList', 'GET', false, function (res) {
            const data = res.sendData.franchiseList;
            dv.chk(data, dtos.receive.managerBelongList, '가맹점 선택출고에 필요한 지점에 속한 가맹점 받아오기');
            const $frList = $('#frList');
            data.forEach(obj => {
                const htmlText = `<option value=${obj.frId}>${obj.frName}</option>`;
                $frList.append(htmlText);
            });
        });
    },

    getReceiptList(searchCondition) {
        dv.chk(searchCondition, dtos.send.branchReceiptBranchInList, '출고 품목 조회 조건 보내기');
        console.log(searchCondition);
        // CommonUI.ajax('', 'GET', searchCondition, function (res) {
        //     wares.receiptList = CommonUI.toppos.killNullFromArray(res.sendData.gridListData);
        //
        //     $('#statPanel').html(
        //         `[${$('#frList option:selected').html()}] 상품이 ${wares.receiptList.length}건 조회되었습니다.`);
        //     grids.f.clearData(0);
        //     grids.f.clearData(1);
        //     $('#inputTagNo').focus();
        // });
    },

    sendOutReceipt(sendList) {
        wares.sl = sendList;
        dv.chk(sendList, dtos.send.branchStateChange, '출고처리 항목 보내기');
        CommonUI.ajax(urls.sendOutReceipt, 'PARAM', sendList, function (res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.branchStateChange, '출고처리된 항목 출고증 인쇄 위한 miNoList 받아오기');
            alertCheck('출고처리가 완료 되었습니다.<br>출고증을 인쇄 하시겠습니까?');
            $('#checkDelSuccessBtn').on('click', function () {
                comms.dispatchPrint(data);
                $('#popupId').remove();
                $('#releaseListPrint').addClass('active');
            });
            grids.f.clearData(0);
            grids.f.clearData(1);
            wares.receiptList = '';
            $('#statPanel').html('조회버튼을 통해 상품을 조회하세요.');
            $('#exportXlsx').hide();
        });
    },

    dispatchPrint(miNoList) {
        dv.chk(miNoList, dtos.send.branchDispatchPrint, '출고증 인쇄를 위한 miNoList 보내기');
        CommonUI.ajax(urls.dispatchPrint, 'GET', miNoList, function (res) {
            for(const obj of res.sendData.issueDispatchDtos) {
                const frCode = obj['qrcode'];
                obj['qrcode'] = location.protocol+'//'+location.host+'/mobile/unAuth/qrpickup?frcode='+frCode;
            }
            dispatchPrintData(res.sendData.issueDispatchDtos);
        });
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    grid() {
        AUIGrid.bind(gridElemets.id[0], 'rowCheckClick', function (e) {
            listCheckChanged();
            if(e.item.fdS4Type === '99') {
                decideCheckResponse(e.item);
            } else if(['05', '06'].includes(e.item.fdS4Type)) {
                grids.f.restoreEditedRow(0, e.rowIndex);
            }
        });
    },

    basic() {
        $('#type01').on('click', function () {
            $('#frSelectUI').hide();
            $('#frList option').first().prop('selected', true);
        });

        $('#type02').on('click', function () {
            $('#frSelectUI').show();
        });

        $('#getListBtn').on('click', function () {
            getReceiptList();
        });

        $('#inputTagBtn').on('click', function () {
            inputTag();
        });

        $('#inputTagNo').on('keyup', function (e) {
            if(e.originalEvent.code === 'Enter' || e.originalEvent.code === 'NumpadEnter') {
                inputTag();
            }
        });

        $('#sendOutBtn').on('click', function () {
            wares.checkedItems = grids.f.getCheckedItems(0);
            if(wares.checkedItems.length) {
                alertCheck('선택된 상품을 출고처리 하시겠습니까?');
                $('#checkDelSuccessBtn').on('click', function () {
                    sendOut();
                    $('#popupId').remove();
                });
            } else {
                alertCaution('출고처리할 상품을 선택해 주세요.', 1);
            }
        });

        $('#addAll').on('click', function () {
            grids.f.addAllData();
            $('#inputTagNo').focus();
        });

        $('#clearUnchecked').on('click', function () {
            grids.f.removeUncheckedData();
            $('#inputTagNo').focus();
        });

        const $miDegree = $('#miDegree');
        $miDegree.on('keyup', function () {
            $miDegree.val($miDegree.val().substring(0, 2));
        });

        $('#exportXlsx').on('click', function () {
            grids.f.exportToXlsx();
        });
    },
};

const gridElemets = {
    id: ['grid_main', 'grid_selected'],
    columnLayout: [],
    prop: [],

    initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.
        /* 0번 그리드의 레이아웃 */
        gridElemets.columnLayout[0] = [
            {
                dataField: 'frName',
                headerText: '가맹점명',
                style: 'grid_textalign_left',
            }, {
                dataField: 'fdS2Dt',
                headerText: '지사입고일',
                width: 90,
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: 'fdUrgentYn',
                headerText: '급세탁',
                width: 55,
            }, {
                dataField: 'fdTag',
                headerText: '택번호',
                style: 'datafield_tag',
                width: 80,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.formatBrTagNo(value);
                },
            }, {
                dataField: 'productName',
                headerText: '상품명',
                style: 'grid_textalign_left',
                width: 200,
                renderer : {
                    type : 'TemplateRenderer',
                },
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    const colorSquare =
                        `<span class='colorSquare' style='background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;'></span>`;
                    const sumName = CommonUI.toppos.makeSimpleProductName(item);
                    item.productName = sumName;
                    return colorSquare + ` <span style='vertical-align: middle;'>` + sumName + `</span>`;
                },
            }, {
                dataField: 'processName',
                headerText: '처리내역',
                style: 'grid_textalign_left',
                width: 130,
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    item.processName = CommonUI.toppos.processName(item);
                    return item.processName;
                }
            }, {
                dataField: 'bcName',
                headerText: '고객',
                width: 100,
            }, {
                dataField: 'fdTotAmt',
                headerText: '접수금액',
                style: 'grid_textalign_right',
                width: 90,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'bpOutsourcingPrice',
                headerText: '외주금액',
                style: 'grid_textalign_right',
                width: 90,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdState',
                headerText: '현재상태',
                width: 90,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.name.fdState[value];
                },
            },
        ];

        /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
        * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
        * */
        gridElemets.prop[0] = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '존재하는 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: false,
            showRowCheckColumn: true,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : true,
            rowHeight : 48,
            headerHeight : 48,
        };

        gridElemets.columnLayout[1] = [
            {
                dataField: 'frName',
                headerText: '가맹점',
            }, {
                dataField: 'qty',
                width: 100,
                headerText: '수량',
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            },
        ];

        gridElemets.prop[1] = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '존재하는 데이터가 없습니다.',
            showAutoNoDataMessage: false,
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
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    receiptList: "",
    checkedItems: [],
    title: "지사출고", // 엑셀 다운로드 파일명 생성에 쓰인다.
    currentDetail: { // 디테일 그리드를 뿌리기 위해 선택된 가맹점과 일자의 정보. 엑셀 파일명 생성에 쓰인다.
        filterFromDt: "",
        filterToDt: "",
        outsourceable: "",
        frName: "",
    },
    sayLoop: false,
};

function getReceiptList() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
        isOutsourceable: $("#isOutsourceable").is(":checked") ? "Y" : "N",
        frId: 0,
    };
    wares.currentDetail.filterFromDt = searchCondition.filterFromDt;
    wares.currentDetail.filterToDt = searchCondition.filterToDt;
    wares.currentDetail.outsourceable = searchCondition.isOutsourceable === "Y" ? "외주대상" : "";

    if($("#type02").is(":checked")) {
        searchCondition.frId = parseInt($("#frList").val(), 10);
    }
    wares.currentDetail.frName = searchCondition.frId ? $("#frList option:selected").html() : "";

    comms.getReceiptList(searchCondition);
}

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    gridElemets.initialization();
    runOnlyOnce.enableDatepicker();

    grids.create(gridElemets.id[0], gridElemets.columnLayout[0], gridElemets.prop[0]);
    grids.create(gridElemets.id[1], gridElemets.columnLayout[1], gridElemets.prop[1]);

    trigs.grid();
    trigs.basic();

    comms.getFrList();
};