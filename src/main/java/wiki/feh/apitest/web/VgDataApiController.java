package wiki.feh.apitest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.web.dto.VgDataGetDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VgDataApiController {
    private final VgDataService vgDataService;

    @GetMapping("/api/v1/vgdata/vgnum/{vgnum}/round/{round}/tournum/{tournum}")
    public List<VgDataGetDto> getAllList(@PathVariable int vgnum, @PathVariable int round, @PathVariable int tournum )
    {
        return vgDataService.getVgDataListbyNumRoundTour(vgnum, round, tournum);
    }

    @GetMapping("/api/v1/vgdata/vgnum/{vgnum}/round/{round}/tournum/{tournum}/first")
    public VgDataGetDto getFirstDto(@PathVariable int vgnum, @PathVariable int round, @PathVariable int tournum )
    {
        return vgDataService.getFirstVgDatabyNumRoundTour(vgnum, round, tournum);
    }

    @GetMapping("/api/v1/vgdata/vgnum/{vgnum}/round/{round}/tournum/{tournum}/latest")
    public VgDataGetDto getLatestDto(@PathVariable int vgnum, @PathVariable int round, @PathVariable int tournum )
    {
        return vgDataService.getLatestVgDatabyNumRoundTour(vgnum, round, tournum);
    }

    @GetMapping("/api/v1/vgdata/vgnum/{vgnum}/round/{round}/latest")
    public List<VgDataGetDto> getLatestDtoListbyVgnumRound(@PathVariable int vgnum, @PathVariable int round )
    {
        return vgDataService.getNowtimeVgDataListbyVgNumberRound(vgnum, round);
    }

    @GetMapping("/api/v1/vgdata/vgnum/{vgnum}/first")
    public List<VgDataGetDto> getFirstDtoListbyVgnum(@PathVariable int vgnum )
    {
        return vgDataService.getFirstVgDataListbyVgNumber(vgnum);
    }

    @GetMapping("/api/v1/vgdata/vgnum/{vgnum}/result")
    List<VgDataGetDto> getLatestVgDataListbyVgNumber(@PathVariable int vgnum )
    {
        return vgDataService.getFirstVgDataListbyVgNumber(vgnum);
    }

}
