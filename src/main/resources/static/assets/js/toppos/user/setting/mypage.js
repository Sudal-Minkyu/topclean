$(function () { // 페이지가 로드되고 나서 실행
	onPageLoad();
});

/* 서버 API와 주고 받게 될 데이터 정의 */
const dtos = {
	send: {
		franchiseMyInfoSave: {
			frOpenWeekday: "s",
			frOpenSaturday: "s",
			frOpenHoliday: "s",
			frCloseWeekday: "s",
			frCloseSaturday: "s",
			frCloseHoliday: "s",
			frStatWeekday: "s",
			frStatSaturday: "s",
			frStatHoliday: "s",
			frDepositAmount: "n", // 2022.03.29 추가
			frRentalAmount: "n", // 2022.03.29 추가
			frRpreName: "s",
			frTelNo: "s",
			frPostNo: "s",
			frAddress: "s",
			frAddressDetail: "s",
			frMultiscreenYn : "s",
			frRemark : "s",
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
			frOpenWeekday: "s",
			frOpenSaturday: "s",
			frOpenHoliday: "s",
			frCloseWeekday: "s",
			frCloseSaturday: "s",
			frCloseHoliday: "s",
			frStatWeekday: "s",
			frStatSaturday: "s",
			frStatHoliday: "s",
			frDepositAmount: "n", // 2022.03.29 추가
			frRentalAmount: "n", // 2022.03.29 추가
			frCode: "sr",
			frName: "sr",
			frContractDt: "sr",
			frContractFromDt: "sr",
			frContractToDt: "sr",
			brName: "sr",
			frCarculateRateBr: "n",
			frCarculateRateFr: "n",
			frRoyaltyRateBr: "n",
			frRoyaltyRateFr: "n",
			frBusinessNo: "s",
			frRpreName: "s",
			frTelNo: "s",
			frTagNo: "sr",
			frEstimateDuration: "nr",
			frPostNo: "s",
			frAddress: "s",
			frAddressDetail: "s",
			frMultiscreenYn: "s",
			frRemark : "s",
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
			console.log(data);
			putFrInfoDataInField(data);
		});
	},
	// 가맹점 정보 저장
	saveMyInfo(formData) {
		formData.set("frOpenWeekday", $("#frOpenWeekdayHour").val() + $("#frOpenWeekdayMinute").val());
		formData.set("frCloseWeekday", $("#frCloseWeekdayHour").val() + $("#frCloseWeekdayMinute").val());
		formData.set("frOpenSaturday", $("#frOpenSaturdayHour").val() + $("#frOpenSaturdayMinute").val());
		formData.set("frCloseSaturday", $("#frCloseSaturdayHour").val() + $("#frCloseSaturdayMinute").val());
		formData.set("frOpenHoliday", $("#frOpenHolidayHour").val() + $("#frOpenHolidayMinute").val());
		formData.set("frCloseHoliday", $("#frCloseHolidayHour").val() + $("#frCloseHolidayMinute").val());
		formData.set("frTelNo", formData.get("frTelNo").numString());
		formData.set("frMultiscreenYn", $('input[name=frMultiscreenYn]:checked').val());

		const jsonData = Object.fromEntries(formData);
		jsonData.frDepositAmount = jsonData.frDepositAmount.toInt();
		jsonData.frRentalAmount = jsonData.frRentalAmount.toInt();
		formData.set("frDepositAmount", jsonData.frDepositAmount);
		formData.set("frRentalAmount", jsonData.frRentalAmount);

		dv.chk(jsonData, dtos.send.franchiseMyInfoSave, "가맹점 정보 보내기");
		console.log(jsonData);
		const url = "/api/user/franchiseMyInfoSave";
		CommonUI.ajax(url, "POST", formData, function (res) {
			alertSuccess("가맹점 정보 변경 완료");
		});
	},

	// 상용구, 수선항목, 추가항목 받기
	getFrFavorite(num) {
		CommonUI.ajax('/api/user/franchiseAddProcessList', "GET", { baType: num }, (res) => {
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
		}

		const data = {
			list: frFavoriteData
		};
		data.baType = num;

		CommonUI.ajax('/api/user/franchiseAddProcess', "MAPPER", data, function (res) {
			alertSuccess('저장되었습니다.');
			grids.f.clearData(num);
			comms.getFrFavorite(num);
		});
	},

	entryPass(password) {
		const url = "/api/user/franchiseCheck";
		CommonUI.ajax(url, "GET", password, function (res) {
			comms.getFrInfo();
			$(".password").hide();
		});
	},

	getReadyCashList(searchCondition) {
		wares.searchCondition = searchCondition;
		console.log('준비금검색', searchCondition);
		CommonUI.ajax("/api/user/franchiseReadyCashList", "GET", searchCondition, function (res) {
			const data = res.sendData.gridListData;
			AUIGrid.setGridData(grids.s.id[3], data);
		});
	},

	/* 영업준비금을 저장하고 영업준비금 그리드를 다시 로딩하기 */
	saveReadyCash(saveData) {
		CommonUI.ajax("/api/user/franchiseReadyCashSave", "PARAM", saveData, function () {
			$("#bcYyyymmdd").val(new Date().format("yyyy-MM-dd"));
			$("#bcReadyAmt").val("");
			let searchCondition;
			if (wares.searchCondition) {
				searchCondition = wares.searchCondition
			} else {
				searchCondition = {
					filterFromDt: saveData.bcYyyymmdd,
					filterToDt: saveData.bcYyyymmdd,
				}
			}
			comms.getReadyCashList(searchCondition);
			alertSuccess("영업준비금이 저장 되었습니다.");
		});
	}
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
			"grid_keyWord", "grid_repair", "grid_addAmount", "grid_readyCash"
		],
		columnLayout: [],
		prop: [],
		id: [],
		data: [],
	},

	f: { // 그리드 펑션
		initialization() { // 가시성을 위해 grids.s 의 일부 요소를 여기서 선언한다.

			/* 0번 그리드, 상용구의 레이아웃 */
			grids.s.columnLayout[0] = [
				{
					dataField: "baName",
					headerText: "항목",
                    style: "grid_textalign_left",
				}, {
					dataField: "baRemark",
					headerText: "비고",
                    style: "grid_textalign_left",
				}, {
					dataField: "",
					headerText: "수정",
					renderer: {
						type: "TemplateRenderer",
					},
					labelFunction(rowIndex, columnIndex, value, headerText, item) {
						return `
							<button class="c-button c-button--solid  c-button--supersmall" 
								onclick="onModifyOrder(0, ${rowIndex})">수정</button>
						`;
					},
				}
			];

			// 1번 그리드, 수선항목의 레이아웃
			grids.s.columnLayout[1] = [
				{
					dataField: "baName",
					headerText: "항목",
                    style: "grid_textalign_left",
				}, {
					dataField: "baRemark",
					headerText: "비고",
                    style: "grid_textalign_left",
				}, {
					dataField: "",
					headerText: "수정",
					renderer: {
						type: "TemplateRenderer",
					},
					labelFunction(rowIndex, columnIndex, value, headerText, item) {
						return `
							<button class="c-button c-button--solid  c-button--supersmall" 
								onclick="onModifyOrder(1, ${rowIndex})">수정</button>
						`;
					},
				}
			];

			// 2번 그리드, 추가항목의 레이아웃
			grids.s.columnLayout[2] = [
				{
					dataField: "baName",
					headerText: "항목",
                    style: "grid_textalign_left",
				}, {
					dataField: "baRemark",
					headerText: "비고",
                    style: "grid_textalign_left",
				}, {
					dataField: "",
					headerText: "수정",
					renderer: {
						type: "TemplateRenderer",
					},
					labelFunction(rowIndex, columnIndex, value, headerText, item) {
						return `
							<button class="c-button c-button--solid  c-button--supersmall" 
								onclick="onModifyOrder(2, ${rowIndex})">수정</button>
						`;
					},
				}
			];

			// 3번 그리드, 준비금 일자와 준비금액
			grids.s.columnLayout[3] = [
				{
					dataField: "bcYyyymmdd",
					headerText: "등록일자",
					dataType: "date",
					formatString: "yyyy-mm-dd",
				}, {
					dataField: "bcReadyAmt",
					headerText: "준비금",
					style: "grid_textalign_right",
					dataType: "numeric",
					autoThousandSeparator: "true",
				},
			];

			/* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
			* https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
			* */
			grids.s.prop[0] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				rowNumHeaderText : "순번",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : true,
                showStateColumn : false,
                enableFilter : false,
				rowHeight: 48,
				headerHeight: 48,
			};

			grids.s.prop[1] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				rowNumHeaderText : "순번",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : true,
                showStateColumn : false,
                enableFilter : false,
				rowHeight: 48,
				headerHeight: 48,
			};

			grids.s.prop[2] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				rowNumHeaderText : "순번",
                showAutoNoDataMessage: false,
                enableColumnResize : false,
                showRowAllCheckBox: true,
                showRowCheckColumn: true,
                showRowNumColumn : true,
                showStateColumn : false,
                enableFilter : false,
				rowHeight: 48,
				headerHeight: 48,
			};

			grids.s.prop[3] = {
				editable: true,
				selectionMode: "singleRow",
				noDataMessage: "출력할 데이터가 없습니다.",
				rowNumHeaderText : "순번",
				showAutoNoDataMessage: false,
				enableColumnResize : false,
				showRowAllCheckBox: false,
				showRowCheckColumn: false,
				showRowNumColumn : false,
				showStateColumn : false,
				enableFilter : false,
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
			}
			comms.saveFrFavorite(num, updateGridData);
		},

		clearData(num) {
			AUIGrid.clearGridData(grids.s.id[num]);
		},

		getRowData(num, rowIndex) {
			return AUIGrid.getItemByRowIndex(grids.s.id[num], rowIndex);
		}
	},
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
				}
			});


			// 팝업 닫기
			$('.pop__close').on('click', function () {
				$(this).parents('.pop').removeClass('active');
			})

			// 라인 추가
			// 팝업 열기
			$('.openItemPop').on('click', function (e) {
				wares.itemEditPopNum = $(this).attr('data-index');
				$('#linePop').addClass('active');
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
			});

			$('#frTelNo').on('keyup', function () {
				const telNum = $('#frTelNo').val();
				$('#frTelNo').val(CommonUI.formatTel(telNum));
			});

			$("#frDepositAmount").on("keyup", function () {
                $(this).val($(this).val().positiveNumberInput());
            });

			$("#frRentalAmount").on("keyup", function () {
                $(this).val($(this).val().positiveNumberInput());
            });

			$("#searchListBtn").on("click", function () {
				searchReadyCashList();
			});

			$("#newReadyAmt").on("click", function () {
				setInputsForNewReadyCash();
			});

			$("#saveReadyAmt").on("click", function () {
				saveReadyCash();
			});

			$("#bcReadyAmt").on("keyup", function () {
				$(this).val($(this).val().positiveNumberInput());
			});

			AUIGrid.bind(grids.s.id[3], "cellClick", function (e) {
				setReadyCashInputs(e.item);
			});
		},

		entryPass() {
			$("#entryPassword").on("keyup", function (e) {
                if(e.originalEvent.code === "Enter" || e.originalEvent.code === "NumpadEnter") {
					const password = {
						password: $("#entryPassword").val(),
					}
					comms.entryPass(password);
                }
			});

			$("#entryPasswordKeyboard").on("click", function () {
				vkey.showKeyboard("entryPassword", {title: "계정 비밀번호 입력", callback: sendPassword});
			});

			$("#entryPasswordConfirm").on("click", function () {
				sendPassword();
			});
		},
	},
	r: { // 이벤트 해제

	}
}

const vKeyboard = {
	targetId: [
		"frRpreName",
		"frTagNo", // 현재 미사용
		"oldpassword",
		"newpassword",
		"passwordconfirm",
		"baName",
		"baRemark",
		"frAddressDetail",
		"frRemark",
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
			vKeyboard.targetProp[7] = {
				title: "상세주소"
			};
			vKeyboard.targetProp[8] = {
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
		"frBusinessNo", // 현재 미사용
		"frTelNo",
		"frEstimateDuration", // 현재 미사용
		"frDepositAmount",
		"frRentalAmount",
		"bcReadyAmt",
	],
	targetProp: [

	],
	f: {
		initialization() {
			vKeypad.targetProp[0] = {
				callback: function () {
					const busiNo = $("#frBusinessNo").val();
					$("#frBusinessNo").val(CommonUI.formatBusinessNo(busiNo));
				},
				midprocess: "business",
				maxlength: 10,
			};
			vKeypad.targetProp[1] = {
				callback: function () {
					const telNum = $('#frTelNo').val();
					$('#frTelNo').val(CommonUI.formatTel(telNum));
				},
				midprocess: "tel",
			};
			vKeypad.targetProp[2] = {
			};
			vKeypad.targetProp[3] = {
				callback: function () {
					$("#frDepositAmount").val(parseInt($("#frDepositAmount").val()).toLocaleString());
				},
			};
			vKeypad.targetProp[4] = {
				callback: function () {
					$("#frRentalAmount").val(parseInt($("#frRentalAmount").val()).toLocaleString());
				},
			};
			vKeypad.targetProp[5] = {
				callback: function () {
					$("#bcReadyAmt").val(parseInt($("#bcReadyAmt").val()).toLocaleString());
				},
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
	setHourMinuteSelectBox();
	grids.f.initialization();
	grids.f.create();

	vKeyboard.f.initialization();
	vKeyboard.e.btnEvent();
	vKeypad.f.initialization();
	vKeypad.e.btnEvent();

	trigs.s.saveFrInfo();
	trigs.s.basic();
	trigs.s.entryPass();

	enableDatepicker();
}


/* 평일, 토요일, 공휴일 영업시간 관련 셀렉트박스에 시간 넣기 */
function setHourMinuteSelectBox() {
	const hourSelectBox = $(".hourSelector");
	const minuteSelectBox = $(".minuteSelector");

	for (let i = 0; i <= 24; i++) {
		const formattedHour = i.toString().padStart(2, '0');
		hourSelectBox.append(`
            <option value="${formattedHour}">${formattedHour} 시</option>
        `);
	}

	minuteSelectBox.append(`
        <option value="00">00 분</option>
        <option value="30">30 분</option>
    `);

}

function putFrInfoDataInField(myInfoData) {
	$("input[name='frCode']").val(myInfoData.frCode);
	$("input[name='frName']").val(myInfoData.frName);
	$("input[name='frContractDt']").val(myInfoData.frContractDt);
	$("input[name='frContractFromDt']").val(myInfoData.frContractFromDt);
	$("input[name='frContractToDt']").val(myInfoData.frContractToDt);
	$("input[name='brName']").val(myInfoData.brName);
	$("input[name='frCarculateRateBr']").val(myInfoData.frCarculateRateBr);
	$("input[name='frCarculateRateFr']").val(myInfoData.frCarculateRateFr);
	$("input[name='frRoyaltyRateFr']").val(myInfoData.frRoyaltyRateFr);
	$("#frBusinessNo").val(CommonUI.formatBusinessNo(myInfoData.frBusinessNo));
	$("input[name='frRpreName']").val(myInfoData.frRpreName);
	$("input[name='frTelNo']").val(CommonUI.formatTel(myInfoData.frTelNo));
	$("#frTagNo").val(myInfoData.frTagNo);
	$("#frEstimateDurationMinus1").val(myInfoData.frEstimateDuration < 2
		? "" : myInfoData.frEstimateDuration - 1);
	$("#frEstimateDuration").val(myInfoData.frEstimateDuration);
	$("input[name='frPostNo']").val(myInfoData.frPostNo);
	$("input[name='frAddress']").val(myInfoData.frAddress);
	$("input[name='frAddressDetail']").val(myInfoData.frAddressDetail);
	$("textarea[name='frRemark']").val(myInfoData.frRemark);

	$("#frStatWeekday").val(myInfoData.frStatWeekday || "0");
	$("#frOpenWeekdayHour").val(myInfoData.frOpenWeekday.substring(0, 2) || "00");
	$("#frOpenWeekdayMinute").val(myInfoData.frOpenWeekday.substring(2, 4) || "00");
	$("#frCloseWeekdayHour").val(myInfoData.frCloseWeekday.substring(0, 2) || "00");
	$("#frCloseWeekdayMinute").val(myInfoData.frCloseWeekday.substring(2, 4) || "00");
	$("#frStatSaturday").val(myInfoData.frStatSaturday || "0");
	$("#frOpenSaturdayHour").val(myInfoData.frOpenSaturday.substring(0, 2) || "00");
	$("#frOpenSaturdayMinute").val(myInfoData.frOpenSaturday.substring(2, 4) || "00");
	$("#frCloseSaturdayHour").val(myInfoData.frCloseSaturday.substring(0, 2) || "00");
	$("#frCloseSaturdayMinute").val(myInfoData.frCloseSaturday.substring(2, 4) || "00");
	$("#frStatHoliday").val(myInfoData.frStatHoliday || "0");
	$("#frOpenHolidayHour").val(myInfoData.frOpenHoliday.substring(0, 2) || "00");
	$("#frOpenHolidayMinute").val(myInfoData.frOpenHoliday.substring(2, 4) || "00");
	$("#frCloseHolidayHour").val(myInfoData.frCloseHoliday.substring(0, 2) || "00");
	$("#frCloseHolidayMinute").val(myInfoData.frCloseHoliday.substring(2, 4) || "00");

	let frDepositAmount = "";
	if(myInfoData.frDepositAmount) {
		frDepositAmount = myInfoData.frDepositAmount.toLocaleString();
	}
	let frRentalAmount = "";
	if(myInfoData.frRentalAmount) {
		frRentalAmount = myInfoData.frRentalAmount.toLocaleString();
	}
	$("input[name='frDepositAmount']").val(frDepositAmount);
	$("input[name='frRentalAmount']").val(frRentalAmount);
	if(myInfoData.frMultiscreenYn === "Y"){
		$("#frMultiscreenYn_Y").prop("checked",true)
	}else{
		$("#frMultiscreenYn_N").prop("checked",true)
	}
}

function saveMyInfo() {
	const formData = new FormData(document.getElementById('frInfo'));

	comms.saveMyInfo(formData);
}

function onModifyOrder(gridNum, rowIndex) {
	wares.itemEditPopNum = gridNum;
	wares.rowIndex = rowIndex;

	const rowData = grids.f.getRowData(gridNum, rowIndex);
	$('#linePop').addClass('active');
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

// 우편번호 검색
function execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			let addr = ''; // 주소 변수

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
				addr = data.roadAddress;
			} else { // 사용자가 지번 주소를 선택했을 경우(J)
				addr = data.jibunAddress;
			}

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById('frPostNo').value = data.zonecode;
			document.getElementById("frAddress").value = addr;
			// 커서를 상세주소 필드로 이동한다.
			document.getElementById("frAddressDetail").focus();
		}
	}).open();
}

function printingReport() {
	const _params = {
		projectName: "toppos",
		formName: "qrprint02",
		name: $("input[name='frName']").val(),
		url: location.protocol+"//"+location.host+"/mobile/unAuth/qrpickup?frcode=" + $("#frCode").val(),
	};

	const _url = "https://report.topcleaners.kr:443/UBIFORM/UView5/index.jsp";
	const fullURL = _url + "?projectName=" + _params.projectName + "&formName=" + _params.formName
		+ "&name=" + _params.name + "&url=" + _params.url;
	console.log(_params);
	$("#qrcode").attr("src", fullURL);
	$("#qrPop").addClass("active");
}

/* 진입시 입력한 암호를 전달한다. */
function sendPassword() {
	const password = {
		password: $("#entryPassword").val(),
	}
	comms.entryPass(password);
}

function enableDatepicker() {
	let fromday = new Date();
	fromday.setDate(fromday.getDate() - 6);
	fromday = fromday.format("yyyy-MM-dd");
	const today = new Date().format("yyyy-MM-dd");

	/* datepicker를 적용시킬 대상들의 dom id들 */
	const datePickerTargetIds = [
		"filterFromDt", "filterToDt", "bcYyyymmdd"
	];

	$("#" + datePickerTargetIds[0]).val(fromday);
	$("#" + datePickerTargetIds[1]).val(today);
	$("#" + datePickerTargetIds[2]).val(today);

	const dateAToBTargetIds = [
		["filterFromDt", "filterToDt"]
	];

	CommonUI.setDatePicker(datePickerTargetIds);
	CommonUI.restrictDateAToB(dateAToBTargetIds);
}

function searchReadyCashList() {
	const searchCondition = {
		filterFromDt: $("#filterFromDt").val().numString(),
		filterToDt: $("#filterToDt").val().numString(),
	};

	comms.getReadyCashList(searchCondition);
}

function setReadyCashInputs(item) {
	$("#bcYyyymmdd").val(item.bcYyyymmdd);
	$("#bcReadyAmt").val(item.bcReadyAmt.toLocaleString());
}

function setInputsForNewReadyCash() {
	$("#bcYyyymmdd").val(new Date().format("yyyy-MM-dd"));
	$("#bcReadyAmt").val("");
}

function saveReadyCash() {
	const bcYyyymmdd = $("#bcYyyymmdd").val().numString();
	const bcReadyAmt = $("#bcReadyAmt").val().toInt();

	if (!CommonUI.regularValidator(bcYyyymmdd, "dateExist")) {
		alertCaution("올바른 날짜를 입력하여 주세요.", 1);
		return;
	}

	if($("#bcReadyAmt").val() === "" || isNaN(bcReadyAmt)) {
		alertCaution("올바른 영업준비금액을 입력해 주세요.", 1);
		return;
	}

	const saveData = {
		bcYyyymmdd: bcYyyymmdd,
		bcReadyAmt: bcReadyAmt,
	}
	comms.saveReadyCash(saveData);
}