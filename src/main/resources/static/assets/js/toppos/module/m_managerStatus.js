/*
 * 지사의 외주 관리 메뉴에서 공통 적으로 쓰이는 함수들의 정리 (외주관리가격설정 제외)
 * */

const dtos = {
    send: {
    },
    receive: {
        /* 가맹점 선택 셀렉트박스에 띄울 가맹점의 리스트 */
        managerBelongList: {
            frId: 'nr',
            frName: 's',
            frTagNo: 's',
        },
    },
};

const wares = {

};

const grids = {
    id: ['grid_detail'],

    create(id, layout, property) {
        AUIGrid.create(id, layout, property);
    },

    setData(id, data) {
        AUIGrid.setGridData(id, data);
    },

    clear(id) {
        AUIGrid.clearGridData(id);
    },
};

const runOnlyOnce = {
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

    /* 셀렉트 박스에 현 지점에 속한 가맹점들을 표시 */
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

    /* grid_detail 그리드의 기본 생성을 담당한다. */
    makeDetailGrid() {
        const processChkChar = function (_rowIndex, _columnIndex, value, _headerText, _item) {
            return value ? '√' : '';
        };
        const dateFormat = 'yyyy-mm-dd';
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
                dataField: 'fdO1Dt',
                headerText: '외주<br>출고일',
                width: 90,
                dataType: 'date',
                formatString: dateFormat,
            }, {
                dataField: 'fdO2Dt',
                headerText: '외주<br>입고일',
                width: 90,
                dataType: 'date',
                formatString: dateFormat,
            }, {
                dataField: 'fdEstimateDt',
                headerText: '고객출고<br>예정일',
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
                dataField: 'fdPromotionType',
                headerText: '적용행사',
                width: 80,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return value ? CommonData.name.fdPromotionType[value] : '';
                },
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
                dataField: 'fdOutsourcingAmt',
                headerText: '외주금액',
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
                        headerText: '고객출고',
                        width: 90,
                        dataType: 'date',
                        formatString: dateFormat,
                    },
                ],
            }, {
                dataField: 'fdS6Time',
                headerText: '고객출고<br>일시',
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
        grids.create(grids.id[0], layout, property);
    },
};

export {grids, runOnlyOnce};


