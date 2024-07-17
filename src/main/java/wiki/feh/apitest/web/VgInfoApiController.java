package wiki.feh.apitest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.web.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.web.dto.VgInfoGetDto;
import wiki.feh.apitest.web.dto.VgInfoSaveRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class VgInfoApiController {

    private final VgInfoService vgInfoService;

    @GetMapping("/api/v1/vginfo/{id}")
    public VgInfoGetDto get(@PathVariable Long id) {
        return vgInfoService.findbyId(id);
    }

    @GetMapping("/api/v1/vginfo")
    public List<VgInfoGetDropdownDto> getAll() {
        return vgInfoService.findAllDescDropdown();
    }

    @PutMapping("/api/v1/vginfo/{id}")
    public Long update(@PathVariable Long id, @RequestBody VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoService.update(id, vgInfoSaveRequestDto);
    }

    @PostMapping("/api/v1/vginfo")
    public Long save(@RequestBody VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoService.save(vgInfoSaveRequestDto);
    }

}
