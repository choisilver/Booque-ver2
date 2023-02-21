# Booque-ver2
최종 프로젝트

책판매 사이트 부끄의 두번째 버전 
지도api, 사진 올리기, 찜, 검색, 알림 설정, 댓글 알림 그리고 채팅기능을 추가함.
기존 책 상세페이지에 중고 판매글로 작성된 글의 수, 중고 판매글에 판매자의 블로그로 연결 등 기존 사이트와 연동시킴.



- 기간 : 1/13 ~ 2/16일
- 팀원 6명 
- 채팅(3명), 부끄장터(3명)
- 기술 스택 : Java 17, Spring Boot, Gradle, WebSocket, Oracle, JavaScript, JPA

---
## 부끄 장터
1. 사용자가 직접 책 판매 글을 올릴수 있음
2. 판매글 임시저장
3. 원하는 판매글을 하트로 찜할 수 있음.
4. 구매가능, 지역, 제목을 조건으로 검색할 수 있음.
5. 검색시 원하는 책의 글 등록 알람을 받을 수 있음.


---
## 부끄 채팅 
1. 중고 판매글의 채팅하기 버튼을 통해서 글번호, 채팅을 건 유저 번호, 판매자 번호 총 3가지 데이터가 Chat DB에 ajax로 비동기 방식으로 생성
2. 생성된 Chat DB의 Pk를 chatRoomId를 return 받아 chat?chatRoomId=1 식으로 채팅방 생성 
3. 전체 채팅 리스트에서 onclick 방식으로 (또는 <a> 방식) chatRoomId를 통해 데이터 받음
4. SockJS,Stom 을 통해 발송한 메시지를 txt파일에 내용/작성자/시간 3가지를 저장함. 
5. Javascript의 input.value.toUpperCase(); 통해 전체 채팅창에서 검색 기능.
6. 판매자일 경우 판매, 예약, 판매완료 를 선택 할 수 있는 버튼 생성. 비동기 방식으로 바로 데이터가 변경됨.

---
부끄장터의 경우 지도api와 사진 여러장 저장을 제외하고 기본 CRUD 방식을 사용함.
많은 기능들을 Ajax를 이용한 비동기 방식을 선택함.
부끄채팅의 경우 WebSocket 연결을 위한 WebSocketMessageBrokerConfigurer 인터페이스를 상속함(implements)
  
 ---
 ## Ajax 방식이란?
  가장 많이 사용한 ajax 방식은 무엇일까?
  
  
 ## WebSocket과 SockJS, Stom은 무엇일까?



