package wiki.feh.apitest.global.config.auth;

import lombok.RequiredArgsConstructor;
import wiki.feh.apitest.domain.user.Role;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) ->
            webSecurity.ignoring()
                .requestMatchers("/api/v1/vginfo/**", "/vg/**", "/error")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.logout(logout -> logout.logoutSuccessUrl("/admin/board/"));

        http.sessionManagement(sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        );

        http.oauth2Login(oauth2Login -> {
            oauth2Login.userInfoEndpoint(userInfoEndpoint -> {
                userInfoEndpoint.userService(customOAuth2UserService);
            });
        });

        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()
                .requestMatchers("/admin/board/posts\\/[\\d]*").hasRole(Role.USER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/vginfo/**", "/api/v1/vgdata/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/v1/vginfo/**").hasRole(Role.USER.name())
                .requestMatchers(HttpMethod.POST,"/api/v1/vginfo/**" ).hasRole(Role.USER.name())
                .requestMatchers("/admin/manualcron","/api/v1/posts/**", "/admin/board/posts/update/**", "/admin/board/posts/save/**").hasRole(Role.USER.name())
                .requestMatchers("/vg/**").permitAll()
                .anyRequest().authenticated()
        );

        return http.build();
    }
}
