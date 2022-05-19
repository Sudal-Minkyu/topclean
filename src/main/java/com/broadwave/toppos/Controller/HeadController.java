package com.broadwave.toppos.Controller;

import com.broadwave.toppos.Account.AcountDtos.AccountRole;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupNameListDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.ItemPriceSetDtDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-05
 * Time :
 * Remark : Toppos 본사 또는 회계관리 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/head")
public class HeadController {

    private final HeadService headService;

    @Autowired
    public HeadController(HeadService headService) {
        this.headService = headService;
    }

    // 본사일반&회계관리 메인페이지
    @RequestMapping("")
    public String head_index(){
        return "index/headindex";
    }

    // dashboard
    // 매출 및 접수현황
    // 월간 매출 현황
    @RequestMapping("monthlysales")
    public String monthlysales(){
        return "head/dashboard/monthlysales";
    }

    // 품목별 매출 현황
    @RequestMapping("itemsales")
    public String itemsales(){
        return "head/dashboard/itemsales";
    }

    // 월간 접수 현황
    @RequestMapping("monthlyreg")
    public String monthlyreg(){
        return "head/dashboard/monthlyreg";
    }

    // 품목별 접수 현황
    @RequestMapping("itemreg")
    public String itemreg(){
        return "head/dashboard/itemreg";
    }

    // 객단가 현황
    @RequestMapping("unitprice")
    public String unitprice(){
        return "head/dashboard/unitprice";
    }


    // 본사일반 사용자등록 페이지
    @RequestMapping("accountreg")
    public String accountreg(Model model){
        model.addAttribute("roles", AccountRole.values());
        return "head/accountreg";
    }

    // 본사일반 계약관리 페이지
    @RequestMapping("contract")
    public String contract(){
        return "head/contract";
    }

    // 상품그룹관리 페이지
    @RequestMapping("itemgroup")
    public String itemgroup(){
        return "head/itemgroup";
    }

    // 상품가격관리 페이지
    @RequestMapping("itemprice")
    public String itemprice(Model model){
        List<ItemGroupNameListDto> itemGroupNameListDtos = headService.findByItemGroupName();
        List<ItemPriceSetDtDto> itemPriceSetDtDtos = headService.findByItemPriceSetDtList();
        model.addAttribute("groupNames", itemGroupNameListDtos);
        model.addAttribute("setDtLists", itemPriceSetDtDtos);
        return "head/itemprice";
    }

    // 특정품목가격관리 페이지
    @RequestMapping("franchiseprice")
    public String franchiseprice(){
        return "head/franchiseprice";
    }

    // 가격기초정보 세팅
    @RequestMapping("pricemanagement")
    public String pricemanagement(){
        return "head/pricemanagement";
    }

    // 나의정보관리
    @RequestMapping("mypage")
    public String mypage(){
        return "head/mypage";
    }

    // 마스터코드
    @RequestMapping("mastercode")
    public String mastercode(){
        return "head/admin/mastercode";
    }

    // 접수 현황
    @RequestMapping("receiptstat")
    public String receiptstat(){
        return "head/process/receiptstat";
    }
}
