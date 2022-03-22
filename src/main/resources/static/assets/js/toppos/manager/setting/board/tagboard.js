/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        tagGalleryDetail: {
            btId: "nr",
        },
        tagGalleryList: {
            filterFromDt: "s",
            filterToDt: "s",
            type: "s", // 1 미완료, 2 전체, 3 가맹 응답
        },
        tagGallerySave: {
            btBrandName: "s",
            btInputDate: "s",
            btMaterial: "s",
            btRemark: "s",
            addPhotoList: "", // 새로 등록된 사진파일들의 리스트
            deletePhotoList: "", // 지울 사진 id들의 리스트
        },
        tagGalleryDelete: {
            btId: "nr",
        },
        tagGalleryEnd: {
            btId: "nr",
        },
    },
    receive: {
        tagGalleryList: {
            btId: "n",
            insertDateTime: "s",
            btBrandName: "s",
            btInputDate: "s",
            btMaterial: "s",
            btRemark: "s",
            tagGalleryCheckFranchise: "s",
            bfPathFilename: { // 썸네일 경로의 경우 이전처럼 파일이름 부분에 s_ 붙여서 오는 방식으로 처리하면 좋을듯 합니다.
                bfId: "n",
                bfPath: "s",
                bfFilename: "s",
            },
        },
        tagGalleryDetail: {
            tagGallery: {
                btId: "n",
                btBrandName: "s",
                btInputDate: "s",
                btMaterial: "s",
                btRemark: "s",
            },
            tagGalleryFileList: { // 썸네일 경로의 경우 이전처럼 파일이름 부분에 s_ 붙여서 오는 방식으로 처리하면 좋을듯 합니다.
                bfId: "n",
                bfPath: "s",
                bfFilename: "s",
            },
            tagGalleryCheckList: { // 가맹점 체크시 인서트, 해제시 딜리트가 된다.
                frCode: "s",
                frName: "s",
                brCompleteYn: "s", // 확인완료시 Y가 된다.
            },
        }
    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    getMainList: "/api/manager/tagGalleryList",
    putNewTaglost: "/api/manager/tagGallerySave",
    getDetail: "/api/manager/tagGalleryDetail",
    removeTaglost: "/api/manager/tagGalleryDelete",
    endTaglost: "/api/manager/tagGalleryEnd",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getMainList(searchCondition) {
        dv.chk(searchCondition, dtos.send.tagGalleryList, "택분실 조회 조건 보내기");
        CommonUI.ajax(urls.getMainList, "GET", searchCondition, function (res) {
            const dataList = res.sendData.tagGalleryList;
            dv.chk(dataList, dtos.receive.tagGalleryList, "조회된 택분실 리스트 받기");

            for(data of dataList) {
                for(const [i, obj] of data.bfPathFilename.entries()) {
                    data["thumbnail" + (i + 1)] = obj.bfPath + "s_" + obj.bfFilename;
                    if(i === 3) break;
                }
            }

            grids.f.setData(0, dataList);
        });
    },

    putNewTaglost(formData) {
        const testObj = Object.fromEntries(formData);
        if(testObj.btId) {
            dtos.send.tagGallerySave.btId = "s";
        } else {
            delete dtos.send.tagGallerySave.btId;
        }
        console.log(testObj);
        if(!testObj.btId) testObj.btId = 0; // btId값이 없는 신규등록건
        dv.chk(testObj, dtos.send.tagGallerySave, "택분실 등록, 수정하기");
        CommonUI.ajax(urls.putNewTaglost, "POST", formData, function (res)  {
            alertSuccess("게시물 저장이 완료되었습니다.");
            closeTaglostPop();
        });
    },

    getDetail(getCondition) {
        
        CommonUI.ajax(urls.getDetail, "GET", getCondition, function (res) {
            console.log(res);
            const data = res.sendData;

            const refinedData = {
                btId: data.tagGallery.btId,
                btBrandName: data.tagGallery.btBrandName,
                btInputDate: data.tagGallery.btInputDate,
                btMaterial: data.tagGallery.btMaterial,
                btRemark: data.tagGallery.btRemark,
                tagGalleryFileList: data.tagGalleryFileList,
                tagGalleryCheckList: data.tagGalleryCheckList,
            }

            wares.currentRequest = refinedData;
            resetTaglostPop();
            tagLostPopUpdateMode();

            for(photo of wares.currentRequest.tagGalleryFileList) {
                const photoHtml = `<li class="tag-imgs__item">
                    <a href="${photo.bfPath + photo.bfFilename}" data-lightbox="images" data-title="이미지 확대">
                        <img src="${photo.bfPath + "s_" + photo.bfFilename}" class="tag-imgs__img" alt=""/>
                    </a>
                </li>`
                $("#photoList").append(photoHtml);
                $("#noImgScreen").hide();
            }

            /* 가맹점 응답의 텍스트 구성 */
            let responseList = ""
            for(fr of wares.currentRequest.tagGalleryCheckList) {
                responseList += fr.frName + " ";
                responseList += fr.brCompleteYn === "Y" ? "(완료)" : "(확인요청)";
                responseList += " ,"
            }
            $("#frResponse").val(responseList.substring(0, responseList.length - 2));

            $("#btBrandName").val(wares.currentRequest.btBrandName);
            $("#btMaterial").val(wares.currentRequest.btMaterial);
            $("#btRemark").val(wares.currentRequest.btRemark);
            $("#btInputDate").val(wares.currentRequest.btInputDate);

            openTaglostPop();
        });
    },

    removeTaglost(target) {
        console.log(target);
        dv.chk(target, dtos.send.tagGalleryDelete, "택분실 게시물 삭제 아이디 보내기");
        CommonUI.ajax(urls.removeTaglost, "PARAM", target, function (res) {
            alertSuccess("게시물 삭제가 완료되었습니다.");
            resetTaglostPop();
            closeTaglostPop();
            console.log(res);
        });
    },

    endTaglost(target) {
        console.log(target);
        // dv.chk(target, dtos.send., "택분실 게시종료 아이디 보내기");
        CommonUI.ajax(urls.endTaglost, "PARAM", target, function (res) {
            alertSuccess("게시 종료가 완료되었습니다.");
            resetTaglostPop();
            closeTaglostPop();
            console.log(res);
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
            "grid_main"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grids.s.columnLayout[0] = [
                {
                    dataField: "insertDateTime",
                    headerText: "등록일시",
                    width: 150,
                    dataType: "date",
                    formatString: "yyyy-mm-dd hh:mm",
                }, {
                    dataField: "btBrandName",
                    headerText: "추정브랜드",
                    style: "grid_textalign_left",
                }, {
                    dataField: "btInputDate",
                    headerText: "예상지사<br>입고일",
                    width: 100,
                    dataType: "date",
                    formatString: "yyyy-mm-dd",
                }, {
                    dataField: "btMaterial",
                    headerText: "소재",
                    style: "grid_textalign_left",
                }, {
                    dataField: "btRemark",
                    headerText: "특이사항",
                    style: "grid_textalign_left",
                }, {
                    dataField: "thumbnail1", // src 내용이 와야 함
                    headerText: "사진1",
                    renderer : {
                        type : "ImageRenderer",
                        imgHeight : 100, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
                        srcFunction : function(rowIndex, columnIndex, value, item) {
                            return value ? value : ""; // 값이 없으면 기본 빈 이미지를 리턴
                        }
                    }
                }, {
                    dataField: "thumbnail2",
                    headerText: "사진2",
                    renderer : {
                        type : "ImageRenderer",
                        imgHeight : 100, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
                        srcFunction : function(rowIndex, columnIndex, value, item) {
                            return value ? value : ""; // 값이 없으면 기본 빈 이미지를 리턴
                        }
                    }
                }, {
                    dataField: "thumbnail3",
                    headerText: "사진3",
                    renderer : {
                        type : "ImageRenderer",
                        imgHeight : 100, // 이미지 높이, 지정하지 않으면 rowHeight에 맞게 자동 조절되지만 빠른 렌더링을 위해 설정을 추천합니다.
                        srcFunction : function(rowIndex, columnIndex, value, item) {
                            return value ? value : ""; // 값이 없으면 기본 빈 이미지를 리턴
                        }
                    }
                }, {
                    dataField: "tagGalleryCheckFranchise",
                    headerText: "가맹응답상태",
                    style: "grid_textalign_left",
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        // 온 데이터 확인하여 가공필요
                        return value;
                    },
                }, {
                    dataField: "detail",
                    headerText: "상세보기",
                    width: 70,
                    renderer : {
                        type: "TemplateRenderer",
                    },
                    labelFunction : function (rowIndex, columnIndex, value, headerText, item ) {
                        return `<button class="c-state">보기</button>`;
                    },
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grids.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: false,
                showRowCheckColumn: false,
                showRowNumColumn : false,
                showStateColumn : false,
                enableFilter : false,
                rowHeight : 100,
                headerHeight : 48,
            };
        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grids.s.columnLayout) {
                grids.s.id[i] = AUIGrid.create(grids.s.targetDiv[i], grids.s.columnLayout[i], grids.s.prop[i]);
            }
        },

        getData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            return AUIGrid.getGridData(grids.s.id[numOfGrid]);
        },

        setData(numOfGrid, data) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            AUIGrid.setGridData(grids.s.id[numOfGrid], data);
        },

        clearData(numOfGrid) { // 해당 배열 번호 그리드의 데이터 비우기
            AUIGrid.clearGridData(grids.s.id[numOfGrid]);
        },

        resize(num) { // 해당 배열 번호 그리드의 크기를 현제 그리드를 감싼 엘리먼트에 맞춰 조절
			AUIGrid.resize(grids.s.id[num]);
		},

        getCheckedItems(numOfGrid) { // 해당 배열 번호 그리드의 엑스트라 체크박스 선택된 (아이템 + 행번호) 객체 반환
            return AUIGrid.getCheckedRowItems(grids.s.id[numOfGrid]);
        }
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    grid() {
        AUIGrid.bind(grids.s.id[0], "cellClick", function (e) {
            console.log(e);
            switch(e.dataField) { // 썸네일 클릭시 이미지가 뜨고 디테일 클릭시 상세보기가 작동한다.
                case "thumbnail1" :
                case "thumbnail2" :
                case "thumbnail3" :
                    if(e.item[e.dataField]) {
                        $("#gridPhoto").attr("href", e.item.bfPathFilename[e.dataField.substring(9, 10) - 1].bfPath
                            + e.item.bfPathFilename[e.dataField.substring(9, 10) - 1].bfFilename);
                        $("#gridPhoto").trigger("click");
                    }
                    break;
                case "detail" :
                        showDetail(e.item.btId);
                    break;
            }
        });
    },

    basic() {
        $("#searchBtn").on("click", function () {
            searchOrder();
        });

        $("#addBtn").on("click", function () {
            wares.currentRequest = {};
            resetTaglostPop();
            tagLostPopCreateMode();
            openTaglostPop();
        });

        $("#closeTaglostPop").on("click", function () {
            closeTaglostPop();
        });

        $("#savePost").on("click", function () {
            savePost();
        });

        $("#endPost").on("click", function () {
            alertCheck("게시종료 하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const target = {
                    btId: wares.currentRequest.btId,
                }
                comms.endTaglost(target);
            });
        });

        $("#takePhotoBtn").on("click", function () {
            takePhoto();
        });

        $("#removePost").on("click", function () {
            alertCheck("현재 게시물을 삭제하시겠습니까?");
            $("#checkDelSuccessBtn").on("click", function () {
                const target = {
                    btId: wares.currentRequest.btId,
                }
                comms.removeTaglost(target);
            });
        });
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    currentRequest: {},
    searchCondition: {},
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grids.f.initialization();
    grids.f.create();
    enableDatepicker();
    trigs.basic();
    trigs.grid();

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190
    });
}

function enableDatepicker() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 6);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");

    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(fromday);
    $("#" + datePickerTargetIds[1]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
}

function searchOrder() {
    const searchCondition = {
        filterFromDt: $("#filterFromDt").val().numString(),
        filterToDt: $("#filterToDt").val().numString(),
        type: $("#type").val(), // 1 미완료, 2 전체, 3 내 가맹점
    };
    wares.searchCondition = searchCondition;

    comms.getMainList(searchCondition);
}

function closeTaglostPop() {
    $("#taglostPop").removeClass("active");
    comms.getMainList(wares.searchCondition);
}

async function openTaglostPop() {

    try {
        wares.isCameraExist = true;
        wares.cameraStream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: {
                width: {ideal: 4096},
                height: {ideal: 2160}
            },
        });

        const screen = document.getElementById("cameraScreen");
        screen.srcObject = wares.cameraStream;
    }catch (e) {
        if(!(e instanceof DOMException)) {
            CommonUI.toppos.underTaker(e, "tagboard : 카메라 찾기");
        }
        wares.isCameraExist = false;
    }

    $("#taglostPop").addClass("active");
}

function takePhoto() {
    if($("#photoList").children().length > 5) {
        alertCaution("사진은 최대 6장 까지 촬영하실 수 있습니다.", 1);
        return false;
    }
    
    if(wares.isCameraExist && wares.cameraStream.active) {
        try {
            const video = document.getElementById("cameraScreen");
            const canvas = document.getElementById('cameraCanvas');
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            const context = canvas.getContext('2d');
            context.drawImage(video, 0, 0, canvas.width, canvas.height);

            const takenPic = canvas.toDataURL();
            const blob = b64toBlob(takenPic);

            const photoHtml = `<li class="tag-imgs__item newPhoto">
                <a href="${takenPic}" data-lightbox="images" data-title="이미지 확대">
                    <img src="${takenPic}" class="tag-imgs__img" alt=""/>
                </a>
            </li>`
            if(!wares.currentRequest.addPhotoList) {
                wares.currentRequest.addPhotoList = [];
            }
            wares.currentRequest.addPhotoList.push(blob);
            $("#photoList").append(photoHtml);
            $("#noImgScreen").hide();
        } catch (e) {
            CommonUI.toppos.underTaker(e, "tagboard : 사진 촬영");
        }
    } else {
        alertCaution("카메라가 감지되지 않아서<br>촬영을 할 수 없습니다.", 1);
    }
}

function resetTaglostPop() {
    resetDatePicker();
    $("#btBrandName").val("");
    $("#btMaterial").val("");
    $("#btRemark").val("");
    $("#frResponse").val("");
    $("#photoList").html("");
    $("#noImgScreen").show();
}

function tagLostPopCreateMode() {
    $("#responseDiv").hide();
    $("#endPost").hide();
    $("#removePost").hide();
}

function tagLostPopUpdateMode() {
    $("#responseDiv").show();
    $("#endPost").show();
    $("#removePost").show();
}

function closeTaglostPop() {
    $("#taglostPop").removeClass("active");
    if(wares.isCameraExist) {
        try {
            wares.cameraStream.getTracks().forEach(function (track) {
                track.stop();
            });
        }catch (e) {
            CommonUI.toppos.underTaker(e, "tagboard : 카메라 스트림 찾아서 끄기");
        }
    }
}

function enableDatepicker() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 6);
    fromday = fromday.format("yyyy-MM-dd");
    const today = new Date().format("yyyy-MM-dd");
    /* datepicker를 적용시킬 대상들의 dom id들 */
    const datePickerTargetIds = [
        "btInputDate", "filterFromDt", "filterToDt"
    ];

    $("#" + datePickerTargetIds[0]).val(today);
    $("#" + datePickerTargetIds[1]).val(fromday);
    $("#" + datePickerTargetIds[2]).val(today);

    const dateAToBTargetIds = [
        ["filterFromDt", "filterToDt"]
    ];

    CommonUI.setDatePicker(datePickerTargetIds);
    CommonUI.restrictDateAToB(dateAToBTargetIds);
    CommonUI.restrictDate("btInputDate", "btInputDate", false);
}

function resetDatePicker() {
    const today = new Date().format("yyyy-MM-dd");
    $("#btInputDate").val(today);
}

function b64toBlob(dataURI) { // 파일을 ajax 통신에 쓰기 위해 변환
    const byteString = atob(dataURI.split(',')[1]);
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);

    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: 'image/jpeg' });
}

function savePost() {
    if(!$("#photoList").children().length) {
        alertCaution("최소한 한장의 사진을 촬영해 주세요.", 1);
        return false;
    }

    const formData = new FormData();
    for(addPhoto of wares.currentRequest.addPhotoList) { // 새로 촬영된 사진들의 추가
        formData.append("addPhotoList", addPhoto);
    }
    
    if(wares.currentRequest.btId) {
        formData.append("btId", wares.currentRequest.btId);
    }

    formData.append("deletePhotoList", wares.currentRequest.deletePhotoList ? wares.currentRequest.deletePhotoList : "");
    formData.append("btBrandName", $("#btBrandName").val());
    formData.append("btMaterial", $("#btMaterial").val());
    formData.append("btInputDate", $("#btInputDate").val().numString());
    formData.append("btRemark", $("#btRemark").val());
    
    comms.putNewTaglost(formData);
}

function showDetail(btId) {
    const getCondition = {
        btId: btId
    };

    comms.getDetail(getCondition);
}