package wiki.feh.apitest.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiki.feh.apitest.domain.BaseTimeEntity;

import jakarta.persistence.*;

@Getter
@NoArgsConstructor
@Table(name= "`feh_user`")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role)
    {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture)
    {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey()
    {
        return this.role.getKey();
    }
}
