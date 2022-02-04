package com.broadwave.toppos.Manager.TagNotice.Comment;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-02-04
 * Time :
 * Remark :
 */
public interface TagNoticeCommentRepositoryCustom {

    List<TagNoticeCommentListDto> findByTagNoticeCommentList(Long htId, String login_id);

}
