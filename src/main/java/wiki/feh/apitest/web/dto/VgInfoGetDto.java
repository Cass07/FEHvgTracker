package wiki.feh.apitest.web.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.vginfo.VgInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class VgInfoGetDto {
    private Long id;

    private int vgNumber;
    private String vgTitle;
    private LocalDate vgStartDate;

    private List<String> teamIds = new ArrayList<>();

    public VgInfoGetDto(VgInfo entity) {
        this.id = entity.getId();
        this.vgNumber = entity.getVgNumber();
        this.vgTitle = entity.getVgTitle();
        this.vgStartDate = entity.getVgStartDate();
        this.teamIds.add(0, entity.getTeam1Id());
        this.teamIds.add(1, entity.getTeam2Id());
        this.teamIds.add(2, entity.getTeam3Id());
        this.teamIds.add(3, entity.getTeam4Id());
        this.teamIds.add(4, entity.getTeam5Id());
        this.teamIds.add(5, entity.getTeam6Id());
        this.teamIds.add(6, entity.getTeam7Id());
        this.teamIds.add(7, entity.getTeam8Id());
    }

    public String getTeamIdbyIndex(int index) {
        try {
            return this.teamIds.get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
