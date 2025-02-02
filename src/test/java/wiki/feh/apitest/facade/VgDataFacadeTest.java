package wiki.feh.apitest.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.util.VgDataCrawl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgDataFacadeTest {
    @InjectMocks
    private VgDataFacade vgDataFacade;

    @Mock
    private VgDataService vgDataService;

    @Mock
    private VgInfoService vgInfoService;

    @Mock
    private PostsService postsService;

    @Mock
    private VgDataCrawl vgDataCrawl;

    @DisplayName("updateVgData - VgInfo 없음")
    @Test
    void updateVgData_NoVgInfo() {
        /**
         * TODO
         * return; 대신 RuntimeException을 던지도록 수정
         */

        // given
        doReturn(null).when(vgInfoService).getLatestVgInfo();

        // when
        vgDataFacade.updateVgData();

        // then
        assertTrue(true);
    }

}