package wiki.feh.apitest.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wiki.feh.apitest.dto.VgDataGetDto;
import wiki.feh.apitest.dto.VgInfoGetDto;
import wiki.feh.apitest.dto.VgViewDto;
import wiki.feh.apitest.facade.VgViewFacade;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class VgViewController {

    private final VgViewFacade vgViewFacade;

    @GetMapping("/vg/vgnum/{vgnum}/round/{round}/tournum/{tournum}")
    public String getVgDataDetail(Model model, @PathVariable int vgnum, @PathVariable int round, @PathVariable int tournum) {
        VgInfoGetDto vgInfoEntity = vgViewFacade.getVgInfoById(vgnum);

        VgDataGetDto vgDataGetDtoList = vgViewFacade.getFirstVgDataByNumRoundTour(vgnum, round, tournum);

        String team1name = vgInfoEntity.getTeamIdbyIndex(vgDataGetDtoList.getTeam1Index()).split("#")[1];
        String team2name = vgInfoEntity.getTeamIdbyIndex(vgDataGetDtoList.getTeam2Index()).split("#")[1];

        model.addAttribute("header_title", "세부 데이터 : " + vgnum + "회 "
                + vgInfoEntity.getVgTitle() + " " + round + "라운드 : "
                + team1name + " vs " + team2name);

        model.addAttribute("vg_info", vgInfoEntity);
        model.addAttribute("team1_name", team1name);
        model.addAttribute("team2_name", team2name);
        model.addAttribute("vgNumber", vgnum);
        model.addAttribute("roundNumber", round);
        model.addAttribute("tourNumber", tournum);

        return "vg-data";
    }

    @GetMapping(value = {"/vg/", "/vg"})
    public String vgMain(Model model) {
        VgViewDto vgViewDto = vgViewFacade.getVgMainByid(-1);

        Map<String, Object> modelList = getVgPageModel(vgViewDto);
        modelList.forEach(model::addAttribute);

        return vgViewDto.getViewString();
    }

    @GetMapping(value = {"/vg/past/", "/vg/past"})
    public String vgPastList(Model model) {
        VgViewDto vgViewDto = vgViewFacade.getVgMainByid(-1);

        Map<String, Object> modelList = getVgPageModel(vgViewDto);
        modelList.forEach(model::addAttribute);

        return "vg-data-all";
    }

    @GetMapping(value = {"/vg/past/{id}", "/vg/{id}"})
    public String vgPastListbyId(Model model, @PathVariable long id) {
        VgViewDto vgViewDto = vgViewFacade.getVgMainByid(id);

        Map<String, Object> modelList = getVgPageModel(vgViewDto);
        modelList.forEach(model::addAttribute);

        return "vg-data-all";
    }

    @GetMapping(value = {"/vg/first/", "/vg/first"})
    public String vgFirst(Model model) {
        VgViewDto vgViewDto = vgViewFacade.getVgFirstById(-1);

        Map<String, Object> modelList = getVgPageModel(vgViewDto);
        modelList.forEach(model::addAttribute);
        model.addAttribute("title", "5시 초동 데이터");

        return vgViewDto.getViewString();
    }

    private Map<String, Object> getVgPageModel(VgViewDto vgViewDto) {

        Map<String, Object> modelList = new HashMap<>(vgViewDto.getViewModel());
        modelList.put("header_title", "투표대전 점수 트래커");
        modelList.put("title", "메인 페이지");

        modelList.put("vgInfo", vgViewDto.getVgInfoEntity());
        modelList.put("currentRoundVgdata", vgViewDto.getCurrentRoundVgdata());
        modelList.put("round1Vgdata", vgViewDto.getRound1Vgdata());
        modelList.put("round2Vgdata", vgViewDto.getRound2Vgdata());
        modelList.put("round3Vgdata", vgViewDto.getRound3Vgdata());
        modelList.put("teamList", vgViewDto.getTeamList());

        return modelList;
    }
}
