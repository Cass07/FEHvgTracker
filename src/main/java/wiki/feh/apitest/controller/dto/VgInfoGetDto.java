package wiki.feh.apitest.controller.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.vginfo.VgInfo;

import java.time.LocalDate;

@Getter
public class VgInfoGetDto {
    private final long id;

    private final int vgNumber;
    private final String vgTitle;
    private final LocalDate vgStartDate;

    private final String[] teamIds = new String[8];

    public VgInfoGetDto(VgInfo entity) {
        this.id = entity.getId();
        this.vgNumber = entity.getVgNumber();
        this.vgTitle = entity.getVgTitle();
        this.vgStartDate = entity.getVgStartDate();
        this.teamIds[0] = entity.getTeam1Id();
        this.teamIds[1] = entity.getTeam2Id();
        this.teamIds[2] = entity.getTeam3Id();
        this.teamIds[3] = entity.getTeam4Id();
        this.teamIds[4] = entity.getTeam5Id();
        this.teamIds[5] = entity.getTeam6Id();
        this.teamIds[6] = entity.getTeam7Id();
        this.teamIds[7] = entity.getTeam8Id();
    }

    public String getTeamIdbyIndex(int index) {
        try {
            return this.teamIds[index-1];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
