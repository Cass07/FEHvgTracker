package wiki.feh.apitest.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiki.feh.apitest.domain.vginfo.VgInfo;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class VgInfoSaveRequestDto {
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

    @Builder
    public VgInfoSaveRequestDto(int vgNumber, String vgTitle, LocalDate vgStartDate,
                                String team1Id, String team2Id, String team3Id, String team4Id, String team5Id, String team6Id, String team7Id, String team8Id) {

        this.vgNumber = vgNumber;
        this.vgTitle = vgTitle;
        this.vgStartDate = vgStartDate;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.team3Id = team3Id;
        this.team4Id = team4Id;
        this.team5Id = team5Id;
        this.team6Id = team6Id;
        this.team7Id = team7Id;
        this.team8Id = team8Id;

    }

    public VgInfo toEntity() {
        return VgInfo.builder()
                .vgNumber(this.vgNumber)
                .vgTitle(this.vgTitle)
                .vgStartDate(this.vgStartDate)
                .team1Id(this.team1Id)
                .team2Id(this.team2Id)
                .team3Id(this.team3Id)
                .team4Id(this.team4Id)
                .team5Id(this.team5Id)
                .team6Id(this.team6Id)
                .team7Id(this.team7Id)
                .team8Id(this.team8Id)
                .build();

    }
}
