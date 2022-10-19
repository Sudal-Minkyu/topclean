package com.broadwave.toppos.Head.Promotion;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionDto;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionListDto;
import com.broadwave.toppos.Head.Promotion.PromotionFr.QPromotionFr;
import com.broadwave.toppos.Head.Promotion.PromotionItem.QPromotionItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark :
 */
@Repository
public class PromotionRepositoryCustomImpl extends QuerydslRepositorySupport implements PromotionRepositoryCustom {

    public PromotionRepositoryCustomImpl() {
        super(Promotion.class);
    }

    @Override
    public List<PromotionDto> findByPromotion(Long branchId, Long franchiseId, String filterDt, String hpName, String hpStatus) {

        QPromotion promotion = QPromotion.promotion;

        QPromotionFr promotionFr = QPromotionFr.promotionFr;
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<PromotionDto> query = from(promotion)
                .innerJoin(promotionFr).on(promotionFr.hpId.eq(promotion.hpId))
                .innerJoin(franchise).on(franchise.frCode.eq(promotionFr.frCode))
                .innerJoin(branch).on(franchise.brCode.eq(branch.brCode))
                .where(promotion.hpStart.substring(0,8).loe(filterDt).and(promotion.hpEnd.substring(0,8).goe(filterDt)))
                .select(Projections.constructor(PromotionDto.class,
                        promotion.hpId,
                        promotion.hpType,
                        promotion.hpName,
                        promotion.hpStart,
                        promotion.hpEnd,
                        promotion.hpWeekend,
                        promotion.hpStatus,
                        promotion.hpContent
                ));

        if(branchId != 0){
            query.where(branch.id.eq(branchId));
        }

        if(franchiseId != 0){
            query.where(franchise.id.eq(franchiseId));
        }

        if(!hpStatus.equals("00")){
            query.where(promotion.hpStatus.eq(hpStatus));
        }

        if(!hpName.isEmpty()){
            query.where(promotion.hpName.likeIgnoreCase("%"+hpName+"%"));
        }

        query.groupBy(promotion.hpId).distinct().orderBy(promotion.hpId.desc());

        return query.fetch();
    }

    @Override
    public List<PromotionListDto> findByPromotionList(String frCode, int dayOfWeek, String nowDate) {

        QPromotion promotion = QPromotion.promotion;
        QPromotionFr promotionFr = QPromotionFr.promotionFr;
        QPromotionItem promotionItem = QPromotionItem.promotionItem;

        JPQLQuery<PromotionListDto> query = from(promotion)
                .innerJoin(promotionFr).on(promotionFr.hpId.eq(promotion.hpId).and(promotionFr.frCode.eq(frCode)))
                .innerJoin(promotionItem).on(promotionItem.hpId.eq(promotion.hpId))
                .where(promotion.hpStart.loe(nowDate).and(promotion.hpEnd.goe(nowDate)))
                .where(promotion.hpStatus.eq("01"))
                .select(Projections.constructor(PromotionListDto.class,
                        promotion.hpId,
                        promotion.hpType,
                        promotion.hpName,
                        promotionItem.biItemcode,
                        promotionItem.hiDiscountRt
                ));

        if(dayOfWeek == 1){
            System.out.println("일요일입니다.");
            query.where(promotion.hpWeekend.charAt(0).eq('1'));
        }else if(dayOfWeek == 2){
            System.out.println("월요일입니다.");
            query.where(promotion.hpWeekend.charAt(1).eq('1'));
        }else if(dayOfWeek == 3){
            System.out.println("화요일입니다.");
            query.where(promotion.hpWeekend.charAt(2).eq('1'));
        }else if(dayOfWeek == 4){
            System.out.println("수요일입니다.");
            query.where(promotion.hpWeekend.charAt(3).eq('1'));
        }else if(dayOfWeek == 5){
            System.out.println("목요일입니다.");
            query.where(promotion.hpWeekend.charAt(4).eq('1'));
        }else if(dayOfWeek == 6){
            System.out.println("금요일입니다.");
            query.where(promotion.hpWeekend.charAt(5).eq('1'));
        }else if(dayOfWeek == 7){
            System.out.println("토요일입니다.");
            query.where(promotion.hpWeekend.charAt(6).eq('1'));
        }

        return query.fetch();
    }

}