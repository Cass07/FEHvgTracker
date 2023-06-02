package wiki.feh.apitest.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiki.feh.apitest.domain.vgdata.VgData;

@Getter
@NoArgsConstructor
public class VgDataGetDto {

    private Long id;
    private int vgNumber;
    private String team1Score;
    private String team2Score;

    private int team1Index;
    private int team2Index;

    private int roundNumber;

    private int tournamentIndex;

    private int timeIndex;

    private int losing;
    private double team1Rate;
    private double team2Rate;

    private String currentMul;

    @Builder
    public VgDataGetDto(VgData entity) {
        this.id = entity.getId();
        this.vgNumber = entity.getVgNumber();
        this.team1Index = entity.getTeam1Index();
        this.team2Index = entity.getTeam2Index();
        this.team1Score = entity.getTeam1Score();
        this.team2Score = entity.getTeam2Score();
        this.roundNumber = entity.getRoundNumber();
        this.tournamentIndex = entity.getTournamentIndex();
        this.timeIndex = entity.getTimeIndex();
        this.team1Rate = Double.parseDouble(calcTeam1Rate());
        this.team2Rate = Double.parseDouble(calcTeam2Rate());
        this.losing = calcLosing();
        this.currentMul = calcMul();
    }

    private int calcLosing() {
        if (this.floor(Long.parseLong(team1Score) / (double) Long.parseLong(team2Score),4) > 1.01)
            return 2;
        else if (this.floor(Long.parseLong(team2Score) / (double) Long.parseLong(team1Score),4) > 1.01)
            return 1;
        else
            return 0;
    }

    private double floor(double number, int dec)
    {
        double pow = Math.pow(10,dec);
        return Math.floor(number * pow) / pow;
    }

    private String calcTeam1Rate() {
        double rate = Long.parseLong(team1Score) / (double) Long.parseLong(team2Score);
        return String.format("%.3f", rate);
    }

    private String calcTeam2Rate() {
        double rate = Long.parseLong(team2Score) / (double) Long.parseLong(team1Score);
        return String.format("%.3f", rate);
    }

    private String calcMul() {
        if(this.losing == 1){
            return "< ×" + this.team2Rate + " <";
        }else{
            return "> ×" + this.team1Rate + " >";
        }
    }
}
