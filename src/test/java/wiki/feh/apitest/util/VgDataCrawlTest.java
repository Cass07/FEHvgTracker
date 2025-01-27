package wiki.feh.apitest.util;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.event.PostEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class VgDataCrawlTest {
    @Spy
    @InjectMocks
    private VgDataCrawl vgDataCrawl;

    @Mock
    private ApplicationEventPublisher testEventPublisher;

    private static Stream<Arguments> VgFileExtension() {
        return Stream.of(
                Arguments.of("vgweb/vg-1.html", 4),
                Arguments.of("vgweb/vg-2.html", 6),
                Arguments.of("vgweb/vg-3.html", 7)
        );
    }

    @DisplayName("Vg Data Crawl 내부 파일 테스트")
    @ParameterizedTest
    @MethodSource("VgFileExtension")
    void vgDataCrawlTest2(String vgFileDir, int resultSize) throws IOException {
        // Given
        int vgNumber = 91;
        URL fileDir = getClass().getClassLoader().getResource(vgFileDir);

        assert fileDir != null;

        File file = new File(fileDir.getFile());
        doReturn(Jsoup.parse(file, "UTF-8")).when(vgDataCrawl).getDocument(vgNumber);

        // When
        List<Map<String, String>> result = vgDataCrawl.getMapListByCrawl(vgNumber);

        // Then
        assertNotNull(result);
        assertEquals(resultSize, result.size());
    }

    @DisplayName("VG 크롤 시 이벤트 리스너 호출 테스트")
    @Test
    void vgDataCrawlEventTest() {
        // Given
        int vgNumber = 90;

        // When
        vgDataCrawl.getMapListByCrawl(vgNumber);

        // Then
        verify(testEventPublisher).publishEvent(any(PostEvent.class));
    }
}