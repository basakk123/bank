# Bank 애플리케이션

### 프로젝트 팁
- 테이블 설계는 처음부터 완벽할 수 없다.
- 처음에는 가장 쉬운 테이블 구조를 만들기
- 그리고 수정 보완 하는 것!

### 진행순서
- 도메인 생성
- ExceptionHandling
- 시큐리티 설정
- logger 세팅
- 회원가입
- 로그인
- DTO Validation AOP
- JWT 로그인으로 변경
- 서비스 기능 만들기

### 기능 정리
- 회원가입
- 로그인
- 계좌 등록
- 본인 계좌 목록 보기 ( Account, User )
- 본인 계좌 상세 보기
- 본인 계좌 삭제 ( update )
- 입금하기
- 출금하기
- 이체하기
- 입출금 목록 보기
- 입금 목록 보기
- 출금 목록 보기


### 이슈
- JPQL (join fetch, left join)
- 조인할때 on은 조인하기전 테이블 필터링
- 조인할때 where은 조인후 결과를 필터링
- Junit 테스트시에 양방향 매핑을 하게 되면 순수객체시점에 문제가 있을때 em.clear() 하기
```txt
em.clear() 영속성 컨텍스트 초기화 (사용하는 이유는 캐시안하고 DB에서 조회하기 위해)
em.flush() DB에 반영
persist()를 하고 em.find()를 바로 호출하면, 조회 쿼리를 볼 수 없음.
영속성 컨텍스트에 있는 데이터를 가지고 오기 때문인데, Junit에서는 DB에서 다시 조회할 필요가 있을때, 양방향 매핑에 있는 데이터는 동기화가 안되있어서
em.clear()를 하면 영속성 컨텍스트가 지워지고 em.find()를 호출하면 영속성 컨텍스트에 없으니 db에서 조회하고, 조회 쿼리를 볼 수 있음.
```