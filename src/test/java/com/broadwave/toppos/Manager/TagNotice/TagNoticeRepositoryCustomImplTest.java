package com.broadwave.toppos.Manager.TagNotice;

import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeTestDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author InSeok
 * Date : 2022-02-15
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TagNoticeRepositoryCustomImplTest {
    @Autowired
    TagNoticeRepositoryCustomImpl tagNoticeRepositoryCustom;

    @Test
    @Ignore
    @DisplayName("QueryDSL Group by 테스트")
    public void querydsl_Group_by_Test(){
        List<TagNoticeTestDto> results = tagNoticeRepositoryCustom.findByGroupByTest();
        for (TagNoticeTestDto v: results) {
            System.out.println(v);

        }
    }

    @Test
    @Ignore
    @DisplayName("QueryDSL Group by 테스트")
    public void querydsl_native_query_Test(){
        List<TagNoticeTestDto> results = tagNoticeRepositoryCustom.findByGroupByNativeTest();
        for (TagNoticeTestDto v: results) {
            System.out.println(v);


        }
    }




}