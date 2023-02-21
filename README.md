# Booque-ver2
최종 프로젝트

### 책판매 사이트 부끄의 두번째 버전 
#### 지도api, 사진 올리기, 찜, 검색, 알림 설정, 댓글 알림 그리고 채팅기능을 추가함.
#### 기존 책 상세페이지에 중고 판매글로 작성된 글의 수, 중고 판매글에 판매자의 블로그로 연결 등 기존 사이트와 연동시킴.



- 기간 : 1/13 ~ 2/16일
- 팀원 6명 
- 채팅(3명), 부끄장터(3명)
- 기술 스택 : Java 17, Spring Boot, Gradle, WebSocket, Oracle, JavaScript, JPA

---
## 부끄 장터
![부끄장터 메인](https://cdn.discordapp.com/attachments/1047044304859828280/1075707786882723861/main1.JPG)

1. 사용자가 직접 책 판매 글을 올릴수 있음
2. 판매글 임시저장
3. 원하는 판매글을 하트로 찜할 수 있음.
4. 구매가능, 지역, 제목을 조건으로 검색할 수 있음.
5. 검색시 원하는 책의 글 등록 알람을 받을 수 있음.



---
## 부끄 채팅 
![부끄 채팅](https://cdn.discordapp.com/attachments/1053972713900871730/1077612989282258974/KakaoTalk_20230216_181935835_02.jpg)

1. 중고 판매글의 채팅하기 버튼을 통해서 글번호, 채팅을 건 유저 번호, 판매자 번호 총 3가지 데이터가 Chat DB에 ajax로 비동기 방식으로 생성
2. 생성된 Chat DB의 Pk를 chatRoomId를 return 받아 chat?chatRoomId=1 식으로 채팅방 생성 
3. 전체 채팅 리스트에서 onclick 방식으로 (또는 <a> 방식) chatRoomId를 통해 데이터 받음
4. SockJS,Stom 을 통해 발송한 메시지를 txt파일에 내용/작성자/시간 3가지를 저장함. 
5. Javascript의 input.value.toUpperCase(); 통해 전체 채팅창에서 검색 기능.
6. 판매자일 경우 판매, 예약, 판매완료 를 선택 할 수 있는 버튼 생성. 비동기 방식으로 바로 데이터가 변경됨.


---
부끄장터의 경우 지도api와 사진 여러장 저장을 제외하고 기본 CRUD 방식을 사용함.

부끄채팅의 경우 WebSocket 연결을 위한 WebSocketMessageBrokerConfigurer 인터페이스를 상속함(implements)

  
  
  
 ---
 ## REST API & Ajax 방식이란?
#### Ajax(Asysnchronous JavaScript And XML)
  - 요청과 응답이 비동기적으로 이루어지니는 통신 방식
  - 클라이언트는 서버에게 요청을 보내고, 응답을 기다리지 않고 다른 작업을 처리 후에 응답을 받았을 때 그에 대한 처리를 함. 
  #### REST(Representational State Transfer)
  - HTTP URL과 방식(post, get 등 )을 통해서 서버로부터 데이터를 전송 받을 수 있는 방식
  
  ### Ajax를 이용하여 REST API를 호출.
  
  HttpEntity, ResponsEntity , RepuestEntity 등의 클래스를 이용함. 
  HTTP는 여전히 뭔지 모르겠음.
  
  
  
  
 ## WebSocket과 SockJS, Stom은 무엇일까?
  
  socket은 양방향 통신/소통을 할때 음성/텍스트/이미지 등의 데이터가 전송되는 도착지점임. 
  예를 을어 포트 넘버나 URL같은 경로가 socket이 되어 TCP(Transmission Control Protocol)가 전달하는 데이터를 알맞게 인지하도록 해줌..?
  
  
  
  
  ---
  
  #### 아쉬웠던 점
  - 기존 계획이었던 서버배포와 Python을 이용한 책 추천 알고리즘 기능을 아예 구현하지 못함. 
      - 시간적 여유가 있었음에도 제대로 구상, 계획조차 세우지 않은 것이 아쉬움. 
  #### 배운 점
-  반복된 오류는 팀원들에게 전달해서 동일한 오류가 발생시 빠르게 해결할 수 있음.
  - DB의 Fk가 없으면 데이터 문제로 오류가 발생 했을 때 한번에 파악하기 어려웠음.
 - 코드의 재사용율을 높이기 위해서는 서로의 코드를 정확하게 알고 있어야 함. 하지만 어려움.
  - ajax와 File 객체에 대해 이해 할 수 있었음.?
  



