// 'use strict';
// strict 모드 사용. 자바스크립트가 묵인했던 에러들의 에러 메시지 발생(엄격한 문법 검사)
// 로그인한 유저가 포함된 채팅방과 websocket 연결
// 유저 정보를 읽어서 유저가 들어가있는 채팅방을 알아낸다.
// 해당 채팅방들과 websocket 연결을 실시한다.

// 실제 채팅방에서 표시되는 내용은 DB내용을 읽어서 표시해야함.
// 채팅방 내부에 있을 때만 DB를 거치면 속도가 약간 느려짐.

// 색깔코드 배열


// WebSocket은 웹 상에서 쉽게 소켓통신을 하게 해주는 라이브러리
// 스프링에서는 SockJS 라이브러리와 STOMP 프로토콜을 사용 가능
// SockJS는 WebSocket 기능 보완, 향상

// STOMP는 메시지 전송을 효율적으로 지원
// 일반 WebSocket 환경은 핸들러만 구현해주고 직접 호출함
// STOMP 이용시 핸들러와 브로커라는 개념으로 서로간 통신함

// STOMP:Simple/Streaming Text Oriented Messaging Protocol의 약자, 텍스트 기반 메시징 프로토콜
// 구독이라는 개념을 통해 내가 통신하고자 하는 주체(topic)를 판단하여 실시간, 지속적으로 관심을 가진다.
// MesssageHandler에서 해당 요청시 처리 과정을 구현





