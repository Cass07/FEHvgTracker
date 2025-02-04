package wiki.feh.apitest.global.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import wiki.feh.apitest.domain.user.Role;
import wiki.feh.apitest.domain.user.User;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture)
    {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registerId, String usernameAttributeName, Map<String, Object> attributes)
    {
        return ofGoogle(usernameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes)
    {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    public User toEntity()
    {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }

}
