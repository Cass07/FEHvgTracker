package wiki.feh.apitest.web.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.vginfo.VgInfo;

import java.time.LocalDate;

@Getter
public class VgInfoGetDto {
    private Long id;

    private int vgNumber;
    private String vgTitle;
    private LocalDate vgStartDate;

    private String team1Id;
    private String team2Id;
    private String team3Id;
    private String team4Id;
    private String team5Id;
    private String team6Id;
    private String team7Id;
    private String team8Id;

    public VgInfoGetDto(VgInfo entity)
    {
        this.id = entity.getId();
        this.vgNumber = entity.getVgNumber();
        this.vgTitle = entity.getVgTitle();
        this.vgStartDate = entity.getVgStartDate();
        this.team1Id = entity.getTeam1Id();
        this.team2Id = entity.getTeam2Id();
        this.team3Id = entity.getTeam3Id();
        this.team4Id = entity.getTeam4Id();
        this.team5Id = entity.getTeam5Id();
        this.team6Id = entity.getTeam6Id();
        this.team7Id = entity.getTeam7Id();
        this.team8Id = entity.getTeam8Id();
    }

    public String getTeamIdbyIndex(int index)
    {
        switch(index)
        {
            case 1:
                return this.team1Id;
            case 2:
                return this.team2Id;
            case 3:
                return this.team3Id;
            case 4:
                return this.team4Id;
            case 5:
                return this.team5Id;
            case 6:
                return this.team6Id;
            case 7:
                return this.team7Id;
            case 8:
                return this.team8Id;
            default:
                return null;
        }
    }
}
