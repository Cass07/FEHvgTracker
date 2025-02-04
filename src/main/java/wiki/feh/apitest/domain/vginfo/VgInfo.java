package wiki.feh.apitest.domain.vginfo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiki.feh.apitest.dto.VgInfoSaveRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
@Table(name = "`vg_info`")
@Entity
public class VgInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int vgNumber;

    @Column(nullable = false)
    private String vgTitle;

    @Column(nullable = false)
    private LocalDate vgStartDate;

    //embedded로 따로빼면 좋겟지요??
    //id#한글이름 이렇게 데이터 저장할것임
    @Column(length = 50)
    private String team1Id;
    @Column(length = 50)
    private String team2Id;
    @Column(length = 50)
    private String team3Id;
    @Column(length = 50)
    private String team4Id;
    @Column(length = 50)
    private String team5Id;
    @Column(length = 50)
    private String team6Id;
    @Column(length = 50)
    private String team7Id;
    @Column(length = 50)
    private String team8Id;

    @Builder
    public VgInfo(int vgNumber, String vgTitle, LocalDate vgStartDate,
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

    public void update(int vgNumber, String vgTitle, LocalDate vgStartDate,
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

    public void update(VgInfoSaveRequestDto dto) {
        this.vgNumber = dto.getVgNumber();
        this.vgTitle = dto.getVgTitle();
        this.vgStartDate = dto.getVgStartDate();
        this.team1Id = dto.getTeam1Id();
        this.team2Id = dto.getTeam2Id();
        this.team3Id = dto.getTeam3Id();
        this.team4Id = dto.getTeam4Id();
        this.team5Id = dto.getTeam5Id();
        this.team6Id = dto.getTeam6Id();
        this.team7Id = dto.getTeam7Id();
        this.team8Id = dto.getTeam8Id();
    }

    /**
     * 현재 시간에 진행 중인 vgInfo인지 여부를 번환
     * true라면 데이터를 수집해야 함을 의미
     *
     * @param now
     * @return
     */
    public boolean isValidTime(LocalDateTime now) {
        long timeDiff = ChronoUnit.HOURS.between(vgStartDate.atTime(16, 0), now);
        return timeDiff <= 141 && timeDiff > 0;
    }

    /**
     * 현재 시간의 vgInfo 라운드 값을 반환
     *
     * @param now
     * @return
     */
    public int getRoundByTime(LocalDateTime now) {
        long timeDiff = ChronoUnit.HOURS.between(vgStartDate.atTime(16, 0), now);
        return (((int) timeDiff - 1) / 48) + 1;
    }

    /**
     * 현재 라운드의 시간차를 반환
     *
     * @param now
     * @param round
     * @return
     */
    public int getRoundTimeDiff(LocalDateTime now, int round) {
        return (int) ChronoUnit.HOURS.between(vgStartDate.plusDays(round * 2L - 2).atTime(16, 0), now);
    }
}
