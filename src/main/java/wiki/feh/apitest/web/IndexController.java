package wiki.feh.apitest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wiki.feh.apitest.global.config.auth.LoginUser;
import wiki.feh.apitest.global.config.auth.dto.SessionUser;
import wiki.feh.apitest.service.global.PostsViewService;
import wiki.feh.apitest.service.global.VgViewService;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.web.dto.*;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    private final VgDataService vgDataService;
    private final VgInfoService vgInfoService;
    private final VgViewService vgViewService;
    
    private final String custUri = "/voting";

    private final VgDataGetCron vgDataGetCron;
    private final PostsViewService postsViewService;

    @GetMapping("/admin/board/")
    public String index(Model model, @LoginUser SessionUser user) {
        PostsViewDto postsViewDto = postsViewService.getPostsListView(1);

        model.addAttribute("header_title", "로그 게시판");
        model.addAttribute("customUri", custUri);
        model.addAttribute("posts", postsViewDto.getPostsList());
        postsViewDto.getViewModel().forEach(model::addAttribute);

        if (user != null) {
            model.addAttribute("userName", user);
        }

        return "index";
    }

    @GetMapping("/admin/board/page/{page}")
    public String indexPage(Model model, @LoginUser SessionUser user, @PathVariable int page)
    {
        PostsViewDto postsViewDto = postsViewService.getPostsListView(page);

        model.addAttribute("header_title", "로그 게시판");
        model.addAttribute("customUri", custUri);
        model.addAttribute("posts", postsViewDto.getPostsList());
        postsViewDto.getViewModel().forEach(model::addAttribute);

        if (user != null) {
            model.addAttribute("userName", user);
        }

        return "index";
    }


    @GetMapping("/admin/board/posts/{id}")
    public String posts(@PathVariable Long id, Model model) {
        model.addAttribute("header_title", "글 내용 보기");
        model.addAttribute("customUri", custUri);
        PostsGetWithPicDto postsGetWithPicDto = postsService.getByIdWithPic(id);

        if (postsGetWithPicDto != null) {
            model.addAttribute("posts", postsGetWithPicDto);
            return "posts";
        } else {
            model.addAttribute("errorMessage", "삭제되었거나 조회되지 않는 게시물입니다.");
            //return "redirect:/";
            return "posts-error";
        }
    }

    @GetMapping("/admin/board/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user) {
        model.addAttribute("header_title", "글 등록");
        model.addAttribute("user", user);
        model.addAttribute("customUri", custUri);
        return "posts-save";
    }

    @GetMapping("/admin/board/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user) {
        model.addAttribute("header_title", "글 수정하기");
        model.addAttribute("customUri", custUri);
        PostsResponceDto dto = postsService.findbyId(id);
        model.addAttribute("posts", dto);

        if (!dto.getAuthor().equals(user.getEmail())) {
            model.addAttribute("errorMessage", "자신이 쓴 글만 수정 가능합니다.");
            //return "redirect:/";
            return "posts-error";
        }

        return "posts-update";
    }


    ////test///


    @GetMapping("/admin/vginfo")
    public String SetVginfo(Model model) {
        model.addAttribute("header_title", "투표대전 데이터 세팅");
        model.addAttribute("customUri", custUri);
        return "setting-vginfo";
    }

    @GetMapping("/vg/vgnum/{vgnum}/round/{round}/tournum/{tournum}")
    public String getVgDataDetail(Model model, @PathVariable int vgnum, @PathVariable int round, @PathVariable int tournum) {
        VgInfoGetDto vginfoEntity = vgInfoService.findbyVgnumber(vgnum);
        if(vginfoEntity  == null)
        {
            model.addAttribute("errorMessage", "해당 투표대전이 존재하지 않습니다.");
            model.addAttribute("customUri", custUri);
            return "posts-error";
        }
        VgDataGetDto vgDataGetDtoList = vgDataService.getFirstVgDatabyNumRoundTour(vgnum, round, tournum);
        if(vgDataGetDtoList == null)
        {
            model.addAttribute("errorMessage", "해당 투표대전 라운드가 존재하지 않습니다.");
            model.addAttribute("customUri", custUri);
            return "posts-error";
        }
        int team1index = vgDataGetDtoList.getTeam1Index();
        int team2index = vgDataGetDtoList.getTeam2Index();

        String team1name = vginfoEntity.getTeamIdbyIndex(team1index).split("#")[1];
        String team2name = vginfoEntity.getTeamIdbyIndex(team2index).split("#")[1];

        model.addAttribute("header_title", "세부 데이터 : " + vgnum + "회 "
                + vginfoEntity.getVgTitle() + " " + round + "라운드 : "
                + team1name + " vs " + team2name);

        model.addAttribute("vg_info", vginfoEntity);
        model.addAttribute("team1_name", team1name);
        model.addAttribute("team2_name", team2name);
        model.addAttribute("vgNumber", vgnum);
        model.addAttribute("roundNumber", round);
        model.addAttribute("tourNumber", tournum);
        model.addAttribute("customUri", custUri);
        return "vg-data";
    }

    //serviceTest//

    @GetMapping(value = {"/vg/", "/vg"})
    public String vgMain(Model model)
    {
        VgViewDto vgViewDto = vgViewService.getVgMainbyid((long) -1);
        vgViewDto.getViewModel().forEach(model::addAttribute);
        model.addAttribute("header_title", "투표대전 점수 트래커");
        model.addAttribute("title", "메인 페이지");
        model.addAttribute("customUri", custUri);
        model.addAttribute("vgInfo", vgViewDto.getVgInfoEntity());
        model.addAttribute("currentRoundVgdata", vgViewDto.getCurrentRoundVgdata());
        model.addAttribute("round1Vgdata", vgViewDto.getRound1Vgdata());
        model.addAttribute("round2Vgdata", vgViewDto.getRound2Vgdata());
        model.addAttribute("round3Vgdata", vgViewDto.getRound3Vgdata());

        return vgViewDto.getViewString();
    }

    @GetMapping(value = {"/vg/past/", "/vg/past"})
    public String vgPastList(Model model)
    {
        VgViewDto vgViewDto = vgViewService.getVgMainbyid((long) -1);
        vgViewDto.getViewModel().forEach(model::addAttribute);
        model.addAttribute("header_title", "투표대전 점수 트래커");
        model.addAttribute("title", "메인 페이지");
        model.addAttribute("customUri", custUri);
        model.addAttribute("vgInfo", vgViewDto.getVgInfoEntity());
        model.addAttribute("currentRoundVgdata", vgViewDto.getCurrentRoundVgdata());
        model.addAttribute("round1Vgdata", vgViewDto.getRound1Vgdata());
        model.addAttribute("round2Vgdata", vgViewDto.getRound2Vgdata());
        model.addAttribute("round3Vgdata", vgViewDto.getRound3Vgdata());

        return !Objects.equals(vgViewDto.getViewString(), "posts-error") ? "vg-data-all" : "posts-error";
    }

    @GetMapping("/vg/past/{id}")
    public String vgPastListbyId(Model model, @PathVariable Long id)
    {
        VgViewDto vgViewDto = vgViewService.getVgMainbyid(id);
        vgViewDto.getViewModel().forEach(model::addAttribute);
        model.addAttribute("header_title", "투표대전 점수 트래커");
        model.addAttribute("title", "메인 페이지");
        model.addAttribute("customUri", custUri);
        model.addAttribute("vgInfo", vgViewDto.getVgInfoEntity());
        model.addAttribute("currentRoundVgdata", vgViewDto.getCurrentRoundVgdata());
        model.addAttribute("round1Vgdata", vgViewDto.getRound1Vgdata());
        model.addAttribute("round2Vgdata", vgViewDto.getRound2Vgdata());
        model.addAttribute("round3Vgdata", vgViewDto.getRound3Vgdata());

        return !Objects.equals(vgViewDto.getViewString(), "posts-error") ? "vg-data-all" : "posts-error";
    }

    @GetMapping(value = {"/vg/first/", "/vg/first"})
    public String vgFirst(Model model)
    {
        VgViewDto vgViewDto = vgViewService.getVgFirstbyId((long) -1);
        vgViewDto.getViewModel().forEach(model::addAttribute);
        model.addAttribute("header_title", "투표대전 점수 트래커");
        model.addAttribute("title", "5시 초동 데이터");
        model.addAttribute("customUri", custUri);
        model.addAttribute("vgInfo", vgViewDto.getVgInfoEntity());
        model.addAttribute("currentRoundVgdata", vgViewDto.getCurrentRoundVgdata());
        model.addAttribute("round1Vgdata", vgViewDto.getRound1Vgdata());
        model.addAttribute("round2Vgdata", vgViewDto.getRound2Vgdata());
        model.addAttribute("round3Vgdata", vgViewDto.getRound3Vgdata());

        return vgViewDto.getViewString();
    }

    @GetMapping("/admin/manualcron")
    public String vgCronManual() throws Exception {
        vgDataGetCron.GetVgData();
        return "redirect:/admin/board/";
    }

}
