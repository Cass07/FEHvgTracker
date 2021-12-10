package wiki.feh.apitest.domain.vginfo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class VgInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
                  String team1Id, String team2Id, String team3Id, String team4Id, String team5Id, String team6Id, String team7Id, String team8Id){

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

    public void update (int vgNumber, String vgTitle, LocalDate vgStartDate,
    String team1Id, String team2Id, String team3Id, String team4Id, String team5Id, String team6Id, String team7Id, String team8Id)
    {
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

    public void UpdateTeamId(int teamIndex, String teamId)
    {
        switch(teamIndex)
        {
            case 1:
                this.team1Id = teamId;
                break;
            case 2:
                this.team2Id = teamId;
                break;
            case 3:
                this.team3Id = teamId;
                break;
            case 4:
                this.team4Id = teamId;
                break;
            case 5:
                this.team5Id = teamId;
                break;
            case 6:
                this.team6Id = teamId;
                break;
            case 7:
                this.team7Id = teamId;
                break;
            case 8:
                this.team8Id = teamId;
                break;
        }
    }
}
