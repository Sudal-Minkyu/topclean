package com.broadwave.toppos.common;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 에러 응답코드
 */
public enum ResponseErrorCode {
    TP001("TP001", "이미 존재하는 아이디입니다."),
    TP002("TP002", "아이디와 비밀번호를 확인해주세요."),
    TP003("TP003", "중복되는 코드가 존재합니다."),
    TP004("TP004", "아이디를 입력해주세요."),
    TP005("TP005", "정보가 존재하지 않습니다."),
    TP006("TP006", "새로고침이후에 다시 등록해주세요."),
    TP007("TP007", "권한이 없습니다."),
    TP008("TP008", "가맹점 전용기능 입니다."),
    TP009("TP009", "코드가 존재하지않습니다."),
    TP010("TP010", "할 년도를 입력해주세요."),
    TP011("TP011", "사용중인 데이터가 존재합니다."),
    TP012("TP012", "엑셀파일만 업로드 해주세요."),
    TP013("TP013", "양식으로 제공한 엑셀파일만 등록해주세요."),
    TP014("TP014", "이미 등록되어있는"),
    TP015("TP015", "중복되는 적용일자가 존재합니다."),
    TP016("TP016", "금액에 숫자만 입력해주세요."),
//    TP017("TP017", "이미 등록되어있는 상품코드입니다."),
    TP018("TP018", "등록되어있지 않은 고객입니다."),
    TP019("TP019", "결제를 실패했습니다."),
    TP020("TP020", "비밀번호가 일치하지 않습니다."),
    TP021("TP021", "신규 입력한 비밀번호가 일치하지 않습니다."),
    TP022("TP022", "데이터가 존재하지 않습니다."),
    TP023("TP023", "수정전 금액보다 낮아질 수 없습니다."),
    TP024("TP024", "접수정보가 존재하지 않습니다."),
    TP025("TP025", "이미 미수완납된 결제입니다."),
    TP026("TP026", "권한이 존재하지 않습니다."),
    TP027("TP027", "새로고침이후 다시 시도해주세요."),
    TP028("TP028", "대상이 아닙니다."),
    TP029("TP029", "상품을 선택해주세요."),
    TP030("TP030", "존재하지 않습니다."),
    TP031("TP031", "실패했습니다."),
    TP032("TP032", "확인품 등록을 할 수 없습니다.."),
    ;

    private String code;
    private String desc;

    ResponseErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
