/* 서버 API와 주고 받게 될 데이터 정의
* 's' 문자형, 'n' 숫자형, 'r' 필수값, 'd' 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 'sr', 'nr' 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {
    send: {
        getPost: {},
        getReply: {},
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

        getReply: {
            isWritter: 's',
            modifyDt: 's',
            name: 's',
            preId: '',
            type: 's',
        },
    },
};

/* 통신에 사용되는 url들 기입 */
const urls = {
    notice: '/api/user/noticeView',
};

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 (communications) */
const comms = {
    getData(condition) {
        dtos.send.getPost[wares[wares.boardType].idKeyName] = 'n'; // 게시판마다 id가 다르므로 dtos의 항목도 동적 추가
        dv.chk(condition, dtos.send.getPost, '게시글의 아이디를 보내기');
        dtos.receive.getPost[wares[wares.boardType].idKeyName] = 'n'; // 받는 dtos도 위와 마찬가지
        CommonUI.ajax(urls[wares.boardType], 'GET', condition, function (res) {
            const data = res.sendData[wares[wares.boardType].dataKeyName];
            dv.chk(data, dtos.receive.getPost, '게시글 받기');
            setFields(data);
        });
    },

    getReplyList(condition) {
        dtos.send.getReply[wares[wares.boardType].idKeyName] = 'n'; // 게시판마다 id가 다르므로 dtos의 항목도 동적 추가
        dv.chk(condition, dtos.send.getReply, '덧글 조회를 위한 게시글의 아이디 보내기');
        dtos.receive.getReply[wares[wares.boardType].commentIdKeyName] = 'n';
        dtos.receive.getReply[wares[wares.boardType].commentKeyName] = 's';

        CommonUI.ajax(urls[wares.boardType + 'ReplyList'], 'GET', condition, function (res) {
            const data = res.sendData.commentListDto;
            dv.chk(data, dtos.receive.getReply, '조회한 덧글 가져오기');
            data.forEach(obj => {
                createReplyHtml(obj[wares[wares.boardType].commentIdKeyName], obj.name, obj.modifyDt, obj.hcComment, obj.type, obj.isWritter, obj.preId);
            });
        });
    },
};

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
    basic() {
        $('#attachSwitch').on('click', function () {
            $('#fileListPanel').toggleClass('active');
        });
    },
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
    url: window.location.href,
    id: '', // 글의 아이디
    params: '',
    page: '',
    searchString: '',
    filterFromDt: '',
    filterToDt: '',
    hnType: '',

    boardType: '', // 아래 게시판 종류의 이름이 담길 곳
    /* 게시판 종류에 따른 데이터들 */
    taglost: {
        idKeyName: 'htId',
        dataKeyName: 'tagNoticeViewDto',
        commentIdKeyName: 'hcId',
        commentKeyName: 'hcComment',
    },
    notice: {
        idKeyName: 'hnId',
        dataKeyName: 'noticeViewDto',
    },
    brnotice: {

    },
};

$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    getParams();
    trigs.basic();
    let condition = {};
    condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
    comms.getData(condition);
    if(!['notice', 'brnotice'].includes(wares.boardType)) {
        condition = {};
        condition[wares[wares.boardType].idKeyName] = parseInt(wares.id);
        comms.getReplyList(condition);
    }

    // lightbox option
    lightbox.option({
        'maxWidth': 1100,
        'positionFromTop': 190,
    });
}

function getParams() {
    let fromday = new Date();
    fromday.setDate(fromday.getDate() - 365);
    fromday = fromday.format('yyyy-MM-dd');
    const today = new Date().format('yyyy-MM-dd');

    const url = new URL(wares.url);
    const tokenedPath = url.pathname.split('/');
    const lastUrl = tokenedPath[tokenedPath.length - 1];
    wares.boardType = lastUrl.substring(0, lastUrl.length - 4);
    wares.params = url.searchParams;

    if(wares.params.has('id')) {
        wares.id = wares.params.get('id');
    } else {
        wares.id = '';
    }

    if(wares.params.has('prevPage')) {
        wares.page = wares.params.get('prevPage');
    } else {
        wares.page = '1';
    }

    if(wares.params.has('prevSearchString')) {
        wares.searchString = wares.params.get('prevSearchString');
    } else {
        wares.searchString = '';
    }

    if(wares.params.has('prevFilterFromDt')) {
        wares.filterFromDt = wares.params.get('prevFilterFromDt');
    } else {
        wares.filterFromDt = fromday;
    }

    if(wares.params.has('prevFilterToDt')) {
        wares.filterToDt = wares.params.get('prevFilterToDt');
    } else {
        wares.filterToDt = today;
    }

    if(wares.params.has('hnType')) {
        wares.hnType = wares.params.get('hnType');
    } else {
        wares.hnType = '00';
    }
}

function setFields(data) {
    $('#subject').html(data.subject);
    $('#name').html(wares.boardType === 'notice' ? CommonData.name.hnType[data.hnType] : data.name);
    $('#insertDateTime').html(data.insertDateTime);
    $('#content').html(data.content);
    const preHnType = data.preHnType ? CommonData.name.hnType[data.preHnType] : '';
    $('#preSubject').html(preHnType + ' ' + data.preSubject);
    $('#preInsertDateTime').html(data.preInsertDateTime);
    const nextHnType = data.nextHnType ? CommonData.name.hnType[data.nextHnType] : '';
    $('#nextSubject').html(nextHnType + ' ' + data.nextSubject);
    $('#nextInsertDateTime').html(data.nextInsertDateTime);

    let specialCondition = '';
    if(wares.boardType === 'notice') specialCondition = '&hnType=' + wares.hnType;

    if(data.preId) {
        $('#linkPrev').attr('href', `./${wares.boardType}view?id=` + data.preId + '&prevPage=' + wares.page
        + '&prevSearchString=' + wares.searchString + '&prevFilterFromDt=' + wares.filterFromDt
        + '&prevFilterToDt=' + wares.filterToDt + specialCondition);
    }

    if(data.nextId) {
        $('#linkNext').attr('href', `./${wares.boardType}view?id=` + data.nextId + '&prevPage=' + wares.page 
        + '&prevSearchString=' + wares.searchString + '&prevFilterFromDt=' + wares.filterFromDt
        + '&prevFilterToDt=' + wares.filterToDt + specialCondition);
    }

    /* 덧글 본문 처리방식 결정되면 수정할 것 */
    if(!['notice', 'brnotice'].includes(wares.boardType)){
        $('#linkReply').show();
        $('#replyList').show();
        $('#replyList').css('height', '300px');
        $('#content').css('height', '200px');
    }

    $('#linkReply').attr('href', `./${wares.boardType}reply?id=`
        + data[wares[wares.boardType].idKeyName] + '&prevPage=' + wares.page + '&prevSearchString='
        + wares.searchString + '&prevFilterFromDt=' + wares.filterFromDt + '&prevFilterToDt=' + wares.filterToDt
        + specialCondition);

    // 페이지 정보 가져와서 해당 페이지로 바로 갈 수 있도록 조치
    $('#linkPrevPage').attr('href', `./${wares.boardType}list?page=` + wares.page
        + '&searchString=' + wares.searchString + '&filterFromDt=' + wares.filterFromDt + '&filterToDt='
        + wares.filterToDt + specialCondition);

    $('#fileCnt').html(data.fileList.length);

    for(let file of data.fileList) {
        let volume = 0;
        if(file.fileVolume > 1000000) {
            volume = (file.fileVolume / 1048576).toFixed(1).toLocaleString() + 'MB';
        } else {
            volume = Math.ceil(file.fileVolume / 1024).toLocaleString() + 'KB';
        }

        /* 파일 확장자가 라이트박스 라이브러리를 지원하는 확장자인지 판단 */
        const isImage = ['PNG', 'BMP', 'JPG', 'JPEG', 'GIF', 'WEBP', 'SVG']
            .includes(file.fileFileName.split('.').pop().toUpperCase());
        const addProp = isImage ? `data-lightbox='images' data-title='${file.fileFileName}'` : ``;

        const element = `
            <li>
                <a href='${file.filePath + file.fileFileName}' ${addProp} class='board-view__file'>
                    <span class='board-view__filename'>${file.fileOriginalFilename}</span>
                    <span class='board-view__filesize'>${volume}</span>
                </a>
            </li>
        `;

        $('#fileList').append(element);
    }
}

function createReplyHtml(commentId, name, modifyDt, comment, type, isWriter, preId) {

    let htmlText = `
    <div class='reply__info'>
        <span class='reply__name'>${name}</span>
        <div class='reply__date'>${modifyDt}</div>
    </div>
    <div class='reply__comment'>${comment}</div>
    `;

    if(type === '1') {
        htmlText = `<li class='reply__item replyItem' data-id='${commentId}'>` + htmlText + `</li>`;
        $('#replyList').append(htmlText);
    }else if(type === '2') {
        const target = `.replyItem[data-id='${preId}']`;
        htmlText = `<div class='reply__item'>` + htmlText + `</div>`;
        $(target).append(htmlText);
    }
}