package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailBranchReturnCurrentListDto;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TagGalleryCheckRepositoryCustomImpl extends QuerydslRepositorySupport implements TagGalleryCheckRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public TagGalleryCheckRepositoryCustomImpl() {
        super(TagGalleryCheck.class);
    }

    public TagGalleryCheckListDto findByTagGalleryCheckList(Long btId){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT b.fr_Name \n");
        sb.append("FROM br_tag_gallery_check a \n");
        sb.append("Inner JOIN bs_franchise b on b.fr_code = a.fr_code \n");
        sb.append("WHERE a.bt_id = ?1 \n");
        sb.append("order BY a.bc_id asc limit 1 \n");

//        sb.append("SELECT group_concat(b.fr_Name) \n");
//        sb.append("FROM br_tag_gallery_check a \n");
//        sb.append("Inner JOIN bs_franchise b on b.fr_code = a.fr_code \n");
//        sb.append("WHERE a.bt_id = ?1 \n");
//        sb.append("group BY a.bt_id \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, btId);

        return jpaResultMapper.uniqueResult(query, TagGalleryCheckListDto.class);
    }

}
