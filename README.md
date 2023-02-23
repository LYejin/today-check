<h1> 📎todayCheck ( 2022.11 ~ 2023.1 ) </h1> 

> 일일 미션을 만들어 이를 인증하고 소통하는 웹사이트

![미션 디테일](https://user-images.githubusercontent.com/49367338/214349734-0f42f28f-3bf5-4e6c-bd80-93c8ff2fd375.png)

<h3>◼ 주요 기능</h3>
프로젝트 주요 기능은 다음과 같습니다.<br/><br/>

- 게시판 : 작성 기능
- 댓글 : 댓글 작성 기능
- 사용자 : Security 회원가입 및 로그인 ( JWT ) , OAuth 2.0 (구글, 네이버) , 회원정보 수정, 회원가입시 중복 검사
- 미션 : 미션 생성 및 참여와 일일 출석 인증 기능
- 보안 : RefreshToken는 클라이언트의 접근 제한 , AccessToken 과 RefreshToken 를 발급해 API 요청에 대한 권한 확인

<h2>Project Structure</h2>

> React ( SPA ) + Spring ( API ) 구조로 개발했습니다.

◼ Front-End ( 김완영 , 이예진 )
- React ( SPA )
- Styled-componets ( Css )
- React-router ( Router )
- Meterial UI ( MUI )

◼ Back-End ( 박상진 , 박철진 )
- Spring Boot ( API Server )
- Spring Security ( Security )
- JUnit ( Test )
- MySQL ( RDBMS )
- JPA Hibernate & QueryDSL ( ORM )
- OAuth 2.0 & JWT ( Login )
- Docker ( Container )

◼ Tools
- Git ( GitHub )
- Swagger ( Open API Specification )

<h2>Entity Relationship Diagram</h2>

![DB](https://user-images.githubusercontent.com/49367338/214645020-a7c0f3cb-66ed-46b3-9e24-e731c18e9eab.png)


<h2>1 . 로그인 / 회원가입</h2>

> 구글 , 네이버 계정으로 회원가입을 하거나 , 별도로 가입할 수 있습니다.

![로그인](https://user-images.githubusercontent.com/49367338/214352661-79cbaec3-7512-4ccf-8db0-5a87e7596b65.png)
- Oauth 2.0 를 통해 불필요한 회원가입을 단축시켰습니다.
- 로그인 후 발급되는 Access Token 과 Refresh Token은 클라이언트에서 다음과 같이 보관합니다. 
  - AccessToken은 로컬 변수에 저장해서 외부 접근을 제한합니다.
  - RefreshToken은 쿠키에 보관하나 Security , HTTPOnly 옵션을 추가해서 스크립트로 쿠키에 접근할 수 없게 차단했습니다.
  - 로그아웃시 Refresh Token 쿠키 삭제 및 세션 무효화를 합니다.

<h2>2 . 메인화면 ( 미션 리스트 )</h2>

> 현재 생성된 미션과 참여자 수를 출력합니다.

![미션 리스트](https://user-images.githubusercontent.com/49367338/214353026-267a5a7d-5664-46b4-bb83-419e881a7eba.png)

- QueryDSL로 미션과 , 미션 생성자 , 참여자 테이블을 fetchJoin을 이용해 한번에 참조해 DB 서버와 통신을 최소화 했습니다. 

<h2>3 . 메인화면 ( 포스트 리스트 )</h2>

> 작성된 모든 포스트들을 불러 들어옵니다.

![포스트 리스트](https://user-images.githubusercontent.com/49367338/214354822-118db7c6-a789-4e60-a663-b176e5f10816.png)

<h2>4 . 미션 생성 화면 </h2>

> 미션을 수행할 기간과 이미지 , 정보를 설정할 수 있습니다.

![미션 생성](https://user-images.githubusercontent.com/49367338/214359529-75fa1dda-1aa7-4844-aea5-7271beea25e9.png)

<h2>5 . 미션 리스트 상세 화면 </h2>

> 미션에 참여중인 사용자와 출석 인증된 데이터를 보여줍니다.

![미션 디테일](https://user-images.githubusercontent.com/49367338/214355195-c6875883-c6b9-4e39-915d-51b5f7cdf93a.png)

- leftJoin과 InnerJoin , FetchJoin을 이용해 단 한번의 DB 통신으로 연관된 데이터를 모두 불러드립니다.

<h2>6 . 포스트 작성 화면</h2>

> 포스트에 들어갈 이미지와 데이터를 입력할 수 있습니다.

![글 작성](https://user-images.githubusercontent.com/49367338/214360042-15a89354-65fd-4592-8652-318d06d7806c.png)

<h2>7 . 포스트 리스트 상세 화면 </h2>

> 포스트 정보와 댓글을 작성한 사용자에 대한 정보를 출력합니다.

![포스트 디테일](https://user-images.githubusercontent.com/49367338/214356917-b4ecd654-50f5-40a6-9aa5-27854e2ab564.png)

- Base64로 인코딩된 이미지 데이터와 댓글 데이터를 함께 Json 형식의 데이터에 담아 전송했습니다.

<h2>8 . 회원 정보 </h2>

> 해당 사용자 정보와 자신이 속한 미션과 생성한 데이터를 보여줍니다.

![회원 정보](https://user-images.githubusercontent.com/49367338/214358079-06d5ae08-f913-42d0-8675-0b26e6be6cdc.png)

- 회원 아이디 정보가 수정되었을 때 배치 쿼리를 통해 포스트와 미션 등 모든 아이디를 수정합니다.

<h2>9 . 미션 인증 </h2>

> 본인이 속한 미션에 이미지를 업로드해 미션을 인증할 수 있습니다.

![미션 인증](https://user-images.githubusercontent.com/49367338/214358773-f596d76b-87c3-49f4-8dc3-d38e1d5fd238.png)

- 인증된 이미지는 해당 미션 DB에 저장이 되어 , 조회할 때 확인할 수 있습니다.
