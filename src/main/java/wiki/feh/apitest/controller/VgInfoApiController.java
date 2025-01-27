package wiki.feh.apitest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.controller.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.controller.dto.VgInfoGetDto;
import wiki.feh.apitest.controller.dto.VgInfoSaveRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VgInfoApiController {

    private final VgInfoService vgInfoService;

    @GetMapping("/api/v1/vginfo/{id}")
    public VgInfoGetDto get(@PathVariable long id) {
        return vgInfoService.findbyId(id);
    }

    @GetMapping("/api/v1/vginfo")
    public List<VgInfoGetDropdownDto> getAll() {
        return vgInfoService.findAllDescDropdown();
    }

    @PutMapping("/api/v1/vginfo/{id}")
    public long update(@PathVariable long id, @RequestBody VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoService.update(id, vgInfoSaveRequestDto);
    }

    @PostMapping("/api/v1/vginfo")
    public long save(@RequestBody VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoService.save(vgInfoSaveRequestDto);
    }

}
