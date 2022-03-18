package com.broadwave.toppos.Manager.TagGallery;

import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryListDto;
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

    public List<TagGalleryListDto>  findByTagGalleryList(String searchString, String filterFromDt, String filterToDt, String brCode){

//        SELECT a.insert_date, a.bt_id, a.bt_brand_name, a.bt_input_date, a.bt_material, a.bt_remark
//        FROM br_tag_gallery a
//        LEFT OUTER JOIN br_tag_gallery_check b ON b.bt_id = a.bt_id
//        WHERE IFNULL(b.fr_code,'x') <> 'x'
//        group BY a.bt_id ORDER BY a.bt_id DESC;
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.insert_date, a.bt_id, a.bt_brand_name, a.bt_input_date, a.bt_material, a.bt_remark, c.fr_name \n");
        sb.append("FROM br_tag_gallery a \n");
        sb.append("LEFT OUTER JOIN br_tag_gallery_check b ON b.bt_id = a.bt_id \n");
        sb.append("LEFT OUTER JOIN bs_franchise c ON c.fr_code = b.fr_code \n");
        sb.append("WHERE a.br_code = ?1 and a.bt_input_dt >= ?2 and a.bt_input_dt <= ?3 \n");
        if(searchString.equals("3")) {
            sb.append("and IFNULL(b.fr_code,'x') <> 'x' \n");
        }else if(searchString.equals("1")){
            sb.append("and a.br_close_yn = 'N' \n");
        }
        sb.append("group BY a.bt_id ORDER BY a.bt_id DESC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);

        return jpaResultMapper.list(query, TagGalleryListDto.class);

    }

}
