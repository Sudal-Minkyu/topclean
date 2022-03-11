package com.broadwave.toppos.Manager.TagGallery.TagGalleryFile;

import com.broadwave.toppos.Manager.TagGallery.TagGallery;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-03-11
 * Time :
 * Remark : Toppos 지사 택분실 첨부파일
 */
@Entity
@Data
@Builder
@EqualsAndHashCode(of = "bfId")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="br_tag_gallery_file")
public class TagGalleryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bf_id")
    private Long bfId;

    @ManyToOne(targetEntity = TagGallery.class,fetch = FetchType.EAGER)
    @JoinColumn(name="bt_id")
    private TagGallery btId; // 택분실 고유값 ID

    @Column(name="bf_path")
    private String bfPath; // S3파일경로

    @Column(name="bf_filename")
    private String bfFilename; // S3파일명

    @Column(name="bf_original_filename")
    private String bfOriginalFilename; // 원래 파일 명

    @Column(name="bf_volume")
    private Long bfVolume; // 파일용량

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
