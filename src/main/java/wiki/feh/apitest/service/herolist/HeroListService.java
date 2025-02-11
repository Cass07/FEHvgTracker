package wiki.feh.apitest.service.herolist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.herolist.HeroList;
import wiki.feh.apitest.domain.herolist.HeroListRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HeroListService {

    private final HeroListRepository heroListRepository;

    @Transactional(readOnly = true)
    public HeroList getById(String id) {
        return heroListRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public String getKornameByJpname(String jpname) {
        List<HeroList> entity = heroListRepository.findByJpname(jpname);

        if (entity.isEmpty()) {
            return null;
        }

        return entity.getFirst().getKorname();
    }
}
