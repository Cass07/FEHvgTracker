package wiki.feh.apitest.service.herolist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.domain.herolist.HeroList;
import wiki.feh.apitest.domain.herolist.HeroListRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class HeroListServiceTest {

    @InjectMocks
    private HeroListService heroListService;

    @Mock
    private HeroListRepository heroListRepository;

    @DisplayName("id로 HeroList 조회")
    @Test
    void getById() {
        // given
        String id = "Dagr_NewYearJötnar";
        HeroList heroList = new HeroList(id, "다그", "거인자매의 신년", "ダグ");
        Optional<HeroList> hero = Optional.of(heroList);
        doReturn(hero).when(heroListRepository).findById(id);

        // when
        HeroList result1 = heroListService.getById(id);

        // then
        assertEquals(heroList.getId(), result1.getId());
        assertEquals(heroList.getKorname(), result1.getKorname());
    }

    @DisplayName("jpname으로 korname 조회")
    @Test
    void getKornameByJpname() {
        // given
        String jpname = "リーフ";
        HeroList heroList = new HeroList("Leif_DestinedScions", "리프", "성제의 왕자들", jpname);
        List<HeroList> heroLists = List.of(new HeroList("Leif_DestinedScions", "리프", "성제의 왕자들", jpname),
                new HeroList("Leif_UnifierofThracia", "리프", "트라키아를 계승하는 자", jpname));
        doReturn(heroLists).when(heroListRepository).findByJpname(jpname);

        // when
        String result2 = heroListService.getKornameByJpname(jpname);

        // then
        assertEquals(heroList.getKorname(), result2);
    }

    @DisplayName("jpname으로 korname 조회 - jpname이 없는 경우")
    @Test
    void getKornameByJpname_NoJpname() {
        // given
        String jpname = "테스트";
        List<HeroList> heroLists = List.of();
        doReturn(heroLists).when(heroListRepository).findByJpname(jpname);

        // when
        String result3 = heroListService.getKornameByJpname(jpname);

        // then
        assertNull(result3);
    }
}