package com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark : Toppos 가맹점 NEW 택분실 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGalleryMapperDto {

    private Long btId;
    private String btBrandName; // 추정브랜드명
    private String btInputDate; // 예상 지사입고일
    private String btMaterial; // 소재
    private String btRemark; // 특이사항
    private List<MultipartFile> addPhotoList;
    private List<Long> deletePhotoList; // 지울 파일들의 id 리스트

}
