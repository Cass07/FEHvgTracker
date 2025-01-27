package wiki.feh.apitest.domain.herolist;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "`hero_list`")
@Entity
public class HeroList {
    @Id
    @Column(length = 35, nullable = false)
    private String id;

    @Column (length = 10, nullable = false)
    private String korname;

    @Column (length = 20, nullable = false)
    private String kornamesub;

    @Column (length = 10, nullable = false)
    private String jpname;

}
