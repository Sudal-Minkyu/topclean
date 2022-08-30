/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */

import {activateBrFrListInputs} from '../../module/m_setBrFrList.js'

const dtos = {
    send: {
        searchList: {
            filterDt: 's', // 행사 적용 일자
            branchId: 'n', // 지사 아이디
            franchiseId: 'n', // 가맹점 아이디
            hpName: 's', // 행사명
            hpStatus: 's', // 행사진행여부  '00'전체  '01'진행  '02'종료
        },

        savePromoData: {
            hbPromotion: {
                hpId: 'n', //행사 아이디
                hpName: 's', // 행사명
                hpType: 's', // 행사유형
                hpStatus: 's', // 진행여부
                hpContent: 's', // 행사내용
                hpStart: 's', // 시작 행사기간
                hpEnd: 's', // 종료 행사기간
                hpWeekend: 's', // 행사 적용 요일
            },

            hbPromotionFranchise: {
                franchiseId: 'n', // 가맹점 아이디
                frCode: 's',
            },

            hbPromotionItem: {
                biItemcode: 's', // 최종 상품 코드
                hiDiscountRt: 'n', // 행사 할인율. x + 1 행사시에는 0으로 저장
            },
        },

        headPromotionSub: {
            hpId: 'n',
        },
    },
    receive: {
        searchList: {
            hpId: 'n',
            hpType: 's',
            hpName: 's',
            hpStart: 's',
            hpEnd: 's',
            hpWeekend: 's',
            hpStatus: 's',
            hpContent: 's',
        },

        headContractList: {
            brName: 's', // 지사명
            children: {
                franchiseId: 'n', // 가맹점 아이디
                frCode: 's',
                brName: 's', // 지사명
                frName: 's', // 가맹점명
            }
        },

        headPromotionSub: {
            hbPromotionFranchise: {
                franchiseId: 'n',
                frCode: 's',
            },
            hbPromotionItem: {
                biItemcode: 's',
                hiDiscountRt: 'n',
            },
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    /* 지사, 가맹점 계층형 선택 그리드에 쓰이는 데이터를 가져온다. */
    getInitialData() {
        CommonUI.ajax('/api/head/headContractList', 'GET', false, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.headContractList, '지사와 소속 가맹점의 리스트 받아오기');
            /* 현재 지사가 보유한 가맹점이 0일 경우 배열에서 제거 */
            for (let i = 0; i < data.length; i++) {
                if (!data[i].children.length) {
                    data.splice(i--, 1);
                }
            }
            grids.set(1, data);
        });

        CommonUI.ajax('/api/head/headItemList', 'GET', false, function (res) {
            const data = res.sendData.gridListData;
            for (let i = 0; i < data.length; i++) {
                if (!data[i].children.length) {
                    data.splice(i--, 1);
                } else {
                    for (let j = 0; j < data[i].children.length; j++) {
                        if (!data[i].children[j]) {
                            data[i].children.splice(j--, 1);
                            if (!data[i].children.length) {
                                data.splice(i--, 1);
                            }
                        }
                    }
                }
            }

            grids.set(3, data);
        });
    },

    /* 조건을 받아 조회된 내용을 그리드에 표출한다. */
    searchList(filterCondition) {
        dv.chk(filterCondition, dtos.send.searchList, '조회시 보내는 정보');
        CommonUI.ajax('/api/head/headPromotionList', 'GET', filterCondition, function (res) {
            wares.filterCondition = filterCondition;
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.searchList, '조회시 받아오는 정보');
            grids.set(0, data);
        });
    },

    /* 선택된 행사의 세부 정보를 받아와 현재의 . */
    getDetailPromoData(targetCondition, callback) {
        dv.chk(targetCondition, dtos.send.headPromotionSub, '목표 행사 아이디 정보');
        CommonUI.ajax('/api/head/headPromotionSub', 'GET', targetCondition, function (res) {
            const data = res.sendData;
            dv.chk(data, dtos.receive.headPromotionSub, '행사 세부 내용 (가맹점, 품목) 정보');
            wares.currentRequest.hbPromotionFranchise = data.hbPromotionFranchise;
            wares.currentRequest.hbPromotionItem = data.hbPromotionItem;
            return callback();
        });
    },

    /* 행사 내용을 저장, 업데이트 한다. */
    savePromoData(saveData) {
        console.log('저장시 데이터', saveData);
        dv.chk(saveData, dtos.send.savePromoData, '행사내용 저장/업데이트 정보 보내기');
        CommonUI.ajax('/api/head/headPromotionSave', 'MAPPER', saveData, function () {
            alertSuccess('행사 내용을 저장하였습니다.');
            /* 수정을 통해 진입한 행사일 경우는 기존 검색조건대로 검색, 아닐 경우 오늘 날짜 기본 상태로 검색 */
            if (wares.currentRequest.hbPromotion) {
                comms.searchList(wares.filterCondition);
            } else {
                const today = new Date().format('yyyyMMdd');
                const todayCommonCondition = {
                    filterDt: today,
                    branchId: 0,
                    franchiseId: 0,
                    hpName: "",
                    hpStatus: "00"
                };
                comms.searchList(todayCommonCondition);
            }

            closePromoInputPop();
        });
    },
};

/* AUI 그리드 관련 설정과 함수들 */
const grids = {
    id: [
        'grid_main', 'grid_fr_list', 'grid_fr_apply', 'grid_item_list', 'grid_item_apply',
    ],
    columnLayout: [],
    prop: [],

    /* 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다. */
    initialization() {

        /* 0번 그리드의 레이아웃 */
        grids.columnLayout[0] = [
            {
                dataField: 'hpId',
                headerText: '행사<br>번호',
                width: 70,
            }, {
                dataField: 'hpName',
                headerText: '행사명',
                width: 200,
                style: 'grid_textalign_left',
            }, {
                dataField: 'hpType',
                headerText: '유형',
                width: 70,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.name.hpType[value];
                },
            }, {
                dataField: 'hpStart',
                headerText: '시작일시',
                width: 150,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return parseDate(value, 'string');
                },
            }, {
                dataField: 'hpEnd',
                headerText: '종료일시',
                width: 150,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return parseDate(value, 'string');
                },
            }, {
                dataField: 'hpStatus',
                headerText: '진행여부',
                width: 70,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.name.hpStatus[value];
                },
            }, {
                dataField: '',
                headerText: '행사요일',
                children: [
                    {
                        dataField: 'hpWeekend',
                        headerText: '일',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, item) {
                            /* 일요일인 첫 루프에서 선택된 행사 요일의 이진형 문자 데이터를 해석 */
                            item.selectedDayOfWeek = getSelectedHpWeekendIntArray(value);
                            return item.selectedDayOfWeek.includes(64) ? '√' : '';
                        },
                    }, {
                        dataField: 'selectedDayOfWeek',
                        headerText: '월',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value.includes(32) ? '√' : '';
                        },
                    }, {
                        dataField: 'selectedDayOfWeek',
                        headerText: '화',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value.includes(16) ? '√' : '';
                        },
                    }, {
                        dataField: 'selectedDayOfWeek',
                        headerText: '수',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value.includes(8) ? '√' : '';
                        },
                    }, {
                        dataField: 'selectedDayOfWeek',
                        headerText: '목',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value.includes(4) ? '√' : '';
                        },
                    }, {
                        dataField: 'selectedDayOfWeek',
                        headerText: '금',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value.includes(2) ? '√' : '';
                        },
                    }, {
                        dataField: 'selectedDayOfWeek',
                        headerText: '토',
                        width: 30,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value.includes(1) ? '√' : '';
                        },
                    },
                ],
            }, {
                dataField: 'hpContent',
                headerText: '행사 내용',
                style: 'grid_textalign_left',
            }, {
                dataField: 'modifyBtn',
                headerText: '수정',
                width: 80,
                renderer : {
                    type: "TemplateRenderer",
                },
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, _item ) {
                    return `<button class="c-state c-state--modify">수정</button>`;
                },
            },
        ];

        /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
        * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
        * */
        grids.prop[0] = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: false,
            showRowCheckColumn: false,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            showEditedCellMarker: false,
            rowHeight : 30,
            headerHeight : 30,
        };

        grids.columnLayout[1] = [
            {
                dataField: '',
                headerText: '이름',
                style: 'grid_textalign_left',
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    return item.frName ? item.frName : item.brName;
                },
            },
        ];

        grids.prop[1] = {
            displayTreeOpen : false,
            rowCheckDependingTree : true,
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: true,
            showRowCheckColumn: true,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            showEditedCellMarker: false,
            rowHeight : 30,
            headerHeight : 30,
        };

        grids.columnLayout[2] = [
            {
                dataField: 'brName',
                headerText: '지사명',
                style: 'grid_textalign_left',
            }, {
                dataField: 'frName',
                headerText: '가맹점명',
                style: 'grid_textalign_left',
            },
        ];

        grids.prop[2] = {
            softRemoveRowMode: false,
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: true,
            showRowCheckColumn: true,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            showEditedCellMarker: false,
            rowHeight : 30,
            headerHeight : 30,
        };

        grids.columnLayout[3] = [
            {
                dataField: '',
                headerText: '이름',
                style: 'grid_textalign_left',
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    return item.biName ? item.biName : item.bsName ? item.bsName : item.bgName;
                },
            },
        ];

        grids.prop[3] = {
            displayTreeOpen : false,
            rowCheckDependingTree : true,
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: true,
            showRowCheckColumn: true,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            showEditedCellMarker: false,
            rowHeight : 30,
            headerHeight : 30,
        };

        grids.columnLayout[4] = [
            {
                dataField: 'bgName',
                headerText: '대분류',
                width: 120,
                style: 'grid_textalign_left',
                editable : false,
            }, {
                dataField: 'bsName',
                headerText: '중분류',
                width: 75,
                style: 'grid_textalign_left',
                editable : false,
            }, {
                dataField: 'biName',
                headerText: '상품명',
                style: 'grid_textalign_left',
                editable : false,
            }, {
                dataField: 'hiDiscountRt',
                headerText: '할인율(%)',
                width: 75,
                style: 'grid_textalign_right',
                dataType: 'numeric',
                editRenderer: {
                    type: 'InputEditRenderer',
                    autoThousandSeparator: 'false',
                    maxlength: 3,
                    onlyNumeric: true,
                    allowPoint: false,
                    allowNegative: false,
                },
                labelFunction(_rowIndex, _columnIndex, value, _headerText, item) {
                    if (value > 100) {
                        item.hiDiscountRt = 100;
                    }
                    return item.hiDiscountRt;
                },
            },
        ];

        grids.prop[4] = {
            softRemoveRowMode: false,
            editable : true,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: true,
            showRowCheckColumn: true,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            showEditedCellMarker: false,
            rowHeight : 30,
            headerHeight : 30,
        };

    },

    /* 그리드 동작 처음 빈 그리드를 생성 */
    create() {
        for (const i in grids.columnLayout) {
            AUIGrid.create(grids.id[i], grids.columnLayout[i], grids.prop[i]);
        }
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    get(gridNum) {
        return AUIGrid.getOrgGridData(grids.id[gridNum]);
    },

    /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
    set(gridNum, data) {
        AUIGrid.setGridData(grids.id[gridNum], data);
    },

    /* 해당 배열 번호 그리드의 데이터 비우기 */
    clear(gridNum) {
        AUIGrid.clearGridData(grids.id[gridNum]);
    },

    /* 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절 */
    resize(gridNum) {
        AUIGrid.resize(grids.id[gridNum]);
    },

    /* 체크된 아이템들을 가져온다. */
    getCheckedItems(gridNum) {
        return AUIGrid.getCheckedRowItemsAll(grids.id[gridNum]);
    },

    /* 그리드 스크롤을 존재하는 마지막 줄로 내린다. */
    setLastRowPosition(gridNum) {
        return AUIGrid.setRowPosition(grids.id[gridNum], AUIGrid.getRowCount(grids.id[gridNum]) - 1);
    },

    /* 특정 키값이 일치하면 체크한다. */
    checkRowsByValue(gridNum, valueKey, valueArray) {
        AUIGrid.addCheckedRowsByValue(grids.id[gridNum], valueKey, valueArray);
    },

    /* 특정 키값이 일치하면 체크를 푼다. */
    uncheckRowsByValue(gridNum, valueKey, valueArray) {
        AUIGrid.addUncheckedRowsByValue(grids.id[gridNum], valueKey, valueArray);
    },

    /* 그리드 아이디가 일치하는 항목의 체크를 푼다. */
    uncheckRowsById(gridNum, uidArray) {
        AUIGrid.addUncheckedRowsByIds(grids.id[gridNum], uidArray);
    },

    /* 그리드 아이디가 일치하는 항목을 체크한다. */
    checkRowsById(gridNum, uidArray) {
        AUIGrid.addCheckedRowsByIds(grids.id[gridNum], uidArray);
    },

    removeRowByRowId(gridNum, rowIdArray) {
        AUIGrid.removeRowByRowId(grids.id[gridNum], rowIdArray);
    },

    isCheckedByRowId(gridNum, rowId) {
        return AUIGrid.isCheckedRowById(grids.id[gridNum], rowId);
    },

    /* 그리드에 표시되는 아이템 갯수의 반환 */
    getCount(gridNum) {
        return AUIGrid.getRowCount(grids.id[gridNum]);
    },

    /* 그리드의 체크박스를 전체 체크 혹은 해제 */
    setAllCheckedRows(gridNum, booleanAllOnOff) {
        AUIGrid.setAllCheckedRows(grids.id[gridNum], booleanAllOnOff);
    },

    updateRowsById(gridNum, items) {
        AUIGrid.updateRowsById(grids.id[gridNum], items);
    },

    updateRow(gridNum, keyNValue, rowIndex) {
        AUIGrid.updateRow(grids.id[gridNum], keyNValue, rowIndex);
    },

    /* 3번 그리드의 biItemcode가 일치하는 행의 hiDiscountRt값을 업데이트한다. */
    updateHiDiscountRtByBiItemcode(biItemcode, hiDiscountRt) {
        const item = AUIGrid.getItemsByValue(grids.id[3], 'biItemcode', biItemcode)[0];
        item.hiDiscountRt = hiDiscountRt;
        grids.updateRowsById(3, item);
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        AUIGrid.bind(grids.id[0], 'cellClick', function (e) {
            if (e.dataField === 'modifyBtn') {
                wares.currentRequest.hbPromotion = e.item;
                onModify();
            }
        });

        AUIGrid.bind(grids.id[1], 'rowCheckClick', function () {
            setCheckedItemsToRightGrid(1);
        });

        AUIGrid.bind(grids.id[1], 'rowAllCheckClick', function () {
            setCheckedItemsToRightGrid(1);
        });

        AUIGrid.bind(grids.id[3], 'rowCheckClick', function () {
            setCheckedItemsToRightGrid(3);
        });

        AUIGrid.bind(grids.id[3], 'rowAllCheckClick', function () {
            setCheckedItemsToRightGrid(3);
        });


        AUIGrid.bind(grids.id[4], 'cellEditEnd', function (e) {
            if (e.dataField === 'hiDiscountRt') {
                grids.updateHiDiscountRtByBiItemcode(e.item.biItemcode, e.value);
            }
        });

        $('#searchListBtn').on('click', function () {
            searchList();
        });

        $('#addPromoBtn').on('click', function () {
            openPromoInputPop();
        });

        $('#closePromoPopBtn').on('click', function () {
            closePromoInputPop();
        });

        $('#selectAllFrBtn').on('click', function () {
            grids.setAllCheckedRows(1, true);
            setCheckedItemsToRightGrid(1);
        });

        $('#cancelAllFrBtn').on('click', function () {
            grids.setAllCheckedRows(1, false);
            setCheckedItemsToRightGrid(1);
        });

        $('#deleteFrBtn').on('click', function () {
            removeCheckedItemsAndSyncLeftGrid(2);
        });

        $('#selectAllItemBtn').on('click', function () {
            grids.setAllCheckedRows(3, true);
            setCheckedItemsToRightGrid(3);
        });

        $('#cancelAllItemBtn').on('click', function () {
            grids.setAllCheckedRows(3, false);
            setCheckedItemsToRightGrid(3);
        });

        $('#applyDiscountRtCheckedBtn').on('click', function () {
            applyDiscountRtOnChecked();
        });

        $('#deleteItemBtn').on('click', function () {
            removeCheckedItemsAndSyncLeftGrid(4);
        });

        $('#savePromoBtn').on('click', function () {
            savePromoData();
        });

        $('#startYear, #startMonth').on('change', function () {
            setDaySelectBoxOption('start');
        });

        $('#endYear, #endMonth').on('change', function () {
            setDaySelectBoxOption('end');
        });

        $('#hiDiscountRt').on('keyup', function () {
            const refinedData = $(this).val().positiveNumberInput();
            $(this).val(refinedData < 100 ? refinedData : 100);
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    currentRequest: {},
    filterCondition: {},
};

/* 조회기간 선택 날짜 선택기 활성화 */
const enableDatepicker = function () {
    const today = new Date().format('yyyy-MM-dd');

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        'filterDt'
    ];

    $('#' + datePickerTargetIds[0]).val(today);

    CommonUI.setDatePicker(datePickerTargetIds);
}

/* 조회기능의 조건을 보낸다. */
const searchList = function () {
    const filterCondition = {
        filterDt: $('#filterDt').val().numString(),
        branchId: parseInt($('#brList').val(), 10),
        franchiseId: parseInt($('#frList').val(), 10),
        hpName: $('#filterHpName').val(),
        hpStatus: $('#filterHpStatus').val(),
    }

    comms.searchList(filterCondition);
}

/* 행사등록/수정 팝업을 연다. */
const openPromoInputPop = function () {
    $('#promoInputPop').addClass('active');
    for (let i = 1; i < 5; i++) {
        grids.resize(i);
    }
}

/* 행사등록/수정 팝업에 데이터를 배치한다. (초기화 할 때도 사용) */
const setDataToPromoInputPop = function (promoData) {
    $('#hpName').val(promoData.hbPromotion.hpName);
    $('#hpType').val(promoData.hbPromotion.hpType);
    $('#hpStatus').val(promoData.hbPromotion.hpStatus);
    $('#hpContent').val(promoData.hbPromotion.hpContent);

    const startTime = parseDate(promoData.hbPromotion.hpStart, 'array');
    const endTime = parseDate(promoData.hbPromotion.hpEnd, 'array');
    $('#startYear').val(startTime[0]);
    $('#startMonth').val(startTime[1]);
    setDaySelectBoxOption('start');
    $('#startDay').val(startTime[2]);
    $('#startHour').val(startTime[3]);
    $('#startMinute').val(startTime[4]);
    $('#endYear').val(endTime[0]);
    $('#endMonth').val(endTime[1]);
    setDaySelectBoxOption('end');
    $('#endDay').val(endTime[2]);
    $('#endHour').val(endTime[3]);
    $('#endMinute').val(endTime[4]);

    const parsedDayOfWeek = getSelectedHpWeekendIntArray(promoData.hbPromotion.hpWeekend);
    const dayOfWeekElements = $('.dayOfTheWeek');
    for (const inputElement of dayOfWeekElements) {
        if (parsedDayOfWeek.includes(parseInt($(inputElement).val()))) {
            $(inputElement).prop('checked', true);
        } else {
            $(inputElement).prop('checked', false);
        }
    }

    let selectedFranchiseId = [];
    for (const {franchiseId} of promoData.hbPromotionFranchise) {
        selectedFranchiseId.push(franchiseId);
    }
    grids.checkRowsByValue(1, 'franchiseId', selectedFranchiseId);
    setCheckedItemsToRightGrid(1);

    let selectedBiItemcode = [];
    for (const {biItemcode, hiDiscountRt} of promoData.hbPromotionItem) {
        selectedBiItemcode.push(biItemcode);
        grids.updateHiDiscountRtByBiItemcode(biItemcode, hiDiscountRt);
    }
    grids.checkRowsByValue(3, 'biItemcode', selectedBiItemcode);
    setCheckedItemsToRightGrid(3);

    openPromoInputPop();
}

const resetPromoInputPop = function () {
    $('#hpName').val('');
    $('#hpType').val('01');
    $('#hpStatus').val('01');
    $('#hpContent').val('');

    $('#startYear').val('');
    $('#startMonth').val('');
    $('#startDay').html(`<option value="">일</option>`);
    $('#startDay').val('');
    $('#startHour').val('00');
    $('#startMinute').val('00');
    $('#endYear').val('');
    $('#endMonth').val('');
    $('#endDay').html(`<option value="">일</option>`);
    $('#endDay').val('');
    $('#endHour').val('00');
    $('#endMinute').val('00');

    $('.dayOfTheWeek').prop('checked', false);

    grids.checkRowsByValue(1, 'franchiseId', []);
    setCheckedItemsToRightGrid(1);
    grids.checkRowsByValue(3, 'biItemcode', []);
    setCheckedItemsToRightGrid(3);

    $('#hiDiscountRt').val('');
    comms.getInitialData();
}

/* 행사등록/수정 팝업을 닫는다. */
const closePromoInputPop = function () {
    wares.currentRequest = {};
    $('#promoInputPop').removeClass('active');
    resetPromoInputPop();
}

/* 선택된 요일을 이진 문자화 1, 0 표현으로 바꿔준다. */
const getHpWeekendBinaryString = function (num) {
    const dayOfTheWeekChkBoxes = $('.dayOfTheWeek');
    let chkBoxSum = 0;
    for (const dayBox of dayOfTheWeekChkBoxes) {
        if (dayBox.checked) {
            chkBoxSum += parseInt(dayBox.value);
        }
    }

    return chkBoxSum.toString(2).padStart(7, '0');
}

/* 이진 문자를 통해 선택된 요일의 숫자가 몇인지를 구한 배열을 반환한다. (ex. 일요일 64, 수요일 8) */
const getSelectedHpWeekendIntArray = function (binaryString) {
    let weekNum = parseInt(binaryString, 2);
    let loopNum = 1;
    const selectedDayOfWeek = [];

    while (weekNum) {
        if (weekNum % 2) {
            selectedDayOfWeek.push(loopNum);
            weekNum--;
        }
        weekNum /= 2;
        loopNum *= 2;
    }

    return selectedDayOfWeek;
}

/* 행사 입력/수정 데이터 구성하고 검증하여 리턴 */
const formPromoData = function () {
    /* 행사 기본 정보 가져오기 */
    const data = {
        hbPromotion: Object.fromEntries(new FormData(document.getElementById('basicInfo'))),
        hbPromotionFranchise: [],
        hbPromotionItem: [],
    };
    data.hbPromotion.hpStart = $('#startYear').val() + $('#startMonth').val() + $('#startDay').val()
        + $('#startHour').val() + $('#startMinute').val();
    data.hbPromotion.hpEnd = $('#endYear').val() + $('#endMonth').val() + $('#endDay').val()
        + $('#endHour').val() + $('#endMinute').val();
    data.hbPromotion.hpWeekend = getHpWeekendBinaryString();
    data.hbPromotion.hpId = wares.currentRequest.hbPromotion? wares.currentRequest.hbPromotion.hpId : 0;

    /* 행사 기본 정보 검증 */
    if (!data.hbPromotion.hpName) {
        alertCaution('행사명칭을 입력해 주세요.', 1);
        return;
    }
    if (data.hbPromotion.hpStart.length !== 12) {
        alertCaution('시작 행사기간을 선택해 주세요.', 1);
        return;
    }
    if (data.hbPromotion.hpEnd.length !== 12) {
        alertCaution('종료 행사기간을 선택해 주세요.', 1);
        return;
    }
    if (parseInt(data.hbPromotion.hpStart, 10) > parseInt(data.hbPromotion.hpEnd, 10)) {
        alertCaution('시작 행사기간이 종료 행사기간 보다 늦습니다.', 1);
        return;
    }
    if (!parseInt(data.hbPromotion.hpWeekend, 2)) {
        alertCaution('최소 한개 이상의<br>행사 적용 요일을 선택해 주세요.', 1);
        return;
    }

    /* 행사 적용 가맹점 가져오기 */
    const selectedFrItems = grids.get(2);
    for (const {franchiseId, frCode} of selectedFrItems) {
        data.hbPromotionFranchise.push({franchiseId, frCode});
    }

    /* 행사 적용 가맹점 검증 */
    if (!data.hbPromotionFranchise.length) {
        alertCaution('최소 하나 이상의<br>적용 가맹점을 선택해 주세요.', 1);
        return;
    }

    /* 행사 적용 상품 가져오기 */
    const selectedItems = grids.get(4);
    for (const {biItemcode, hiDiscountRt} of selectedItems) {
        data.hbPromotionItem.push({biItemcode, hiDiscountRt: hiDiscountRt ? hiDiscountRt : 0});
    }

    /* 행사 적용 상품 검증*/
    if (!data.hbPromotionItem.length) {
        alertCaution('최소 하나 이상의<br>적용 상품을 선택해 주세요.', 1);
        return;
    }

    return data;
}

const savePromoData = function () {
    const saveData = formPromoData();
    /* 일반행사가 아닐 때 할인율이 존재하면 질의 후 진행 */
    if (saveData.hbPromotion.hpType !== '01') {
        let isPositiveHiDiscountRtExist = false;
        for (let i = 0; i < saveData.hbPromotionItem.length; i++) {
            if (saveData.hbPromotionItem[i].hiDiscountRt) {
                isPositiveHiDiscountRtExist = true;
                saveData.hbPromotionItem[i].hiDiscountRt = 0;
            }
        }
        if (isPositiveHiDiscountRtExist) {
            alertCheck('해당 행사 유형은 할인율의 의미가 없습니다.<br>할인율은 모두 0으로 바뀌어 저장되게 됩니다.<br>계속 진행하시겠습니까?');
            $("#checkDelSuccessBtn").on("click", function () {
                comms.savePromoData(saveData);
                $('#popupId').remove();
            });
        } else {
            comms.savePromoData(saveData);
        }
    } else {
        comms.savePromoData(saveData);
    }
}


/* 행사 입력시의 연도를 2022 ~ 지금부터 10년 뒤 까지를 설정 가능하게 셀렉트 박스에 넣어준다. */
const initializeDateSelectBox = function () {
    const fillYearSelectBox = function (selectBoxElementId) {
        const selectBox = $('#' + selectBoxElementId);
        for (let i = thisYear; i <= downYear; i++) {
            const htmlText = `<option value='${i}'>${i}년</option>`;
            selectBox.append(htmlText);
        }
    }
    const today = new Date();
    const thisYear = 2022;
    const downYear = today.getFullYear() + 10;

    fillYearSelectBox('startYear');
    fillYearSelectBox('endYear');
}

/* 선택한 년과 월에 맞춰 일의 범위를 세팅하고 기본값(일) 을 선택하도록 함 */
const setDaySelectBoxOption = function (targetTime) {
    const targetYear = parseInt($('#' + targetTime + 'Year').val());
    const targetMonth = parseInt($('#' + targetTime + 'Month').val());
    if (isNaN(targetYear) || isNaN(targetMonth)) {
        return;
    }
    const lastDayOfMonth = new Date(targetYear, targetMonth, 0).getDate();

    const $targetElement = $('#' + targetTime + 'Day');
    $targetElement.html(`<option value="">일</option>`);
    for (let d = 1; d <= lastDayOfMonth; d++) {
        const dayString = d.toString().padStart(2, '0');
        $targetElement.append(`<option value="${dayString}">${dayString}일</option>`);
    }
}

/* 행사 적용 가맹점, 행사 적용 상품을 체크/해제 될 때 우측의 선택된 항목 그리드에 반영 */
const setCheckedItemsToRightGrid = function (gridNum) {
    const selectedItemsGridNum = gridNum + 1;
    const checkedItems = grids.getCheckedItems(gridNum);
    const targetDepthData = [];
    for (const item of checkedItems) {
        if (!item._$isBranch) {
            if (gridNum === 3 && !item.hiDiscountRt) {
                item.hiDiscountRt = 0;
            }
            targetDepthData.push(item);
        }
    }
    grids.set(selectedItemsGridNum, targetDepthData);
    grids.setLastRowPosition(selectedItemsGridNum);
    countSelectedItems(selectedItemsGridNum);
}

const countSelectedItems = function (gridNum) {
    const rowCount = grids.getCount(gridNum);

    if (gridNum === 2) {
        $('#frRowCount').html(rowCount);
    } else if (gridNum === 4) {
        $('#itemRowCount').html(rowCount);
    }

}

/* 선택된 요소들을 보여주는 그리드의 체크되어 있는 항목 삭제 기능을 반영한다. */
const removeCheckedItemsAndSyncLeftGrid = function (gridNum) {
    const relatedMasterGridNum = gridNum -1;
    const checkedItems = grids.getCheckedItems(gridNum);
    let keyName;
    let valueArray = [];
    let rowIdArray = [];
    if (gridNum === 2) {
        keyName = 'franchiseId';
        for (const {franchiseId, _$uid} of checkedItems) {
            valueArray.push(franchiseId);
            rowIdArray.push(_$uid);
        }
    } else if (gridNum === 4) {
        keyName = 'biItemcode';
        for (const {biItemcode, _$uid} of checkedItems) {
            valueArray.push(biItemcode);
            rowIdArray.push(_$uid);
        }
    } else {
        return;
    }

    grids.uncheckRowsByValue(relatedMasterGridNum, keyName, valueArray);
    releaseParentsWhenNoChildrenLeft(relatedMasterGridNum);
    grids.removeRowByRowId(gridNum, rowIdArray);
    countSelectedItems(gridNum);
}

/* 삭제 기능을 통해 체크 해제된 적용 리스트의 부모 요소가 정상적으로 해제되지 않는 AUIGrid의 버그를 보완하기 위한 기능 (임시) */
const releaseParentsWhenNoChildrenLeft = function (gridNum) {
    const items = grids.get(gridNum);
    let removeTargetUids = [];

    checkChildrenLoop(items);

    if (removeTargetUids.length) {
        grids.checkRowsById(gridNum, removeTargetUids);
        grids.uncheckRowsById(gridNum, removeTargetUids);
    }

    function checkChildrenLoop(items) {
        let isAllUnchecked = true;
        for (const item of items) {
            if (item._$isBranch) {
                if (checkChildrenLoop(item.children)) {
                    removeTargetUids.push(item._$uid);
                } else {
                    isAllUnchecked = false;
                }
            } else {
                if (grids.isCheckedByRowId(gridNum, item._$uid)) {
                    isAllUnchecked = false;
                    return false;
                }
            }
        }

        if (isAllUnchecked && items.length) {
            for (const {_$uid, _$isBranch} of items) {
                if (!_$isBranch) {
                    removeTargetUids.push(_$uid);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}

/* 체크된 항목들에 할인율 일괄 적용 */
const applyDiscountRtOnChecked = function () {
    const checkedItems = grids.getCheckedItems(4);
    const hiDiscountRt = parseInt($('#hiDiscountRt').val());
    for (let i = 0; i < checkedItems.length; i++) {
        checkedItems[i].hiDiscountRt = hiDiscountRt;
        grids.updateHiDiscountRtByBiItemcode(checkedItems[i].biItemcode, hiDiscountRt);
    }
    grids.updateRowsById(4, checkedItems);
}

/* 날짜의 의미가 있는 12자리 문자열의 데이터를, 그리드나, 팝업에 필요한 형식으로 변환한다. */
const parseDate = function (char12date, returnType) {
    const dateArray = [
        char12date.substring(0, 4),
        char12date.substring(4, 6),
        char12date.substring(6, 8),
        char12date.substring(8, 10),
        char12date.substring(10, 12),
    ];
    switch (returnType) {
        case 'string':
            return dateArray[0] + '-' + dateArray[1] + '-' + dateArray[2] + ' ' + dateArray[3] + ':' + dateArray[4];
        case 'array':
            return dateArray;
    }
}

/* 행사의 수정버튼을 눌렀을 때 일어나야 할 동작들 */
const onModify = function () {
    if (wares.currentRequest.hbPromotion.hpId) {
        const targetCondition = {
            hpId: wares.currentRequest.hbPromotion.hpId,
        }
        comms.getDetailPromoData(targetCondition, function () {
            setDataToPromoInputPop(wares.currentRequest);
        });
    }
}

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function () {
    grids.initialization();
    grids.create();
    enableDatepicker();
    activateBrFrListInputs();
    initializeDateSelectBox();
    comms.getInitialData();
    trigs.basic();
};
