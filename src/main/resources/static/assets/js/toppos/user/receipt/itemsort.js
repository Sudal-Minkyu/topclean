$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dto = {
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
const data = {

}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const ajax = {
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function(req) {
            if(numOfGrid === 0) {
                dv.chk(req.sendData.gridListData, dto.receive.franchiseItemGroupList,
                    "대분류 위치 조정 데이터 받아오기");
            }
            grid.s.data[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(grid.s.id[numOfGrid], grid.s.data[numOfGrid]);
        });
    },
    saveSortData(numOfGrid, dataList) {
        if(numOfGrid === 0) {
            if(dv.chk(dataList.list, dto.send.franchiseItemGroupUpdate, "대분류 위치 저장", true)) {
                return;
            }
        }else if(numOfGrid === 2) {
            if(dv.chk(dataList.list, dto.send.franchiseItemSortUpdate, "상품 위치 저장", true)) {
                return;
            }
        }
        console.log(dataList);
        CommonUI.ajax(grid.s.url.update[numOfGrid], "MAPPER", dataList, function(req) {
            alertSuccess("조정 정보 저장을 완료했습니다.");
        });
    },
    filterBsList(condition) {
        dv.chk(condition, dto.send.franchiseItemSortList, "분류 항목 검색 조건");
        CommonUI.ajax(grid.s.url.read[1], "PARAM", condition, function(req) {
            grid.s.data[1] = req.sendData.gridListData;
            dv.chk(grid.s.data[1], dto.receive.franchiseItemSortList, "분류 항목 검색 결과");
            AUIGrid.setGridData(grid.s.id[1], grid.s.data[1]);
        });
    },
    filterBiList(condition) {
        dv.chk(condition, dto.send.franchiseItemList, "상품 위치 조정 데이터 받아오기");
        CommonUI.ajax(grid.s.url.read[2], "PARAM", condition, function(req) {
            grid.s.data[2] = req.sendData.gridListData;
            AUIGrid.setGridData(grid.s.id[2], grid.s.data[2]);
        });
    },

};

/* .s : AUI 그리드 관련 설정들
*  같은 번호의 배열에 있는 요소들 끼리 철저하게 연동한다는 원칙을 따른다.
*  어쩔 수 없이 한 그리드에 여러개의 요소가 필요할 경우 다차원 배열을 통해 구현한다.
*  .f : 그리드 관련 함수들 배치
*  .e : 그리드 객체에 걸리는 이벤트들 배치
* */
const grid = {
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
        initialization() { // 가시성을 위해 grid.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grid.s.columnLayout[0] = [
                {
                    dataField: "bgItemGroupcode",
                    headerText: "분류코드",
                }, {
                    dataField: "bgName",
                    headerText: "분류명칭",
                }, {
                    dataField: "bgFavoriteYn",
                    headerText: "즐겨찾기",
                    style: "grid_input_bgfavoriteyn",
                    renderer : {
                        type : "DropDownListRenderer",
                        list : ["Y", "N"],
                    },
                }
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grid.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : false,
                enableFilter : true,
                showRowNumColumn : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grid.s.columnLayout[1] = [
                {
                    dataField: "",
                    headerText: "분류코드",
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return item.bgItemGroupcode + item.bsItemGroupcodeS;
                    }
                }, {
                    dataField: "",
                    headerText: "분류명칭",
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return item.bgName + " " + item.bsName;
                    }
                },
            ];

            grid.s.prop[1] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : false,
                enableFilter : true,
                showRowNumColumn : false,
                rowHeight : 48,
                headerHeight : 48,
            };

            grid.s.columnLayout[2] = [
                {
                    dataField: "",
                    headerText: "상품코드",
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return item.biItemcode;
                    }
                }, {
                    dataField: "",
                    headerText: "상품명",
                    labelFunction: function(rowIndex, columnIndex, value, headerText, item) {
                        return item.biName;
                    }
                },
            ];

            grid.s.prop[2] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : false,
                enableFilter : true,
                showRowNumColumn : false,
                rowHeight : 48,
                headerHeight : 48,
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성 후 창에 맞게 리사이징
            for (const i in grid.s.columnLayout) {
                grid.s.id[i] = AUIGrid.create(grid.s.targetDiv[i], grid.s.columnLayout[i], grid.s.prop[i]);
            }
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            ajax.setDataIntoGrid(numOfGrid, grid.s.url.read[numOfGrid]);
        },

        resizeOnTab(type) {
            if(type === 0) {
                AUIGrid.resize(grid.s.id[0]);
            }else if(type === 1) {
                AUIGrid.resize(grid.s.id[1]);
                AUIGrid.resize(grid.s.id[2]);
            }
        },

        getGridData(numOfGrid) {
            return AUIGrid.getGridData(grid.s.id[numOfGrid]);
        }
    },

    e: {
        basicEvent() {
            AUIGrid.bind(grid.s.id[1], "cellClick", function (e) {
                const condition = CommonUI.newDto(dto.send.franchiseItemList);
                condition.bgItemGroupcode = e.item.bgItemGroupcode;
                condition.bsItemGroupcodeS = e.item.bsItemGroupcodeS;
                ajax.filterBiList(condition);
            });
        }
    }
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grid.e에 위치 */
const event = {
    s: { // 이벤트 설정
        switchTab() {
            const $tabsBtn = $('.c-tabs__btn');
            const $tabsContent = $('.c-tabs__content');

            $tabsBtn.on('click', function() {
                const idx = $(this).index();

                $tabsBtn.removeClass('active');
                $tabsBtn.eq(idx).addClass('active');
                $tabsContent.removeClass('active');
                $tabsContent.eq(idx).addClass('active');

                grid.f.resizeOnTab(idx);
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
                const data = {
                    filterCode: $("#filterCode").val(),
                    filterName: $("#filterName").val(),
                }
                ajax.filterBsList(data);
                AUIGrid.clearGridData(grid.s.id[2]);
            });
        }
    },
    r: { // 이벤트 해제

    }
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grid.f.initialization();
    grid.f.create();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    grid.f.setInitialData(0);

    grid.e.basicEvent();

    event.s.switchTab();
    event.s.pushUpDown();
    event.s.save();
    event.s.filterBs();

    $("#filterBs").trigger("click");
}

function pushUp(numOfGrid) {
    const selectedRowId = AUIGrid.getSelectedIndex(grid.s.id[numOfGrid]);
    AUIGrid.moveRowsToUp(grid.s.id[numOfGrid], selectedRowId);
}

function pushDown(numOfGrid) {
    const selectedRowId = AUIGrid.getSelectedIndex(grid.s.id[numOfGrid]);
    AUIGrid.moveRowsToDown(grid.s.id[numOfGrid], selectedRowId);

}

function saveSort(numOfGrid) {
    const gridData = grid.f.getGridData(numOfGrid);
    let refinedData = [];
    let newDto;

    for(let i = 0; i < gridData.length; i++) {
        if(numOfGrid === 0) {
            newDto = CommonUI.newDto(dto.send.franchiseItemGroupUpdate);
            newDto.bgItemGroupcode = gridData[i].bgItemGroupcode;
            newDto.bgFavoriteYn = gridData[i].bgFavoriteYn;
            newDto.bgSort = i;
            refinedData.push(newDto);
        }else if(numOfGrid === 2) {
            newDto = CommonUI.newDto(dto.send.franchiseItemSortUpdate);
            newDto.biItemcode = gridData[i].biItemcode;
            newDto.bfSort = i;
            refinedData.push(newDto);
        }
    }

    const param = {
        "list": refinedData
    };

    ajax.saveSortData(numOfGrid, param);
}