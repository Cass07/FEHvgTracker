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

### localDateTime.now()를 사용하는 메소드의 테스트
* 실행할 때마다 now()의 기댓값이 다르기 때문에 똑같은 환경에서 테스트해도 시간에 따라 테스트가 실패함
* 모킹할 때 now()값을 베이스로 모킹해도, 시작 시간과 실제 호출 시간에 차이가 있기 때문에 실패할 여지가 있음

#### LocalDateTime.now를 모킹하면 되지 않나?
1. LocalDateTime.now()를 static mocking하라고 함
   * 다만 Mockito는 static 메소드를 모킹할 수 없음
   * 또한 Static mocking은 추천되는 방법이 아닌 듯 함
2. 그냥 패러미터로 LocalDateTime을 넘김
   * 단 호출자가 시간을 생성해서 넘겨줘야 하기 때문에, 결국 다른 객체에서 now()를 호출해야 됨
3. LocalDateTime.now(Clock)을 사용하고, Clock을 모킹해라!
   * Oracle에서도 이 방법을 사용한다고함
   * 후술해서 정리

#### Clock을 사용한 LocalDateTime.now() 테스트
* LocalDateTime.now() 자체에서도 Clock을 사용하여 시간을 가져온다

```java
public static LocalDateTime now() {
  return now(Clock.systemDefaultZone());
}

public static LocalDateTime now(Clock clock) {
  Objects.requireNonNull(clock, "clock");
  final Instant now = clock.instant();  // called once
  ZoneOffset offset = clock.getZone().getRules().getOffset(now);
  return ofEpochSecond(now.getEpochSecond(), now.getNano(), offset);
}
```

* 먼저 Clock을 빈으로 등록하자
```java
@Configuration
public class ClockConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
```

* 그리고 메소드에서 Clock 빈을 주입받고, LocalDateTime.now(clock)을 사용하자
```java
public class VgDataFacade {
    private final Clock clock;
    
    public void updateVgData() {
        LocalDateTime now = LocalDateTime.now(clock);
        
    }
}
```

* 테스트 코드에서 Clock을 모킹하자
```java
class Test {
  @InjectMocks
  private VgDataFacade vgDataFacade;
  
  @Mock
  private Clock clock;
  
  private static final LocalDateTime NOW = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
  
  @Test
  void test_fixedClock() {
      Clock fixedClock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
      doReturn(fixedClock.instant()).when(clock).instant();
      doReturn(fixedClock.getZone()).when(clock).getZone();
      
      vgDataFacade.updateVgData();
  }
}
```

#### Github action에서 실패하는데??
* 오류 로그를 보니 timezone 문제임
  * 로컬환경은 asia/seoul이고, Github action은 UTC임
* LocalDate.startDate랑 localDateTime을 같이 썼기에 발생하는 문제다
* 테스트 코드에서 타임존을 명확하게 지정하거나, 타임존 변경에 따라 테스트의 결과가 변경되지 않도록 수정하자
* 또는 github Action에서의 타임존을 배포 환경과 동일하게 지정

