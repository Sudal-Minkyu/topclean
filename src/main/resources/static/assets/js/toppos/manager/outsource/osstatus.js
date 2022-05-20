import {grids, runOnlyOnce} from '../../module/m_outsourcing.js';

/* 서버 API와 주고 받게 될 데이터 정의
* "s" 문자형, "n" 숫자형, "a" 배열형, "r" 필수값, "d" 불필요한 데이터 삭제(receive에 있을 경우 앞으로도 불필요할 경우에는 API에서 삭제요청할것)
* 조합하여 "sr", "nr" 같은 형식도 가능
* 추가로 필요한 검사항목이 생긴다면 문의 바랍니다.
* */
const dtos = {

}

const gridElemets = {
    id: ['grid_main'],
    columnLayout: [],
    prop: [],

    initialization() {
        /* 0번 그리드의 레이아웃 */
        gridElemets.columnLayout[0] = [
            {
                dataField: 'frName',
                headerText: '가맹점명',
                style: 'grid_textalign_left',
            }, {
                dataField: 'fdS2Dt',
                headerText: '지사입고일',
                width: 90,
                dataType: 'date',
                formatString: 'yyyy-mm-dd',
            }, {
                dataField: 'fdUrgentYn',
                headerText: '급세탁',
                width: 55,
            }, {
                dataField: 'fdTag',
                headerText: '택번호',
                style: 'datafield_tag',
                width: 80,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.formatBrTagNo(value);
                },
            }, {
                dataField: 'productName',
                headerText: '상품명',
                style: 'grid_textalign_left',
                width: 200,
                renderer : {
                    type : 'TemplateRenderer',
                },
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    const colorSquare =
                        `<span class='colorSquare' style='background-color: ${CommonData.name.fdColorCode[item.fdColor]}; vertical-align: middle;'></span>`;
                    const sumName = CommonUI.toppos.makeSimpleProductName(item);
                    item.productName = sumName;
                    return colorSquare + ` <span style='vertical-align: middle;'>` + sumName + `</span>`;
                },
            }, {
                dataField: 'processName',
                headerText: '처리내역',
                style: 'grid_textalign_left',
                width: 130,
                labelFunction(_rowIndex, _columnIndex, _value, _headerText, item) {
                    item.processName = CommonUI.toppos.processName(item);
                    return item.processName;
                }
            }, {
                dataField: 'bcName',
                headerText: '고객',
                width: 100,
            }, {
                dataField: 'fdTotAmt',
                headerText: '접수금액',
                style: 'grid_textalign_right',
                width: 90,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'bpOutsourcingPrice',
                headerText: '외주금액',
                style: 'grid_textalign_right',
                width: 90,
                dataType: 'numeric',
                autoThousandSeparator: 'true',
            }, {
                dataField: 'fdState',
                headerText: '현재상태',
                width: 90,
                labelFunction(_rowIndex, _columnIndex, value, _headerText, _item) {
                    return CommonData.name.fdState[value];
                },
            },
        ];

        /* 0번 그리드의 프로퍼티(옵션) 아래의 링크를 참조
        * https://www.auisoft.net/documentation/auigrid/DataGrid/Properties.html
        * */
        gridElemets.prop[0] = {
            editable : false,
            selectionMode : 'singleRow',
            noDataMessage : '존재하는 데이터가 없습니다.',
            showAutoNoDataMessage: false,
            enableColumnResize : true,
            showRowAllCheckBox: false,
            showRowCheckColumn: true,
            showRowNumColumn : false,
            showStateColumn : false,
            enableFilter : true,
            rowHeight : 48,
            headerHeight : 48,
        };
    },
}

$(function() {
    onPageLoad();
});

const onPageLoad = function () {
    runOnlyOnce.enableDatepicker();
    runOnlyOnce.getFrList();
};