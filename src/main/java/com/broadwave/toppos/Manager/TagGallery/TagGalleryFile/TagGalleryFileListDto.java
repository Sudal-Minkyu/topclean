package com.broadwave.toppos.Manager.TagGallery.TagGalleryFile;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-03-18
 * Time :
 * Remark : Toppos 가맹점 NEW 택분실 TagGalleryFileListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagGalleryFileListDto {

    private Long bfId;
    private String bfPath;
    private String bfFilename;

}
