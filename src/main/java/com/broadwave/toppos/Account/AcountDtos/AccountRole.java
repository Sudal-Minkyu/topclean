package com.broadwave.toppos.Account.AcountDtos;

/**
 * @author Minkyu
 * Date : 2021-10-08
 * Time :
 * Remark : 사용자 권한 구분
 */
public enum AccountRole {

    ROLE_USER("ROLE_USER", "가맹점주"),

    ROLE_MANAGER("ROLE_MANAGER", "지사장"),
    ROLE_NORMAL("ROLE_NORMAL", "지사일반"),

    ROLE_CALCULATE("ROLE_CALCULATE", "회계관리"),
    ROLE_HEAD("ROLE_HEAD", "본사일반"),

    ROLE_ADMIN("ROLE_ADMIN", "시스템관리자");

    private final String code;
    private final String desc;

    AccountRole(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}


