package wiki.feh.apitest.web.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.vginfo.VgInfo;

@Getter
public class VgInfoGetDropdownDto {
    private Long id;
    private String text;

    public VgInfoGetDropdownDto(VgInfo entity) {
        this.id = entity.getId();
        this.text = entity.getVgNumber() + "회 : " + entity.getVgTitle() + " : " + entity.getVgStartDate();
    }

    public VgInfoGetDropdownDto(Long id, String text) {
        this.id = id;
        this.text = text;
    }

}
