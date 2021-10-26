package com.broadwave.toppos.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : Toppos 공용 util static 함수
 */
@Slf4j
public abstract class CommonUtils {

    //IP를 가져오는 함수 -> 프록시서버 등 앞단에 다른 네트웍이 존재할경우 원래의 IP를 가져오기 위함
    public static String getIp(HttpServletRequest request) {
        log.info("접속 유저 IP를 가져오는 공통 함수 호출");

        String ip = request.getHeader("X-Forwarded-For");

        log.info(">>>> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        log.info(">>>> Result : IP Address : "+ip);

        return ip;

    }

    public static String getCurrentuser(HttpServletRequest request){
        log.info("현재 접속 유저 정보 가져오기 공통 함수 호출");

//        HttpSession session = request.getSession();
//        String currentuserid = (String) session.getAttribute("userid");
        String currentuserid = "system";
        String a;
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {
            currentuserid = userPrincipal.getName();
            if (currentuserid == null || currentuserid.equals("")) {
                currentuserid = "system";
            }
        }
        return currentuserid;

    }

    public static String GetZeroToNullString(String str){
        log.info("문자열을받아 0.0 0.00 이면 null 문자를 반환하는 공통 함수 호출");
        if (str.equals("0.0") || str.equals("0.00")){
            return "null";
        }else{
            return str;
        }

    }
    //문자열이 Null 이면 빈문자열 반환하는 함수
    public static String GetNulltoString(String str){
        if (str == null) {
            return "";
        }
        return str;
    }

    //Double Null 이면 빈문자열 반환하는 함수
    public static Double GetNulltoDouble(Double num){
        if (num == null) {
            return 0d;
        }
        return num;
    }
}
