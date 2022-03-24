package com.broadwave.toppos.Manager.TagGallery;

import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryDetailDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryListDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryMainListDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryFile.QTagGalleryFile;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryFile.TagGalleryFileListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TagGalleryRepositoryCustomImpl extends QuerydslRepositorySupport implements TagGalleryRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public TagGalleryRepositoryCustomImpl() {
        super(TagGallery.class);
    }

    // 택분실 갤러리 리스트 호출
    public List<TagGalleryListDto>  findByTagGalleryList(String searchString, String filterFromDt, String filterToDt, String brCode){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.insert_date, a.bt_id, a.bt_brand_name, a.bt_input_date, a.bt_material, a.bt_remark, c.fr_name, a.br_close_yn \n");
        sb.append("FROM br_tag_gallery a \n");
        sb.append("LEFT OUTER JOIN br_tag_gallery_check b ON b.bt_id = a.bt_id \n");
        sb.append("LEFT OUTER JOIN bs_franchise c ON c.fr_code = b.fr_code \n");
        sb.append("WHERE a.br_code = ?1 and a.bt_input_dt >= ?2 and a.bt_input_dt <= ?3 \n");
        if(searchString.equals("3")) {
            sb.append("and IFNULL(b.fr_code,'x') <> 'x' \n"); // 가맹응답상태 : 가맹점이 응답한것만 리스트호출
        }else if(searchString.equals("1")){
            sb.append("and a.br_close_yn = 'N' \n");  // 미완료상태 : brCloseYn상태가 "N"인것만 호출
        }
        sb.append("group BY a.bt_id ORDER BY a.bt_id DESC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);

        return jpaResultMapper.list(query, TagGalleryListDto.class);
    }

    // 택분실 갤러리 상세보기 호출
    public TagGalleryDetailDto findByTagGalleryDetail(Long btId, String brCode){
        QTagGallery tagGallery = QTagGallery.tagGallery;
        JPQLQuery<TagGalleryDetailDto> query = from(tagGallery)
                .where(tagGallery.btId.eq(btId))

                .select(Projections.constructor(TagGalleryDetailDto.class,
                        tagGallery.btId,
                        tagGallery.btBrandName,
                        tagGallery.btInputDate,
                        tagGallery.btMaterial,
                        tagGallery.btRemark,
                        tagGallery.brCloseYn
                    ));
        return query.fetchOne();
    }

    // 메인페이지용 택분실 갤러리 리스트 호출
    public List<TagGalleryMainListDto> findByTagGalleryMainList(String brCode){
        QTagGallery tagGallery = QTagGallery.tagGallery;
        JPQLQuery<TagGalleryMainListDto> query = from(tagGallery)
                .where(tagGallery.brCode.eq(brCode).and(tagGallery.brCloseYn.eq("N")))
                .select(Projections.constructor(TagGalleryMainListDto.class,
                        tagGallery.btId,
                        tagGallery.btBrandName,
                        tagGallery.btMaterial,
                        tagGallery.btInputDt
                ));

        query.orderBy(tagGallery.btId.desc()).limit(3);

        return query.fetch();
    }

}
