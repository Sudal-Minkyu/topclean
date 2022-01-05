$(function() { // 페이지가 로드되고 나서 실행
    onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의 */
const dto = {
    send: {
		franchiseMyInfoSave: {
			frCode: "sr",
			frName: "sr",
			frContractDt: "sr",
			frContractFromDt: "sr",
			frContractToDt: "sr",
			brName: "sr",
			brCarculateRateHq: "nr",
			brCarculateRateBr: "nr",
			brCarculateRateFr: "nr",
			frBusinessNo: "s",
			frRpreName: "s",
			frTelNo: "s",
			frTagNo: "sr",
			frEstimateDuration: "nr",
		},
		
    },
    receive: {
		myInfo: {
			frCode: "sr",
			frName: "sr",
			frContractDt: "sr",
			frContractFromDt: "sr",
			frContractToDt: "sr",
			brName: "sr",
			brCarculateRateHq: "nr",
			brCarculateRateBr: "nr",
			brCarculateRateFr: "nr",
			frBusinessNo: "s",
			frRpreName: "s",
			frTelNo: "s",
			frTagNo: "sr",
			frEstimateDuration: "nr",
			brContractFromDt: "d",
		},
		franchiseAddProcessList: {
			repairListData: {
				baId: "nr",
				baName: "sr",
				baRemark: "s",
			},
			addAmountData: {
				baId: "nr",
				baName: "sr",
				baRemark: "s",
			},
			keyWordData: {
				baId: "nr",
				baName: "sr",
				baRemark: "s",
			}
		},
    }
};

/* dto가 아닌 일반적인 데이터들 정의 */
const data = {

}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const ajax = {
    setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
        CommonUI.ajax(url, "GET", false, function (req) {
            grid.s.data[numOfGrid] = req.sendData.gridListData;
            AUIGrid.setGridData(grid.s.id[numOfGrid], grid.s.data[numOfGrid]);
        });
    },
    // 가맹점 정보 받기
    getFrInfo() {
		const url = "/api/user/myInfo";
		CommonUI.ajax(url, "GET", false, function(res) {
			
			const data = res.sendData.franchisInfoDto;
			dv.chk(data, dto.receive.myInfo, "가맹점 정보 받아오기");
			putFrInfoDataInField(data);
		});
	},
	// 가맹점 정보 저장
	saveMyInfo(formData) {
		dv.chk(Object.fromEntries(formData), dto.send.franchiseMyInfoSave, "가맹점 정보 보내기");
		const url = "/api/user/franchiseMyInfoSave";
		CommonUI.ajax(url, "POST", formData, function(res) {
			
		});
	},
	
	// 상용구 항목 받기
	getFrFavorite(num) {
		const url = "/api/user/franchiseAddProcessList";
		CommonUI.ajax(url, "GET", {baType: num}, (res) => {
			let data;
			switch(num) {
				case "1" :
					data = res.sendData.repairListData;
					dv.chk(data, dto.receive.franchiseAddProcessList.repairListData, "수선항목 받아오기");
				break;
				
				case "2" :
					data = res.sendData.addAmountData;
					dv.chk(data, dto.receive.franchiseAddProcessList.addAmountData, "추가항목 받아오기");
				break;
				
				case "3" :
					data = res.sendData.keyWordData;
					dv.chk(data, dto.receive.franchiseAddProcessList.keyWordData, "상용구 받아오기");
				break;
			}
			// dv.chk(data, dto.receive.franchiseAddProcessList, "상용구, 수선항목, 추가항목 받아오기");
			grid.f.setFavoriteData(num - 1, res.sendData);
		});
	}
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
            "targetHtmlId"
        ],
        columnLayout: [],
        prop: [],
        id: [],
        data: [],
        url: {
            create: [
                "/api/"
            ],
            read: [
                "/api/"
            ],
            update: [
                "/api/"
            ],
            delete: [
                "/api/"
            ]
        }
    },

    f: { // 그리드 펑션
        initialization() { // 가시성을 위해 grid.s 의 일부 요소를 여기서 선언한다.

            /* 0번 그리드의 레이아웃 */
            grid.s.columnLayout[0] = [
                {
                    dataField: "",
                    headerText: "",
                }, {
                    dataField: "",
                    headerText: "",
                },
            ];

            /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
            * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
            * */
            grid.s.prop[0] = {
                editable : false,
                selectionMode : "singleRow",
                noDataMessage : "출력할 데이터가 없습니다.",
                enableColumnResize : false,
                showStateColumn : true,
                enableFilter : true
            };

        },

        create() { // 그리드 동작 처음 빈 그리드를 생성
            for (const i in grid.s.columnLayout) {
                grid.s.id[i] = AUIGrid.create(grid.s.targetDiv[i], grid.s.columnLayout[i], grid.s.prop[i]);
            }
        },

        setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
            ajax.setDataIntoGrid(numOfGrid, grid.s.read[numOfGrid]);
        },
        
        setFavoriteData(num, data) {
	
			window.aaa = data;
			console.log(num);
			console.log(data);
		}
    },

    e: {
        basicEvent() {
            /* 0번그리드 내의 셀 클릭시 이벤트 */
            AUIGrid.bind(gridId[0], "cellClick", function (e) {
                console.log(e.item); // 이밴트 콜백으로 불러와진 객체의, 클릭한 대상 row 키(파라메터)와 값들을 보여준다.
            });
        }
    }
};

const event = {
    s: { // 이벤트 설정
		saveFrInfo() {
			$('#saveFrInfo').on('click', function() {
				saveMyInfo();
			});
		}
    },
    r: { // 이벤트 해제

    }
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
    grid.f.initialization();
    
    ajax.getFrInfo();
    
    event.s.saveFrInfo();

    /* grid.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
    // grid.f.setInitialData(0);

    /* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
    // grid.e.basicEvent();
}

function putFrInfoDataInField(myInfoData) {
	$("input[name='frCode']").val(myInfoData.frCode);
	$("input[name='frName']").val(myInfoData.frName);
	$("input[name='frContractDt']").val(myInfoData.frContractDt);
	$("input[name='frContractFromDt']").val(myInfoData.frContractFromDt);
	$("input[name='frContractToDt']").val(myInfoData.frContractToDt);
	$("input[name='brName']").val(myInfoData.brName);
	$("input[name='brCarculateRateHq']").val(myInfoData.brCarculateRateHq);
	$("input[name='brCarculateRateBr']").val(myInfoData.brCarculateRateBr);
	$("input[name='brCarculateRateFr']").val(myInfoData.brCarculateRateFr);
	$("input[name='frBusinessNo']").val(myInfoData.frBusinessNo);
	$("input[name='frRpreName']").val(myInfoData.frRpreName);
	$("input[name='frTelNo']").val(myInfoData.frTelNo);
	$("input[name='frTagNo']").val(myInfoData.frTagNo);
	$("input[name='frEstimateDuration']").val(myInfoData.frEstimateDuration);
}

function saveMyInfo() {
	
	const formData = new FormData(document.getElementById('frInfo'));
	
	console.log(Object.fromEntries(formData));
	
	ajax.saveMyInfo(formData);
}


/* jest 기본 동작을 테스트 하기 위한 함수, 기본 테스트를 통과하면 아래의 module.exports 부분을 수정하고 이 함수는 지울 것 */
function testForJest() {
    return "hi";
}

/* jest 테스트를 위해 nodejs 의 요소에 테스트가 필요한 기능을 탑재하여 내보내기 한다. 보통의 실행 환경에서는 무시된다. */
try {
    module.exports = {testForJest};
}catch (e) {
    if(!(e instanceof ReferenceError)) {
        console.log(e);
    }
}




// 비밀번호 수정 실행 함수
function passwordUpdate(){
    const $oldpassword = $("#oldpassword");
    if(!$oldpassword.val().length) {
        alertCaution("현재비밀번호를 입력해 주세요", 1);
        return false;
    }

    const $newpassword = $("#newpassword");
    if(!$newpassword.val().length) {
        alertCaution("신규비밀번호를 입력해 주세요", 1);
        return false;
    }

    const $passwordconfirm = $("#passwordconfirm");
    if(!$passwordconfirm.val().length) {
        alertCaution("신규번호확인을 입력해 주세요", 1);
        return false;
    }

    const formData = new FormData();
    formData.append("oldpassword", $oldpassword.val());
    formData.append("newpassword", $newpassword.val());
    formData.append("passwordconfirm", $passwordconfirm.val());

    CommonUI.ajax("/api/user/franchisePassword", "POST", formData, function (req) {
        $oldpassword.val('');
        $newpassword.val('');
        $passwordconfirm.val('');
        alertSuccess("비밀번호 변경 완료");
    });

}

