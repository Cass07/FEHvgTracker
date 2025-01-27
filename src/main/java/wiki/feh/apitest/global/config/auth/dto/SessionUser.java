package wiki.feh.apitest.global.config.auth.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.user.User;

@Getter
public class SessionUser {
    private final String name;
    private final String email;
    private final String picture;

    public SessionUser(User user)
    {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
