/*
 * 본사 현황 페이지들 관련하여 공통적인 함수를 관리한다.
 *
 */


const dtos = {

};

const comms = {
    /* 그리드 좌측, 일별 누계 데이터 조회값을 가져옴 */
    searchSummaryData(searchCondition) {
        console.log(searchCondition);
        CommonUI.ajax('/api/head/headReceiptList', 'GET', searchCondition, function (res) {
            const data = res.sendData.gridListData;
            grids.setData('grid_summary', data);
        });
    },

    getDetailData(condition) {
        CommonUI.ajax('/api/head/headReceiptSubList', 'GET', condition, function (res) {
            const data = res.sendData.gridListData;
            console.log(data);
            grids.setData('grid_detail', data);
        });
    },
};

/* 그리드 관련한 공통함수 */
const grids = {
    create(id, layout, property) {
        AUIGrid.create(id, layout, property);
    },

    setData(id, data) {
        AUIGrid.setGridData(id, data);
    },
};

/* 페이지에 진입하여 단 한번 실행되는 함수 */
const runOnlyOnce = {
    /* grid_summary 그리드의 기본 생성을 담당한다. */
    makeSummaryGrid(prop) {
        const layout = [
            {
                dataField: 'brName',
                headerText: '지사명',
                visible: true,
            }, {
                dataField: 'frName',
                headerText: '가맹점명',
                visible: true,
            }, {
                dataField: prop.targetDate.dataField,
                headerText: prop.targetDate.headerText,
                width: 100,
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: prop.targetQty.dataField,
                headerText: prop.targetQty.headerText,
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdTotAmt',
                headerText: '접수총액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            },
        ];
        const property = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : false,
            showRowAllCheckBox: false,
            showRowCheckColumn: false,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            rowHeight : 48,
            headerHeight : 48,
        };
        grids.create('grid_summary', layout, property);
    },

    /* grid_detail 그리드의 기본 생성을 담당한다. */
    makeDetailGrid(prop
                       = {fdUrgentTypeVisible: true , fiProgressStateDtVisible: false}) {
        const processChkChar = function (_rowIndex, _columnIndex, value, _headerText, _item) {
            return value ? '√' : '';
        };
        const dateFormat = 'yyyy-mm-dd';
        const dateTimeFormat = 'yyyy-mm-dd<br>hh:mm';
        const layout = [
            {
                headerText: '지사/가맹점 정보',
                children: [
                    {
                        dataField: 'brName',
                        headerText: '지사명',
                        width: 100,
                    }, {
                        dataField: 'frName',
                        headerText: '가맹점명',
                        width: 100,
                    },
                ],
            }, {
                headerText: '고객 정보',
                children: [
                    {
                        dataField: 'bcName',
                        headerText: '고객명',
                        style: 'grid_textalign_left',
                    }, {
                        dataField: 'bcHp',
                        headerText: '연락처',
                        width: 120,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return CommonUI.formatTel(value);
                        },
                    }, {
                        dataField: 'bcGrade',
                        headerText: '할인등급',
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return value === '01' ? '' : CommonData.name.bcGradeName[value];
                        },
                    },
                ],
            }, {
                dataField: 'fdTag',
                headerText: '택번호',
                style: 'datafield_tag',
                width: 90,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.formatBrTagNo(value);
                },
            }, {
                dataField: 'frInsertDt',
                headerText: '최초<br>접수일시',
                width: 90,
            }, {
                dataField: 'fdEstimateDt',
                headerText: '인도<br>예정일',
                width: 100,
                dataType: 'date',
                formatString: dateFormat,
            }, {
                headerText: '품목 정보',
                children: [
                    {
                        dataField: '',
                        headerText: '품목',
                        labelFunction: function (_rowIndex, _columnIndex, _value, _headerText, item) {
                            return item.bsName === '일반' ? item.bgName : item.bsName + item.bgName;
                        }
                    }, {
                        dataField: 'biName',
                        headerText: '소재',
                    }, {
                        dataField: 'fdQty',
                        headerText: '수량',
                        style: 'grid_textalign_right',
                        width: 80,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    }, {
                        dataField: 'fdColor',
                        headerText: '색상',
                    }, {
                        dataField: 'fdPattern',
                        headerText: '무늬',
                    },
                ],
            }, {
                dataField: 'fdUrgentType',
                headerText: '긴급',
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return value ? CommonData.name.fdUrgentType[value] : '';
                },
                visible: prop.fdUrgentTypeVisible,
            }, {
                headerText: '처리내역',
                children: [
                    {
                        dataField: 'fdPressed',
                        headerText: '다림질',
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdAdd1Amt',
                        headerText: '추가요금',
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdRepairAmt',
                        headerText: '수선',
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdWhitening',
                        headerText: '표백',
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdPollution',
                        headerText: '오염',
                        labelFunction: processChkChar,
                    }, {
                        dataField: '',
                        headerText: '발수가공',
                        labelFunction: processChkChar,
                    },
                ],
            }, {
                dataField: '',
                headerText: '추가금액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdDiscountAmt',
                headerText: '할인금액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdTotAmt',
                headerText: '최종가격',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdState',
                headerText: '현재상태',
            }, {
                headerText: '단계별 일자',
                children: [
                    {
                        dataField: 'fdS2Dt',
                        headerText: '지사입고',
                        width: 100,
                        dataType: 'date',
                        formatString: dateFormat,
                    }, {
                        dataField: 'fdS4Dt',
                        headerText: '지사출고',
                        width: 100,
                        dataType: 'date',
                        formatString: dateFormat,
                    }, {
                        dataField: 'fdS5Dt',
                        headerText: '완성',
                        width: 100,
                        dataType: 'date',
                        formatString: dateFormat,
                    }, {
                        dataField: 'fdS6Dt',
                        headerText: '고객인도',
                        width: 100,
                        dataType: 'date',
                        formatString: dateFormat,
                    },
                ],
            }, {
                dataField: 'fiProgressStateDt',
                headerText: '반품요청<br>일시',
                visible: prop.fiProgressStateDtVisible,
                width: 90,
                dataType: 'date',
                renderer : {
                    type: 'TemplateRenderer',
                },
                formatString: dateTimeFormat,
            }, {
                dataField: 'fdS6Time',
                headerText: '실인도<br>일시',
                width: 90,
                dataType: 'date',
                renderer : {
                    type: 'TemplateRenderer',
                },
                formatString: dateTimeFormat,
            }, {
                dataField: 'fdNormalAmt',
                headerText: '기본가격',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            },
        ];
        const property = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '출력할 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : false,
            showRowAllCheckBox: false,
            showRowCheckColumn: false,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : false,
            rowHeight : 30,
            headerHeight : 30,
        };
        grids.create('grid_detail', layout, property);
    },

    /* 데이트 피커의 가동과 날짜 선택의 제한 */
    enableDatepicker() {
        const today = new Date().format('yyyy-MM-dd');

        /* datepicker를 적용시킬 대상들의 dom id들 */
        const datePickerTargetIds = [
            'filterFromDt', 'filterToDt',
        ];

        $('#' + datePickerTargetIds[0]).val(today);
        $('#' + datePickerTargetIds[1]).val(today);

        const dateAToBTargetIds = [
            ['filterFromDt', 'filterToDt'],
        ];

        CommonUI.setDatePicker(datePickerTargetIds);
        CommonUI.restrictDateAToB(dateAToBTargetIds);
    },

    /* 지사와 가맹점의 이름과 id들을 콜백으로 반환한다. */
    getBrFrList(callback) {
        CommonUI.ajax('/api/head/headBrFrInfoList', 'GET', false, function (res) {
            callback(res.sendData);
        });
    },

    /* 현황페이지에 공통적으로 적용되는 이벤트들을 적용 */
    setCommonEvents() {
        $('#brList').on('change', function () {
            setBrFrList(window.wares.brFrList, parseInt($('#brList').val(), 10));
        });

        $('#searchListBtn').on('click', function () {
            searchSummaryData();
        });
    },

    setGridEvents(prop) {
        AUIGrid.bind('grid_summary', 'cellClick', function (e) {
            const item = e.item;
            getDetailData(item, prop.targetDateName);
        });
    },
};

/* 처음 시작시 지사 리스트를 셀렉트박스에 세팅하고, 지사 선택시 가맹점 리스트를 세팅 */
const setBrFrList = function (brFrList, selectedBranchId, isInitialization = false) {
    if (isInitialization) {
        for (const {branchId, brName} of brFrList.branchList) {
            $('#brList').append(`
                <option value='${branchId}'>${brName}</option>
            `);
        }
    } else {
        const $frList = $('#frList');
        $frList.html(`<option value='0'>전체선택</option>`);
        if (selectedBranchId) {
            for (const {franchiseId, branchId, frName} of brFrList.franchiseList) {
                if (branchId === selectedBranchId) {
                    $frList.append(`
                        <option value='${franchiseId}'>${frName}</option>
                    `);
                }
            }
            $frList.prop('disabled', false);
        } else {
            $frList.val('0').prop('disabled', true);
        }
    }
};

/* 조회 조건에 따라 조회가 되도록 한다 */
const searchSummaryData = function () {
    const filterFromDate = new Date($('#filterFromDt').val());
    const filterToDate = new Date($('#filterToDt').val());
    const millisecondsOfADay = 86400000;
    const durationDays = (filterToDate - filterFromDate) / millisecondsOfADay;
    const limitDurationDays = 180;
    if(durationDays > limitDurationDays) {
        alertCaution(`조회 기간은 ${limitDurationDays}일을 초과할 수 없습니다.`, 1);
        return;
    }

    const searchCondition = {
        branchId: parseInt($('#brList').val(), 10),
        franchiseId: parseInt($('#frList').val(), 10),
        filterFromDt: $('#filterFromDt').val().numString(),
        filterToDt: $('#filterToDt').val().numString(),
    };

    comms.searchSummaryData(searchCondition);
};

/* 클릭으로 선택되어 그리드에서 넘겨받은 item을 기준으로 우측 그리 */
const getDetailData = function (item, tragetDateName) {
    console.log(item);
    const condition = {
        branchId: item.branchId,
        franchiseId: item.franchiseId,
    };
    condition[tragetDateName] = item[tragetDateName].numString();

    comms.getDetailData(condition);
};

export {grids, runOnlyOnce, setBrFrList};
