package wiki.feh.apitest.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.vginfo.VgInfo;

@Getter
public class VgInfoGetDropdownDto {
    private final long id;
    private final String text;

    public VgInfoGetDropdownDto(VgInfo entity) {
        this.id = entity.getId();
        this.text = entity.getVgNumber() + "회 : " + entity.getVgTitle() + " : " + entity.getVgStartDate();
    }

    public VgInfoGetDropdownDto(long id, String text) {
        this.id = id;
        this.text = text;
    }

}
