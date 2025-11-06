MaplibreTest - README

이 프로젝트는 Spring Boot + MyBatis + Lombok + Thymeleaf 기반의 웹 애플리케이션으로, 여러 MapLibre 지도를 제공하고 지도에서 위치 기반 메모(노트)를 추가/수정/삭제하여 MySQL에 저장할 수 있습니다.

주요 변경 사항
- `rasterMap.html`에 `map.html`과 동일한 노트(메모) 기능 추가
  - 지도 클릭으로 입력 팝업(경도/위도 표시, 텍스트 입력, 저장/취소)
  - 저장 시 `/api/notes`로 POST하여 DB에 저장, 팝업이 읽기 전용으로 전환
  - 기존 노트는 `/api/notes` GET으로 로드하여 팝업으로 표시
  - 노트 수정 지원: 읽기 팝업에서 "수정" 버튼으로 편집 팝업을 열어 PUT으로 업데이트
  - 노트 삭제 지원: 읽기 팝업의 "삭제" 버튼으로 DELETE /api/notes/{id} 호출하여 삭제
  - 10초 주기 폴링(poll)으로 새 노트가 있으면 자동으로 표시

사전 준비
- Java 17 JDK
- Gradle wrapper (`gradlew.bat`) — 프로젝트에 포함됨
- MySQL 서버 (로컬 또는 접근 가능한 DB 서버)
- (선택) curl 등 HTTP 테스트 도구

데이터베이스 설정 예시
- DB 이름: `maplibretest`
- DB 사용자: `maptest`
- DB 비밀번호: `maptest`

`src/main/resources/application.yml` 예시:
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/maplibretest?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: maptest
    password: maptest
```

MySQL에서 DB/계정 생성(예):
```sql
CREATE DATABASE IF NOT EXISTS maplibretest CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'maptest'@'localhost' IDENTIFIED BY 'maptest';
GRANT ALL PRIVILEGES ON maplibretest.* TO 'maptest'@'localhost';
FLUSH PRIVILEGES;
```
테이블 스키마는 `create_table_notes.sql`을 참고하세요.

서버 실행 방법
- 프로젝트 루트에서 (Windows cmd):
```cmd
cd /d "C:\Users\user\Documents\workspace\MaplibreTest"
gradlew.bat bootRun
```
- 기본 포트는 8080입니다. 포트 충돌이 있을 경우 `--args="--server.port=8081"` 같은 방식으로 다른 포트로 실행할 수 있습니다.

주요 엔드포인트
- GET `/api/notes` : 모든 노트 조회 (클라이언트에서 로드/폴링에 사용)
- POST `/api/notes` : 새로운 노트 저장 (payload: { lng, lat, content })
- PUT `/api/notes` : 기존 노트 수정 (payload: { id, lng, lat, content })
- DELETE `/api/notes/{id}` : 기존 노트 삭제 (경로 변수로 id 전달)

rasterMap/map 동작(사용자 관점)
1. 브라우저에서 `/rasterMap` 또는 `/map` 접속
2. 지도 클릭 → 입력 팝업이 열림 → 메모 입력 → 저장(서버에 POST) → 읽기 팝업으로 전환
3. 저장된 노트는 다른 클라이언트가 페이지를 새로 고치거나 폴링에 의해 10초 이내로 표시
4. 읽기 팝업의 "수정" 버튼을 누르면 편집 팝업이 열리고 저장하면 PUT으로 업데이트
5. 읽기 팝업의 "삭제" 버튼을 누르면 확인창 이후 DELETE 요청이 호출되어 노트가 삭제되고 팝업이 제거됨

간단한 API 테스트 예시 (cmd)
- GET:
```cmd
curl http://localhost:8080/api/notes
```
- POST 예시:
```cmd
curl -H "Content-Type: application/json" -d "{\"lng\":127.0246,\"lat\":37.5326,\"content\":\"테스트 노트\"}" http://localhost:8080/api/notes
```
- PUT 예시 (id가 4인 노트 수정):
```cmd
curl -H "Content-Type: application/json" -X PUT -d "{\"id\":4,\"lng\":127.0246,\"lat\":37.5326,\"content\":\"수정된 내용\"}" http://localhost:8080/api/notes
```
- DELETE 예시 (id가 4인 노트 삭제):
```cmd
curl -X DELETE http://localhost:8080/api/notes/4
```
(포트가 8081로 실행한 경우 위에서 8080을 8081로 변경)

문제 해결(자주 발생하는 이슈)
- 지도가 보이지 않을 때
  - 브라우저 콘솔(F12) 확인: `maplibregl is not defined`, CSS/JS 404, 또는 자바스크립트 파싱 오류 등이 있는지 확인
  - CDN 차단(회사 네트워크 등)일 경우 MapLibre JS/CSS를 로컬로 내려받아 프로젝트의 정적 리소스로 서빙하도록 변경
- 서버가 포트 8080/8081에 실패할 경우: 포트가 이미 사용 중인지 확인하고 해당 프로세스를 중지하거나 다른 포트로 실행

추가로 할 수 있는 개선
- 실시간 동기화: 폴링 대신 WebSocket 또는 Server-Sent Events(SSE)로 노트 동기화
- 인증/권한: 사용자 계정과 노트 소유권 연동
- UI 개선: 삭제 시 로딩 표시, 삭제 취소(undo) 기능, 최대 문자열 길이 등

변경 완료: README를 갱신하여 삭제 API 및 UI 사용법을 추가했습니다. (git 관련 명령은 요청에 따라 수행하지 않았습니다.)