package wiki.feh.apitest.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wiki.feh.apitest.facade.VgInfoFacade;
import wiki.feh.apitest.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.dto.VgInfoGetDto;
import wiki.feh.apitest.dto.VgInfoSaveRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VgInfoApiController {

    private final VgInfoFacade vgInfoFacade;

    @GetMapping("/api/v1/vginfo/{id}")
    public VgInfoGetDto get(@PathVariable long id) {
        return vgInfoFacade.findById(id);
    }

    @GetMapping("/api/v1/vginfo")
    public List<VgInfoGetDropdownDto> getAll() {
        return vgInfoFacade.getVgInfoDropdownList();
    }

    @PutMapping("/api/v1/vginfo/{id}")
    public long update(@PathVariable long id, @RequestBody VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoFacade.vgInfoUpdate(id, vgInfoSaveRequestDto);
    }

    @PostMapping("/api/v1/vginfo")
    public long save(@RequestBody VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoFacade.save(vgInfoSaveRequestDto);
    }

}
