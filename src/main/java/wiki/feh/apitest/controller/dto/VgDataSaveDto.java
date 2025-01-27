package wiki.feh.apitest.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiki.feh.apitest.domain.vgdata.VgData;

@Getter
@NoArgsConstructor
public class VgDataSaveDto {
    private int vgNumber;
    private String team1Score;
    private String team2Score;

    private int team1Index;
    private int team2Index;

    private int roundNumber;
    private int tournamentIndex;
    private int timeIndex;

    @Builder
    public VgDataSaveDto(int vgNumber, String team1Score, String team2Score, int team1Index, int team2Index, int roundNumber,
                         int tournamentIndex, int timeIndex) {
        this.vgNumber = vgNumber;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.team1Index = team1Index;
        this.team2Index = team2Index;
        this.roundNumber = roundNumber;
        this.tournamentIndex = tournamentIndex;
        this.timeIndex = timeIndex;
    }

    public VgData toEntity() {
        return VgData.builder()
                .vgNumber(this.vgNumber)
                .team1Score(this.team1Score.replace(",", ""))
                .team2Score(this.team2Score.replace(",", ""))
                .team1Index(this.team1Index)
                .team2Index(this.team2Index)
                .roundNumber(this.roundNumber)
                .tournamentIndex(this.tournamentIndex)
                .timeIndex(this.timeIndex)
                .build();
    }
}
