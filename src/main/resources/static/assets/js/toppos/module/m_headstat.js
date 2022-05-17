/*
 * 본사 현황 페이지들 관련하여 공통적인 함수를 관리한다.
 *
 */


/* 그리드 관련한 공통함수 */
const grids = {
    create(id, layout, property) {
        AUIGrid.create(id, layout, property);
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
            }, {
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: prop.targetDate.dataField,
                headerText: prop.targetDate.headerText,
            }, {
                dataField: prop.targetQty.dataField,
                headerText: prop.targetQty.headerText,
            }, {
                dataField: '',
                headerText: '접수총액',
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
        const layout = [
            {
                dataField: 'brName',
                headerText: '지사명',
            }, {
                dataField: 'frName',
                headerText: '가맹점명',
            }, {
                dataField: 'bcName',
                headerText: '고객명',
            }, {
                dataField: 'bcHp',
                headerText: '연락처',
            }, {
                dataField: 'bcGrade',
                headerText: '할인등급',
            }, {
                dataField: 'fdTag',
                headerText: '택번호',
            }, {
                dataField: 'frInsertDt',
                headerText: '최초<br>접수일시',
            }, {
                dataField: 'fdEstimateDt',
                headerText: '인도<br>예정일',
            }, {
                dataField: '',
                headerText: '품목',
            }, {
                dataField: 'biName',
                headerText: '소재',
            }, {
                dataField: 'fdQty',
                headerText: '수량',
            }, {
                dataField: 'fdColor',
                headerText: '색상',
            }, {
                dataField: 'fdPattern',
                headerText: '무늬',
            }, {
                dataField: 'fdUrgentType',
                headerText: '긴급',
                visible: prop.fdUrgentTypeVisible,
            }, {
                dataField: 'fdPressed',
                headerText: '다림질',
            }, {
                dataField: 'fdAdd1Amt',
                headerText: '추가요금',
            }, {
                dataField: 'fdRepairAmt',
                headerText: '수선',
            }, {
                dataField: 'fdWhitening',
                headerText: '표백',
            }, {
                dataField: 'fdPollution',
                headerText: '오염',
            }, {
                dataField: '',
                headerText: '발수가공',
            }, {
                dataField: '',
                headerText: '추가금액',
            }, {
                dataField: 'fdDiscountAmt',
                headerText: '할인금액',
            }, {
                dataField: 'fdTotAmt',
                headerText: '최종가격',
            }, {
                dataField: 'fdState',
                headerText: '현재상태',
            }, {
                dataField: 'fdS2Dt',
                headerText: '지사입고',
            }, {
                dataField: 'fdS4Dt',
                headerText: '지사출고',
            }, {
                dataField: 'fdS5Dt',
                headerText: '완성',
            }, {
                dataField: 'fdS6Dt',
                headerText: '고객인도',
            }, {
                dataField: 'fiProgressStateDt',
                headerText: '반품요청<br>일시',
                visible: prop.fiProgressStateDtVisible,
            }, {
                dataField: 'fdS6Time',
                headerText: '실인도<br>일시',
            }, {
                dataField: 'fdNormalAmt',
                headerText: '기본가격',
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

        });
    },
};

/* 처음 시작시 지사 리스트를 셀렉트박스에 세팅하고, 지사 선택시 가맹점 리스트를 세팅 */
const setBrFrList = function (brFrList, selectedBranchId, isInitialization = false) {
    console.log(brFrList);
    if (isInitialization) {
        for (const {branchId, brName} of brFrList.branchList) {
            $('#brList').append(`
                <option value='${branchId}'>${brName}</option>
            `);
        }
    } else {
        const $frList = $('#frList');
        $frList.html('<option value="0">전체선택</option>');
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



export {grids, runOnlyOnce, setBrFrList};
