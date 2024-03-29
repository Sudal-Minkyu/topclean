/* 서버 API와 주고 받게 될 데이터 정의
* 's' 문자형, 'n' 숫자형, 'a' 배열형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 'sr', 'nr' 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        getPost: {
        },
        save: { // 이번만은 예외적으로 게시판의 id 요소가 숨어있다. 또한 삭제할 파일의 아이디리스트를 다루는 항목 추가 예정,
            id: '', // 존재할 경우 수정작업, 빈 문자일 경우 새 글 작성이 된다.
            subject: 's', // 제목
            content: 's', // 내용
            multipartFileList: '', // 업로드할 파일들이 input type='file' multiple 과 같은 형태로 올라간다.
            deleteFileList: '', // 지울 파일들의 id 리스트
        },
    },
    receive: {
        getPost: {
            hnType: 's',
            nextHnType: 's',
            preHnType: 's',
            fileList: {
                fileId: 'n',
                filePath: 's',
                fileFileName: 's',
                fileOriginalFilename: 's',
                fileVolume: 'n',
            },
            content: 's',
            subject: 's',
            insertDateTime: 's',
            isWritter: 's',
            name: 's',
            nextId: '',
            nextSubject: 's',
            nextInsertDateTime: 's',
            preId: '',
            preSubject: 's',
            preInsertDateTime: 's',
        },

    },
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    noticeget: '/api/manager/noticeView',
    noticesave: '/api/manager/noticeSave',
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData(condition) {
        dtos.send.getPost[wares[wares.boardType].idKeyName] = 'n'; // 게시판마다 id가 다르므로 dtos의 항목도 동적 추가
        dv.chk(condition, dtos.send.getPost, '읽어올 게시물의 아이디 보내기');
        CommonUI.ajax(urls[wares.boardType + 'get'], 'GET', condition, function (res) {
            dtos.receive.getPost[wares[wares.boardType].idKeyName] = 'n'; // 받는 dtos도 위와 마찬가지
            const data = res.sendData[wares[wares.boardType].dataKeyName];
            dv.chk(data, dtos.receive.getPost, '받아온 게시물');
            setFields(data);
        });
    },

    save(formData) {
        const jsonFormData = Object.fromEntries(formData);
        dv.chk(jsonFormData, dtos.send.save, '글쓰기 데이터 보내기');
        CommonUI.ajax(urls[wares.boardType + 'save'], 'POST', formData, function (res) {
            alertSuccess('저장이 완료되었습니다.');
            $('#successBtn').on('click', function () {
                if(wares.id) {
                    location.href = $('#previousLink').attr('href');
                } else {
                    location.href = `./${wares.boardType}view?prevPage=`
                        + wares.page + '&id=' + res.sendData.id;
                }
            });
        });
    },
};

/* 이벤트를 설정하거나 해지하는 함수들을 담는다. */
const trigs = {
    basic() {
        $('#fileSelector').on('change', function () {
            addFile();
        });

        $('#saveBtn').on('click', function () {
            saveProgress();
        });

        $('#fileListBtn').on('click', function () {
            $('#fileListPanel').toggleClass('active');
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    url: window.location.href,
    dataTransfer: new DataTransfer(),
    existFileList: [],
    boardType: '',
    id: 0,
    params: '',
    page: '',
    searchString: '',
    filterFromDt: '',
    filterToDt: '',
    totVolume: 0,
    existTotVolume: 0,
    totCnt: 0,
    existTotCnt: 0,
    taglost: {
        idKeyName: 'htId',
        dataKeyName: 'tagNoticeViewDto',
        masterKeyName: 'htId',
        commentKeyName: 'hcId',
    },
    notice: {
        idKeyName: 'hnId',
        dataKeyName: 'noticeViewDto',
        masterKeyName: 'hnId',
        commentKeyName: '',
    },
    deleteFileList: [], // 서버에 올라가 있는 지울 파일들의 id 리스트
};

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    trigs.basic();
    getParams();
    setInputs();

    summernoteInit();
    if(wares.id) { // 글의 id가 왔다는 것은 글이 새글쓰기가 아닌 수정모드임을 의미한다.
        const condition = {};
        condition[wares[wares.boardType].idKeyName] = parseInt(wares.id, 10);
        comms.getData(condition);
    }
}

function getParams() {
    const url = new URL(wares.url);
    const tokenedPath = url.pathname.split('/');
    const lastUrl = tokenedPath[tokenedPath.length - 1];
    wares.boardType = lastUrl.substring(0, lastUrl.length - 5);
    wares.params = url.searchParams;

    if(wares.params.has('id')) {
        wares.id = wares.params.get('id');
    }

    if(wares.params.has('prevPage')) {
        wares.page = wares.params.get('prevPage');
    } else {
        wares.page = '1';
    }

    if(wares.params.has('prevFilterFromDt')) {
        wares.filterFromDt = wares.params.get('prevFilterFromDt');
    }

    if(wares.params.has('prevFilterToDt')) {
        wares.filterToDt = wares.params.get('prevFilterToDt');
    }

    if(wares.params.has('prevSearchString')) {
        wares.searchString = wares.params.get('prevSearchString');
    }

    if(wares.params.has('hnType')) {
        wares.hnType = wares.params.get('hnType');
    } else {
        wares.hnType = '00';
    }
}

function setInputs() {
    let specialCondition = '';
    if(wares.boardType === 'notice') {
        specialCondition = '&hnType=' + wares.hnType;
    }
    if(wares.id) {
        $('#previousLink').attr('href', `./${wares.boardType}view?prevPage=` + wares.page + '&id=' + wares.id
            + '&prevSearchString=' + wares.searchString + '&prevFilterFromDt='
            + wares.filterFromDt + '&prevFilterToDt=' + wares.filterToDt + specialCondition);
    } else {
        $('#previousLink').attr('href', `./${wares.boardType}list?page=` + wares.page
            + '&searchString=' + wares.searchString + '&filterFromDt=' + wares.filterFromDt
            + '&filterToDt=' + wares.filterToDt + specialCondition);
    }
}

function summernoteInit() {
    $('#summernote').summernote({
        height: 530,
        minHeight: null,
        maxHeight: null,
        focus: true,
        lang: 'ko-KR',
    });
}

function addFile() {
    const files = $('#fileSelector')[0].files;
    for(const file of files) {
        wares.dataTransfer.items.add(file);
    }
    refreshFileList();
    $('#fileListPanel').addClass('active');
}

function saveProgress() {
    if(wares.totVolume > 52428800) {
        alertCaution('첨부파일 총용량은 50MB를 넘을 수 없습니다.', 1);
        return;
    }

    const subject = $('#subject').val();
    if(!subject) {
        alertCaution('제목을 입력해 주세요.', 1);
        return;
    }

    const formData = new FormData();
    formData.set('subject', subject);
    formData.set('content', $('#summernote').summernote('code'));

    if(wares.id) {
        formData.set('id', wares.id);
    }

    if(wares.dataTransfer.files.length) {
        for(const file of wares.dataTransfer.files) {
            formData.append('multipartFileList', file, file.name);
        }
    }

    if(wares.deleteFileList.length) {
        formData.append('deleteFileList', wares.deleteFileList);
    }

    comms.save(formData);
}

function refreshFileList() {
    $('#fileList').html('');

    /* 이미 서버에 존재하는 첨부 파일들의 리프래시 */
    for(let i = 0; i < wares.existFileList.length; i++) {
        let existVolume = 0;
        if(wares.existFileList[i].fileVolume > 1000000) {
            existVolume = (wares.existFileList[i].fileVolume / 1048576).toFixed(1).toLocaleString() + 'MB';
        } else {
            existVolume = Math.ceil(wares.existFileList[i].fileVolume / 1024).toLocaleString() + 'KB';
        }
        $('#fileList').append(`
            <li>
                <div class='board__upload-file-item'>
                    <span class='board__upload-filename'>${wares.existFileList[i].fileOriginalFilename}</span>
                    <span class='board__upload-filesize'>${existVolume}</span>
                    <button type='button' class='board__upload-delete'
                        onclick='removeExistFile(${i})'>삭제</button>
                </div>
            </li>
        `);
    }

    /* 새로 추가되는 첨부 파일들의 리프래시 */
    for(let i = 0; i <wares.dataTransfer.files.length; i++) {
        let volume = 0;
        if(wares.dataTransfer.files[i].size > 1000000) {
            volume = (wares.dataTransfer.files[i].size / 1048576).toFixed(1).toLocaleString() + 'MB';
        } else {
            volume = Math.ceil(wares.dataTransfer.files[i].size / 1024).toLocaleString() + 'KB';
        }
        $('#fileList').append(`
            <li>
                <div class='board__upload-file-item'>
                    <span class='board__upload-filename'>${wares.dataTransfer.files[i].name}</span>
                    <span class='board__upload-filesize'>${volume}</span>
                    <button type='button' class='board__upload-delete' onclick='removeFile(${i})'>삭제</button>
                </div>
            </li>
        `);
    }
    calculateFileStatus();
}

function calculateFileStatus() {
    /* 서버에 존재하는 파일 - 삭제예정인 파일의 카운팅 */
    let existCnt = 0;
    let existTotVolume = 0;

    for(const file of wares.existFileList) {
        existCnt++;
        existTotVolume += file.fileVolume;
    }

    wares.existTotCnt = existCnt;
    wares.existTotVolume = existTotVolume;

    /* 새로 추가한 파일의 카운팅 */
    let cnt = 0;
    let totVolume = 0;

    for(const file of wares.dataTransfer.files) {
        cnt++;
        totVolume += file.size;
    }

    wares.totCnt = cnt;
    wares.totVolume = totVolume;

    let sumVolume = wares.totVolume + wares.existTotVolume;
    if(sumVolume > 1000000) {
        sumVolume = (sumVolume / 1048576).toFixed(1).toLocaleString() + 'MB';
    } else {
        sumVolume = Math.ceil(sumVolume / 1024).toLocaleString() + 'KB';
    }

    $('#fileCnt').html(wares.totCnt + wares.existTotCnt);
    $('#fileTotVolume').html(sumVolume);
}

function removeFile(index) {
    const dt = new DataTransfer();
    const files = wares.dataTransfer.files;

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (index !== i) {
            dt.items.add(file);
        }
    }

    wares.dataTransfer = dt;
    refreshFileList();
}

function setFields(data) {
    $('#subject').val(data.subject);
    $('#summernote').summernote('code', data.content);

    wares.existFileList = data.fileList;
    refreshFileList();
}

function removeExistFile(index) {
    wares.deleteFileList.push(wares.existFileList[index].fileId);

    const newFileList = [];
    const files = wares.existFileList;

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (index !== i) {
            newFileList.push(file);
        }
    }

    wares.existFileList = newFileList;
    refreshFileList();
}
