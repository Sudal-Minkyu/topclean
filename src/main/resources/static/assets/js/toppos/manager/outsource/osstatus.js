import {grids, runOnlyOnce} from '../../module/m_outsourcing.js';

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {

}

const gridElemets = {
    id: ['grid_main'],
    columnLayout: [],
    prop: [],

    initialization() {

        const processChkChar = function (_rowIndex, _columnIndex, value, _headerText, _item) {
            return value ? '√' : '';
        };
        const dateFormat = 'yyyy-mm-dd';
        const dateTimeFormat = 'yyyy-mm-dd hh:mm';

        this.columnLayout[0] = [
            {
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: '',
                headerText: '출고일',
                width: 100,
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: '',
                headerText: '출고<br>건수',
            }, {
                dataField: '',
                headerText: '입고<br>건수',
            }, {
                dataField: 'fdTotAmt',
                headerText: '접수총액',
                style: 'grid_textalign_right',
                width: 80,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: '',
                headerText: '외주총액',
            },
        ];

        this.prop[0] = {
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

        this.columnLayout[1] = [
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
                dataField: 'fdS6Time',
                headerText: '실인도<br>일시',
                width: 140,
            },
        ];

        this.prop[1] = {
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
    },
}

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    gridElemets.initialization();
    grids.create('grid_summary', gridElemets.columnLayout[0], gridElemets.prop[0]);
    grids.create('grid_detail', gridElemets.columnLayout[1], gridElemets.prop[1]);

    runOnlyOnce.enableDatepicker();
    runOnlyOnce.getFrList();
};