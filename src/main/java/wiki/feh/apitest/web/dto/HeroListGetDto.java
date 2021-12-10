package wiki.feh.apitest.web.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.herolist.HeroList;

@Getter
public class HeroListGetDto {
    public String id;
    public String korname;
    public String jpname;
    public String kornamesub;

    public HeroListGetDto(HeroList entity)
    {
        id = entity.getId();
        korname = entity.getKorname();
        jpname = entity.getJpname();
        kornamesub = entity.getKornamesub();
    }
}
