/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        headTagNoReceiptSearch: {
            branchId: 'n',
            franchiseId: 'n',
            tagNo: 's',
        },
    },
    receive: {
        headTagNoSearch: {
            branchList: {
                branchId: 'n',
                brName: 's',
            },
            franchiseList: {
                franchiseId: 'n',
                branchId: 'n',
                frName: 's',
                frTagNo: 's',
            },
        },

        headTagNoReceiptSearch: {
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
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getBrFrList() {
        CommonUI.ajax('/api/head/headTagNoSearch', 'GET', false, function (res) {
            wares.brFrListData = res.sendData;
            dv.chk(wares.brFrListData, dtos.receive.headTagNoSearch, '지사와 가맹점의 이름, id, 소속지사(가맹점의 경우) 받아오기');
            setBrFrList(wares.brFrListData, 0, true);
        });
    },

    searchTagData(searchCondition) {
        dv.chk(searchCondition, dtos.send.headTagNoReceiptSearch, '택번호 검색 조건 보내기');
        CommonUI.ajax('/api/head/headTagNoReceiptSearch', 'GET', searchCondition, function (res) {
            const data = res.sendData.gridListData;
            dv.chk(data, dtos.receive.headTagNoReceiptSearch, '택번호 검색 결과 리스트 받기');
            grids.f.set(0, data);
            $("#aftTag").val("");
            $("#foreTag").val("");
            $("#fullTag").val("");
        });
    }
};

/*
*  .s : AUI 그리드 관련 설정들, 같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  .f : 그리드 관련 함수들 배치
* */
const grids = {
    /* 그리드 세팅 */
    s: {
        id: [
            'grid_main',
        ],
        columnLayout: [],
        prop: [],
    },

    /* 그리드 펑션 */
    f: {
        /* grid_detail 그리드의 기본 생성을 담당한다. */
        makeDetailGrid() {
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
            grids.f.create('grid_main', layout, property);
        },

        /* 그리드 동작 처음 빈 그리드를 생성 */
        create(id, layout, property) {
            AUIGrid.create(id, layout, property);
        },

        /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
        get(numOfGrid) {
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
        set(numOfGrid, data) {
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        /* 해당 배열 번호 그리드의 데이터 비우기 */
        clear(numOfGrid) {
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        /* 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절 */
        resize(num) {
            AUIGrid.resize(grids.s.id[num]);
        },
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#brList').on('change', function () {
            setBrFrList(wares.brFrListData, parseInt($('#brList').val(), 10));
            $("#aftTag").val("");
            $("#foreTag").val("");
            $("#fullTag").val("");
            $('.doubleTag').hide();
            $('.singleTag').show();
        });

        $('#frList').on('change', function () {
            if($(this).val() === "0") {
                $("#aftTag").val("");
                $("#foreTag").val("");
                $(".doubleTag").css("display", "none");
                $(".singleTag").css("display", "flex");
            } else {
                $("#foreTag").val($("#frList option:selected").attr("data-tagno"));
                $("#fullTag").val("");
                $(".doubleTag").css("display", "flex");
                $(".singleTag").css("display", "none");
                const aftLength = 7 - $("#foreTag").val().length;
                $("#aftTag").attr("maxlength", aftLength);
                $("#aftTag").val("");
            }
        });

        $("#searchListBtn").on("click", function () {
            searchTagData();
        });

        $('#fullTag, #aftTag').on("keypress", function (e) {
            if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
                searchTagData();
            }
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    brListData: {},
};

/* 조회 조건에 따라 조회가 되도록 한다 */
const searchTagData = function () {

    const frId = parseInt($("#frList").val(), 10);

    const searchCondition = {
        branchId: parseInt($('#brList').val(), 10),
        franchiseId: frId,
    };

    if(parseInt(frId, 10)) {
        searchCondition.tagNo = $("#foreTag").val() + $("#aftTag").val().numString();
    } else {
        searchCondition.tagNo = $("#fullTag").val().numString();
    }


    if(searchCondition.tagNo.length !==7) {
        alertCaution("택번호를 완전히 입력해 주세요.", 1);
        return;
    }

    comms.searchTagData(searchCondition);
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
            for (const {franchiseId, branchId, frName, frTagNo} of brFrList.franchiseList) {
                if (branchId === selectedBranchId) {
                    $frList.append(`
                        <option value='${franchiseId}' data-tagno='${frTagNo}'>${frName}</option>
                    `);
                }
            }
            $frList.prop('disabled', false);
        } else {
            $frList.val('0').prop('disabled', true);
        }
    }
};


/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    comms.getBrFrList();
    trigs.basic();

    grids.f.makeDetailGrid();
};
