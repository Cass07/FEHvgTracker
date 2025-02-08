### 하위 객체에서 에러가 발생했을 때, 호출한 위치에 따라 다르게 핸들링하기?
* 기존에 Null값을 전달받고 이를 받으면 그냥 return;을 호출하는 방식에서, Exception을 사용하여 리팩토링하려고 하니까 컨트롤러별로 다르게 핸들링해야 할 필요성이 생김
  * 이는 그냥 return;으로 끝내버리면 테스트 코드 작성 시 애로사항이 있었기 때문
    * 예를 들어서 특정 구문에서 실패했을 때 그냥 return;으로 빠져나와버리면, 정확히 어디까지 실행되고 어디에서 종료되었는지 JUnit으로는 잡아내기가 어려웠다
    * verify()를 통해서 모킹 중인 객체의 메소드의 호출 여부를 확인할 수도 있는데, 여러 if문 사이에 모킹 객체 메소드가 없으면 이도 부정확함
* 현재 프로젝트의 경우 restController와 viewController가 모두 존재한다
  * restController에서는 exception발생 시 rest api형태로 에러를 반환하고
  * viewController에서는 exception발생 시 front 화면에 에러 메시지를 띄우고 싶다 (view를 호출해야 한다는 소리)

#### 대충 구조가?
ex: vgInfo가 조회되지 않는 경우

* Repository
  * Service - Exception 발생 1
    * Facade - Exception 발생 2
      * Controller
        * RestController
        * ViewController

#### 현재 에러를 핸들링하는 방법?
* ViewException과 BusinessException 두 가지를 에러 객체를 만들어서, 이를 상속받아서 사용ㅈㅇ
  * ViewException 계열은 View에서 사용하는 Facade에서 발생한 에러이고, viewExceptionHandler를 통해서 에러를 표현하는 뷰를 리턴한다
  * BusinessException 계열은 비즈니스 로직 전반에 사용되며, 아직 핸들링 안함

#### 생각?
* viewFacade에서만 발생하는 에러를 따로 핸들링하고 있으니까? viewController와 restController의 Facade를 구분해셔 사용하면 되는 것 같은데?
* 현재 Facade 레이어를 신규로 도입해서, 일부 controller에서는 Service 레이어를 바로 호출하고 있어서, service 레이어에서 exception을 던질 필요가 있는 게 문제임
* 기존에 Service - Controller 구조로만 구성했을 때는, 여러 컨트롤러가 하나의 Service 레이어의 로직을 나눠써서 controller에 따라 exception을 핸들링하려면 아마 controller에서 처리해야 했을 듯?
* Facade 레이어의 사용의 이점 중 하나가 아닌가 싶다
  * Service 레이어와 Controller 레이어의 결합?을 낮추기

#### 수정방법
* Facade 레이어를 완전히 도입하여, Controller에서 Facade 객체만 호출하도록 한다
* RestController/ViewController 각각에서 호출하는 Facade가 혼용되지 않도록 한다
* BusinessException 의 핸들러를 추가하여 RestController로 그 결과를 리턴하도록 한다
* Excepion 패키지 구조를 변경해서 두 exception이 구분되도록 한다

### 근데 그러면 jpa 조회 시 Optional 은 어떻게 처리하면 좋을까?
#### 우선 예전 설계
* ex. vgInfo를 조회해서 존재 여부에 따라 추가 로직을 수행함
* jpaRepositoty -> Optional.of(VgInfo)를 리턴
* queryDsl -> 존재하지 않으면 null을 리턴
* Service -> Optional.orElse(null)로 처리
  * Optional.orElse(null)은 별로 좋은 방법이 아닌 것 같음..
  * 그러나 ifPresentOrElse()를 사용하려면, service layer에서 exception이 나오는데, 위에서 말한 문제로 facade에서 나와야함
  * 또한, optional이 아닌 null값을 반환해줄 경우, 클라이언트에서 매번 null인지 비교하고 throw하는 로직을 추가해줘야함 (못생김)
  * service에서 Optional<>로 그대로 리턴하고, facade에서 처리하는 것이 좋나?
    * 이 경우 jpaRepository와 queryDsl의 처리방식이 다른데, 통일하는 것이 좋지 않나?
    * queryDsl에서의 결과값에 Optional.ofNullable() 추가?


```java
//null로 리턴해줄 때
VgInfo vgInfo = vgInfoRepository.findByVgId(vgId);
if (vgInfo == null) {
    throw new IllegalArgumentException("vgInfo가 null임~");
}

// optional로 리턴해준다면
VgInfo vgInfo = vgInfoRepository.findByVgId(vgId).orElseThrow(() -> new IllegalArgumentException("vgInfo가 null임~"));
```

#### 수정
* Optional<>로 리턴시키고, facade에서 처리한다
* 테스트 클래스도 수정한다


### 하나의 Exception 클래스에 대해서 컨트롤러별로 다른 Handling이 필요한 경우

#### ExceptionHandler produces?
* https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html#mvc-ann-exceptionhandler-media
* Accept request header에 따라서 Response를 세분화할 수 있는 기능이라고함 
* 그런데 일단 Accept request header가 front와 api를 호출할 때 명확하게 달라지지 않는 경우가 있는듯함 (특히 get api에서)
  * 전역 설정인 것이 문제인가? produces 값에 따른 작동 방식을 찾아봐야 할듯
* 또한 6.1.16에서는 사용불가하고 Spring 6.2.2로 올려야함

#### Controller 자체에서 핸들러 추가
* 이렇게 하면 확실하게 컨트롤러 별로 별개의 에러 헨들링을 할 수 있음
* 단, 전역으로 설정이 불가능하고 controller마다 매 번 추가해줘야할 필요가 있음
  * ViewController와 RestController에 예외처리 메소드를 작성하고 final처리한 뒤에 이를 상속해서 개별 컨트롤러 객체를 만들기??
  * 혹은 Annotation을 사용해서 컴파일 시 메소드를 추가해 주기??

```java
@Controller
public class testController {
    
  @ExceptionHandler(value = {BusinessException.class})
  public String handleViewException(BusinessException e, Model model) {
    model.addAttribute("error", e.getMessage());
    return "error";
  }
}

@RestController
public class testRestController {
    
  @ExceptionHandler(value = {BusinessException.class})
  public ResponseEntity<String> handleApiException(BusinessException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
```