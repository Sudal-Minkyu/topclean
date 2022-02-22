/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {

    },
    receive: {

    }
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    save: "/api/",
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    save(formData) {
        console.log(Object.fromEntries(formData));
        wares.fdata = Object.fromEntries(formData);

        // API 시험시 주석을 풀고 urls.save 에 API 주소를 입력
        // CommonUI.ajax(urls.save, "POST", formData, function (res) {
        //     console.log(res);
        // });
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
    },
}

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    dataTransfer: new DataTransfer(),
}

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    trigs.basic();
    summernoteInit();
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
}

function saveProgress() {

    $("#selectedFiles")[0].files = wares.dataTransfer.files;
    const formData = new FormData(document.getElementById('writeForm'));
    formData.set("htContent", $('#summernote').summernote('code'));
    for(let file of wares.dataTransfer.files) {
        formData.append("filesThree", file, file.name);
    }

    comms.save(formData);
}