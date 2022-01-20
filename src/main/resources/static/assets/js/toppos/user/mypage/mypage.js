$(function () { // 페이지가 로드되고 나서 실행
	onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의 */
const dtos = {
	send: {
		franchiseMyInfoSave: {
			frBusinessNo: "s",
			frRpreName: "s",
			frTelNo: "s",
			frTagNo: "sr",
			frEstimateDuration: "nr",
		},
		franchiseAddProcess: {
			repairListData: {
				baSort: "nr",
				baName: "sr",
				baRemark: "s",
				_$uid: "d",
			},
			addAmountData: {
				baSort: "nr",
				baName: "sr",
				baRemark: "s",
				_$uid: "d",
			},
			keyWordData: {
				baSort: "nr",
				baName: "sr",
				baRemark: "s",
				_$uid: "d",
			}
		}
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
		},
		franchiseAddProcessList: {
			repairListData: {
				baSort: "nr",
				baName: "sr",
				baRemark: "s",
			},
			addAmountData: {
				baSort: "nr",
				baName: "sr",
				baRemark: "s",
			},
			keyWordData: {
				baSort: "nr",
				baName: "sr",
				baRemark: "s",
			}
		},
	}
};

/* 통신 객체로 쓰이지 않는 일반적인 데이터들 정의 (warehouse) */
const wares = {
	// 항목 추가 팝업 구분
	itemEditPopNum: 0,
	rowIndex: 0,
}

/* 서버 API를 AJAX 통신으로 호출하며 커뮤니케이션 하는 함수들 */
const comms = {
	setDataIntoGrid(numOfGrid, url) { // 해당 numOfGrid 배열번호의 그리드에 url 로부터 받은 데이터값을 통신하여 주입한다.
		CommonUI.ajax(url, "GET", false, function (req) {
			grids.s.data[numOfGrid] = req.sendData.gridListData;
			AUIGrid.setGridData(grids.s.id[numOfGrid], grids.s.data[numOfGrid]);
		});
	},
	// 가맹점 정보 받기
	getFrInfo() {
		const url = "/api/user/myInfo";
		CommonUI.ajax(url, "GET", false, function (res) {

			const data = res.sendData.franchisInfoDto;
			dv.chk(data, dtos.receive.myInfo, "가맹점 정보 받아오기");
			putFrInfoDataInField(data);
		});
	},
	// 가맹점 정보 저장
	saveMyInfo(formData) {
		const jsonData = Object.fromEntries(formData);
		jsonData.frEstimateDuration = Number(jsonData.frEstimateDuration);
		dv.chk(jsonData, dtos.send.franchiseMyInfoSave, "가맹점 정보 보내기");
		const url = "/api/user/franchiseMyInfoSave";
		CommonUI.ajax(url, "POST", formData, function (res) {
			alertSuccess("가맹점 정보 변경 완료");
		});
	},

	// 상용구, 수선항목, 추가항목 받기
	getFrFavorite(num) {
		CommonUI.ajax(grids.s.url.create[num], "GET", { baType: num }, (res) => {
			console.log(res);
			let data;
			switch (num) {
				case "1":
					data = res.sendData.repairListData;
					dv.chk(data, dtos.receive.franchiseAddProcessList.repairListData, "수선항목 받아오기");
					break;

				case "2":
					data = res.sendData.addAmountData;
					dv.chk(data, dtos.receive.franchiseAddProcessList.addAmountData, "추가항목 받아오기");
					break;

				case "0":
					data = res.sendData.keyWordData;
					dv.chk(data, dtos.receive.franchiseAddProcessList.keyWordData, "상용구 받아오기");
					break;
			}
			grids.f.setFavoriteData(num, data);
		});
	},

	// 상용구, 수선항목, 추가항목 저장
	saveFrFavorite(num, frFavoriteData) {
		switch (num) {
			case "0":
				dv.chk(frFavoriteData, dtos.send.franchiseAddProcess.keyWordData, "상용구 저장");
				break;

			case "1":
				dv.chk(frFavoriteData, dtos.send.franchiseAddProcess.repairListData, "수선항목 저장");
				break;

			case "2":
				dv.chk(frFavoriteData, dtos.send.franchiseAddProcess.addAmountData, "추가항목 저장");
				break;
		};
		const data = {
			list: frFavoriteData
		};
		wares.baType = num;

		CommonUI.ajaxjson(grids.s.url.update[num], JSON.stringify(data), function (res) {
			alertSuccess('저장되었습니다.');
			grids.f.clearData(num);
			comms.getFrFavorite(num);
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
			"grid_keyWord", "grid_repair", "grid_addAmount"
		],
		columnLayout: [],
		prop: [],
		id: [],
		data: [],
		url: {
			create: [
				"/api/user/franchiseAddProcessList",
				"/api/user/franchiseAddProcessList",
				"/api/user/franchiseAddProcessList"
			],
			read: [
				"/api/"
			],
			update: [
				"/api/user/franchiseAddProcess",
				"/api/user/franchiseAddProcess",
				"/api/user/franchiseAddProcess"
			],
			delete: [
				"/api/"
			]
		}
	},

	f: { // 그리드 펑션
		initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

			/* 0번 그리드, 상용구의 레이아웃 */
			grids.s.columnLayout[0] = [
				{
					dataField: "baName",
					headerText: "항목",
				}, {
					dataField: "baRemark",
					headerText: "비고",
				}, {
					dataField: "",
					headerText: "수정",
					renderer: {
						type: "TemplateRenderer",
					},
					labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
						const template = `
							<button class="c-button c-button--solid  c-button--supersmall" 
								onclick="onModifyOrder(0, ${rowIndex})">수정</button>
						`;
						return template;
					},
				}
			];

			// 1번 그리드, 수선항목의 레이아웃
			grids.s.columnLayout[1] = [
				{
					dataField: "baName",
					headerText: "항목",
				}, {
					dataField: "baRemark",
					headerText: "비고",
				}, {
					dataField: "",
					headerText: "수정",
					renderer: {
						type: "TemplateRenderer",
					},
					labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
						const template = `
							<button class="c-button c-button--solid  c-button--supersmall" 
								onclick="onModifyOrder(1, ${rowIndex})">수정</button>
						`;
						return template;
					},
				}
			];

			// 2번 그리드, 추가항목의 레이아웃
			grids.s.columnLayout[2] = [
				{
					dataField: "baName",
					headerText: "항목",
				}, {
					dataField: "baRemark",
					headerText: "비고",
				}, {
					dataField: "",
					headerText: "수정",
					renderer: {
						type: "TemplateRenderer",
					},
					labelFunction: function (rowIndex, columnIndex, value, headerText, item) {
						const template = `
							<button class="c-button c-button--solid  c-button--supersmall" 
								onclick="onModifyOrder(2, ${rowIndex})">수정</button>
						`;
						return template;
					},
				}
			];

			/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
			* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
			* */
			grids.s.prop[0] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				enableColumnResize: false,
				showStateColumn: true,
				enableFilter: true,
				showRowCheckColumn: true,
				rowHeight: 48,
				headerHeight: 48,
			};

			grids.s.prop[1] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				enableColumnResize: false,
				showStateColumn: true,
				enableFilter: true,
				showRowCheckColumn: true,
				rowHeight: 48,
				headerHeight: 48,
			};

			grids.s.prop[2] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				enableColumnResize: false,
				showStateColumn: true,
				enableFilter: true,
				showRowCheckColumn: true,
				rowHeight: 48,
				headerHeight: 48,
			};

		},

		create() { // 그리드 동작 처음 빈 그리드를 생성
			for (const i in grids.s.columnLayout) {
				grids.s.id[i] = AUIGrid.create(grids.s.targetDiv[i], grids.s.columnLayout[i], grids.s.prop[i]);
			}
		},

		setInitialData(numOfGrid) { // 해당 배열 번호 그리드의 url.read 를 참조하여 데이터를 그리드에 뿌린다.
			comms.setDataIntoGrid(numOfGrid, grids.s.read[numOfGrid]);
		},

		setFavoriteData(num, data) {
			AUIGrid.setGridData(grids.s.id[num], data);
		},

		resize(num) {
			AUIGrid.resize(grids.s.id[num]);
		},

		createRow(num, item) {
			// let item = {"항목": "", "비고": ""};
			AUIGrid.addRow(grids.s.id[num], item, "last");
		},

		modifyRow(num, item, rowIndex) {
			AUIGrid.updateRow(grids.s.id[num], item, rowIndex);
		},

		removeRow(num) {
			const items = AUIGrid.getCheckedRowItems(grids.s.id[num]);
			if (items.length) {
				AUIGrid.removeCheckedRows(grids.s.id[num]);
			} else {
				AUIGrid.removeRow(grids.s.id[num], "selectedIndex");
			}
		},

		upRows(num) {
			AUIGrid.moveRowsToUp(grids.s.id[num]);
		},

		downRows(num) {
			AUIGrid.moveRowsToDown(grids.s.id[num]);
		},

		saveGridData(num) {
			AUIGrid.removeSoftRows(grids.s.id[num]);
			const updateGridData = AUIGrid.getGridData(grids.s.id[num]);
			for (let i = 0; updateGridData.length > i; i++) {
				updateGridData[i].baSort = i;
			};
			comms.saveFrFavorite(num, updateGridData);
		},

		clearData(num) {
			AUIGrid.clearGridData(grids.s.id[num]);
		},

		getRowData(num, rowIndex) {
			return AUIGrid.getItemByRowIndex(grids.s.id[num], rowIndex);
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

/* 이벤트를 s : 설정하거나 r : 해지하는 함수들을 담는다. 그리드 관련 이벤트는 grids.e에 위치 (trigger) */
const trigs = {
	s: { // 이벤트 설정
		saveFrInfo() {
			$('#saveFrInfo').on('click', function () {
				saveMyInfo();
			});
		},

		basic() {
			// 탭
			const $tabsBtn = $('.c-tabs__btn');
			const $tabsContent = $('.c-tabs__content');

			$tabsBtn.on('click', function () {
				const idx = $(this).index();
				const num = idx - 2;
				const dataNum = $(this).attr("data-index");

				$tabsBtn.removeClass('active');
				$tabsBtn.eq(idx).addClass('active');
				$tabsContent.removeClass('active');
				$tabsContent.eq(idx).addClass('active');

				if (dataNum) {
					grids.f.resize(num);
					comms.getFrFavorite(dataNum);
				};
			});


			// 팝업 닫기
			$('.pop__close').on('click', function () {
				$(this).parents('.pop').removeClass('active');
			})

			// 라인 추가
			// 팝업 열기
			$('.openItemPop').on('click', function (e) {
				wares.itemEditPopNum = $(this).attr('data-index');
				$('.pop').addClass('active');
				// input값 초기화
				$('#baName').val('');
				$('#baRemark').val('');
				$('#sendItemValue').attr('data-ismodify', '0');
				console.log(wares.itemEditPopNum);
			});
			// input 값 보내기
			$('#sendItemValue').on('click', function () {
				const baName = $('#baName').val();
				const baRemark = $('#baRemark').val();
				const item = { "baName": baName, "baRemark": baRemark };

				if ($(this).attr('data-ismodify') == "0") {
					grids.f.createRow(wares.itemEditPopNum, item);
				} else {
					grids.f.modifyRow(wares.itemEditPopNum, item, wares.rowIndex);
				}
			})

			// 상용구, 수정항목, 추가항목 그리드 저장
			$('.gridSave').on('click', function () {
				const gridnum = $(this).attr('data-gridnum');
				grids.f.saveGridData(gridnum);
			});

			// 라인 삭제
			$('.removeRow').on('click', function () {
				const gridnum = $(this).attr('data-index');
				grids.f.removeRow(gridnum);
			});

			// 선택행 올림
			$('.upRows').on('click', function () {
				const gridnum = $(this).attr('data-index');
				grids.f.upRows(gridnum);
			})

			// 선택행 내림
			$('.downRows').on('click', function () {
				const gridnum = $(this).attr('data-index');
				grids.f.downRows(gridnum);
			})

			// 키보드 

		},

		// 상용구, 수선항목, 추가항목 저장 버튼 클릭
		saveFrFavorite() {

		}
	},
	r: { // 이벤트 해제

	}
}

const vKeyboard = {
	targetId: [
		"frRpreName",
		"frTagNo",
		"oldpassword",
		"newpassword",
		"passwordconfirm",
		"baName",
		"baRemark",
	],
	targetProp: [

	],
	f: {
		initialization() {
			vKeyboard.targetProp[0] = {
				title: "대표자명",
			};
			vKeyboard.targetProp[1] = {
				title: "가맹점 택코드"
			};
			vKeyboard.targetProp[2] = {
				title: "현재 비밀번호"
			};
			vKeyboard.targetProp[3] = {
				title: "신규 비밀번호"
			};
			vKeyboard.targetProp[4] = {
				title: "신규 비밀번호 확인"
			};
			vKeyboard.targetProp[5] = {
				title: "항목"
			};
			vKeyboard.targetProp[6] = {
				title: "비고"
			};
			window.vkey = new VKeyboard();
		},
	},
	e: {
		btnEvent() {
			$('.keyboardBtn').on('click', function () {
				const vKeyNum = $(this).attr('data-keyIndex');
				vkey.showKeyboard(vKeyboard.targetId[vKeyNum],
					vKeyboard.targetProp[vKeyNum]);
			});
		}
	}
}

const vKeypad = {
	targetId: [
		"frBusinessNo",
		"frTelNo",
		"frEstimateDuration",
	],
	targetProp: [

	],
	f: {
		initialization() {
			vKeypad.targetProp[0] = {
				callback: function () {
					const busiNo = $("#frBusinessNo").val();
					$("#frBusinessNo").val(busiNo.replace(/^(\d{3,3})+[-]+(\d{2,2})+[-]+(\d{5,5})/, ""));
				}
			};
			vKeypad.targetProp[1] = {
				callback: function () {
					const telNum = $('#frTelNo').val();
					$('#frTelNo').val(CommonUI.onPhoneNumChange(telNum));
				}
			};
			vKeypad.targetProp[2] = {

			};
		}
	},
	e: {
		btnEvent() {
			$('.keypadBtn').on('click', function () {
				const vKeyNum = $(this).attr('data-keyIndex');
				vkey.showKeypad(vKeypad.targetId[vKeyNum], vKeypad.targetProp[vKeyNum]);
			});
		}
	}
}

/* 페이지가 로드되고 나서 실행 될 코드들을 담는다. */
function onPageLoad() {
	grids.f.initialization();
	grids.f.create();

	vKeyboard.f.initialization();
	vKeyboard.e.btnEvent();
	vKeypad.f.initialization();
	vKeypad.e.btnEvent();

	comms.getFrInfo();

	trigs.s.saveFrInfo();
	trigs.s.basic();

	/* grids.s 에 적절한 값을 세팅하였다면, 해당 함수 호출시 해당 배열번호의 그리드에 의도한 데이터들이 주입되어진다. */
	// grids.f.setInitialData(0);

	/* 생성된 그리드에 기본적으로 필요한 이벤트들을 적용한다. */
	// grids.e.basicEvent();
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

	comms.saveMyInfo(formData);
}

function onModifyOrder(gridNum, rowIndex) {
	wares.itemEditPopNum = gridNum;
	wares.rowIndex = rowIndex;

	const rowData = grids.f.getRowData(gridNum, rowIndex);
	$('.pop').addClass('active');
	// input값 초기화
	$('#baName').val(rowData.baName);
	$('#baRemark').val(rowData.baRemark);
	$('#sendItemValue').attr('data-ismodify', '1');
}

// 비밀번호 수정 실행 함수
function passwordUpdate() {
	const $oldpassword = $("#oldpassword");
	if (!$oldpassword.val().length) {
		alertCaution("현재비밀번호를 입력해 주세요", 1);
		return false;
	}

	const $newpassword = $("#newpassword");
	if (!$newpassword.val().length) {
		alertCaution("신규비밀번호를 입력해 주세요", 1);
		return false;
	}

	const $passwordconfirm = $("#passwordconfirm");
	if (!$passwordconfirm.val().length) {
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

