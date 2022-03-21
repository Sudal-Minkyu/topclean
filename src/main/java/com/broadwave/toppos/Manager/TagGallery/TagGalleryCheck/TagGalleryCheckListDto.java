package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-18
 * Time :
 * Remark : Toppos 가맹점 NEW 택분실 TagGalleryCheckListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGalleryCheckListDto {

    private String frName; // 가맹점명
    private String brCompleteYn;  // 기본값 N, 해당 분실 세탁물을 고객에게 인수완료했을 경우 Y로 변경

}
