/*
 * 본사 현황 페이지들 관련하여 공통적인 함수를 관리한다.
 *
 */

import {activateBrFrListInputs} from './m_setBrFrList.js';

const dtos = {
    send: {
        summaryAPI: {
            branchId: 'n',
            franchiseId: 'n',
            filterFromDt: 's',
            filterToDt: 's',
        },
        detailAPI: {
            branchId: 'n',
            franchiseId: 'n',
            frYyyymmdd: 's',
        },
    },
    receive: {
        summaryAPI: {
            branchId: 'n',
            brName: 's',
            franchiseId: 'n',
            frName: 's',
            requestCount: 'n',
            fdTotAmt: 'n',
        },
        detailAPI: {
            brName: 's',
            frName: 's',
            bcName: 's',
            bcHp: 's',
            bcGrade: 's',
            fdTag: 's',
            fr_insert_date: 's',
            fdEstimateDt: 's',
            bgName: 's',
            bsName: 's',
            biName: 's',
            fdQty: 'n',
            fdColor: 's',
            fdPattern: 's',
            fdUrgentType: 's',
            fdUrgentYn: 's',
            fdPriceGrade: 's',
            fdRetryYn: 's',
            fdPressed: 'n',
            fdAdd1Amt: 'n',
            fdRepairAmt: 'n',
            fdWhitening: 'n',
            fdPollution: 'n',
            fdWaterRepellent: 'n',
            fdStarch: 'n',
            fdAdd2Amt: 'n',
            fdUrgentAmt: 'n',
            fdNormalAmt: 'n',
            fdTotAmt: 'n',
            fdDiscountAmt: 'n',
            fdState: 's',
            fdPollutionType: 'n',
            fdPollutionBack: 'n',
            fdS2Dt: 's',
            fdS5Dt: 's',
            fdS4Dt: 's',
            fdS3Dt: 's',
            fdS6Dt: 's',
            fdS6Time: 's',
            fiProgressStatet: 's',
        },
    },
};

const wares = {
    searchCondition: {},
    xlsxNaming: {
        title: '',
        brName: '',
        frName: '',
        date: '',
    },

};

const urls = {
    searchSummaryData: '',
    getDetailData: '',
};

const comms = {
    /* 그리드 좌측, 일별 누계 데이터 조회값을 가져옴 */
    searchSummaryData(searchCondition) {
        dv.chk(searchCondition, dtos.send.summaryAPI, '누계 데이터 조회를 위한 조건 보내기');
        wares.searchCondition = searchCondition;
        CommonUI.ajax(urls.searchSummaryData, 'GET', searchCondition, function (res) {
            if (searchCondition.branchId) {
                grids.setFrNameVisibility(true);
            } else {
                grids.setFrNameVisibility(false);
            }
            const data = res.sendData.gridListData;
            console.log('누계조회결과', data);
            dv.chk(data, dtos.receive.summaryAPI, '일별 누계 데이터 받아오기');
            grids.clear(grids.id[1]);
            grids.setData(grids.id[0], data);
        });
    },

    /* 누게리스트의 지사,가맹점정보,날짜를 보내 해당 일자에 속한 모든 접수데이터 가져오기 */
    getDetailData(condition) {
        dv.chk(condition, dtos.send.detailAPI, '상세 품목을 받아오기 위한 조건 보내기');
        CommonUI.ajax(urls.getDetailData, 'GET', condition, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.detailAPI, '받아온 상세 품목들의 정보');
            grids.setData(grids.id[1], data);
        });
    },
};

/* 그리드 관련한 공통함수 */
const grids = {
    id: [
        'grid_summary',
        'grid_detail',
    ],

    create(id, layout, property) {
        AUIGrid.create(id, layout, property);
    },

    setData(id, data) {
        AUIGrid.setGridData(id, data);
    },

    clear(id) {
        AUIGrid.clearGridData(id);
    },

    setFrNameVisibility(boolean) {
        if (boolean) {
            AUIGrid.showColumnByDataField(grids.id[0], 'frName');
        } else {
            AUIGrid.hideColumnByDataField(grids.id[0], 'frName');
        }
    },

    // 엑셀 내보내기(Export)
    exportToXlsx() {
        //FileSaver.js 로 로컬 다운로드가능 여부 확인
        if(!AUIGrid.isAvailableLocalDownload(grids.id[1])) {
            alertCaution('파일 다운로드가 불가능한 브라우저 입니다.', 1);
            return;
        }
        AUIGrid.exportToXlsx(grids.id[1], {
            fileName : wares.xlsxNaming.title + '_' + wares.xlsxNaming.brName
                + (wares.xlsxNaming.frName ? '_' : '') + wares.xlsxNaming.frName
                + '_' + wares.xlsxNaming.date,
            progressBar : true,
        });
    },
};

/* 페이지에 진입하여 단 한번 실행되는 함수 */
const runOnlyOnce = {
    /* grid_summary 그리드의 기본 생성을 담당한다. */
    makeSummaryGrid(prop) {
        urls.searchSummaryData = prop.url;
        /* 동적으로 dtos에 검사대상 부여 */
        dtos.receive.summaryAPI[prop.targetDate.dataField] = "s";

        const layout = [
            {
                dataField: 'brName',
                headerText: '지사명',
            }, {
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: prop.targetDate.dataField,
                headerText: prop.targetDate.headerText,
                width: 100,
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: 'requestCount',
                headerText: '건수',
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
        grids.create(grids.id[0], layout, property);
    },

    /* grid_detail 그리드의 기본 생성을 담당한다. */
    makeDetailGrid(prop) {
        urls.getDetailData = prop.url;
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
                        width: 120,
                    }, {
                        dataField: 'frName',
                        headerText: '가맹점명',
                        width: 120,
                    },
                ],
            }, {
                headerText: '고객 정보',
                children: [
                    {
                        dataField: 'bcName',
                        headerText: '고객명',
                        width: 100,
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
                        width: 70,
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
                dataField: 'fr_insert_date',
                headerText: '최초<br>접수일시',
                width: 140,
            }, {
                dataField: 'fdEstimateDt',
                headerText: '인도<br>예정일',
                width: 90,
                dataType: 'date',
                formatString: dateFormat,
            }, {
                headerText: '품목 정보',
                children: [
                    {
                        dataField: '',
                        headerText: '품목',
                        width: 100,
                        labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                            return item.bsName === '일반' ? item.bgName : item.bsName + item.bgName;
                        },
                    }, {
                        dataField: 'biName',
                        headerText: '소재',
                        width: 70,
                    }, {
                        dataField: 'fdQty',
                        headerText: '수량',
                        style: 'grid_textalign_right',
                        width: 50,
                        dataType: 'numeric',
                        autoThousandSeparator: 'true',
                    }, {
                        dataField: 'fdColor',
                        headerText: '색상',
                        width: 50,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return CommonData.name.fdColor[value];
                        },
                    }, {
                        dataField: 'fdPattern',
                        headerText: '무늬',
                        width: 50,
                        labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                            return CommonData.name.fdPattern[value];
                        },
                    },
                ],
            }, {
                dataField: 'fdUrgentType',
                headerText: '긴급',
                width: 50,
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
                        width: 70,
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdAdd1Amt',
                        headerText: '추가요금',
                        width: 70,
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdRepairAmt',
                        headerText: '수선',
                        width: 70,
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdWhitening',
                        headerText: '표백',
                        width: 70,
                        labelFunction: processChkChar,
                    }, {
                        dataField: 'fdPollution',
                        headerText: '오염',
                        width: 70,
                        labelFunction: processChkChar,
                    }, {
                        dataField: '',
                        headerText: '발수가공',
                        width: 70,
                        labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                            let result = '';
                            if (item.fdWaterRepellent) {
                                result += '발수 ';
                            }
                            if (item.fdStarch) {
                                result += '풀먹임';
                            }
                            return result;
                        },
                    },
                ],
            }, {
                dataField: 'fdNormalAmt',
                headerText: '기본가격',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: '',
                headerText: '추가금액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    let result = 0;
                    if (item.fdRetryYn === 'N') {
                        result = item.fdPressed + item.fdWhitening + item.fdWaterRepellent + item.fdStarch
                            + item.fdPollution + item.fdAdd1Amt + item.fdRepairAmt + item.fdUrgentAmt;
                    }
                    return result;
                },
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
                width: 90,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return value ? CommonData.name.fdState[value] : '';
                },
            }, {
                headerText: '단계별 일자',
                children: [
                    {
                        dataField: 'fdS2Dt',
                        headerText: '지사입고',
                        width: 90,
                        dataType: 'date',
                        formatString: dateFormat,
                    }, {
                        dataField: 'fdS4Dt',
                        headerText: '지사출고',
                        width: 90,
                        dataType: 'date',
                        formatString: dateFormat,
                    }, {
                        dataField: 'fdS5Dt',
                        headerText: '완성',
                        width: 90,
                        dataType: 'date',
                        formatString: dateFormat,
                    }, {
                        dataField: 'fdS6Dt',
                        headerText: '고객인도',
                        width: 90,
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
                width: 140,
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
        grids.create(grids.id[1], layout, property);
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

    /* 현황페이지에 공통적으로 적용되는 이벤트들을 적용 */
    setCommonEvents() {
        $('#searchListBtn').on('click', function () {
            searchSummaryData();
        });

        $('#exportXlsx').on('click', function () {
            if (wares.xlsxNaming.date) {
                grids.exportToXlsx();
            } else {
                alertCaution('먼저 상세항목 정보를 조회해 주세요.', 1);
            }
        });
    },

    setGridEvents(prop) {
        AUIGrid.bind(grids.id[0], 'cellClick', function (e) {
            const item = e.item;
            getDetailData(item, prop.targetDateName);
        });
    },

    setXlsxTitleName(titleName) {
        wares.xlsxNaming.title = titleName;
    },

    activateBrFrListInputs: activateBrFrListInputs,
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

/* 클릭으로 선택되어 그리드에서 넘겨받은 item을 기준으로 우측 그리드를 띄운다. */
const getDetailData = function (item, targetDateName) {
    wares.xlsxNaming.frName = item.frName;
    wares.xlsxNaming.brName = item.brName;
    wares.xlsxNaming.date = item[targetDateName];

    const condition = {
        branchId: item.branchId,
        franchiseId: $("#frList").val() !== "0" ? item.franchiseId : 0,
    };
    condition[targetDateName] = item[targetDateName].numString();

    comms.getDetailData(condition);
};

export {grids, runOnlyOnce};
