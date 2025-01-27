package wiki.feh.apitest.domain.vgdata;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "`vg_data`")
@Entity
public class VgData{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int vgNumber;//vg 회차 숫자

    @Column(length = 20, nullable = false)
    private String team1Score;

    @Column(length = 20, nullable = false)
    private String team2Score;

    private int team1Index;//1~8
    private int team2Index;

    private int roundNumber;//1~3

    private int tournamentIndex;//라운드별 팀 인덱스 (검색할때편하라고) 1~4

    private int timeIndex;//현재시간 - 시작시간 (1~45) (45는 결과)

    @Builder
    public VgData(int vgNumber, String team1Score, String team2Score, int team1Index, int team2Index, int roundNumber,
            int tournamentIndex, int timeIndex)
    {
        this.vgNumber = vgNumber;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.team1Index = team1Index;
        this.team2Index = team2Index;
        this.roundNumber = roundNumber;
        this.tournamentIndex = tournamentIndex;
        this.timeIndex = timeIndex;
    }
}
