/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        save: { // 이번만은 예외적으로 게시판의 id 요소가 숨어있다. 또한 삭제할 파일의 아이디리스트를 다루는 항목 추가 예정,
            id: "", // 존재할 경우 수정작업, null일 경우 새 글 쓰기가 된다.
            subject: "s", // 제목
            content: "s", // 내용
            multipartFileList: "", // 업로드할 파일들이 input type="file" multiple 과 같은 형태로 올라간다.
            deleteFileList: "", // 지울 파일들의 id 리스트
        },
    },
    receive: {

    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    taglostsave: "/api/manager/lostNoticeSave",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData() {

    },

    save(formData) {
        const jsonFormData = Object.fromEntries(formData);
        dv.chk(jsonFormData, dtos.send.save, "글쓰기 데이터 보내기");
        console.log(jsonFormData);
        CommonUI.ajax(urls[wares.boardType + "save"], "POST", formData, function (res) {
            console.log(res);
        });
    }
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $("#fileSelector").on("change", function () {
            addFile();
        });

        $("#saveBtn").on("click", function () {
            saveProgress();
        });

        $("#fileListBtn").on("click", function () {
            $("#fileListPanel").toggleClass("active");
        });
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    dataTransfer: new DataTransfer(),
    url: window.location.href,
    boardType: "",
    params: "",
    id: "",
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    trigs.basic();
    getParams();
    summernoteInit();
    if(wares.id) { // 글의 id가 왔다는 것은 글이 새글쓰기가 아닌 수정모드임을 의미한다.
        getData();
    } else {

    }
}

function getParams() {
    const url = new URL(wares.url);
    const tokenedPath = url.pathname.split("/");
    const lastUrl = tokenedPath[tokenedPath.length - 1];
    wares.boardType = lastUrl.substring(0, lastUrl.length - 5);
    wares.params = url.searchParams;

    if(wares.params.has("id")) {
        wares.id = wares.params.get("id");
    } else {
        wares.id = null;
    }
}

function getData() {

}

function summernoteInit() {
    $('#summernote').summernote({
        height: 530,
        minHeight: null,
        maxHeight: null,
        focus: true,
        lang: "ko-KR"
    });
}

function addFile() {
    const files = $("#fileSelector")[0].files;
    for(let file of files) {
        wares.dataTransfer.items.add(file);
    }
    refreshFileList();
    $("#fileListPanel").addClass("active");
}

function saveProgress() {
    const formData = new FormData();
    formData.set("subject", $("#subject").val());
    formData.set("content", $('#summernote').summernote('code'));
    formData.set("id", wares.id);
    if(wares.dataTransfer.files.length) {
        for(let file of wares.dataTransfer.files) {
            formData.append("multipartFileList", file, file.name);
        }
    } else {
        formData.set("multipartFileList", null);
    }
    formData.set("deleteFileList", null); // 삭제리스트 작성할 때 까지만
    comms.save(formData);
}

function refreshFileList() {
    $("#fileList").html("");
    for(let i = 0; i <wares.dataTransfer.files.length; i++) {
        $("#fileList").append(`
            <li>
                <div class="board__upload-file-item">
                    <span class="board__upload-filename">${wares.dataTransfer.files[i].name}</span>
                    <span class="board__upload-filesize">
                        ${Math.floor(wares.dataTransfer.files[i].size / 1024).toLocaleString()}KB</span>
                    <button type="button" class="board__upload-delete" onclick="removeFile(${i})">삭제</button>
                </div>
            </li>
        `);
    }
    calculateFileStatus();
}

function calculateFileStatus() {
    let cnt = 0;
    let totVolume = 0;
    for(let file of wares.dataTransfer.files) {
        cnt++;
        totVolume += file.size;
    }

    $("#fileCnt").html(cnt);
    $("#fileTotVolume").html(Math.floor(totVolume / 1024).toLocaleString());
}

function removeFile(index) {
    const dt = new DataTransfer();
    const files = wares.dataTransfer.files;

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (index !== i) dt.items.add(file);
    }

    wares.dataTransfer = dt;
    refreshFileList();
}