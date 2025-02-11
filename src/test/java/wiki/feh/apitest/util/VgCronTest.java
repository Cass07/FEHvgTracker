package wiki.feh.apitest.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.facade.VgDataFacade;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgCronTest {
    @InjectMocks
    private VgCron vgCron;

    @Mock
    private VgDataFacade vgDataFacade;

    @DisplayName("VgCron 실행")
    @Test
    void CronVgData() {
        // given
        doNothing().when(vgDataFacade).updateVgData();

        // when
        vgCron.CronVgData();

        // then
        verify(vgDataFacade).updateVgData();
    }
}