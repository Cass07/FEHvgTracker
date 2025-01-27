#### 웹 컨트롤러의 junit 테스트?
rest controller는 해봤는데 juit은??

일단 테스트 해야하는 항목들
* 원하는 jsp 페이지를 호출하는지?
* 입력한 값에 맞는 결과를 반환하는지??

#### 단위 테스트에서의 Spring Security?
* 기존처럼 controller의 단위 테스트를 처리하면, Controller 클래스만 로딩해서 진행하기 때문에, Spring Security가 적용된 경우에는 SecurityContext를 로딩하지 않았음
* 결국 Spring Security 자체를 테스트하려면 이 방법들뿐인 것 같음??
  * 전체 빈을 로드해서 테스트 : 너무 헤비한데?
  * Spring Security에 필요한 모든 빈과 테스트 빈을 불러와서 테스트
    * Oauth2Service 종속성이 발목을 잡는다!! -> Oauth2Service를 Mocking해서 처리해야함
    * 

#### You cannot define roles attribute [*] with authorities attribute [*]
* Spring Security의 테스트 클래스를 작성하는 도중 발생한 오류
* MockUser의 role과 authority를 동시에 설정했을 때 발생햇음

```java
final class WithMockUserSecurityContextFactory implements
        WithSecurityContextFactory<WithMockUser> {

    public SecurityContext createSecurityContext(WithMockUser withUser) {
        String username = StringUtils.hasLength(withUser.username()) ? withUser
                .username() : withUser.value();
        if (username == null) {
            throw new IllegalArgumentException(withUser
                    + " cannot have null username on both username and value properites");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : withUser.authorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        if (grantedAuthorities.isEmpty()) {
            for (String role : withUser.roles()) {
                if (role.startsWith("ROLE_")) {
                    throw new IllegalArgumentException("roles cannot start with ROLE_ Got "
                            + role);
                }
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        } else if (!(withUser.roles().length == 1 && "USER".equals(withUser.roles()[0]))) {
            throw new IllegalStateException("You cannot define roles attribute "+ Arrays.asList(withUser.roles())+" with authorities attribute "+ Arrays.asList(withUser.authorities()));
        }

        User principal = new User(username, withUser.password(), true, true, true, true,
                grantedAuthorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
```
* GrantedAuthority가 비어있지 않으면서, (roles가 단 한개만 있고 그 값이 "USER")가 아닐 경우
  * 즉 Authority 값은 role이 ["USER"]인 경우에만 설정할 수 있다고 한다
* 또한 role의 이름은 "ROLE_" 접미사를 자동으로 붙여서 Authorities에 넣기 때문에, "ROLE_"로 시작하면 안된다

#### 비동기 이벤트의 단위 테스트?
* Event publisher를 사용해 로그를 테이블에 저장하는 로직을 생성함