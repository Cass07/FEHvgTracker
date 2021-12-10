package wiki.feh.apitest.service.herolist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.herolist.HeroList;
import wiki.feh.apitest.domain.herolist.HeroListRepository;
import wiki.feh.apitest.web.dto.HeroListGetDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HeroListService {

    private final HeroListRepository heroListRepository;

    @Transactional(readOnly = true)
    public HeroListGetDto getbyId(String id)
    {
        HeroList entity = heroListRepository.findById(id).orElse(null);

        if(entity == null)
        {
            return null;
        }else{
            return new HeroListGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public String getKornamebyJpname(String jpname)
    {
        List<HeroList> entity = heroListRepository.findByJpname(jpname);

        if(entity.size() == 0)
        {
            return null;
        }else{
            return entity.get(0).getKorname();
        }
    }
}
