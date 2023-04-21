# API Server

## Tech

- Backend: SpringBoot 3 , Jpa without Querydsl ,JUnit 5, Mockito
- Data: Redis, Kafka, ElasticSearch

---

## How to build and run

Before you run this project, you should run mariadb named tiny_link

- `./gradlew clean build` && `java -jar build/libs/*.jar`

---

## Edge Case

- Invalid URL parameter
    - Invalid URL Format
    - URL Empty
- `ShortId` collision
- `ShortId` lack
- non-existing `ShortId`

---

## Entry Point

| API                           | Description                                                                                | Detail Info                                                                    |
|-------------------------------|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| `POST /short-links`           | API to generate Short Link with random ID when URL is entered                              | [/short-links](http://localhost:8080/docs/post-short-links.html)               |
| `GET /short-links/{short_id}` | API for querying 1 Short Link                                                              | [/short-links/{short_id}](http://localhost:8080/docs/get-short-links.html)     |
| `GET /r/{short_id}`           | API that redirects to the URL that was originally entered when accessed through Short Link | [/r/{short_id}](http://localhost:8080/docs/redirect-short-links.html)          |
| `GET /statistics/{short_id}`  | API that provides weekly request statistics for Short Link                                 | [/statistics/{short_id}](http://localhost:8080/docs/redirect-short-links.html) |

### Error code

| Http Status | ErrorCode | ErrorMsg              | Case/Description        |
|-------------|-----------|-----------------------|-------------------------|
| 400         | `400000`  | NOT_VALID_ARGUMENT    | 잘못된 요청 파라미터로 요청         |
| 400         | `400010`  | NOT_EXIST_SHORT_ID    | 존재하지 않는 ShortId로 요청     |
| 400         | `400020`  | INVALID_URL_FORMAT    | 올바르지 않은 URL 요청 파라미터로 요청 |
| 400         | `404000`  | NOT_FOUND_RESOURCE    | 존재하지 않는 리소스 요청          |
| 400         | `405000`  | NOT_SUPPORTED_METHOD  | 잘못된 요청 메서드              |
| 500         | `500000`  | INTERNAL_SERVER_ERROR | 서버 내부 오류                |
| 500         | `500001`  | EXHAUSTED_SHORT_LINK  | ShortId의 고갈             |


