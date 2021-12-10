package wiki.feh.apitest.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import wiki.feh.apitest.domain.user.Role;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()
                .regexMatchers("/admin/board/posts\\/[\\d]*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/vginfo/**", "/api/v1/vgdata/**").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/v1/vginfo/**").hasRole(Role.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/vginfo/**" ).hasRole(Role.USER.name())
                .antMatchers("/admin/manualcron","/api/v1/posts/**", "/admin/board/posts/update/**", "/admin/board/posts/save/**").hasRole(Role.USER.name())
                .antMatchers("/vg/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/admin/board/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }
}
