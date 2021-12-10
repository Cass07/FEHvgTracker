package wiki.feh.apitest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wiki.feh.apitest.service.herolist.HeroListService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.web.dto.HeroListGetDto;
import wiki.feh.apitest.web.dto.VgDataGetDto;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class HelloController {
    public final HeroListService heroListService;
    public final VgDataService vgDataService;
    public final VgDataGetCron vgDataGetCron;

    @GetMapping("/hello")
    public String hello()
    {
        return "Hello world!";
    }

    @GetMapping("/test")
    public String test() throws IOException {
        jsouptest test1 = new jsouptest();
        test1.timetest();
        return test1.getDivFromWeb(53);
    }
    @GetMapping("/test/{id}")
    public String getHeroData(@PathVariable String id) throws IOException{
        HeroListGetDto heroListGetDto = heroListService.getbyId(id);
        if(heroListGetDto == null)
        {
            return "없음";
        }else{
            return heroListGetDto.getId() + " " + heroListGetDto.getKorname() + " " + heroListGetDto.getJpname();
        }
    }

    @GetMapping("/test/korname/{jpname}")
    public String getKorname(@PathVariable String jpname) throws IOException{
        String korname = heroListService.getKornamebyJpname(jpname);

        if(korname == null)
        {
            return "없음";
        }else
        {
            return korname;
        }
    }

    @GetMapping("/test/vgdata")
    public String testvgdata() throws Exception {
        vgDataGetCron.GetVgData();
        return "끗";
    }


}
