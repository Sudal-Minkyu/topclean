/*
 * 서버 API와 주고 받게 될 데이터 정의
 * 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
 * 조합하여 'sr', 'nr' 같은 형식도 가능
 * 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
 */
const dtos = {
    send: {
        outsourcingPriceList: {
            biItemcode: "s",
            biName: "s",
            bpOutsourcingYn: "s",
        },
        outsourcingPriceSave: {
            biItemcode: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",

            setDt: "s",
            bpBasePrice: "n",
            bpAddPrice: "n",
            bpPriceA: "n",

            bpOutsourcingYn: "s",
            bpOutsourcingPrice: "n",
            bpRemark: "s",
            _$uid: "d",
        },
    },
    receive: {
        itemGroupcodeAndNameList: {
            bgItemGroupcode: "sr",
            bgName: "s",
        },

        outsourcingPriceList: {
            bsItemcode: "s",
            bgName: "s",
            bsName: "s",
            biName: "s",

            setDt: "s",
            bpBasePrice: "n",
            bpAddPrice: "n",
            bpPriceA: "n",

            bpOutsourcingYn: "s",
            bpOutsourcingPrice: "n",
            bpRemark: "s",
        },
    },
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    // 대분류 코드 가져오기
    getBsItemGroupCode() {
        CommonUI.ajax("/api/manager/itemGroupcodeAndNameList", "GET", false, function (res) {
            const data = res.sendData.bgItemListData;
            dv.chk(data, dtos.receive.itemGroupcodeAndNameList, "대분류 리스트 받아오기");
            const $bsItemGroup = $("#bsItemGroup");
            data.forEach(obj => {
                const htmlText = `<option value="${obj.bgItemGroupcode}">${obj.bgName}</option>`;
                $bsItemGroup.append(htmlText);
            })
        })
    },

    // 조회 조건 보내고 리스트 받기
    getOsPriceList(searchCondition) {
        wares.searchCondition = searchCondition;
        dv.chk(searchCondition, dtos.send.outsourcingPriceList, "외주가격 리스트 조회 조건 보내기");
        CommonUI.ajax("/api/manager/outsourcingPriceList", "MAPPER", searchCondition, function(res) {
            const data = res.sendData.gridListData;
            for (const obj of data) {
                if(obj.bpOutsourcingPrice == null || obj.bpOutsourcingPrice.length === 0) {
                    obj.bpOutsourcingPrice = 0;
                } else {
                   obj.bpOutsourcingPrice = obj.bpOutsourcingPrice;
                }
            }
            grids.f.clear(0);
            grids.f.set(0, data);
        })
    },

    // 변경한 값 저장
    setOsPriceList(changeData) {
        dv.chk(changeData, dtos.send.outsourcingPriceSave, "외주가격 리스트 변경 데이터 보내기");
        CommonUI.ajax("/api/manager/outsourcingPriceSave", "MAPPER", changeData, function() {
            alertSuccess("저장 완료");
            comms.getOsPriceList(wares.searchCondition);
        })
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
        /* 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다. */
        initialization() {

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: 'biItemcode',
                    headerText: '상품코드',
                    width: 80,
                    editable: false,
                }, {
                    dataField: 'bgName',
                    headerText: '대분류',
                    width: 80,
                    editable: false,
                }, {
                    dataField: 'bsName',
                    headerText: '중분류',
                    width: 80,
                    editable: false,
                }, {
                    dataField: 'biName',
                    headerText: '상품명',
                    width: 80,
                    editable: false,
                }, {
                    dataField: 'setDt',
                    headerText: '적용일자',
                    width: 100,
                    editable: false,
                }, {
                    dataField: 'bpBasePrice',
                    headerText: '기본금액',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                    editable: false,
                }, {
                    dataField: 'bpAddPrice',
                    headerText: '추가금액',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                    editable: false,
                }, {
                    dataField: 'bpPriceA',
                    headerText: '최종금액(A)',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                    editable: false,
                }, {
                    dataField: 'bpOutsourcingYn',
                    headerText: '외주처리대상',
                    width: 110,
                    renderer: {
                        type: "DropDownListRenderer",
                        list: ["Y", "N"]
                    }
                }, {
                    dataField: 'bpOutsourcingPrice',
                    headerText: '외주금액',
                    style: "grid_textalign_right",
                    width: 100,
                    dataType: "numeric",
                    autoThousandSeparator: "true",
                }, {
                    dataField: 'bpRemark',
                    headerText: '특이사항',
                    style: "grid_textalign_left",
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : true,
                selectionMode : 'singleRow',
                noDataMessage : '출력할 데이터가 없습니다.',
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

        /* 그리드 동작 처음 빈 그리드를 생성 */
        create() {
            for (const i in grids.s.columnLayout) {
                AUIGrid.create(grids.s.id[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
        get(numOfGrid) {
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        /* 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다. */
        set(numOfGrid, data) {
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        // 그리드 체크된 로우
        getCheckedItems(numOfGrid) {
            return AUIGrid.getCheckedRowItemsAll(grids.s.id[numOfGrid]);
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
        // 대분류 전체선택 외 선택시 인풋 활성화 및 상품코드 입력
        $("#bsItemGroup").on("change", function() {
            $("#bsItemGroupCode").val(this.value);
            if(this.value !== "") {
                $("#bsCode").attr("readonly", false);
            } else {
                $("#bsCode").attr("readonly", true);
                $("#bsCode").val("");
            }
        });

        // 조회 리스트 불러오기
        $("#searchListBtn").on("click", function() {
            searchOsPriceList();
        })

        // 외주가격 리스트 저장
        $("#saveOsPriceList").on("click", function() {
            saveOsPriceList();
        })

        /* 0번그리드 내의 셀 클릭시 이벤트 */
        AUIGrid.bind(grids.s.id[0], 'cellClick', function (e) {
            // console.log(e.item);
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    searchCondition: {

    },
};

/* 페이지가 로드되고 나서 실행 */
$(function() {
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
const onPageLoad = function() {
    comms.getBsItemGroupCode();

    grids.f.initialization();
    grids.f.create()

    trigs.basic();
};

function searchOsPriceList() {
    const searchCondition = {
        biItemcode: $("#bsItemGroupCode").val() + $("#bsCode").val(),
        biName: $("#biName").val(),
        bpOutsourcingYn: $("#bpOutsourcingYn").val(),
    };

    comms.getOsPriceList(searchCondition);
}

function saveOsPriceList() {
    const changeData = grids.f.getCheckedItems(0);
    if (changeData.length) {
        comms.setOsPriceList(changeData);
    } else {
        alertCaution("저장할 데이터가 없습니다. 저장할 내용을 체크해주세요.", 1);
    }
}