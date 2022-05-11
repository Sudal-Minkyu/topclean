$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        franchiseItemGroupUpdate: {
            bgItemGroupcode: "sr",
            bgFavoriteYn: "s",
            bgSort: "nr",
        },
        franchiseItemSortList: {
            filterCode: "s",
            filterName: "s",
        },
        franchiseItemList: {
            bgItemGroupcode: "sr",
            bsItemGroupcodeS: "sr",
        },
        franchiseItemSortUpdate: {
            biItemcode: "sr",
            bfSort: "nr",
        }
    },
    receive: {
        franchiseItemGroupList: {
            bgSort: "nr",
            bgItemGroupcode: "sr",
            bgFavoriteYn: "s",
            bgName: "s",
            bgIconFilename: "d",
        },
        franchiseItemSortList: {
            bgItemGroupcode: "sr",
            bgName: "s",
            bsItemGroupcodeS: "sr",
            bsName: "s",
        },
        franchiseItemList: {
            biItemcode: "sr",
            biName: "s",
            bfSort: "nr",
        }
    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const wares = {

}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const comms = {
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function(req) {
            if(numOfGrid === 0) {
                dv.chk(req.sendData.gridListData, dtos.receive.franchiseItemGroupList,
                    "대분류 위치 조정 데이터 받아오기");
            }
            grids.s.data[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(grids.s.id[numOfGrid], grids.s.data[numOfGrid]);
        });
    },

    saveSortData(numOfGrid, dataList) {
        if(numOfGrid === 0) {
            if(dv.chk(dataList.list, dtos.send.franchiseItemGroupUpdate, "대분류 위치 저장", true)) {
                return;
            }
        }else if(numOfGrid === 2) {
            if(dv.chk(dataList.list, dtos.send.franchiseItemSortUpdate, "상품 위치 저장", true)) {
                return;
            }
        }
        console.log(dataList);
        CommonUI.ajax(grids.s.url.update[numOfGrid], "MAPPER", dataList, function(req) {
            alertSuccess("조정 정보 저장을 완료했습니다.");
            grids.f.resetState(numOfGrid);
        });
    },

    filterBsList(condition) {
        dv.chk(condition, dtos.send.franchiseItemSortList, "분류 항목 검색 조건");
        CommonUI.ajax(grids.s.url.read[1], "PARAM", condition, function(req) {
            grids.s.data[1] = req.sendData.gridListData;
            dv.chk(grids.s.data[1], dtos.receive.franchiseItemSortList, "분류 항목 검색 결과");
            AUIGrid.setGridData(grids.s.id[1], grids.s.data[1]);
        });
    },
    filterBiList(condition) {
        dv.chk(condition, dtos.send.franchiseItemList, "상품 위치 조정 데이터 받아오기");
        CommonUI.ajax(grids.s.url.read[2], "PARAM", condition, function(req) {
            grids.s.data[2] = req.sendData.gridListData;
            AUIGrid.setGridData(grids.s.id[2], grids.s.data[2]);
        });
    },

};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grids = {
    s: { // 그리드 세팅
        targetDiv: [
            "grid_bgOrder", "grid_bsList", "grid_biOrder"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/user/", "/api/user/", "/api/user/"
            ],
            read: [
                "/api/user/franchiseItemGroupList", "/api/user/franchiseItemSortList", "/api/user/franchiseItemList"
            ],
            update: [
                "/api/user/franchiseItemGroupUpdate", "/api/user/", "/api/user/franchiseItemSortUpdate"
            ],
            delete: [
                "/api/user/", "/api/user/", "/api/user/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: "bgItemGroupcode",
                    headerText: "분류코드",
                }, {
                    dataField: "bgName",
                    headerText: "분류명칭",
                    style: "grid_textalign_left",
                }, {
                    dataField: "bgFavoriteYn",
                    headerText: "즐겨찾기",
                    style: "grid_input_bgfavoriteyn",
                    renderer : {
                        type : "DropDownListRenderer",
                        list : ["Y", "N"],
                    },
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        if(value !== "Y") {
                            item.bgFavoriteYn = "N";
                        }
                        return item.bgFavoriteYn;
                    },
                }
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                rowNumHeaderText : "순번",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : true,
                showStateColumn : false,
                enableFilter : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[1] = [
                {
                    dataField: "",
                    headerText: "분류코드",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return item.bgItemGroupcode + item.bsItemGroupcodeS;
                    }
                }, {
                    dataField: "",
                    headerText: "분류명칭",
                    style: "grid_textalign_left",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return item.bgName + " " + item.bsName;
                    }
                },
            ];

            grids.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: true,
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grids.s.columnLayout[2] = [
                {
                    dataField: "",
                    headerText: "상품코드",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return item.biItemcode;
                    }
                }, {
                    dataField: "",
                    headerText: "상품명",
                    style: "grid_textalign_left",
                    labelFunction(rowIndex, columnIndex, value, headerText, item) {
                        return item.biName;
                    }
                },
            ];

            grids.s.prop[2] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                rowNumHeaderText : "순번",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : true,
                showStateColumn : false,
                enableFilter : false,
                rowHeight : 48,
                headerHeight : 48,
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성 후 창에 맞게 리사이징
            for (const i in grids.s.columnLayout) {
                grids.s.id[i] = AUIGrid.create(grids.s.targetDiv[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            comms.setDataIntoGrid(numOfGrid, grids.s.url.read[numOfGrid]);
        },

        resizeOnTab(type) {
            if(type === 0) {
                AUIGrid.resize(grids.s.id[0]);
            }else if(type === 1) {
                AUIGrid.resize(grids.s.id[1]);
                AUIGrid.resize(grids.s.id[2]);
            }
        },

        getGridData(numOfGrid) {
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        resetState(numOfGrid) {
            AUIGrid.resetUpdatedItems(grids.s.id[numOfGrid]);
        },
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. */
const trigs = {
    grid() {
        AUIGrid.bind(grids.s.id[1], "cellClick", function (e) {
            const condition = CommonUI.newDto(dtos.send.franchiseItemList);
            condition.bgItemGroupcode = e.item.bgItemGroupcode;
            condition.bsItemGroupcodeS = e.item.bsItemGroupcodeS;
            comms.filterBiList(condition);
        });
    },

    switchTab() {
        const $tabsBtn = $('.c-tabs__btn');
        const $tabsContent = $('.c-tabs__content');

        $tabsBtn.on('click', function() {
            const idx = $(this).index();

            $tabsBtn.removeClass('active');
            $tabsBtn.eq(idx).addClass('active');
            $tabsContent.removeClass('active');
            $tabsContent.eq(idx).addClass('active');

            grids.f.resizeOnTab(idx);
        });
    },

    pushUpDown() {
        $("#bgUp").on("click", function() {
            pushUp(0)
        });
        $("#bgDown").on("click", function() {
            pushDown(0)
        });
        $("#biUp").on("click", function() {
            pushUp(2)
        });
        $("#biDown").on("click", function() {
            pushDown(2)
        });
    },

    save() {
        $("#bgSortSave").on("click", function() {
            saveSort(0);
        });

        $("#biSortSave").on("click", function() {
            saveSort(2);
            alertSuccess("상품 위치조정을 완료했습니다.");
        });
    },
    
    filterBs() {
        $("#filterBs").on("click", function() {
            filterBsList();
        });
    },

    vkeys() {
        $("#vkeyboard0").on("click", function () {
            onShowVKeyboard(0);
        });

        $("#vkeyboard1").on("click", function () {
            onShowVKeyboard(1);
        });
    }
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();

    /* grids.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    grids.f.setInitialData(0);

    /* 가상키보드의 사용 선언 */
    window.vkey = new VKeyboard();

    trigs.grid();
    trigs.switchTab();
    trigs.pushUpDown();
    trigs.save();
    trigs.filterBs();
    trigs.vkeys();

    $("#filterBs").trigger("click");
}

function pushUp(numOfGrid) {
    const selectedRowId = AUIGrid.getSelectedIndex(grids.s.id[numOfGrid]);
    AUIGrid.moveRowsToUp(grids.s.id[numOfGrid], selectedRowId);
}

function pushDown(numOfGrid) {
    const selectedRowId = AUIGrid.getSelectedIndex(grids.s.id[numOfGrid]);
    AUIGrid.moveRowsToDown(grids.s.id[numOfGrid], selectedRowId);

}

function saveSort(numOfGrid) {
    const gridData = grids.f.getGridData(numOfGrid);
    let refinedData = [];
    let newDto;

    for(let i = 0; i < gridData.length; i++) {
        if(numOfGrid === 0) {
            newDto = CommonUI.newDto(dtos.send.franchiseItemGroupUpdate);
            newDto.bgItemGroupcode = gridData[i].bgItemGroupcode;
            newDto.bgFavoriteYn = gridData[i].bgFavoriteYn;
            newDto.bgSort = i;
            refinedData.push(newDto);
        }else if(numOfGrid === 2) {
            newDto = CommonUI.newDto(dtos.send.franchiseItemSortUpdate);
            newDto.biItemcode = gridData[i].biItemcode;
            newDto.bfSort = i;
            refinedData.push(newDto);
        }
    }

    const param = {
        "list": refinedData
    };

    comms.saveSortData(numOfGrid, param);
}

function onShowVKeyboard(num) {
    /* 가상키보드 사용을 위해 */
    let vkeyProp = [];
    const vkeyTargetId = ["filterCode", "filterName"];


    vkeyProp[0] = {
        title: "분류코드 검색어 입력",
        defaultKeyboard: 1,
        callback: filterBsList,
    };

    vkeyProp[1] = {
        title: "분류명칭 검색어 입력",
        callback: filterBsList,
    };

    vkey.showKeyboard(vkeyTargetId[num], vkeyProp[num]);
}

function filterBsList() {
    const data = {
        filterCode: $("#filterCode").val(),
        filterName: $("#filterName").val(),
    }
    comms.filterBsList(data);
    AUIGrid.clearGridData(grids.s.id[2]);
}