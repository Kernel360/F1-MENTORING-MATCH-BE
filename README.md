# F2-BIENGUAL-BE
영어 학습 플랫폼 Biengual [웹사이트](https://biengual.store)

## 프로젝트 개요
- **개발자 입장에서 영어 학습을 하는데 불편함을 느꼈던 것들**
    - 챌린저스 앱으로 영어 학습을 시작해보려고 함 → 인증용으로만 쓰다보니 영어 학습도 한 플랫폼에서 같이 가능했다면 좋았겠다 싶음
    - 외국계 수요가 높아지는 요즘, 나 또한 …
        - IT 외국계 기업으로 취업하고 싶음 → 그럼 IT 관련 아티클(기사 등)을 많이 접해보고, 읽어보고
        - 그쪽 취준하는 사람들이랑 같이 으쌰으쌰 하면서 스피킹 연습 하고 싶은데 그럴 기회가 너무 적음
    - 외국어 학습(유튜브 등 기반) 플랫폼은 존재하나 한국인 기준으로 만들어진 게 아니라 좀 불편..
- [기획](https://www.notion.so/5df05f4700e942889551999ee587dcd4?pvs=21)
  
## 프로젝트 소개

- 구현 기능
    - 소셜 로그인
        - Kakao
        - Naver
        - Google
    - 크롤링
        - Youtube API - Youtube 정보
        - Selenium - Youtube 자막 동적 크롤링
        - JSoup - CNN 기사 크롤링
    - 카테고리
        - 크롤링 해 온 카테고리가 없으면 엔티티 추가하는 방식으로 생성
    - 번역
        - Microsoft Azure AI API
    - 북마크, 스크랩
        - 기본적인 CRUD
    - Swagger
    - 배포 : Docker, Github Action
    - 로그 : Kibana
    - 컨텐츠 메인 화면: 인기있는 콘텐츠를 리스닝과 리딩 각각 8개씩 볼 수 있습니다.
    - 컨텐츠 프리뷰 화면: 카테고리를 선택할 수 있고, 해당 조건의 컨텐츠를 10개씩 볼 수 있습니다.
    - 컨텐츠 디테일 화면(리스닝): 선택한 컨텐츠에 대한 유튜브 영상을 재생할 수 있고, 해당 영상에 대한 En, Ko 스크립트를 볼 수 있습니다.
    - 컨텐츠 디테일 화면(리딩): 선택한 컨텐츠에 대한 CNN 기사를 볼 수 있고, 해당 기사에 대한 En, Ko 스크립트를 볼 수 있습니다.
    - 검색: 키워드에 해당하는 컨텐츠를 검색할 수 있습니다.
    - 스크랩 화면: 스크랩한 컨텐츠, 북마크, 메모한 내용을 볼 수 있습니다.
    - 마이페이지: 자신의 정보를 볼 수 있습니다.

## ERD
- [ERD](https://dbdiagram.io/d/Mentoring-66debf32bc6a4b5bb5a1afe5)
    - MySQL : 전반적인 데이터 저장
    - Mongo : 스크립트 및 문제 관련 내용 저장

## 기술 스택
 - Java
 - Spring Boot
 - Spring Security
 - JWT
 - Cookie
 - JPA
 - QueryDsl
 - MySQL
 - MongoDB
 - Swagger
 - OAuth2
 - Jsoup
 - Selenium
 - Rest API
 - AOP
 - ELK
 - EC2
 - GitHub Actions
 - Docker
 - Nginx
