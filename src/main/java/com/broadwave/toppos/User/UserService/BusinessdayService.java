package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentBusinessdayListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.RequestDetailBusinessdayDeliveryDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.RequestDetailBusinessdayListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouseRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestBusinessdayCustomerListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestBusinessdayListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyBusinessdayListDto;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-01-25
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 서비스
 */
@Slf4j
@Service
public class BusinessdayService {

    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    private final InhouseRepository inhouseRepository;
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final RequestRepository requestRepository;
    private final RequestRepositoryCustom requestRepositoryCustom;

    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    private final PaymentRepositoryCustom paymentRepositoryCustom;
    private final SaveMoneyRepositoryCustom saveMoneyRepositoryCustom;

    @Autowired
    public BusinessdayService(ModelMapper modelMapper, TokenProvider tokenProvider,
                              InhouseRepository inhouseRepository, InspeotRepositoryCustom inspeotRepositoryCustom,
                              RequestRepository requestRepository, RequestRepositoryCustom requestRepositoryCustom,
                              RequestDetailRepositoryCustom requestDetailRepositoryCustom, PaymentRepositoryCustom paymentRepositoryCustom, SaveMoneyRepositoryCustom saveMoneyRepositoryCustom){
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.inhouseRepository = inhouseRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.requestRepository = requestRepository;
        this.requestRepositoryCustom = requestRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
        this.paymentRepositoryCustom = paymentRepositoryCustom;
        this.saveMoneyRepositoryCustom = saveMoneyRepositoryCustom;
    }

    //  일일영업일보 - 리스트호출 테이블
    public ResponseEntity<Map<String, Object>> businessdayList(String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("businessdayList 호출");

        log.info("조회 시작날짜 : "+filterFromDt);
        log.info("조회 마지막날짜 : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 접수 총건수, 접수금액 통계
        List<RequestBusinessdayListDto> requestBusinessdayListDtos = requestRepositoryCustom.findByBusinessDayList(frCode, filterFromDt, filterToDt);

        // 접수세부(부착물, 재세탁) 통계
        List<RequestDetailBusinessdayListDto> requestDetailBusinessSumDtos = requestDetailRepositoryCustom.findByRequestDetailBusinessdayList(frCode, filterFromDt,  filterToDt);

        // 결제 통계
        List<PaymentBusinessdayListDto> paymentBusinessdayListDtos = paymentRepositoryCustom.findByPaymentBusinessdayListDto(frCode, filterFromDt,  filterToDt);

        // 적림금사옹 통계
        List<SaveMoneyBusinessdayListDto> saveMoneyBusinessdayListDtos = saveMoneyRepositoryCustom.findBySaveMoneyBusinessdayListDto(frCode, filterFromDt,  filterToDt);

        // 방문고객 접수 통계
        List<RequestBusinessdayCustomerListDto> requestBusinessdayCustomerListDtos = requestRepositoryCustom.findByBusinessDayCustomerList(frCode, filterFromDt,  filterToDt);

        // 방문고객 출고 통계
        List<RequestDetailBusinessdayDeliveryDto> requestDetailBusinessdayDeliveryDtos = requestDetailRepositoryCustom.findByRequestDetailBusinessdayDeliveryList(frCode, filterFromDt,  filterToDt);

        data.put("접수 관련",requestBusinessdayListDtos);
        data.put("접수세부 관련",requestDetailBusinessSumDtos);
        data.put("결제 관련",paymentBusinessdayListDtos);
        data.put("적립금 관련",saveMoneyBusinessdayListDtos);
        data.put("방문고객 접수관련",requestBusinessdayCustomerListDtos);
        data.put("방문고객 출고관련",requestDetailBusinessdayDeliveryDtos);

        List<HashMap<String,Object>> businessdayListData = new ArrayList<>();
        HashMap<String,Object> businessdayListInfo;
//        for (RequestBusinessdayListDto requestBusinessdayListDto: requestBusinessdayListDtos) {
//            businessdayListInfo = new HashMap<>();
//            businessdayListInfo.put("yyyymmdd", requestBusinessdayListDto.getYyyymmdd());
//            businessdayListInfo.put("frQtyAll", requestBusinessdayListDto.getFrQtyAll());
//            businessdayListInfo.put("frTotalAmountAll", requestBusinessdayListDto.getFrTotalAmountAll());
//            businessdayListInfo.put("totalAverageAll", requestBusinessdayListDto.getFrTotalAmountAll()/requestBusinessdayListDto.getFrQtyAll());
//            businessdayListData.add(businessdayListInfo);
//        }

        return ResponseEntity.ok(res.dataSendSuccess(data));

    }







}
