### 데이터 크롤링 리팩토링
#### 전체 작업 플로우
* 모든 크롤링 작업은 매 시간 5분마다 자동으로 cron을 통해 수행되고, 수동으로 수행할 수도 있음
1. 최신의 vg 데이터를 조회해서 개최 시간과 number 정보를 가져옴
2. 현재 시간이 최신 vg 데이터의 개최 시간인지 확인
   * 개최 시간이 아니라면, 아무 작업도 하지 않고 종료함
3. 개최 시간이라면, vg 데이터를 가지고 jsoup를 통해 저장할 데이터를 파싱함
   * 모든 파싱 과정에서, 가져온 데이터 로그를 board에 저장함 
4. 가져온 데이터가 이미 DB에 저장되어있는지 확인
5. DB에 저장되어 있지 않다면, DB에 저장함

#### 변경시 체크할 점
* 작업을 최소 단위로 나누어서 역할을 분배함
  1. 웹을 크롤링하여 parsing하는 역할
  2. DB에 저장하는 역할
  3. 크롤링 로그를 저장하는 역할
  4. 전체 작업을 묶는 facade 레이어
* 크롤링 로그는 작업의 성공 여부와 관계없이 크롤링이 성공하면 항상 저장되므로, AOP나 이벤트 리스너를 사용하면 좋을지도
* vg 데이터의 유효성 여부 체크는 (개최 시간 체크) vg 객체에 위임하자

#### vg 객체 유효성 체크 시 포인트
* 라운드 시간차 0 (16~17)에는 데이터가 없음
* 1~2라운드 시간차 45~47(1시부터 3시 59분까지)이라면 데이터를 조회할 수 없음
* 3라운드 시간차 45에 결과가 출력됨

### Facade 레이어의 플로우 
1. 최신의 vg 객체 조회
   * VG 객체의 유효성을 판단해, 유효하지 않다면 exception throw
2. jsoup를 사용해 데이터 크롤링 후 파싱
   * 파싱 과정에서 exception이 발생했다면, throw (board에 throw된 exception 저장)
3. 파싱한 데이터가 이미 DB에 저장되어 있는지 확인
   * 저장되어 있다면, exception throw
4. 저장
5. 트랜잭션 종료
